package co.edu.uniandes.fp.analysis;

import co.edu.uniandes.fp.annotation.FP;

public class Test2 {
	
	/**
	 * A method with only one case into consideration and no statements inside
	 */
	@FP(useCases = { "C1" })
	public void nonStatementsTest() {
		
	}
	
	/**
	 * A method with only one case into consideration and statements inside
	 * @return
	 */
	@FP(useCases = { "C1" })
	public int statementsTest() {
		int a = 1;
		int b = 2;
		return a + b;
	}
	
	/**
	 * A method with more than one case into consideration
	 * @return
	 */
	@FP(useCases = { "C1", "C2" })
	public int statementsMixTest() {
		int a = 1;
		int b = 2;
		while(a<10) {
			a =+1;
		}
		return a + b;
	}
	
	
}
