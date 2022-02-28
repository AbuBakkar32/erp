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

import com.asl.entity.Pdmst;
import com.asl.entity.Pdsalarydetail;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.PdmstService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/paypersonal")
public class PayPersonalInfoController extends ASLAbstractController{
	@Autowired private PdmstService pdmstService;
	@Autowired private XcodesService xcodesService;
	@GetMapping
	public String loadPayPersonalInfoPage(Model model) {
		model.addAttribute("payinfo", getDefaultPersonalInfo());
		model.addAttribute("allPayinfos", pdmstService.getAllPdmstByXtrn(TransactionCodeType.EMPLOYEE_NUMBER.getdefaultCode()));
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
		return "pages/hr/paypersonal";
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
		if (pdmst == null) return "redirect:/paypersonal";
		
		pdmst.setXtypetrn(TransactionCodeType.EMPLOYEE_NUMBER.getCode());
		model.addAttribute("payinfo", pdmst);
		model.addAttribute("allPayinfos", pdmstService.getAllPdmstByXtrn(TransactionCodeType.EMPLOYEE_NUMBER.getdefaultCode()));
		model.addAttribute("payslist", pdmstService.findByPdsalarydetail(xstaff));
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
		return "pages/hr/paypersonal";
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
		responseHelper.setRedirectUrl("/paypersonal/" + pi.getXstaff());
		return responseHelper.getResponse();
	}
	// if new
	long count = pdmstService.save(pdmst );
	if(count == 0) {
		responseHelper.setErrorStatusAndMessage("Can't save Personal info");
		return responseHelper.getResponse();
	}
	responseHelper.setSuccessStatusAndMessage("Personal info saved successfully");
	responseHelper.setRedirectUrl("/paypersonal/" + pdmst .getXstaff());
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
		responseHelper.setRedirectUrl("/paypersonal/" + xstaff );
		return responseHelper.getResponse();
	}
	
	
	//start of Salary Details
	
		@GetMapping("/{xstaff}/paysalary/{xrow}/show")
		public String loadPaySalaryDetailsModal(@PathVariable String xstaff, @PathVariable String xrow, Model model) {
			if("new".equalsIgnoreCase(xrow)) {
				Pdsalarydetail pays = new Pdsalarydetail();
				pays.setXstaff(xstaff);
				model.addAttribute("pays", pays);
				model.addAttribute("salaryType", pdmstService.getXcodes());
				//model.addAttribute("salaryType", xcodesService.findByXtype(CodeType.SALARY_TYPE.getCode(), Boolean.TRUE));
			}
			else {
				Pdsalarydetail pays = pdmstService.findPdsalarydetailByXstaffAndXrow(xstaff, Integer.parseInt(xrow));
				if(pays==null) {
					pays = new Pdsalarydetail();
					
				}
				model.addAttribute("pays", pays);
				model.addAttribute("salaryType", pdmstService.getXcodes());
				//model.addAttribute("salaryType", xcodesService.findByXtype(CodeType.SALARY_TYPE.getCode(), Boolean.TRUE));
			}
			
			return "pages/hr/paysalarymodal::paysalarymodal";
		}
		
		@PostMapping("/paysalary/save")
		public @ResponseBody Map<String, Object> savePaySalaryDetails(Pdsalarydetail pdsa) {
			if (pdsa == null || StringUtils.isBlank(pdsa.getXstaff())) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
	
			
			/*
			 * Xcodes sign = pdmstService.getXsign(pdsa.getXtype()); if (sign.getXprops() ==
			 * null) sign.setXprops(""); if(sign.getXprops().equals("Addition")) {
			 * pdsa.setXsign("+1"); } else {pdsa.setXsign("-1");}
			 */
			
			// if existing
			Pdsalarydetail exist = pdmstService.findPdsalarydetailByXstaffAndXrow(pdsa.getXstaff(), pdsa.getXrow());
			if (exist != null) {
				BeanUtils.copyProperties(pdsa, exist,"xstaff");
				long count = pdmstService.updatePdsalarydetail(exist);
				if (count == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}
				responseHelper.setReloadSectionIdWithUrl("paysalarytable","/paypersonal/paysalary/" + pdsa.getXstaff());
				responseHelper.setSuccessStatusAndMessage("Salary Details updated successfully");
				return responseHelper.getResponse();
			}
	
			
			// if new detail
			long count = pdmstService.savePdsalarydetail(pdsa);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("paysalarytable","/paypersonal/paysalary/" + pdsa.getXstaff());
			responseHelper.setSuccessStatusAndMessage("Salary Details saved successfully");
			return responseHelper.getResponse();
		}
	
		@GetMapping("/paysalary/{xstaff}")
		public String reloadPaySalaryDetailsTable(@PathVariable String xstaff, Model model) {
			List<Pdsalarydetail> payslist = pdmstService.findByPdsalarydetail(xstaff);
			model.addAttribute("payslist", payslist);
			model.addAttribute("payinfo", pdmstService.findAllPdmst(xstaff));
			return "pages/hr/paypersonal::paysalarytable";
		}
		
		//delete
		@PostMapping("{xstaff}/paysalary/{xrow}/delete")
		public @ResponseBody Map<String, Object> deletePaySalaryDetails(@PathVariable String xstaff, @PathVariable String xrow, Model model) {
			Pdsalarydetail pays = pdmstService.findPdsalarydetailByXstaffAndXrow(xstaff, Integer.parseInt(xrow));
			if(pays == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = pdmstService.deletePdsalarydetail(pays);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("paysalarytable", "/paypersonal/paysalary/" + xstaff);
			return responseHelper.getResponse();
		}
		
		@PostMapping("/autobreak/{xstaff}")
		public @ResponseBody Map<String, Object> transferToGL(@PathVariable String xstaff){
			
			Pdmst pdmst = pdmstService.findAllPdmst(xstaff);
			if(pdmst == null) {
				responseHelper.setErrorStatusAndMessage("Employee is not found in this system");
				return responseHelper.getResponse();
			}
			
			String p_seq;
			
				p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
				pdmstService.procPD_Salary_Upload(pdmst.getXstaff(),pdmst.getXgrade(),pdmst.getXgross(),pdmst.getXdatejoin(), p_seq);
				String em = getProcedureErrorMessages(p_seq);
				if(StringUtils.isNotBlank(em)) {
					responseHelper.setErrorStatusAndMessage(em);
					return responseHelper.getResponse();
				}
			
			responseHelper.setSuccessStatusAndMessage("Auto Beakdowned successfully");
			responseHelper.setRedirectUrl("/paypersonal/" + xstaff);
			return responseHelper.getResponse();
		}

}
