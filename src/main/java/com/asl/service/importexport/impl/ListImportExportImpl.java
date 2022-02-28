package com.asl.service.importexport.impl;

import static com.asl.enums.ListImportExportColumns.LIST_CODE;
import static com.asl.enums.ListImportExportColumns.LIST_DESC;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT1;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT10;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT11;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT12;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT13;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT14;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT15;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT16;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT2;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT3;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT4;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT5;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT6;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT7;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT8;
import static com.asl.enums.ListImportExportColumns.LIST_PRMPT9;
import static com.asl.enums.ListImportExportColumns.LIST_TYPE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

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

import com.asl.entity.DataList;
import com.asl.entity.ListHead;
import com.asl.enums.ListImportExportColumns;
import com.asl.model.AsyncCSVResult;
import com.asl.model.ImportExportPage;
import com.asl.model.ModuleOption;
import com.asl.service.ListService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since AUG 23, 2021
 */
@Slf4j
@Service("listimportexportService")
public class ListImportExportImpl extends AbstractImportExport {

	private static final String MODULE_NAME = "listimportexport";
	private static final String EXPORT_FILE_NAME = "list";

	@Autowired private ListService listService;

	@Override
	public String showExportImportPage(Model model) {
		log.debug("Load List export/import module");
		model.addAttribute("module", getnerateExportImportPageData());
		return IMPORT_EXPORT_PATH;
	}

	private ImportExportPage getnerateExportImportPageData() {
		ImportExportPage eip = new ImportExportPage();
		eip.setModuleName(MODULE_NAME);
		eip.setPageTitle("List Import/Export");
		eip.setModuleColumns(getModuleColumns(ListImportExportColumns.class));
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
		listDwnloadOptn.setDependentPromt("Select List Code");
		listDwnloadOptn.setDependentName("listSelect");

		List<ListHead> result = listService.getAllListHead();
		Map<String, String> lhMap = new HashMap<>();
		if(result != null && !result.isEmpty()) {
			lhMap = result.stream().collect(Collectors.toMap(s -> s.getListcode(), s -> s.getListcode()));
		}
		listDwnloadOptn.setDependentOptions(lhMap);
		downloadOptions.add(listDwnloadOptn);

		eip.setDownloadOption(downloadOptions);

		return eip;
	}

	@Override
	public void downloadTemplate(AsyncCSVResult asyncCSVResult) throws IOException {
		HttpServletResponse response = asyncCSVResult.getHttpServletResponse();
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("text/csv;charset=UTF-8");
		String fileName = "list-template.csv";
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

		List<String> listcodes = new ArrayList<>();
		if(!isDownloadAll(op) && !op.getSelectedData().isEmpty()) {
			for(String id : op.getSelectedData()) {
				listcodes.add(id);
			}
		}

		long totalSize = 0;
		long chunkLimit = CHUNK_LIMIT;
		long offset = 0;
		long dataSize = 1;

		while(dataSize > 0) {
			List<String[]> fileLineData = new ArrayList<>();

			List<Map<String, Object>> result = listService.getExportDataByChunk(chunkLimit, offset + 1, listcodes, asyncCSVResult.getBusinessId());
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
			String listHeadCode = (String) row.get("LH_CODE");
			if(StringUtils.isBlank(listHeadCode)) continue;

			if(!cachedPrintedListHead.contains(listHeadCode.toUpperCase())) {
				// Creating list head 
				String[] dataLine = new String[19];
				dataLine[0] = "H";
				dataLine[1] = listHeadCode.toUpperCase();
				dataLine[2] = modifiedValue((String) row.get("LH_DESC"));
				dataLine[3] = modifiedValue((String) row.get("LH_PRMP_1"));
				dataLine[4] = modifiedValue((String) row.get("LH_PRMP_2"));
				dataLine[5] = modifiedValue((String) row.get("LH_PRMP_3"));
				dataLine[6] = modifiedValue((String) row.get("LH_PRMP_4"));
				dataLine[7] = modifiedValue((String) row.get("LH_PRMP_5"));
				dataLine[8] = modifiedValue((String) row.get("LH_PRMP_6"));
				dataLine[9] = modifiedValue((String) row.get("LH_PRMP_7"));
				dataLine[10] = modifiedValue((String) row.get("LH_PRMP_8"));
				dataLine[11] = modifiedValue((String) row.get("LH_PRMP_9"));
				dataLine[12] = modifiedValue((String) row.get("LH_PRMP_10"));
				dataLine[13] = modifiedValue((String) row.get("LH_PRMP_11"));
				dataLine[14] = modifiedValue((String) row.get("LH_PRMP_12"));
				dataLine[15] = modifiedValue((String) row.get("LH_PRMP_13"));
				dataLine[16] = modifiedValue((String) row.get("LH_PRMP_14"));
				dataLine[17] = modifiedValue((String) row.get("LH_PRMP_15"));
				dataLine[18] = modifiedValue((String) row.get("LH_PRMP_16"));
				fileLineData.add(dataLine);
				if(!cachedPrintedListHead.isEmpty()) cachedPrintedListHead.remove(0);
				cachedPrintedListHead.add(listHeadCode.toUpperCase());
			}

			// Creation list line
			String[] dl = new String[19];
			dl[0] = "D";
			dl[1] = listHeadCode.toUpperCase();
			dl[2] = modifiedValue((String) row.get("LS_DESC"));
			dl[3] = modifiedValue((String) row.get("LS_VAL_1"));
			dl[4] = modifiedValue((String) row.get("LS_VAL_2"));
			dl[5] = modifiedValue((String) row.get("LS_VAL_3"));
			dl[6] = modifiedValue((String) row.get("LS_VAL_4"));
			dl[7] = modifiedValue((String) row.get("LS_VAL_5"));
			dl[8] = modifiedValue((String) row.get("LS_VAL_6"));
			dl[9] = modifiedValue((String) row.get("LS_VAL_7"));
			dl[10] = modifiedValue((String) row.get("LS_VAL_8"));
			dl[11] = modifiedValue((String) row.get("LS_VAL_9"));
			dl[12] = modifiedValue((String) row.get("LS_VAL_10"));
			dl[13] = modifiedValue((String) row.get("LS_VAL_11"));
			dl[14] = modifiedValue((String) row.get("LS_VAL_12"));
			dl[15] = modifiedValue((String) row.get("LS_VAL_13"));
			dl[16] = modifiedValue((String) row.get("LS_VAL_14"));
			dl[17] = modifiedValue((String) row.get("LS_VAL_15"));
			dl[18] = modifiedValue((String) row.get("LS_VAL_16"));
			fileLineData.add(dl);
		}
	}

	@Override
	public String getHeader() {
		return getHeader(ListImportExportColumns.class);
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

					String listType = getRecordValue(row, tnoc, LIST_TYPE.getColumnIndex());		// A - list type
					String listCode = getRecordValue(row, tnoc, LIST_CODE.getColumnIndex());		// B - required
					String listDesc = getRecordValue(row, tnoc, LIST_DESC.getColumnIndex());		// C

					// A -- List Type
					if(StringUtils.isBlank(listType) || (!"H".equalsIgnoreCase(listType) && !"D".equalsIgnoreCase(listType))) {
						generateErrors(asyncCSVResult.getCsvErrors(), LIST_TYPE.getColumn() + " - " + LIST_TYPE.getColumnName(), "List type required", rowNumber);
					}

					// B -- List Code
					if(StringUtils.isBlank(listCode)) {
						generateErrors(asyncCSVResult.getCsvErrors(), LIST_CODE.getColumn() + " - " + LIST_CODE.getColumnName(), "List Code required", rowNumber);
					} else {
						listCode = listService.modifiedListcode(listCode);
					}

					// Duplicate list head line check
					if("H".equalsIgnoreCase(listType) && StringUtils.isNotBlank(listCode) && duplicateListHeadCheckCache.contains(listCode.toUpperCase())) {
						generateErrors(asyncCSVResult.getCsvErrors(), LIST_CODE.getColumn() + " - " + LIST_CODE.getColumnName(), "Duplicate list head line", rowNumber);
					}

					boolean existingListHead = false;
					if("H".equalsIgnoreCase(listType) && StringUtils.isNotBlank(listCode)) {
						ListHead existListHead = listService.findListHeadByListcode(listCode, asyncCSVResult.getBusinessId());
						if(existListHead != null) {
							existingListHead = true;
						}
						if(existingListHead && !updateExisting) {  // Update existing record must be check to do update
							generateErrors(asyncCSVResult.getCsvErrors(), LIST_CODE.getColumn() + " - " + LIST_CODE.getColumnName(), "List code already exist in the system", rowNumber);
						}
						duplicateListHeadCheckCache.add(listCode.toUpperCase());
					}

					ListHead lh = null;
					DataList dl = null;
					if("H".equalsIgnoreCase(listType)) {
						lh = createListHeadFromRow(row, listCode, listDesc, tnoc);
						validateListHead(lh, asyncCSVResult, rowNumber);
					} else {
						dl = createDataListFromRow(row, listCode, listDesc, tnoc);
						validateDataList(dl, asyncCSVResult, rowNumber);
					}

					if(!asyncCSVResult.getCsvErrors().isEmpty()) continue;  // IF got any errors from above then no need to do anything else below

					// Create data and print
					if("H".equalsIgnoreCase(listType)) {
						csvPrinter.printRecord(getPrintableDataFromListHead(lh, existingListHead));
					} else if ("D".equalsIgnoreCase(listType)) {
						csvPrinter.printRecord(getPrintableDataFromListLine(dl));
					}

					// Progress 
					if(existingListHead) {
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

	private List<String> getPrintableDataFromListHead(ListHead lh, boolean existingListHead){
		if(lh == null) return Collections.emptyList();
		List<String> data = new ArrayList<>();
		data.add("H");												// 0
		data.add(modifiedValue(lh.getListcode().toUpperCase()));	// 1
		data.add(modifiedValue(lh.getDescription()));				// 2
		data.add(modifiedValue(lh.getPrompt1()));					// 3
		data.add(modifiedValue(lh.getPrompt2()));					// 4
		data.add(modifiedValue(lh.getPrompt3()));					// 5
		data.add(modifiedValue(lh.getPrompt4()));					// 6
		data.add(modifiedValue(lh.getPrompt5()));					// 7
		data.add(modifiedValue(lh.getPrompt6()));					// 8
		data.add(modifiedValue(lh.getPrompt7()));					// 9
		data.add(modifiedValue(lh.getPrompt8()));					// 10
		data.add(modifiedValue(lh.getPrompt9()));					// 11
		data.add(modifiedValue(lh.getPrompt10()));					// 12
		data.add(modifiedValue(lh.getPrompt11()));					// 13
		data.add(modifiedValue(lh.getPrompt12()));					// 14
		data.add(modifiedValue(lh.getPrompt13()));					// 15
		data.add(modifiedValue(lh.getPrompt14()));					// 16
		data.add(modifiedValue(lh.getPrompt15()));					// 17
		data.add(modifiedValue(lh.getPrompt16()));					// 18
		data.add(existingListHead ? "Y" : "N");						// 19
		return data;
	}

	private List<String> getPrintableDataFromListLine(DataList dl){
		if(dl == null) return Collections.emptyList();
		List<String> data = new ArrayList<>();
		data.add("D");												// 0
		data.add(modifiedValue(dl.getListcode().toUpperCase()));	// 1
		data.add(modifiedValue(dl.getDescription()));				// 2
		data.add(modifiedValue(dl.getListvalue1()));				// 3
		data.add(modifiedValue(dl.getListvalue2()));				// 4
		data.add(modifiedValue(dl.getListvalue3()));				// 5
		data.add(modifiedValue(dl.getListvalue4()));				// 6
		data.add(modifiedValue(dl.getListvalue5()));				// 7
		data.add(modifiedValue(dl.getListvalue6()));				// 8
		data.add(modifiedValue(dl.getListvalue7()));				// 9
		data.add(modifiedValue(dl.getListvalue8()));				// 10
		data.add(modifiedValue(dl.getListvalue9()));				// 11
		data.add(modifiedValue(dl.getListvalue10()));				// 12
		data.add(modifiedValue(dl.getListvalue11()));				// 13
		data.add(modifiedValue(dl.getListvalue12()));				// 14
		data.add(modifiedValue(dl.getListvalue13()));				// 15
		data.add(modifiedValue(dl.getListvalue14()));				// 16
		data.add(modifiedValue(dl.getListvalue15()));				// 17
		data.add(modifiedValue(dl.getListvalue16()));				// 18
		data.add("Y");												// 19
		return data;
	}

	private void validateDataList(DataList dl, AsyncCSVResult asyncCSVResult, int zLine) {
		Set<ConstraintViolation<DataList>> constraintViolations = validator.validate(dl);
		Iterator<ConstraintViolation<DataList>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<DataList> item = it.next();
			if("listCode".equalsIgnoreCase(item.getPropertyPath().toString())) generateErrors(asyncCSVResult.getCsvErrors(), "", item.getMessage(), zLine);
		}
	}

	private void validateListHead(ListHead lh, AsyncCSVResult asyncCSVResult, int zLine) {
		Set<ConstraintViolation<ListHead>> constraintViolations = validator.validate(lh);
		Iterator<ConstraintViolation<ListHead>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<ListHead> item = it.next();
			generateErrors(asyncCSVResult.getCsvErrors(), "", item.getMessage(), zLine);
		}
	}

	private DataList createDataListFromRow(CSVRecord row, String listCode, String listDesc, long tnoc) {
		DataList dl = new DataList();
		dl.setListcode(listCode.toUpperCase());
		dl.setDescription(listDesc);
		dl.setListvalue1(getRecordValue(row, tnoc, LIST_PRMPT1.getColumnIndex()));
		dl.setListvalue2(getRecordValue(row, tnoc, LIST_PRMPT2.getColumnIndex()));
		dl.setListvalue3(getRecordValue(row, tnoc, LIST_PRMPT3.getColumnIndex()));
		dl.setListvalue4(getRecordValue(row, tnoc, LIST_PRMPT4.getColumnIndex()));
		dl.setListvalue5(getRecordValue(row, tnoc, LIST_PRMPT5.getColumnIndex()));
		dl.setListvalue6(getRecordValue(row, tnoc, LIST_PRMPT6.getColumnIndex()));
		dl.setListvalue7(getRecordValue(row, tnoc, LIST_PRMPT7.getColumnIndex()));
		dl.setListvalue8(getRecordValue(row, tnoc, LIST_PRMPT8.getColumnIndex()));
		dl.setListvalue9(getRecordValue(row, tnoc, LIST_PRMPT9.getColumnIndex()));
		dl.setListvalue10(getRecordValue(row, tnoc, LIST_PRMPT10.getColumnIndex()));
		dl.setListvalue11(getRecordValue(row, tnoc, LIST_PRMPT11.getColumnIndex()));
		dl.setListvalue12(getRecordValue(row, tnoc, LIST_PRMPT12.getColumnIndex()));
		dl.setListvalue13(getRecordValue(row, tnoc, LIST_PRMPT13.getColumnIndex()));
		dl.setListvalue14(getRecordValue(row, tnoc, LIST_PRMPT14.getColumnIndex()));
		dl.setListvalue15(getRecordValue(row, tnoc, LIST_PRMPT15.getColumnIndex()));
		dl.setListvalue16(getRecordValue(row, tnoc, LIST_PRMPT16.getColumnIndex()));
		return dl;
	}

	private ListHead createListHeadFromRow(CSVRecord row, String listCode, String listDesc, long tnoc) {
		ListHead lh = new ListHead();
		lh.setListcode(listCode.toUpperCase());										// 0
		lh.setDescription(listDesc);												// 1
		lh.setPrompt1(getRecordValue(row, tnoc, LIST_PRMPT1.getColumnIndex()));		// 2
		lh.setPrompt2(getRecordValue(row, tnoc, LIST_PRMPT2.getColumnIndex()));		// 3
		lh.setPrompt3(getRecordValue(row, tnoc, LIST_PRMPT3.getColumnIndex()));		// 4
		lh.setPrompt4(getRecordValue(row, tnoc, LIST_PRMPT4.getColumnIndex()));		// 5
		lh.setPrompt5(getRecordValue(row, tnoc, LIST_PRMPT5.getColumnIndex()));		// 6
		lh.setPrompt6(getRecordValue(row, tnoc, LIST_PRMPT6.getColumnIndex()));		// 7
		lh.setPrompt7(getRecordValue(row, tnoc, LIST_PRMPT7.getColumnIndex()));		// 8
		lh.setPrompt8(getRecordValue(row, tnoc, LIST_PRMPT8.getColumnIndex()));		// 9
		lh.setPrompt9(getRecordValue(row, tnoc, LIST_PRMPT9.getColumnIndex()));		// 10
		lh.setPrompt10(getRecordValue(row, tnoc, LIST_PRMPT10.getColumnIndex()));	// 11
		lh.setPrompt11(getRecordValue(row, tnoc, LIST_PRMPT11.getColumnIndex()));	// 12
		lh.setPrompt12(getRecordValue(row, tnoc, LIST_PRMPT12.getColumnIndex()));	// 13
		lh.setPrompt13(getRecordValue(row, tnoc, LIST_PRMPT13.getColumnIndex()));	// 14
		lh.setPrompt14(getRecordValue(row, tnoc, LIST_PRMPT14.getColumnIndex()));	// 15
		lh.setPrompt15(getRecordValue(row, tnoc, LIST_PRMPT15.getColumnIndex()));	// 16
		lh.setPrompt16(getRecordValue(row, tnoc, LIST_PRMPT16.getColumnIndex()));	// 17
		return lh;
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
			List<String> archivedDataListListCodes = new ArrayList<>();
			List<String> listCodes = new ArrayList<>();
			for (CSVRecord csvRecord : csvParser) {
				updateExisting = "Y".equalsIgnoreCase(csvRecord.get(19));
				String listType = csvRecord.get(0);
				String listCode = csvRecord.get(1);

				if("H".equalsIgnoreCase(listType)) {
					ListHead listHead = null;
					if(updateExisting) {  // update existing then update
						listHead = listService.findListHeadByListcode(listCode, asyncCSVResult.getBusinessId());
						if(listHead == null) {
							updateExisting = false;
							listHead = new ListHead();
						}
					} else { // create new list head
						listHead = new ListHead();
					}
					listHead.setListcode(listCode);
					listHead.setDescription(csvRecord.get(2));
					listHead.setPrompt1(csvRecord.get(3));
					listHead.setPrompt2(csvRecord.get(4));
					listHead.setPrompt3(csvRecord.get(5));
					listHead.setPrompt4(csvRecord.get(6));
					listHead.setPrompt5(csvRecord.get(7));
					listHead.setPrompt6(csvRecord.get(8));
					listHead.setPrompt7(csvRecord.get(9));
					listHead.setPrompt8(csvRecord.get(10));
					listHead.setPrompt9(csvRecord.get(11));
					listHead.setPrompt10(csvRecord.get(12));
					listHead.setPrompt11(csvRecord.get(13));
					listHead.setPrompt12(csvRecord.get(14));
					listHead.setPrompt13(csvRecord.get(15));
					listHead.setPrompt14(csvRecord.get(16));
					listHead.setPrompt15(csvRecord.get(17));
					listHead.setPrompt16(csvRecord.get(18));
					listCodes.add(listCode);
					if(updateExisting) {
						listService.updateListHead(listHead, asyncCSVResult.getBusinessId(), asyncCSVResult.getLoggedInUserDetail().getUsername());
						numberOfUpdateRecord++;
					} else {
						listService.saveListHead(listHead, asyncCSVResult.getBusinessId(), asyncCSVResult.getLoggedInUserDetail().getUsername());
						numberOfNewRecord++;
					}
				} else {
					// Archive previous datalist with listcode
					if(!archivedDataListListCodes.contains(listCode) && listCodes.contains(listCode)) {
						listService.deleteDataList(listCode, asyncCSVResult.getBusinessId());
						archivedDataListListCodes.add(listCode);
					}

					// Create new datalist with listcode
					DataList dl = new DataList();
					dl.setListcode(listCode);
					dl.setDescription(csvRecord.get(2));
					dl.setListvalue1(csvRecord.get(3));
					dl.setListvalue2(csvRecord.get(4));
					dl.setListvalue3(csvRecord.get(5));
					dl.setListvalue4(csvRecord.get(6));
					dl.setListvalue5(csvRecord.get(7));
					dl.setListvalue6(csvRecord.get(8));
					dl.setListvalue7(csvRecord.get(9));
					dl.setListvalue8(csvRecord.get(10));
					dl.setListvalue9(csvRecord.get(11));
					dl.setListvalue10(csvRecord.get(12));
					dl.setListvalue11(csvRecord.get(13));
					dl.setListvalue12(csvRecord.get(14));
					dl.setListvalue13(csvRecord.get(15));
					dl.setListvalue14(csvRecord.get(16));
					dl.setListvalue15(csvRecord.get(17));
					dl.setListvalue16(csvRecord.get(18));
					listService.saveDataList(dl, asyncCSVResult.getBusinessId(), asyncCSVResult.getLoggedInUserDetail().getUsername());
					numberOfNewRecord++;
				}

				// PROGRESS OF CSV PARSING
				Double progress = ((double) (++rowNumber) / totalLines) * 100;
				asyncCSVResult.setProgress(progress.intValue());
			}

			asyncCSVResult.setNumberOfUpdateRecord(numberOfUpdateRecord);
			asyncCSVResult.setNumberOfCreateRecord(numberOfNewRecord);
			asyncCSVResult.setTotalNumberOfRecords(numberOfUpdateRecord + numberOfNewRecord);

		} catch (IOException e) {
			log.error("Can not read file {}: {}", fileLocation, e.getMessage());
		}
	}

}
