package co.edu.uniandes.fp.processor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.edu.uniandes.annotation.FP;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

public class FPProcessor extends AbstractAnnotationProcessor<FP, CtElement>{

	//Number of LoC per method
	public final HashMap<String, Double> countOfCaseMethods = new HashMap<String, Double>();

	//Signatures of methods already processed
	public final HashMap<String, String> processedMethods = new HashMap<String, String>();

	/**
	 * Count control statements in the string passed as parameter
	 * @param body
	 * @return Number of statements.
	 */
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

	/**
	 * Determine if the method is one of the basic methods added since is an Object.
	 * @param signature of the method
	 * @return True if is a basic method False otherwise
	 */
	public boolean basicMethod(String signature) {
		boolean basic = false;
		if(signature.equals("getClass")) {
			basic = true;
		}
		else if(signature.equals("wait")){
			basic = true;
		}
		else if(signature.equals("notifyAll")){
			basic = true;
		}
		else if(signature.equals("equals")){
			basic = true;
		}
		else if(signature.equals("notify")){
			basic = true;
		}
		else if(signature.equals("hashCode")){
			basic = true;
		}
		else if(signature.equals("finalize")){
			basic = true;
		}
		else if(signature.equals("toString")){
			basic = true;
		}
		else if(signature.equals("clone")){
			basic = true;
		}
		else if(signature.equals("registerNatives")){
			basic = true;
		}
		return basic;				
	}

	/**
	 * Auxiliary method to count the LoC of a method
	 * @param method
	 */
	private void countMethod(CtMethod<?> method, FP classAnnotation) {
		CtBlock<?> body = ((CtMethod<?>) method).getBody();
		double loc = 0;
		String[] cases;
		if(classAnnotation == null) {
			cases = method.getAnnotation(FP.class).useCases();
			if(cases.length != 0)
				processedMethods.put(method.getSignature(), method.getBody().toString());
		}
		else {
			cases = classAnnotation.useCases();
		}
		double numStatements = body.getStatements().size()
				+ controlStatementsCount(body.toString()) + 2;//+2 for the signature of the method
		double numCases = cases.length!= 0?cases.length:1;
		loc = numStatements/numCases;
		addToCount(cases, loc);
	}

	/**
	 * Add new values to the count of LoC by case
	 * @param cases list of cases to add
	 * @param value value to add to each case
	 */
	private void addToCount(String[] cases, Double value) {
		for (String cu : cases) {
			if(countOfCaseMethods.containsKey(cu)) {
				double count = countOfCaseMethods.get(cu) + value;
				countOfCaseMethods.put(cu, count);
			}else {
				countOfCaseMethods.put(cu, value);
			}
		}
	}

	/**
	 * Tells if the method was already processed
	 * @param method
	 * @return True if the method was processed false otherwise.
	 */
	private boolean wasProcessed(CtMethod<?> method) {
		boolean processed = false;
		if(processedMethods.containsKey(method.getSignature())) {
			processed = processedMethods.get(method.getSignature())
					.equals(method.getBody().toString());
		}
		return processed;
	}

	@Override
	public void process(FP annotation, CtElement element) {

		if( element instanceof CtMethod) {
			countMethod((CtMethod<?>) element, null);
		}else {
			Collection<CtFieldReference<?>> fields = ((CtClass<?>) element).getAllFields();
			double fieldsLoc = (double)(fields.size());

			Set<CtTypeReference<?>> imports = ((CtClass<?>) element).getUsedTypes(true);
			double importLoc = (double)(imports.size()-2);
			double numLoc = (importLoc + fieldsLoc + 3.0) / (double)annotation.useCases().length; //Add 3 for the package(1) and signature of class (2)

			addToCount(annotation.useCases(), numLoc); 

			Set<CtMethod<?>> methods = ((CtClass<?>) element).getAllMethods();
			for (CtMethod<?> method : methods) {
				if(!basicMethod(method.getSimpleName())
						|| method.getAnnotation(Override.class) != null) {
					if(!wasProcessed(method)) {
						countMethod(method, annotation);
					}
				}
			}
		}
	}

}
