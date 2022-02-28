package com.asl.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Zubayer Ahamed
 * @since May 16, 2021
 */
@Converter
public class CKTimeConverter implements AttributeConverter<CKTime, String> {

	@Override
	public String convertToDatabaseColumn(CKTime time) {
		return time == null ? null : time.getT5Time();
	}

	@Override
	public CKTime convertToEntityAttribute(String data) {
		return data == null || data.isEmpty() ? null : new CKTime(data);
	}

}
