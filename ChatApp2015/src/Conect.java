import java.io.*;
import java.net.*;

public class Conect {
	static String modifiedSentence;

	private static void Conect() throws IOException {

	}

	public static void name(String name, String IP) throws IOException {
		DatagramSocket Socket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(IP);
		byte[] send_name = new byte[name.length()];
		send_name = name.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(send_name, send_name.length, IPAddress, 5555);
		Socket.send(sendPacket);
		System.out.println("Send");
		Socket.close();
	}

	public static void sms(String sms, String IP) throws IOException {
		DatagramSocket Socket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(IP);
		byte[] send_name = new byte[sms.length()];
		send_name = sms.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(send_name, send_name.length, IPAddress, 5555);
		Socket.send(sendPacket);
		System.out.println("Отпарвило");
		Socket.close();
	}

	public static void you_name() throws IOException {

		while (true) {
			DatagramSocket Socket = new DatagramSocket();
			byte[] send_name = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(send_name, send_name.length);
			Socket.receive(receivePacket);
			System.out.println(receivePacket.getAddress());
			modifiedSentence = new String(receivePacket.getData());
			System.out.println("Пришло собшенея");
		}

	}
}
