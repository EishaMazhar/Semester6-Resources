package filterIpSpoofing;

public class testing {

	public boolean check(String data)
	{
		
		int i=8;
		String src, dst, calling;
		String temp="";
		while(data.charAt(++i) != '.')
		{
			temp += data.charAt(i);
		}
		
		i = data.indexOf("Src =");
		System.out.println("Position of i = "+ i + "and data = " + data.charAt(i));
		
		
		return false;
	}
	
}
