/**
 * @author Christelle
 * 
 */
public class ScannerDemo {


	private static String file1 = "C:\\Users\\admin\\workspace\\ParserScanner\\src\\prog2.jay";
	
	private static int counter = 1;

	public static void main(String args[]) {

		TokenStream ts = new TokenStream(file1);

		System.out.println(file1);

		while (!ts.isEoFile()) {   //from TokenStream.java
			Token to = ts.nextToken();
			System.out.println(to.toString());
		}
		
		/*
		while (!ts.isEndofFile()) {
			// TO BE COMPLETEDs
		}*/
	}
}
