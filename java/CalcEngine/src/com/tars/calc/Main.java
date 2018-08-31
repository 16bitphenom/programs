package com.tars.calc;

public class Main {

    public static void main(String[] args) {

        MathEquation[] equations = new MathEquation[4];
        equations[0] = create(1, 2, '+');
        equations[1] = create(33,22, '-');
        equations[2] = create(22, 444, '*');
        equations[3] = create(2, 4, '/');

        for (int i = 0; i < equations.length; i++){
            System.out.println("Result: " + equations[i].getResult());
        }
    }

    public static MathEquation create(double leftVal, double rightVal, char opCode){

        MathEquation me = new MathEquation(leftVal, rightVal, opCode);
        me.execute();
        return me;
    }
}
