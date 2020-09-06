package cnProject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Firewall1 {

	int AttackerToFirewall1 = 8021;
	int Firewall1ToAttacker = 8022;
	int Firewall1ToServer = 8023;
	int ServerToFirewall1 = 8024;
	String recievedFromAttacker = "";
	DatagramSocket ds;
	public void receive(int receiverSocket) throws IOException
	{
		byte buff[]=new byte[1024];
		DatagramPacket dp;
		ds=new DatagramSocket(receiverSocket);
		dp=new DatagramPacket(buff,buff.length);
		ds.receive(dp);

		recievedFromAttacker=new String (dp.getData(),0,0,dp.getLength());
		System.out.println("Recieved: "+recievedFromAttacker);
		ds.close();
	}
	public void send(int senderSocket) throws IOException
	{
		byte buff[];
		ds=new DatagramSocket();
		DatagramPacket dp;        
		DataInputStream in=new DataInputStream(System.in);
//		System.out.print("Enter the msg:");
		buff=recievedFromAttacker.getBytes();
		dp=new DatagramPacket(buff,buff.length,InetAddress.getLocalHost(),senderSocket);
		ds.send(dp);
		ds.close();

	}

	public static void main(String args[])throws Exception
	{
		Firewall1 re=new Firewall1();
		while(!re.recievedFromAttacker.equals("exit"))
		{
			
			re.receive(re.AttackerToFirewall1);
			System.out.println(re.recievedFromAttacker);
//			re.send(re.Firewall1ToAttacker);
			re.send(re.Firewall1ToServer);
//			System.out.println("Received command is: " + re.recievedFromAttacker);
			
		}
//		System.out.println("outside loop");
	}
}