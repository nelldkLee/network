package chat;

import java.net.Socket;

public class ChatServerRunnable implements Runnable{

	private String nickname;
	private Socket socket;

	public ChatServerRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		
	}
	
}
