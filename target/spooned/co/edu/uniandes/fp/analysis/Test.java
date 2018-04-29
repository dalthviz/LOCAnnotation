package co.edu.uniandes.fp.analysis;


import co.edu.uniandes.annotation.FP;


public class Test {
    @FP(useCases = { "C1", "C3" })
    public void nonStatementsTest() {
    }

    @FP(useCases = { "C1" })
    public int statementsTest() {
        int a = 1;
        int b = 2;
        return a + b;
    }

    @FP(useCases = { "C1", "C2", "C3" })
    public int statementsMixTest() {
        int a = 1;
        int b = 2;
        while (a < 10) {
            a = +1;
        } 
        return a + b;
    }
}

