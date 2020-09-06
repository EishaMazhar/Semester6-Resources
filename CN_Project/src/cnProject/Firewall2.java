package cnProject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Firewall2 extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int ServerToFirewall2 = 8025;
	int Firewall2ToServer = 8026;
	int Firewall2ToVictim = 8027;

	String receivedFromServer="";

	DatagramSocket ds;
	
	public void receiving(int number) throws IOException
	{
		for(int i=0;i<number; i++)
		{
			receive(ServerToFirewall2);
			send(Firewall2ToVictim);
		}
	}
	
	public void receive(int receiverSocket) throws IOException
	{
		byte buff[]=new byte[1024];
		DatagramPacket dp;
		ds=new DatagramSocket(receiverSocket);
		dp=new DatagramPacket(buff,buff.length);
		ds.receive(dp);

		receivedFromServer=new String (dp.getData(),0,0,dp.getLength());
		System.out.println("Recieved: "+receivedFromServer);
		ds.close();
	}
	public void send(int senderSocket) throws IOException
	{
		try{
		byte buff[];
		ds=new DatagramSocket();
		DatagramPacket dp;        
		DataInputStream in=new DataInputStream(System.in);
//		System.out.print("Enter the msg:");
		buff=receivedFromServer.getBytes();
		dp=new DatagramPacket(buff,buff.length,InetAddress.getLocalHost(),senderSocket);
		ds.send(dp);
		ds.close();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}

	}

	public static void main(String args[])throws Exception
	{
		Firewall2 re=new Firewall2();
		while(!re.receivedFromServer.equals("exit"))
		{
			re.receive(re.ServerToFirewall2);
			re.send(re.Firewall2ToVictim);
		}
	}
}