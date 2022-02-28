package com.asl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.Moheader;
import com.asl.entity.Opordheader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.service.ImtorService;
import com.asl.service.MoService;
import com.asl.service.OpordService;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since May 4, 2021
 */
@Controller
@RequestMapping("/inventory/pchtotrord")
public class ProdChalanToTransferOrder extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private MoService moService;
	@Autowired private XcodesService xcodeService;
	@Autowired private ImtorService imtorService;

	@GetMapping
	public String loadPage(Model model) {
		List<Opordheader> allheaders = opordService.findAllProductionCompletedChalan(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode());
		model.addAttribute("salesorderchalanList", allheaders);
		return "pages/inventory/pchtotrord/pchtotrord";
	}

	@GetMapping("/createto/{chalan}/show")
	public String loadModal(@PathVariable String chalan, Model model) {
		ImtorHeader imtorHeader = new ImtorHeader();
		imtorHeader.setXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode());
		imtorHeader.setXtrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getdefaultCode());
		imtorHeader.setXstatustor("Open");
		imtorHeader.setXdate(new Date());
		imtorHeader.setXchalanref(chalan);
		model.addAttribute("imtorheader", imtorHeader);
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		return "pages/inventory/pchtotrord/pchtotrordmodal::pchtotrordmodal";
	}

	@PostMapping("/createto")
	public @ResponseBody Map<String, Object> createTransferOrder(ImtorHeader imtorHeader, Model model){

		List<Moheader> batches = moService.findMoheaderByXchalan(imtorHeader.getXchalanref());
		if(batches == null || batches.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Batch not found in this system for chalan : " + imtorHeader.getXchalanref());
			return responseHelper.getResponse();
		}

		// validation
		if(imtorHeader.getXdate() == null) imtorHeader.setXdate(new Date());
		if(imtorHeader.getXfwh().equalsIgnoreCase(imtorHeader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Transfer Order can't be create for same warehouse");
			return responseHelper.getResponse();
		}
		if(imtorService.findImtorHeaderByXchalanref(imtorHeader.getXchalanref()) != null) {
			responseHelper.setErrorStatusAndMessage("Transfer Order already created for this chalan : " + imtorHeader.getXchalanref());
			return responseHelper.getResponse();
		}

		// create transfer order first
		long count = imtorService.save(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create Transfer Order");
			return responseHelper.getResponse();
		}

		imtorHeader = imtorService.findImtorHeaderByXchalanref(imtorHeader.getXchalanref()).stream().findFirst().orElse(null);

		// Update chalan with transfer order reference
		Opordheader oh = opordService.findOpordHeaderByXordernum(imtorHeader.getXchalanref());
		oh.setXtornum(imtorHeader.getXtornum());
		long ohcount = opordService.updateOpordHeader(oh);
		if(ohcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update chalan with transfer order reference");
			return responseHelper.getResponse();
		}

		// now crate transfer details from batch
		List<ImtorDetail> toDetails = new ArrayList<>();
		for(Moheader b : batches) {
			ImtorDetail id = new ImtorDetail();
			id.setXtornum(imtorHeader.getXtornum());
			id.setXchalanref(imtorHeader.getXchalanref());
			id.setXitem(b.getXitem());
			id.setXqtyord(b.getXqtycom() != null ? b.getXqtycom() : BigDecimal.ZERO);
			id.setXunit(b.getXqtyprdunit());
			id.setXrate(BigDecimal.ZERO);
			toDetails.add(id);
		}

		// save all details
		try {
			long c = imtorService.saveDetail(toDetails);
			if(c == 0) {
				responseHelper.setErrorStatusAndMessage("Can't add transfer details");
				return responseHelper.getResponse();
			}
		} catch (ServiceException e) {
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Transfer order created successfully");
		responseHelper.setRedirectUrl("/inventory/pchtotrord");
		return responseHelper.getResponse();
	}
}
