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

import com.asl.entity.LandComEvent;
import com.asl.entity.LandEventsMember;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandComEventService;

@Controller
@RequestMapping("/landotherevents")
public class LandOtherEventsController extends ASLAbstractController{

@Autowired private LandComEventService landComEventService;
	
	@GetMapping
	public String loadLandOtherEventsPage(Model model) {
		
		model.addAttribute("comevent", getDefaultLandOtherEvent());
		model.addAttribute("allComEvents", landComEventService.getAllLandOtherEvent());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LANDOTHEREVENT_ID.getCode(), Boolean.TRUE));
		model.addAttribute("otherEventTypes", xcodesService.findByXtype(CodeType.OTHEREVENT_TYPE.getCode(), Boolean.TRUE));
		return "pages/land/landotherevents";
	}
	
	private LandComEvent getDefaultLandOtherEvent() {
		LandComEvent lce  = new LandComEvent();
		lce.setXtypetrn(TransactionCodeType.LANDOTHEREVENT_ID.getCode());
		lce.setXtrn(TransactionCodeType.LANDOTHEREVENT_ID.getdefaultCode());
		return lce;
	}
	
	@GetMapping("/{xevent}")
	public String loadComEventPage(@PathVariable String xevent, Model model) {
		LandComEvent landComEvent = landComEventService.findAllLandComEvent(xevent);
		if (landComEvent == null) return "redirect:/landotherevents";
		
		model.addAttribute("comevent", landComEvent);
		model.addAttribute("allComEvents", landComEventService.getAllLandOtherEvent());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LANDOTHEREVENT_ID.getCode(), Boolean.TRUE));
		model.addAttribute("otherEventTypes", xcodesService.findByXtype(CodeType.OTHEREVENT_TYPE.getCode(), Boolean.TRUE));
		return "pages/land/landotherevents";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandComEvent landComEvent, BindingResult bindingResult) {
		if (landComEvent == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
	// if existing
	if(StringUtils.isNotBlank(landComEvent.getXevent())) {
		LandComEvent xlp = landComEventService.findAllLandComEvent(landComEvent.getXevent());
		BeanUtils.copyProperties(landComEvent, xlp,"xtypetrn","xtrn");
		long count = landComEventService.updateLandComEvent(xlp);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update Other Event info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Other Event info updated successfully");
		responseHelper.setRedirectUrl("/landotherevents/" + xlp.getXevent());
		return responseHelper.getResponse();
	}
	// if new
	long count = landComEventService.saveLandComEvent(landComEvent);
	if(count == 0) {
		responseHelper.setErrorStatusAndMessage("Can't save Other Event info");
		return responseHelper.getResponse();
	}
	responseHelper.setSuccessStatusAndMessage("Other Event info saved successfully");
	responseHelper.setRedirectUrl("/landotherevents/" + landComEvent.getXevent());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/delete/{xevent}")
	public @ResponseBody Map<String, Object> deleteOtherEvent(@PathVariable String xevent,  Model model) {
		LandComEvent lp = landComEventService.findAllLandComEvent(xevent);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landComEventService.delete(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/landotherevents/" + xevent );
		return responseHelper.getResponse();
}
	//start of event members
	
	@GetMapping("/{xevent}/eventmember/{xrow}/show")
	public String loadeventMemberModal(@PathVariable String xevent, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			LandEventsMember lem = new LandEventsMember();
			lem.setXevent(xevent);
			lem.setXmembertype("");
			lem.setXname("");
			lem.setXnote("");
			lem.setXperson("");
			
			model.addAttribute("lem", lem);
			model.addAttribute("eventMemberTypes", xcodesService.findByXtype(CodeType.EVENTMEMBER_TYPE.getCode(), Boolean.TRUE));
			
		}
		else {
			LandEventsMember lem = landComEventService.findlandLandEventsMemberByXeventAndXrow(xevent, Integer.parseInt(xrow));
			if(lem==null) {
				lem = new LandEventsMember();
				
			}
			model.addAttribute("lem", lem);
			model.addAttribute("eventMemberTypes", xcodesService.findByXtype(CodeType.EVENTMEMBER_TYPE.getCode(), Boolean.TRUE));
		}
		
		return "pages/land/eventmembermodal::eventmembermodal";
	}
	
	@PostMapping("/eventmember/save")
	public @ResponseBody Map<String, Object> saveLandEventMember(LandEventsMember landEventsMember){
		if(landEventsMember == null || StringUtils.isBlank(landEventsMember.getXevent())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		LandEventsMember exist = landComEventService.findlandLandEventsMemberByXeventAndXrow(landEventsMember.getXevent(), landEventsMember.getXrow());
		LandEventsMember existing = landComEventService.findByXeventAndXperson(landEventsMember.getXevent(), landEventsMember.getXperson());
		
		// if new data
		if(landEventsMember.getXrow() == 0 && existing != null) {
			responseHelper.setErrorStatusAndMessage("Event " + landEventsMember.getXevent() + " with this person " + landEventsMember.getXperson() + " data already exist in this system");
			return responseHelper.getResponse();
		}
		
		// if existing
		if(landEventsMember.getXrow() != 0 && exist != null) {
			BeanUtils.copyProperties(landEventsMember, exist);
			long count = landComEventService.update(exist);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("eventmembertable","/landevents/eventmember/" + landEventsMember.getXevent());
			responseHelper.setSuccessStatusAndMessage("Event Member Details updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = landComEventService.save(landEventsMember);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("eventmembertable","/landevents/eventmember/" + landEventsMember.getXevent());
		responseHelper.setSuccessStatusAndMessage("Event Member Details saved successfully");
		return responseHelper.getResponse();
	}
		
		@GetMapping("/eventmember/{xevent}")
		public String reloadOwnerTable(@PathVariable String xevent, Model model) {
			List<LandEventsMember> memberList = landComEventService.findByLandEventsMember(xevent);
			model.addAttribute("lemlist", memberList);
			model.addAttribute("comevent", landComEventService.findAllLandComEvent(xevent));
			return "pages/land/landcomevent::eventmembertable";
		}
		
		//delete
			@PostMapping("{xevent}/eventmember/{xrow}/delete")
			public @ResponseBody Map<String, Object> deleteLandEventsMemberDetails(@PathVariable String xevent, @PathVariable String xrow, Model model) {
				LandEventsMember lem = landComEventService.findlandLandEventsMemberByXeventAndXrow(xevent, Integer.parseInt(xrow));
				if(lem == null) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}

				long count = landComEventService.deleteLandEventsMember(lem);
				if(count == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}

				responseHelper.setSuccessStatusAndMessage("Deleted successfully");
				responseHelper.setReloadSectionIdWithUrl( "eventmembertable","/landevents/eventmember/" + xevent);
				return responseHelper.getResponse();
			}
			////end of event member

}
