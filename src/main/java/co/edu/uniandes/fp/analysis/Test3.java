package co.edu.uniandes.fp.analysis;

import co.edu.uniandes.fp.annotation.FP;
import java.util.ArrayList;

@FP(useCases= {"C4"})
public class Test3 {
	
	private int attribute = 0;
	private ArrayList<Integer> array = new ArrayList<>();
	
	/**
	 * A method with only one case into consideration and no statements inside
	 */
	public void nonStatementsTest() {

	}
	
	/**
	 * A method with only one case into consideration and statements inside
	 * @return
	 */
	public int statementsTest() {
		int a = 1;
		int b = 2;
		array.add(new Integer(a));
		return a + b + attribute;
	}
	
	/**
	 * A method with more than one case into consideration
	 * @return
	 */
	@FP(useCases = {"C2"})
	public int statementsMixTest() {
		int a = 1;
		int b = 2;
		while(a<10) {
			a =+1;
		}
		return a + b;
	}
	
}
