package com.asl.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.asl.model.AsyncCSVProcessor;
import com.asl.model.AsyncCSVResult;
import com.asl.model.ImportExportPage;
import com.asl.service.importexport.ImportExportService;
import com.opencsv.CSVWriter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jan 1, 2021
 */
@Slf4j
@Controller
@RequestMapping("/importexport")
public class ImportExportController extends ASLAbstractController {

	@Autowired AsyncCSVProcessor asyncCSVProcessor;

	@GetMapping("/{module}")
	public String loadImportExportPage(@PathVariable String module, Model model) {
		ImportExportService importExportService = getImportExportService(module);
		return importExportService.showExportImportPage(model);
	}

	@GetMapping("/downloadtemplate/{module}")
	public void downloadTemplate(@PathVariable String module, HttpServletResponse response) throws IOException {
		String token = UUID.randomUUID().toString();
		AsyncCSVResult asyncCSVResult = new AsyncCSVResult()
											.setHttpServletResponse(response)
											.setLatch(new CountDownLatch(1))
											.setToken(token)
											.setProgress(0)
											.setIsWorkInProgress(true)
											.setModuleName(module)
											.setBusinessId(sessionManager.getBusinessId())
											.setLoggedInUserDetail(sessionManager.getLoggedInUserDetails());

		ImportExportService importExportService = getImportExportService(module);
		importExportService.downloadTemplate(asyncCSVResult);
	}

	@PostMapping("/download/{module}")
	public @ResponseBody AsyncCSVResult downloadDataFromDB(@PathVariable String module, ImportExportPage exp) throws IOException {
		String token = UUID.randomUUID().toString();
		AsyncCSVResult asyncCSVResult = new AsyncCSVResult()
											.setLatch(new CountDownLatch(1))
											.setToken(token)
											.setProgress(0)
											.setIsWorkInProgress(true)
											.setModuleName(module)
											.setBusinessId(sessionManager.getBusinessId())
											.setLoggedInUserDetail(sessionManager.getLoggedInUserDetails())
											.setExportOptions(exp.getDownloadOption());

		ImportExportService importExportService = getImportExportService(module);

		asyncCSVProcessor.exportData(asyncCSVResult, importExportService);
		sessionManager.addToMap(token, asyncCSVResult);

		return asyncCSVResult;
	}

	@PostMapping("/{module}/exportfinal/{token}")
	public ResponseEntity<Resource> exportFinal(@PathVariable String module, @PathVariable("token") String token){
		
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
		ImportExportService service = getImportExportService(module);
		List<String[]> fileData = new ArrayList<>();
		if(service == null) {
			returnFile(response, module + FILE_SUFFIX, fileData);
			return null;
		}

		String header = service.getHeader();
		fileData.add(header.split(","));

		AsyncCSVResult asyncCSVResult = null;
		try {
			asyncCSVResult = (AsyncCSVResult) sessionManager.getFromMap(token);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
		}

		sessionManager.removeFromMap(token);
		if(asyncCSVResult == null) {
			returnFile(response, module + FILE_SUFFIX, fileData);
			return null;
		}

		final HttpHeaders headers = new HttpHeaders();
		MediaType mType = asyncCSVResult.getMimeType();
		headers.setContentType(mType);
		headers.add("Content-Disposition", "attachment; filename=" + asyncCSVResult.getFileName());
		headers.add("X-Content-Type-Options", "nosniff");

		File file = new File(asyncCSVResult.getDataFileName());
		if(!file.exists()) {
			returnFile(response, module + FILE_SUFFIX, fileData);
			return null;
		}

		InputStreamResource resource = null;
		try {
			resource = new InputStreamResource(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			log.error("File not found : {}", e.getMessage());
		}

		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(file.length())
				.contentType(mType)
				.body(resource);
	}

	private void returnFile(HttpServletResponse response, String fileName, List<String[]> fileData) {
		/* Set-up the headers to tell the browser we are downloading a CSV file */
		response.setContentType(CONTENT_TYPE);
		response.setCharacterEncoding(UTF_CODE);
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
		/*Data*/
		try {
			CSVWriter writer = new CSVWriter(response.getWriter());
			writer.writeAll(fileData); //Close Writer
			writer.close();
		} catch (IOException e) {
			log.error("Unable to write CSV template, due to..." + e.getMessage(), e);
		}
	}

	@PostMapping("/upload/csv/{module}")
	public @ResponseBody AsyncCSVResult uploadAndProcessCSV(@PathVariable String module, @RequestParam MultipartFile file, @RequestParam(required = false) boolean ignoreHeading, @RequestParam(required = false) boolean updateExisting, char delimeterType) {
		// Upload file and get file name
		String csvFilenameWithLoation = null;
		try {
			csvFilenameWithLoation = uploadCSVFile(file, module);
		} catch (Exception e) {
			log.error("Error is : {} - {}", e.getMessage(), e);
		}
		if(StringUtils.isBlank(csvFilenameWithLoation)) {
			return new AsyncCSVResult().setError("Can't upload csv file");
		}

		String token = UUID.randomUUID().toString();
		AsyncCSVResult asyncCSVResult = new AsyncCSVResult()
											.setUpdateExisting(updateExisting)
											.setIgnoreHeading(ignoreHeading)
											.setDelimeterType(delimeterType)
											.setLatch(new CountDownLatch(1))
											.setToken(token)
											.setProgress(0)
											.setIsWorkInProgress(true)
											.setFileName(file.getOriginalFilename() + " - " + file.getSize() + "kb")
											.setUploadedFileLocation(csvFilenameWithLoation)
											.setModuleName(module)
											.setBusinessId(sessionManager.getBusinessId())
											.setLoggedInUserDetail(sessionManager.getLoggedInUserDetails());

		ImportExportService importExportService = getImportExportService(module);

		asyncCSVProcessor.processDataFromCSV(asyncCSVResult, importExportService);
		sessionManager.addToMap(token, asyncCSVResult);

		return asyncCSVResult;
	}

	@GetMapping("/progress/status/{token}")
	public @ResponseBody AsyncCSVResult checkProgressStatus(@PathVariable String token){
		AsyncCSVResult asyncCSVResult = (AsyncCSVResult) sessionManager.getFromMap(token);
		if(asyncCSVResult.getLatch().getCount() == 0) {
			asyncCSVResult.setIsWorkInProgress(false);
			asyncCSVResult.setProgress(100);
			sessionManager.removeFromMap(token);
		}
		return asyncCSVResult;
	}

	@GetMapping("/exportprogress/status/{token}")
	public @ResponseBody AsyncCSVResult checkExportProgressStatus(@PathVariable String token){
		AsyncCSVResult asyncCSVResult = (AsyncCSVResult) sessionManager.getFromMap(token);
		if(asyncCSVResult.getLatch().getCount() == 0) {
			asyncCSVResult.setIsWorkInProgress(false);
			asyncCSVResult.setProgress(100);
		}
		return asyncCSVResult;
	}

	@PostMapping("/import/csv/{module}")
	public @ResponseBody AsyncCSVResult importProcessedCSVData(@PathVariable String module, @RequestParam String fileName) {
		String token = UUID.randomUUID().toString();
		AsyncCSVResult asyncCSVResult = new AsyncCSVResult()
											.setLatch(new CountDownLatch(1))
											.setToken(token)
											.setProgress(0)
											.setIsWorkInProgress(true)
											.setFileLocationToImportData(fileName)
											.setModuleName(module)
											.setBusinessId(sessionManager.getBusinessId())
											.setLoggedInUserDetail(sessionManager.getLoggedInUserDetails());

		ImportExportService importExportService = getImportExportService(module);
		asyncCSVProcessor.importDataFromCSV(asyncCSVResult, importExportService);
		sessionManager.addToMap(token, asyncCSVResult);

		return asyncCSVResult;
	}

	private String uploadCSVFile(MultipartFile csvFile, String module) {
		if(csvFile == null || StringUtils.isEmpty(csvFile.getOriginalFilename())) return null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String fileType = getFileExtention(csvFile);
		String tmpDirectory = appConfig.getDefaultExportImportPath() + File.separator + module + File.separator + sdf.format(new Date()) + File.separator;

		File file = new File(tmpDirectory);
		if(!file.exists()) file.mkdirs();
		UUID fileID = UUID.randomUUID();
		String csvFilename = tmpDirectory + fileID + fileType;
		try (OutputStream out = new FileOutputStream(new File(csvFilename))) {
			IOUtils.copy(csvFile.getInputStream(), out);
		} catch (IOException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		return csvFilename;
	}

	private String getFileExtention(MultipartFile csvFile) {
		if(csvFile == null || StringUtils.isBlank(csvFile.getOriginalFilename())) return null;
		int indx = csvFile.getOriginalFilename().indexOf(".csv");
		return csvFile.getOriginalFilename().substring(indx);
	}
}
