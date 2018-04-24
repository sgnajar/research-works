package testing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class testHashMapUnique {

	public static void main(String args[]) {
		String[] name1 = { "Amy", "Jose", "Jeremy", "Alice", "Patrick" };

		String[] name2 = { "Alan", "Amy", "Jeremy", "Helen", "Alexi" };

		String[] name3 = { "Adel", "Aaron", "Amy", "James", "Alice" };

		HashMap<String, String> letter = new HashMap <String, String>();


		for (int i = 0; i < name1.length; i++)
			letter.put(name1[i], null);

		for (int j = 0; j < name2.length; j++)
			letter.put(name2[j], null);

		for (int k = 0; k < name3.length; k++)
			letter.put(name3[k], null);
		
		System.out.println(letter.size() + " letters must be sent to: \n" + letter);
		
//		int n = 0;
//		int x = 0;
//		while (n < 3) {
//			n++;
//			x += n;
//			System.out.println(n);
//		}
		
	}

}
