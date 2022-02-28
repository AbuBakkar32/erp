package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Caitem;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.Vatait;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CaitemService;
import com.asl.service.OpordService;
import com.asl.service.RoomBookingService;
import com.asl.service.VataitService;
import com.asl.util.CKTime;

@Controller
@RequestMapping("/roommanagement/roombooking")
public class RoomBookingController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private VataitService vataitService;
	@Autowired private CaitemService caitemService;
	@Autowired private RoomBookingService roomBookingService;

	@GetMapping
	public String loadBookingPage(Model model) {
		model.addAttribute("hallbookingpreffix", xtrnService.findByXtypetrn(TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("opordheader", getDefaultOpordHeader());
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("bookingOrderList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getCode(), TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getdefaultCode()));
		model.addAttribute("paymentType", xcodesService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("paymentMode", xcodesService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		return "pages/roommanagement/roombooking/opord";
	}

	@GetMapping("/{xordernum}")
	public String loadBookingPage(@PathVariable String xordernum, Model model) {
		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if (oh == null) return "redirect:/conventionmanagement/hallbooking";

		model.addAttribute("opordheader", oh);
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("oporddetailsList", opordService.findOporddetailByXordernum(xordernum));
		model.addAttribute("bookingOrderList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getCode(), TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getdefaultCode()));
		model.addAttribute("paymentType", xcodesService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("paymentMode", xcodesService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		return "pages/roommanagement/roombooking/opord";
	}

	public Opordheader getDefaultOpordHeader() {
		Opordheader oh = new Opordheader();
		oh.setXtypetrn(TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getCode());
		oh.setXtotguest(0);
		oh.setXstarttime("00:00");
		oh.setXendtime("23:59");
		oh.setXroomamt(BigDecimal.ZERO);
		oh.setXfoodamt(BigDecimal.ZERO);
		oh.setXfacamt(BigDecimal.ZERO);
		oh.setXtotamt(BigDecimal.ZERO);
		oh.setXvatait("No Vat");
		oh.setXvatamt(BigDecimal.ZERO);
		oh.setXaitamt(BigDecimal.ZERO);
		oh.setXdiscamt(BigDecimal.ZERO);
		oh.setXgrandtot(BigDecimal.ZERO);
		oh.setXpaid(BigDecimal.ZERO);
		oh.setXdue(BigDecimal.ZERO);
		oh.setXstatus("Open");
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

		if(opordheader.getXroomamt() == null) opordheader.setXroomamt(BigDecimal.ZERO);
		if(opordheader.getXfoodamt() == null) opordheader.setXfoodamt(BigDecimal.ZERO);
		if(opordheader.getXfacamt() == null) opordheader.setXfacamt(BigDecimal.ZERO);
		if(opordheader.getXtotamt() == null) {
			BigDecimal tot = opordheader.getXroomamt().add(opordheader.getXfoodamt()).add(opordheader.getXfacamt());
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
			List<String> bookedRooms = roomBookingService.allBookedRoomsInDateRange("Room", xstartdate, xenddate, existOh.getXordernum());
			Oporddetail od = opordService.findOporddetailByXordernum(existOh.getXordernum()).stream().filter(f -> "Room".equalsIgnoreCase(f.getXcatitem())).collect(Collectors.toList()).stream().findFirst().orElse(null);
			if(bookedRooms != null && !bookedRooms.isEmpty()) {
				for(String b : bookedRooms) {
					if(od != null && b.equalsIgnoreCase(od.getXitem())) {
						responseHelper.setErrorStatusAndMessage(od.getXdesc() + " is Not available in this time");
						return responseHelper.getResponse();
					}
				}
			}


			long count = opordService.updateOpordHeader(existOh);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Booking");
				return responseHelper.getResponse();
			}

			responseHelper.setRedirectUrl("/roommanagement/roombooking/" + existOh.getXordernum());
			responseHelper.setSuccessStatusAndMessage("Booking order updated successfully");
			return responseHelper.getResponse();
		}

		// if new record
		long count = opordService.saveOpordHeader(opordheader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Booking order not created");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/roommanagement/roombooking/" + opordheader.getXordernum());
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
		responseHelper.setRedirectUrl("/roommanagement/roombooking/" + opordHeader.getXordernum());
		return responseHelper.getResponse();
	}

	@GetMapping("{xordernum}/oporddetail/{xrow}/show")
	public String openOpordDetailModal(@PathVariable String xordernum, @PathVariable String xrow, Model model) {

		Map<String, List<Caitem>> map = new HashMap<>();
		map.put("Room Facility", caitemService.findByXcatitem("Room Facility"));
		map.put("Food", caitemService.findByXcatitem("Room Food"));
		model.addAttribute("itemMap", map);

		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String xstartdate = sdf.format(oh.getXstartdate()).toUpperCase().concat(" ").concat(oh.getXstarttime());
		String xenddate = sdf.format(oh.getXenddate()).toUpperCase().concat(" ").concat(oh.getXendtime());
		List<String> bookedRooms = roomBookingService.allBookedRoomsInDateRange("Room", xstartdate, xenddate, xordernum);

		List<Caitem> halls = caitemService.findByXcatitem("Room");
		for(Caitem c : halls) {
			for(String s : bookedRooms) {
				if(c.getXitem().equalsIgnoreCase(s)) c.setBooked(true);
			}
		}
		map.put("Room", halls);

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

		return "pages/roommanagement/roombooking/oporddetailmodal::oporddetailmodal";
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
		boolean roomExist = false;
		List<Oporddetail> deletableDL = new ArrayList<>();
		List<Oporddetail> existDetails = opordService.findOporddetailByXordernum(xordernum);
		if(existDetails != null && !existDetails.isEmpty()) {
			for(Oporddetail d :  existDetails) {
				// if already added in db then remove from list
				if(xitemsL.contains(d.getXitem())) {
					xitemsL.remove(xitemsL.indexOf(d.getXitem()));
					if("Room".equalsIgnoreCase(d.getXcatitem())) roomExist = true;
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

		// validation
		long totalRooms = details.stream().filter(f -> "Room".equalsIgnoreCase(f.getXcatitem())).collect(Collectors.toList()).size();
		if(totalRooms == 0 && !roomExist) {
			responseHelper.setErrorStatusAndMessage("Hall selection required");
			return responseHelper.getResponse();
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

		responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/roommanagement/roombooking/oporddetail/" + oh.getXordernum());
		responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/roommanagement/roombooking/opordheaderform/" + oh.getXordernum());
		responseHelper.setSuccessStatusAndMessage("Items saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/oporddetail/{xordernum}")
	public String reloadOpdoDetailTable(@PathVariable String xordernum, Model model) {
		model.addAttribute("oporddetailsList", opordService.findOporddetailByXordernum(xordernum));
		model.addAttribute("opordheader", opordService.findOpordHeaderByXordernum(xordernum));
		return "pages/roommanagement/roombooking/opord::oporddetailtable";
	}

	@GetMapping("/opordheaderform/{xordernum}")
	public String reloadOpdoheaderform(@PathVariable String xordernum, Model model) {
		model.addAttribute("hallbookingpreffix", xtrnService.findByXtypetrn(TransactionCodeType.ROOM_BOOKING_SALES_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("opordheader", opordService.findOpordHeaderByXordernum(xordernum));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("paymentType", xcodesService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("paymentMode", xcodesService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		return "pages/roommanagement/roombooking/opord::opordheaderform";
	}

	@GetMapping("{xordernum}/oporddetail2/{xrow}/show")
	public String openOpordDetailModal2(@PathVariable String xordernum, @PathVariable int xrow, Model model) {
		Oporddetail detail = opordService.findOporddetailByXordernumAndXrow(xordernum, xrow);
		if(detail == null) return "redirect:/roommanagement/roombooking" + xordernum;

		model.addAttribute("oporddetail", detail);
		return "pages/roommanagement/roombooking/oporddetailmodal2::oporddetailmodal2";
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
			responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/roommanagement/roombooking/oporddetail/" + opordDetail.getXordernum());
			responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/roommanagement/roombooking/opordheaderform/" + opordDetail.getXordernum());

			return responseHelper.getResponse();
		}

		// if new detail
		long count = opordService.saveOpordDetail(opordDetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/roommanagement/roombooking/oporddetail/" + opordDetail.getXordernum());
		responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/roommanagement/roombooking/opordheaderform/" + opordDetail.getXordernum());
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
		responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/roommanagement/roombooking/oporddetail/" + xordernum);
		responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/roommanagement/roombooking/opordheaderform/" + xordernum);
		return responseHelper.getResponse();
	}

}
