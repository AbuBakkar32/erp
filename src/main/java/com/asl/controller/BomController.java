package com.asl.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Bmbomdetail;
import com.asl.entity.Bmbomheader;
import com.asl.entity.Caitem;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.BmbomService;
import com.asl.service.CaitemService;
import com.asl.service.XtrnService;

/**
 * @author Zubayer Ahamed
 * @since Mar 13, 2021
 */
@Controller
@RequestMapping("/production/bom")
public class BomController extends ASLAbstractController {

	@Autowired private XtrnService xtrnService;
	@Autowired private CaitemService caitemService;
	@Autowired private BmbomService bmbomService;

	@GetMapping
	public String loadBomPage(Model model) {
		model.addAttribute("bomprefix", xtrnService.findByXtypetrn(TransactionCodeType.BOM_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("bom", getDefaultBmbomheader());
		model.addAttribute("bomheaders", bmbomService.getAllBmbomheader());
		return "pages/production/bom/bom";
	}

	@GetMapping("/{xbomkey}")
	public String loadBomPage(@PathVariable String xbomkey, Model model) {
		Bmbomheader bh = bmbomService.findBmbomheaderByXbomkey(xbomkey);
		if(bh == null) return "redirect:/production/bom";

		model.addAttribute("bomprefix", xtrnService.findByXtypetrn(TransactionCodeType.BOM_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("bom", bh);
		model.addAttribute("bomheaders", bmbomService.getAllBmbomheader());
		model.addAttribute("bomdetails", bmbomService.findBmbomdetailByXbomkey(bh.getXbomkey()));
		return "pages/production/bom/bom";
	}

	private Bmbomheader getDefaultBmbomheader() {
		Bmbomheader bh = new Bmbomheader();
		bh.setXtypetrn(TransactionCodeType.BOM_NUMBER.getCode());
		return bh;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Bmbomheader bmbomheader, Model model){
		if(bmbomheader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(bmbomheader.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}

		// Search item first
		Caitem caitem = caitemService.findByXitem(bmbomheader.getXitem());
		if(caitem == null) {
			responseHelper.setErrorStatusAndMessage("Item not found in the system");
			return responseHelper.getResponse();
		}
		if(!"Finished Good".equals(caitem.getXgitem()) && !caitem.isXproditem()) {
			responseHelper.setErrorStatusAndMessage("Invalid item for BOM entry");
			return responseHelper.getResponse();
		}
		
		

		// check existing
		Bmbomheader existBom = bmbomService.findBmbomheaderByXbomkey(bmbomheader.getXbomkey());
		if(existBom != null) {
			// check item bom already exist before update
			Bmbomheader bh = bmbomService.findBmBomHeaderByXitem(caitem.getXitem());
			if(bh != null && !bh.getXbomkey().equals(existBom.getXbomkey())) {
				responseHelper.setErrorStatusAndMessage("BOM already exist using this : "+ caitem.getXitem() + " - " + caitem.getXdesc() +" item");
				return responseHelper.getResponse();
			}

			BeanUtils.copyProperties(bmbomheader, existBom, "xtypetrn","xtrn","xbomkey","xdate","xpreferbatchqty","xqtybase");
			existBom.setXdesc(caitem.getXdesc());
			long count = bmbomService.updateBmbomheader(existBom);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update BOM");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("BOM updated successfully");
			responseHelper.setRedirectUrl("/production/bom/" + existBom.getXbomkey());
			return responseHelper.getResponse();
		}

		// if new
		// check item bom already exist in the system
		Bmbomheader bh = bmbomService.findBmBomHeaderByXitem(caitem.getXitem());
		if(bh != null) {
			responseHelper.setErrorStatusAndMessage("BOM already exist using this : "+ caitem.getXitem() + " - " + caitem.getXdesc() +" item");
			return responseHelper.getResponse();
		}

		bmbomheader.setXdesc(caitem.getXdesc());
		bmbomheader.setXdate(new Date());
		bmbomheader.setXpreferbatchqty(1);
		bmbomheader.setXqtybase(1);
		long count = bmbomService.saveBmbomheader(bmbomheader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save BOM");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("BOM saved successfully");
		responseHelper.setRedirectUrl("/production/bom/" + bmbomheader.getXbomkey());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xbomkey}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xbomkey){
		return doArchiveOrRestore(xbomkey, true);
	}

	@PostMapping("/restore/{xbomkey}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xbomkey){
		return doArchiveOrRestore(xbomkey, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xbomkey, boolean archive){
		Bmbomheader bh = bmbomService.findBmbomheaderByXbomkey(xbomkey);
		if(bh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		bh.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = bmbomService.updateBmbomheader(bh);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// archive all details
		List<Bmbomdetail> details = bmbomService.findBmbomdetailByXbomkey(xbomkey);
		if(archive && details != null && !details.isEmpty()) {
			long countDetail = bmbomService.archiveBmbomdetailByXbomkey(xbomkey);
			if(countDetail == 0) {
				responseHelper.setErrorStatusAndMessage("Can't archive details");
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("BOM updated successfully");
		responseHelper.setRedirectUrl("/production/bom/" + xbomkey);
		return responseHelper.getResponse();
	}

	@GetMapping("{xbomkey}/bomdetail/{xbomrow}/show")
	public String openBomDetailModal(@PathVariable String xbomkey, @PathVariable String xbomrow, Model model) {
		Bmbomdetail detail = null;

		if("new".equalsIgnoreCase(xbomrow)) {
			detail = new Bmbomdetail();
			detail.setXbomkey(xbomkey);
			detail.setXunit("gm");
			detail.setXqtymix(BigDecimal.ZERO);
		} else {
			detail = bmbomService.findBmbomdetailByXbomkeyAndXbomrow(xbomkey, Integer.parseInt(xbomrow));
		}

		model.addAttribute("bomdetail", detail);
		return "pages/production/bom/bomdetailmodal::bomdetailmodal";
	}

	@PostMapping("/bomdetail/save")
	public @ResponseBody Map<String, Object> saveBomdetail(Bmbomdetail bmbomdetail){
		if(bmbomdetail == null || StringUtils.isBlank(bmbomdetail.getXbomkey())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(bmbomdetail.getXbomcomp())) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}

		// find item first
		Caitem caitem = caitemService.findByXitem(bmbomdetail.getXbomcomp());
		if(caitem == null) {
			responseHelper.setErrorStatusAndMessage("Item not found in the system");
			return responseHelper.getResponse();
		}

		bmbomdetail.setXwh("02");

		// if existing
		Bmbomdetail existDetail = bmbomService.findBmbomdetailByXbomkeyAndXbomrow(bmbomdetail.getXbomkey(), bmbomdetail.getXbomrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(bmbomdetail, existDetail);
			existDetail.setXdesc(caitem.getXdesc());
			existDetail.setXstype(caitem.getXgitem());
			long count = bmbomService.updateBmbomdetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("bomdetailtable", "/production/bom/bomdetail/" + bmbomdetail.getXbomkey());
			responseHelper.setSuccessStatusAndMessage("BOM detail update successfully");
			return responseHelper.getResponse();
		}

		// if new
		bmbomdetail.setXdesc(caitem.getXdesc());
		bmbomdetail.setXstype(caitem.getXgitem());
		long count = bmbomService.saveBmbomdetail(bmbomdetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("bomdetailtable", "/production/bom/bomdetail/" + bmbomdetail.getXbomkey());
		responseHelper.setSuccessStatusAndMessage("BOM detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/bomdetail/{xbomkey}")
	public String reloadDetailSection(@PathVariable String xbomkey, Model model) {
		model.addAttribute("bom", bmbomService.findBmbomheaderByXbomkey(xbomkey));
		model.addAttribute("bomdetails", bmbomService.findBmbomdetailByXbomkey(xbomkey));
		return "pages/production/bom/bom::bomdetailtable";
	}

	@PostMapping("/{xbomkey}/bomdetail/{xbomrow}/delete")
	public @ResponseBody Map<String, Object> deletePoordDetail(@PathVariable String xbomkey, @PathVariable String xbomrow, Model model) {
		Bmbomheader bh = bmbomService.findBmbomheaderByXbomkey(xbomkey);
		if(bh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		Bmbomdetail bd = bmbomService.findBmbomdetailByXbomkeyAndXbomrow(xbomkey, Integer.parseInt(xbomrow));
		if(bd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = bmbomService.deleteBmbomdetailByXbomkeyAndXbomrow(bd);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("can't delete detail");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("bomdetailtable", "/production/bom/bomdetail/" + bh.getXbomkey());
		responseHelper.setSuccessStatusAndMessage("BOM detail deleted successfully");
		return responseHelper.getResponse();
	}
}
