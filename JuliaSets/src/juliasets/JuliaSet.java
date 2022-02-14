package juliasets;

import java.awt.Color;
import java.awt.image.BufferedImage;
import static juliasets.JuliaSets.say;

public abstract class JuliaSet {
    //The number of chunks along each edge to split the plane into
    public static int chunks = 800;
    
    //The function to repeat
    public abstract Complex func(Complex[] points, int index);
    
    //The coloring function
    //Returns the color that the given point should be.
    //Points is a collection of values after iterating the function
    //  with the first index being the starting point
    public abstract Color getColor(Complex[] points, int index);
    
    //Determines whether or not we should stop looking at this point
    public abstract boolean stopLooking(Complex[] points, int index);
    
    //The factor to multiply min/max x and y by when making the plot
    public double xmin, xmax, ymin, ymax;
    
    //Actually build the image
    public BufferedImage buildImage(){
        int chunksX = (xmax-xmin) > (ymax-ymin) ? chunks 
                : (int)( ((xmax-xmin)/(ymax-ymin))*chunks );
        int chunksY = (ymax-ymin) > (xmax-xmin) ? chunks 
                : (int)( ((ymax-ymin)/(xmax-xmin))*chunks );
        
        BufferedImage img = new BufferedImage(chunksX, chunksY, BufferedImage.TYPE_INT_RGB);
        
        double incX = (xmax-xmin)/chunksX;
        double incY = (ymax-ymin)/chunksY;
        
        int lookingTime = 0;
        
        for(int i = 0; i<chunksX; i++){
            //midpoint between this and next start of chunk
            double xVal = ((xmin + i*incX) + (xmin + (i+1)*incX))/2;
            for(int j = 0; j<chunksY; j++){
                double yVal = ((ymin + j*incY) +(ymin + (j+1)*incY))/2;
                
                //Starts off at 10 just cause I think we should know whether or not
                //to stop within 10 iterations of the function, but the array will
                //be increased if necessary
                Complex[] points = new Complex[10];
                points[0] = new Complex(xVal, yVal);
                
                //The current index in the points array
                int index = 0;
                while(!stopLooking(points, index)){
                    points = addPoint(func(points, index), points, index);
                    index ++;
                }
                
                lookingTime += index;
                
                img.setRGB(i, j, getColor(points, index).getRGB());
            }
        }
        
        say("Average Looking Time: " + ((double)lookingTime/chunks/chunks));
        
        return img;
    }
    
    //Helper to add points to the array and resize if necessary
    private static Complex[] addPoint(Complex c1, Complex[] points, int index){
        if(index+1 >= points.length){
            Complex[] newPoints = new Complex[points.length*2];
            System.arraycopy(points, 0, newPoints, 0, points.length);
            points = newPoints;
        }
        
        points[index+1] = c1;
        return points;
    }
    
    //Reset the bounds
    public void setBounds(double xmin, double xmax, double ymin, double ymax){
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }
}
