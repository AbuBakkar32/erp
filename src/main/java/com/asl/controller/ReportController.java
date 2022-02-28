package com.asl.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import com.asl.entity.DataList;
import com.asl.entity.ProfileLine;
import com.asl.entity.ProjectStoreView;
import com.asl.enums.MenuProfile;
import com.asl.enums.ReportParamDataType;
import com.asl.enums.ReportType;
import com.asl.model.ProfileLineWrapper;
import com.asl.model.ReportProfile;
import com.asl.model.RequestParameters;
import com.asl.service.ProfileService;
import com.asl.service.ProjectStoreViewService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
@Slf4j
@Controller
@RequestMapping("/report")
public class ReportController extends ASLAbstractController {

	@Autowired ProfileService profileService;
	@Autowired ProjectStoreViewService projectStoreViewService;

	@GetMapping
	public String loadReportPage(Model model) {
		ReportProfile rp = getLoggedInUserReportProfile();

		Map<String, ProfileLineWrapper> profileLinesMap = new HashMap<>();
		rp.getProfileLines().stream().forEach(pl -> {
			if(profileLinesMap.get(pl.getPgroupname()) != null) {
				ProfileLineWrapper wrapper = profileLinesMap.get(pl.getPgroupname());
				wrapper.getProfileLines().add(pl);
				if(!wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
			} else {
				List<ProfileLine> list = new ArrayList<>();
				list.add(pl);
				ProfileLineWrapper wrapper = new ProfileLineWrapper();
				wrapper.setAllchecked(false);
				wrapper.getProfileLines().add(pl);
				if(!wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
				profileLinesMap.put(pl.getPgroupname(), wrapper);
			}
		});

		if(!isBoshila()) {
			profileLinesMap.remove("Land Management");
		}

		model.addAttribute("plmap", profileLinesMap);
		return "pages/report/reportlist";
	}

	@GetMapping("/{menuCode}")
	public String loadReportPage(@PathVariable String menuCode, Model model) {
		MenuProfile rm = null;
		try {
			rm = MenuProfile.valueOf(menuCode);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			return "redirect:/accessdenied";
		}

		model.addAttribute("fieldsList", getReportFieldService(rm).getReportFields());
		model.addAttribute("menuGroup", rm.getSeqn());
		model.addAttribute("selectedReport", rm.name());
		model.addAttribute("reportCode", rm.name());
		model.addAttribute("reportName", rm.getDescription());
		getLoggedInUserMenuProfile().getProfileLines().stream().forEach(i -> {
			if(i.getProfilelinecode().equalsIgnoreCase(menuCode)) {
				model.addAttribute("fopEnabled", i.isEnabled());
			}
		});

		return "pages/report/report";
	}

	@PostMapping("/print")
	public @ResponseBody String print(RequestParameters params) throws IOException {
		MenuProfile rm = MenuProfile.valueOf(params.getReportCode().toUpperCase());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Map<String, Object> reportParams = new HashMap<>();
		for(Map.Entry<String, String> m : rm.getParamMap().entrySet()) {
			String reportParamFieldName = m.getKey();
			String[] arr = m.getValue().split("\\|");
			String cristalReportParamName = arr[0];
			ReportParamDataType paramType = ReportParamDataType.valueOf(arr[1]);
			Object method = RequestParameters.invokeGetter(params, reportParamFieldName);
			convertObjectAndPutIntoMap(cristalReportParamName, paramType, method, reportParams);
		}

		StringBuilder url = new StringBuilder(getReportFileName(rm.name()) + ".rptdesign").append("&__toolbar=false&__showtitle=false&__title=report");
		reportParams.entrySet().stream().forEach(m -> {
			url.append("&" + m.getKey() + "=" + m.getValue());
		});

		return url.toString();
	}

	private String getReportFileName(String reportcode) {
		List<DataList> list = listService.getList("RPROFILE", reportcode);
		if(list == null || list.isEmpty()) return reportcode;

		DataList dl = list.stream().findFirst().orElse(null);
		if(dl == null || StringUtils.isBlank(dl.getListvalue12())) return reportcode;

		return dl.getListvalue12();
	}

	@PostMapping("/print/fop")
	public ResponseEntity<Object> printFop(RequestParameters params) throws IOException {
		MenuProfile rm = MenuProfile.valueOf(params.getReportCode().toUpperCase());

		String message = "";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		// Parameters to send
		String reportName = appConfig.getReportTemplatepath() + "/" + rm.name() + ".rptdesign";
		String reportTitle = "Test Report";
		boolean attachment = true;

		ReportType reportType = ReportType.PDF;
		Map<String, Object> reportParams = new HashMap<>();
		for(Map.Entry<String, String> m : rm.getParamMap().entrySet()) {
			String reportParamFieldName = m.getKey();
			String[] arr = m.getValue().split("\\|");
			String cristalReportParamName = arr[0];
			ReportParamDataType paramType = ReportParamDataType.valueOf(arr[1]);
			Object method = RequestParameters.invokeGetter(params, reportParamFieldName);
			if("reportViewType".equalsIgnoreCase(cristalReportParamName)) {
				reportType = (ReportType) method;
				continue;
			}
			convertObjectAndPutIntoMap(cristalReportParamName, paramType, method, reportParams);
		}

		// FOP
		reportName = getOnScreenReportTemplate(rm.name() + ".xsl");
		byte[] byt = null;
		try {
			byt = getReportFieldService(rm).getPDFReportByte(reportName, reportParams);
		} catch (JAXBException | ParserConfigurationException | SAXException | TransformerFactoryConfigurationError
				| TransformerException e) {
			log.error(ERROR, e.getMessage(), e);
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		if(byt == null) {
			String encodedString = Base64.getEncoder().encodeToString(message.getBytes());
			return new ResponseEntity<>(encodedString, headers, HttpStatus.OK);
		}

		String encodedString = Base64.getEncoder().encodeToString(byt);
		return new ResponseEntity<>(encodedString, headers, HttpStatus.OK);

	}

	private void convertObjectAndPutIntoMap(String paramName, ReportParamDataType paramType, Object method, Map<String, Object> reportParams) {
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MMM-yyyy");
		switch (paramType) {
			case DATE:
				try {
					reportParams.put(paramName, sdf3.format(sdf.parseObject((String) method)).toUpperCase());
				} catch (ParseException e) {
					log.error(ERROR, e.getMessage(), e);
				}
				break;
			case DATESTRING:
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = (Date) sdf.parseObject((String) method);
					reportParams.put(paramName, sdf2.format(date));
				} catch (ParseException e) {
					log.error(ERROR, e.getMessage(), e);
				}
				break;
			default:
				reportParams.put(paramName, method);
				break;
		}
	}
	
	
	@GetMapping("/search/store/{projectid}")
	public @ResponseBody List<ProjectStoreView> getAllProjectWiseStore(@PathVariable String projectid){
		return projectStoreViewService.getProjectStoresByXproject(projectid);
	}
	
}
