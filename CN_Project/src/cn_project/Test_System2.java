
package cn_project;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

import com.sun.xml.internal.fastinfoset.algorithm.HexadecimalEncodingAlgorithm;

public class Test_System2 {
    
	public String toHex(String arg) {
	    return String.format("%040x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
	}
	
    public static void main(String a[]) throws IOException {
        // new Thread(new Amplifier()).start();
        System.out.println("\n\n\t\t\t\t\t---------------");
        System.out.println("\t\t\t\t\t Test System 2");
        System.out.println("\t\t\t\t\t---------------\n");
        System.out.println("\n IP : 168.1.11.2\n Port1 : 8003\t(With DNS Resolver)\n Port2 : 8004\t(With Victim)\n\n");
        ServerSocket ss = new ServerSocket(8003);
        Socket s1 = new Socket("localHost", 8004);
        Socket s = ss.accept();
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataInputStream in1 = new DataInputStream(s1.getInputStream());
        PrintStream dos = new PrintStream(s1.getOutputStream());
        PrintStream dos1 = new PrintStream(s.getOutputStream());
        // System.out.println("\n Note : \n\tTo retrieve a message please press enter without enter any string")      
        while (true) {
            String str1 = in.readLine();
            if (str1.length() != 0 && !str1.equals("end")) {
                System.out.println(" Message Received From Test System 2 : " + str1);
            }
            String str;
            if(str1.equals("end"))
            {
            	str = str1;
            }
            else
            {
            	str=in.readLine();
            	System.out.println("hexPort: " + str1);
            	System.out.println("Data Received: " + str);
            	String temp = "At Text System 2: A? "+str1+"rub.de"+" Src Port: 8003 Dst Port: 8000\n" + str;
            	System.out.println(temp);
            }
            if(str1.equals(Integer.toHexString(8000)))
            {
            	dos.println(str);
            }
            else{
            	System.out.println("Spoof network detected...");
            	System.out.println("Content blocked...");
            }
            if(!str.equals("end"))
                System.out.println(" Message Sent to Victim... ");
            if (str.equals("end")) {
                ss.close();
                s1.close();
                break;
            }
            str = in1.readLine();
            if (str.length() != 0) {
                System.out.println(" Message Received From Victim : " + str);
            }
            dos1.println(str);
            System.out.println(" Message Sent to Test System 2... ");
            System.out.println("");
        } 
        System.out.println("\n");
    }
    
}
