package co.edu.uniandes.fp.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class JSFPProcessor {
	//Number of LoC per method
	public final HashMap<String, Double> countOfCaseMethods = new HashMap<String, Double>();

	//Path to the files
	private String path;

	/**
	 * Builds a processor for JS files
	 * @param path
	 */
	public JSFPProcessor(String path) {
		this.path = path;
		process();
	}

	/**
	 * Process the files on the path
	 */
	private void process() {
		List<File> files;
		try {
			files = Files.walk(Paths.get(path))
					.filter(Files::isRegularFile)
					.map(Path::toFile)
					.collect(Collectors.toList());
			for (File file : files) {
				countFP(file);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void countFP(File f) throws IOException {
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr); 

		String t = br.readLine();
		if (t.startsWith("<!") || t.startsWith("//") && !f.getName().toLowerCase().endsWith(".java")) {
			t = t.trim().replace("//", "").replace("<!", "").replace("-", "").replace(">", "").replace(" ","");
			Double i = 0.0;
			String[] annotations = t.split(",");

			if (annotations.length > 0 && !"".equals(annotations[0])) {
				for (String string : annotations) {
					string = string.trim();
				}
				boolean isEOF=false;
				do{
					t = br.readLine();
					if(t!=null){
						isEOF=true;
						t=t.replaceAll("\\n|\\t|\\s", "");
						if(!t.equals("") && !t.startsWith("//")
								&& !t.startsWith("/*") && !t.startsWith("*")
								&& !t.startsWith("*/") && !t.startsWith("@FP")
								&& !t.startsWith("<!")) {
							i = i + 1;
						}
					}
					else {
						isEOF=false;
					}
				}while(isEOF);
				i = i/(double)annotations.length;
				setCountOfCaseMethods(i, annotations);
				br.close();
				fr.close();
			}
		}
	}

	private void setCountOfCaseMethods(Double value, String[] annotations) {
		for (String cu : annotations) {
			if(countOfCaseMethods.containsKey(cu)) {
				double count = countOfCaseMethods.get(cu) + value;
				countOfCaseMethods.put(cu, count);
			}else {
				countOfCaseMethods.put(cu, value);
			}
		}
	}
}
