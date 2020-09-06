package cn_project;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test_System1 extends Thread {

	static List<String> vulnerableList = new ArrayList<String>();
	
	public boolean checkVulnerable(String checkString, String address) 
	{
		boolean check = true;
		System.out.println("Checking String is " + checkString);
		if(checkString.contains("SSDP") || checkString.contains("CharGen") || checkString.contains("QOTD"))
		{
			for(int i=0; i<vulnerableList.size(); i++)
			{
				if(vulnerableList.get(i).contains(address))
				{	
					check = false;
				}
			}
			if(check == true)
				vulnerableList.add(address);
		}

		return check;
	}
	
    public static void main(String a[]) throws IOException {
        int i = 1, count=0;
        System.out.println("\n\n\t\t\t\t\t---------------");
        System.out.println("\t\t\t\t\t Test System 1");
        System.out.println("\t\t\t\t\t---------------\n");
        System.out.println("\n IP : 172.16.10.2\n Port1 : 8000\t(With Scanner)\n Port2 : 8001\t(With DNS Proxy)\n\n");
        // new Thread(new Proxy_Server()).start();
        ServerSocket ss = new ServerSocket(8000);
        Socket s1 = new Socket("localHost", 8001);
        Socket s = ss.accept();

        Random rn = new Random();
        int range = 100;
        int randomNum =  rn.nextInt(range) + 1;
       
        
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataInputStream in1 = new DataInputStream(s1.getInputStream());
 
        PrintStream dos = new PrintStream(s1.getOutputStream());
        PrintStream dos1 = new PrintStream(s.getOutputStream());
        
        //System.out.println("\n Note : \n\tTo retrieve a message please press enter without enter any string");
        
        while (true) {
            
        	if(count == 0)
        	{
        		System.out.println("receive SNY BIT");
        		System.out.println("SYNACK response is SYNACK with key is " + randomNum);
        		dos1.println(Integer.toString(randomNum));
        		String temp = in.readLine();
        		if(temp.equals(Integer.toString(randomNum)))
        		{
        			System.out.println("Condition match with Scanner1");
        			count++;
        		}
        		else{
        			System.out.println("Not match Please try again");
        			 s1.close();
        			 
                     ss.close();
                     
                     break;
        		}
        	}
        	
        	
        	String str,str1;
            str1 = in.readLine();
            if(str1.equals("end"))
            {
            	str = str1;
            }
            else
            {
            	str=in.readLine();
            	System.out.println("hexPort: " + str1);
            	System.out.println("Data Received: " + str);
            	String temp = "At Text System 1: A? "+str1+"rub.de"+" Src Port: 8001 Dst Port: 8002\n" + str;
            	System.out.println(temp);
            }
            	
            if (str.length() != 0 && !str.equals("end")) {
                System.out.println(" Message Received From Scanner : " + str);
            }
            
            if(!str.equals("end"))
            {
            	System.out.println();
            }
            
            Test_System1 obj = new Test_System1();
            System.out.println("he");
            if(obj.checkVulnerable(str, str1 ))
            {
            	dos.println(str1);
                dos.println(str);
            }
            else
            {
            	System.out.println("Repeated Amplification Quries found, Address is blocked");
            	str = "end";
            }
            
            
            
            if(!str.equals("end"))
                System.out.println(" Message Sent to DNS Proxy... ");
            if (str.equals("end")) {
                s1.close();
                ss.close();
                break;
            }
            str = in1.readLine();
            if (str.length() != 0) {
                System.out.println(" Message Received From DNS Proxy : " + str);
            }
            dos1.println(str);
            System.out.println(" Message Sent to Scanner... ");
            System.out.println("");
        	}
        }
}
