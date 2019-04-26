package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try {
			checkPublicIpAddress(sc);
		} catch (UnknownHostException e) {
			System.out.println("잘못된 주소입니다. 다시입력해주세요.");
			main(null);
		}
	}
	
	public static void checkPublicIpAddress(Scanner sc) throws UnknownHostException{
		while(sc.hasNext()) {
			String line = sc.nextLine();
			
			if("exit".equals(line)){
				break;
			}
			InetAddress[] inetAddresses = InetAddress.getAllByName(line);
			
			for(InetAddress addr : inetAddresses) {
				System.out.println(addr.getHostAddress());
			}	
		}
	}
}
