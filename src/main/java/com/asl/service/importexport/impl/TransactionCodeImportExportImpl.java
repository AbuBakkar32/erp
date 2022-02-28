package com.asl.service.importexport.impl;

import static com.asl.enums.TransactionCodeImportExportColumns.XDESC;
import static com.asl.enums.TransactionCodeImportExportColumns.XINC;
import static com.asl.enums.TransactionCodeImportExportColumns.XNUM;
import static com.asl.enums.TransactionCodeImportExportColumns.XTRN;
import static com.asl.enums.TransactionCodeImportExportColumns.XTYPETRN;

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

import com.asl.entity.Xtrn;
import com.asl.enums.TransactionCodeImportExportColumns;
import com.asl.model.AsyncCSVResult;
import com.asl.model.ImportExportPage;
import com.asl.model.ServiceException;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since AUG 23, 2021
 */
@Slf4j
@Service("transactioncodeimportexportService")
public class TransactionCodeImportExportImpl extends AbstractImportExport {

	private static final String MODULE_NAME = "transactioncodeimportexport";
	private static final String EXPORT_FILE_NAME = "transactioncode";

	@Autowired private XtrnService xtrnService;

	@Override
	public String showExportImportPage(Model model) {
		log.debug("Load transaction code export/import module");
		model.addAttribute("module", getnerateExportImportPageData());
		return IMPORT_EXPORT_PATH;
	}

	private ImportExportPage getnerateExportImportPageData() {
		ImportExportPage eip = new ImportExportPage();
		eip.setModuleName(MODULE_NAME);
		eip.setPageTitle("Transaction Code Import/Export");
		eip.setModuleColumns(getModuleColumns(TransactionCodeImportExportColumns.class));
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

		return eip;
	}

	@Override
	public void downloadTemplate(AsyncCSVResult asyncCSVResult) throws IOException {
		HttpServletResponse response = asyncCSVResult.getHttpServletResponse();
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("text/csv;charset=UTF-8");
		String fileName = "transaction-code-template.csv";
		response.addHeader("content-disposition", "attachment; filename=\"" + fileName +"\"");

		// Print header
		out.println(getHeader());
		out.flush();

		response.flushBuffer();
		response.getOutputStream().close();
	}

	@Override
	public void retreiveData(AsyncCSVResult asyncCSVResult) throws IOException {

		String fileType = "csv";
		String fileName = getFileName(EXPORT_FILE_NAME, fileType);

		String tempoFileName = getResult(fileName, asyncCSVResult);

		asyncCSVResult.setFileName(fileName);
		asyncCSVResult.setMimeType(new MediaType("text", fileType));
		asyncCSVResult.setDataFileName(tempoFileName);

	}

	private String getResult(String fileName, AsyncCSVResult asyncCSVResult) {
		
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

		long totalSize = 0;
		long chunkLimit = CHUNK_LIMIT;
		long offset = 0;
		long dataSize = 1;

		while(dataSize > 0) {
			List<String[]> fileLineData = new ArrayList<>();

			List<Map<String, Object>> result = xtrnService.getExportDataByChunk(chunkLimit, offset + 1, asyncCSVResult.getBusinessId());
			dataSize = result.size();

			offset = offset + dataSize;

			createFileLineDataFromResult(fileLineData, result);

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

	private void createFileLineDataFromResult(List<String[]> fileLineData, List<Map<String, Object>> result) {
		for(Map<String, Object> row : result) {
			String xtypetrn = modifiedValue((String) row.get("XTYPETRN"));
			String xtrn = modifiedValue((String) row.get("XTRN"));
			if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) continue;

			String[] dataLine = new String[5];
			dataLine[0] = xtypetrn;
			dataLine[1] = xtrn;
			dataLine[2] = modifiedValue((String) row.get("XDESC"));
			dataLine[3] = ((BigDecimal) row.get("XNUM")).toString();
			dataLine[4] = ((BigDecimal) row.get("XINC")).toString();
			fileLineData.add(dataLine);
		}
	}

	@Override
	public String getHeader() {
		return getHeader(TransactionCodeImportExportColumns.class);
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

				List<String> duplicateLineCheckCache = new ArrayList<>();

				for (CSVRecord row : csvParser) {
					++rowNumber;

					long tnoc = row.size();

					String xtypetrn = getRecordValue(row, tnoc, XTYPETRN.getColumnIndex());		// A - Transaction Code type -- required
					String xtrn = getRecordValue(row, tnoc, XTRN.getColumnIndex());				// B - Transaction code -- required

					// A -- List Type
					if(StringUtils.isBlank(xtypetrn)) {
						generateErrors(asyncCSVResult.getCsvErrors(), XTYPETRN.getColumn() + " - " + XTYPETRN.getColumnName(), "Transaction Type required", rowNumber);
					}

					// B - Transaction code
					if(StringUtils.isBlank(xtrn)) {
						generateErrors(asyncCSVResult.getCsvErrors(), XTRN.getColumn() + " - " + XTRN.getColumnName(), "Code required", rowNumber);
					}

					// Duplicate list head line check
					if(StringUtils.isNotBlank(xtypetrn) && StringUtils.isBlank(xtrn) && duplicateLineCheckCache.contains(xtypetrn + xtrn)) {
						generateErrors(asyncCSVResult.getCsvErrors(), XTYPETRN.getColumn() + " - " + XTYPETRN.getColumnName(), "Duplicate Transaction Type And Code line", rowNumber);
					}

					Xtrn existingXtrn = null;
					boolean existingRecord = false;
					if(StringUtils.isNotBlank(xtypetrn) && StringUtils.isNotBlank(xtrn)) {
						existingXtrn = xtrnService.findByXtypetrnAndXtrnAndZid(xtypetrn, xtrn, asyncCSVResult.getBusinessId());   //listService.findListHeadByListcode(listCode, asyncCSVResult.getBusinessId());
						if(existingXtrn != null) {
							existingRecord = true;
						}
						if(existingRecord && !updateExisting) {  // Update existing record must be check to do update
							generateErrors(asyncCSVResult.getCsvErrors(), XTYPETRN.getColumn() + " - " + XTYPETRN.getColumnName(), "Transaction Type And Code already exist in the system", rowNumber);
						}
						duplicateLineCheckCache.add(xtypetrn + xtrn);
					}

					if(!asyncCSVResult.getCsvErrors().isEmpty()) continue;  // IF got any errors from above then no need to do anything else below

					String xdesc = getRecordValue(row, tnoc, XDESC.getColumnIndex());			// C
					String xnum = getRecordValue(row, tnoc, XNUM.getColumnIndex());				// d
					if(StringUtils.isBlank(xnum) || !StringUtils.isNumeric(xnum)) xnum = "0";
					String xinc = getRecordValue(row, tnoc, XINC.getColumnIndex());				// e
					if(StringUtils.isBlank(xinc) || !StringUtils.isNumeric(xinc)) xnum = "1";

					Xtrn ob = new Xtrn();
					ob.setXtypetrn(xtypetrn);
					ob.setXtrn(xtrn);
					ob.setXdesc(xdesc);
					ob.setXnum(existingRecord == true ? existingXtrn.getXnum() : Integer.parseInt(xnum));
					ob.setXinc(existingRecord == true ? existingXtrn.getXinc() :Integer.parseInt(xinc));

					// Create data and print
					csvPrinter.printRecord(getPrintableDataFromListHead(ob, existingRecord));

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
		} catch (IOException e) {
			log.error("Can not write header {}: {}", parsedCsvFileName, e.getMessage());
			return;
		}

		if(asyncCSVResult.getCsvErrors().isEmpty()) {
			asyncCSVResult.setAllOk(true);
			asyncCSVResult.setFileLocationToImportData(parsedCsvFileName);
		}
	}

	private List<String> getPrintableDataFromListHead(Xtrn ob, boolean existingRecord){
		if(ob == null) return Collections.emptyList();
		List<String> data = new ArrayList<>();
		data.add(ob.getXtypetrn());												// 0
		data.add(ob.getXtrn());													// 1
		data.add(modifiedValue(ob.getXdesc()));									// 2
		data.add(ob.getXnum().toString());										// 3
		data.add(ob.getXinc().toString());										// 4
		data.add(existingRecord ? "Y" : "N");									// 5
		return data;
	}


	@Override
	public void importCSV(AsyncCSVResult asyncCSVResult) {
		if(asyncCSVResult == null) return;

		// Make sure uploaded file is exist
		String fileLocation = asyncCSVResult.getFileLocationToImportData();
		try {
			File file = new File(fileLocation);
			if(!file.exists()) {
				log.error("File not found : {}", fileLocation);
				return;
			}
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
		}

		int numberOfUpdateRecord = 0;
		int numberOfNewRecord = 0;

		CSVFormat csvFormat = CSVFormat.DEFAULT.withTrim().withDelimiter(',').withIgnoreEmptyLines(true);
		try (Reader reader = Files.newBufferedReader(Paths.get(fileLocation));
				CSVParser csvParser = new CSVParser(reader, csvFormat);
				Stream<String> stream = Files.lines(Paths.get(fileLocation), Charset.defaultCharset())) {

			long totalLines = stream.count();
			int rowNumber = 0;

			boolean updateExisting = false;

			for (CSVRecord csvRecord : csvParser) {
				updateExisting = "Y".equalsIgnoreCase(csvRecord.get(5));
				String xtypetrn = csvRecord.get(0);
				String xtrn = csvRecord.get(1);

				Xtrn obj = null;
				if(updateExisting) {  // update existing then update
					obj = xtrnService.findByXtypetrnAndXtrnAndZid(xtypetrn, xtrn, asyncCSVResult.getBusinessId());
					if(obj == null) {
						updateExisting = false;
						obj = new Xtrn();
					}
				} else { // create new list head
					obj = new Xtrn();
				}
				obj.setXtypetrn(xtypetrn);
				obj.setXtrn(xtrn);
				obj.setXdesc(csvRecord.get(2));
				obj.setXnum(Integer.parseInt(csvRecord.get(3)));
				obj.setXinc(Integer.parseInt(csvRecord.get(4)));

				if(updateExisting) {
					xtrnService.update(obj, asyncCSVResult.getBusinessId(), asyncCSVResult.getLoggedInUserDetail().getUsername());
					numberOfUpdateRecord++;
				} else {
					xtrnService.save(obj, asyncCSVResult.getBusinessId(), asyncCSVResult.getLoggedInUserDetail().getUsername());
					numberOfNewRecord++;
				}

				// PROGRESS OF CSV PARSING
				Double progress = ((double) (++rowNumber) / totalLines) * 100;
				asyncCSVResult.setProgress(progress.intValue());
			}

			asyncCSVResult.setNumberOfUpdateRecord(numberOfUpdateRecord);
			asyncCSVResult.setNumberOfCreateRecord(numberOfNewRecord);
			asyncCSVResult.setTotalNumberOfRecords(numberOfUpdateRecord + numberOfNewRecord);

		} catch (IOException | ServiceException e) {
			log.error("Can not read file {}: {}", fileLocation, e.getMessage());
		}
	}

}
