package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class KeyboardTest {
	
	public static void main(String[] args) {
		//기반스트림(표준입력, 키보드, system.in)
		BufferedReader br = null;
		
		
		try {
			//보조스트림
			InputStreamReader isr = new InputStreamReader(System.in, "utf-8");

			//보조스트림2
			br = new BufferedReader(isr);
			
			String line = null;
			while((line = br.readLine()) != null) {
				if("exit".equals(line)) {
					break;
				}
				System.out.println(">>" + line);
			}
			
		} catch (IOException e) {
			System.out.println("error: +  " + e);
			
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
}
