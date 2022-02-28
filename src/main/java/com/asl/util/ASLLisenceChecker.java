package com.asl.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Zubayer Ahamed
 * @since Jan 6, 2021
 */
@Service
public class ASLLisenceChecker {

	@Autowired private static ApplicationContext appContext;

	public static void checkLisence() {
		SpringApplication.exit(appContext, () -> 0);
	}
}
