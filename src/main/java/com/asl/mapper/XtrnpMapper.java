package com.asl.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Xtrnp;

@Mapper
public interface XtrnpMapper {
	public long save(Xtrnp xtrnp);
	public long update(Xtrnp xtrnp);
	public long delete(Xtrnp xtrnp);
	
	public Xtrnp findXtrnpByXvoucher(String xtypetrn, String xtrn, String xtyperel,  String zid);
	public List<Xtrnp> getAllXtrnp(String zid);
}
