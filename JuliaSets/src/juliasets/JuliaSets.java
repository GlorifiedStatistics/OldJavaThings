package juliasets;

import java.awt.Color;
import java.util.Arrays;


public class JuliaSets {
    public static double pow = 2.0;
    
    public static void say(Object s){
        if(s instanceof Complex[]){
            for(Complex c : (Complex[])s){
                say(c);
            }
        }
        
        if(s instanceof Color[]){
            for(Color c : (Color[])s){
                say(c);
            }
        }
        
        System.out.println(s);
    }
    
    public static JuliaDisplay disp;
    
    public static void main(String[] args) {
        //disp = new JuliaDisplay(new MandelbrotSet(-2,2,-2,2));
        Color[] colors = {
            new Color(128, 0, 169),
            new Color(0,0,90),
            new Color(0,0,255),
            new Color(0, 204, 255),
            new Color(0, 150, 0),
            new Color(204, 255, 102),
            new Color(255, 204, 0),
            new Color(255, 0, 0),
            new Color(128, 0, 0),
            new Color(255, 0, 102),
            new Color(255, 102, 255)
        };
        
        MandelbrotSet.colors = Utils.interpolateOverColors(colors, 10);
        TestSet.colors = MandelbrotSet.colors;
        
        int[] ints = new int[100];
        for(int i = 0; i<ints.length; i++){
            ints[i] = 4*i;
        }
        MandelbrotSet.colorPoints = ints;
        TestSet.colorPoints = ints;
        
        disp = new JuliaDisplay(new MandelbrotSet(-2,2,-2,2));
            //new Complex(-0.37017, 0.25628), 1.9999999999999974E-6);
        disp.setVisible(true);
    }
    
}
