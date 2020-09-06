package cnProject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {

	int bytesReceived=0;
	int bytesSent=0;
	
	DatagramSocket ds;
	int Firewall1ToServer = 8023;
	int ServerToFirewall1 = 8024;
	int ServerToFirewall2 = 8025;
	int Firewall2ToServer = 8026;
	String receivedFromFirewall1="";
	int count=0;
	
	public String headerServer()
	{
		String headerText = "";
		if(count++%3 == 0)
		{
			headerText =  "IP Address of Src= 192.168.0.4, Destination is 10.1.4.3 and AttackerSrc = 172.16.5.2\n";
		}
		else if(count%3 == 1)
		{
			headerText =  "IP Address of Src= 192.168.0.4, Destination is 10.1.4.3 and AttackerSrc = 188.19.5.3\n";
		}
		else
		{
			headerText =  "IP Address of Src= 192.168.0.4, Destination is 10.1.4.3 and AttackerSrc = 162.17.4.4\n";
		}
		
		headerText +="0000  00 00 00 00 00 00 00 00  00 00 00 00 08 00 45 00\n" +
				"0010  00 7a 00 00 40 00 40 11  3c 71 7f 00 00 01 7f 00\n" +
				"0020  00 01 00 35 ec ed 00 66  fe 79 24 1a 81 80 00 01\n" +
				"0030  00 03 00 00 00 00 03 77  77 77 06 67 6f 6f 67 6c\n" +
				"0040  65 03 63 6f 6d 00 00 01  00 01 c0 0c 00 05 00 01\n" +
 				"0050  00 05 28 39 00 12 03 77  77 77 01 6c 06 67 6f 6f\n" +
				"0060  67 6c 65 03 63 6f 6d 00  c0 2c 00 01 00 01 00 00\n" +
				"0070  00 e3 00 04 42 f9 59 63  c0 2c 00 01 00 01 00 00\n" +
				"0080  00 e3 00 04 42 f9 59 68  c0 2c 00 01 00 01 00 00\n" +
				"0090  00 03 00 00 00 00 03 77  77 77 06 67 6f 6f 67 6c\n" +
				"0100  65 03 63 6f 6d 00 00 01  00 01 c0 0c 00 05 00 01\n" +
				"0110  00 05 28 39 00 12 03 77  77 77 01 6c 06 67 6f 6f\n" +
				"0120  67 6c 65 03 63 6f 6d 00  c0 2c 00 01 00 01 00 00\n" +
				"0130  00 e3 00 04 42 f9 59 63  c0 2c 00 01 00 01 00 00\n" +
				"0140  00 e3 00 04 42 f9 59 68  c0 2c 00 01 00 01 00 00\n" +
				"0150  00 03 00 00 00 00 03 77  77 77 06 67 6f 6f 67 6c\n" +
				"0160  65 03 63 6f 6d 00 00 01  00 01 c0 0c 00 05 00 01\n" +
				"0170  00 05 28 39 00 12 03 77  77 77 01 6c 06 67 6f 6f\n" +
				"0180  67 6c 65 03 63 6f 6d 00  c0 2c 00 01 00 01 00 00\n" +
				"0190  00 e3 00 04 42 f9 59 63  c0 2c 00 01 00 01 00 00\n" +
				"0200  00 e3 00 04 42 f9 59 68  c0 2c 00 01 00 01 00 00\n" +
				"0210  00 03 00 00 00 00 03 77  77 77 06 67 6f 6f 67 6c\n" +
				"0220  65 03 63 6f 6d 00 00 01  00 01 c0 0c 00 05 00 01\n" +
				"0230  00 05 28 39 00 12 03 77  77 77 01 6c 06 67 6f 6f\n" +
				"0240  67 6c 65 03 63 6f 6d 00  c0 2c 00 01 00 01 00 00\n" +
				"0250  00 e3 00 04 42 f9 59 63  c0 2c 00 01 00 01 00 00\n" +
				"0260  00 e3 00 04 42 f9 59 68  c0 2c 00 01 00 01 00 00\n";
				
		return headerText;
	}
	
	public void sending() throws IOException{
		send(ServerToFirewall2);
		send(ServerToFirewall2);
		send(ServerToFirewall2);
		send(ServerToFirewall2);
		send(ServerToFirewall2);
		send(ServerToFirewall2);
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
		dp=new DatagramPacket(buff,buff.length,InetAddress.getLocalHost(),ServerToFirewall2);
		ds.send(dp);
		ds.close();
	}
	
	public void receive(int receiverSocket) throws IOException
	{
		byte buff[]=new byte[1024];
		DatagramPacket dp;
		ds=new DatagramSocket(receiverSocket);
		dp=new DatagramPacket(buff,buff.length);
		ds.receive(dp);
		
		receivedFromFirewall1=new String (dp.getData(),0,0,dp.getLength());
		
		if(receivedFromFirewall1.equals("exit"))
		{
			sendExit();
		}
		else
		{
			bytesReceived+=receivedFromFirewall1.length();
		}
		System.out.println("Recieved: "+receivedFromFirewall1);
		ds.close();
	}
	public void send(int senderSocket) throws IOException
	{
		byte buff[];
		ds=new DatagramSocket();
		DatagramPacket dp;        
		DataInputStream in=new DataInputStream(System.in);
		String str = headerServer();
		bytesSent+=str.length();
		buff=str.getBytes();
		dp=new DatagramPacket(buff,buff.length,InetAddress.getLocalHost(),senderSocket);
		ds.send(dp);
		ds.close();

	}

	public static void main(String args[])throws Exception
	{
		Server re=new Server();
		while(!re.receivedFromFirewall1.equals("exit"))
		{
			re.receive(re.Firewall1ToServer);
			
			re.send(re.ServerToFirewall2);
			re.send(re.ServerToFirewall2);
			re.send(re.ServerToFirewall2);
			re.send(re.ServerToFirewall2);
			re.send(re.ServerToFirewall2);
			re.send(re.ServerToFirewall2);
			
			System.out.println("Bytes Received = "+ re.bytesReceived);
			System.out.println("Bytes Sent = " + re.bytesSent);
		}
	}
}