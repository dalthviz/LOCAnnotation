package co.edu.uniandes.fp.fp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import co.edu.uniandes.fp.processor.CUProcessor;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

/**
 * FP - Count to get LOC for functional points analysis.
 */
public class FP 
{
	/**
	 * Run the application
	 * @param a
	 */
	public static void main( String[] a )
	{
		final String[] args = {
				"-i", "src/main/java/co/edu/uniandes/fp/analysis/",
				"-o", "target/spooned/",
		};
		try {
			// Total LOC
			double lines = 0.0;
			List<File> files = Files.walk(Paths.get("src/main/java/co/edu/uniandes/fp/analysis/"))
					.filter(Files::isRegularFile)
					.map(Path::toFile)
					.collect(Collectors.toList());
			for (File file : files) {
				lines += (double)getLines(file);
			}
			//CSV generation;
			HashMap<String, Double> cases = doCUProcess(args).countOfCaseMethods;
			CSVExport("target/spooned/export.csv", cases, lines);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Get the User case (Caso de uso) processor 
	 * @param args
	 * @return
	 */
	private static CUProcessor doCUProcess(String[] args) {
		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();

		final Factory factory = launcher.getFactory();
		final ProcessingManager processingManager = new QueueProcessingManager(factory);
		final CUProcessor processor = new CUProcessor();
		processingManager.addProcessor(processor);
		processingManager.process(factory.Class().getAll());

		return processor;
	}

	/**
	 * Generate a csv file with the LOC by case and total loc
	 * @param route
	 * @throws IOException
	 */
	private static void CSVExport(String route, HashMap<String, Double> cases, double lines) throws IOException {
		try (
				BufferedWriter writer = Files.newBufferedWriter(Paths.get(route));

				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
						.withHeader("Case", "LOC", "%"));
				) {

			String leftAlignFormat = " %-5s | %.2f  | %.2f %n";
			double countToAdd = 0.0;

			System.out.format("-------|--------|------------%n");
			System.out.format(" Case  | LOC    | Proportion %n");
			System.out.format("-------|--------|------------%n");

			for (Entry<String, Double> entry: cases.entrySet()) {
				countToAdd += entry.getValue();
			}
			countToAdd = (lines - countToAdd) / (double) cases.size();
			for (Entry<String, Double> entry: cases.entrySet()) {
				System.out.format(leftAlignFormat, entry.getKey(), entry.getValue() + countToAdd,
						(entry.getValue() + countToAdd)/lines);
				csvPrinter.printRecord(entry.getKey(), entry.getValue() + countToAdd,
						(entry.getValue() + countToAdd)/lines);
			}
			System.out.format(leftAlignFormat, "Total", lines, 1.0);
			csvPrinter.printRecord("Total", lines, 1.0);
			
			System.out.format("-------|--------|------------%n");

			csvPrinter.flush();            
		}
	}

	/**
	 * Get the number of LOC. Based on https://www.codeproject.com/Tips/139036/Simple-Source-Line-Counter-in-Java-for-Java
	 * @param f
	 * @return
	 * @throws IOException
	 */
	private static int getLines(File f) throws IOException { 
		FileReader fr=new FileReader(f);
		BufferedReader br=new BufferedReader(fr); 
		int i=0;
		boolean isEOF=false;
		do{
			String t=br.readLine();
			if(t!=null){
				isEOF=true;
				t=t.replaceAll("\\n|\\t|\\s", "");
				if(!t.equals("") && !t.startsWith("//")
						&& !t.startsWith("/*") && !t.startsWith("*") && !t.startsWith("*/")) {
					i = i + 1;
				}
			}
			else {
				isEOF=false;
			}
		}while(isEOF);
		br.close();
		fr.close();
		return i;
	}
	
}
