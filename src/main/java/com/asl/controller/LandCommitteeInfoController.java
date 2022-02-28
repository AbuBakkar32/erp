package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.asl.entity.LandCommitteeInfo;
import com.asl.entity.LandCommitteeMembers;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;
import com.asl.entity.LandSurveyor;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandCommitteeInfoService;


@Controller
@RequestMapping("/landcommitteeinfo")
public class LandCommitteeInfoController extends ASLAbstractController{
	
	@Autowired private LandCommitteeInfoService  landCommitteeInfoService ;
	@GetMapping
	public String loadLandCommitteeInfoPage(Model model) {
		
		model.addAttribute("committee", getDefaultLandCommitteeinfo());
		model.addAttribute("allCommitteeInfos", landCommitteeInfoService.getAllLandCommitteeInfo());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.COMMITTEEINFO_ID.getCode(), Boolean.TRUE));
		return "pages/land/landcommitteeinfo";
	}
	
	private LandCommitteeInfo getDefaultLandCommitteeinfo() {
		LandCommitteeInfo lf  = new LandCommitteeInfo();
		lf.setXtypetrn(TransactionCodeType.COMMITTEEINFO_ID.getCode());
		lf.setXtrn(TransactionCodeType.COMMITTEEINFO_ID.getdefaultCode());
		
		return lf;
	}
	
	@GetMapping("/{xcommittee}")
	public String loadSurveyorPage(@PathVariable String xcommittee, Model model) {
		LandCommitteeInfo landCommitteeInfo = landCommitteeInfoService.findByLandCommitteeInfo(xcommittee);
		if (landCommitteeInfo == null) return "redirect:/landcommitteeinfo";
		
		model.addAttribute("committee", landCommitteeInfo);
		model.addAttribute("lcmlist", landCommitteeInfoService.findByLandCommitteeMembers(xcommittee));
		model.addAttribute("allCommitteeInfos", landCommitteeInfoService.getAllLandCommitteeInfo());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.COMMITTEEINFO_ID.getCode(), Boolean.TRUE));
		return "pages/land/landcommitteeinfo";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandCommitteeInfo landCommitteeInfo, BindingResult bindingResult) {
		if (landCommitteeInfo == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

	// if existing
	if(StringUtils.isNotBlank(landCommitteeInfo.getXcommittee())) {
		LandCommitteeInfo xlp = landCommitteeInfoService.findByLandCommitteeInfo(landCommitteeInfo.getXcommittee());
		BeanUtils.copyProperties(landCommitteeInfo, xlp,"xtypetrn","xtrn");
	long count = landCommitteeInfoService.update(xlp);
	if(count == 0) {
		responseHelper.setErrorStatusAndMessage("Can't update committee info");
		return responseHelper.getResponse();
	}
	responseHelper.setSuccessStatusAndMessage("Committee info updated successfully");
	responseHelper.setRedirectUrl("/landcommitteeinfo/" + xlp.getXcommittee());
	return responseHelper.getResponse();
	}
		
	// if new
	long count = landCommitteeInfoService.save(landCommitteeInfo);
	if(count == 0) {
		responseHelper.setErrorStatusAndMessage("Can't save committee info");
		return responseHelper.getResponse();
	}
	responseHelper.setSuccessStatusAndMessage("Committee info saved successfully");
	responseHelper.setRedirectUrl("/landcommitteeinfo/" + landCommitteeInfo.getXcommittee());
		return responseHelper.getResponse();
	}
	

	@PostMapping("/delete/{xcommittee}")
	public @ResponseBody Map<String, Object> deleteCommitteeInfo(@PathVariable String xcommittee,  Model model) {
		LandCommitteeInfo lci = landCommitteeInfoService.findByLandCommitteeInfo(xcommittee);
		if(lci == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landCommitteeInfoService.delete(lci);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/landcommitteeinfo/" + xcommittee );
		return responseHelper.getResponse();
}
	//start of landCommitteeMembers
	
		@GetMapping("/{xcommittee}/commembers/{xrow}/show")
		public String loadComMembersModal(@PathVariable String xcommittee, @PathVariable String xrow, Model model) {
			if("new".equalsIgnoreCase(xrow)) {
				LandCommitteeMembers lcm = new LandCommitteeMembers();
				lcm.setXcommittee(xcommittee);
				lcm.setXcontribution("");
				lcm.setXmembertype("");
				lcm.setXnote("");
				lcm.setXperson("");
				model.addAttribute("lcm", lcm);
				model.addAttribute("contyributionTypes", xcodesService.findByXtype(CodeType.COMMITEECONTRIBUTION_TYPE.getCode(), Boolean.TRUE));
				model.addAttribute("memberTypes", xcodesService.findByXtype(CodeType.COMMITEEMEMBER_TYPE.getCode(), Boolean.TRUE));

			}
			else {
				LandCommitteeMembers lcm = landCommitteeInfoService.findComMembersByXcommitteeAndXrow(xcommittee, Integer.parseInt(xrow));
				if(lcm==null) {
					lcm = new LandCommitteeMembers();
					
				}
				model.addAttribute("lcm", lcm);
				model.addAttribute("contyributionTypes", xcodesService.findByXtype(CodeType.COMMITEECONTRIBUTION_TYPE.getCode(), Boolean.TRUE));
				model.addAttribute("memberTypes", xcodesService.findByXtype(CodeType.COMMITEEMEMBER_TYPE.getCode(), Boolean.TRUE));
			}
			
			return "pages/land/commembersmodal::commembersmodal";
		}

		@PostMapping("/commembers/save")
		public @ResponseBody Map<String, Object> saveComMembers(LandCommitteeMembers landCommitteeMembers){
			if(landCommitteeMembers == null || StringUtils.isBlank(landCommitteeMembers.getXcommittee())) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			LandCommitteeMembers exist = landCommitteeInfoService.findByXcommitteeAndXperson(landCommitteeMembers.getXcommittee(), landCommitteeMembers.getXperson());

			// if new data
			if(landCommitteeMembers.getXrow() == 0 && exist != null) {
				responseHelper.setErrorStatusAndMessage("Committee " + landCommitteeMembers.getXcommittee() + " with this member " + landCommitteeMembers.getXperson() + " data already exist in this system");
				return responseHelper.getResponse();
			}
			
			// if existing
			if(landCommitteeMembers.getXrow() != 0 && exist != null) {
				BeanUtils.copyProperties(landCommitteeMembers, exist);
				long count = landCommitteeInfoService.update(exist);
				if(count == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}
				responseHelper.setReloadSectionIdWithUrl("commemberstable","/landcommitteeinfo/commembers/" + landCommitteeMembers.getXcommittee());
				responseHelper.setSuccessStatusAndMessage("Committee Member Details updated successfully");
				return responseHelper.getResponse();
			}

			// if new detail
			long count = landCommitteeInfoService.save(landCommitteeMembers);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("commemberstable","/landcommitteeinfo/commembers/" + landCommitteeMembers.getXcommittee());
			responseHelper.setSuccessStatusAndMessage("Committee Member Details saved successfully");
			return responseHelper.getResponse();
		}
		
		@GetMapping("/commembers/{xcommittee}")
		public String reloadComMemberTable(@PathVariable String xcommittee, Model model) {
			List<LandCommitteeMembers> commembersList = landCommitteeInfoService.findByLandCommitteeMembers(xcommittee);
			model.addAttribute("lcmlist", commembersList);
			model.addAttribute("committee", landCommitteeInfoService.findByLandCommitteeInfo(xcommittee));
			return "pages/land/landcommitteeinfo::commemberstable";
		}
		
		//delete
		@PostMapping("{xcommittee}/commembers/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteOwnerDetails(@PathVariable String xcommittee, @PathVariable String xrow, Model model) {
			LandCommitteeMembers lcm = landCommitteeInfoService.findComMembersByXcommitteeAndXrow(xcommittee, Integer.parseInt(xrow));
			if(lcm == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = landCommitteeInfoService.deleteLandCommitteeMembers(lcm);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("commemberstable", "/landcommitteeinfo/commembers/" + xcommittee);
			return responseHelper.getResponse();
		}
		////end of LandCommitteeMembers
}
