package cnProject;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Attacker {
	DatagramSocket ds;
	int AttackerToFirewall1 = 8021;
	int Firewall1ToAttacker = 8022;
	String AttackerIP = "1.2.3.4";
	String domainPrefix="scan.syssec.rub.de";
	int count=0;
	public String headerAttacker()
	{
		
		String headerText = "A? ";
		headerText += "q9ZbKc";
		headerText += String.format("%040x", new BigInteger(1, AttackerIP.getBytes(/*YOUR_CHARSET?*/)));
		headerText += "./ scan.syssec.rub.de";
		if(count++%3 == 0)
		{
			headerText +=  "IP Address of Src= 1.2.3.4, Destination is 10.1.4.3\n";
		}
		else if(count%3 == 1)
		{
			headerText +=  "IP Address of Src= 192.168.0.4, Destination is 10.1.4.3\n";
		}
		else
		{
			headerText +=  "IP Address of Src= 192.168.0.4, Destination is 10.1.4.3\n";
		}
		
		headerText+=	"0000  00 00 00 00 00 00 00 00  00 00 00 00 08 00 45 00 \n" + 
				"0010  00 3c 51 e3 40 00 40 11  ea cb 7f 00 00 01 7f 00\n" +
				"0020  00 01 ec ed 00 35 00 28  fe 3b 24 1a 01 00 00 01\n" +
				"0030  00 00 00 00 00 00 03 77  77 77 06 67 6f 6f 67 6c\n" +
				"0040  65 03 63 6f 6d 00 00 01  00 01";
		
		return headerText;
		
	}
	public void send() throws IOException
	{
		byte buff[];
		String str;
		ds=new DatagramSocket();
		DatagramPacket dp;  
//		DataInputStream in=new DataInputStream(System.in);
		str = headerAttacker();
		buff=str.getBytes();
		dp=new DatagramPacket(buff,buff.length,InetAddress.getLocalHost(),AttackerToFirewall1);
		ds.send(dp);
		System.out.println("Data send: "+ str);
		ds.close();
	}

	public void sendExit() throws IOException
	{
		byte buff[];
		String str;
		ds=new DatagramSocket();
		DatagramPacket dp;  
		DataInputStream in=new DataInputStream(System.in);
		str = "exit";
		buff=str.getBytes();
		dp=new DatagramPacket(buff,buff.length,InetAddress.getLocalHost(),AttackerToFirewall1);
		ds.send(dp);
		ds.close();
	}

	public void receive() throws IOException
	{
		byte buff[]=new byte[1024];
		String str;

		DatagramPacket dp;
		ds=new DatagramSocket(Firewall1ToAttacker);
		dp=new DatagramPacket(buff,buff.length);
		ds.receive(dp);

		str=new String (dp.getData(),0,0,dp.getLength());
		System.out.println("Recieved: "+str);
		ds.close();
	}

	public static void main(String args[])throws IOException
	{
		Attacker se=new Attacker();
		Scanner userInputScanner = new Scanner (System.in);
		char check = '1';

		while(check == '1' )
		{
			se.send();
			se.send();
			se.send();
			se.send();
			se.send();
			se.send();
//			se.receive();
			System.out.println("Press 1 to continue: ");
			check = userInputScanner.next().toString().charAt(0);
		}
		se.sendExit();
		userInputScanner.close();
	}
}