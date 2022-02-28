package com.asl.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Zubayer Ahamed
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ImportExportPage {
	private String moduleName;
	private String pageTitle;

	private List<ImportExportModuleColumn> moduleColumns;

	private boolean showExportTab;
	private String exportTabPrompt;
	private boolean showImportTab;
	private String importTabPrompt;

	private boolean showFileDelimiter;
	private boolean showIgnoreFirstRow;
	private boolean showUpdateExistingRecord;
	private String updateExistingRecordPrompt;
	private boolean showDownloadAll;
	private String downloadAllPrompt;
	private boolean showNotes;
	private List<String> notes;

	private List<ModuleOption> downloadOption;
}
