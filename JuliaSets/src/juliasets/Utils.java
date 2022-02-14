package juliasets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Utils {

    public static boolean isInteger(double d) {
        return d == Math.floor(d) && Double.isFinite(d);
    }

    public static boolean isInteger(float d) {
        return d == Math.floor(d) && Float.isFinite(d);
    }

    public static double pow(double a, int b) {
        if (b == 0) {
            return 1;
        }
        if (b == 1) {
            return a;
        }
        if (b == 2) {
            return a * a;
        }
        if (b < 0) {
            return 1 / pow(a, -b);
        }

        //Recursively call this function
        int factor = b / 2;
        double ret = pow(a, factor);
        ret = ret * ret;

        //Add the extra in case it is odd
        if (b % 2 != 0) {
            ret = ret * a;
        }

        return ret;
    }

    public static double logBase(double a, double b) {
        if (a <= 0 || a == 1) {
            return Double.NaN;
        }
        return Math.log(b) / Math.log(a);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    
    public static Color[] interpolateOverColors(Color[] colors, int stepsPerColor){
        Color[] ret = new Color[(colors.length-1)*stepsPerColor];
        
        int index = 0;
        for(int i = 0; i<colors.length-1; i++){
            Color first = colors[i];
            Color second = colors[i+1];
            
            Color[] temp = interpolateColor(first, second, stepsPerColor);
            
            for (Color temp1 : temp) {
                ret[index] = temp1;
                index ++;
            }
        }
        
        return ret;
    }
    
    public static Color[] interpolateColor(Color first, Color second, int steps){
        Color[] ret = new Color[steps];
        int incR = second.getRed()-first.getRed();
        int incG = second.getGreen()-first.getGreen();
        int incB = second.getBlue()-first.getBlue();
        
        for(int i = 0; i<steps-1; i++){
            double factor = (double)i/(steps-1);
            Color next = new Color(
                    (int)(first.getRed() + factor*incR),
                    (int)(first.getGreen() + factor*incG),
                    (int)(first.getBlue() + factor*incB)
            );
            ret[i] = next;
        }
        
        ret[steps-1] = second;
        return ret;
    }
}
