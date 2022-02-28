package com.asl.controller;

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

import com.asl.entity.Vatait;
import com.asl.enums.ResponseStatus;
import com.asl.service.VataitService;

@Controller
@RequestMapping("/mastersetup/vatait")
public class VataitController extends ASLAbstractController {
	
	@Autowired
	private VataitService vataitService;
	
	@GetMapping
	public String loadCaitemPage(Model model) {
		
		model.addAttribute("vatait", getDefaultVatait());
		//model.addAttribute("allVatait", new ArrayList<Vatait>());
		model.addAttribute("allVatait", vataitService.getAllVatait());
		
		return "pages/mastersetup/vatait/vatait";
	}

	@GetMapping("/{xvatait}")
	public String loadCaitemPage(@PathVariable String xvatait, Model model) {
		
		Vatait data = vataitService.findVataitByXvatait(xvatait); 
		if(data == null) data = getDefaultVatait();

		model.addAttribute("vatait", data);
		//model.addAttribute("allVatait", new ArrayList<Vatait>());
		model.addAttribute("allVatait", vataitService.getAllVatait());
		
		return "pages/mastersetup/vatait/vatait";
	}
	
	private Vatait getDefaultVatait() {
		Vatait vatait = new Vatait();
		//caitem.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		//caitem.setXminqty(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
		//caitem.setXtype(TransactionCodeType.ITEM_NUMBER.getCode());
		return vatait;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Vatait vatait, BindingResult bindingResult){
		if(vatait == null || StringUtils.isBlank(vatait.getXvatait())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// if existing record
		Vatait existVatait = vataitService.findVataitByXvatait(vatait.getXvatait());
		if(existVatait != null) {
			BeanUtils.copyProperties(vatait, existVatait, "xvatait");
			long count = vataitService.update(existVatait);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("VAT & Tax indentifier updated successfully");
			responseHelper.setRedirectUrl("/mastersetup/vatait/" + vatait.getXvatait());
			return responseHelper.getResponse();
		}

		// If new
		long count = vataitService.save(vatait);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("VAT & Tax indentifier created successfully");
		responseHelper.setRedirectUrl("/mastersetup/vatait/" + vatait.getXvatait());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/archive/{xvatait}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xvatait){
		return doArchiveOrRestore(xvatait, true);
	}

	@PostMapping("/restore/{xvatait}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xvatait){
		return doArchiveOrRestore(xvatait, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xvatait, boolean archive){
		Vatait vatait = vataitService.findVataitByXvatait(xvatait);
		if(vatait == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		vatait.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = vataitService.update(vatait);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Vat & Tax Indentifier updated successfully");
		responseHelper.setRedirectUrl("/mastersetup/vatait/" + vatait.getXvatait());
		return responseHelper.getResponse();
	}
	
	@GetMapping("/vataitdetail/{xvatait}")
	public @ResponseBody Vatait getVatait(@PathVariable String xvatait){
		
		Vatait vatait = vataitService.findVataitByXvatait(xvatait);
			
		return vatait;
	}

}
