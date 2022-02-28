package com.asl.service;

import java.math.BigDecimal;

import com.asl.entity.PSV;

/**
 * @author Zubayer Ahamed
 * @since May 20, 2021
 */
public interface PSVService {

	public long savePSV(PSV psv);

	public long updatePSV(PSV psv);

	public long deletePSV(PSV psv);

	public PSV findByXchalanAndXbatchAndXrawitem(String xchalan, String xbatch, String xrawitem);

	public BigDecimal getTotalRawUsedExceptCurrentBatch(String xchalan, String xrawitem, String xbatch);
}
