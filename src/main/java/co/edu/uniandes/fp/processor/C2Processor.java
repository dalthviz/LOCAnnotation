package co.edu.uniandes.fp.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.edu.uniandes.fp.annotation.C2;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtMethod;

public class C2Processor extends AbstractAnnotationProcessor<C2, CtMethod<?>>{

	public final List<Double> countOfCaseMethods = new ArrayList<Double>();
		
	private double controlStatementsCount(String body) {
		double count = 0.0;
		Pattern p = Pattern.compile("\\bwhile\\b");
		Matcher m = p.matcher(body);
		while(m.find()){
		    count+=2;
		}
		p = Pattern.compile("\\bfor\\b");
		m = p.matcher(body);
		while(m.find()){
		    count+=2;
		}
		p = Pattern.compile("\\bif\\b");
		m = p.matcher(body);
		while(m.find()){
		    count+=2;
		}
		p = Pattern.compile("\\belse if\\b");
		m = p.matcher(body);
		while(m.find()){
		    count+=2;
		}
		p = Pattern.compile("\\belse\\b");
		m = p.matcher(body);
		while(m.find()){
		    count+=2;
		}
		return count;
	}

	@Override
	public void process(C2 annotation, CtMethod<?> method) {
		CtBlock<?> body = method.getBody();
		
		double loc = 0;
		double numStatements = body.getStatements().size() + controlStatementsCount(body.toString()) + 2;
		double numAnnotations = method.getAnnotations().size()!= 0?method.getAnnotations().size():1;
		loc = numStatements/numAnnotations;        
        countOfCaseMethods.add(loc);
	}
	
	public double totalLOC() {
		double total = 0.0;
		for(double loc: countOfCaseMethods) {
			total+=loc;
		}
		return total;
	}

}
