/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/
import java.util.Scanner;

public class Main 
{

    public static void main(String[] args) {

        //scanner class object creation
		Scanner sc=new Scanner(System.in);
		//input from user
		System.out.print("Enter Starting Number : ");
		int low = sc.nextInt();
		System.out.print("Enter Ending Number : ");
		int high = sc.nextInt();
        

        while (low < high) {
            boolean flag = false;

            for(int i = 2; i <= low/2; ++i) {
                // condition for nonprime number
                if(low % i == 0) {
                    flag = true;
                    break;
                }
            }

            if (!flag && low != 0 && low != 1)
                System.out.print(low + " ");

            ++low;
        }
    }
}