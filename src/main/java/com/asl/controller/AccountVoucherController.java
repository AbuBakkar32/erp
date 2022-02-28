package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Acdef;
import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;
import com.asl.entity.Acmst;
import com.asl.entity.Acsubview;
import com.asl.entity.DataList;
import com.asl.entity.ProjectStoreView;
import com.asl.entity.Xcodes;
import com.asl.entity.Xtrn;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.VoucherReport;
import com.asl.service.AcService;
import com.asl.service.AcdefService;
import com.asl.service.AcmstService;
import com.asl.service.AcsubviewSerevice;
import com.asl.service.ProjectStoreViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/account/voucher")
public class AccountVoucherController extends ASLAbstractController{

	@Autowired private AcService acService;
	@Autowired private AcdefService acdefService;
	@Autowired private AcsubviewSerevice acsubviewSerevice;
	@Autowired private AcmstService acmstService;
	@Autowired private ProjectStoreViewService projectStoreViewService;

	@GetMapping
	public String loadAccountVoucherPage(Model model) {
		model.addAttribute("acheader", getDefaultAcheader());
		model.addAttribute("voucherprefix", xtrnService.findByXtypetrn(TransactionCodeType.GL_VOUCHER.getCode(), Boolean.TRUE));

		Map<String, String> projectsMap = new TreeMap<>();
		List<ProjectStoreView> projects = projectStoreViewService.getProjectStores();
		for(ProjectStoreView p : projects) {
			if(StringUtils.isNotBlank(p.getXproject()) && projectsMap.get(p.getXproject()) == null) {
				projectsMap.put(p.getXproject(), p.getXprojectname());
			}
		}
		
		
		List<Xcodes> project = xcodesService.findByXtype(CodeType.PROJECT.getCode(), true);
		model.addAttribute("projects", project);
		model.addAttribute("selectedStores", Collections.emptyList());
		model.addAttribute("bins", xcodesService.findByXtype(CodeType.BIN_NUMBER.getCode(), Boolean.TRUE));

		model.addAttribute("vouchers", acService.getAllAcheader());

		List<DataList> configlist = listService.getList("SYSTEM", "VOUCHER_ENTRY_PAGE");
		if(configlist != null && !configlist.isEmpty()) {
			DataList pageConfig = configlist.stream().findFirst().orElse(null);
			if(pageConfig != null && StringUtils.isNotBlank(pageConfig.getListvalue2())) {
				
				model.addAttribute("accounts", acmstService.getAllAcmst());
				return"pages/account/voucher/" + pageConfig.getListvalue2();
			}
		}

		return"pages/account/voucher/voucher";
	}

	private Acheader getDefaultAcheader() {
		Acheader acheader = new Acheader();
		acheader.setXtypetrn(TransactionCodeType.GL_VOUCHER.getCode());
		acheader.setXtrn(TransactionCodeType.GL_VOUCHER.getdefaultCode());
		acheader.setXstatusjv("Balanced");
		acheader.setXdate(new Date());
		return acheader;
	}

	@GetMapping("/{xvoucher}")
	public String loadAccountVoucherPage(@PathVariable String xvoucher, Model model) {
		Acheader acheader = acService.findAcheaderByXvoucher(xvoucher);
		if(acheader == null) return "rdirect:/account/voucher";

		model.addAttribute("acheader", acheader);
		model.addAttribute("voucherprefix", xtrnService.findByXtypetrn(TransactionCodeType.GL_VOUCHER.getCode(), Boolean.TRUE));

		Map<String, String> projectsMap = new TreeMap<>();
		List<ProjectStoreView> projects = projectStoreViewService.getProjectStores();
		for(ProjectStoreView p : projects) {
			if(StringUtils.isNotBlank(p.getXproject()) && projectsMap.get(p.getXproject()) == null) {
				projectsMap.put(p.getXproject(), p.getXprojectname());
			}
		}
		List<Xcodes> project = xcodesService.findByXtype(CodeType.PROJECT.getCode(), true);
		model.addAttribute("projects", project);
		model.addAttribute("bins", xcodesService.findByXtype(CodeType.BIN_NUMBER.getCode(), Boolean.TRUE));
		
		
		List<ProjectStoreView> selectedStores = projectStoreViewService.getProjectStoresByXproject(acheader.getXwh());
		model.addAttribute("selectedStores", selectedStores);
		

		List<Acdetail> acdetails = acService.findAcdetailsByXvoucher(xvoucher);
		BigDecimal totalDebit = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
		BigDecimal totalCredit = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
		if(acdetails != null && !acdetails.isEmpty()) {
			for(Acdetail acd : acdetails) {
				totalDebit = totalDebit.add(acd.getXdebit() == null ? BigDecimal.ZERO.setScale(2, RoundingMode.DOWN) : acd.getXdebit().setScale(2, RoundingMode.DOWN));
				totalCredit = totalCredit.add(acd.getXcredit() == null ? BigDecimal.ZERO.setScale(2, RoundingMode.DOWN) : acd.getXcredit().setScale(2, RoundingMode.DOWN));

				// sub accounts list
				List<Acsubview> list = acsubviewSerevice.findSubAccountByXacc(acd.getXacc());
				list.sort(Comparator.comparing(Acsubview::getXsub));
				acd.setSubAccounts(list);

			}
		}
		model.addAttribute("acdetails", acdetails);
		model.addAttribute("totalDebit", totalDebit);
		model.addAttribute("totalCredit", totalCredit);
		model.addAttribute("accounts", acmstService.getAllAcmst());
		model.addAttribute("vouchers", acService.getAllAcheader());

		List<DataList> configlist = listService.getList("SYSTEM", "VOUCHER_ENTRY_PAGE");
		if(configlist != null && !configlist.isEmpty()) {
			DataList pageConfig = configlist.stream().findFirst().orElse(null);
			if(pageConfig != null && StringUtils.isNotBlank(pageConfig.getListvalue2())) {
				return"pages/account/voucher/" + pageConfig.getListvalue2();
			}
		}

		return"pages/account/voucher/voucher";
	}

	@GetMapping("/voucherslist")
	public String loadVoucherListPage(Model model) {
		model.addAttribute("vouchers", acService.getAllAcheader());
		return"pages/account/voucher/voucherslist";
	}

	@PostMapping(value = "/savevoucher" , headers="Accept=application/json")
	public @ResponseBody Map<String, Object> save(@RequestBody String json){

		VoucherMaster vm = new VoucherMaster();

		ObjectMapper obm = new ObjectMapper();
		obm.setDateFormat(SDF2);
		try {
			vm = obm.readValue(json, VoucherMaster.class);
			System.out.println(vm.toString());
		} catch (JsonProcessingException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		Acheader acheader = vm.getAcheader();
		List<Acdetail> acdetails = vm.getAcdetails();

		try {
			return acService.saveWithDetail(acheader, acdetails, responseHelper);
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}
	}


	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Acheader acheader, BindingResult bindingResult){
		// validate
		if(acheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Voucher date required");
			return responseHelper.getResponse();
		}

		// set year and date based on xdate   
		Acdef acdef = acdefService.find();
		if(acdef == null) {
			responseHelper.setErrorStatusAndMessage("Account default is not set in this sytem");
			return responseHelper.getResponse();
		}

		// voucher cal
		acheader = acService.setXyearAndXper(acheader, acdef);

		// if existing
		if(StringUtils.isNotBlank(acheader.getXvoucher())) {
			Acheader exist = acService.findAcheaderByXvoucher(acheader.getXvoucher());
			if(exist == null) {
				responseHelper.setErrorStatusAndMessage("Can't find voucher in this system");
				return responseHelper.getResponse();
			}

			BeanUtils.copyProperties(acheader, exist, "xvoucher","xstatusjv");
			long count = acService.updateAcheader(exist);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update voucher");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Voucher updated successfully");
			responseHelper.setRedirectUrl("/account/voucher/" + exist.getXvoucher());
			return responseHelper.getResponse();
		}

		// if new
		acheader.setXstatusjv("Balanced");
		long count = acService.saveAcheader(acheader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create voucher");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Voucher created successfully");
		responseHelper.setRedirectUrl("/account/voucher/" + acheader.getXvoucher());
		return responseHelper.getResponse();
	}

	@GetMapping("{xvoucher}/voucherdetail/{xrow}/show")
	public String openVoucherDetailModal(@PathVariable String xvoucher, @PathVariable String xrow, Model model) {
		Acheader acheader = acService.findAcheaderByXvoucher(xvoucher);

		if("new".equalsIgnoreCase(xrow)) {
			Acdetail acdetail = new Acdetail();
			acdetail.setXvoucher(xvoucher);
			acdetail.setXdebit(BigDecimal.ZERO);
			acdetail.setXcredit(BigDecimal.ZERO);
			acdetail.setXwh(acheader != null ? acheader.getXwh() : "01");
			model.addAttribute("acdetail", acdetail);
			model.addAttribute("subaccounts", Collections.emptyList());
		} else {
			Acdetail acdetail = acService.findAcdetailByXrowAndXvoucher(Integer.parseInt(xrow), xvoucher);	
			model.addAttribute("acdetail", acdetail);
			List<Acsubview> list = acsubviewSerevice.findSubAccountByXacc(acdetail.getXacc());
			list.sort(Comparator.comparing(Acsubview::getXsub));
			model.addAttribute("subaccounts", list);
		}

		return "pages/account/voucher/voucherdetailmodal::voucherdetailmodal";
	}

	@PostMapping("/voucherdetail/save")
	public @ResponseBody Map<String, Object> savePoorddetail(Acdetail acdetail){
		if(StringUtils.isBlank(acdetail.getXvoucher())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(acdetail.getXacc())) {
			responseHelper.setErrorStatusAndMessage("Account required");
			return responseHelper.getResponse();
		}

		if(acdetail.getXdebit() == null) acdetail.setXdebit(BigDecimal.ZERO);
		if(acdetail.getXcredit() == null) acdetail.setXcredit(BigDecimal.ZERO);

		if(acdetail.getXdebit().compareTo(BigDecimal.ZERO) == -1 || acdetail.getXcredit().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid David or Credit amount");
			return responseHelper.getResponse();
		}

		if(acdetail.getXdebit().equals(BigDecimal.ZERO) && acdetail.getXcredit().equals(BigDecimal.ZERO)) {
			responseHelper.setErrorStatusAndMessage("Invalid David and Credit amount");
			return responseHelper.getResponse();
		}

		if(acdetail.getXdebit().compareTo(BigDecimal.ZERO) == 1 && acdetail.getXcredit().compareTo(BigDecimal.ZERO) == 1) {
			responseHelper.setErrorStatusAndMessage("You should enter only David or Credit amount");
			return responseHelper.getResponse();
		}

		if(acdetail.getXdebit().compareTo(BigDecimal.ZERO) == 1) {
			acdetail.setXprime(acdetail.getXdebit());
		} else {
			acdetail.setXprime(acdetail.getXcredit().multiply(BigDecimal.valueOf(-1)));
		}

		// if existing
		if(acdetail.getXrow() > 0) {
			Acdetail exist = acService.findAcdetailByXrowAndXvoucher(acdetail.getXrow(), acdetail.getXvoucher());
			if(exist == null) {
				responseHelper.setErrorStatusAndMessage("Can't find detail to do update");
				return responseHelper.getResponse();
			}

			BeanUtils.copyProperties(acdetail, exist, "xvoucher", "xrow");
			long count = acService.updateAcdetail(exist);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update voucher detail");
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("voucherdetailtable", "/account/voucher/voucherdetail/" + exist.getXvoucher());
			responseHelper.setSecondReloadSectionIdWithUrl("voucherform", "/account/voucher/voucherform/" + exist.getXvoucher());
			responseHelper.setSuccessStatusAndMessage("Voucher detail update successfully");
			return responseHelper.getResponse();
		}

		// if new
		long count = acService.saveAcdetail(acdetail);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save voucher detail");
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("voucherdetailtable", "/account/voucher/voucherdetail/" + acdetail.getXvoucher());
		responseHelper.setSecondReloadSectionIdWithUrl("voucherform", "/account/voucher/voucherform/" + acdetail.getXvoucher());
		responseHelper.setSuccessStatusAndMessage("Voucher detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/voucherdetail/{xvoucher}")
	public String reloadVouvherDetailTabble(@PathVariable String xvoucher, Model model) {
		model.addAttribute("acheader", acService.findAcheaderByXvoucher(xvoucher));
		
		List<Acdetail> acdetails = acService.findAcdetailsByXvoucher(xvoucher);
		BigDecimal totalDebit = BigDecimal.ZERO;
		BigDecimal totalCredit = BigDecimal.ZERO;
		if(acdetails != null && !acdetails.isEmpty()) {
			for(Acdetail acd : acdetails) {
				totalDebit = totalDebit.add(acd.getXdebit() == null ? BigDecimal.ZERO : acd.getXdebit());
				totalCredit = totalCredit.add(acd.getXcredit() == null ? BigDecimal.ZERO : acd.getXcredit());
			}
		}
		model.addAttribute("acdetails", acdetails);
		model.addAttribute("totalDebit", totalDebit);
		model.addAttribute("totalCredit", totalCredit);
		return"pages/account/voucher/voucher::voucherdetailtable";
	}

	@GetMapping("/voucherform/{xvoucher}")
	public String reloadVouvherForm(@PathVariable String xvoucher, Model model) {
		model.addAttribute("acheader", acService.findAcheaderByXvoucher(xvoucher));
		return"pages/account/voucher/voucher::voucherform";
	}

	@PostMapping("/posted/{xvoucher}")
	public @ResponseBody Map<String, Object> posted(@PathVariable String xvoucher){
		Acheader acheader = acService.findAcheaderByXvoucher(xvoucher);
		if(acheader == null) {
			responseHelper.setErrorStatusAndMessage("Voucher not found in this system");
			return responseHelper.getResponse();
		}

		// validate
		if(StringUtils.isBlank(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher status invalid");
			return responseHelper.getResponse();
		}
		if(!"Balanced".equalsIgnoreCase(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher status is not Balanced");
			return responseHelper.getResponse();
		}
		
		List<Acdetail> details = acService.findAcdetailsByXvoucher(xvoucher);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Voucher details is empty");
			return responseHelper.getResponse();
		}

		try {
			acService.procAcVoucherPost(acheader.getXyear(), acheader.getXper(), acheader.getXvoucher(), acheader.getXvoucher());
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/account/voucher/" + acheader.getXvoucher());
		responseHelper.setSuccessStatusAndMessage("Posted successful");
		return responseHelper.getResponse();
	}

	@PostMapping("/unposted/{xvoucher}")
	public @ResponseBody Map<String, Object> unposted(@PathVariable String xvoucher){
		Acheader acheader = acService.findAcheaderByXvoucher(xvoucher);
		if(acheader == null) {
			responseHelper.setErrorStatusAndMessage("Voucher not found in this system");
			return responseHelper.getResponse();
		}

		// validate
		if(StringUtils.isBlank(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher status invalid");
			return responseHelper.getResponse();
		}
		if(!"Posted".equalsIgnoreCase(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher status is not Posted");
			return responseHelper.getResponse();
		}

		try {
			acService.procAcVoucherUnPost(acheader.getXyear(), acheader.getXper(), acheader.getXvoucher(), acheader.getXvoucher());
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/account/voucher/" + acheader.getXvoucher());
		responseHelper.setSuccessStatusAndMessage("Unposted successful");
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xvoucher}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xvoucher){
		return doArchiveOrRestore(xvoucher, true);
	}

	private Map<String, Object> doArchiveOrRestore(String xvoucher, boolean archive){
		Acheader acheader = acService.findAcheaderByXvoucher(xvoucher);
		if(acheader == null) {
			responseHelper.setErrorStatusAndMessage("Voucher not found in this system");
			return responseHelper.getResponse();
		}

		if("Posted".equalsIgnoreCase(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		List<Acdetail> details = acService.findAcdetailsByXvoucher(xvoucher);
		if(details != null && !details.isEmpty()) {
			// delete all voucher details first
			for(Acdetail acd : details) {
				acService.deleteAcdetail(acd.getXrow(), acd.getXvoucher());
			}
		}

		long count = acService.deleteAcheader(acheader.getXvoucher());
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Voucher");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Voucher deleted successfully");
		responseHelper.setRedirectUrl("/account/voucher");
		return responseHelper.getResponse();
	}

	@PostMapping("{xvoucher}/voucherdetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteVoucherDetail(@PathVariable String xvoucher, @PathVariable String xrow, Model model) {
		Acdetail acdetail = acService.findAcdetailByXrowAndXvoucher(Integer.parseInt(xrow), xvoucher);
		if(acdetail == null) {
			responseHelper.setErrorStatusAndMessage("Voucher detail not found in this system");
			return responseHelper.getResponse();
		}

		long count = acService.deleteAcdetail(acdetail.getXrow(), acdetail.getXvoucher());
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete voucher detail");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("voucherdetailtable", "/account/voucher/voucherdetail/" + xvoucher);
		responseHelper.setSecondReloadSectionIdWithUrl("voucherform", "/account/voucher/voucherform/" + xvoucher);
		responseHelper.setSuccessStatusAndMessage("Voucher detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/subaccount/{xacc}")
	public @ResponseBody List<Acsubview> getSubAccount(@PathVariable String xacc){
		List<Acsubview> list = acsubviewSerevice.findSubAccountByXacc(xacc);
		list.sort(Comparator.comparing(Acsubview::getXsub));
		return list;
	}

	@GetMapping("/preloadeddata")
	public @ResponseBody PreData getAccountList(){
		PreData preData = new PreData();

		List<Acmst> acmstList = acmstService.getAllAcmst();
		acmstList.sort(Comparator.comparing(Acmst::getXacc));
		preData.setAcmstList(acmstList);

		List<ProjectStoreView> projects = projectStoreViewService.getProjectStores();
		projects.sort(Comparator.comparing(ProjectStoreView::getXproject));
		preData.setStores(projects);

		List<Acsubview> list = acsubviewSerevice.findAllSubAccount();
		list.sort(Comparator.comparing(Acsubview::getXsub));
		preData.setSubAccounts(list);
		return preData;
	}
	
	@GetMapping("/print/{xvoucher}")
	public ResponseEntity<byte[]> printSalseOrderWithDetails(@PathVariable String xvoucher, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Acheader data = acService.findAcheaderByXvoucher(xvoucher);
		Acheader reportName = acService.findReportName(xvoucher);
		Xtrn rName = acService.getReportName(xvoucher);
		
		if(reportName== null) {
			message = "No trn for voucher : " + xvoucher;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
	
		VoucherReport report = new VoucherReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName(rName.getXdesc());
		
		
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());

		report.setXvoucher(data.getXvoucher());
		report.setXdate(sdf.format(data.getXdate()));
		report.setXstatusjv(data.getXstatusjv());
		report.setXyear(data.getXyear());
		report.setXref(data.getXref());
		report.setXchequeno(data.getXchequeno());
		report.setXlcno(data.getXlcno());
		report.setXper(data.getXper());
		report.setTotalDebit(BigDecimal.ZERO);
		report.setTotalCredit(BigDecimal.ZERO);
		report.setXbin(data.getXbin());

		report.setXwh("");
		List<ProjectStoreView> porjects = projectStoreViewService.getProjectStoresByXproject(data.getXwh());
		if(porjects != null && !porjects.isEmpty()) {
			report.setXwh(data.getXwh() + " - " + porjects.get(0).getXprojectname());
		}
		report.setXlong(data.getXlong());
		
		
		List<Acdetail> items = acService.findAcdetailsByXvoucher(data.getXvoucher());	
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setXacc(it.getXacc());
				item.setAccountname(it.getAccountname());
				item.setXsub(it.getXsub());
				item.setXorg(it.getXorg());
				item.setXcredit(it.getXcredit() != null ? it.getXcredit() : BigDecimal.ZERO);
				item.setXdebit(it.getXdebit() != null ? it.getXdebit() : BigDecimal.ZERO);
				item.setSpellword(it.getSpellword());
				item.setXprimeword(it.getXprimeword());

				item.setXwh("");
				if(porjects != null && !porjects.isEmpty()) {
					List<ProjectStoreView> filteredStore = porjects.stream().filter(f -> f.getXcode().equals(it.getXwh())).collect(Collectors.toList());
					if(filteredStore != null && !filteredStore.isEmpty()) {
						item.setXwh(it.getXwh() + " - " + filteredStore.get(0).getXcodename());
					}
				}
				
				
				report.getItems().add(item);
				report.setTotalCredit(report.getTotalCredit().add(item.getXcredit()));
				report.setTotalDebit(report.getTotalDebit().add(item.getXdebit()));
				report.setSpellword(item.getSpellword());
				report.setXprimeword(item.getXprimeword());
			});
		}
		
		
		byte[] byt = getPDFByte(report, "voucherreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for voucher : " + xvoucher;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}
	
	
	@GetMapping("/getblankrow")
	public String loadBlankRow(Model model) {
		
		return "pages/account/voucher/voucher2::voucherdetailtablerow";
	}
	
	
}


@Data
class VoucherMaster{
	private Acheader acheader;
	private List<Acdetail> acdetails;
}

@Data
class PreData {
	List<Acmst> acmstList;
	List<ProjectStoreView> stores;
	List<Acsubview> subAccounts;
}

