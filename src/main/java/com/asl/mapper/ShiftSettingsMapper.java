package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ShiftSettings;

@Mapper
public interface ShiftSettingsMapper {

	public long saveShiftSettings(ShiftSettings shiftSettings);

	public long updateShiftSettings(ShiftSettings shiftSettings);

	public List<ShiftSettings> getAllShiftSettings();

	public ShiftSettings findByShiftSettings(String xshift, String zid);
}
