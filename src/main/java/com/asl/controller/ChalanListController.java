package com.asl.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Caitem;
import com.asl.entity.Immofgdetail;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.Moheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.Zbusiness;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ProductionSuggestion;
import com.asl.model.ServiceException;
import com.asl.model.report.AllSalesOrderChalanReport;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesOrderChalan;
import com.asl.service.CaitemService;
import com.asl.service.ImmofgdetailService;
import com.asl.service.ImtorService;
import com.asl.service.MoService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;
import com.asl.service.ProductionSuggestionService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Mar 9, 2021
 */
@Slf4j
@Controller
@RequestMapping("/salesninvoice/chalanlist")
public class ChalanListController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private ImmofgdetailService immofgdetailService;
	@Autowired private MoService moService;
	@Autowired private OpdoService opdoService;
	@Autowired private CaitemService caitemService;
	@Autowired private ImtorService imtorService;
	@Autowired private ProductionSuggestionService productionSuggestionService;

	@GetMapping
	public String loadSalesOrderChalanPage(Model model) {
		model.addAttribute("productioncompleted", false);
		model.addAttribute("salesorderchalan", getDefaultOpordheader());
		model.addAttribute("salesorderchalanprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("salesorderchalanList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode()));
		return "pages/salesninvoice/salesorderchalan/chalanlist";
	}

	@GetMapping("/{xordernum}")
	public String loadSalesOrderChalanPage(@PathVariable String xordernum, Model model) {
		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) return "redirect:/salesninvoice/chalanlist";

		model.addAttribute("productioncompleted", moService.isProductionProcessCompleted(oh.getXordernum()));
		model.addAttribute("salesorderchalan", oh);
		model.addAttribute("salesorderchalanprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("salesorderchalanList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode()));

		List<Opordheader> allOpenAndConfirmesSalesOrders = new ArrayList<>();
		if("Open".equalsIgnoreCase(oh.getXstatus())) allOpenAndConfirmesSalesOrders.addAll(opordService.findAllSalesOrder(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), "Open", new Date()));
		allOpenAndConfirmesSalesOrders.addAll(opordService.findAllSalesOrderByChalan(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), xordernum));
		model.addAttribute("opensalesorders", allOpenAndConfirmesSalesOrders);
		model.addAttribute("chalandetails", opordService.findOporddetailByXordernum(xordernum));
		return "pages/salesninvoice/salesorderchalan/chalanlist";
	}

	private Opordheader getDefaultOpordheader() {
		Opordheader oh = new Opordheader();
		oh.setXtypetrn(TransactionCodeType.CHALAN_NUMBER.getCode());
		oh.setXdate(new Date());
		oh.setXstatus("Open");
		return oh;
	}

	@GetMapping("/ordreqdetails/{xordernum}/show")
	public String displayItemDetailsOfOrderRequistion(@PathVariable String xordernum, Model model) {
		model.addAttribute("oporddetailsList", opordService.findOporddetailByXordernum(xordernum));
		return "pages/salesninvoice/salesorderchalan/salesorderdetailmodal::salesorderdetailmodal";
	}

	@GetMapping("/chalandetail/{xordernum}")
	public String reloadChalanDetailSection(@PathVariable String xordernum, Model model) {
		model.addAttribute("salesorderchalan", opordService.findOpordHeaderByXordernum(xordernum));
		model.addAttribute("chalandetails", opordService.findOporddetailByXordernum(xordernum));
		return "pages/salesninvoice/salesorderchalan/salesorderchalan::salesorderchalandetailtable";
	}


}

