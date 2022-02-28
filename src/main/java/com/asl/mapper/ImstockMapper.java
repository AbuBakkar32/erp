package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Imstock;

@Mapper
public interface ImstockMapper {

	public List<Imstock> findByXitem(String xitem, String zid);

	public Imstock findByXitemAndXwh(String xitem, String xwh, String zid);

	public List<Imstock> searchXitem(String xitem, String zid);

	public List<Imstock> search(String xwh, String xitem, String zid);
}
