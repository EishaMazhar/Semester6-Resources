package filterIpSpoofing;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DnsProxy {

	public static final int DnsScanner2DnsProxy = 9000;
	public static final int DnsProxy2DnsScanner = 9001;
	public static final int DnsProxy2DnsResolver = 9002;
	public static final int DnsResolver2DnsProxy = 9003;

	public static final String ScannerIP = "7.7.7.7";
	public static final String DnsProxyIP ="1.2.3.4";
	public static final String DnsResolverIP = "8.8.8.8";

	String receivedData="";

	DatagramSocket ds;

	public String headerProxy()
	{

		String headerText = "A? ";
		headerText += "q9ZbKc";
		headerText += String.format("%040x", new BigInteger(1, DnsProxyIP.getBytes(/*YOUR_CHARSET?*/)));
		headerText += "./ scan.syssec.rub.de";

		headerText +=  "IP Address of Src= " + ScannerIP + ", Destination is " + DnsResolverIP + "\n";

		//		headerText+=	"0000  00 00 00 00 00 00 00 00  00 00 00 00 08 00 45 00 \n" + 
		//				"0010  00 3c 51 e3 40 00 40 11  ea cb 7f 00 00 01 7f 00\n" +
		//				"0020  00 01 ec ed 00 35 00 28  fe 3b 24 1a 01 00 00 01\n" +
		//				"0030  00 00 00 00 00 00 03 77  77 77 06 67 6f 6f 67 6c\n" +
		//				"0040  65 03 63 6f 6d 00 00 01  00 01";

		return headerText;

	}

	public void receive(int receiverSocket) throws IOException
	{
		byte buff[]=new byte[1024];
		DatagramPacket dp;
		ds=new DatagramSocket(receiverSocket);
		dp=new DatagramPacket(buff,buff.length);
		ds.receive(dp);

		receivedData=new String (dp.getData(),0,0,dp.getLength());
		System.out.println("Recieved: "+receivedData);
		ds.close();
	}
	public void send(int senderSocket) throws IOException
	{
		try{
			byte buff[];
			ds=new DatagramSocket();
			DatagramPacket dp;        
			DataInputStream in=new DataInputStream(System.in);
			System.out.println("Sent: "  + headerProxy());
			buff=headerProxy().getBytes();
			dp=new DatagramPacket(buff,buff.length,InetAddress.getLocalHost(),senderSocket);
			ds.send(dp);
			ds.close();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}

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
	public static void main(String args[])throws Exception
	{
		DnsProxy re=new DnsProxy();
		while(!re.receivedData.equals("exit"))
		{
			re.receive(DnsProxy.DnsScanner2DnsProxy);
			if(!re.receivedData.equals("exit"))
				re.send(DnsProxy.DnsProxy2DnsResolver);
			else
				re.sendExit(DnsProxy.DnsProxy2DnsResolver);
		}
		
	}
}