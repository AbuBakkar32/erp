package com.asl.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.AccountGroup;
import com.asl.enums.AccountType;
import com.asl.enums.ResponseStatus;
import com.asl.service.AccountGroupService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/account/acgroup")
public class AccountGroupController extends ASLAbstractController {

	@Autowired private AccountGroupService agService;

	@GetMapping
	public String LoadAccountGroupPage(@RequestParam(required = false) int level, @RequestParam(required = false) String asparent, Model model){

		model.addAttribute("acgroup", getDefaultAccountGroup(level, asparent));
		model.addAttribute("groups", level == 1 || StringUtils.isBlank(asparent) ? agService.getAllByLevel(level) : agService.getAllByXagparent(asparent));
		model.addAttribute("childgroups", Collections.emptyList());

		return "pages/account/accountgroup/accountgroup";
	}

	private AccountGroup getDefaultAccountGroup(int level, String asparent) {
		AccountGroup ag = new AccountGroup();
		ag.setXaglevel(level == 0 ? 1 : level);
		ag.setXagtype(AccountType.ASSET.getCode());
		if(!"null".equalsIgnoreCase(asparent)) {
			ag.setXagparent(asparent);
			AccountGroup parent = agService.findByCode(asparent);
			ag.setParentname(parent != null ? parent.getXagname() : "");
			ag.setXagtype(parent != null ? parent.getXagtype() : AccountType.ASSET.getCode());
		}
		return ag;
	}

	@GetMapping("/{xagcode}")
	public String LoadAccountGroupPage(@PathVariable String xagcode, @RequestParam(required = false) int level, @RequestParam(required = false) String asparent, Model model){
		AccountGroup ag = agService.findByCode(xagcode);
		if(ag == null) {
			return "redirect:/acgroup?level=" + level;
		}

		ag.setExistingRecord(true);
		AccountGroup parent = agService.findByCode(StringUtils.isNotBlank(asparent) ? asparent : ag.getXagparent());
		ag.setParentname(parent != null ? parent.getXagname() : "");
		model.addAttribute("acgroup", ag);
		model.addAttribute("groups", level == 1 ? agService.getAllByLevel(level) : agService.getAllByXagparent(ag.getXagparent()));
		model.addAttribute("childgroups", agService.getAllByXagparent(ag.getXagcode()));
		return "pages/account/accountgroup/accountgroup";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(AccountGroup accountGroup){
		// validation
		if(StringUtils.isBlank(accountGroup.getXagname())) {
			responseHelper.setErrorStatusAndMessage("Group Name Required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(accountGroup.getXagcode())) {
			responseHelper.setErrorStatusAndMessage("Group Code Required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(accountGroup.getXagtype())) {
			responseHelper.setErrorStatusAndMessage("Group Type Required");
			return responseHelper.getResponse();
		}
		try {
			Long.valueOf(accountGroup.getXagcode());
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		// if existing
		if(StringUtils.isNotBlank(accountGroup.getXagcode()) && accountGroup.isExistingRecord()) {
			AccountGroup exg = agService.findByCode(accountGroup.getXagcode());
			if(exg == null) {
				responseHelper.setErrorStatusAndMessage("Can't find this group in this system");
				return responseHelper.getResponse();
			}

			exg.setXagname(accountGroup.getXagname());
			long count = agService.update(exg);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save account group");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Account group updated successfully");
			responseHelper.setRedirectUrl("/account/acgroup/"+ exg.getXagcode() +"?level=" + exg.getXaglevel());
			return responseHelper.getResponse();
		}

		// if new
		long count = agService.save(accountGroup);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save account group");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Account group created successfully");
		responseHelper.setRedirectUrl("/account/acgroup/"+ accountGroup.getXagcode() +"?level=" + accountGroup.getXaglevel());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xagcode}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xagcode){
		return doArchiveOrRestore(xagcode, true);
	}

	public Map<String, Object> doArchiveOrRestore(String xagcode, boolean archive){
		AccountGroup ag = agService.findByCode(xagcode);
		if(ag == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate first
		List<AccountGroup> childs = agService.getAllByXagparent(ag.getXagcode());
		if(childs != null && !childs.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Can't delete this group, Child group found in this system");
			return responseHelper.getResponse();
		}
		// TODO:  check already it used in an account


		long count = agService.delete(xagcode);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Delete Account Group");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Account Group deleted successfully");
		responseHelper.setRedirectUrl("/account/acgroup?level=" + ag.getXaglevel());
		return responseHelper.getResponse();
	}
}
