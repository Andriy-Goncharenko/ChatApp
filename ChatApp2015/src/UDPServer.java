import java.io.*;
import java.net.*;
import java.sql.Date;

import org.omg.CORBA.PUBLIC_MEMBER;

class UDPServer {
	private static DatagramSocket serverSocket;

	public static void main(String args[]) throws Exception {
		new Runnable() {
			public void run() {
				
			
		serverSocket = new DatagramSocket(9844);
		byte[] receiveData = new byte[10];
		byte[] sendData = new byte[256];
		System.out.println("Сервак запущен");
			DatagramPacket receivePacket = new DatagramPacket(receiveData, 10);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			System.out.println("На сервер пришло: " + sentence);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String capitalizedSentence = sentence.toString();
			sendData = capitalizedSentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			try {
				serverSocket.send(sendPacket);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}
}