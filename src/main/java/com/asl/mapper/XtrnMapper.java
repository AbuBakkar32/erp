package com.asl.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Xtrn;

/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Mapper
public interface XtrnMapper {

	public long save(Xtrn xtrn);

	public long update(Xtrn xtrn);

	public List<Xtrn> getAllXtrn(String zid, Boolean zactive);

	public List<Xtrn> findByXtypetrn(String xtypetrn, String zid, Boolean zactive);

	public List<Xtrn> findByXtrn(String xtrn, String zid, Boolean zactive);

	public Xtrn findByXtypetrnAndXtrn(String xtypetrn, String xtrn, String zid, Boolean zactive);

	public String generateXtrn(String xtypetrn, String xtrn, int leftpad, String zid);

	public long delete(String xtypetrn, String xtrn, String zid);

	public List<Map<String, Object>> getExportDataByChunk(long limit, long page, String zid);

}
