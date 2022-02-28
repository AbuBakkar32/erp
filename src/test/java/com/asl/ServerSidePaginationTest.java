package com.asl;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.asl.entity.Caitem;
import com.asl.service.CaitemService;

/**
 * @author Zubayer Ahamed
 * @since Feb 7, 2022
 */
@SpringBootTest
public class ServerSidePaginationTest {

	@Autowired private CaitemService caitemService;

	
	@Test
	public void serverSidePagination() {
		
		List<Caitem> items = caitemService.getAllCaitems();
		
		items.stream().forEach(i -> {
			System.out.println(i.toString());
		});
		
	}
}
