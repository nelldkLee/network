package test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Localhost {
	
	public static void main(String[] args) {
		try {
			
			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostName = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			System.out.println(hostName + " : " + hostAddress);
			
			byte[] addresses = inetAddress.getAddress();
			
			System.out.println(Arrays.toString(addresses));
			for( byte address : addresses) {
				System.out.println(address & 0x00000000ff);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
