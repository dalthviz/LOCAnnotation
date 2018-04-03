package co.edu.uniandes.fp.processor;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.fp.annotation.C1;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtMethod;

public class C1Processor extends AbstractAnnotationProcessor<C1, CtMethod<?>>{

	public final List<Double> countOfMethods = new ArrayList<Double>();
	
	@Override
	public void process(C1 annotation, CtMethod<?> method) {
		CtBlock<?> body = method.getBody();
		// TODO Count the number of statements in the body of the method, add number to count;
        double loc = body.getStatements().size(); //TODO take into account number of annotations
        loc += controlStatementsCount();//TODO add if, else, else, if , while, for, etc.... in the count
        countOfMethods.add(loc);
	}
	
	private double controlStatementsCount() {
		return 0.0;
	}

}
