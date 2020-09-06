package cn_project;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DNS_Proxy extends Thread {

    public static void main(String a[]) throws IOException {
        // new Thread(new Amplifier()).start();
        System.out.println("\n\n\t\t\t\t\t-----------");
        System.out.println("\t\t\t\t\t DNS Proxy");
        System.out.println("\t\t\t\t\t-----------\n");
        System.out.println("\n IP : 10.1.10.2\n Port1 : 8001\t(With Test System 1)\n Port2 : 8002\t(With DNS Resolver)\n\n");
        
        ServerSocket ss = new ServerSocket(8001);
        Socket s1 = new Socket("localHost", 8002);
        Socket s = ss.accept();
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataInputStream in1 = new DataInputStream(s1.getInputStream());
        PrintStream dos = new PrintStream(s1.getOutputStream());
        PrintStream dos1 = new PrintStream(s.getOutputStream());
        // System.out.println("\n Note : \n\tTo retrieve a message please press enter without enter any string");
        
        while (true) {
        	
        	String str;
            String str1 = in.readLine();
            if (str1.length() != 0 && !str1.equals("end")) {
                
            	System.out.println(" Message Received From Test System 1 : " + str1);
            }
            if(str1.equals("end"))
            {
            	str = str1;
            }
            else
            {
            	str=in.readLine();
            	
            	System.out.println("hexPort: " + str1);
            	System.out.println("Data Received: " + str);
            	String temp = "At DNS Proxy: A? "+str1+"rub.de"+" Src Port: 8001 Dst Port: 8002\n" + str;
            	System.out.println(temp);
            }
            
            dos.println(str1);
            dos.println(str);
            if(!str.equals("end"))
                System.out.println(" Message Sent to DNS Resolver... ");
            else if (str.equals("end")) { 
                ss.close();
                s1.close();
                break;
            }
            str = in1.readLine();
            if (str.length() != 0 && !str.equals("")) {
                System.out.println(" Message Received From DNS Resolver : " + str);
            }
            dos1.println(str);
            System.out.println(" Message Sent to Test System 1... ");
            System.out.println("");
        }
        System.out.println("\n");
    }
}
