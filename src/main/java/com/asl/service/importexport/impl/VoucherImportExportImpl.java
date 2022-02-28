package com.asl.service.importexport.impl;

import static com.asl.enums.VoucherImportExportColumns.CHEQUE_NO;
import static com.asl.enums.VoucherImportExportColumns.LC_NO;
import static com.asl.enums.VoucherImportExportColumns.NOTE;
import static com.asl.enums.VoucherImportExportColumns.ROW_TYPE;
import static com.asl.enums.VoucherImportExportColumns.VOUCHER_CODE;
import static com.asl.enums.VoucherImportExportColumns.VOUCHER_DATE;
import static com.asl.enums.VoucherImportExportColumns.VOUCHER_REFERENCE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.asl.entity.Acheader;
import com.asl.enums.VoucherImportExportColumns;
import com.asl.model.AsyncCSVResult;
import com.asl.model.ImportExportPage;
import com.asl.model.ModuleOption;
import com.asl.service.AcService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jan 16, 2022
 */
@Slf4j
@Service("voucherimportexportService")
public class VoucherImportExportImpl extends AbstractImportExport {

	private static final String MODULE_NAME = "voucherimportexport";
	private static final String EXPORT_FILE_NAME = "voucher";

	@Autowired private AcService acService;

	@Override
	public String showExportImportPage(Model model) {
		log.debug("Load Voucher export/import module");
		model.addAttribute("module", getnerateExportImportPageData());
		return IMPORT_EXPORT_PATH;
	}

	private ImportExportPage getnerateExportImportPageData() {
		ImportExportPage eip = new ImportExportPage();
		eip.setModuleName(MODULE_NAME);
		eip.setPageTitle("Voucher Import/Export");
		eip.setModuleColumns(getModuleColumns(VoucherImportExportColumns.class));
		eip.setShowExportTab(true);
		eip.setExportTabPrompt("Export");
		eip.setShowImportTab(true);
		eip.setImportTabPrompt("Import");
		eip.setShowFileDelimiter(true);
		eip.setShowIgnoreFirstRow(true);
		eip.setShowUpdateExistingRecord(true);
		eip.setUpdateExistingRecordPrompt("Update existing record? :");
		eip.setShowNotes(true);
		List<String> notes = new ArrayList<>();
		notes.add("You must select \"update existing record?\" to update existing system record");
		eip.setNotes(notes);
		
		List<ModuleOption> downloadOptions = new ArrayList<>();
		ModuleOption listDwnloadOptn = new ModuleOption();
		listDwnloadOptn.setPromt("Download All");
		listDwnloadOptn.setName("downloadAllList");
		listDwnloadOptn.setId("downloadAllList");

		downloadOptions.add(listDwnloadOptn);
		eip.setDownloadOption(downloadOptions);

		return eip;
	}

	@Override
	public void downloadTemplate(AsyncCSVResult asyncCSVResult) throws IOException {
		HttpServletResponse response = asyncCSVResult.getHttpServletResponse();
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("text/csv;charset=UTF-8");
		String fileName = "voucher-template.csv";
		response.addHeader("content-disposition", "attachment; filename=\"" + fileName +"\"");

		// Print header
		out.println(getHeader());
		out.flush();

		response.flushBuffer();
		response.getOutputStream().close();
	}

	@Override
	public void retreiveData(AsyncCSVResult asyncCSVResult) throws IOException {
		List<ModuleOption> downloadOption = asyncCSVResult.getExportOptions();
		
		String fileType = "csv";
		String fileName = getFileName(EXPORT_FILE_NAME, fileType);
		if(downloadOption == null || downloadOption.isEmpty() || downloadOption.get(0) == null) return;
		
		String tempoFileName = getResult(downloadOption.get(0), fileName, asyncCSVResult);
		
		asyncCSVResult.setFileName(fileName);
		asyncCSVResult.setMimeType(new MediaType("text", fileType));
		asyncCSVResult.setDataFileName(tempoFileName);
	}

	private String getResult(ModuleOption op, String fileName, AsyncCSVResult asyncCSVResult) {
		String headerLines = getHeader() + LINE_BREAK;
		String tempPath = appConfig.getAppTempDir();
		String fileNameWithDirectory = tempPath + File.separator + fileName;

		try (FileWriter fw = new FileWriter(fileNameWithDirectory); 
			BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write(headerLines);
		} catch (IOException e) {
			log.error("Can not write file {}: {}", fileNameWithDirectory, e);
			return null;
		}

		List<String> cachedPrintedListHead = new ArrayList<>();  // Used to identify that the list head is already been download
		
		long totalSize = 0;
		long chunkLimit = CHUNK_LIMIT;
		long offset = 0;
		long dataSize = 1;
		
		while(dataSize > 0) {
			List<String[]> fileLineData = new ArrayList<>();
			
			List<Map<String, Object>> result = acService.getExportDataByChunk(chunkLimit, offset + 1, asyncCSVResult.getBusinessId());
			dataSize = result.size();

			offset = offset + dataSize;

			createFileLineDataFromResult(fileLineData, result, cachedPrintedListHead);

			StringBuilder dataLines = new StringBuilder();
			fileLineData.forEach(ln -> {
				if (ln != null) {
					dataLines.append(String.join(",", ln)).append(LINE_BREAK);
				}
			});

			try (FileWriter fw = new FileWriter(fileNameWithDirectory, true); 
					BufferedWriter bw = new BufferedWriter(fw)) {
				bw.append(dataLines.toString());
			} catch (IOException e) {
				log.error(ERROR, e.getMessage(), e);
			}

			totalSize = totalSize + dataSize;
			if(dataSize < chunkLimit) {
				break; 
			}
		}
		
		log.debug("Total size: {}", totalSize);
		return fileNameWithDirectory;
	}

	private void createFileLineDataFromResult(List<String[]> fileLineData, List<Map<String, Object>> result, List<String> cachedPrintedListHead) {
		for(Map<String, Object> row : result) {
			String xvoucher = (String) row.get("XVOUCHER");
			if(StringUtils.isBlank(xvoucher)) continue;

			if(!cachedPrintedListHead.contains(xvoucher)) {
				String[] dataLine = new String[7];
				dataLine[0] = "H";
				dataLine[1] = xvoucher;
				dataLine[2] = modifiedValue(SDF.format((Date) row.get("XDATE")));
				dataLine[3] = modifiedValue((String) row.get("XREF"));
				dataLine[4] = modifiedValue((String) row.get("XLCNO"));
				dataLine[5] = modifiedValue((String) row.get("XCHEQUENO"));
				dataLine[6] = modifiedValue((String) row.get("VOUCHERNOTE"));
				fileLineData.add(dataLine);
				if(!cachedPrintedListHead.isEmpty()) cachedPrintedListHead.remove(0);
				cachedPrintedListHead.add(xvoucher);
			}

			String[] dl = new String[7];
			dl[0] = "D";
			dl[1] = xvoucher;
			dl[2] = modifiedValue((String) row.get("XACC"));
			dl[3] = modifiedValue((String) row.get("XSUB"));
			dl[4] = modifiedValue((String) row.get("VOUCHERDETAILNOTE"));
			dl[5] = modifiedValue(((BigDecimal) row.get("XDEBIT")).toString());
			dl[6] = modifiedValue(((BigDecimal) row.get("XCREDIT")).toString());
			fileLineData.add(dl);
		}
	}

	@Override
	public void processCSV(AsyncCSVResult asyncCSVResult) {
		String fileLocation = asyncCSVResult.getUploadedFileLocation();
		boolean updateExisting = asyncCSVResult.isUpdateExisting();
		boolean ignoreHeading = asyncCSVResult.isIgnoreHeading();
		char delimeterType = asyncCSVResult.getDelimeterType();

		// Make shure uploaded file is exist
		try {
			File file = new File(fileLocation);
			if(!file.exists()) {
				log.error("File not found : {}", fileLocation);
				return;
			}
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
		}
		
		String parsedCsvFileName = getTemporaryDiretory(asyncCSVResult.getModuleName(), true) + UUID.randomUUID() + ".csv";
		try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(parsedCsvFileName, true), CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL)
				.withIgnoreEmptyLines()
				.withDelimiter(',')
				.withIgnoreSurroundingSpaces())) {
			
			CSVFormat csvFormat = CSVFormat.DEFAULT.withTrim().withDelimiter(delimeterType).withIgnoreEmptyLines(true);
			if(ignoreHeading) {
				csvFormat = CSVFormat.DEFAULT.withHeader().withTrim().withDelimiter(delimeterType).withIgnoreEmptyLines(true).withIgnoreHeaderCase(true);
			}
			
			try (Reader reader = Files.newBufferedReader(Paths.get(fileLocation));
					CSVParser csvParser = new CSVParser(reader, csvFormat);
					Stream<String> stream = Files.lines(Paths.get(fileLocation), Charset.defaultCharset())) {
				
				long totalLines = stream.count();
				if(ignoreHeading) totalLines--;
				
				int numberOfUpdateRecord = 0;
				int numberOfNewRecord = 0;
				int totalNumberOfRecord = 0;
				int rowNumber = ignoreHeading ? 1 : 0;
				
				List<String> duplicateListHeadCheckCache = new ArrayList<>();
				
				for (CSVRecord row : csvParser) {
					++rowNumber;

					long tnoc = row.size();

					// A - Row Type
					String colA = getRecordValue(row, tnoc, ROW_TYPE.getColumnIndex());
					if(StringUtils.isBlank(colA) || (!"H".equalsIgnoreCase(colA) && !"D".equalsIgnoreCase(colA))) {
						generateErrors(asyncCSVResult.getCsvErrors(), ROW_TYPE.getColumn() + " - " + ROW_TYPE.getColumnName(), "Row type required", rowNumber);
					}

					// B - Voucher Code
					String colB = getRecordValue(row, tnoc, VOUCHER_CODE.getColumnIndex());
					if(StringUtils.isBlank(colB)) {
						generateErrors(asyncCSVResult.getCsvErrors(), VOUCHER_CODE.getColumn() + " - " + VOUCHER_CODE.getColumnName(), "Voucher Code required", rowNumber);
					}
					if("H".equalsIgnoreCase(colA) && !updateExisting) {
						Acheader ah = acService.findAcheaderByXvoucher(colB, asyncCSVResult.getBusinessId());
						if(ah != null) {
							generateErrors(asyncCSVResult.getCsvErrors(), VOUCHER_CODE.getColumn() + " - " + VOUCHER_CODE.getColumnName(), "Voucher Code already exist in this system. Please select update exisiting record", rowNumber);
						}
					}


					// C - Date / Account
					String colC = getRecordValue(row, tnoc, VOUCHER_DATE.getColumnIndex());
					if("H".equalsIgnoreCase(colA)) {
						if(StringUtils.isBlank(colC)) {
							generateErrors(asyncCSVResult.getCsvErrors(), VOUCHER_DATE.getColumn() + " - " + VOUCHER_DATE.getColumnName(), "Voucher date required", rowNumber);
						}
					} else if ("D".equalsIgnoreCase(colA)) {
						if(StringUtils.isBlank(colC)) {
							generateErrors(asyncCSVResult.getCsvErrors(), VOUCHER_DATE.getColumn() + " - " + VOUCHER_DATE.getColumnName(), "Account number required", rowNumber);
						}
					}

					if(!asyncCSVResult.getCsvErrors().isEmpty()) continue;

					String colD = getRecordValue(row, tnoc, VOUCHER_REFERENCE.getColumnIndex());
					String colE = getRecordValue(row, tnoc, LC_NO.getColumnIndex());
					String colF = getRecordValue(row, tnoc, CHEQUE_NO.getColumnIndex());
					String colG = getRecordValue(row, tnoc, NOTE.getColumnIndex());

					// Create data and print
					boolean existingRecord = false;
					if("H".equalsIgnoreCase(colA)) {
						Acheader acheader = new Acheader();
						csvPrinter.printRecord(getPrintableDataFromListHead(acheader, existingRecord));
					} else {
						//
					}

					// Progress 
					if(existingRecord) {
						asyncCSVResult.setNumberOfUpdateRecord(++numberOfUpdateRecord);
					} else {
						asyncCSVResult.setNumberOfCreateRecord(++numberOfNewRecord);
					}
					asyncCSVResult.setTotalNumberOfRecords(++totalNumberOfRecord);
					
				}
				
				if(totalLines < 1) {
					generateErrors(asyncCSVResult.getCsvErrors(), "CSV file", "File is empty", 0);
				}
				
			} catch (Exception e) {
				log.error(ERROR, e.getMessage());
				generateErrors(asyncCSVResult.getCsvErrors(), "CSV file", "Invalid file", 0);
			}
		}  catch (IOException e) {
			log.error("Can not write header {}: {}", parsedCsvFileName, e.getMessage());
			return;
		}

		if(asyncCSVResult.getCsvErrors().isEmpty()) {
			asyncCSVResult.setAllOk(true);
			asyncCSVResult.setFileLocationToImportData(parsedCsvFileName);
		}
	}

	private List<String> getPrintableDataFromListHead(Acheader acheader, boolean existingVoucher){
		if(acheader == null) return Collections.emptyList();
		List<String> data = new ArrayList<>();
		data.add("H");																				// 0
		data.add(existingVoucher ? "Y" : "N");														// 2
		data.add(modifiedValue(existingVoucher ? acheader.getXvoucher() : acheader.getXtrn()));		// 3
		data.add(SDF.format(acheader.getXdate()));													// 4
		data.add(modifiedValue(acheader.getXref()));												// 5
		data.add(modifiedValue(acheader.getXlcno()));												// 6
		data.add(modifiedValue(acheader.getXchequeno()));											// 7
		data.add(modifiedValue(acheader.getXnote()));												// 8
		return data;
	}

	@Override
	public void importCSV(AsyncCSVResult asyncCSVResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHeader() {
		return getHeader(VoucherImportExportColumns.class);
	}

}
