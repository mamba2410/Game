package callummcgregor.lib;

public class Utilities {
	
	public static int getPositiveDifference(int i1, int i2){
		return (int)(Math.sqrt((i1-i2)*(i1-i2)));
	}
	
	public static void printf(String str, Object... o){
		System.out.println(String.format(str, o));
	}

}
