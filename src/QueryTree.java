import java.util.Vector;

public class QueryTree {
	
	static Vector<String> M;
	static Vector<String> Q;
	static String[] tags;
	static String temp;

	public static void main(String[] args) {
		
		long start = System.nanoTime();
		
		Arquivo arq;		
		
		String folder;
		for (int g = 0; g < 3; ++g) {			
			folder = "tags" + (g+1) + "/";
			
			for (int i = 100; i <= 1000; i += 100) {
				String path_in = folder + i + "tags/tags.in";
				String path_out = folder + i + "tags/tags.out";
				arq = new Arquivo(path_in, path_out);
				
				tags = new String[i];
				for (int j = 0; j < i; ++j) {
					tags[j] = arq.readString();
				}
				// tags lidas
				
				
				M = new Vector<String>();  // Memory (ja lidas)
				Q = new Vector<String>();  // Query queue
				
				Q.add("");
				
				while (!Q.isEmpty()) {
					String prefix = Q.remove(Q.size()-1);
//					System.out.println("take: " + prefix);
					query_tree(prefix);
				}
				
				System.out.println("M size: " + M.size());
				for (int m = 0; m < M.size(); ++m) {
					arq.println(M.get(m));
				}
				arq.close();
			}
		}
	
		System.out.println("finished");
		
		
		long end = System.nanoTime();
		
		System.out.println((end - start)/1000000 + "ms");

	}
	
	public static void query_tree (String prefix) {
		int result = query(prefix);
		if (result > 1) { // deu colisao
			Q.add(prefix + "0");
			Q.add(prefix + "1");
		} else if (result == 1) {
			M.add(temp);
		} else if (result == 0) {
			// idle
		}
	}

	public static int query (String prefix) {
		int result = 0;
		for (int i = 0; i < tags.length && result <= 2; ++i) {
			if (tags[i].startsWith(prefix)) {
				result++;
				temp = tags[i];
			}
		}	
		return result;
	}
	
	public static void query_tree_shortcut (String prefix) {
		int result = query(prefix);
		if (result > 1) { // deu colisao
			Q.add(prefix + "0");
			Q.add(prefix + "1");		
		} else if (result == 1) {
			M.remove(temp); // pelo mecanismo abaixo, ele pode ler uma tag mais de uma vez
			M.add(temp);
		} else if (result == 0) {
			String new_query = changeLastBit(prefix);
			Q.remove(new_query);
			Q.remove(new_query + "0");
			Q.remove(new_query + "1");
			Q.add(new_query + "0");
			Q.add(new_query + "1");
		}
	}

	private static String changeLastBit(String prefix) {
		String first = "";
		if (prefix.length() != 1) 
			first = prefix.substring(0, prefix.length()-1);
		
		if (prefix.charAt(prefix.length()-1) == '0') 
			first = first + "1";
		else
			first = first + "0";
		
		return first;
	}

}
