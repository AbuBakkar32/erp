package com.asl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Zubayer Ahamed
 *
 */
@Data
@Accessors(chain = true)
public class AsyncCSVResult {

	private String token;
	private Integer progress;
	private Boolean isWorkInProgress;
	private HttpServletResponse httpServletResponse;

	private String fileName;
	private String uploadedFileSize;
	private String uploadedFileLocation;
	private String fileLocationToImportData;

	private int totalNumberOfRecords;
	private int numberOfCreateRecord;
	private int numberOfUpdateRecord;
	private int numberOfDeleteRecord;

	private CountDownLatch latch;
	private Map<String, Object> response;

	private boolean allOk = false;
	private String error;
	private List<CSVError> csvErrors;

	private boolean updateExisting;
	private boolean ignoreHeading;
	private char delimeterType;

	private String businessId;
	private String moduleName;
	private String audtiUser;
	private LoggedInUserDetails loggedInUserDetail;

	List<ModuleOption> exportOptions = new ArrayList<>();
	private MediaType mimeType;
	private String dataFileName;

	public List<CSVError> getCsvErrors(){
		if(this.csvErrors == null) this.csvErrors = new ArrayList<>();
		return this.csvErrors;
	}

	public Map<String, Object> getResponse(){
		if(this.response == null) response = new HashMap<>();
		return this.response;
	}
}
