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
    	
    		final Launcher launcher = new Launcher();
    		launcher.setArgs(args);
    		launcher.run();

    		final Factory factory = launcher.getFactory();
    		final ProcessingManager processingManagerC1 = new QueueProcessingManager(factory);
    		final ProcessingManager processingManagerC2 = new QueueProcessingManager(factory);
    		final C1Processor processorC1 = new C1Processor();
    		final C2Processor processorC2 = new C2Processor();
    		
    		processingManagerC1.addProcessor(processorC1);
    		processingManagerC2.addProcessor(processorC2);
    		processingManagerC1.process(factory.Class().getAll());
    		processingManagerC2.process(factory.Class().getAll());
    		
    		String leftAlignFormat = " %-5s | %.2f  %n";
    		
    		System.out.println(processorC1.countOfCaseMethods);
    		System.out.println(processorC2.countOfCaseMethods);
    		
    		System.out.format("-------+--------%n");
    		System.out.format(" Case  | LOC    %n");
    		System.out.format("-------+--------%n");
    		System.out.format(leftAlignFormat, "C" + 1, ((C1Processor) processorC1).totalLOC());
    		System.out.format(leftAlignFormat, "C" + 2, ((C2Processor) processorC2).totalLOC());
    		System.out.format("-------+--------%n");
		
    }
}
