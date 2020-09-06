/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/
import java.util.Scanner;

public class Sum_Numbers 
{
    int sum = 0, j = 0;
    public static void main(String[] args) 
    {
        int n;
        Scanner s = new Scanner(System.in);
        System.out.print("Enter the no. of elements you want:");
        n = s.nextInt();
        int a[] = new int[n];
        System.out.print("Enter all the elements you want:");
        for(int i = 0; i < n; i++)
        {
            a[i] = s.nextInt();
        }
        Sum_Numbers obj = new Sum_Numbers();
        int x = obj.add(a, a.length, 0);
        System.out.println("Sum:"+x);
    }
    int add(int a[], int n, int i)
    {
        if(i < n)
        {
            return a[i] + add(a, n, ++i);
        }   
        else
        {
            return 0;
        }
    }
}