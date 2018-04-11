package co.edu.uniandes.fp.processor;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.edu.uniandes.fp.annotation.CU;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtMethod;

public class CUProcessor extends AbstractAnnotationProcessor<CU, CtMethod<?>>{

	public final HashMap<String, Double> countOfCaseMethods = new HashMap<String, Double>();
		
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
		p = Pattern.compile("\\btry\\b");
		m = p.matcher(body);
		while(m.find()){
		    count+=2;
		}
		p = Pattern.compile("\\bcatch\\b");
		m = p.matcher(body);
		while(m.find()){
		    count+=2;
		}
		p = Pattern.compile("\\bfinally\\b");
		m = p.matcher(body);
		while(m.find()){
		    count+=2;
		}
		p = Pattern.compile("\\bswitch\\b");
		m = p.matcher(body);
		while(m.find()){
		    count+=2;
		}
		
		return count;
	}

	@Override
	public void process(CU annotation, CtMethod<?> method) {
		CtBlock<?> body = method.getBody();
		
		// TODO Count the number of statements in the body of the method, add number to count;
		double loc = 0;
		String[] cases = method.getAnnotation(CU.class).useCases();
		double numStatements = body.getStatements().size() + controlStatementsCount(body.toString()) + 2;
		double numCases = cases.length!= 0?cases.length:1;
		loc = numStatements/numCases;
		
		for (String cu : cases) {
			if(countOfCaseMethods.containsKey(cu)) {
				double count = countOfCaseMethods.get(cu) + loc;
				countOfCaseMethods.put(cu, count);
			}else {
				countOfCaseMethods.put(cu, loc);
			}
		}
	}
	
}
