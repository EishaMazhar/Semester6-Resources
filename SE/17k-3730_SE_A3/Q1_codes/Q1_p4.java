/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/
public class Main{
	public static int fibonacciRecursion(int n){
	if(n == 0)
		return 0;
	if(n == 1 || n == 2)
			return 1;
	return fibonacciRecursion(n-2) + fibonacciRecursion(n-1);
	}
    public static void main(String args[]) {
	int maxNumber = 10;
	System.out.print("Fibonacci Series of "+maxNumber+" numbers: ");
	for(int i = 0; i < maxNumber; i++)
			System.out.print(fibonacciRecursion(i) +" ");
	}
}
