import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Out extends Thread {

	public static  void you_name() throws IOException {

		DatagramSocket Socket = new DatagramSocket(4343);

		byte[] send_name = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(send_name, send_name.length);
		Socket.receive(receivePacket);
		System.out.println(receivePacket.getAddress());
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println(modifiedSentence);

	}

	public static void main(String[] args) {
		new Runnable() {
			public void run() {
				System.out.println("Запуск");
				try {
					while (true) {
						you_name();
					}
				} catch (IOException e) {	
					e.printStackTrace();
				}
			}
		}.run();
	}
}
