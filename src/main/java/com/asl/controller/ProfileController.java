
package com.asl.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
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
import com.asl.entity.Profile;
import com.asl.entity.ProfileLine;
import com.asl.enums.MenuProfile;
import com.asl.enums.ProfileType;
import com.asl.enums.ReportMenu;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ProfileLineWrapper;
import com.asl.model.ReportProfile;
import com.asl.service.ProfileLineService;
import com.asl.service.ProfileService;

@Controller
@RequestMapping("/system/profile")
public class ProfileController extends ASLAbstractController {

	@Autowired private ProfileService profileService;
	@Autowired private ProfileLineService profileLineService;

	@GetMapping
	public String loadProfilePage(Model model) {
		model.addAttribute("profile", getDefaultProfile());
		model.addAttribute("profiles", profileService.getAllProfiles());
		return "pages/system/usersentry/profile/profile";
	}

	private Profile getDefaultProfile() {
		Profile profile = new Profile();
		profile.setProfiletype(ProfileType.M);
		return profile;
	}

	@GetMapping("/{profilecode}")
	public String loadProfilePageByProfileCode(@PathVariable String profilecode, Model model) {
		Profile profile = profileService.findByProfilecode(profilecode);
		if(profile == null) return "redirect:/system/profile";

		Map<String, ProfileLineWrapper> profileLinesMap = new HashMap<>();
		Map<String, String> groupMap = new HashMap<>();
		if(ProfileType.M.equals(profile.getProfiletype())) {

			com.asl.model.MenuProfile mp = profileService.getMenuProfileByProfilecode(profile.getProfilecode());
			
			mp.getProfileLines().sort(Comparator.comparing(ProfileLine::getGroupseqn).thenComparing(ProfileLine::getSeqn));

			for(ProfileLine pl : mp.getProfileLines()) {
				if(profileLinesMap.get(pl.getPgroupname()) != null) {
					ProfileLineWrapper wrapper = profileLinesMap.get(pl.getPgroupname());
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
				} else {
					List<ProfileLine> list = new ArrayList<>();
					list.add(pl);
					ProfileLineWrapper wrapper = new ProfileLineWrapper();
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
					profileLinesMap.put(pl.getPgroupname(), wrapper);
				}

				if(groupMap.get(pl.getPgroupname()) == null) {
					groupMap.put(pl.getPgroupname(), pl.getParent());
				}

			}
			
		} else if (ProfileType.U.equals(profile.getProfiletype())) {
			// TODO: 
		} else if (ProfileType.R.equals(profile.getProfiletype())) {

			ReportProfile rp = profileService.getReportProfileByProfilecode(profile.getProfilecode());

			for(ProfileLine pl : rp.getProfileLines()) {
				if(profileLinesMap.get(pl.getPgroup()) != null) {
					ProfileLineWrapper wrapper = profileLinesMap.get(pl.getPgroup());
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
					if(wrapper.isAllenabled()) wrapper.setAllenabled(pl.isEnabled());
				} else {
					List<ProfileLine> list = new ArrayList<>();
					list.add(pl);
					ProfileLineWrapper wrapper = new ProfileLineWrapper();
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
					if(wrapper.isAllenabled()) wrapper.setAllenabled(pl.isEnabled());
					profileLinesMap.put(pl.getPgroup(), wrapper);
				}

				if(groupMap.get(pl.getPgroup()) == null) {
					groupMap.put(pl.getPgroup(), pl.getPgroupname());
				}

			}
			
		}


		// new system
		List<ProfileLine> masters = new ArrayList<>();
		com.asl.model.MenuProfile mp = profileService.getMenuProfileByProfilecode(profilecode);
		if(mp != null) {
			for(ProfileLine pl : mp.getProfileLines()) {
				if(StringUtils.isNotBlank(pl.getRoles())) {
					String[] roles = pl.getRoles().split(",");
					boolean rolePermissionOk = false;
					for(String role : Arrays.asList(roles)) {
						if(sessionManager.getLoggedInUserDetails().getRoles().contains(role)) {
							rolePermissionOk = true;
						}
					}
					pl.setRoleHasAccess(rolePermissionOk);
				}

				pl.setSubmenus(new ArrayList<>());
				if("MASTER".equalsIgnoreCase(pl.getMenutype())) {
					pl.setProfilelebel(1);
					masters.add(pl);
				}
			}
		}

		for(ProfileLine master : masters) {
			for(ProfileLine pl : mp.getProfileLines()) {
				if(pl == null || !pl.getParent().equalsIgnoreCase(master.getProfilelinecode())) {
					continue;
				}

				if(StringUtils.isNotBlank(pl.getRoles())) {
					String[] roles = pl.getRoles().split(",");
					boolean rolePermissionOk = false;
					for(String role : Arrays.asList(roles)) {
						if(sessionManager.getLoggedInUserDetails().getRoles().contains(role)) {
							rolePermissionOk = true;
						}
					}
					pl.setRoleHasAccess(rolePermissionOk);
				}

				pl.setProfilelebel(2);
				master.getSubmenus().add(pl);
				pl.setSubmenus(new ArrayList<>());

				// get sub menus menu
				if("SUB-MASTER".equalsIgnoreCase(pl.getMenutype())) {
					for(ProfileLine pl2 : mp.getProfileLines()) {
						if(pl2 == null || !pl2.getParent().equalsIgnoreCase(pl.getProfilelinecode())) {
							continue;
						}

						if(StringUtils.isNotBlank(pl2.getRoles())) {
							String[] roles = pl2.getRoles().split(",");
							boolean rolePermissionOk = false;
							for(String role : Arrays.asList(roles)) {
								if(sessionManager.getLoggedInUserDetails().getRoles().contains(role)) {
									rolePermissionOk = true;
								}
							}
							pl2.setRoleHasAccess(rolePermissionOk);
						}

						pl2.setProfilelebel(3);
						pl.getSubmenus().add(pl2);
					}
				}
			}
		}
		
		
		masters.stream().forEach(m -> System.out.println(m.toString()));
		model.addAttribute("profiletree", masters);

		model.addAttribute("grpmap", groupMap);
		model.addAttribute("plmap", profileLinesMap);
		model.addAttribute("profile", profile);
		model.addAttribute("profiles", profileService.getAllProfiles());
		return "pages/system/usersentry/profile/profile";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> saveProfile(Profile profile, BindingResult bindingResult, Model model){
		if(profile == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Modify profile code first
		profile.setProfilecode(profileService.modifiedProfileCode(profile.getProfilecode()));

		// Validate profile data
		modelValidator.validateProfile(profile, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// if new profile
		if("Y".equalsIgnoreCase(profile.getNewflag())) {
			long count = profileService.save(profile);
			if(count == 0) {
				responseHelper.setSuccessStatusAndMessage("Can't save profile : " + profile.getProfilecode());
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Profile saved successfully");
			responseHelper.setRedirectUrl("/system/profile/" + profile.getProfilecode());
			return responseHelper.getResponse();
		}

		// if existing
		Profile existProfile = profileService.findByProfilecode(profile.getProfilecode());
		if(existProfile == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(profile, existProfile, "profilecode", "profiletype");
		long count = profileService.update(existProfile);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update profile : " + profile.getProfilecode());
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Profile saved successfully");
		responseHelper.setRedirectUrl("/system/profile/" + profile.getProfilecode());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{profilecode}/{profiletype}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String profilecode, @PathVariable ProfileType profiletype){
		return doArchiveOrRestore(profilecode, profiletype, true);
	}

	@PostMapping("/restore/{profilecode}/{profiletype}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String profilecode, @PathVariable ProfileType profiletype){
		return doArchiveOrRestore(profilecode, profiletype, false);
	}

	private Map<String, Object> doArchiveOrRestore(String profilecode, ProfileType profiletype, boolean doArchive) {
		if(StringUtils.isBlank(profilecode)) {
			responseHelper.setErrorStatusAndMessage("Can't update profile");
			return responseHelper.getResponse();
		}

		Profile pr = profileService.findByProfilecodeAndProfiletype(profilecode, profiletype);
		if(pr == null) {
			responseHelper.setErrorStatusAndMessage("Profile not exist into system");
			return responseHelper.getResponse();
		}

		pr.setZactive(doArchive ? Boolean.FALSE : Boolean.TRUE);
		long count = profileService.update(pr);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update profile");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Profile update successful");
		responseHelper.setRedirectUrl("/system/profile/" + pr.getProfilecode());
		return responseHelper.getResponse();
	}

	@PostMapping("/allprofilelines/update")
	public @ResponseBody Map<String, Object> updateAllProfileLine(ProfileLine profileLine){
		if(profileLine == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(ProfileType.M.equals(profileLine.getProfiletype())) {
			com.asl.model.MenuProfile mp = profileService.getMenuProfileByProfilecode(profileLine.getProfilecode());

			for(ProfileLine pl : mp.getProfileLines()) {
				if(!pl.getPgroup().equalsIgnoreCase(profileLine.getPgroup())) continue;
				if(StringUtils.isBlank(pl.getProfilelineid()) && pl.isDisplay() != profileLine.isDisplay()) {
					// create new
					pl.setPgroup(profileLine.getPgroup());
					pl.setDisplay(profileLine.isDisplay());
					profileLineService.save(pl);
				} else if(StringUtils.isNotBlank(pl.getProfilelineid()) && pl.isDisplay() != profileLine.isDisplay()){
					// update existing
					pl.setPgroup(profileLine.getPgroup());
					pl.setDisplay(profileLine.isDisplay());
					profileLineService.update(pl);
				}
			}
		} else if (ProfileType.U.equals(profileLine.getProfiletype())) {
			
		} else if (ProfileType.R.equals(profileLine.getProfiletype())) {
			ReportProfile rp = profileService.getReportProfileByProfilecode(profileLine.getProfilecode());

			for(ProfileLine pl : rp.getProfileLines()) {
				if(!pl.getPgroup().equalsIgnoreCase(profileLine.getPgroup())) continue;
				if((StringUtils.isBlank(pl.getProfilelineid()) && pl.isDisplay() != profileLine.isDisplay())
						|| (StringUtils.isBlank(pl.getProfilelineid()) && pl.isEnabled() != profileLine.isEnabled())) {
					// create new
					pl.setPgroup(profileLine.getPgroup());
					pl.setDisplay(profileLine.isDisplay());
					pl.setEnabled(profileLine.isEnabled());
					profileLineService.save(pl);
				} else if((StringUtils.isNotBlank(pl.getProfilelineid()) && pl.isDisplay() != profileLine.isDisplay())
						|| (StringUtils.isNotBlank(pl.getProfilelineid()) && pl.isEnabled() != profileLine.isEnabled())){
					// update existing
					pl.setPgroup(profileLine.getPgroup());
					pl.setDisplay(profileLine.isDisplay());
					pl.setEnabled(profileLine.isEnabled());
					profileLineService.update(pl);
				}
			}
		}

		if(ProfileType.M.equals(profileLine.getProfiletype())) {
			responseHelper.setReloadSectionIdWithUrl("menuprofilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
		} else if(ProfileType.U.equals(profileLine.getProfiletype())) {
			responseHelper.setReloadSectionIdWithUrl("userprofilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
		} else {
			responseHelper.setReloadSectionIdWithUrl("reportprofilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
		}
		responseHelper.setSuccessStatusAndMessage("Profile Line updated successfully");
		return responseHelper.getResponse();
	}

	// recursive method to get all profile parents
	private void getProfileLine(String listCode, String profileLineCode, List<ProfileLine> list, boolean forParent) {
		if(forParent) {
			ProfileLine pl = new ProfileLine(listService.getList(listCode, profileLineCode).stream().findFirst().orElse(null));
			if(pl == null || StringUtils.isBlank(pl.getProfilelinecode())) return;
			list.add(pl);
			if(!pl.getProfilelinecode().equalsIgnoreCase(pl.getParent())) getProfileLine(listCode, pl.getParent(), list, forParent);
		} else {
			List<DataList> dlist = listService.getList(listCode, null, null, null, null, null, null, null, null, profileLineCode);
			if(dlist == null || dlist.isEmpty()) return;
			for(DataList dl : dlist) {
				ProfileLine pl = new ProfileLine(dl);
				if(pl == null || StringUtils.isBlank(pl.getProfilelinecode())) return;
				list.add(pl);
				getProfileLine(listCode, pl.getProfilelinecode(), list, forParent);
			}
		}

	}

	@PostMapping("/profileline/save")
	public @ResponseBody Map<String, Object> saveProfileLine(ProfileLine profileLine){
		if(profileLine == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
		// new modification start
		ProfileLine currentProfile = new ProfileLine(listService.getList("MPROFILE", profileLine.getProfilelinecode()).stream().findFirst().orElse(null));
		List<ProfileLine> parentProfiles = new ArrayList<>();
		List<ProfileLine> childProfiles = new ArrayList<>();

		// get all parents
		if(!profileLine.getProfilelinecode().equalsIgnoreCase(currentProfile.getParent())) {
			getProfileLine("MPROFILE", currentProfile.getParent(), parentProfiles, true);
		}

		// get all childs
		getProfileLine("MPROFILE", currentProfile.getProfilelinecode(), childProfiles, false);

		if(profileLine.isDisplay()) {
			if(!currentProfile.getProfilelinecode().equalsIgnoreCase(currentProfile.getParent())) {
				// get all parent profile lines from dl
				// update all parent if exist
				// create parent profile if not exist
				for(ProfileLine parent : parentProfiles) {
					parent.setDisplay(true);
					ProfileLine existpl = profileLineService.findByProfilelinecodeAndProfilecodeAndProfiletype(parent.getProfilelinecode(), profileLine.getProfilecode(), parent.getProfiletype());
					if(existpl != null) {
						BeanUtils.copyProperties(parent, existpl, "profilecode", "profiletype", "profilelineid", "profilelinecode", "xtypetrn", "xtrn");
						long count = profileLineService.update(existpl);
					} else {
						parent.setProfilecode(profileLine.getProfilecode());
						parent.setXtypetrn(TransactionCodeType.PROFILE_LINE.getCode());
						parent.setXtrn(TransactionCodeType.PROFILE_LINE.getdefaultCode());
						long count = profileLineService.save(parent);
					}
				}
			}

		} 

		// get all childs
		// update all childs if exist
		// create all childs if not exist
		for(ProfileLine child : childProfiles) {
			child.setDisplay(profileLine.isDisplay());
			ProfileLine existpl = profileLineService.findByProfilelinecodeAndProfilecodeAndProfiletype(child.getProfilelinecode(), profileLine.getProfilecode(), child.getProfiletype());
			if(existpl != null) {
				BeanUtils.copyProperties(child, existpl, "profilecode", "profiletype", "profilelineid", "profilelinecode", "xtypetrn", "xtrn");
				long count = profileLineService.update(existpl);
			} else {
				child.setProfilecode(profileLine.getProfilecode());
				child.setXtypetrn(TransactionCodeType.PROFILE_LINE.getCode());
				child.setXtrn(TransactionCodeType.PROFILE_LINE.getdefaultCode());
				long count = profileLineService.save(child);
			}
		}

		

		
		
		// new modification end
		
		
		// if new profile line
		if(StringUtils.isBlank(profileLine.getProfilelineid())) {
			profileLine.setXtypetrn(TransactionCodeType.PROFILE_LINE.getCode());
			profileLine.setXtrn(TransactionCodeType.PROFILE_LINE.getdefaultCode());
			long count = profileLineService.save(profileLine);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			if(ProfileType.M.equals(profileLine.getProfiletype())) {
				responseHelper.setReloadSectionIdWithUrl("menuprofilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
			} else if(ProfileType.U.equals(profileLine.getProfiletype())) {
				responseHelper.setReloadSectionIdWithUrl("userprofilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
			} else {
				responseHelper.setReloadSectionIdWithUrl("reportprofilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
			}
			responseHelper.setSuccessStatusAndMessage("Profile Line updated successfully");
			return responseHelper.getResponse();
		}

		ProfileLine existProfileLine = profileLineService.findByProfilelinecodeAndProfilecodeAndProfiletype(profileLine.getProfilelinecode(), profileLine.getProfilecode(), profileLine.getProfiletype());
		if(existProfileLine == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(profileLine, existProfileLine, "profilecode", "profiletype", "profilelineid", "profilelinecode", "xtypetrn", "xtrn");
		long count = profileLineService.update(existProfileLine);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(ProfileType.M.equals(profileLine.getProfiletype())) {
			responseHelper.setReloadSectionIdWithUrl("menuprofilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
		} else if(ProfileType.U.equals(profileLine.getProfiletype())) {
			responseHelper.setReloadSectionIdWithUrl("userprofilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
		} else {
			responseHelper.setReloadSectionIdWithUrl("reportprofilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
		}
		responseHelper.setSuccessStatusAndMessage("Profile Line updated successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/profilelines/{profilecode}/{profiletype}")
	public String reloadProfileLineTable(@PathVariable String profilecode, @PathVariable ProfileType profiletype, Model model) {
		Map<String, ProfileLineWrapper> profileLinesMap = new HashMap<>();
		Map<String, String> groupMap = new HashMap<>();
		if(ProfileType.M.equals(profiletype)) {
			com.asl.model.MenuProfile mp = profileService.getMenuProfileByProfilecode(profilecode);

			mp.getProfileLines().stream().forEach(pl -> {
				if(profileLinesMap.get(pl.getPgroup()) != null) {
					ProfileLineWrapper wrapper = profileLinesMap.get(pl.getPgroup());
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
				} else {
					List<ProfileLine> list = new ArrayList<>();
					list.add(pl);
					ProfileLineWrapper wrapper = new ProfileLineWrapper();
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
					profileLinesMap.put(pl.getPgroup(), wrapper);
				}
				
				if(groupMap.get(pl.getPgroup()) == null) {
					groupMap.put(pl.getPgroup(), pl.getParent());
				}
			});
		} else if (ProfileType.U.equals(profiletype)) {
			// TODO: 
		} else if (ProfileType.R.equals(profiletype)) {
			ReportProfile rp = profileService.getReportProfileByProfilecode(profilecode);

			rp.getProfileLines().stream().forEach(pl -> {
				if(profileLinesMap.get(pl.getPgroup()) != null) {
					ProfileLineWrapper wrapper = profileLinesMap.get(pl.getPgroup());
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
					if(wrapper.isAllenabled()) wrapper.setAllenabled(pl.isEnabled());
				} else {
					List<ProfileLine> list = new ArrayList<>();
					list.add(pl);
					ProfileLineWrapper wrapper = new ProfileLineWrapper();
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
					if(wrapper.isAllenabled()) wrapper.setAllenabled(pl.isEnabled());
					profileLinesMap.put(pl.getPgroup(), wrapper);
				}
				
				if(groupMap.get(pl.getPgroup()) == null) {
					groupMap.put(pl.getPgroup(), pl.getPgroupname());
				}
			});
			
		}
		
		
		
		
		// New system
		List<ProfileLine> masters = new ArrayList<>();
		com.asl.model.MenuProfile mp = profileService.getMenuProfileByProfilecode(profilecode);
		if(mp != null) {
			for(ProfileLine pl : mp.getProfileLines()) {
				if(StringUtils.isNotBlank(pl.getRoles())) {
					String[] roles = pl.getRoles().split(",");
					boolean rolePermissionOk = false;
					for(String role : Arrays.asList(roles)) {
						if(sessionManager.getLoggedInUserDetails().getRoles().contains(role)) {
							rolePermissionOk = true;
						}
					}
					pl.setRoleHasAccess(rolePermissionOk);
				}

				pl.setSubmenus(new ArrayList<>());
				if("MASTER".equalsIgnoreCase(pl.getMenutype())) {
					pl.setProfilelebel(1);
					masters.add(pl);
				}
			}
		}

		for(ProfileLine master : masters) {
			for(ProfileLine pl : mp.getProfileLines()) {
				if(pl == null || !pl.getParent().equalsIgnoreCase(master.getProfilelinecode())) {
					continue;
				}

				if(StringUtils.isNotBlank(pl.getRoles())) {
					String[] roles = pl.getRoles().split(",");
					boolean rolePermissionOk = false;
					for(String role : Arrays.asList(roles)) {
						if(sessionManager.getLoggedInUserDetails().getRoles().contains(role)) {
							rolePermissionOk = true;
						}
					}
					pl.setRoleHasAccess(rolePermissionOk);
				}

				pl.setProfilelebel(2);
				master.getSubmenus().add(pl);
				pl.setSubmenus(new ArrayList<>());

				// get sub menus menu
				if("SUB-MASTER".equalsIgnoreCase(pl.getMenutype())) {
					for(ProfileLine pl2 : mp.getProfileLines()) {
						if(pl2 == null || !pl2.getParent().equalsIgnoreCase(pl.getProfilelinecode())) {
							continue;
						}

						if(StringUtils.isNotBlank(pl2.getRoles())) {
							String[] roles = pl2.getRoles().split(",");
							boolean rolePermissionOk = false;
							for(String role : Arrays.asList(roles)) {
								if(sessionManager.getLoggedInUserDetails().getRoles().contains(role)) {
									rolePermissionOk = true;
								}
							}
							pl2.setRoleHasAccess(rolePermissionOk);
						}

						pl2.setProfilelebel(3);
						pl.getSubmenus().add(pl2);
					}
				}
			}
		}
		
		
		masters.stream().forEach(m -> System.out.println(m.toString()));
		model.addAttribute("profiletree", masters);
		
		

		model.addAttribute("grpmap", groupMap);
		model.addAttribute("plmap", profileLinesMap);

		Profile p = new Profile();
		p.setProfilecode(profilecode);
		p.setProfiletype(profiletype);
		model.addAttribute("profile", p);

		if(ProfileType.M.equals(profiletype)) {
			return "pages/system/usersentry/profile/profile::menuprofilelinetable";
		} else if (ProfileType.U.equals(profiletype)) {
			return "pages/system/usersentry/profile/profile::userprofilelinetable";
		} else {
			return "pages/system/usersentry/profile/profile::reportprofilelinetable";
		}
	}

	@GetMapping("/profilelinemodal/{profilelinecode}/{profilecode}/{profiletype}/show")
	public String loadProfileLineModal(@PathVariable String profilelinecode, @PathVariable String profilecode, @PathVariable ProfileType profiletype, Model model) {
		ProfileLine pl = profileLineService.findByProfilelinecodeAndProfilecodeAndProfiletype(profilelinecode, profilecode, profiletype);
		if(pl == null) {
			if(ProfileType.M.equals(profiletype)) {
				pl = new ProfileLine(MenuProfile.valueOf(profilelinecode));
			} else if (ProfileType.U.equals(profiletype)) {
				
			} else if (ProfileType.R.equals(profiletype)) {
				pl = new ProfileLine(ReportMenu.valueOf(profilelinecode));
			}
		}

		pl.setProfilecode(profilecode);
		model.addAttribute("profileline", pl);

		if(ProfileType.M.equals(profiletype)) {
			return "pages/system/usersentry/profile/menuprofilelinemodal::menuprofilelinemodal";
		} else if (ProfileType.R.equals(profiletype)) {
			return "pages/system/usersentry/profile/reportprofilelinemodal::reportprofilelinemodal";
		} else {
			return "pages/system/usersentry/profile/userprofilelinemodal::userprofilelinemodal";
		}
	}

}

