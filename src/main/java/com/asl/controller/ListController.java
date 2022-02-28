package com.asl.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import com.asl.entity.DataList;
import com.asl.entity.ListHead;
import com.asl.enums.ResponseStatus;
import com.asl.service.ListService;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 *
 */
@Controller
@RequestMapping("/system/list")
public class ListController extends ASLAbstractController {

	@Autowired private ListService listService;

	@GetMapping
	public String loadListPage(Model model) {
		ListHead lh = new ListHead();
		lh.setNewData(true);
		model.addAttribute("listHead", lh);
		model.addAttribute("listHeads", listService.getAllListHead());
		return "pages/system/list/list";
	}

	@GetMapping("/{listcode}")
	public String loadListPage(@PathVariable String listcode, Model model) {
		ListHead lh = listService.findListHeadByListcode(listcode);
		if(lh == null) return "redirect:/system/list";

		model.addAttribute("listHead", lh);
		model.addAttribute("listHeads", listService.getAllListHead());
		model.addAttribute("listLines", listService.findDataListByListcode(listcode));

		return "pages/system/list/list";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> saveListHead(ListHead listHead, BindingResult bindingResult){
		if(listHead == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Modify List code first
		listHead.setListcode(listService.modifiedListcode(listHead.getListcode()));

		// Validate listhead data
		modelValidator.validateListHead(listHead, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// If new listhead
		if(listHead.isNewData()) {
			long count = listService.saveListHead(listHead);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save list");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Listhead saved successfully");
			responseHelper.setRedirectUrl("/system/list/" + listHead.getListcode());
			return responseHelper.getResponse();
		}

		// If previous listhead
		ListHead lh = listService.findListHeadByListcode(listHead.getListcode());
		if(lh == null) {
			responseHelper.setErrorStatusAndMessage("List not found in this system");
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(listHead, lh, "listcode");
		long count = listService.updateListHead(lh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update list");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Listhead updateed successful");
		responseHelper.setRedirectUrl("/system/list/" + lh.getListcode());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{listcode}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String listcode){
		return doArchiveOrRestore(listcode, true);
	}

	private Map<String, Object> doArchiveOrRestore(String listcode, boolean doArchive) {
		if(StringUtils.isBlank(listcode)) {
			responseHelper.setErrorStatusAndMessage("Can't delete list");
			return responseHelper.getResponse();
		}

		ListHead lh = listService.findListHeadByListcode(listcode);
		if(lh == null) {
			responseHelper.setErrorStatusAndMessage("Can't find list in this system");
			return responseHelper.getResponse();
		}

		// check list has list lines
		List<DataList> dlist = listService.findDataListByListcode(listcode);
		if(dlist != null && !dlist.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete all list lines first");
			return responseHelper.getResponse();
		}

		long count = listService.deleteListHead(listcode);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete list");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("List deleted successfully");
		responseHelper.setRedirectUrl("/system/list");
		return responseHelper.getResponse();
	}


	@GetMapping("{listcode}/listline/{listid}/show")
	public String openModalToAddOrUpdateList(@PathVariable String listcode, @PathVariable String listid, Model model) {
		ListHead lh = listService.findListHeadByListcode(listcode);
		if(lh == null) return "redirect:/system/list";

		model.addAttribute("listHead", lh);

		if("new".equalsIgnoreCase(listid)) {
			DataList dl = new DataList();
			dl.setListcode(listcode);
			model.addAttribute("dataList", dl);
		} else {
			DataList dl = listService.findDataListByListcodeAndListid(Integer.valueOf(listid), listcode);
			if(dl == null) return "redirect:/system/list";
			model.addAttribute("dataList", dl);
		}

		return "pages/system/list/listmodal::listmodal";
	}

	@PostMapping("/listline/save")
	public @ResponseBody Map<String, Object> saveListLine(DataList dataList) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(StringUtils.isBlank(dataList.getListcode()) || dataList == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		ListHead lh = listService.findListHeadByListcode(dataList.getListcode());
		if(lh == null) {
			responseHelper.setErrorStatusAndMessage("List not found in this system");
			return responseHelper.getResponse();
		}

		// if new
		if(dataList.getListid() == 0) {
			long count = listService.saveDataList(dataList);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save list line");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("List line saved successfully");
			responseHelper.setReloadSectionIdWithUrl("listtable", "/system/list/listlines/" + dataList.getListcode());
			return responseHelper.getResponse();
		}

		DataList exist = listService.findDataListByListcodeAndListid(dataList.getListid(), dataList.getListcode());
		if(exist == null) {
			responseHelper.setErrorStatusAndMessage("Can't find list line in this system");
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(dataList, exist);
		long count = listService.updateDataList(exist);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update list line");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("List line updated successfully");
		responseHelper.setReloadSectionIdWithUrl("listtable", "/system/list/listlines/" + dataList.getListcode());
		return responseHelper.getResponse();
	}

	@GetMapping("/listlines/{listcode}")
	public String getReloadedListLineSection(@PathVariable String listcode, Model model){
		model.addAttribute("listHead", listService.findListHeadByListcode(listcode));
		model.addAttribute("listLines", listService.findDataListByListcode(listcode));
		return "pages/system/list/list::listtable";
	}

	@PostMapping("{listcode}/listline/{listid}/archive")
	public @ResponseBody Map<String, Object> archiveListLine(@PathVariable String listcode, @PathVariable int listid, Model model) {

		long count = listService.deleteDataList(listid, listcode);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete list line");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("List line deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("listtable", "/system/list/listlines/" + listcode);
		return responseHelper.getResponse();
	}
}
