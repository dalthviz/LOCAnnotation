package co.edu.uniandes.fp.fp;

import co.edu.uniandes.fp.processor.C1Processor;
import co.edu.uniandes.fp.processor.C2Processor;
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
				"-i", "src/main/java/co/edu/uniandes/fp/analysis/Test.java",
				"-o", "target/spooned/",
		};
    		//TODO Create multiple launchers;
    		
    		String leftAlignFormat = " %-5s | %.2f  %n";
    		
    		System.out.format("-------+--------%n");
    		System.out.format(" Case  | LOC    %n");
    		System.out.format("-------+--------%n");
    		System.out.format(leftAlignFormat, "C" + 1, doC1Process(args).totalLOC());
    		System.out.format(leftAlignFormat, "C" + 2, doC2Process(args).totalLOC());
    		System.out.format("-------+--------%n");
		
    }
    
    public static C1Processor doC1Process(String[] args) {
		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();

		final Factory factory = launcher.getFactory();
		final ProcessingManager processingManagerC1 = new QueueProcessingManager(factory);
		final C1Processor processorC1 = new C1Processor();
		processingManagerC1.addProcessor(processorC1);
		processingManagerC1.process(factory.Class().getAll());
		
		return processorC1;
    }
    
    public static C2Processor doC2Process(String[] args) {
		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();

		final Factory factory = launcher.getFactory();
		final ProcessingManager processingManager = new QueueProcessingManager(factory);
		final C2Processor processorC2 = new C2Processor();
		processingManager.addProcessor(processorC2);
		processingManager.process(factory.Class().getAll());
		
		return processorC2;
    }
}
