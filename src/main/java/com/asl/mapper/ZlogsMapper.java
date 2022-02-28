package com.asl.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.asl.entity.Zlogs;

@Mapper
public interface ZlogsMapper {
	public long save(Zlogs zlogs);
	public long update(Zlogs zlogs);
	
	public List<Zlogs> getAllZlogs(String zerrorid);
}
