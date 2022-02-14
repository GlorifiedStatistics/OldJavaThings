package juliasets;

import java.awt.Color;
import static juliasets.JuliaSets.say;

public class MandelbrotSet extends JuliaSet{
    public static int[] colorPoints;
    public static Color[] colors;
    

    @Override
    public Complex func(Complex[] points, int index) {
        return points[index].square().add(points[0]);
    }

    @Override
    public Color getColor(Complex[] points, int index) {
        if(points[index].isFinite()){
            return Color.BLACK;
        }
        
        while(index > 400){
            index -= 400;
        }
        
        for(int i =0; i<this.colorPoints.length; i++){
            if(index <= colorPoints[i]){
                return colors[i];
            }
        }
        return Color.PINK;
    }

    @Override
    public boolean stopLooking(Complex[] points, int index) {
        return points[index].DNE()|| index > 1200 
                || (index > 1 && points[index].isCloseTo(points[index-2]))
                || (index > 0 && points[index].isCloseTo(points[index-1]));
    }
    
    public MandelbrotSet(double xmin, double xmax, double ymin, double ymax){
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }
    
}
