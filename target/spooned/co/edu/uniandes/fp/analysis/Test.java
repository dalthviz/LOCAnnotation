package co.edu.uniandes.fp.analysis;


public class Test {
    @co.edu.uniandes.fp.annotation.C1
    public void nonStatementsTest() {
    }

    @co.edu.uniandes.fp.annotation.C1
    public int statementsTest() {
        int a = 1;
        return a;
    }

    @co.edu.uniandes.fp.annotation.C1
    @co.edu.uniandes.fp.annotation.C2
    public int statementsMixTest() {
        int a = 1;
        int b = 2;
        while (a < 10) {
            a = +1;
        } 
        return a + b;
    }
}

