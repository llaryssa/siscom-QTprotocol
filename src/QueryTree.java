import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;

import sun.awt.X11.XAbstractMenuItem;

public class QueryTree {

	static Vector<String> M;
	static Vector<String> Q;
	static String[] tags;
	static String temp;
	static Vector<Double> colisoesgraph = new Vector<Double>();
	static Vector<Double> sucessosgraph = new Vector<Double>();
	static Vector<Double> vaziosgraph = new Vector<Double>();
	static Vector<Double> xgraph = new Vector<Double>();
	static Vector<Double> colisoesTag = new Vector<Double>();
	static Vector<Double> vaziosTag = new Vector<Double>();
	static Vector<Double> sucessosTag = new Vector<Double>();
	static double colisoes;
	static double sucessos;
	static double vazios;

	public static void main(String[] args) {
		
		long start = System.nanoTime();

		Arquivo arq;

		String folder;
		for (int teste = 0; teste < 3; ++teste) {// para cada teste
			folder = "tags" + (teste + 1) + "/";
			colisoesgraph = new Vector<Double>();
			sucessosgraph = new Vector<Double>();
			vaziosgraph = new Vector<Double>();
			xgraph = new Vector<Double>();
			for (int q = 100; q <= 1000; q += 100) {// para gada quantidade de
													// tags
				colisoesTag = new Vector<Double>();
				vaziosTag = new Vector<Double>();
				sucessosTag = new Vector<Double>();
				for (int sim = 0; sim < 20; ++sim) {// faça 20 testes
					colisoes = 0;
					sucessos = 0;
					vazios = 0;
					String path_in = folder + q + "tags/tags" + (sim + 1)
							+ ".in";
					String path_out = folder + q + "tags/tags.out";
					arq = new Arquivo(path_in, path_out);

					tags = new String[q];
					for (int j = 0; j < q; ++j) {
						tags[j] = arq.readString();
					}
					// tags lidas
					M = new Vector<String>(); // Memory (ja lidas)
					Q = new Vector<String>(); // Query queue
					Q.add("");
					while (!Q.isEmpty()) {
						// String prefix = Q.remove(Q.size()-1);
						String prefix = Q.remove(0); // assim o QT shortcut nao
														// falha
						// System.out.println("take: " + prefix);
						query_tree(prefix);
						//query_tree_shortcut(prefix);
					}

					// System.out.println("M size: " + M.size());
					for (int m = 0; m < M.size(); ++m) {
						arq.println(M.get(m));
					}
					arq.close();
					colisoesTag.add(colisoes);
					sucessosTag.add(sucessos);
					vaziosTag.add(vazios);

					System.out.print("*");
				}// acabaram os 20 testes de quantidade fixa
				double mediaColisoes = getMedia(colisoesTag);
				double mediaSucessos = getMedia(sucessosTag);
				double mediaVazios = getMedia(vaziosTag);
				xgraph.add((double) Math.round(q));
				colisoesgraph.add(mediaColisoes);
				sucessosgraph.add(mediaSucessos);
				vaziosgraph.add(mediaVazios);
				System.out.println();
			}// finalizou teste
				// desenhando graficos;
			JFrame frame = new JFrame("Teste" + (teste+1));
			Chart2D chart = new Chart2D();
			frame.getContentPane().add(chart);
			chart.setBackground(new Color(255,255,255));
			
			
			showGraph("Sucessos", vectorToArray(xgraph), vectorToArray(sucessosgraph), new Color(0, 0, 255),
					chart);
			showGraph("Colisoes", vectorToArray(xgraph), vectorToArray(colisoesgraph), new Color(255, 0, 0),
					chart);
			showGraph("Vazios", vectorToArray(xgraph), vectorToArray(vaziosgraph), new Color(0, 255, 0),
					chart);
			frame.setSize(600,  400);
			frame.setVisible(true);

			System.out.println();
		}

		System.out.println("finished");

		long end = System.nanoTime();

		// Iterator<String> it = M.iterator();
		// while(it.hasNext())System.out.println(it.next());

		System.out.println((end - start) / 1000000 + "ms");

	}
	public static  double[] vectorToArray(Vector<Double> vector){
		int t = vector.size();
		double [] array = new double[t];
		for(int i =0;i<t;i++){
			array[i] = vector.elementAt(i);
		}
			return array;
	}

	public static void showGraph(String title, double[] xa, double[] ya,
			Color lineColor, Chart2D chart) {
		ITrace2D trace = new Trace2DSimple();
		trace.setColor(lineColor);
		trace.setName(title);

		chart.addTrace(trace);
		for (int i = 0; i < xa.length; i++) {
			trace.addPoint(xa[i], ya[i]);
		}
	}

	public static void testGraph2() {
		JFrame frame = new JFrame("Teste" + 2);
		Chart2D chart = new Chart2D();
		frame.getContentPane().add(chart);
		frame.setVisible(true);
	}

	public static void testGraph() {

		// Create a chart:
		Chart2D chart = new Chart2D();
		// Create an ITrace:
		ITrace2D trace = new Trace2DSimple();
		// Add the trace to the chart. This has to be done before adding points
		// (deadlock prevention):
		trace.setColor(new Color(0, 0, 255));
		trace.setName("Geeo");

		chart.addTrace(trace);
		// Add all points, as it is static:
		Random random = new Random();
		for (int i = 100; i >= 0; i--) {
			trace.addPoint(i, random.nextDouble() * 10.0 + i);
		}
		// Make it visible:
		// Create a frame.
		JFrame frame = new JFrame("MinimalStaticChart");
		// add the chart to the frame:
		frame.getContentPane().add(chart);
		frame.setSize(400, 300);
		// Enable the termination button [cross on the upper right edge]:

		frame.setVisible(true);
	}

	public static void query_tree(String prefix) {
		int result = query(prefix);
		if (result > 1) { // deu colisao
			colisoes++;
			Q.add(prefix + "0");
			Q.add(prefix + "1");
		} else if (result == 1) {
			sucessos++;
			M.add(temp);
		} else if (result == 0) {
			// idle
			vazios++;
		}
	}

	public static int query(String prefix) {
		int result = 0;
		for (int i = 0; i < tags.length && result <= 2; ++i) {
			if (tags[i].startsWith(prefix)) {
				result++;
				temp = tags[i];
			}
		}
		return result;
	}

	public static void query_tree_shortcut(String prefix) {
		int result = query(prefix);
		if (result > 1) { // deu colisao
			colisoes++;
			Q.add(prefix + "0");
			Q.add(prefix + "1");
		} else if (result == 1) {
			M.remove(temp); // pelo mecanismo abaixo, ele pode ler uma tag mais
							// de uma vez
			sucessos++;
			M.add(temp);
		} else if (result == 0) {
			String new_query = changeLastBit(prefix);
			Q.remove(new_query);
			Q.remove(new_query + "0");
			Q.remove(new_query + "1");
			Q.add(new_query + "0");
			Q.add(new_query + "1");
			vazios++;
		}
	}

	private static String changeLastBit(String prefix) {
		String first = "";
		if (prefix.length() != 1)
			first = prefix.substring(0, prefix.length() - 1);

		if (prefix.charAt(prefix.length() - 1) == '0')
			first = first + "1";
		else
			first = first + "0";

		return first;
	}

	public static double getMedia(Vector<Double> list) {
		Iterator<Double> it = list.iterator();
		double r = 0;
		while (it.hasNext()) {
			r += it.next();
		}
		return r / list.size();

	}

}
