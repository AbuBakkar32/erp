package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.Caitemdetail;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.Vatait;
import com.asl.entity.Zbusiness;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.ConventionHallBookingReport;
import com.asl.model.report.HallBookingFacilitiesDetail;
import com.asl.model.report.HallBookingFoodDetail;
import com.asl.model.report.HallBookingHeader;
import com.asl.model.report.HallBookingSubItems;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.HallBookingService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;
import com.asl.service.VataitService;
import com.asl.util.CKTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/conventionmanagement/hallbooking")
public class ConventionHallBookingController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private VataitService vataitService;
	@Autowired private CaitemService caitemService;
	@Autowired private HallBookingService hallBookingService;
	@Autowired private CacusService cacusService;
	@Autowired private OpdoService opdoService;

	@GetMapping
	public String loadBookingPage(Model model) {
		model.addAttribute("hallbookingpreffix", xtrnService.findByXtypetrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("opordheader", getDefaultOpordHeader());
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("bookingOrderList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode(), TransactionCodeType.HALL_BOOKING_SALES_ORDER.getdefaultCode()));
		model.addAttribute("paymentType", xcodesService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("paymentMode", xcodesService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		return "pages/conventionmanagement/hallbooking/opord";
	}

	@GetMapping("/{xordernum}")
	public String loadBookingPage(@PathVariable String xordernum, Model model) {
		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if (oh == null) return "redirect:/conventionmanagement/hallbooking";

		model.addAttribute("opordheader", oh);
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("bookingOrderList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode(), TransactionCodeType.HALL_BOOKING_SALES_ORDER.getdefaultCode()));
		model.addAttribute("paymentType", xcodesService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("paymentMode", xcodesService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));

		List<Oporddetail> detail = opordService.findOporddetailByXordernum(xordernum);
		model.addAttribute("facilities", detail.stream().filter(d -> "Facilities".equalsIgnoreCase(d.getXcatitem())).collect(Collectors.toList()));

		List<Oporddetail> mainfoodList = detail.stream().filter(d -> "Food".equalsIgnoreCase(d.getXcatitem()) && !"Set Item".equalsIgnoreCase(d.getXtype())).collect(Collectors.toList());
		List<Oporddetail> subfoodList = detail.stream().filter(d -> "Food".equalsIgnoreCase(d.getXcatitem()) && "Set Item".equalsIgnoreCase(d.getXtype())).collect(Collectors.toList());
		for(Oporddetail m : mainfoodList) {
			for(Oporddetail s : subfoodList) {
				if(m.getXrow() == s.getXparentrow()) {
					m.getSubitems().add(s);
				}
			}
		}
		model.addAttribute("foods", mainfoodList);
		return "pages/conventionmanagement/hallbooking/opord";
	}

	public Opordheader getDefaultOpordHeader() {
		Opordheader oh = new Opordheader();
		oh.setXtypetrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode());
		oh.setXtotguest(0);
		oh.setXstarttime("00:01");
		oh.setXendtime("23:59");
		oh.setXhallamt(BigDecimal.ZERO);
		oh.setXfunctionamt(BigDecimal.ZERO);
		oh.setXfoodamt(BigDecimal.ZERO);
		oh.setXfacamt(BigDecimal.ZERO);
		oh.setXtotamt(BigDecimal.ZERO);
		oh.setXvatait("No Vat");
		oh.setXvatamt(BigDecimal.ZERO);
		oh.setXaitamt(BigDecimal.ZERO);
		oh.setXdiscamt(BigDecimal.ZERO);
		oh.setXgrandtot(BigDecimal.ZERO);
		oh.setXstatus("Open");
		oh.setXpaid(BigDecimal.ZERO);
		oh.setXdue(BigDecimal.ZERO);
		return oh;
	}


	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opordheader opordheader, BindingResult bindingResult, Model model) {
		if (opordheader == null || StringUtils.isBlank(opordheader.getXtypetrn()) || StringUtils.isBlank(opordheader.getXtrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// set default
		if(StringUtils.isBlank(opordheader.getXordernum())) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			opordheader.setXdate(cal.getTime());
		}

		// Validation
		if(StringUtils.isBlank(opordheader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer name required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(opordheader.getXfunction())) {
			responseHelper.setErrorStatusAndMessage("Function selection required");
			return responseHelper.getResponse();
		}
		if(opordheader.getXtotguest() <= 0) {
			responseHelper.setErrorStatusAndMessage("Guest Quantity invalid");
			return responseHelper.getResponse();
		}
		if(opordheader.getXstartdate() == null) {
			responseHelper.setErrorStatusAndMessage("Start date required");
			return responseHelper.getResponse();
		}
		if(opordheader.getXstarttime() == null) {
			responseHelper.setErrorStatusAndMessage("Start time required");
			return responseHelper.getResponse();
		}
		if(opordheader.getXenddate() == null) {
			responseHelper.setErrorStatusAndMessage("End date required");
			return responseHelper.getResponse();
		}
		if(opordheader.getXendtime()== null) {
			responseHelper.setErrorStatusAndMessage("End time required");
			return responseHelper.getResponse();
		}

		Calendar stdt  = Calendar.getInstance();
		stdt.setTime(opordheader.getXstartdate());
		stdt.set(Calendar.HOUR_OF_DAY, new CKTime(opordheader.getXstarttime()).getHour());
		stdt.set(Calendar.MINUTE, new CKTime(opordheader.getXstarttime()).getMinute());
		stdt.set(Calendar.SECOND, 0);

		Calendar endt  = Calendar.getInstance();
		endt.setTime(opordheader.getXenddate());
		endt.set(Calendar.HOUR_OF_DAY, new CKTime(opordheader.getXendtime()).getHour());
		endt.set(Calendar.MINUTE, new CKTime(opordheader.getXendtime()).getMinute());
		endt.set(Calendar.SECOND, 0);

		if(stdt.getTime().after(endt.getTime())) {
			responseHelper.setErrorStatusAndMessage("Start date can't be after End date");
			return responseHelper.getResponse();
		}

		if(opordheader.getXdate().after(stdt.getTime())) {
			responseHelper.setErrorStatusAndMessage("Booking date can't be after Start date");
			return responseHelper.getResponse();
		}

		if(opordheader.getXhallamt() == null) opordheader.setXhallamt(BigDecimal.ZERO);
		if(opordheader.getXfunctionamt() == null) opordheader.setXfunctionamt(BigDecimal.ZERO);
		if(opordheader.getXfoodamt() == null) opordheader.setXfoodamt(BigDecimal.ZERO);
		if(opordheader.getXfacamt() == null) opordheader.setXfacamt(BigDecimal.ZERO);
		if(opordheader.getXtotamt() == null) {
			BigDecimal tot = opordheader.getXhallamt().add(opordheader.getXfunctionamt()).add(opordheader.getXfoodamt()).add(opordheader.getXfacamt());
			opordheader.setXtotamt(tot);
		}
		if(opordheader.getXdiscamt() == null) opordheader.setXdiscamt(BigDecimal.ZERO);
		if(StringUtils.isBlank(opordheader.getXvatait())) opordheader.setXvatait("No Vat");

		Vatait vatait = vataitService.findVataitByXvatait(opordheader.getXvatait());
		if(vatait != null) {
			if(opordheader.getXvatamt() == null) opordheader.setXvatamt((opordheader.getXtotamt().multiply(vatait.getXvat())).divide(BigDecimal.valueOf(100)));
			if(opordheader.getXaitamt() == null) opordheader.setXaitamt((opordheader.getXtotamt().multiply(vatait.getXait())).divide(BigDecimal.valueOf(100)));
		} else {
			if(opordheader.getXvatamt() == null) opordheader.setXvatamt((opordheader.getXtotamt().multiply(BigDecimal.ZERO)).divide(BigDecimal.valueOf(100)));
			if(opordheader.getXaitamt() == null) opordheader.setXaitamt((opordheader.getXtotamt().multiply(BigDecimal.ZERO)).divide(BigDecimal.valueOf(100)));
		}

		BigDecimal grandTotal = (opordheader.getXtotamt().add(opordheader.getXvatamt()).add(opordheader.getXaitamt())).subtract(opordheader.getXdiscamt());
		if(opordheader.getXgrandtot() == null) opordheader.setXgrandtot(grandTotal);

		if(opordheader.getXpaid() == null) opordheader.setXpaid(BigDecimal.ZERO);
		opordheader.setXdue(opordheader.getXgrandtot().subtract(opordheader.getXpaid()));

		// if existing then update
		Opordheader existOh = opordService.findOpordHeaderByXordernum(opordheader.getXordernum());
		if (existOh != null) {
			BeanUtils.copyProperties(opordheader, existOh, "xtypetrn", "xtrn", "xordernum");

			// before update validate hall availability
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			String xstartdate = sdf.format(existOh.getXstartdate()).toUpperCase().concat(" ").concat(existOh.getXstarttime());
			String xenddate = sdf.format(existOh.getXenddate()).toUpperCase().concat(" ").concat(existOh.getXendtime());
			List<String> bookedHalls = hallBookingService.allBookedHallsInDateRange(xstartdate, xenddate, null);
			if(bookedHalls != null && !bookedHalls.isEmpty()) {
				for(String book : bookedHalls) {
					if(!book.equalsIgnoreCase(existOh.getXordernum())) {
						responseHelper.setErrorStatusAndMessage("Booking time not available");
						return responseHelper.getResponse();
					}
				}
			}


			long count = opordService.updateOpordHeader(existOh);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Booking");
				return responseHelper.getResponse();
			}

			responseHelper.setRedirectUrl("/conventionmanagement/hallbooking/" + existOh.getXordernum());
			responseHelper.setSuccessStatusAndMessage("Booking order updated successfully");
			return responseHelper.getResponse();
		}

		// if new record
		// before save validate hall availability
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String xstartdate = sdf.format(opordheader.getXstartdate()).toUpperCase().concat(" ").concat(opordheader.getXstarttime());
		String xenddate = sdf.format(opordheader.getXenddate()).toUpperCase().concat(" ").concat(opordheader.getXendtime());
		List<String> bookedHalls = hallBookingService.allBookedHallsInDateRange(xstartdate, xenddate, null);
		if(bookedHalls != null && !bookedHalls.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Booking time not available");
			return responseHelper.getResponse();
		}

		long count = opordService.saveOpordHeader(opordheader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Booking order not created");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/conventionmanagement/hallbooking/" + opordheader.getXordernum());
		responseHelper.setSuccessStatusAndMessage("Booking Order created successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xordernum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xordernum) {
		return doArchiveOrRestore(xordernum, true);
	}

	public Map<String, Object> doArchiveOrRestore(String xordernum, boolean archive) {
		Opordheader opordHeader = opordService.findOpordHeaderByXordernum(xordernum);
		if (opordHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Remove all detail first
		if(!opordService.findOporddetailByXordernum(xordernum).isEmpty()) {
			long count = opordService.archiveAllOporddetailByXordernum(xordernum);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't archive booking details");
				return responseHelper.getResponse();
			}
		}

		opordHeader.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = opordService.updateOpordHeader(opordHeader);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Booking updated successfully");
		responseHelper.setRedirectUrl("/conventionmanagement/hallbooking/" + opordHeader.getXordernum());
		return responseHelper.getResponse();
	}

	@GetMapping("{xordernum}/oporddetail/{xrow}/show")
	public String openOpordDetailModal(@PathVariable String xordernum, @PathVariable String xrow, Model model) {

		Map<String, List<Caitem>> map = new HashMap<>();
		List<Caitem> facList = caitemService.findByXcatitem("Facilities");
		facList.sort(Comparator.comparing(Caitem::getXdesc));
		map.put("Facilities", facList);

		List<Oporddetail> details = opordService.findOporddetailByXordernum(xordernum);
		if(details != null && !details.isEmpty()) {
			for(Oporddetail d : details) {
				for(Map.Entry<String, List<Caitem>> m : map.entrySet()) {
					for(Caitem c : m.getValue()) {
						if(c.getXitem().equalsIgnoreCase(d.getXitem())) {
							c.setSelected(true);
						}
					}
				}
			}
		}

		model.addAttribute("itemMap", map);
		return "pages/conventionmanagement/hallbooking/oporddetailmodal::oporddetailmodal";
	}


	@PostMapping("/oporddetails/save")
	public @ResponseBody Map<String, Object> saveOporddetail(@RequestParam(value="xitems[]") String[] xitems, @RequestParam(value="xordernum") String xordernum) {
		if(xitems == null) {
			responseHelper.setErrorStatusAndMessage("Items not found to add");
			return responseHelper.getResponse();
		}

		List<String> xitemsL = new ArrayList<>(Arrays.asList(xitems));

		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			responseHelper.setErrorStatusAndMessage("Booking header "+ xordernum +" not found");
			return responseHelper.getResponse();
		}

		// validation
		List<Oporddetail> deletableDL = new ArrayList<>();
		List<Oporddetail> existDetails = opordService.findOporddetailByXordernum(xordernum).stream().filter(f -> "Facilities".equalsIgnoreCase(f.getXcatitem())).collect(Collectors.toList());
		if(existDetails != null && !existDetails.isEmpty()) {
			for(Oporddetail d :  existDetails) {
				// if already added in db then remove from list
				if(xitemsL.contains(d.getXitem())) {
					xitemsL.remove(xitemsL.indexOf(d.getXitem()));
				} else { // if d is not in list, then delete from db
					deletableDL.add(d);
				}
			}
		}

		List<Oporddetail> details = new ArrayList<>();
		for(String item : xitemsL) {
			Caitem caitem = caitemService.findByXitem(item);
			if(caitem == null) {
				responseHelper.setErrorStatusAndMessage("Item " + item + " not found to add");
				return responseHelper.getResponse();
			}

			Oporddetail detail = new Oporddetail();
			detail.setXordernum(xordernum);
			detail.setXunit(caitem.getXunitpur());
			detail.setXitem(caitem.getXitem());
			detail.setXgitem(caitem.getXgitem());
			detail.setXcatitem(caitem.getXcatitem());
			detail.setXdesc(caitem.getXdesc());
			detail.setXqtyord(BigDecimal.ONE);
			detail.setXrate(caitem.getXrate());
			detail.setXlineamt(detail.getXqtyord().multiply(detail.getXrate()));
			details.add(detail);
		}

		// delete first
		if(!deletableDL.isEmpty()) {
			long dcount = opordService.batchDeleteOpordDetail(deletableDL);
			if(dcount == 0) {
				responseHelper.setErrorStatusAndMessage("Can't delete previous selected items which is not selected now");
				return responseHelper.getResponse();
			}
		}

		// save 
		if(!details.isEmpty()) {
			long count = opordService.saveBatchOpordDetail(details);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save items");
				return responseHelper.getResponse();
			}
		}

		responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/conventionmanagement/hallbooking/oporddetail/" + oh.getXordernum());
		responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/conventionmanagement/hallbooking/opordheaderform/" + oh.getXordernum());
		responseHelper.setSuccessStatusAndMessage("Items saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/oporddetail/{xordernum}")
	public String reloadOpdoDetailTable(@PathVariable String xordernum, Model model) {
		List<Oporddetail> detail = opordService.findOporddetailByXordernum(xordernum);
		model.addAttribute("facilities", detail.stream().filter(d -> "Facilities".equalsIgnoreCase(d.getXcatitem())).collect(Collectors.toList()));
		model.addAttribute("opordheader", opordService.findOpordHeaderByXordernum(xordernum));
		return "pages/conventionmanagement/hallbooking/opord::oporddetailtable";
	}

	@GetMapping("/opordheaderform/{xordernum}")
	public String reloadOpdoheaderform(@PathVariable String xordernum, Model model) {
		model.addAttribute("hallbookingpreffix", xtrnService.findByXtypetrn(TransactionCodeType.HALL_BOOKING_SALES_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("opordheader", opordService.findOpordHeaderByXordernum(xordernum));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("paymentType", xcodesService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("paymentMode", xcodesService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		return "pages/conventionmanagement/hallbooking/opord::opordheaderform";
	}

	@GetMapping("{xordernum}/oporddetail2/{xrow}/show")
	public String openOpordDetailModal2(@PathVariable String xordernum, @PathVariable int xrow, Model model) {
		Oporddetail detail = opordService.findOporddetailByXordernumAndXrow(xordernum, xrow);
		if(detail == null) return "redirect:/conventionmanagement/hallbooking/" + xordernum;

		model.addAttribute("oporddetail", detail);
		return "pages/conventionmanagement/hallbooking/oporddetailmodal2::oporddetailmodal2";
	}

	@PostMapping("/oporddetail/save")
	public @ResponseBody Map<String, Object> saveOporddetail(Oporddetail opordDetail) {
		if (opordDetail == null || StringUtils.isBlank(opordDetail.getXordernum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if (opordDetail.getXrow() == 0 && opordService.findOporddetailByXordernumAndXitem(opordDetail.getXordernum(), opordDetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		// modify line amount
		if(opordDetail.getXqtyord() == null) opordDetail.setXqtyord(BigDecimal.ZERO);
		if(opordDetail.getXrate() == null) opordDetail.setXrate(BigDecimal.ZERO);
		opordDetail.setXlineamt(opordDetail.getXqtyord().multiply(opordDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		Oporddetail existDetail = opordService.findOporddetailByXordernumAndXrow(opordDetail.getXordernum(), opordDetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(opordDetail, existDetail, "xordernum", "xrow", "xitem");
			long count = opordService.updateOpordDetail(existDetail);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Booking detail");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Booking detail updated successfully");
			responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/conventionmanagement/hallbooking/oporddetail/" + opordDetail.getXordernum());
			responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/conventionmanagement/hallbooking/opordheaderform/" + opordDetail.getXordernum());

			return responseHelper.getResponse();
		}

		// if new detail
		long count = opordService.saveOpordDetail(opordDetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/conventionmanagement/hallbooking/oporddetail/" + opordDetail.getXordernum());
		responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/conventionmanagement/hallbooking/opordheaderform/" + opordDetail.getXordernum());
		return responseHelper.getResponse();
	}

	@PostMapping("{xordernum}/oporddetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpordDetail(@PathVariable String xordernum, @PathVariable String xrow, Model model) {
		Oporddetail od = opordService.findOporddetailByXordernumAndXrow(xordernum, Integer.parseInt(xrow));
		if (od == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = opordService.deleteOpordDetail(od);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/conventionmanagement/hallbooking/oporddetail/" + xordernum);
		responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/conventionmanagement/hallbooking/opordheaderform/" + xordernum);
		return responseHelper.getResponse();
	}

	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}

	@GetMapping("/food/{xordernum}/oporddetail/{xrow}/show")
	public String openFoodOpordDetailModal(@PathVariable String xordernum, @PathVariable String xrow, Model model) {

		Oporddetail detail = null;

		if(!"new".equalsIgnoreCase(xrow)) {
			detail = opordService.findOporddetailByXordernumAndXrow(xordernum, Integer.parseInt(xrow));
			if(detail != null) {
				// find sub items too
				List<Oporddetail> list = opordService.findAllSubitemDetail(xordernum, detail.getXrow(), "Set Item");
				model.addAttribute("oporddetail", detail);
				model.addAttribute("subitems", list != null ? list : Collections.emptyList());
				return "pages/conventionmanagement/hallbooking/oporddetailfoodmodal::oporddetailfoodmodal";
			}
		}

		detail = new Oporddetail();
		detail.setXordernum(xordernum);

		model.addAttribute("oporddetail", detail);
		model.addAttribute("subitems", Collections.emptyList());
		return "pages/conventionmanagement/hallbooking/oporddetailfoodmodal::oporddetailfoodmodal";
	}

	@GetMapping("/subitemdetails/{xitem}")
	public String loadSubitemTable(@PathVariable String xitem, Model model) {
		List<Caitemdetail> cdetails = caitemService.findCaitemdetailByXitem(xitem);
		List<Oporddetail> subitems = new ArrayList<>();
		for(Caitemdetail c : cdetails) {
			Oporddetail o = new Oporddetail();
			o.setXitem(c.getXsubitem());
			o.setXdesc(c.getXdesc());
			o.setXqtyord(c.getXqtyord());
			o.setXunit(c.getXunit());
			subitems.add(o);
		}

		model.addAttribute("subitems", subitems);
		model.addAttribute("oporddetail", caitemService.findByXitem(xitem));
		return "pages/conventionmanagement/hallbooking/oporddetailfoodmodal::oporddetailfoodmodaltable";
	}


	@PostMapping(value = "/foodoporddetail/save", headers="Accept=application/json")
	public @ResponseBody Map<String, Object> saveFoodOporddetail(@RequestBody String json){

		Oporddetail oporddetail = new Oporddetail();
		List<Oporddetail> subdetails = new ArrayList<>();
		ObjectMapper obm = new ObjectMapper();
		try {
			oporddetail = obm.readValue(json, Oporddetail.class);
			JsonNode rootNode = obm.readTree(json);

			JsonNode itemsNode = rootNode.get("subitems");
			TypeFactory typeFactory = obm.getTypeFactory();
			CollectionType cType = typeFactory.constructCollectionType(List.class, Oporddetail.class);
			subdetails = obm.readValue(itemsNode.toString(), cType);
		} catch (JsonProcessingException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		// validation
		List<Oporddetail> exists = opordService.findAllOporddetailByXordernumAndXitem(oporddetail.getXordernum(), oporddetail.getXitem());
		Oporddetail exist = exists.stream().filter(f -> !"Set Item".equalsIgnoreCase(f.getXtype())).collect(Collectors.toList()).stream().findFirst().orElse(null);

		// if new but exist
		if(oporddetail.getXrow() == 0 && exist != null) {
			responseHelper.setErrorStatusAndMessage("Item detail already added");
			return responseHelper.getResponse();
		}

		if(oporddetail.getXrow() != 0 && exist == null) {
			responseHelper.setErrorStatusAndMessage("Item detail not found in this system");
			return responseHelper.getResponse();
		}

		// if new
		if(oporddetail.getXrow() == 0 && exist == null) {
			// save main item first
			Caitem caitem = caitemService.findByXitem(oporddetail.getXitem());
			if(caitem == null) {
				responseHelper.setErrorStatusAndMessage("Item not found in this system");
				return responseHelper.getResponse();
			}

			oporddetail.setXdesc(caitem.getXdesc());
			oporddetail.setXcatitem(caitem.getXcatitem());
			oporddetail.setXgitem(caitem.getXgitem());
			if(oporddetail.getXrate() == null) oporddetail.setXrate(BigDecimal.ZERO);
			if(oporddetail.getXqtyord() == null) oporddetail.setXqtyord(BigDecimal.ONE);
			oporddetail.setXlineamt(oporddetail.getXrate().multiply(oporddetail.getXqtyord()));
			long count = opordService.saveOpordDetail(oporddetail);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't add item detail");
				return responseHelper.getResponse();
			}

			// prepare sub item and save it
			List<Oporddetail> subitems = new ArrayList<>();
			for(Oporddetail o : subdetails) {
				Caitem c = caitemService.findByXitem(o.getXitem());
				if(c == null) continue;

				o.setXdesc(c.getXdesc());
				o.setXcatitem(c.getXcatitem());
				o.setXgitem(c.getXgitem());
				if(o.getXqtyord() == null) o.setXqtyord(BigDecimal.ONE);
				o.setXrate(BigDecimal.ZERO);
				o.setXlineamt(o.getXqtyord().multiply(o.getXrate()));
				o.setXparentrow(oporddetail.getXrow());
				o.setXtype("Set Item");
				subitems.add(o);
			}
			if(!subitems.isEmpty()) {
				long count2 = opordService.saveBatchOpordDetail(subitems);
				if(count2 == 0) {
					responseHelper.setErrorStatusAndMessage("Can't save set items");
					return responseHelper.getResponse();
				}
			}

			responseHelper.setSuccessStatusAndMessage("Item detail added successfully");
			responseHelper.setReloadSectionIdWithUrl("oporddetailfoodtable", "/conventionmanagement/hallbooking/food/oporddetail/" + oporddetail.getXordernum());
			responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/conventionmanagement/hallbooking/opordheaderform/" + oporddetail.getXordernum());
			return responseHelper.getResponse();
		}


		// update main item first
		if(oporddetail.getXrate() == null) oporddetail.setXrate(BigDecimal.ZERO);
		if(oporddetail.getXqtyord() == null) oporddetail.setXqtyord(BigDecimal.ONE);
		oporddetail.setXlineamt(oporddetail.getXrate().multiply(oporddetail.getXqtyord()));
		BeanUtils.copyProperties(oporddetail, exist, "xitem", "xrow","xordernum","xunit","xcatitem","xdesc","xgitem");
		long count = opordService.updateOpordDetail(exist);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update item");
			return responseHelper.getResponse();
		}

		// Now archive previous details of sub items
		if(!opordService.findAllSubitemDetail(exist.getXordernum(), exist.getXrow(), "Set Item").isEmpty()) {
			long countd = opordService.deleteSubItems(exist.getXordernum(), exist.getXrow(), "Set Item");
			if(countd == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update sub items");
				return responseHelper.getResponse();
			}
		}

		// Now add new sub items if available
		List<Oporddetail> subitems = new ArrayList<>();
		for(Oporddetail o : subdetails) {
			Caitem c = caitemService.findByXitem(o.getXitem());
			if(c == null) continue;

			o.setXdesc(c.getXdesc());
			o.setXcatitem(c.getXcatitem());
			o.setXgitem(c.getXgitem());
			if(o.getXqtyord() == null) o.setXqtyord(BigDecimal.ONE);
			o.setXrate(BigDecimal.ZERO);
			o.setXlineamt(o.getXqtyord().multiply(o.getXrate()));
			o.setXparentrow(exist.getXrow());
			o.setXtype("Set Item");
			subitems.add(o);
		}
		if(!subitems.isEmpty()) {
			long count2 = opordService.saveBatchOpordDetail(subitems);
			if(count2 == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update set items");
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Item detail updated successfully");
		responseHelper.setReloadSectionIdWithUrl("oporddetailfoodtable", "/conventionmanagement/hallbooking/food/oporddetail/" + exist.getXordernum());
		responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/conventionmanagement/hallbooking/opordheaderform/" + exist.getXordernum());
		return responseHelper.getResponse();
	}

	@PostMapping("/food/{xordernum}/oporddetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteFoodOpordDetail(@PathVariable String xordernum, @PathVariable String xrow, Model model) {

		Oporddetail mainitem = opordService.findOporddetailByXordernumAndXrow(xordernum, Integer.parseInt(xrow));
		if(mainitem == null) {
			responseHelper.setErrorStatusAndMessage("Can't find item detail in this system");
			return responseHelper.getResponse();
		}

		// archive set item first if available
		if(!opordService.findAllSubitemDetail(mainitem.getXordernum(), mainitem.getXrow(), "Set Item").isEmpty()) {
			long count = opordService.deleteSubItems(mainitem.getXordernum(), mainitem.getXrow(), "Set Item");
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't delete items");
				return responseHelper.getResponse();
			}
		}

		// now delete main item
		long count = opordService.deleteOpordDetail(mainitem);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete item detail");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Item detail updated successfully");
		responseHelper.setReloadSectionIdWithUrl("oporddetailfoodtable", "/conventionmanagement/hallbooking/food/oporddetail/" + xordernum);
		responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/conventionmanagement/hallbooking/opordheaderform/" + xordernum);
		return responseHelper.getResponse();
	}

	@GetMapping("/food/oporddetail/{xordernum}")
	public String loadFoodOporddetailTable(@PathVariable String xordernum, Model model) {
		model.addAttribute("opordheader", opordService.findOpordHeaderByXordernum(xordernum));

		List<Oporddetail> detail = opordService.findOporddetailByXordernum(xordernum);

		List<Oporddetail> mainfoodList = detail.stream().filter(d -> "Food".equalsIgnoreCase(d.getXcatitem()) && !"Set Item".equalsIgnoreCase(d.getXtype())).collect(Collectors.toList());
		List<Oporddetail> subfoodList = detail.stream().filter(d -> "Food".equalsIgnoreCase(d.getXcatitem()) && "Set Item".equalsIgnoreCase(d.getXtype())).collect(Collectors.toList());
		for(Oporddetail m : mainfoodList) {
			for(Oporddetail s : subfoodList) {
				if(m.getXrow() == s.getXparentrow()) {
					m.getSubitems().add(s);
				}
			}
		}
		model.addAttribute("foods", mainfoodList);

		return "pages/conventionmanagement/hallbooking/opord::oporddetailfoodtable";
	}

	@GetMapping("/print/{xordernum}")
	public ResponseEntity<byte[]> printChalan(@PathVariable String xordernum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			message = "Booking not found to do print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<Oporddetail> details = opordService.findOporddetailByXordernum(xordernum);
		if(details == null || details.isEmpty()) {
			message = "Booking Details is empty";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		for(Oporddetail o : details) {
			Caitem ca = caitemService.findByXitem(o.getXitem());
			if(ca == null) continue;
			o.setXitem(ca.getXitem() + " - " + ca.getXdesc());
		}

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		Zbusiness zb = sessionManager.getZbusiness();

		// report start
		ConventionHallBookingReport report = new ConventionHallBookingReport();
		report.setBusinessName(zb.getZorg());
		report.setBusinessAddress(zb.getXmadd());
		report.setReportName("Event Contract : " + oh.getXordernum());
		report.setFromDate(sdf.format(oh.getXdate()));
		report.setToDate(sdf.format(oh.getXdate()));
		report.setPrintDate(sdf.format(new Date()));

		HallBookingHeader header = new HallBookingHeader();
		BeanUtils.copyProperties(oh, header);
		header.setXdate(sdf.format(oh.getXdate()));
		header.setXfunction(caitemService.findByXitem(oh.getXfunction()).getXdesc());
		header.setXstartdate(sdf.format(oh.getXstartdate()));
		header.setXenddate(sdf.format(oh.getXenddate()));
		Cacus c = cacusService.findByXcus(oh.getXcus());
		header.setXcus(c.getXorg());
		header.setClientaddress(c.getXmadd());
		header.setClientphone(c.getXphone());
		report.setHeader(header);

		List<HallBookingFoodDetail> foodbookingdetails = new ArrayList<>();
		List<HallBookingFacilitiesDetail> facilitiesbookingdetails = new ArrayList<>();

		for(Oporddetail detail : details) {
			if("Set Item".equalsIgnoreCase(detail.getXtype()) || "Facilities".equalsIgnoreCase(detail.getXcatitem())) continue;
			HallBookingFoodDetail hb = new HallBookingFoodDetail();
			BeanUtils.copyProperties(detail, hb);
			foodbookingdetails.add(hb);
		}

		for(Oporddetail detail : details) {
			if(!"Facilities".equalsIgnoreCase(detail.getXcatitem())) continue;
			HallBookingFacilitiesDetail hb = new HallBookingFacilitiesDetail();
			BeanUtils.copyProperties(detail, hb);
			facilitiesbookingdetails.add(hb);
		}

		for(HallBookingFoodDetail hb : foodbookingdetails) {
			for(Oporddetail detail : details) {
				if(!"Set Item".equalsIgnoreCase(detail.getXtype())) continue;

				if(hb.getXrow() == detail.getXparentrow()) {
					HallBookingSubItems sub = new HallBookingSubItems();
					BeanUtils.copyProperties(detail, sub);
					hb.getSubitems().add(sub);
				}
			}
		}

		report.setFooddetails(foodbookingdetails);
		report.setFacilitiesdetails(facilitiesbookingdetails);
		// report end

		byte[] byt = getPDFByte(report, "hallbookingreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for chalan : " + xordernum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

	@PostMapping("/createinvoice/{xordernum}")
	public @ResponseBody Map<String, Object> createInvoice(@PathVariable String xordernum, Model model) {
		Opordheader  booking = opordService.findOpordHeaderByXordernum(xordernum);
		if(booking == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Booking in this system");
			return responseHelper.getResponse();
		}

		// validation
		Opdoheader existInvoice = opdoService.findOpdoheaderByXordernum(xordernum).stream().findFirst().orElse(null);
		if(existInvoice != null) {
			responseHelper.setSuccessStatusAndMessage("Invoice created successfully");
			responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + existInvoice.getXdornum());
			return responseHelper.getResponse();
		}

		// Create invoice first
		Opdoheader sales = new Opdoheader();
		sales.setXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
		sales.setXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
		sales.setXdate(new Date());
		sales.setXstatusord("Open");
		sales.setXstatusjv("Open");
		sales.setXstatusar("Open");
		sales.setXordernum(booking.getXordernum());
		sales.setXvatait(booking.getXvatait());
		sales.setXtotamt(booking.getXtotamt() == null ? BigDecimal.ZERO : booking.getXtotamt());
		sales.setXait(booking.getXaitamt() == null ? BigDecimal.ZERO : booking.getXaitamt());
		sales.setXvatamt(booking.getXvatamt() == null ? BigDecimal.ZERO : booking.getXvatamt());
		sales.setXdiscamt(booking.getXdiscamt() == null ? BigDecimal.ZERO : booking.getXdiscamt());
		sales.setXgrandtot(booking.getXgrandtot() == null ? BigDecimal.ZERO : booking.getXgrandtot());
		sales.setXcus(booking.getXcus());
		sales.setXpaid(booking.getXpaid() == null ? BigDecimal.ZERO : booking.getXpaid());

		long count = opdoService.save(sales);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create invoice from booking order");
			return responseHelper.getResponse();
		}

		// create invoice details
		// find booking details first
		List<Opdodetail> salesDetails = new ArrayList<>();
		List<Oporddetail> bookingDetails = opordService.findOporddetailByXordernum(xordernum);
		if(bookingDetails != null && !bookingDetails.isEmpty()) {
			List<Oporddetail> subitems = bookingDetails.stream().filter(f -> "Set Item".equalsIgnoreCase(f.getXtype())).collect(Collectors.toList());
			List<Oporddetail> mainitems = bookingDetails.stream().filter(f -> !"Set Item".equalsIgnoreCase(f.getXtype())).collect(Collectors.toList());
			for(Oporddetail m : mainitems) {
				for(Oporddetail s : subitems) {
					if(s.getXparentrow() == m.getXrow()) {
						m.getSubitems().add(s);
					}
				}
			}

			for(Oporddetail bd : mainitems) {
				// create invoice details first
				Opdodetail salesDetail = new Opdodetail();
				salesDetail.setXdornum(sales.getXdornum());
				salesDetail.setXitem(bd.getXitem());
				salesDetail.setXqtyord(bd.getXqtyord() == null ? BigDecimal.ZERO : bd.getXqtyord());
				salesDetail.setXunitsel(bd.getXunit());
				salesDetail.setXrate(bd.getXrate() == null ? BigDecimal.ZERO : bd.getXrate());
				salesDetail.setXlineamt(bd.getXlineamt() == null ? BigDecimal.ZERO : bd.getXlineamt());
				salesDetail.setXcatitem(bd.getXcatitem());
				salesDetail.setXgitem(bd.getXgitem());
				salesDetail.setXtype(bd.getXtype());
				salesDetail.setXparentrow(0);

				if(bd.getSubitems() != null && !bd.getSubitems().isEmpty()) {
					// create subitems
					for(Oporddetail sub : bd.getSubitems()) {
						Opdodetail subitem = new Opdodetail();
						subitem.setXdornum(sales.getXdornum());
						subitem.setXitem(sub.getXitem());
						subitem.setXqtyord(sub.getXqtyord() == null ? BigDecimal.ZERO : sub.getXqtyord());
						subitem.setXunitsel(sub.getXunit());
						subitem.setXrate(sub.getXrate() == null ? BigDecimal.ZERO : sub.getXrate());
						subitem.setXlineamt(sub.getXlineamt() == null ? BigDecimal.ZERO : sub.getXlineamt());
						subitem.setXcatitem(sub.getXcatitem());
						subitem.setXgitem(sub.getXgitem());
						subitem.setXtype(sub.getXtype());
						salesDetail.getSubitems().add(subitem);
					}

				}

				salesDetails.add(salesDetail);

			}

			// Now save sales details
			if(salesDetails != null && !salesDetails.isEmpty()) {
				for(Opdodetail detail : salesDetails) {
					long countd = opdoService.saveDetail(detail);
					if(countd == 0) {
						responseHelper.setErrorStatusAndMessage("Can't save detail");
						return responseHelper.getResponse();
					}

					if(detail.getSubitems() != null && !detail.getSubitems().isEmpty()) {
						for(Opdodetail subdetail : detail.getSubitems()) {
							subdetail.setXparentrow(detail.getXrow());
							long subcount = opdoService.saveDetail(subdetail);
							if(subcount == 0) {
								responseHelper.setErrorStatusAndMessage("Can't save detail");
								return responseHelper.getResponse();
							}
						}
					}
				}
			}
		}

		responseHelper.setSuccessStatusAndMessage("Invoice created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + sales.getXdornum());
		return responseHelper.getResponse();
	}

}
