package com.asl.service.importexport.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Validator;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.asl.config.AppConfig;
import com.asl.model.CSVError;
import com.asl.model.ImportExportModuleColumn;
import com.asl.model.ModuleOption;
import com.asl.service.importexport.GenericImportExportColumns;
import com.asl.service.importexport.ImportExportService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 *
 */
@Slf4j
public abstract class AbstractImportExport implements ImportExportService {

	static final String ERROR = "Error is : {}, {}";
	static final String IMPORT_EXPORT_PATH = "pages/importexport/importexport";
	static final long CHUNK_LIMIT = 1000;
	static final String LINE_BREAK = "\r\n";
	static final  SimpleDateFormat DATE_FORMATER_2 = new SimpleDateFormat("ddMMyyyy_HHmmssSSSSSSS");
	static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired protected Validator validator;
	@Autowired protected AppConfig appConfig;

	protected String modifiedValue(String value) {
		return StringUtils.isBlank(value) ? "" : value;
	}

	protected String getValueForCSV(String dataValue) {
		if(StringUtils.isBlank(dataValue)) return "";
		return dataValue;
	}

	protected String getValueForCSV(Double dataValue) {
		if(dataValue == null) return "";
		return String.valueOf(dataValue);
	}

	protected String getTemporaryDiretory(String module, boolean isImport) {
		SimpleDateFormat fileDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String tempPath = appConfig.getDefaultExportImportPath() + "/tmp/";
		tempPath = tempPath 
				+ File.separator 
				+ (isImport ? "importdata" : "exportdata")
				+ File.separator + module + File.separator + fileDateFormat.format(new Date()) 
				+ File.separator;
		try {
			File fl =  new File(tempPath);
			if(!fl.exists()) {
				fl.mkdirs();
			}
		} catch (Exception e) {
			log.error("Error to write dir file: {} {}", tempPath, e.getMessage());
		}
		return tempPath;
	}

	@Override
	public void generateErrors(List<CSVError> csvErrors, String column, String reason, int rowNumber) {
		csvErrors.add(new CSVError().setLineNo(rowNumber)
									.setColumn(column)
									.setReason(reason));
	}

	@Override
	public <E extends Enum<E> & GenericImportExportColumns> String getHeader(Class<E> clazz) {
		StringBuilder header = new StringBuilder();

		for (E enumFields : clazz.getEnumConstants()) {
			header.append(enumFields.getColumnName() + ",");
		}

		int lastComma = header.toString().lastIndexOf(',');
		return header.toString().substring(0, lastComma);
	}

	@Override
	public <E extends Enum<E> & GenericImportExportColumns> List<ImportExportModuleColumn> getModuleColumns(Class<E> clazz){
		List<ImportExportModuleColumn> eimcList = new ArrayList<>();
		for (E enumFields : clazz.getEnumConstants()) {
			ImportExportModuleColumn iemc = new ImportExportModuleColumn();
			iemc.setColumnIndex(enumFields.getColumnIndex());
			iemc.setColumnName(enumFields.getColumnName());
			iemc.setColumn(enumFields.getColumn());
			iemc.setColumnType(enumFields.getIect().getCode());
			iemc.setCssClass(enumFields.getIect().getCssClass());
			iemc.setColumnDescription(enumFields.getColumnDescription());
			eimcList.add(iemc);
		}
		return eimcList;
	}

	/**
	 * Check CSV record has column and return the value 
	 * @param csvRecord
	 * @param totalNumberOfColumn
	 * @param limit
	 * @return 
	 */
	protected String getRecordValue(CSVRecord csvRecord, long totalNumberOfColumn, int limit) {
		return totalNumberOfColumn > limit ? csvRecord.get(limit) : "";
	}

	protected boolean isDownloadAll(ModuleOption downloadOption) {
		if(downloadOption == null) return false;
		return "downloadAll".equals(downloadOption.getName());
	}

	protected String getFileName(String name, String extension) {
		name = StringUtils.isBlank(name) ? "ajuerp" : name;
		return UUID.randomUUID() + "_" + name + "_" + DATE_FORMATER_2.format(new java.util.Date()) + "." + extension;
	}

	protected StringBuilder getCSVHeaders(List<String> headerLines) {
		StringBuilder csvLines = new StringBuilder();
		csvLines.append(String.join(",", headerLines)).append(LINE_BREAK);
		return csvLines;
	}
}
