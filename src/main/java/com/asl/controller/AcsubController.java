package com.asl.controller;

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

import com.asl.entity.Acsub;
import com.asl.service.AcsubService;

/**
 * @author Zubayer Ahamed
 * @since Jul 25, 2021
 */
@Controller
@RequestMapping("/account/acsub")
public class AcsubController extends ASLAbstractController {

	@Autowired private AcsubService acsubService;

	@GetMapping
	public String loadAcsubPage(Model model) {
		Acsub acsub = new Acsub();
		acsub.setNewData(true);
		model.addAttribute("acsub", acsub);
		model.addAttribute("acsublist", acsubService.getAll());
		return "pages/account/acsub/acsub";
	}

	@GetMapping("/{xacc}/{xsub}")
	public String loadAcsubPage(@PathVariable String xacc, @PathVariable String xsub, Model model) {
		Acsub acsub = acsubService.findByXaccAndXsub(xacc, xsub);
		if(acsub == null) return "redirect:/account/acsub";

		model.addAttribute("acsub", acsub);
		model.addAttribute("acsublist", acsubService.getAll());
		return "pages/account/acsub/acsub";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Acsub acsub){

		// if new
		if(acsub.isNewData()) {
			long count = acsubService.save(acsub);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save Sub account");
				return responseHelper.getResponse();
			}
			
			responseHelper.setSuccessStatusAndMessage("Sub Account created successfully");
			responseHelper.setRedirectUrl("/account/acsub/" + acsub.getXacc() + "/" + acsub.getXsub());
			return responseHelper.getResponse();
		}

		// if exist
		Acsub exist = acsubService.findByXaccAndXsub(acsub.getXacc(), acsub.getXsub());
		if(exist == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Sub account to do update");
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(acsub, exist, "xacc", "xsub");
		long count = acsubService.update(exist);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update Sub account");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Sub Account updated successfully");
		responseHelper.setRedirectUrl("/account/acsub/" + exist.getXacc() + "/" + exist.getXsub());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xacc}/{xsub}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xacc, @PathVariable String xsub){
		return doArchiveOrRestore(xacc, xsub, true);
	}

	private Map<String, Object> doArchiveOrRestore(String xacc, String xsub, boolean archive){
		Acsub acsub = acsubService.findByXaccAndXsub(xacc, xsub);
		if(acsub == null) {
			responseHelper.setErrorStatusAndMessage("Sub Account not found in this system");
			return responseHelper.getResponse();
		}

		long count = acsubService.delete(xacc, xsub);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Sub Account");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Sub Account deleted successfully");
		responseHelper.setRedirectUrl("/account/acsub");
		return responseHelper.getResponse();
	}
}
