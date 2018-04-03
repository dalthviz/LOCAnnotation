package co.edu.uniandes.fp.analysis;

import co.edu.uniandes.fp.annotation.C1;
import co.edu.uniandes.fp.annotation.C2;

public class Test {
	
	/**
	 * A method with only one case into consideration and no statements inside
	 */
	@C1
	public void nonStatementsTest() {
		
	}
	
	/**
	 * A method with only one case into consideration and statements inside
	 * @return
	 */
	@C1
	public int statementsTest() {
		int a = 1;
		return a;
	}
	
	/**
	 * A method with more than one case into consideration
	 * @return
	 */
	@C1
	@C2
	public int statementsMixTest() {
		int a = 1;
		int b = 2;
		while(a<10) {
			a =+1;
		}
		return a + b;
	}
	
	
}
