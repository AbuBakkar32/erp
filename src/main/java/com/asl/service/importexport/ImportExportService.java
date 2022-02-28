package com.asl.service.importexport;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.asl.model.AsyncCSVResult;
import com.asl.model.CSVError;
import com.asl.model.ImportExportModuleColumn;

/**
 * @author Zubayer Ahamed
 *
 */
@Component
public interface ImportExportService {

	public String showExportImportPage(Model model);

	public void downloadTemplate(AsyncCSVResult asyncCSVResult) throws IOException;

	public void retreiveData(AsyncCSVResult asyncCSVResult) throws IOException;

	public void processCSV(AsyncCSVResult asyncCSVResult);

	public void importCSV(AsyncCSVResult asyncCSVResult);

	public <E extends Enum<E> & GenericImportExportColumns> String getHeader(Class<E> clazz);

	public String getHeader();

	public void generateErrors(List<CSVError> csvErrors, String column, String reason, int rowNumber);

	public <E extends Enum<E> & GenericImportExportColumns> List<ImportExportModuleColumn> getModuleColumns(Class<E> clazz);
}
