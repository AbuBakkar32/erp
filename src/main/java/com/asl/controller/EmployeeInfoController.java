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

import com.asl.entity.Pdmst;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandDocumentService;
import com.asl.service.PdmstService;

@Controller
@RequestMapping("/employeeinfo")
public class EmployeeInfoController extends ASLAbstractController{
	
	@Autowired private PdmstService pdmstService;
	@Autowired private LandDocumentService landDocumentService;
	@GetMapping
	public String loadHRPersonalInfoPage(Model model) {
		model.addAttribute("empinfo", getDefaultPersonalInfo());
		model.addAttribute("allEmpinfos", pdmstService.getAllPdmstByXtypetrn(TransactionCodeType.EMPLOYEE_NUMBER.getCode()));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.EMPLOYEE_NUMBER.getCode(), Boolean.TRUE));
		//model.addAttribute("sexTypes", xcodesService.findByXtype(CodeType.SEX.getCode(), Boolean.TRUE));
		//model.addAttribute("maritalStatus", xcodesService.findByXtype(CodeType.MARITAL_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("departmentNames", xcodesService.findByXtype(CodeType.DEPARTMENT.getCode(), Boolean.TRUE));
		model.addAttribute("designationNames", xcodesService.findByXtype(CodeType.DESIGNATION.getCode(), Boolean.TRUE));
		model.addAttribute("grades", xcodesService.findByXtype(CodeType.GRADE.getCode(), Boolean.TRUE));
		model.addAttribute("employeeStatus", xcodesService.findByXtype(CodeType.EMPLOYESS_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("districtNames", xcodesService.findByXtype(CodeType.DISTRICT.getCode(), Boolean.TRUE));
		model.addAttribute("employeeCategories", xcodesService.findByXtype(CodeType.EMPLOYESS_CATEGORY.getCode(), Boolean.TRUE));
		//model.addAttribute("bloodGropus", xcodesService.findByXtype(CodeType.BLOOD_GROUP.getCode(), Boolean.TRUE));
		model.addAttribute("leavePlans", xcodesService.findByXtype(CodeType.LEAVE_PLAN.getCode(), Boolean.TRUE));
		model.addAttribute("employeeTypes", xcodesService.findByXtype(CodeType.EMPLOYEE_TYPE.getCode(), Boolean.TRUE));
		//model.addAttribute("weekDays", xcodesService.findByXtype(CodeType.WEEK_DAY.getCode(), Boolean.TRUE));
		model.addAttribute("holidays", xcodesService.findByXtype(CodeType.HOLIDAY_APPLICABLE.getCode(), Boolean.TRUE));
		return "pages/mastersetup/employeeinfo";
	}
	
	private Pdmst getDefaultPersonalInfo() {
		Pdmst pd  = new Pdmst();
		pd.setXtypetrn(TransactionCodeType.EMPLOYEE_NUMBER.getCode());
		pd.setXtrn(TransactionCodeType.EMPLOYEE_NUMBER.getdefaultCode());
		pd.setXstatus("Open");
		return pd;
	}
	
	@GetMapping("/{xstaff}")
	public String loadHRPersonalInfoPage(@PathVariable String xstaff, Model model) {
		Pdmst pdmst = pdmstService.findAllPdmst(xstaff);
		if (pdmst == null) return "redirect:/employeeinfo";
		
		model.addAttribute("empinfo", pdmst);
		model.addAttribute("allEmpinfos", pdmstService.getAllPdmstByXtypetrn(TransactionCodeType.EMPLOYEE_NUMBER.getCode()));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.EMPLOYEE_NUMBER.getCode(), Boolean.TRUE));
		//model.addAttribute("sexTypes", xcodesService.findByXtype(CodeType.SEX.getCode(), Boolean.TRUE));
		//model.addAttribute("maritalStatus", xcodesService.findByXtype(CodeType.MARITAL_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("departmentNames", xcodesService.findByXtype(CodeType.DEPARTMENT.getCode(), Boolean.TRUE));
		model.addAttribute("designationNames", xcodesService.findByXtype(CodeType.DESIGNATION.getCode(), Boolean.TRUE));
		model.addAttribute("grades", xcodesService.findByXtype(CodeType.GRADE.getCode(), Boolean.TRUE));
		model.addAttribute("employeeStatus", xcodesService.findByXtype(CodeType.EMPLOYESS_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("districtNames", xcodesService.findByXtype(CodeType.DISTRICT.getCode(), Boolean.TRUE));
		model.addAttribute("employeeCategories", xcodesService.findByXtype(CodeType.EMPLOYESS_CATEGORY.getCode(), Boolean.TRUE));
		//model.addAttribute("bloodGropus", xcodesService.findByXtype(CodeType.BLOOD_GROUP.getCode(), Boolean.TRUE));
		model.addAttribute("leavePlans", xcodesService.findByXtype(CodeType.LEAVE_PLAN.getCode(), Boolean.TRUE));
		model.addAttribute("employeeTypes", xcodesService.findByXtype(CodeType.EMPLOYEE_TYPE.getCode(), Boolean.TRUE));
		//model.addAttribute("weekDays", xcodesService.findByXtype(CodeType.WEEK_DAY.getCode(), Boolean.TRUE));
		model.addAttribute("holidays", xcodesService.findByXtype(CodeType.HOLIDAY_APPLICABLE.getCode(), Boolean.TRUE));
		return "pages/mastersetup/employeeinfo";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pdmst pdmst, BindingResult bindingResult) {
		if (pdmst == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
	// if existing
	if(StringUtils.isNotBlank(pdmst.getXstaff())) {
		Pdmst pi = pdmstService.findAllPdmst(pdmst.getXstaff());
		BeanUtils.copyProperties(pdmst , pi,"xtypetrn","xtrn");
		long count = pdmstService.update(pi);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update Personal info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Personal info updated successfully"); 
		responseHelper.setRedirectUrl("/employeeinfo/" + pi.getXstaff());
		return responseHelper.getResponse();
	}
	// if new
	long count = pdmstService.save(pdmst );
	if(count == 0) {
		responseHelper.setErrorStatusAndMessage("Can't save Personal info");
		return responseHelper.getResponse();
	}
	responseHelper.setSuccessStatusAndMessage("Personal info saved successfully");
	responseHelper.setRedirectUrl("/employeeinfo/" + pdmst .getXstaff());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/delete/{xstaff}")
	public @ResponseBody Map<String, Object> deleteHRPersonalInfo(@PathVariable String xstaff,  Model model) {
		Pdmst lp = pdmstService.findAllPdmst(xstaff);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = pdmstService.delete(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/employeeinfo/" + xstaff );
		return responseHelper.getResponse();
}

}
