package com.asl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author Zubayer Ahamed
 * @since Dec 31, 2020
 */
public class GetMac {

//	public static void main(String[] args) throws UnknownHostException, SocketException {
//
//		InetAddress address = InetAddress.getLocalHost();
//
//		System.out.println(address);
//
//		NetworkInterface nif = NetworkInterface.getByInetAddress(address);
//		System.out.println(nif);
//
//		byte[] mac = nif.getHardwareAddress();
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < mac.length; i++) {
//			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
//		}
//		System.out.println(sb.toString());
//
//		try {
//			final String[][] commands = { { "CMD", "/C", "WMIC OS GET Installdate,SerialNumber" },
//					{ "CMD", "/C", "WMIC BASEBOARD GET SerialNumber" }, { "CMD", "/C", "WMIC CPU GET ProcessorId" },
//					{ "CMD", "/C", "WMIC COMPUTERSYSTEM GET Name" } };
//			for (int i = 0; i < commands.length; ++i) {
//				final String[] com = commands[i];
//				final Process process = Runtime.getRuntime().exec(com);
//				process.getOutputStream().close();
//				System.out.println();
//				String s = null;
//				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//				while ((s = reader.readLine()) != null) {
//					System.out.print(s);
//				}
//				reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//				while ((s = reader.readLine()) != null) {
//					System.out.print(s);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
