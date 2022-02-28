package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Acsub;

/**
 * @author Zubayer Ahamed
 * @since Jul 25, 2021
 */
@Mapper
public interface AcsubMapper {

	public long save(Acsub acsub);

	public long update(Acsub acsub);

	public long delete(String xacc, String xsub, String zid);

	public List<Acsub> getAll(String zid);

	public Acsub findByXaccAndXsub(String xacc, String xsub, String zid);
}
