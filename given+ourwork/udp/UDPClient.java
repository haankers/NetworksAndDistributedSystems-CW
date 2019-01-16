/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.io.*;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;
	
	public static void main(String[] args) {
		InetAddress	serverAddr = null;
		//Inetaddress class representing an IP address
		int		recvPort;
		int 		countTo;
		String 		message;

		// Get the parameters
		if (args.length != 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		//gets host's ip address given the host name
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);
		//parseInt--string to integer variable

		// TO-DO: Construct UDP client class and try to send messages
		UDPClient client = new UDPClient();
		//UDP client is a class and initialised here
		System.out.println("Attempting to send some messages");
		client.testLoop(serverAddr, recvPort, countTo);
	}
	
	public UDPClient() {
		// TO-DO: Initialise the UDP socket for sending data
		try {
			sendSoc = new DatagramSocket();

		} catch (SocketException e) {
			System.out.println("Socket error");
}
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
		int				tries = 0;

		// TO-DO: Send the messages to the server
		MessageInfo mess;
		ByteArrayOutputStream byteStream;
		ObjectOutputStream oos;

		for(int i = 0; i < countTo; i++) {
			mess = new MessageInfo(countTo,i);
			
			//thought 2000 would be enough
			byteStream = new ByteArrayOutputStream(60000);
			try {
				oos = new ObjectOutputStream(new BufferedOutputStream(byteStream));
				oos.writeObject(mess);
				oos.flush();
			} catch (Exception e) {
				System.out.println("Error doing the client stream");
				System.exit(-1);
			}
			
			byte[] sendByteArr = byteStream.toByteArray();    
			send(sendByteArr, serverAddr, recvPort);	
		}
	}

	private void send(byte[] data, InetAddress destAddr, int destPort) {
		DatagramPacket		pkt;

		// TO-DO: build the datagram packet and send it to the server
		pkt = new DatagramPacket(data, data.length, destAddr, destPort);
		try {
			sendSoc.send(pkt);
		} catch (IOException e) {
			System.out.println("Error sending");
			System.exit(-1);
}
	}
}
