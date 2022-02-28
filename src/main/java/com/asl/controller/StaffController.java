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
import com.asl.entity.Xusers;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.PdmstService;
import com.asl.service.XusersService;

/**
 * @author Zubayer Ahamed
 * @since May 6, 2021
 */
@Controller
@RequestMapping("/system/staff")
public class StaffController extends ASLAbstractController {

	@Autowired private PdmstService pdmstService;
	@Autowired private XusersService usersService;

	@GetMapping
	public String loadStaffPage(Model model) {
		model.addAttribute("pdmst", getDefaultPdmst());

		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.EMPLOYEE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("designations", xcodesService.findByXtype(CodeType.EMPLOYEE_DESIGNATION.getCode(), Boolean.TRUE));
		model.addAttribute("departments", xcodesService.findByXtype(CodeType.EMPLOYEE_DEPARTMENT.getCode(), Boolean.TRUE));
		model.addAttribute("empcategories", xcodesService.findByXtype(CodeType.EMPLOYEE_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("empstatus", xcodesService.findByXtype(CodeType.EMPLOYEE_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("emptypes", xcodesService.findByXtype(CodeType.EMPLOYEE_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("sex", xcodesService.findByXtype(CodeType.SEX.getCode(), Boolean.TRUE));
		model.addAttribute("religion", xcodesService.findByXtype(CodeType.RELIGION.getCode(), Boolean.TRUE));
		model.addAttribute("bloodgroup", xcodesService.findByXtype(CodeType.BLOOD_GROUP.getCode(), Boolean.TRUE));

		model.addAttribute("staffs", pdmstService.getAll(null));

		return "pages/system/usersentry/staff/staff";
	}

	@GetMapping("/{xstaff}")
	public String loadStaffPage(@PathVariable String xstaff, Model model) {
		Pdmst pdmst = pdmstService.findByXstaff(xstaff, null);
		if(pdmst == null) return "redirect:/system/staff";

		model.addAttribute("pdmst", pdmst);

		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.EMPLOYEE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("designations", xcodesService.findByXtype(CodeType.EMPLOYEE_DESIGNATION.getCode(), Boolean.TRUE));
		model.addAttribute("departments", xcodesService.findByXtype(CodeType.EMPLOYEE_DEPARTMENT.getCode(), Boolean.TRUE));
		model.addAttribute("empcategories", xcodesService.findByXtype(CodeType.EMPLOYEE_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("empstatus", xcodesService.findByXtype(CodeType.EMPLOYEE_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("emptypes", xcodesService.findByXtype(CodeType.EMPLOYEE_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("sex", xcodesService.findByXtype(CodeType.SEX.getCode(), Boolean.TRUE));
		model.addAttribute("religion", xcodesService.findByXtype(CodeType.RELIGION.getCode(), Boolean.TRUE));
		model.addAttribute("bloodgroup", xcodesService.findByXtype(CodeType.BLOOD_GROUP.getCode(), Boolean.TRUE));

		model.addAttribute("staffs", pdmstService.getAll(null));

		return "pages/system/usersentry/staff/staff";
	}

	private Pdmst getDefaultPdmst() {
		Pdmst pdmst = new Pdmst();
		pdmst.setXtypetrn(TransactionCodeType.EMPLOYEE_NUMBER.getCode());
		pdmst.setXtrn(TransactionCodeType.EMPLOYEE_NUMBER.getdefaultCode());
		pdmst.setXsex("Male");
		pdmst.setXempstatus("Normal");
		return pdmst;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pdmst pdmst, BindingResult bindingResult){
		if(pdmst == null || StringUtils.isBlank(pdmst.getXtypetrn()) || StringUtils.isBlank(pdmst.getXtrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// validation

		// if existing
		Pdmst exist = pdmstService.findByXstaff(pdmst.getXstaff(), null);
		if(exist != null) {
			// first check already user assigned
			Xusers x = usersService.findUserByXstaff(exist.getXstaff());
			if(x != null && !pdmst.isAllowlogin()) {
				responseHelper.setErrorStatusAndMessage("This staff already assigned with user : " + x.getZemail() + ". To disable allow login, you must de-assgin.");
				return responseHelper.getResponse();
			}

			BeanUtils.copyProperties(pdmst, exist, "xtypetrn", "xtrn","xstaff");
			long count = pdmstService.update(exist);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Staff info not updated");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Staff info updated successfully");
			responseHelper.setRedirectUrl("/system/staff/" + pdmst.getXstaff());
			return responseHelper.getResponse();
		}

		// if new
		long count = pdmstService.save(pdmst);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save staff info");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Staff info saved successfully");
		responseHelper.setRedirectUrl("/system/staff");
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xstaff}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xstaff){
		return doArchiveOrRestore(xstaff, true);
	}

	@PostMapping("/restore/{xstaff}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xstaff){
		return doArchiveOrRestore(xstaff, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xstaff, boolean archive){
		Pdmst pdmst = pdmstService.findByXstaff(xstaff, null);
		if(pdmst == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(archive) {
			Xusers x = usersService.findUserByXstaff(pdmst.getXstaff());
			if(x != null) {
				responseHelper.setErrorStatusAndMessage("This staff already assigned with user : " + x.getZemail() + ". To archive, you must de-assgin first.");
				return responseHelper.getResponse();
			}
		}

		pdmst.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = pdmstService.update(pdmst);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		responseHelper.setRedirectUrl("/system/staff/" + pdmst.getXstaff());
		return responseHelper.getResponse();
	}
}
