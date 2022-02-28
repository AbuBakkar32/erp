package com.asl;

import org.junit.jupiter.api.Test;

/**
 * @author Zubayer Ahamed
 * @since Jan 19, 2022
 */
public class PaginationTest {

	
	@Test
	public void pagingTest() {

		int perPageDataLimit = 10;
		int numberOfData = 1000;
		int totalPage = numberOfData / perPageDataLimit;
		String next = "Next";
		String prev = "Prev";
		String dotprint = "...";
		int selectedPage = 100;
		boolean prevDotPrint = true;
		boolean nextDotPrint = true;

		for(int i = 1; i <= totalPage; i++) {

			if(i == 1) System.out.print(" | " + prev + " | ");

			if(totalPage <= 5) {
				if(selectedPage == i) {
					System.out.print(" | {" + i + "} | ");
				} else {
					System.out.print(" | " + i + " | ");
				}
			} else {

				if(i == 1 && selectedPage != i) System.out.print(" | " + i + " | ");
				
				if((selectedPage - 1 ) - 1 > 1 && prevDotPrint) {
					System.out.print(" | " + dotprint + " | ");
					prevDotPrint = false;
				}
				
				if(selectedPage - 1 == i && (selectedPage - 1) != 1) {
					System.out.print(" | " + i + " | ");
				}
				if(selectedPage == i) {
					System.out.print(" | {" + i + "} | ");
				}
				if(selectedPage + 1 == i && (selectedPage + 1) != totalPage) {
					System.out.print(" | " + i + " | ");
				}
				
				if(i > (selectedPage + 1) && (selectedPage + 1 ) + 1 < totalPage && nextDotPrint) {
					System.out.print(" | " + dotprint + " | ");
					nextDotPrint = false;
				}
				
				if(i == totalPage && selectedPage != i) System.out.print(" | " + i + " | ");

			}

			if(i == totalPage) System.out.print(" | " + next + " | ");
		}

	}
}
