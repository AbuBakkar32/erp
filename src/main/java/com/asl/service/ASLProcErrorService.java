package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.ASLProcError;

/**
 * @author Zubayer Ahamed
 * @since Apr 26, 2021
 */
@Component
public interface ASLProcErrorService {

	List<ASLProcError> getAllProcErrors();
}
