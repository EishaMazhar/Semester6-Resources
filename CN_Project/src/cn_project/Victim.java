package cn_project;

import java.io.*;
import java.net.*;

public class Victim extends Thread {

    public static void main(String a[]) throws IOException {
        //new Thread(new Github_Server()).start();
    	int count=0;
        System.out.println("\n\n\t\t\t\t\t--------");
        System.out.println("\t\t\t\t\t Victim");
        System.out.println("\t\t\t\t\t--------\n");
        System.out.println("\n IP : 19.1.10.2\n Port : 8003\t(With Proxy)\n\n");
        ServerSocket ss = new ServerSocket(8004);
        Socket s = ss.accept();
        DataInputStream in1 = new DataInputStream(s.getInputStream());
        PrintStream dos = new PrintStream(s.getOutputStream());
        DataInputStream in = new DataInputStream(System.in);
        //System.out.println("\n Note : \n\tTo retrieve a message please press enter without enter any string");
        
        while (true) {
        	String str = in1.readLine();
            if (str.length() != 0 && !str.equals("end")) {
                System.out.println(" Message Received From Amplifier : " + str);
            }
            if (str.equals("end")) {
                ss.close();
                break;
            }
            str = "OK";
            dos.println(str);
            System.out.println(" Message Sent To Amplifier : " + str);
            if (str.equals("end")) {
                ss.close();
                break;
            }
            System.out.println("");
        }
        System.out.println("\n");
    }
}