import java.util.Scanner;
class Test 
{
    public static void main(String[] args)  
    { 
        int num = 0, n = 0;
	    Scanner s = new Scanner(System.in);
        System.out.print("Enter the no. to find it's factorial: ");
        num = s.nextInt();
	    n = num;
        if (num>0)
        {
            int fact = 1;
            while(num != 1)
            {
                fact = fact * num;
                num = num - 1;
            }
            System.out.println("Factorial of "+ n + " is " +fact);
        }
        else
        {
            System.out.println("\nError");
        }
    }
}