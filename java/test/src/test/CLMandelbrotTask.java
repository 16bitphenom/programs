package test;

/**
 * Created by Sudhabindu on 01-Jul-17.
 */
public class CLMandelbrotTask {

    public int id;
    public int maxIterations;
    public double y;
    public double xmin;
    public double dx;
    public int count;
    public int[] results;

    public void compute(){

        results = new int[count];
        for(int i = 0; i < count; i++){
            results[i] = countIterations(xmin + i * dx, y);
        }
    }

    public int countIterations(double startX, double startY){

        int ct = 0;
        double x = startX;
        double y = startY;
        while(ct < maxIterations && x * x + y * y < 5){
            double newX = x * x - y * y + startX;
            y = 2 * x * y + startY;
            x = newX;
            ct++;
        }
        return ct;
    }
}
