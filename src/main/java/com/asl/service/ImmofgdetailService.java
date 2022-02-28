package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Immofgdetail;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2021
 */
@Component
public interface ImmofgdetailService {

	public long save(Immofgdetail immofgdetail);

	public long update(Immofgdetail immofgdetail);

	public Immofgdetail findImmofgDetailByXtornumAndXrow(String xtornum, int xrow);

	public List<Immofgdetail> getAllImmofgDetail();
}
