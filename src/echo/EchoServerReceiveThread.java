package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;

	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
		String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
		int remotePort = inetRemoteSocketAddress.getPort();
		
		EchoServer.log("connected by client[" + remoteHostAddress + ":" + remotePort + "]");
		
		try {
			// 4. IOStream 받아오기 
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			PrintWriter pr = new PrintWriter(new OutputStreamWriter(os, "utf-8"), true);
			
			while(true) {
				// 5. 데이터 읽기
				String data = br.readLine();
				
				if(data == null) {
					EchoServer.log("closed by client");
					break;
				}
				
				EchoServer.log("received" + data);
				
				//6 . 데이터 쓰기
				pr.println(data);
			}
			
		} catch (SocketException e) {
			EchoServer.log("sudden closed");
				
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if(socket != null && socket.isClosed()) {
				try {
					socket.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}
	}
	
	
}
