package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {
	private static String documentRoot = "";
	private Map<String, String> errorMap = new HashMap<String, String>();
	private static final String ERROR_MESSAGE_400 = "400 Bad Request";
	private static final String ERROR_MESSAGE_404 = "404 File Not Found";

	static {
		try {
			documentRoot = new File(RequestHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
			documentRoot += "/webapp";
			System.out.println("--------->" + documentRoot);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Socket socket;

	public RequestHandler(Socket socket) {
		this.socket = socket;
		errorMap.put("400", ERROR_MESSAGE_400);
		errorMap.put("404", ERROR_MESSAGE_404);
	}

	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			consoleLog("connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort());

			// get IOStream
			OutputStream os = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

			String request = null;

			while (true) {
				String line = br.readLine();

				// 브라우저가 연결을 끊으면...
				if (line == null) {
					break;
				}

				// Request Header만 읽음
				if ("".equals(line)) {
					break;
				}

				// Header의 첫번째 라인만 처리
				if (request == null) {
					request = line;
				}
			}

			String[] tokens = request.split(" ");
			String method = tokens[0];
			String url = tokens[1];
			String protocol = tokens[2];
			
			if ("GET".contentEquals(method)) {
				consoleLog("request:" + url);
				responseStaticResource(os, url, protocol);
			} else { // POST, PUT, DELETE, HEAD, CONNECT 와 같은 Method는 무시
				responseError(os, protocol,"400");
				consoleLog("Bad Request:" + request);
			}

		} catch (Exception ex) {
			consoleLog("error:" + ex);
		} finally {
			// clean-up
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}

			} catch (IOException ex) {
				consoleLog("error:" + ex);
			}
		}
	}

	

	public void responseStaticResource(OutputStream os, String url, String protocol) throws IOException {

		if ("/".equals(url)) {
			url = "/index.html";
		}

		File file = new File(documentRoot + url);
		if (file.exists() == false) {
			responseError(os, protocol,"404");
			return;
		}
		// nio
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());

		// 응답
		os.write((protocol + " 200 OK\r\n").getBytes("UTF-8"));
		os.write(("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes("UTF-8"));
		os.write("\r\n".getBytes());
		os.write(body);
	}
	//response404,400 메소드를 리팩토링.
	private void responseError(OutputStream os, String protocol,String errorType) throws IOException {

		File file = new File(documentRoot + "/error/" + errorType + ".html");
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());

		os.write((protocol + errorMap.get(errorType) + "\r\n").getBytes("UTF-8"));
		os.write(("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes("UTF-8"));
		os.write("\r\n".getBytes());
		os.write(body);
	}
	/*
	 * responseError로 리팩토링
	private void response404Error(OutputStream os, String protocol) throws IOException {

		File file = new File(documentRoot + "/error/404.html");
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());

		os.write((protocol + " 404 File Not Found\r\n").getBytes("UTF-8"));
		os.write(("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes("UTF-8"));
		os.write("\r\n".getBytes());
		os.write(body);
	}
	*/
	public void consoleLog(String message) {
		System.out.println("[RequestHandler#" + getId() + "] " + message);
	}
}