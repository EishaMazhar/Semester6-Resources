package filterIpSpoofing;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import cnProject.Attacker;

public class DnsScanner {

	DatagramSocket ds;
	public static final int DnsScanner2DnsProxy = 9000;
	public static final int DnsProxy2DnsScanner = 9001;
	public static final int DnsResolver2DnsScanner = 9004;
	public static final int DnsScanner2DnsResolver = 9005;

	public static final String ScannerIP = "7.7.7.7";
	public static final String DnsProxyIP ="1.2.3.4";
	public static final String DnsResolverIP = "8.8.8.8";
	
	public String receivedData="";

	String domainPrefix="scan.syssec.rub.de";
	int count=0;

	public void checkData()
	{
		String headerText = String.format("%040x", new BigInteger(1, DnsProxyIP.getBytes(/*YOUR_CHARSET?*/)));
		System.out.println("IP address we got is" +  headerText);
		System.out.println("Hex to Char = " + DnsProxyIP);
		System.out.println("IP address we got is "+ DnsResolverIP);
		System.out.println("This mismatch proves that This network is Vulnerable");
	}
	
	public String headerScanner()
	{

		String headerText = "A? ";
		headerText += "q9ZbKc";
		headerText += String.format("%040x", new BigInteger(1, DnsProxyIP.getBytes(/*YOUR_CHARSET?*/)));
		headerText += "./ scan.syssec.rub.de";

		headerText +=  "IP Address of Src= " + ScannerIP + ", Destination is " + DnsProxyIP + "\n";

//		headerText+=	"0000  00 00 00 00 00 00 00 00  00 00 00 00 08 00 45 00 \n" + 
//				"0010  00 3c 51 e3 40 00 40 11  ea cb 7f 00 00 01 7f 00\n" +
//				"0020  00 01 ec ed 00 35 00 28  fe 3b 24 1a 01 00 00 01\n" +
//				"0030  00 00 00 00 00 00 03 77  77 77 06 67 6f 6f 67 6c\n" +
//				"0040  65 03 63 6f 6d 00 00 01  00 01";

		return headerText;

	}

	public void send(int sendingIP) throws IOException
	{
		byte buff[];
		String str;
		ds=new DatagramSocket();
		DatagramPacket dp;  
		//		DataInputStream in=new DataInputStream(System.in);
		str = headerScanner();
		buff=str.getBytes();
		dp=new DatagramPacket(buff,buff.length,InetAddress.getLocalHost(),sendingIP);
		ds.send(dp);
		System.out.println("Data send: "+ str);
		ds.close();
	}

	public void sendExit(int sendingIP) throws IOException
	{
		byte buff[];
		String str;
		ds=new DatagramSocket();
		DatagramPacket dp;  
		DataInputStream in=new DataInputStream(System.in);
		str = "exit";
		buff=str.getBytes();
		dp=new DatagramPacket(buff,buff.length,InetAddress.getLocalHost(),sendingIP);
		ds.send(dp);
		ds.close();
	}

	public void receive(int receivingIP) throws IOException
	{
		byte buff[]=new byte[1024];
		DatagramPacket dp;
		ds=new DatagramSocket(receivingIP);
		
		dp=new DatagramPacket(buff,buff.length);
		System.out.println("here 0");
		ds.receive(dp);
		System.out.println("here1");
		receivedData=new String (dp.getData(),0,0,dp.getLength());
		System.out.println("Recieved: "+receivedData);
		ds.close();
		checkData();
	}

	public static void main(String args[])throws IOException
	{
		DnsScanner se=new DnsScanner();
		Scanner userInputScanner = new Scanner (System.in);
		char check = '1';

		while(check == '1')
		{
			se.send(DnsScanner.DnsScanner2DnsProxy);
			try{
				System.out.println("in DnsResolver2DnsScanner");
			se.receive(DnsScanner.DnsResolver2DnsScanner);
			}
			catch(Exception ex)
			{
				System.out.println("receiving error: ");
				System.out.print(ex.getMessage());
			}
			System.out.println("Press 1 to continue: ");
			check = userInputScanner.next().toString().charAt(0);
			
		}
		se.sendExit(DnsScanner.DnsScanner2DnsProxy);
		
		userInputScanner.close();
	}
}
