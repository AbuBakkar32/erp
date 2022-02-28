package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Vatait;

@Mapper
public interface VataitMapper {
	public long saveVatait(Vatait vatait);
	public long updateVatait(Vatait vatait);
	public long deleteVatait(Vatait vatait);
	
	public Vatait findVataitByXvatait(String xvatait, String zid);
	public List<Vatait> getAllVatait(String zid);
}
