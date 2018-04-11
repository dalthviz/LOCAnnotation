package co.edu.uniandes.fp.fp;

import java.util.HashMap;
import java.util.Map.Entry;

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
    		//TODO Add markdown render;
    		
    		String leftAlignFormat = " %-5s | %.2f  %n";
    		
    		System.out.format("-------|--------%n");
    		System.out.format(" Case  | LOC    %n");
    		System.out.format("-------|--------%n");
    		
    		HashMap<String, Double> cases = doCUProcess(args).countOfCaseMethods;
    		for (Entry<String, Double> entry: cases.entrySet()) {
        		System.out.format(leftAlignFormat, entry.getKey(), entry.getValue());
			}
    		System.out.format("-------|--------%n");
		
    		
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
    
  }
