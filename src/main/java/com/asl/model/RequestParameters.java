package com.asl.model;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.asl.enums.ReportType;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Data
public class RequestParameters {

	private String reportCode;
	private String status;
	private ReportType reportType;

	private Object param1;
	private Object param2;
	private Object param3;
	private Object param4;
	private Object param5;
	private Object param6;
	private Object param7;
	private Object param8;
	private Object param9;
	private Object param10;
	private Object param11;
	private Object param12;
	private Object param13;
	private Object param14;
	private Object param15;
	private Object param16;
	private Object param17;
	private Object param18;
	private Object param19;
	private Object param20;
	private Object param21;
	private Object param22;
	private Object param23;
	private Object param24;
	private Object param25;
	private Object param26;
	private Object param27;
	private Object param28;
	private Object param29;
	private Object param30;

	public static Object invokeGetter(Object obj, String variableName){
		try {
			PropertyDescriptor pd = new PropertyDescriptor(variableName, obj.getClass());
			Method getter = pd.getReadMethod();
			return getter.invoke(obj);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
	}
}
