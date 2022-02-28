package com.asl.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.POSPromotion;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.POSPromotionService;


@Controller
@RequestMapping("/promotion")
public class POSPromotionController extends ASLAbstractController{
	
	@Autowired
	private POSPromotionService pOSPromotionService;

	
	@GetMapping
	public String loadPromotionPage(Model model) {
		POSPromotion pOSPromotion = new POSPromotion();
		pOSPromotion.setXdisc(BigDecimal.ZERO);
		pOSPromotion.setNewdata(true);

		model.addAttribute("promotions", pOSPromotion);
		model.addAttribute("allPromotions", pOSPromotionService.getAllPOSPromotion());
		
		model.addAttribute("discountTypes", xcodesService.findByXtype(CodeType.DISCOUNT_TYPE.getCode(), Boolean.TRUE));
		
		return"pages/promotion";
	}
	
	@GetMapping("/{xpromono}")
	public String loadRolePage(@PathVariable String xpromono, Model model) {
		POSPromotion pOSPromotion = pOSPromotionService.findByPOSPromotion(xpromono);
		if(pOSPromotion == null) return "redirect:/role";

		pOSPromotion.setNewdata(false);
		model.addAttribute("promotions", pOSPromotion);
		model.addAttribute("allPromotions", pOSPromotionService.getAllPOSPromotion());
		model.addAttribute("discountTypes", xcodesService.findByXtype(CodeType.DISCOUNT_TYPE.getCode(), Boolean.TRUE));
		return "pages/promotion";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(POSPromotion pOSPromotion, BindingResult bindingResult){
		if(pOSPromotion == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		} 

		if(StringUtils.isBlank(pOSPromotion.getXpromono())) {
			responseHelper.setErrorStatusAndMessage("Promotion Name required");
			return responseHelper.getResponse();
		}

		// modify role  name
		String xr = pOSPromotion.getXpromono().trim();
		xr = xr.toUpperCase();
		xr = xr.replace(" ", "_");
		pOSPromotion.setXpromono(xr);

		// check existing if want to save new role
		if(pOSPromotion.isNewdata()) {
			POSPromotion existingPromono = pOSPromotionService.findByPOSPromotion(pOSPromotion.getXpromono());
			if(existingPromono != null) {
				responseHelper.setErrorStatusAndMessage("Promotion name already exist");
				return responseHelper.getResponse();
			}
		}

		if (pOSPromotion.getXdisc().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("Discount should be Positive Value");
			return responseHelper.getResponse();
		}
		
		// save or update
		long count = pOSPromotion.isNewdata() ? pOSPromotionService.save(pOSPromotion) : pOSPromotionService.update(pOSPromotion);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Data "+ (pOSPromotion.isNewdata() ? "saved" : "updated") +" successfully");
		responseHelper.setRedirectUrl("/promotion/" + pOSPromotion.getXpromono());
		return responseHelper.getResponse();

	}


 
}
