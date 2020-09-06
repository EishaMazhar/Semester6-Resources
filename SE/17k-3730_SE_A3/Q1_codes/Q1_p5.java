import java.util.*;
class pyramid { 
    public static void main(String []args) 
    { 
        Scanner sc= new Scanner(System.in);  System.out.print("Enter N: ");   int no_of_rows= sc.nextInt();   
        int t; 

        int i=1;
        while(i <= no_of_rows)
        {

            int j=i;
            while(j < no_of_rows)
            {
                System.out.print("\t"); 
                j++;
            }
 
            t = i;

            int k=1;
            while( k <= i)
            {
                System.out.print(t + "\t\t");
                t = t + no_of_rows - k; 
                k++;
            }
            System.out.println(); 
            i++;
        }
        sc.close(); //line no. 31
    }//main
     
} //class


