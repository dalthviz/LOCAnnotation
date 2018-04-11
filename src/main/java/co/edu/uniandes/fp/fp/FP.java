package co.edu.uniandes.fp.fp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;

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
    	
    	//CSV generation;
		HashMap<String, Double> cases = doCUProcess(args).countOfCaseMethods;
    	try {
			CSVExport("target/spooned/export.csv", cases);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
    }
    
    /**
     * Get the User case (Caso de uso) processor 
     * @param args
     * @return
     */
    public static CUProcessor doCUProcess(String[] args) {
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
     * Generate a csv file with the LOC by case. Based on 
     * @param route
     * @throws IOException
     */
    public static void CSVExport(String route, HashMap<String, Double> cases) throws IOException {
    	 try (
    	      BufferedWriter writer = Files.newBufferedWriter(Paths.get(route));

    	      CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
    	                    .withHeader("Case", "LOC"));
    	      ) {
    		 
    			String leftAlignFormat = " %-5s | %.2f  %n";
        		
        		System.out.format("-------|--------%n");
        		System.out.format(" Case  | LOC    %n");
        		System.out.format("-------|--------%n");
        		
        		for (Entry<String, Double> entry: cases.entrySet()) {
            		System.out.format(leftAlignFormat, entry.getKey(), entry.getValue());
        		    csvPrinter.printRecord(entry.getKey(), entry.getValue());
    			}
        		System.out.format("-------|--------%n");
    	         
    	         csvPrinter.flush();            
    	        }
    }
    
  }
