package com.asl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.service.PogrnService;
import com.asl.service.PoordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Apr 24, 2021
 */
@Slf4j
@Controller
@RequestMapping("/procurement/potogrn")
public class PurchaseOrderToGrnController extends ASLAbstractController {

	@Autowired private PoordService poordService;
	@Autowired private PogrnService pogrnService;

	@GetMapping
	public String loadPoToGrnPage(Model model) {
		model.addAttribute("poordHeaders", poordService.getPoordHeadersByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode()));
		return "pages/procurement/potogrn/potogrn";
	}

	@PostMapping("/create/{xpornum}")
	public @ResponseBody Map<String, Object> creategrn(@PathVariable String xpornum){
		// Get PoordHeader record by Xpornum
		PoordHeader poordHeader = poordService.findPoordHeaderByXpornum(xpornum);
		if(poordHeader == null) {
			responseHelper.setErrorStatusAndMessage("Purchase Order not found in the system");
			return responseHelper.getResponse();
		}

		//Get PO items to copy them in GRN.
		List<PoordDetail> poordDetailList = poordService.findPoorddetailByXpornum(xpornum);
		if(poordDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This purchase order has no item");
			return responseHelper.getResponse();
		}

		PogrnHeader pogrnHeader = new PogrnHeader();
		BeanUtils.copyProperties(poordHeader, pogrnHeader, "xdate", "xtype", "xtrngrn", "xnote", "xtypetrn", "xgrnnum");
		pogrnHeader.setXpornum(xpornum);
		pogrnHeader.setXstatusgrn("Open");
		pogrnHeader.setXdate(new Date());
		pogrnHeader.setXtype(TransactionCodeType.GRN_NUMBER.getCode());
		pogrnHeader.setXvatamt(BigDecimal.ZERO);
		pogrnHeader.setXaitamt(BigDecimal.ZERO);
		pogrnHeader.setXdiscprime(BigDecimal.ZERO);
		BigDecimal grandTotal = pogrnHeader.getXtotamt().add(pogrnHeader.getXvatamt()).add(pogrnHeader.getXaitamt()).subtract(pogrnHeader.getXdiscprime());
		pogrnHeader.setXgrandtot(grandTotal);
		pogrnHeader.setXvatait("No Vat");

		long count = pogrnService.save(pogrnHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create GRN for purchase order : " + xpornum);
			return responseHelper.getResponse();
		}

		// Now find grn header which is saved now
		//pogrnHeader = pogrnService.findPogrnHeaderByXpornum(xpornum);

		List<PogrnDetail> grnDetails = new ArrayList<>();
		for(int i=0; i< poordDetailList.size(); i++) {
			PogrnDetail pogrnDetail = new PogrnDetail();
			//Copying PO items to GRN items.
			BeanUtils.copyProperties(poordDetailList.get(i), pogrnDetail, "xrow", "xnote");
			pogrnDetail.setXgrnnum(pogrnHeader.getXgrnnum());
			pogrnDetail.setXqtygrn(poordDetailList.get(i).getXqtyord());
			grnDetails.add(pogrnDetail);
		}

		// Save all grn detail
		try {
			if(pogrnService.saveDetails(grnDetails) == 0) {
				responseHelper.setErrorStatusAndMessage("Grn created. But PO details not added to GRN");
				return responseHelper.getResponse();
			}
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		//Update PoordHeader Status and add grn reference
		poordHeader.setXstatuspor("Confirmed");
		poordHeader.setXgrnnum(pogrnHeader.getXgrnnum());
		long pCount = poordService.update(poordHeader);
		if(pCount == 0) {
			responseHelper.setErrorStatusAndMessage("Purchase order status not updated after creating GRN");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("GRN created successfully");
		responseHelper.setRedirectUrl("/procurement/potogrn");
		return responseHelper.getResponse();
	}

	@GetMapping("/poorddetails/{xpornum}/show")
	public String loadItemDetailsModal(@PathVariable String xpornum, Model model) {
		model.addAttribute("poorddetailsList", poordService.findPoorddetailByXpornum(xpornum));
		return "pages/procurement/potogrn/purchaseorderdetailmodal::purchaseorderdetailmodal";
	}
}
