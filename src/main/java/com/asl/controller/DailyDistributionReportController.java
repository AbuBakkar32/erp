package com.asl.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import com.asl.entity.DailyDistribution;
import com.asl.model.BranchesRequisitions;
import com.asl.model.report.BranchItem;
import com.asl.model.report.BranchRow;
import com.asl.model.report.MatrixReport;
import com.asl.model.report.MatrixReportData;
import com.asl.model.report.TableColumn;
import com.asl.model.report.Total;
import com.asl.service.DailyDistributionService;
import com.asl.service.OpdoService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;

/**
 * @author Zubayer Ahamed
 * @since May 9, 2021
 */
@Controller
@RequestMapping("/salesninvoice/ddisr")
public class DailyDistributionReportController extends ASLAbstractController {

	@Autowired private DailyDistributionService service;
	@Autowired private OpdoService opdoService;

	@GetMapping
	public ResponseEntity<byte[]> printChalan(HttpServletRequest request) throws JAXBException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException, ParseException, DocumentException {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		List<DailyDistribution> list = service.getDailyDistribution();
		if(list == null || list.isEmpty()) {
			message = "Data not found";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		list.stream().forEach(l ->{
			l.setTotalinventory(l.getOpening().add(l.getProduction()));
			l.setPhysicalstock(l.getTotalinventory().subtract(l.getDistribution()));
		});

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		com.asl.model.report.DailyDistributionReport report = new com.asl.model.report.DailyDistributionReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Daily Distribution Report (Inventory)");
		report.setFromDate(sdf.format(new Date()));
		report.setToDate(sdf.format(new Date()));
		report.setPrintDate(sdf.format(new Date()));
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());

		report.getDistributions().addAll(list);

		// Distribution matrix
		MatrixReport mr = new MatrixReport();
		mr.setBusinessName(sessionManager.getZbusiness().getZorg());
		mr.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		mr.setReportName("Branch Wise Item Distribution Report");
		mr.setFromDate(SDF2.format(new Date()));
		mr.setPrintDate(SDF2.format(new Date()));
		mr.setReportLogo(appConfig.getReportLogo());

		generateMatrixData2(new Date(), mr);


		List<ByteArrayOutputStream> streams = new ArrayList<>();
		ByteArrayOutputStream b1 = printingService.getPDFReportByteAttayOutputStream(report, getOnScreenReportTemplate("dailydistributionreport.xsl"), request); 
		ByteArrayOutputStream b2 = printingService.getPDFReportByteAttayOutputStream(mr, getOnScreenReportTemplate("matrixreport.xsl"), request);
		if(b1 != null) streams.add(b1);
		if(b2 != null) streams.add(b2);
		ByteArrayOutputStream baus = new ByteArrayOutputStream();

		Document document = new com.itextpdf.text.Document();
		PdfCopy copy = new PdfSmartCopy(document, baus);
		document.open();
		PdfReader reader = null;
		for (ByteArrayOutputStream byt : streams) {
			reader = new PdfReader(byt.toByteArray());
			copy.addDocument(reader);
			reader.close();
		}
		baus.flush();
		document.close();

		byte[] byt = baus.toByteArray();
		if(byt == null) {
			message = "Can't generate report";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

	private void generateMatrixData2(Date date, MatrixReport mr) {
		List<BranchesRequisitions> bqList = opdoService.getSalesInvoiceMatrxi(date);

		List<String> items = new ArrayList<>();
		List<String> branches = new ArrayList<>();
		List<TableColumn> columns = new ArrayList<>();
		for(BranchesRequisitions b : bqList) {
			if(!items.contains(b.getXitem())) {
				items.add(b.getXitem());
				TableColumn tc = new TableColumn();
				tc.setXitem(b.getXitem());
				tc.setXdesc(b.getXdesc());
				tc.setXunitpur(b.getXunitpur());
				columns.add(tc);
			}
			if(!branches.contains(b.getZorg())) {
				branches.add(b.getZorg());
			}
		}
		Collections.sort(items);
		columns.sort(Comparator.comparing(TableColumn::getXitem));

		Map<String, BranchItem> browMap = new TreeMap<>();
		for(BranchesRequisitions br : bqList) {
			if(browMap.get(br.getZorg() + "|" + br.getXitem()) != null) {
				BranchItem brow = browMap.get(br.getZorg() + "|" + br.getXitem());
				brow.setXqtyord(brow.getXqtyord().add(br.getXqtyord()));
			} else {
				BranchItem brow = new BranchItem();
				brow.setZorg(br.getZorg());
				brow.setXitem(br.getXitem());
				brow.setXqtyord(br.getXqtyord() != null ? br.getXqtyord() : BigDecimal.ZERO);
				browMap.put(br.getZorg() + "|" + br.getXitem(), brow);
			}
		}

		for(String item : items) {
			for(String branch : branches) {
				if(browMap.get(branch + "|" + item) == null) {
					BranchItem brow = new BranchItem();
					brow.setZorg(branch);
					brow.setXitem(item);
					brow.setXqtyord(BigDecimal.ZERO);
					browMap.put(branch + "|" + item, brow);
				}
			}
		}

		Map<String, Total> totalmap = new TreeMap<>();
		for(Map.Entry<String, BranchItem> m : browMap.entrySet()) {
			if(totalmap.get(m.getValue().getXitem()) != null) {
				Total total = totalmap.get(m.getValue().getXitem());
				total.setXqtyord(total.getXqtyord().add(m.getValue().getXqtyord()));
			} else {
				Total total = new Total();
				total.setXitem(m.getValue().getXitem());
				total.setXqtyord(m.getValue().getXqtyord());
				totalmap.put(m.getValue().getXitem(), total);
			}
		}

		Map<String, List<BranchItem>> branchWiseItems = new TreeMap<>();
		for(Map.Entry<String, BranchItem> m : browMap.entrySet()) {
			if(branchWiseItems.get(m.getValue().getZorg()) != null) {
				branchWiseItems.get(m.getValue().getZorg()).add(m.getValue());
			} else {
				List<BranchItem> list = new ArrayList<>();
				list.add(m.getValue());
				branchWiseItems.put(m.getValue().getZorg(), list);
			}
		}

		List<Total> totals = new ArrayList<>();
		totalmap.entrySet().stream().forEach(m -> totals.add(m.getValue()));
		totals.sort(Comparator.comparing(Total::getXitem));

		Map<String, BranchRow> browtracker = new HashMap<>();
		int chunk = 0;
		List<MatrixReportData> dataList = new ArrayList<>();
		MatrixReportData mrd = null;
		for(int i = 0; i < columns.size(); i++) {
			if(chunk == 9) {
				for(Map.Entry<String, BranchRow> m : browtracker.entrySet()) {
					mrd.getRows().add(m.getValue());
				}
				browtracker = new HashMap<>();
				chunk = 0;
			}
			if(chunk == 0) {
				mrd = new MatrixReportData();
				dataList.add(mrd);
			}
			mrd.getColumns().add(columns.get(i));
			mrd.getTotals().add(totals.get(i));

			for(int m = 0; m < branchWiseItems.size(); m++) {
				BranchItem item = branchWiseItems.get(branches.get(m)).get(i);
				BranchRow brow = null;
				if(browtracker.get(item.getZorg()) != null) {
					brow = browtracker.get(item.getZorg());
					brow.getItems().add(item);
					browtracker.put(item.getZorg(), brow);
				} else {
					brow = new BranchRow();
					brow.setZorg(item.getZorg());
					brow.getItems().add(item);
					browtracker.put(item.getZorg(), brow);
				}
			}

			chunk++;

			if(i == columns.size() - 1) {
				for(Map.Entry<String, BranchRow> m : browtracker.entrySet()) {
					mrd.getRows().add(m.getValue());
				}
				browtracker = new HashMap<>();
			}
		}

		mr.getDatas().addAll(dataList);
	}
}
