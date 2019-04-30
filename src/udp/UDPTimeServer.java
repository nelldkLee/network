package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UDPTimeServer {

	public static final int PORT = 8000;
	public static final int BUFFER_SIZE = 1024;
	
	public static void main(String[] args) {
		DatagramSocket socket = null;
		
		try {
			socket = new DatagramSocket(PORT);
			
			while (true) {
				// 2. 데이터 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket);

				byte[] data = receivePacket.getData();
				int length = receivePacket.getLength();
				String message = new String(data, 0, length, "utf-8");

				System.out.println("[server] received:" + message);
				if("\"\"".equals(message)) {
					showTime(socket, receivePacket);
				}else {
					byte[] sendData = message.getBytes("utf-8");
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),
							receivePacket.getPort());
					socket.send(sendPacket);
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}

	private static void showTime(DatagramSocket socket, DatagramPacket receivePacket) throws IOException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");

		String data = format.format(cal.getTime());
		byte[] sendData = data.getBytes("utf-8");
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),
				receivePacket.getPort());
		socket.send(sendPacket);
	}

}