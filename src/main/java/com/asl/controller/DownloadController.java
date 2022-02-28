package com.asl.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jun 22, 2021
 */
@Slf4j
@Controller
@RequestMapping("/download")
public class DownloadController extends ASLAbstractController {

	private static final byte[] RESOURCE_NOT_FOUND = "Sorry, we could not find the requested resource".getBytes();

	@Autowired ApplicationContext context;

	@GetMapping("/document/{filename}/{actualname}")
	public ResponseEntity<byte[]> downloadDocumentFile(@PathVariable String filename, @PathVariable String actualname) {

		String url = appConfig.getDocumentPath().concat("/").concat(filename);

		File file = new File(url);
		if (!file.exists()) {		// Check if file is exists 
			log.info("File: {} not found", url);
			return resourceNotFound();
		}

		InputStream in = null;
		byte[] bytes = null;
		try {
			in = context.getResource("file:" + url).getInputStream();
			bytes = IOUtils.toByteArray(in);
			in.close();
		} catch (IOException e) {
			log.error("Failed to download document. orginal error is: " + e);
			return resourceNotFound();
		}

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(getMediaType(url, bytes));
		String ext =  FilenameUtils.getExtension(file.getName());
		if(StringUtils.isBlank(ext)) ext = "txt";
		String headerValue = String.format("attachment; filename=\"%s\"", actualname);
		headers.add("Content-Disposition",  headerValue);
		return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
	}
	
	private ResponseEntity<byte[]> resourceNotFound() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.set("Refresh", "0; url=" + request.getContextPath() + "/download/resource_not_found");
		return new ResponseEntity<>(RESOURCE_NOT_FOUND, headers, HttpStatus.NOT_FOUND);
	}

	public MediaType getMediaType(String filePath, byte[] bytes) {
		MediaType mediaType = null;
		String mimeType = null;

		String ext = FilenameUtils.getExtension(filePath);
		if (ext.equalsIgnoreCase("pdp")) {
			mimeType = "application/x-pdpprint";
		} else if (ext.equalsIgnoreCase("epl")) {
			mimeType = "application/x-epl";
		} else if (ext.equalsIgnoreCase("epl2")) {
			mimeType = "application/x-epl2";
		} else if (ext.equalsIgnoreCase("zpl")) {
			mimeType = "application/x-zpl";
		} else if (ext.equalsIgnoreCase("csv")) {
			mimeType = "text/csv";
		} else if (ext.equalsIgnoreCase("pdf")) {
			mimeType = "application/pdf";
		} else if (ext.equalsIgnoreCase("html")) {
			mimeType = "text/html";
		} else if (ext.equalsIgnoreCase("eml")) {
			mimeType = "message/rfc822";
		} else if (ext.equalsIgnoreCase("txt")) {
			mimeType = "text/plain";
		}

		if (StringUtils.isBlank(mimeType)) {
			try {
				mimeType = detectMimeType(bytes, filePath);
			} catch (IOException e) {
				log.warn(e.getMessage());
			}
		}

		if (StringUtils.isBlank(mimeType)) mimeType = "application/octet-stream";

		if (mimeType.indexOf("/") > 0) {
			String part1 = mimeType.substring(0, mimeType.indexOf("/"));
			String part2 = mimeType.substring(mimeType.indexOf("/") + 1);
			mediaType = new MediaType(part1, part2);
		}

		if (mediaType == null) mediaType = MediaType.APPLICATION_OCTET_STREAM;

		return mediaType;
	}
	
	private String detectMimeType(byte[] bytes, String filenameWithExtension) throws IOException {
		TikaInputStream tikaIS = null;
		try {
			tikaIS = TikaInputStream.get(bytes);
			/*
			 * You might not want to provide the file's name. If you provide an Excel document with a .xls extension, it
			 * will get it correct right away; but if you provide an Excel document with .doc extension, it will guess
			 * it to be a Word document
			 */
			Metadata metadata = new Metadata();
			metadata.add(Metadata.RESOURCE_NAME_KEY, filenameWithExtension);
			return TikaConfig.getDefaultConfig().getDetector().detect(tikaIS, metadata).toString();
		} finally {
			if (tikaIS != null) tikaIS.close();
		}
	}
}
