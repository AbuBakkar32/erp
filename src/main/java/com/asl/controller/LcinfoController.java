package com.asl.controller;

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

import com.asl.entity.Lcamend;
import com.asl.entity.Lcinfo;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.LcinfoService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/cost/lcinfo")
public class LcinfoController extends ASLAbstractController{
	
	@Autowired private LcinfoService  lcinfoService ;
	@Autowired private XcodesService xcodeService;
	@GetMapping
	public String loadLcinfoController(Model model) {
		
		model.addAttribute("lcinfo", getDefaultLcinfo());
		model.addAttribute("allLcInfos", lcinfoService.getAllLcinfo());
		model.addAttribute("curruency", xcodeService.findByXtype(CodeType.CURRENCY.getCode(), Boolean.TRUE));
		return "pages/cost/lcinfo/lcinfo";

}
	private Lcinfo getDefaultLcinfo() {
		Lcinfo lc  = new Lcinfo();
		lc.setXstatuspor("Open");
		return lc;
	}
	
	@GetMapping("/{xlcno}")
	public String loadSurveyorPage(@PathVariable String xlcno, Model model) {
		Lcinfo lcinfo = lcinfoService.findByLcinfo(xlcno);
		if (lcinfo == null) return "redirect:/cost/lcinfo";
		
		model.addAttribute("lcinfo", lcinfo);
		model.addAttribute("allLcInfos", lcinfoService.getAllLcinfo());
		model.addAttribute("curruency", xcodeService.findByXtype(CodeType.CURRENCY.getCode(), Boolean.TRUE));
		return "pages/cost/lcinfo/lcinfo";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Lcinfo lcinfo, BindingResult bindingResult) {
		if (lcinfo == null) {
			responseHelper.setErrorStatusAndMessage("LC info is null is not acceptable.");
			return responseHelper.getResponse();
		}

	// if existing
	if(StringUtils.isNotBlank(lcinfo.getXlcno())) {
		Lcinfo xlp = lcinfoService.findByLcinfo(lcinfo.getXlcno());
		
	long count = lcinfoService.updateLcinfo(xlp);
	if(count == 0) {
		responseHelper.setErrorStatusAndMessage("Can't update LC info");
		return responseHelper.getResponse();
	}
	responseHelper.setSuccessStatusAndMessage("LC info updated successfully");
	responseHelper.setRedirectUrl("/cost/lcinfo/" + xlp.getXlcno());
	return responseHelper.getResponse();
	}
		
	// if new
	if(lcinfoService.findByLcinfo(lcinfo.getXlcno()) != null) {
		responseHelper.setErrorStatusAndMessage("LC No. " + lcinfo.getXlcno() + " already exits in the system");
		return responseHelper.getResponse();
	}
	
	long count = lcinfoService.saveLcinfo(lcinfo);
	if(count == 0) {
		responseHelper.setErrorStatusAndMessage("Can't save LC info");
		return responseHelper.getResponse();
	}
	responseHelper.setSuccessStatusAndMessage("LC info saved successfully");
	responseHelper.setRedirectUrl("/cost/lcinfo/" + lcinfo.getXlcno());
		return responseHelper.getResponse();
	}
	

	@PostMapping("/delete/{xlcno}")
	public @ResponseBody Map<String, Object> deleteLCInfo(@PathVariable String xlcno,  Model model) {
		Lcinfo lci = lcinfoService.findByLcinfo(xlcno);
		if(lci == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = lcinfoService.deleteLcinfo(lci);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/cost/lcinfo/" + xlcno );
		return responseHelper.getResponse();
}
	
	//start of landCommitteeMembers
	
	@GetMapping("/{xlcno}/lcamend/{xrow}/show")
	public String loadLCamendModal(@PathVariable String xlcno, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			Lcamend lcm = new Lcamend();
			lcm.setXlcno(xlcno);
			lcm.setXnote("");
			lcm.setXapprover(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());
			
			model.addAttribute("lcm", lcm);
		}
		else {
			Lcamend lcm = lcinfoService.findLcamendByXlcnoAndXrow(xlcno, Integer.parseInt(xrow));
			if(lcm==null) {
				lcm = new Lcamend();
				
			}
			model.addAttribute("lcm", lcm);
		}
		
		return "pages/cost/lcinfo/lcamendmodal::lcamendmodal";
	}
	
	@PostMapping("/lcamend/save")
	public @ResponseBody Map<String, Object> saveLCamend(Lcamend lcamend){
		if(lcamend == null || StringUtils.isBlank(lcamend.getXlcno())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		lcamend.setXapprover(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());

		Lcamend exist = lcinfoService.findLcamendByXlcnoAndXrow(lcamend.getXlcno(), lcamend.getXrow());

		// if existing
		if(exist != null) {
			BeanUtils.copyProperties(lcamend, exist);
			long count = lcinfoService.updateLcamend(exist);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("lcamendtable","/cost/lcinfo/lcamend/" + lcamend.getXlcno());
			responseHelper.setSuccessStatusAndMessage("Amendment Details updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = lcinfoService.saveLcamend(lcamend);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("lcamendtable","/cost/lcinfo/lcamend/" + lcamend.getXlcno());
		responseHelper.setSuccessStatusAndMessage("Amendment Details updated successfully");
		return responseHelper.getResponse();
	}
	
	@GetMapping("/lcamend/{xlcno}")
	public String reloadLCamendTable(@PathVariable String xlcno, Model model) {
		List<Lcamend> lcamendList = lcinfoService.findByLcamend(xlcno);
		model.addAttribute("lcmlist", lcamendList);
		model.addAttribute("committee", lcinfoService.findByLcinfo(xlcno));
		return "pages/cost/lcinfo/lcinfo::lcamendtable";
	}
	
	//delete
	@PostMapping("{xlcno}/lcamend/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteLCamend(@PathVariable String xlcno, @PathVariable String xrow, Model model) {
		Lcamend lcm = lcinfoService.findLcamendByXlcnoAndXrow(xlcno, Integer.parseInt(xrow));
		if(lcm == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = lcinfoService.deleteLcamend(lcm);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("lcamendtable", "/cost/lcinfo/lcamend/" + xlcno);
		return responseHelper.getResponse();
	}

}
