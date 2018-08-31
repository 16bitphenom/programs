package com.tars.calc;

/**
 * Created by Sudhabindu on 29-Jun-17.
 */
public class MathEquation {

    private char opCode;
    private double leftVal;
    private double rightVal;
    private double result;

    public MathEquation(){
        this.opCode = '+';
    }

    public MathEquation(char opCode){

        this.opCode = opCode;
    }

    public MathEquation(double leftVal, double rightVal, char opCode){

        this(opCode);
        this.leftVal = leftVal;
        this.rightVal = rightVal;
    }

    public double getLeftVal() {
        return leftVal;
    }

    public double getRightVal() {
        return rightVal;
    }

    public double getResult(){
        return result;
    }

    public char getOpCode(){

        return opCode;
    }

    public void execute(){

        switch (opCode){
            case '+':
                result = leftVal + rightVal;
                break;
            case '-':
                result = leftVal - rightVal;
                break;
            case '*':
                result = leftVal * rightVal;
                break;
            case '/':
                result = (rightVal != 0)? leftVal / rightVal : 0.0d;
                break;
            default:
                result = 0.0d;
                break;
        }
    }
}
