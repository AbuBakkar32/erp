package com.asl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.asl.entity.Xusers;
import com.asl.service.XusersService;

@SpringBootTest
public class XusersTest {

	@Autowired private XusersService xusersService;

	@Test
	void testXusersSave() {
		Xusers user = getCentralUser();
		long count = xusersService.save(user);
		System.out.println(count);
	}

	private Xusers getCentralUser() {
		Xusers user = new Xusers();
		user.setZemail("admin");
		user.setZactive(Boolean.TRUE);
		user.setZid("900010");
		user.setXpassword("1234");
		user.setSystemadmin(true);
		return user;
	}

//	insert into xusers (zemail, zid, xpassword, zactive, systemadmin) values ('admin', '900000', '1234','1','1');
//	insert into xusers (zemail, zid, xpassword, zactive, systemadmin) values ('admin', '900010', '1234','1','1');
//	insert into xusers (zemail, zid, xpassword, zactive, systemadmin) values ('admin', '900020', '1234','1','1');
//	commit;
}
