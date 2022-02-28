package com.asl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2021
 */
public class UtilTest {

	@Test
	void someTest() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 0);
		System.out.println(cal.getTime());
		
		System.out.println(cal.get(Calendar.DAY_OF_MONTH));
		System.out.println(cal.get(Calendar.MONTH));
		
	}
}
