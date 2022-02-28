package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Caitem;
import com.asl.entity.Imtrn;
import com.asl.entity.ProjectStoreView;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CaitemService;
import com.asl.service.ImtrnService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/inventory/openingentry")
public class StockOpeningEntryController extends ASLAbstractController {

	@Autowired private ImtrnService imtrnService;
	@Autowired private XcodesService xcodeService;
	@Autowired private CaitemService caitemService;
	@Autowired private ProjectStoreViewService projectstoreviewService;


	@GetMapping
	public String loadStockOpeningEntryPage(Model model) {
		model.addAttribute("imtrn", getDefaultImtrn());
		model.addAttribute("allImtrn", imtrnService.getAllImtrn());
		model.addAttribute("imtrnprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.INVENTORY_TRANSACTION2.getCode(),TransactionCodeType.INVENTORY_TRANSACTION2.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("allcodes", Collections.emptyList());
		
		return "pages/inventory/openingentry/imtrn";
	}

	@GetMapping("/{ximtrnnum}")
	public String loadStockOpeningEntryPage(@PathVariable String ximtrnnum, Model model) {
		Imtrn data = imtrnService.findImtrnByXimtrnnum(ximtrnnum);
		if(data == null) data = getDefaultImtrn();

		model.addAttribute("imtrn", data);
		model.addAttribute("allImtrn", imtrnService.getAllImtrn());
		model.addAttribute("imtrnprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.INVENTORY_TRANSACTION2.getCode(),TransactionCodeType.INVENTORY_TRANSACTION2.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
		return "pages/inventory/openingentry/imtrn";
	}

	private Imtrn getDefaultImtrn() {
		Imtrn imtrn = new Imtrn();
		imtrn.setXtype(TransactionCodeType.INVENTORY_TRANSACTION2.getCode());
		imtrn.setXtrn(TransactionCodeType.INVENTORY_TRANSACTION2.getdefaultCode());
		imtrn.setXqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		imtrn.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		imtrn.setXval(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		imtrn.setXunit("");
		imtrn.setXnote("Inventory Upload");
		imtrn.setXvalpost(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		imtrn.setXcqtyuse(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		imtrn.setXrateavg(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		return imtrn;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Imtrn imtrn, BindingResult bindingResult){
		if(imtrn == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if item is empty
		if(StringUtils.isBlank(imtrn.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an item first");
			return responseHelper.getResponse();
		}

		if(imtrn.getXqty().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please insert valid item quantity");
			return responseHelper.getResponse();
		}

		if(imtrn.getXqty().compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("Item quantity should be greater then 0");
			return responseHelper.getResponse();
		}
		
		if(imtrn.getXrate() == null) imtrn.setXrate(BigDecimal.ZERO);
		if(imtrn.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please insert valid rate");
			return responseHelper.getResponse();
		}
		if(imtrn.getXrate().compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("Cost rate should be greater then 0");
			return responseHelper.getResponse();
		}

		// if existing record
		Imtrn existImtrn = imtrnService.findImtrnByXimtrnnum(imtrn.getXimtrnnum());
		if(existImtrn != null) {
			BeanUtils.copyProperties(imtrn, existImtrn, "xtype","xtrn","xitem","xsign");
			long count = imtrnService.update(existImtrn);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Opening Entry updated successfully");
			responseHelper.setRedirectUrl("/inventory/openingentry/" + imtrn.getXimtrnnum());
			return responseHelper.getResponse();
		}

		// If new
		imtrn.setXsign(1);
		imtrn.setXdocrow(0);
		imtrn.setXstatusjv("Open");
		long count = imtrnService.save(imtrn);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Opening Entry created successfully");
		responseHelper.setRedirectUrl("/inventory/openingentry/" + imtrn.getXimtrnnum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{ximtrnnum}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String ximtrnnum){
		Imtrn imtrn = imtrnService.findImtrnByXimtrnnum(ximtrnnum);
		if(imtrn == null) {
			responseHelper.setErrorStatusAndMessage("Can't find opening entry : " + ximtrnnum);
			return responseHelper.getResponse();
		}

		long count = imtrnService.deleteByXimtrnnum(ximtrnnum);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete opening entry : " + ximtrnnum);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Opening Entry deleted successfully");
		responseHelper.setRedirectUrl("/inventory/openingentry/");
		return responseHelper.getResponse();
	}

	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}
	
	@GetMapping("/allcodesbyproject/{xhwh}")
	public @ResponseBody List<ProjectStoreView> getProjectstoreview(@PathVariable String xhwh){
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(xhwh);
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		return list;
	}
}
