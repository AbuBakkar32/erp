package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Imstock;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Component
public interface ImstockService {

	public List<Imstock> findByXitem(String xitem);

	public Imstock findByXitemAndXwh(String xitem, String xwh);

	public List<Imstock> searchXitem(String xitem);

	public List<Imstock> search(String xwh, String xitem);
}
