package com.asl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.asl.entity.Cacus;
import com.asl.entity.PoordHeader;
import com.asl.entity.UserAuditRecord;
import com.asl.entity.Xusers;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CacusService;
import com.asl.service.PoordService;
import com.asl.service.UserAuditRecordService;
import com.asl.service.XusersService;

@SpringBootTest
class AslErpApplicationTests {

	@Autowired private XusersService userService;
	@Autowired private UserAuditRecordService userAuditRecordService;
	@Autowired private CacusService cacusService;
	@Autowired private PoordService poordService;

	@Test
	void saveUserAuditRecord() {
		UserAuditRecord aur = new UserAuditRecord();
		aur.setUserId("admin");
		aur.setBusinessId("D900010");
		aur.setRecordDate(new Date());
		long count = userAuditRecordService.save(aur);
		System.out.println(count);
	}

	@Test
	void saveUser() {
		Xusers user = new Xusers();
		user.setZemail("admin");
		user.setZactive(Boolean.TRUE);
		user.setZid("900010");
		user.setXpassword("1234");
		user.setSystemadmin(true);
		long count = userService.save(user);
		System.out.println(count);
	}

	@Test
	void findByUsername() {
		List<Xusers> list = userService.findAllUserByZemail("admin");
		list.stream().forEach(l -> {
			System.out.println(l.toString());
			System.out.println(l.isSystemadmin());
		});
	}

	@Test
	void update() {
		List<Xusers> list = userService.findAllUserByZemail("admin");
		Xusers user = list.get(0);
		user.setXpassword("admin");
		long count = userService.update(user);
		System.out.println(count);
	}

	@Test
	void saveCacus() {
		Cacus cacus = new Cacus();
		cacus.setXcustype("SUP-");
		cacus.setXtype(TransactionCodeType.SUPPLIER_NUMBER.getCode());
		cacus.setXcrlimit(BigDecimal.ZERO);
		long count = cacusService.save(cacus);
		System.out.println(count);
	}

	@Test
	void savePoordHeader() {
		PoordHeader ph = new PoordHeader();
		ph.setXtype(TransactionCodeType.PURCHASE_ORDER.getCode());
		ph.setXtrn("PO-");
		
		long count = poordService.save(ph);
		System.out.println(count);
	}

}
