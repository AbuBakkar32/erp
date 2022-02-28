package com.asl.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.PSV;

/**
 * @author Zubayer Ahamed
 * @since May 20, 2021
 */
@Mapper
public interface PSVMapper {

	public long savePSV(PSV psv);

	public long updatePSV(PSV psv);

	public PSV findByXchalanAndXbatchAndXrawitem(String xchalan, String xbatch, String xrawitem, String zid);

	public BigDecimal getTotalRawUsedExceptCurrentBatch(String xchalan, String xrawitem, String xbatch, String zid);

	public long deletePSV(PSV psv);
}
