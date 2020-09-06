package cnProject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Victim {


	DatagramSocket ds;
	int Firewall2ToVictim = 8027;
	String receivedFromFirewall2="";
	
	public void receiving(int number) throws IOException
	{
		for(int i=0;i<number; i++)
		{
			receive(Firewall2ToVictim);
		}
	}
	
	public void receive(int receiverSocket) throws IOException
	{
		byte buff[]=new byte[1024];
		DatagramPacket dp;
		ds=new DatagramSocket(receiverSocket);
		dp=new DatagramPacket(buff,buff.length);
		ds.receive(dp);

		receivedFromFirewall2=new String (dp.getData(),0,0,dp.getLength());
		System.out.println("Recieved: "+receivedFromFirewall2);
		ds.close();
	}
	public void send(int senderSocket) throws IOException
	{
		byte buff[];
		String str;
		ds=new DatagramSocket();
		DatagramPacket dp;        
		DataInputStream in=new DataInputStream(System.in);
		buff=receivedFromFirewall2.getBytes();
		dp=new DatagramPacket(buff,buff.length,InetAddress.getLocalHost(),senderSocket);
		ds.send(dp);
		ds.close();
	}

	public static void main(String args[])throws Exception
	{
		Victim re=new Victim();
		while(!re.receivedFromFirewall2.equals("exit"))
		{
			re.receive(re.Firewall2ToVictim);
		}
	}

}