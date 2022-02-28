package com.asl.controller;

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

import com.asl.entity.Cawhfact;
import com.asl.entity.ProjectStoreView;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CawhfactIssueService;
import com.asl.service.ProjectStoreViewService;

@Controller
@RequestMapping("/project/issue")
public class CawhfactIssueController extends ASLAbstractController {
	
	@Autowired private CawhfactIssueService cawhfactService;
	@Autowired private ProjectStoreViewService projectstoreviewService;	
	

	
	@GetMapping
	public String loadIssuePage(Model model) {
		model.addAttribute("issue", getDefaultIssue());
		model.addAttribute("allIssue", cawhfactService.getAllIssue());
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.ISU_NUMBER.getCode(), Boolean.TRUE));
		return "pages/project/task/issue";
	}

	private Cawhfact getDefaultIssue() {
		Cawhfact issue = new Cawhfact();
		issue.setXtypetrn(TransactionCodeType.ISU_NUMBER.getCode());
		issue.setXtrn(TransactionCodeType.ISU_NUMBER.getdefaultCode());
		issue.setXstatus("Open");
		issue.setXtype("Issue");
		issue.setXwh(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? "" :sessionManager.getLoggedInUserDetails().getXwh());
		
		return issue;
	}
	
	

	@GetMapping("/{xtrnnum}")
	public String loadEditIssuePage(@PathVariable String xtrnnum, Model model) {
		Cawhfact data = cawhfactService.findByIssueId(xtrnnum);
		if (data == null) data = getDefaultIssue();
		
		if(StringUtils.isNotBlank(data.getXdtrnnum())) {
			Cawhfact depandsOn = cawhfactService.findByIssueId(data.getXdtrnnum());
			if(depandsOn != null) {
				data.setTaskName(depandsOn.getXname());
			}
		}
		
		model.addAttribute("issue", data);
		model.addAttribute("allIssue", cawhfactService.getAllIssue());
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.ISU_NUMBER.getCode(), Boolean.TRUE));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXproject(data.getXproject());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("codes", list);
		
		

		return "pages/project/task/issue";
	}
	
	

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Cawhfact issue, BindingResult bindingResult){
		if(issue == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff())) {
			responseHelper.setErrorStatusAndMessage("Preparer Can't be empty.");
			return responseHelper.getResponse();
		}
				
		  if(StringUtils.isBlank(issue.getXproject())) {
		  responseHelper.setErrorStatusAndMessage("Project should not empty"); 
		  return responseHelper.getResponse(); 
		 }
		 

		// If existing
		Cawhfact data = cawhfactService.findByIssueId(issue.getXtrnnum());
		if(data != null) {
			BeanUtils.copyProperties(issue, data, "xtypetrn", "xtrn");
			long count = cawhfactService.update(data);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Issue.");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Issue Updated Successfully");
			responseHelper.setRedirectUrl("/project/issue/" + data.getXtrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = cawhfactService.save(issue);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Create Issue ");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Issue Saved Successfully");
		responseHelper.setRedirectUrl("/project/issue/" + issue.getXtrnnum());
		return responseHelper.getResponse();
	}
	
	
		//delete
		@PostMapping("/delete/{xtrnnum}")
		public @ResponseBody Map<String, Object> deleteIssue(@PathVariable String xtrnnum, Model model) {
			Cawhfact data = cawhfactService.findByIssueId(xtrnnum);
			if(data == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = cawhfactService.delete(data);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setRedirectUrl("/project/issue");

			return responseHelper.getResponse();
		}
	
	
	


}
