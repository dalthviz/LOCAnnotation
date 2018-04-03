package co.edu.uniandes.fp.fp;


import co.edu.uniandes.fp.processor.C1Processor;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

/**
 * FP - Count to get LOC for functional points analysis.
 */
public class FP 
{
    public static void main( String[] a )
    {
    	final String[] args = {
				"-i", "src\\main\\java\\co\\edu\\uniandes\\fp\\analysis\\Test.java",
				"-o", "target/spooned/",
		};
    	
    		final Launcher launcher = new Launcher();
    		launcher.setArgs(args);
    		launcher.run();

    		final Factory factory = launcher.getFactory();
    		final ProcessingManager processingManager = new QueueProcessingManager(factory);
    		final C1Processor processor = new C1Processor();
    		processingManager.addProcessor(processor);
    		processingManager.process(factory.Class().getAll());
    		System.out.println(processor.countOfMethods);
		
    }
}
