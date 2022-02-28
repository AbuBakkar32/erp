package com.asl.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.asl.entity.POSSettings;;

@Mapper
public interface POSSettingsMapper {

	public long savePOSSettings(POSSettings pOSSettings);

	public long updatePOSSettings(POSSettings pOSSettings);

	public List<POSSettings> getAllPOSSettings(String zid);

	public POSSettings findByPOSSettings(String xacc, String zid);
}
