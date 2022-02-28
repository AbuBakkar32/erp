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
import com.asl.service.CawhfactService;
import com.asl.service.ProjectStoreViewService;

@Controller
@RequestMapping("/project/task")
public class CawhfactController extends ASLAbstractController{
	
	@Autowired private CawhfactService cawhfactService;
	@Autowired private ProjectStoreViewService projectstoreviewService;
	
	
	@GetMapping
	public String loadTaskPage(Model model) {
		model.addAttribute("task", getDefaultTask());
		model.addAttribute("allTask", cawhfactService.getAllTask());
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.TSK_NUMBER.getCode(), Boolean.TRUE));
		return "pages/project/task/cawhfact";
	}

	private Cawhfact getDefaultTask() {
		Cawhfact task = new Cawhfact();
		task.setXtypetrn(TransactionCodeType.TSK_NUMBER.getCode());
		task.setXtrn(TransactionCodeType.TSK_NUMBER.getdefaultCode());
		task.setXstatus("Open");
		task.setXtype("Task");
		task.setXwh(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? "" :sessionManager.getLoggedInUserDetails().getXwh());
		
		return task;
	}
	
	

	@GetMapping("/{xtrnnum}")
	public String loadEditTaskPage(@PathVariable String xtrnnum, Model model) {
		Cawhfact data = cawhfactService.findByTaskId(xtrnnum);
		if (data == null) data = getDefaultTask();
		
		if(StringUtils.isNotBlank(data.getXdtrnnum())) {
			Cawhfact depandsOn = cawhfactService.findByTaskId(data.getXdtrnnum());
			if(depandsOn != null) {
				data.setTaskName(depandsOn.getXname());
			}
		}
		
		model.addAttribute("task", data);
		model.addAttribute("allTask", cawhfactService.getAllTask());
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.TSK_NUMBER.getCode(), Boolean.TRUE));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXproject(data.getXproject());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("codes", list);
		
		

		return "pages/project/task/cawhfact";
	}
	
	

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Cawhfact task, BindingResult bindingResult){
		if(task == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff())) {
			responseHelper.setErrorStatusAndMessage("Preparer Can't be empty.");
			return responseHelper.getResponse();
		}
				
		  if(StringUtils.isBlank(task.getXproject())) {
		  responseHelper.setErrorStatusAndMessage("Project should not empty"); 
		  return responseHelper.getResponse(); 
		 }
		 

		// If existing
		Cawhfact data = cawhfactService.findByTaskId(task.getXtrnnum());
		if(data != null) {
			BeanUtils.copyProperties(task, data, "xtypetrn", "xtrn");
			long count = cawhfactService.update(data);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Task.");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Task Updated Successfully");
			responseHelper.setRedirectUrl("/project/task/" + data.getXtrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = cawhfactService.save(task);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Create Task ");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Task Saved Successfully");
		responseHelper.setRedirectUrl("/project/task/" + task.getXtrnnum());
		return responseHelper.getResponse();
	}
	
	
		//delete
		@PostMapping("/delete/{xtrnnum}")
		public @ResponseBody Map<String, Object> deleteTask(@PathVariable String xtrnnum, Model model) {
			Cawhfact data = cawhfactService.findByTaskId(xtrnnum);
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
			responseHelper.setRedirectUrl("/project/task");

			return responseHelper.getResponse();
		}
		
		
		
		@GetMapping("/codesbyproject/{xproject}")
		public @ResponseBody List<ProjectStoreView> getProjectstoreview(@PathVariable String xproject){
			List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXproject(xproject);
			list.sort(Comparator.comparing(ProjectStoreView::getXcode));
			return list;
		}
	
	
	
	
	

}
