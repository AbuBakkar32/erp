package com.asl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.asl.entity.Zbusiness;
import com.asl.service.ZbusinessService;

@SpringBootTest
public class ZbusinessTest {

	@Autowired private ZbusinessService zbusinessService;

	@Test
	void testZbusinessSave() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		System.out.println(sdf.format(new Date()).toUpperCase());
		
//		Zbusiness z = getBranch2();
//		long count = zbusinessService.save(z);
//		System.out.println(count);
	}

	@Test
	void testZbusinessUpdate() {
		Zbusiness z = getBranch1();
		long count = zbusinessService.update(z);
		System.out.println(count);
	}

	private Zbusiness getCentral() {
		Zbusiness z = new Zbusiness();
		z.setZid("900000");
		z.setZorg("Khana's Central");
		z.setCentral(true);
		z.setBranch(false);
		z.setZactive(true);
		return z;
	}
	
	

	private Zbusiness getBranch1() {
		Zbusiness z = new Zbusiness();
		z.setZid("900010");
		z.setZorg("Dhanmondi Branch");
		z.setZactive(true);
		z.setCentral(false);
		z.setBranch(true);
		z.setCentralzid("900000");
		return z;
	}

	private Zbusiness getBranch2() {
		Zbusiness z = new Zbusiness();
		z.setZid("900020");
		z.setZorg("Mohammodpur Brnach");
		z.setZactive(true);
		z.setCentral(false);
		z.setBranch(true);
		z.setCentralzid("900000");
		return z;
	}
}
