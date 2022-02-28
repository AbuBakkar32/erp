package com.asl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.AccountGroup;
import com.asl.entity.Acheader;
import com.asl.entity.Acmst;
import com.asl.entity.Arhed;
import com.asl.entity.Cabank;
import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.Caproject;
import com.asl.entity.Cawhfact;
import com.asl.entity.LandCommitteeInfo;
import com.asl.entity.LandInfo;
import com.asl.entity.LandPerson;
import com.asl.entity.LandSurveyor;
import com.asl.entity.Lcinfo;
import com.asl.entity.Modetail;
import com.asl.entity.Moheader;
import com.asl.entity.Opdoheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.Pdmst;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.Xcodes;
import com.asl.enums.TransactionCodeType;
import com.asl.model.SearchSuggestResult;
import com.asl.service.AcService;
import com.asl.service.AccountGroupService;
import com.asl.service.AcmstService;
import com.asl.service.ArhedService;
import com.asl.service.BmbomService;
import com.asl.service.CabankService;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.CaprojectService;
import com.asl.service.CawhfactIssueService;
import com.asl.service.CawhfactService;
import com.asl.service.LandCommitteeInfoService;
import com.asl.service.LandDocumentService;
import com.asl.service.LandInfoService;
import com.asl.service.LandOwnerService;
import com.asl.service.LandPersonService;
import com.asl.service.LandSurveyorService;
import com.asl.service.LcinfoService;
import com.asl.service.MoService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;
import com.asl.service.PdmstService;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.PoordService;
import com.asl.service.ProductionSuggestionService;
import com.asl.service.ZbusinessService;


/**
 * @author Zubayer Ahamed
 * @since Mar 2, 2021
 */
@Controller
@RequestMapping("/search")
public class SearchSuggestController extends ASLAbstractController {

	@Autowired private CacusService cacusService;
	@Autowired private CaitemService caitemService;
	@Autowired private ProductionSuggestionService productionSuggestionService;
	@Autowired private PogrnService pogrnService;
	@Autowired private OpordService opordService;
	@Autowired private PocrnService pocrnService;
	@Autowired private OpdoService  opdoService;
	@Autowired private BmbomService bmbomService;
	@Autowired private PoordService poordService;
	@Autowired private MoService MoService;
	@Autowired private ArhedService arhedService;
	@Autowired private PdmstService pdmstService;
	@Autowired private LandOwnerService landOwnerService;
	@Autowired private LandDocumentService landDocumentService;
	@Autowired private LandPersonService landPersonService;
	@Autowired private LandCommitteeInfoService landCommitteeInfoService;
	@Autowired private LandInfoService  landInfoService;
	@Autowired private AccountGroupService accountGroupService;
	@Autowired private AcmstService acmstServicee;
	@Autowired private CabankService cabankService;
	@Autowired private AcService acService;
	@Autowired private LandSurveyorService landSurveyorService;
	@Autowired private ZbusinessService businessService;
	@Autowired private CaprojectService caprojectService;
	@Autowired private LcinfoService lcinfoService;
	@Autowired private CawhfactService cawhfactService;
	@Autowired private CawhfactIssueService cawhfactIssueService;

	@GetMapping("/lcinfo/{hint}")
	public @ResponseBody List<SearchSuggestResult> getLcInfo(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<SearchSuggestResult> list = new ArrayList<>();
		List<Lcinfo> grns = lcinfoService.searchLcNo(hint);
		grns.stream().forEach(g -> list.add(new SearchSuggestResult(g.getXlcno(), g.getXlcno())));
		return list;
	}

	@GetMapping("/voucher/{hint}")
	public @ResponseBody SearchSuggestResult getVouchers(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return null;
		SearchSuggestResult result = new SearchSuggestResult();
		result.setColumns(new String[] {"Voucher No","Date","Status"});
		List<String[]> data = new ArrayList<>();

		List<Acheader> resultData = acService.searchVoucher(hint);
		if(resultData != null && !resultData.isEmpty()) {
			for(Acheader ah : resultData) {
				data.add(new String[] {
					ah.getXvoucher(), 
					ah.getXdate() != null ? SDF2.format(ah.getXdate()) : "",
					StringUtils.isNotBlank(ah.getXstatus()) ? ah.getXstatus() : ""
				});
			}
		}

		result.setData(data);
		result.setValueindex(0);
		result.setPromptindex(new int[] {0});
		result.setTableMood(true);

		
		return result;
	}

	@GetMapping("/confirmedgrn/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllConfirmedGRN(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<SearchSuggestResult> list = new ArrayList<>();
		List<PogrnHeader> grns = pogrnService.searchGrn(hint, "Confirmed");
		grns.stream().forEach(g -> list.add(new SearchSuggestResult(g.getXgrnnum(), g.getXgrnnum())));
		return list;
	}

	@GetMapping("/opordcaitem/{xordernum}/{hint}")
	public @ResponseBody List<SearchSuggestResult> getSalesOrderAvailableItem(@PathVariable String xordernum, @PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<SearchSuggestResult> list = new ArrayList<>();
		List<Oporddetail> details = opordService.searchSalesOrderAvailableItem(xordernum, hint);
		details.stream().forEach(d -> list.add(new SearchSuggestResult(d.getXitem().concat("|").concat(String.valueOf(d.getXrow())).concat("|").concat(d.getXqtyord().subtract(d.getXqtydel() == null ? BigDecimal.ZERO : d.getXqtydel()).toString()), d.getXrow() + ". " + d.getXitem() + " - " + d.getItemname())));
		return list;
	}

	@GetMapping("/poordcaitem/{xpornum}/{hint}")
	public @ResponseBody List<SearchSuggestResult> getPurchaseOrderAvailableItem(@PathVariable String xpornum, @PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<SearchSuggestResult> list = new ArrayList<>();
		List<PoordDetail> details = poordService.searchPurchaseOrderAvailableItem(xpornum, hint);
		details.stream().forEach(d -> list.add(new SearchSuggestResult(d.getXitem().concat("|").concat(String.valueOf(d.getXrow())).concat("|").concat(d.getXqtyord().subtract(d.getXqtygrn() == null ? BigDecimal.ZERO : d.getXqtygrn()).toString()), d.getXrow() + ". " + d.getXitem() + " - " + d.getItemname())));
		return list;
	}

	@GetMapping("/account/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAccount(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Acmst> acgroups = acmstServicee.searchByXaccORXdesc(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		acgroups.stream().forEach(a -> list.add(new SearchSuggestResult(a.getXacc(), a.getXacc() + " - " + a.getXdesc())));
		return list;
	}

	@GetMapping("/acgroup/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAcgroup(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<AccountGroup> acgroups = accountGroupService.searchByCodeOrName(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		acgroups.stream().forEach(a -> list.add(new SearchSuggestResult(a.getXagcode(), a.getXagcode() + " - " + a.getXagname() + " - " + a.getXagtype())));
		return list;
	}

	@GetMapping("/staff/{hint}")
	public @ResponseBody List<SearchSuggestResult> getStaff(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Pdmst> pdmstList = pdmstService.searchStaff(hint, TransactionCodeType.EMPLOYEE_NUMBER.getCode());
		List<SearchSuggestResult> list = new ArrayList<>();
		pdmstList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXstaff(), c.getXstaff() + " - " + c.getXname())));
		return list;
	}
	
	@GetMapping("/depend/{hint}")
	public @ResponseBody List<SearchSuggestResult> getDependsOn(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cawhfact> taskList = cawhfactService.searchDependsOn(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		taskList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXtrnnum(), c.getXtrnnum()+ " - " + c.getXname())));
		return list;
	}
	
	@GetMapping("/depend-issue/{hint}")
	public @ResponseBody List<SearchSuggestResult> getDependsOnIssue(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cawhfact> taskList = cawhfactIssueService.searchDependsOn(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		taskList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXtrnnum(), c.getXtrnnum()+ " - " + c.getXname())));
		return list;
	}
	
	@GetMapping("/payrollstaff/{hint}")
	public @ResponseBody List<SearchSuggestResult> getPayrollStaff(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Pdmst> pdmstList = pdmstService.searchPayrollStaff(hint, TransactionCodeType.PAYROLL_EMPLOYEE_ID.getCode());
		List<SearchSuggestResult> list = new ArrayList<>();
		pdmstList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXstaff(), c.getXstaff() + " - " + c.getXname())));
		return list;
	}

	@GetMapping("/supplier/{hint}")
	public @ResponseBody List<SearchSuggestResult> getSuppliers(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cacus> cacusList = cacusService.searchCacus(TransactionCodeType.SUPPLIER_NUMBER.getCode(), hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}
	
	@GetMapping("/confirmedinvoice/{hint}")
	public @ResponseBody List<SearchSuggestResult> getInvoices(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Opdoheader> opdoList = opdoService.searchOpdoHeader(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), "Confirmed", hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		opdoList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXdornum(), c.getXdornum())));
		return list;
	}


	@GetMapping("/customer/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCustomers(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cacus> cacusList = cacusService.searchCacus(TransactionCodeType.CUSTOMER_NUMBER.getCode(), hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}

	@GetMapping("/party/{hint}")
	public @ResponseBody List<SearchSuggestResult> getParty(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cacus> cacusList = cacusService.searchParty(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}
	
	@GetMapping("/member/{hint}")
	public @ResponseBody List<SearchSuggestResult> getMemberss(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cacus> memList = cacusService.searchMember(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		memList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}
	
	@GetMapping("/memberdir/{hint}")
	public @ResponseBody List<SearchSuggestResult> getMemberDir(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cacus> memList = cacusService.searchMemberDir(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		memList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}
	
	@GetMapping("/landId/{hint}")
	public @ResponseBody List<SearchSuggestResult> getLandId(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		
		List<LandInfo> LandList = landInfoService.searchLandId(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		LandList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXland(),c.getXland()+ " - " + c.getXlname())));
		return list;
	}
	
	@GetMapping("/memLandId/{hint}")
	public @ResponseBody List<SearchSuggestResult> getMemLandId(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		
		List<LandInfo> LandList = landInfoService.searchMemLand(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		LandList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXland(),c.getXland()+ " - " + c.getXlname())));
		return list;
	}
	
	
//	  @GetMapping("/memberId/{hint}") public @ResponseBody
//	  List<SearchSuggestResult> getMemnerId(@PathVariable String hint){
//	  if(StringUtils.isBlank(hint)) return Collections.emptyList();
//	  
//	  List<LandMemberInfo> MemberList = landMemberInfoService.searchMemberId(hint);
//	  List<SearchSuggestResult> list = new ArrayList<>();
//	  MemberList.stream().forEach(c -> list.add(new
//	  SearchSuggestResult(c.getXmember(),c.getXmember()))); return list; }
	 
	
	@GetMapping("/landSur/{hint}")
	public @ResponseBody List<SearchSuggestResult> getLandDoc(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		
		List<LandSurveyor> LandDocList = landDocumentService.searchServeyorId(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		LandDocList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXsurveyor(),c.getXsurveyor())));
		return list;
	}

	@GetMapping("/committeeId/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCommitteeId(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		
		List<LandCommitteeInfo> CommitteeList = landCommitteeInfoService.searchCommitteeId(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		CommitteeList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcommittee(),c.getXcommittee())));
		return list;
	}
	
	@GetMapping("/personId/{hint}")
	public @ResponseBody List<SearchSuggestResult> getPersonId(@PathVariable String hint){
		
		
		List<LandPerson> LandList = landPersonService.searchPersonId(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		LandList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXperson(),c.getXperson()+ " - " +c.getXname())));
		return list;
	}
	
	@GetMapping("/surveyor/{hint}")
	public @ResponseBody List<SearchSuggestResult> getSurveyor(@PathVariable String hint){
		
		
		List<LandSurveyor> SurveyorList = landSurveyorService.searchSurveyor(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		SurveyorList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXsurveyor(),c.getXsurveyor()+ " - " +c.getXname())));
		return list;
	}
	
	@GetMapping("/purpose/{hint}")
	public @ResponseBody List<SearchSuggestResult> getpurpose(@PathVariable String hint){
		List<Arhed> ArhedList = arhedService.searchPurpose(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		ArhedList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXpurpose(),c.getXpurpose())));
		return list;
	}
	
	@GetMapping("/bank/{hint}")
	public @ResponseBody List<SearchSuggestResult> getBank(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cabank> bankList = cabankService.searchBank(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		bankList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXbank(),c.getXbank()+ " - " +c.getXname())));
		return list;
	}
	
	
	/*
	 * @GetMapping("/customerId/{hint}") public @ResponseBody
	 * List<SearchSuggestResult> getCustomerId(@PathVariable String hint){
	 * 
	 * 
	 * List<Cacus> CusList = cacusService.searchCustomer(hint);
	 * List<SearchSuggestResult> list = new ArrayList<>();
	 * CusList.stream().forEach(c -> list.add(new
	 * SearchSuggestResult(c.getXcus(),c.getXcus()+ " - " +c.getXorg()))); return
	 * list; }
	 */

	@GetMapping("/cus/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCus(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cacus> cusList = cacusService.searchCus(TransactionCodeType.CUSTOMER_NUMBER.getCode(), hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		cusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}
	
	@GetMapping("/branchCustomer/{hint}")
	public @ResponseBody List<SearchSuggestResult> getBranchCustomers(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cacus> branchCusList = cacusService.searchCustomer(TransactionCodeType.CUSTOMER_NUMBER.getCode(), hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		branchCusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}

	@GetMapping("/personName/{hint}")
	public @ResponseBody List<SearchSuggestResult> getPersonName(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		
		List<LandPerson> PersonName = landOwnerService.searchPersonName(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		PersonName.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXname(),c.getXname())));
		return list;
	}

	@GetMapping("/caitem/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/assetCaitem/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAssetCaitems(@PathVariable String hint){
		List<Caitem> assetCaitemList = caitemService.searchAssetCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		assetCaitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/central/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCentralCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchCentralCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/nonServiceCaitem/{hint}")
	public @ResponseBody List<SearchSuggestResult> getNonServiceCaitem(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchNonServiceCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/requisition/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCentralCaitemsForRequisitions(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchCentralCaitemForRequisition(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}


	@GetMapping("/caitem/finishednprod/{hint}")
	public @ResponseBody List<SearchSuggestResult> getFinsihedNProductionCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchFinishedProductionCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/rawmaterialprod/{hint}")
	public @ResponseBody List<SearchSuggestResult> getRawMaterialCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchRawMaterialsCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/withoutproduction/{hint}")
	public @ResponseBody List<SearchSuggestResult> getWithoutProductionCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.getWithoutProductionCaitems(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/function/{hint}")
	public @ResponseBody List<SearchSuggestResult> getConventionFunctions(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.getFunctionItems(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/food/{hint}")
	public @ResponseBody List<SearchSuggestResult> getConventionFoods(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.getFoodItems(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/food/withoutset/{hint}")
	public @ResponseBody List<SearchSuggestResult> getNonSetItem(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.getFoodItems(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().filter(c -> !c.isXsetmenu()).forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/{xitem}/subitem/{hint}")
	public @ResponseBody List<SearchSuggestResult> getItemAsSubitem(@PathVariable String xitem, @PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().filter(c -> !xitem.equalsIgnoreCase(c.getXitem())).forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/salesorderchalan/confirmed/{hint}")
	public @ResponseBody List<SearchSuggestResult> getOnlyConfirmedSalesOrderChalan(@PathVariable String hint){
		List<Opordheader> chalans = opordService.searchOpordheaderByXtypetrnAndXtrnAndXordernum(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), hint, "Confirmed");
		List<SearchSuggestResult> list = new ArrayList<>();
		chalans.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXordernum(), c.getXordernum())));
		return list;
	}


	/*
	 * @GetMapping("/site/{hint}") public @ResponseBody List<SearchSuggestResult>
	 * getSite(@PathVariable String hint){ if(StringUtils.isBlank(hint)) return
	 * Collections.emptyList();
	 * 
	 * List<Cawh> siteList = cawhService.searchSite(hint); List<SearchSuggestResult>
	 * list = new ArrayList<>(); siteList.stream().forEach(c -> list.add(new
	 * SearchSuggestResult(c.getXwh(),c.getXwh() + " - " + c.getXname()))); return
	 * list; }
	 */
	
	@GetMapping("/site/{hint}")
	public @ResponseBody List<SearchSuggestResult> getSite(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Xcodes> siteList = xcodesService.searchSite(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		siteList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcode(), c.getXcode() + " - " + c.getXlong())));
		return list;
	}
	
	@GetMapping("/project/{hint}")
	public @ResponseBody List<SearchSuggestResult> getProject(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Xcodes> siteList = xcodesService.searchProject(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		siteList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcode(), c.getXcode() + " - " + c.getXlong())));
		return list;
	}
	@GetMapping("/allproject/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllProject(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Xcodes> siteList = xcodesService.searchAllProject(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		siteList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcode(), c.getXcode() + " - " + c.getXlong())));
		return list;
	}
	@GetMapping("/caproject/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCaProject(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Caproject> siteList = caprojectService.searchProjectFromCaproject(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		siteList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXproject(), c.getXproject() + " - " + c.getXdesc())));
		return list;
	}
	// REPORT
	@GetMapping("/report/party/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllCustomerAndSupplier(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();

		List<Cacus> supList = cacusService.searchCacus(TransactionCodeType.SUPPLIER_NUMBER.getCode(), hint);
		supList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));

		List<Cacus> cusList = cacusService.searchCacus(TransactionCodeType.CUSTOMER_NUMBER.getCode(), hint);
		cusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));

		return list;
	}
	
	@GetMapping("/report/chalan/{hint}")
	public @ResponseBody List<SearchSuggestResult> getChalan(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		productionSuggestionService.searchClananNumbers(hint).stream().forEach(c -> list.add(new SearchSuggestResult(c, c)));

		return list;
	}
	
	@GetMapping("/report/ponumber/{hint}")
	public @ResponseBody List<SearchSuggestResult> getPonumber(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		pogrnService.searchPoord(hint).stream().forEach(c -> list.add(new SearchSuggestResult(c.getXpornum(),c.getXpornum())));
		return list;
	}
	
	@GetMapping("/report/cus/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllCustomer(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();

		List<Cacus> cusList = cacusService.searchCacus(TransactionCodeType.CUSTOMER_NUMBER.getCode(), hint);
		cusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));

		return list;
	}
	
	@GetMapping("/report/sup/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllSupplier(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();

		List<Cacus> supList = cacusService.searchCacus(TransactionCodeType.SUPPLIER_NUMBER.getCode(), hint);
		supList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));

		return list;
	}
	
	@GetMapping("/report/xorg/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllXorg(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		cacusService.searchXorg(hint).stream().forEach(c -> list.add(new SearchSuggestResult(c.getXorg(),c.getXorg())));
		return list;
	}
	
	@GetMapping("/report/xgcus/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllXgcus(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		cacusService.searchXgcus(hint).stream().forEach(c -> list.add(new SearchSuggestResult(c.getXgcus(),c.getXgcus())));
		return list;
	}
	
	@GetMapping("/report/stock/xitem/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllXitem(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<SearchSuggestResult> list = new ArrayList<>();
		List<Caitem> caitemList = caitemService.searchCaitem(hint);
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}
	
	@GetMapping("/report/xbomkey/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllXbomkey(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		bmbomService.searchXbom(hint).stream().forEach(c -> list.add(new SearchSuggestResult(c.getXbomkey(),c.getXbomkey())));
		return list;
	}
	
	@GetMapping("/report/poor/xpornum/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllpornum(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		poordService.searchXpornum(hint).stream().forEach(c -> list.add(new SearchSuggestResult(c.getXpornum(),c.getXpornum())));
		return list;
	}
	@GetMapping("/report/opord/xpornum/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllxpornum(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<SearchSuggestResult> list = new ArrayList<>();
		opordService.searchXpornum(hint).stream().forEach(c -> list.add(new SearchSuggestResult(c.getXpornum(),c.getXpornum())));
		return list;
	}
	
	@GetMapping("/report/opdo/xdornum/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllxdornum(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<SearchSuggestResult> list = new ArrayList<>();
		opdoService.findOpdoXdornum(hint).stream().forEach(c -> list.add(new SearchSuggestResult(c.getXdornum(),c.getXdornum())));
		return list;
	}
	
	@GetMapping("/report/caitemname/{hint}")
	public @ResponseBody List<SearchSuggestResult> getItemName(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchItemName(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXdesc(), c.getXdesc())));
		return list;
	}
	
	@GetMapping("/report/xstatuscrn/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCrnstatus(@PathVariable String hint){
		List<Pocrnheader> crnStatusList = pocrnService.findPocrnXstatuscrn(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		crnStatusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXstatuscrn(), c.getXstatuscrn())));
		return list;
	}
	@GetMapping("/report/xbatch/{hint}")
	public @ResponseBody List<SearchSuggestResult> getXbatch(@PathVariable String hint){
		List<Moheader> xbatchList = MoService.findModetailXbatch(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		xbatchList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXbatch(), c.getXbatch())));
		return list;
	}
	
	@GetMapping("/report/xtype/{hint}")
	public @ResponseBody List<SearchSuggestResult> getXtype(@PathVariable String hint){
		List<Modetail> xtypeList = MoService.findModetailByXtype(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		xtypeList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXtype(), c.getXtype())));
		return list;
	}
	
	@GetMapping("/report/xstaff/{hint}")
	public @ResponseBody List<SearchSuggestResult> getXstaff(@PathVariable String hint){
		List<Pdmst> xstaffList = arhedService.findByXstaff(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		xstaffList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXstaff(), c.getXstaff() + " - " + c.getXname())));
		return list;
	}
	
	@GetMapping("/report/xvoucher/{hint}")
	public @ResponseBody List<SearchSuggestResult> getXvoucher(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<SearchSuggestResult> list = new ArrayList<>();
		List<Acheader> xvoucherList = acService.getAllVoucherNumber(hint);
		xvoucherList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXvoucher(), c.getXvoucher())));
		return list;
	}
	
	@GetMapping("/report/xacc/{hint}")
	public @ResponseBody List<SearchSuggestResult> getXacc(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<SearchSuggestResult> list = new ArrayList<>();
		List<Acmst> xaccList = acmstServicee.getAllAcc(hint);
		xaccList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXacc(), c.getXacc() + " - " + c.getXdesc())));
		return list;
	}
	
	@GetMapping("/report/xsub/{hint}")
	public @ResponseBody List<SearchSuggestResult> getXsub(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Acmst> xsubList = acmstServicee.getAllSub(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		xsubList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXsub(), c.getXsub() + " - " + c.getXorg())));
		return list;
	}
	
	@GetMapping("/report/xstatusjv/{hint}")
	public @ResponseBody List<SearchSuggestResult> getXstatusjv(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		
		List<Acheader> xstatusjvList = acService.getAllStatusNumber();
		List<SearchSuggestResult> list = new ArrayList<>();
		xstatusjvList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXstatusjv(), c.getXstatusjv())));
		return list;
	}
	
	@GetMapping("/report/xstatus/{hint}")
	public @ResponseBody List<SearchSuggestResult> getXstatus(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		
		List<Acheader> xstatusList = acService.getAllStatusNumber();
		List<SearchSuggestResult> list = new ArrayList<>();
		xstatusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXstatus(), c.getXstatus())));
		return list;
	}
	@GetMapping("/report/zorg/{hint}")
	public @ResponseBody List<SearchSuggestResult> getZorg(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		
		List<Cacus> zorgList = businessService.getBusinessName(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		zorgList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus()+" - "+c.getXorg())));
		return list;
	}
	
}
