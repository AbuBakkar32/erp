package com.asl.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.asl.entity.Profile;
import com.asl.entity.ProfileAllocation;
import com.asl.enums.ProfileType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ProfileAllocationService;
import com.asl.service.ProfileService;

/**
 * @author Zubayer Ahamed
 * @since Apr 6, 2021
 */
@Controller
@RequestMapping("/system/profileallocation")
public class ProfileAllocationController extends ASLAbstractController {

	@Autowired private ProfileAllocationService paService;
	@Autowired private ProfileService profileService;

	@GetMapping
	public String loadProfileAllocationPage(Model model) {
		List<ProfileAllocation> paList = paService.getAllProfileAllocation();
		paList.sort(Comparator.comparing(ProfileAllocation::getZemail));
		model.addAttribute("profileallocations", paList);
		return "pages/system/usersentry/pa/pa";
	}

	@GetMapping("/pamodal/{zemail}/show")
	public String loadPaModal(@PathVariable String zemail, Model model) {
		ProfileAllocation pa = paService.findByZemail(zemail);
		if(pa == null) {
			pa = new ProfileAllocation();
			pa.setZemail(zemail);
		}
		pa.setXtypetrn(TransactionCodeType.PROFILE_ALLOCATION.getCode());
		pa.setXtrn(TransactionCodeType.PROFILE_ALLOCATION.getdefaultCode());
		model.addAttribute("pa", pa);

		List<Profile> profiles = profileService.getAllProfiles();
		model.addAttribute("mpcodes", profiles.stream().filter(p -> ProfileType.M.equals(p.getProfiletype()) && Boolean.TRUE.equals(p.getZactive())).collect(Collectors.toList()));
		model.addAttribute("rpcodes", profiles.stream().filter(p -> ProfileType.R.equals(p.getProfiletype()) && Boolean.TRUE.equals(p.getZactive())).collect(Collectors.toList()));
		model.addAttribute("upcodes", profiles.stream().filter(p -> ProfileType.U.equals(p.getProfiletype()) && Boolean.TRUE.equals(p.getZactive())).collect(Collectors.toList()));
		return "pages/system/usersentry/pa/pamodal::pamodal";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> saveProfileAllocation(ProfileAllocation profileAllocation, BindingResult bindingResult, Model model){
		if(profileAllocation == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		ProfileAllocation pa = paService.findByZemail(profileAllocation.getZemail());
		if(pa != null) {
			BeanUtils.copyProperties(profileAllocation, pa, "xtypetrn", "xtrn", "zemail", "paid");
			long count = paService.update(pa);
			if(count == 0) {
				responseHelper.setSuccessStatusAndMessage("Can't update profile allocation");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Profile allocation update successfully");
			responseHelper.setRedirectUrl("/system/profileallocation/");
			return responseHelper.getResponse();
		}

		// if new
		long count = paService.save(profileAllocation);
		if(count == 0) {
			responseHelper.setSuccessStatusAndMessage("Can't allocate profiles to user : " + profileAllocation.getZemail());
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Profile allocation saved successfully");
		responseHelper.setRedirectUrl("/system/profileallocation/");
		return responseHelper.getResponse();
	}
}
