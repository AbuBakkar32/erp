package com.asl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asl.entity.Opordheader;
import com.asl.entity.Zbusiness;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ProductionSuggestion;
import com.asl.model.report.ProductionPlanningsReport;
import com.asl.model.report.RawMaterialItems;
import com.asl.model.report.SalesOrderChalan;
import com.asl.model.report.Suggestion;
import com.asl.service.OpordService;
import com.asl.service.ProductionSuggestionService;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Controller
@RequestMapping("/production/suggestion")
public class ProductionSuggestionController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private ProductionSuggestionService productionSuggestionService;

	@GetMapping
	public String loadSuggestion(@RequestParam(required = false) String xordernum, Model model) {
		List<Opordheader> allChalans = new ArrayList<>();
		Opordheader chalan = null;
		if(StringUtils.isBlank(xordernum)) {
			allChalans.addAll(opordService.findAllOpordHeaderByXtypetrnAndXtrnAndXdate(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), new Date()));
		} else {
			chalan = opordService.findOpordHeaderByXordernum(xordernum);
			if(chalan != null) allChalans.add(chalan);
		}

		Map<String, List<ProductionSuggestion>> allSuggestions = new TreeMap<>();
		Map<String, Map<String, BigDecimal>> totalMap = new HashMap<>();
		for(Opordheader c : allChalans) {
			if(c == null) continue;
			List<ProductionSuggestion> list = productionSuggestionService.getProductionSuggestion(c.getXordernum(), c.getXdate());

			allSuggestions.put(c.getXordernum(), list);

			Map<String, BigDecimal> m = new TreeMap<>();
			for(ProductionSuggestion p : list) {
				if(m.get(p.getXrawitem() + " - " + p.getXrawdes() + " (" + p.getXrawunit() +" )") != null) {
					BigDecimal qty = p.getXrawqty() != null ? p.getXrawqty() : BigDecimal.ZERO;
					m.put(p.getXrawitem() + " - " + p.getXrawdes() + " (" + p.getXrawunit() +" )", m.get(p.getXrawitem() + " - " + p.getXrawdes() + " (" + p.getXrawunit() +" )").add(qty));
				} else {
					m.put(p.getXrawitem() + " - " + p.getXrawdes() + " (" + p.getXrawunit() +" )", p.getXrawqty() != null ? p.getXrawqty() : BigDecimal.ZERO);
				}
			}
			totalMap.put(c.getXordernum(), m);
		}

		allSuggestions.entrySet().stream().forEach(s -> {
			s.getValue().sort(Comparator.comparing(ProductionSuggestion::getXitem));
		});

		model.addAttribute("allSuggestions", allSuggestions);
		model.addAttribute("totalMap", totalMap);
		return "pages/production/suggestion/suggestion";
	}

	@GetMapping("/create/{xordernum}")
	public String createSuggestionAndLoadSuggestionPage(@PathVariable String xordernum, Model model) {
		Opordheader opordHeader = opordService.findOpordHeaderByXordernum(xordernum);
		if(opordHeader == null) return "redirect:/production/suggestion";

		// delete suggestion table where xordernum
		productionSuggestionService.deleteSuggestion(xordernum, opordHeader.getXdate());

		// create suggestion
		productionSuggestionService.createSuggestion(xordernum);

		// update chalan status
		opordHeader.setSuggestionCreated(true);
		opordService.updateOpordHeader(opordHeader);

		return "redirect:/production/suggestion?xordernum=" + xordernum;
	}

	@GetMapping("/print/{xordernum}")
	public ResponseEntity<byte[]> printChalan(@PathVariable String xordernum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			message = "Chalan not found to do print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<ProductionSuggestion> details = productionSuggestionService.getProductionSuggestion(oh.getXordernum(), oh.getXdate());
		if(details == null || details.isEmpty()) {
			message = "Suggestion not found for this chalan";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		Zbusiness zb = sessionManager.getZbusiness();
		ProductionPlanningsReport report = new ProductionPlanningsReport();
		report.setBusinessName(zb.getZorg());
		report.setBusinessAddress(zb.getXmadd());
		report.setReportName("Production Planning of Chalan : " + oh.getXordernum());
		report.setFromDate(sdf.format(oh.getXdate()));
		report.setToDate(sdf.format(oh.getXdate()));
		report.setPrintDate(sdf.format(new Date()));

		List<SalesOrderChalan> chalans = new ArrayList<>();
		SalesOrderChalan chalan = new SalesOrderChalan();
		chalan.setChalanName(oh.getXordernum());
		chalan.setChalanDate(sdf.format(oh.getXdate()));
		chalan.setStatus(oh.getXstatus());
		chalans.add(chalan);

		List<Suggestion> suggestions = new ArrayList<>();
		details.stream().forEach(d -> {
			Suggestion s = new Suggestion();
			s.setXitem(d.getXitem());
			s.setXrawmaterial(d.getXrawitem());
			s.setProductionItem(d.getXitem() + " - " + d.getXitemdes());
			s.setProductionItemQty(d.getXqtyord() != null ? d.getXqtyord().toString() : BigDecimal.ZERO.toString());
			s.setProductionItemUnit(d.getXitemunit());
			s.setRawMaterial(d.getXrawitem() + " - " + d.getXrawdes());
			s.setRawMaterialQty(d.getXrawqty() != null ? d.getXrawqty().toString() : BigDecimal.ZERO.toString());
			s.setRawMaterialUnit(d.getXrawunit());
			suggestions.add(s);
		});
		suggestions.sort(Comparator.comparing(Suggestion::getProductionItem));
		chalan.getSuggestions().addAll(suggestions);
		report.getChalans().addAll(chalans);

		Map<String, RawMaterialItems> rawitems = new HashMap<>();
		for(ProductionSuggestion s : details) {
			if(rawitems.get(s.getXrawitem()) != null) {
				RawMaterialItems rm = rawitems.get(s.getXrawitem());
				rm.setXqtyl(rm.getXqtyl().add(s.getXrawqty()));
			} else {
				RawMaterialItems rm = new RawMaterialItems();
				rm.setXitem(s.getXrawitem() + " - " + s.getXrawdes() + " (" + s.getXrawunit() + ")");
				rm.setXqtyl(s.getXrawqty());
				rawitems.put(s.getXrawitem(), rm);
			}
		}

		List<RawMaterialItems> rmList = new ArrayList<>();
		rawitems.entrySet().stream().forEach(m -> rmList.add(m.getValue()));
		rmList.sort(Comparator.comparing(RawMaterialItems::getXitem));
		chalan.getRawItems().addAll(rmList);
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());

		byte[] byt = getPDFByte(report, "productionplanningofchalanreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for chalan";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}
}
