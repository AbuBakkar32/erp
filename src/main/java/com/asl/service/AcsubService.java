package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Acsub;

/**
 * @author Zubayer Ahamed
 * @since Jul 25, 2021
 */
@Component
public interface AcsubService {

	public long save(Acsub acsub);

	public long update(Acsub acsub);

	public long delete(String xacc, String xsub);

	public List<Acsub> getAll();

	public Acsub findByXaccAndXsub(String xacc, String xsub);
}
