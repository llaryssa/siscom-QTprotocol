import java.io.File;
import java.util.Random;
import java.util.Vector;

public class TagsGenerator {
	static int maxTestes = 3;
	static int maxEtiquetas = 300000;
	static int sizeEtiqueta = 96;
	static Vector<String> geradas = new Vector<String>();
	static Random rand = new Random();
	static Vector<String> pilha = new Vector<String>();

	public static void main(String[] args) {
		for (int teste = 1; teste <= maxTestes; teste++) {
			File testePath = new File("tags" + teste);
			if (!testePath.exists()) {
				testePath.mkdir();
			}

			for (int tags = 100; tags <= 1000; tags += 100) {
				for (int s = 0; s < 20; s++) {
					File tagsFolder = new File(testePath.getAbsolutePath()
							+ "/" + tags + "tags");
					if (!tagsFolder.exists()) {
						tagsFolder.mkdir();
					}
					String filename = tagsFolder.getAbsolutePath() + "/tags"
							+ (s) + ".in";
					geradas.clear();
					Arquivo arq = new Arquivo("dumb.in", filename);
					System.out.println("teste"+teste+" " +tags+" etiquetas "+(s+1));
					switch (teste) {
					case 1:

						generate("", sizeEtiqueta);
					case 2:
						generatePrefix("10011110101000101101010011010110",
								sizeEtiqueta);
						break;
					case 3:
						generateSufix("10011110101000101101010011010110",
								sizeEtiqueta);
						break;
					}

					int t = 0;

					while (t != tags) {
						int k = rand.nextInt(Math.max(2, geradas.size()));

						String tag = geradas.elementAt(k);
						arq.println(tag);
						geradas.removeElementAt(k);
						t++;
					}

				}
			}
		}

	}

	public static void generate(String prefix, int finalSize) {
		while(geradas.size()<maxEtiquetas){
			String aux = prefix;
			while(aux.length()<finalSize){
				aux+=rand.nextInt(2)%2;
			}
		//	System.out.println(aux);
			geradas.add(aux);
		}
		
		/*
			if (prefix.length() < sizeEtiqueta) {
				if(rand.nextBoolean()){
				generate(prefix + 1, finalSize);
				generate(prefix + 0, finalSize);}
				else{
					generate(prefix + 0, finalSize);
					generate(prefix + 1, finalSize);	
				}
			} else {
				geradas.addElement(prefix);
				System.out.println(prefix);
			}
		*/
		
			
		/*
		Vector<String> pilha = new Vector<String>();
		String prefix = "";
		pilha.add(prefix);
		while (geradas.size() < maxEtiquetas) {
			String atual = pilha.elementAt(0);
			pilha.remove(0);
			if(atual.length()<sizeEtiqueta){
				if(atual.length()>sizeEtiqueta/2)
				if (rand.nextBoolean()) {
					pilha.add(atual+1);
					pilha.add(atual+0);

				} else {
					pilha.add(atual+0);
					pilha.add(atual+1);
				}
			}else{
				pilha.add(atual);
			}
		}
*/

	}

	public static void generatePrefix(String prefix, int finalSize) {
		if (geradas.size() < maxEtiquetas) {
			if (prefix.length() < sizeEtiqueta) {
				generatePrefix(prefix + 0, finalSize);
				generatePrefix(prefix + 1, finalSize);
			} else {
				geradas.addElement(prefix);
				System.out.println(prefix);
			}
		}
	}

	public static void generateSufix(String sufix, int finalSize) {
		if (geradas.size() < maxEtiquetas) {
			if (sufix.length() < sizeEtiqueta) {
				generateSufix(0 + sufix, finalSize);
				generateSufix(1 + sufix, finalSize);
			} else {
				geradas.addElement(sufix);
			}
		}
	}

}
