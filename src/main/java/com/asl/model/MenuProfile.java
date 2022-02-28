package com.asl.model;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.asl.entity.ProfileLine;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since 02-12-2020
 */
@Slf4j
@Data
public class MenuProfile implements Serializable {

	private static final long serialVersionUID = 5690006573031426825L;

	private ProfileLine M0100;
	private ProfileLine M0101;
	private ProfileLine M0102;
	private ProfileLine M0103;
	private ProfileLine M0104;
	private ProfileLine M0105;
	private ProfileLine M0106;
	private ProfileLine M0107;
	private ProfileLine M0108;
	private ProfileLine M0109;
	private ProfileLine M0110;
	private ProfileLine M0111;
	private ProfileLine M0112;
	private ProfileLine M0113;
	private ProfileLine M0114;
	private ProfileLine M0115;
	private ProfileLine M0116;
	private ProfileLine M0117;
	private ProfileLine M0119;

	private ProfileLine M0200;
	private ProfileLine M0201;
	private ProfileLine M0202;
	private ProfileLine M0203;
	private ProfileLine M0204;
	private ProfileLine M0205;
	private ProfileLine M0206;
	private ProfileLine M0207;
	private ProfileLine M0208;

	private ProfileLine M0300;
	private ProfileLine M0301;
	private ProfileLine M0302;
	private ProfileLine M0303;
	private ProfileLine M0304;
	private ProfileLine M0305;
	private ProfileLine M0306;
	private ProfileLine M0307;
	private ProfileLine M0308;
	private ProfileLine M0309;
	private ProfileLine M0310;
	private ProfileLine M0311;
	private ProfileLine M0312;
	private ProfileLine M0313;
	private ProfileLine M0314;
	private ProfileLine M0315;
	private ProfileLine M0316;
	private ProfileLine M0317;
	private ProfileLine M0318;
	private ProfileLine M0319;
	private ProfileLine M0320;
	private ProfileLine M0321;
	private ProfileLine M0322;
	//Report
	private ProfileLine RM0300;
	private ProfileLine RM0301;
	private ProfileLine RM0305;
	private ProfileLine RM0306;

	private ProfileLine M0400;
	private ProfileLine M0401;
	private ProfileLine M0402;
	private ProfileLine M0403;
	private ProfileLine M0404;
	private ProfileLine M0405;
	private ProfileLine M0406;
	private ProfileLine M0407;
	private ProfileLine M0408;
	private ProfileLine M0409;
	private ProfileLine M0410;
	private ProfileLine M0411;
	private ProfileLine M0412;
	private ProfileLine M0413;
	//Report
	private ProfileLine RM0400;
	private ProfileLine RM0401;
	private ProfileLine RM0402;
	private ProfileLine RM0403;
	private ProfileLine RM0407;
	private ProfileLine RM0410;
	private ProfileLine RM0411;
	private ProfileLine RM0420;
	private ProfileLine RM0421;
	private ProfileLine RM0422;
	private ProfileLine RM0423;
	private ProfileLine RM0424;
	private ProfileLine RM0425;
	private ProfileLine RM0426;

	private ProfileLine M0500;
	private ProfileLine M0502;
	private ProfileLine M0503;
	private ProfileLine M0504;
	private ProfileLine M0505;
	private ProfileLine M0506;
	private ProfileLine M0507;
	//Report
	private ProfileLine RM0500;
	private ProfileLine RM0501;
	private ProfileLine RM0502;
	private ProfileLine RM0503;

	private ProfileLine M0600;
	private ProfileLine M0601;
	private ProfileLine M0602;
	private ProfileLine M0603;
	private ProfileLine M0604;
	private ProfileLine M0605;
	private ProfileLine M0606;
	private ProfileLine M0607;
	private ProfileLine M0608;
	private ProfileLine M0609;
	private ProfileLine M0610;
	private ProfileLine M0611;
	private ProfileLine M0612;
	private ProfileLine M0613;
	private ProfileLine M0614;
	private ProfileLine M0615;
	//Report
	private ProfileLine RM0600;
	private ProfileLine RM0601;
	private ProfileLine RM0602;
	private ProfileLine RM0603;
	private ProfileLine RM0605;
	private ProfileLine RM0606;
	private ProfileLine RM0607;
	private ProfileLine RM0608;
	private ProfileLine RM0609;
	private ProfileLine RM0610;
	private ProfileLine RM0611;

	private ProfileLine M0700;

	private ProfileLine M0800;
	private ProfileLine M0801;
	private ProfileLine M0802;
	private ProfileLine M0803;
	private ProfileLine M0804;
	//Report
	private ProfileLine RM0800;
	private ProfileLine RM0314;
	private ProfileLine RM0404;
	private ProfileLine RM0405;
	private ProfileLine RM0406;

	private ProfileLine M0900;
	private ProfileLine M0901;
	private ProfileLine M0902;
	private ProfileLine M0903;
	//Report
	private ProfileLine RM0900;
	private ProfileLine RM0307;
	private ProfileLine RM0308;
	private ProfileLine RM0309;
	private ProfileLine RM0311;
	private ProfileLine RM0312;
	private ProfileLine RM0313;

	private ProfileLine M1000;
	private ProfileLine M1001;
	private ProfileLine M1002;
	private ProfileLine M1003;
	private ProfileLine M1004;
	private ProfileLine M1005;
	private ProfileLine M1006;
	private ProfileLine M1007;
	private ProfileLine M1008;
	private ProfileLine M1009;
	private ProfileLine M1010;
	private ProfileLine M1011;
	private ProfileLine M1012;
	private ProfileLine M1013;
	private ProfileLine M1014;
	private ProfileLine M1015;
	
	//Report
	private ProfileLine RM1000;
	private ProfileLine RM0801;
	private ProfileLine RM0803;
	private ProfileLine RM0804;
	private ProfileLine RM0805;
	private ProfileLine RM0806;
	private ProfileLine RM0807;
	private ProfileLine RM0808;
	private ProfileLine RM0809;
	private ProfileLine RM0810;

	private ProfileLine M1100;
	private ProfileLine M1101;
	private ProfileLine M1102;
	private ProfileLine M1103;
	private ProfileLine M1104;
	private ProfileLine M1105;
	private ProfileLine M1106;
	private ProfileLine M1107;
	private ProfileLine M1108;
	private ProfileLine M1109;
	//Report
	private ProfileLine RM1100;
	
	private ProfileLine M1200;
	private ProfileLine M1201;
	private ProfileLine M1202;
	private ProfileLine M1203;
	private ProfileLine M1204;
	private ProfileLine M1205;
	private ProfileLine M1206;
	private ProfileLine M1207;
	//Report
	private ProfileLine RM1200;
	private ProfileLine RM0701;
	private ProfileLine RM0702;
	private ProfileLine RM0703;
	private ProfileLine RM0704;
	private ProfileLine RM0705;
	private ProfileLine RM0706;
	private ProfileLine RM0707;
	private ProfileLine RM0708;

	private ProfileLine M1300;
	private ProfileLine M1301;
	private ProfileLine M1302;
	private ProfileLine M1303;
	private ProfileLine M1304;
	private ProfileLine M1305;
	private ProfileLine M1306;
	//Report
	private ProfileLine RM1300;
	private ProfileLine RM0901;
	private ProfileLine RM0902;
	private ProfileLine RM0903;
	private ProfileLine RM0904;
	private ProfileLine RM0905;
	private ProfileLine RM0906;
	private ProfileLine RM0907;
	private ProfileLine RM0908;
	private ProfileLine RM0909;
	private ProfileLine RM0910;
	private ProfileLine RM0911;
	private ProfileLine RM0912;
	//MIS
	private ProfileLine M1400;
	private ProfileLine M1401;
	//Report
	private ProfileLine RM1400;

	private List<ProfileLine> profileLines = new ArrayList<>();

	private static Map<String, Method> profileSetterMethods = new HashMap<>();

	/**
	 * Add newly added menu option declared in PROFILE list
	 */
	static {
		setterCaller();
	}

	public static void setterCaller() {
		EnumSet.allOf(com.asl.enums.MenuProfile.class).forEach(mp -> addSetter(mp.name(), mp.name()));
	}

	public static void addSetter(String code, String fieldName) {
		try {
			String methodName = "set" + StringUtils.capitalize(fieldName);
			Method method = MenuProfile.class.getMethod(methodName, ProfileLine.class);
			profileSetterMethods.put(code.toUpperCase(), method);
		} catch (NoSuchMethodException | SecurityException  e) {
			log.error("Error is : {}, {}", e.getMessage(),e);
		}
	}

	public static ProfileLine invokeGetter(Object obj, String variableName){
		try {
			PropertyDescriptor pd = new PropertyDescriptor(variableName, obj.getClass());
			Method getter = pd.getReadMethod();
			return (ProfileLine) getter.invoke(obj);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void addToProfileLineMap(ProfileLine profileLine) {
		profileLines.add(profileLine);
		profileLines.sort(Comparator.comparing(ProfileLine::getSeqn));
	}

	public void setProfileLine(ProfileLine profileLine) {
		if(profileLine == null || StringUtils.isEmpty(profileLine.getProfilelinecode())) {
			log.warn("No profile or profile code found....");
			return;
		}

		String code = profileLine.getProfilelinecode().toUpperCase();
		if(profileSetterMethods.containsKey(code)) {
			Method method = profileSetterMethods.get(code);
			try {
				method.invoke(this, profileLine);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.warn("Unbale to set profile for {}", code, e);
			}
		} else {
			addToProfileLineMap(profileLine);
		}
	}

	// Setter methods
	public void setM0100(ProfileLine profileLine) {
		this.M0100 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0101(ProfileLine profileLine) {
		this.M0101 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0102(ProfileLine profileLine) {
		this.M0102 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0103(ProfileLine profileLine) {
		this.M0103 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0104(ProfileLine profileLine) {
		this.M0104 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0105(ProfileLine profileLine) {
		this.M0105 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0106(ProfileLine profileLine) {
		this.M0106 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0107(ProfileLine profileLine) {
		this.M0107 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0108(ProfileLine profileLine) {
		this.M0108 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0109(ProfileLine profileLine) {
		this.M0109 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0110(ProfileLine profileLine) {
		this.M0110 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0111(ProfileLine profileLine) {
		this.M0111 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0112(ProfileLine profileLine) {
		this.M0112 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0113(ProfileLine profileLine) {
		this.M0113 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0114(ProfileLine profileLine) {
		this.M0114 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0115(ProfileLine profileLine) {
		this.M0115 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0116(ProfileLine profileLine) {
		this.M0116 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0117(ProfileLine profileLine) {
		this.M0117 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0119(ProfileLine profileLine) {
		this.M0119 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0200(ProfileLine profileLine) {
		this.M0200 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0201(ProfileLine profileLine) {
		this.M0201 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0202(ProfileLine profileLine) {
		this.M0202 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0203(ProfileLine profileLine) {
		this.M0203 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0204(ProfileLine profileLine) {
		this.M0204 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0205(ProfileLine profileLine) {
		this.M0205 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0206(ProfileLine profileLine) {
		this.M0206 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0207(ProfileLine profileLine) {
		this.M0207 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0208(ProfileLine profileLine) {
		this.M0208 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0300(ProfileLine profileLine) {
		this.M0300 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0301(ProfileLine profileLine) {
		this.M0301 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0302(ProfileLine profileLine) {
		this.M0302 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0303(ProfileLine profileLine) {
		this.M0303 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0304(ProfileLine profileLine) {
		this.M0304 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0305(ProfileLine profileLine) {
		this.M0305 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0306(ProfileLine profileLine) {
		this.M0306 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0307(ProfileLine profileLine) {
		this.M0307 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0308(ProfileLine profileLine) {
		this.M0308 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0309(ProfileLine profileLine) {
		this.M0309 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0310(ProfileLine profileLine) {
		this.M0310 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0311(ProfileLine profileLine) {
		this.M0311 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0312(ProfileLine profileLine) {
		this.M0312 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0313(ProfileLine profileLine) {
		this.M0313 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0314(ProfileLine profileLine) {
		this.M0314 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0315(ProfileLine profileLine) {
		this.M0315 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0316(ProfileLine profileLine) {
		this.M0316 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0317(ProfileLine profileLine) {
		this.M0317 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0318(ProfileLine profileLine) {
		this.M0318 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0319(ProfileLine profileLine) {
		this.M0319 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0320(ProfileLine profileLine) {
		this.M0320 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0321(ProfileLine profileLine) {
		this.M0321 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0322(ProfileLine profileLine) {
		this.M0322 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//Report
	public void setRM0300(ProfileLine profileLine) {
		this.RM0300 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0301(ProfileLine profileLine) {
		this.RM0301 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0305(ProfileLine profileLine) {
		this.RM0305 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0306(ProfileLine profileLine) {
		this.RM0306 = profileLine;
		addToProfileLineMap(profileLine);
	}



	public void setM0400(ProfileLine profileLine) {
		this.M0400 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0401(ProfileLine profileLine) {
		this.M0401 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0402(ProfileLine profileLine) {
		this.M0402 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0403(ProfileLine profileLine) {
		this.M0403 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0404(ProfileLine profileLine) {
		this.M0404 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0405(ProfileLine profileLine) {
		this.M0405 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0406(ProfileLine profileLine) {
		this.M0406 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0407(ProfileLine profileLine) {
		this.M0407 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0408(ProfileLine profileLine) {
		this.M0408 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0409(ProfileLine profileLine) {
		this.M0409 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0410(ProfileLine profileLine) {
		this.M0410 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0411(ProfileLine profileLine) {
		this.M0411 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0412(ProfileLine profileLine) {
		this.M0412 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0413(ProfileLine profileLine) {
		this.M0413 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//Report
	public void setRM0400(ProfileLine profileLine) {
		this.RM0400 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0401(ProfileLine profileLine) {
		this.RM0401 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0402(ProfileLine profileLine) {
		this.RM0402 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0403(ProfileLine profileLine) {
		this.RM0403 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0407(ProfileLine profileLine) {
		this.RM0407 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0410(ProfileLine profileLine) {
		this.RM0410 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0411(ProfileLine profileLine) {
		this.RM0411 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0420(ProfileLine profileLine) {
		this.RM0420 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0421(ProfileLine profileLine) {
		this.RM0421 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0422(ProfileLine profileLine) {
		this.RM0422 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0423(ProfileLine profileLine) {
		this.RM0423 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0424(ProfileLine profileLine) {
		this.RM0424 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0425(ProfileLine profileLine) {
		this.RM0425 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0426(ProfileLine profileLine) {
		this.RM0426 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0500(ProfileLine profileLine) {
		this.M0500 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0502(ProfileLine profileLine) {
		this.M0502 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0503(ProfileLine profileLine) {
		this.M0503 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0504(ProfileLine profileLine) {
		this.M0504 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0505(ProfileLine profileLine) {
		this.M0505 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0506(ProfileLine profileLine) {
		this.M0506 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0507(ProfileLine profileLine) {
		this.M0507 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//Report
	public void setRM0500(ProfileLine profileLine) {
		this.RM0500 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0501(ProfileLine profileLine) {
		this.RM0501 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0502(ProfileLine profileLine) {
		this.RM0502 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0503(ProfileLine profileLine) {
		this.RM0503 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0600(ProfileLine profileLine) {
		this.M0600 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0601(ProfileLine profileLine) {
		this.M0601 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0602(ProfileLine profileLine) {
		this.M0602 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0603(ProfileLine profileLine) {
		this.M0603 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0604(ProfileLine profileLine) {
		this.M0604 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0605(ProfileLine profileLine) {
		this.M0605 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0606(ProfileLine profileLine) {
		this.M0606 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0607(ProfileLine profileLine) {
		this.M0607 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0608(ProfileLine profileLine) {
		this.M0608 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0609(ProfileLine profileLine) {
		this.M0609 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0610(ProfileLine profileLine) {
		this.M0610 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0611(ProfileLine profileLine) {
		this.M0611 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0612(ProfileLine profileLine) {
		this.M0612 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0613(ProfileLine profileLine) {
		this.M0613 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0614(ProfileLine profileLine) {
		this.M0614 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0615(ProfileLine profileLine) {
		this.M0615 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//Report
	public void setRM0600(ProfileLine profileLine) {
		this.RM0600 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0601(ProfileLine profileLine) {
		this.RM0601 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0602(ProfileLine profileLine) {
		this.RM0602 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0603(ProfileLine profileLine) {
		this.RM0603 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0605(ProfileLine profileLine) {
		this.RM0605 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0606(ProfileLine profileLine) {
		this.RM0606 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0607(ProfileLine profileLine) {
		this.RM0607 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0611(ProfileLine profileLine) {
		this.RM0611 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0608(ProfileLine profileLine) {
		this.RM0608 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0609(ProfileLine profileLine) {
		this.RM0609 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0610(ProfileLine profileLine) {
		this.RM0610 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0700(ProfileLine profileLine) {
		this.M0700 = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setM0800(ProfileLine profileLine) {
		this.M0800 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0801(ProfileLine profileLine) {
		this.M0801 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0802(ProfileLine profileLine) {
		this.M0802 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0803(ProfileLine profileLine) {
		this.M0803 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0804(ProfileLine profileLine) {
		this.M0804 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//Report
	public void setRM0800(ProfileLine profileLine) {
		this.RM0800 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0314(ProfileLine profileLine) {
		this.RM0314 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0404(ProfileLine profileLine) {
		this.RM0404 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0405(ProfileLine profileLine) {
		this.RM0405 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0406(ProfileLine profileLine) {
		this.RM0406 = profileLine;
		addToProfileLineMap(profileLine);
	}


	public void setM0900(ProfileLine profileLine) {
		this.M0900 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0901(ProfileLine profileLine) {
		this.M0901 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0902(ProfileLine profileLine) {
		this.M0902 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM0903(ProfileLine profileLine) {
		this.M0903 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//Report
	public void setRM0900(ProfileLine profileLine) {
		this.RM0900 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0307(ProfileLine profileLine) {
		this.RM0307 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0308(ProfileLine profileLine) {
		this.RM0308 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0309(ProfileLine profileLine) {
		this.RM0309 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0311(ProfileLine profileLine) {
		this.RM0311 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0312(ProfileLine profileLine) {
		this.RM0312 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0313(ProfileLine profileLine) {
		this.RM0313 = profileLine;
		addToProfileLineMap(profileLine);
	}



	public void setM1000(ProfileLine profileLine) {
		this.M1000 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1001(ProfileLine profileLine) {
		this.M1001 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1002(ProfileLine profileLine) {
		this.M1002 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1003(ProfileLine profileLine) {
		this.M1003 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1004(ProfileLine profileLine) {
		this.M1004 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1005(ProfileLine profileLine) {
		this.M1005 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1006(ProfileLine profileLine) {
		this.M1006 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1007(ProfileLine profileLine) {
		this.M1007 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1008(ProfileLine profileLine) {
		this.M1008 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1009(ProfileLine profileLine) {
		this.M1009 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1010(ProfileLine profileLine) {
		this.M1010 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1011(ProfileLine profileLine) {
		this.M1011 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1012(ProfileLine profileLine) {
		this.M1012 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1013(ProfileLine profileLine) {
		this.M1013 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1015(ProfileLine profileLine) {
		this.M1015 = profileLine;
		addToProfileLineMap(profileLine);
	}
	
	
	
	
	//Report
	public void setRM1000(ProfileLine profileLine) {
		this.RM1000 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0801(ProfileLine profileLine) {
		this.RM0801 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0803(ProfileLine profileLine) {
		this.RM0803 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0804(ProfileLine profileLine) {
		this.RM0804 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0805(ProfileLine profileLine) {
		this.RM0805 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0806(ProfileLine profileLine) {
		this.RM0806 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0807(ProfileLine profileLine) {
		this.RM0807 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0808(ProfileLine profileLine) {
		this.RM0808 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0809(ProfileLine profileLine) {
		this.RM0809 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0810(ProfileLine profileLine) {
		this.RM0810 = profileLine;
		addToProfileLineMap(profileLine);
	}
	



	public void setM1100(ProfileLine profileLine) {
		this.M1100 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1101(ProfileLine profileLine) {
		this.M1101 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1102(ProfileLine profileLine) {
		this.M1102 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1103(ProfileLine profileLine) {
		this.M1103 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1104(ProfileLine profileLine) {
		this.M1104 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1105(ProfileLine profileLine) {
		this.M1105 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1106(ProfileLine profileLine) {
		this.M1106 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1107(ProfileLine profileLine) {
		this.M1107 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1108(ProfileLine profileLine) {
		this.M1108 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1109(ProfileLine profileLine) {
		this.M1109 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//Report
	public void setRM1100(ProfileLine profileLine) {
		this.RM1100 = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setM1200(ProfileLine profileLine) {
		this.M1200 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1201(ProfileLine profileLine) {
		this.M1201 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1202(ProfileLine profileLine) {
		this.M1202 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1203(ProfileLine profileLine) {
		this.M1203 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1204(ProfileLine profileLine) {
		this.M1204 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1205(ProfileLine profileLine) {
		this.M1205 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1206(ProfileLine profileLine) {
		this.M1206 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1207(ProfileLine profileLine) {
		this.M1207 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//Report
	public void setRM1200(ProfileLine profileLine) {
		this.RM1200 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0701(ProfileLine profileLine) {
		this.RM0701 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0702(ProfileLine profileLine) {
		this.RM0702 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0703(ProfileLine profileLine) {
		this.RM0703 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0704(ProfileLine profileLine) {
		this.RM0704 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0705(ProfileLine profileLine) {
		this.RM0705 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0706(ProfileLine profileLine) {
		this.RM0706 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0707(ProfileLine profileLine) {
		this.RM0707 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0708(ProfileLine profileLine) {
		this.RM0708 = profileLine;
		addToProfileLineMap(profileLine);
	}

	public void setM1300(ProfileLine profileLine) {
		this.M1300 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1301(ProfileLine profileLine) {
		this.M1301 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1302(ProfileLine profileLine) {
		this.M1302 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1303(ProfileLine profileLine) {
		this.M1303 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1304(ProfileLine profileLine) {
		this.M1304 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1305(ProfileLine profileLine) {
		this.M1305 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1306(ProfileLine profileLine) {
		this.M1306 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//Report
	public void setRM1300(ProfileLine profileLine) {
		this.RM1300 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0901(ProfileLine profileLine) {
		this.RM0901 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0902(ProfileLine profileLine) {
		this.RM0902 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0903(ProfileLine profileLine) {
		this.RM0903 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0904(ProfileLine profileLine) {
		this.RM0904 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0905(ProfileLine profileLine) {
		this.RM0905 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0906(ProfileLine profileLine) {
		this.RM0906 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0907(ProfileLine profileLine) {
		this.RM0907 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0908(ProfileLine profileLine) {
		this.RM0908 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0909(ProfileLine profileLine) {
		this.RM0909 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0910(ProfileLine profileLine) {
		this.RM0910 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0911(ProfileLine profileLine) {
		this.RM0911 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setRM0912(ProfileLine profileLine) {
		this.RM0912 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//MIS
	public void setM1400(ProfileLine profileLine) {
		this.M1400 = profileLine;
		addToProfileLineMap(profileLine);
	}
	public void setM1401(ProfileLine profileLine) {
		this.M1401 = profileLine;
		addToProfileLineMap(profileLine);
	}
	//Report
	public void setRM1400(ProfileLine profileLine) {
		this.RM1400 = profileLine;
		addToProfileLineMap(profileLine);
	}
	
}
