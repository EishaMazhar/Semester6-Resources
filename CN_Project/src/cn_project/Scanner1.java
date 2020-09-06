package cn_project;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Scanner1 extends Thread {

	public static void main(String args[]) throws IOException {
		String ip = "192.168.10.2";
		int port = 8000;
		// new Thread(new User()).start();
		int count = 0;
		System.out.println("\n\n\t\t\t\t\t---------");
		System.out.println("\t\t\t\t\t Scanner");
		System.out.println("\t\t\t\t\t---------\n");
		System.out.println("\n IP : 192.168.10.2\n Port : 8000\t(With Test System 1)\n\n");
		Socket s = new Socket("localHost", 8000);
		ServerSocket s3 = new ServerSocket(8005);
		Socket ss3 = s3.accept();
		DataInputStream in2 = new DataInputStream(ss3.getInputStream());
		PrintStream dos3 = new PrintStream(ss3.getOutputStream());
		PrintStream dos = new PrintStream(s.getOutputStream());
		DataInputStream in = new DataInputStream(s.getInputStream());
		DataInputStream in1 = new DataInputStream(System.in);
		while (true) {
			String str;
			// send
			if (count == 0) {
				str = "SYN";
				dos3.println(str);
				System.out.println("SYN Bit is send.");
				String str1 = in.readLine();
				System.out.println("SYNACK: " + str1);
				System.out.println("Enter SYNACK number to complete handshake...");
				dos.println(in1.readLine());
				System.out.println("System work done");
				count++;
				
			} else {
				System.out.print(" Enter Message : ");
				str = in1.readLine();
				if(!str.equals("end"))
				{
					String hexPort =  Integer.toHexString(port);
					String str1 = "At Scanner: A? "+hexPort+"rub.de"+" Src Port: 8000 Dst Port: 8005\n" + str;
					System.out.println(str1 + "Sent");
					dos.println(hexPort);
				}
				dos.println(str);
				System.out.println(" Message Sent... ");
				
			if (str.equals("end")) {
				s.close();
				s3.close();
				break;
			}
			// recieve
			str = in.readLine();
			if (str.length() != 0) {
				System.out.println(" Message Received : " + str);
			}
			System.out.println("");
		}
		System.out.println("\n");
	}
	}

	private static void toHexString(int port) {
		// TODO Auto-generated method stub
	}
}