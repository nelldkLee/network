package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NSLookupServer {
	
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(5000);
			Socket socket = serverSocket.accept();
			
			try {
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				while(true) {
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer);
					if(readByteCount == -1) {
						System.out.println("[server] closed by client");
						break;
					}
					String data = new String(buffer, 0, readByteCount, "utf-8");
					System.out.println("[server] received:" + data);
					
					// 6. 데이터 쓰기
					os.write(data.getBytes("utf-8"));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
