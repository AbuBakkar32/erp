package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ASLProcError;

/**
 * @author Zubayer Ahamed
 * @since Apr 26, 2021
 */
@Mapper
public interface ASLProcErrorMapper {

	List<ASLProcError> getAllProcErrors(String zid);
}
