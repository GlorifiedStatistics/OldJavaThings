package neuralnetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Util {
    //the true and false values of BiPolarUtil
    public static double TRUE = 1;
    public static double FALSE = -1;

    
    
    /////////////////////////////////////////////////
    //                Converters                   //
    /////////////////////////////////////////////////
    //<editor-fold defaultstate="collapsed" desc="booleanToBipolar">
    /**
     * Returns the bipolar representation of @vals. False is now -1, and True is
     * now 1.
     *
     * @param vals the values to convert.
     * @return The bipolar representation of @vals.
     */
    public static double[] booleanToBipolar(boolean[] vals) {
        double[] ret = new double[vals.length];
        for (int i = 0; i < vals.length; i++) {
            if (vals[i]) {
                ret[i] = TRUE;
            } else {
                ret[i] = FALSE;
            }
        }
        return ret;
    }
    //</editor-fold>
    

    //<editor-fold defaultstate="collapsed" desc="bipolarToBoolean">
    /**
     * Returns the boolean representation of @vals. False is now 0, and True is
     * 1.
     * <p>
     * If the values in @vals do not match @TRUE or @FALSE, then @FALSE is
     * automatically assumed, and an error is shown.
     *
     * @param vals the values to convert.
     * @return The boolean representation of @vals.
     */
    public static boolean[] bipolarToBoolean(double[] vals) {
        boolean[] ret = new boolean[vals.length];
        for (int i = 0; i < vals.length; i++) {
            if (vals[i] == TRUE) {
                ret[i] = true;
            } else if (vals[i] == FALSE) {
                ret[i] = false;
            } else {
                NeuralNetwork.say("Cannot determine true or false of: " + vals[i] + ". False: " + FALSE + ", True: " + TRUE);
                ret[i] = false;
            }
        }
        return ret;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="booleanToDouble">
    /**
     * Returns the double representation of @vals. False is 0, and True is 1.
     * <p>
     * If the values in @vals do not match @TRUE or @FALSE, then @FALSE is
     * automatically assumed, and an error is shown.
     *
     * @param vals the values to convert.
     * @return The boolean representation of @vals.
     */
    public static double[] booleanToDouble(boolean[] vals) {
        double[] ret = new double[vals.length];
        for (int i = 0; i < vals.length; i++) {
            if (vals[i]) {
                ret[i] = 1;
            } else {
                ret[i] = 0;
            }
        }
        return ret;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="doubleToBipolar">
    /**
     * Returns The BiPolar representation of @val.
     * <p>
     * If @val is not 1 or 0, an error is shown, and False is assumed.
     *
     * @param val the value to convert.
     * @return The BiPolar representation of @val.
     */
    public static double doubleToBipolar(double val) {
        if (val == 1) {
            return 1;
        } else {
            return -1;
        }
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="euclideanToBaseary">
    /**
     * Converts the inputed Matrix of points into Base-ary
     *
     * @param points the Matrix of points
     * @return The converted points as a String array
     */
    public static String[] euclideanToBaseary(Matrix points) {
        ArrayList<String> ret = new ArrayList<>();
        for(int i = 0; i<points.getRows(); i++){
            String r = "";
            for(int j = 0; j<points.getCols(); j++){
                
                //if r1 is positive, multiply by 2
                //if r1 is negative, multiply by -2 and subtract 1
                int r1 = (int)points.get(i, j);
                if(r1>=0){
                    r1 *= 2;
                }else{
                    r1 = r1*-2-1;
                }
                if(r1>9||r1<-9)NeuralNetwork.say("R1 is not within base-ary bounds, add in hexadeci-decimal!");
                r = r+r1;
                
            }
            ret.add(r);
        }
        String[] ret2 = new String[ret.size()];
        for(int i = 0; i<ret.size(); i++){
            ret2[i] = ret.get(i);
        }
        return ret2;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="basearyToBipoints">
    /**
     * Converts the String array of Baseary points to a single bipoints String.
     * 
     * @param points the points to convert
     * @return The Bipoints String
     */
    public static String basearyToBipoints(String[] points){
        String ret = "";
        int max = NeuralNetwork.BASE*NeuralNetwork.BASE*NeuralNetwork.BASE;
        for(int i = 0; i<max; i++){
            if(NeuralNetwork.act==FeedforwardNetwork.TANH){
                ret = ret+"-1";
            }else{
                ret = ret+"0";
            }
        }
        
        for(String s : points){
            int index = 1;
            int total = 0;
            for(int i = s.length()-1; i>=0; i--){
                int si = Integer.parseInt(s.charAt(i)+"");
                total += si*index;
                index *= NeuralNetwork.BASE;
            }
            try{
            ret = ret.substring(0, total)+
                    "1"+ret.substring(total+1);
            }catch(Exception e){
                int i = 0;
            }
        }
        return ret;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="euclideanToBipoints">
    /**
     * Returns the bipoints representation of the euclidean CubePiece.
     * 
     * @param m the Matrix of points
     * @return the Bipoints representation of the euclidean CubePiece.
     */
    public static String euclideanToBipoints(Matrix m){
        return basearyToBipoints(euclideanToBaseary(m));
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="doubleToBoolean">
    /**
     * Returns true if the input is greater than or equal to 0.5.
     * 
     * @param val the value to convert
     * @return True if the input is greater than or equal to 0.5.
     */
    public static boolean doubleToBoolean(double val){
        return val>=0.5;
    }
    //</editor-fold>
    
        
    //<editor-fold defaultstate="collapsed" desc="stringToDoubles">
    /**
     * Converts the String to a double array.
     * 
     * @param s the String to convert
     * @return The String as a double array.
     */
    public static double[] stringToDoubles(String s) {
        double[] ret = new double[s.length()];
        for (int i = 0; i < s.length(); i++) {
            ret[i] = Double.parseDouble(s.charAt(i) + "");
        }
        return ret;
    }
    //</editor-fold>
    
    
    /**
     * Converts the integer @in in base @base to a double array of Euclidean points.
     * 
     * @param in the integer to convert
     * @param base the base of the integer
     * @return A double array of three euclidean points.
     */
    public static double[] basearyToEuclidean(int in, int base){
        double[] ret = new double[3];
        return null;
    }
    
    
    /**
     * Converts the String of bipoints to a Matrix of points in Euclidean space.
     * <p>
     * The string of bipoints should be only one CubePiece long, and should not have the
     * true/false bit at the end.
     * 
     * @param points the points to convert
     * @param base the base to convert in
     * @return A matrix representation of the bipoints @points.
     */
    public static Matrix bipointsToEuclidean(String points, int base){
        int length = 0;
        for(char c : points.toCharArray()){
            if(c=='1')length++;
        }
        Matrix ret = new Matrix(length,3);
        ArrayList<Integer> baseary = new ArrayList<>();
        
        
        for(int i = 0; i<length; i++){
            if(points.charAt(i)=='1'){
                baseary.add(i);
            }
        }
        
        for(int i = 0; i<ret.getRows(); i++){
            ret.setRow(i,basearyToEuclidean(baseary.get(i),base));
        }
        
        return ret;
    }
    
    
    /**
     * Converts the String of BiPoints (including the true/false bit at the end) to an array of two CubePieces
     * <p>
     * The string of bipoints should be the total testing String: it should have two CubePieces, and a 
     * true/false bit at the end.
     * 
     * @param points the biPoints to convert
     * @return An array of CubePieces of size 2.
     */
    public static CubePiece[] bipointsToCubePieces(String points){
        int base = (int)Math.cbrt(points.length()-1);
        String piece1 = points.substring(0, base);
        String piece2 = points.substring(base, points.length()-1);
        CubePiece[] ret = new CubePiece[2];
        ret[0] = new CubePiece(bipointsToEuclidean(piece1,base));
        ret[1] = new CubePiece(bipointsToEuclidean(piece2,base));
        return ret;
    }
    
    
    
    /**
     * Shuffles the data randomly and returns it
     * 
     * @param vals the values to shuffle
     * @return the randomly shuffled values
     */
    public static double[][] shuffleData(double[][] vals){
        ArrayList<double[]> data = new ArrayList<>();
        data.addAll(Arrays.asList(vals));
        ArrayList<double[]> retA = new ArrayList<>();
        for(int i = data.size(); i>0; i--){
            retA.add(data.remove(i-1));
        }
        double[][] ret = vals;
        for(int i = 0; i<retA.size(); i++){
            ret[i] = retA.get(i);
        }
        return ret;
    }
    
    
    
    
    
    
    
    /////////////////////////////////////////////////
    //                  Getters                    //
    /////////////////////////////////////////////////
    //<editor-fold defaultstate="collapsed" desc="getCubes">
    /**
     * Reads in the data from the file NeuralNetwork.f and returns it as a double array
     * 
     * @return The data as a double array.
     */
    public static double[][] getCubes() {
        ArrayList<double[]> reta = new ArrayList<>();
        File in = new File(NeuralNetwork.home + "\\BASE"+NeuralNetwork.BASE+" CUBEPIECES.txt");
        try (BufferedReader read = new BufferedReader(new FileReader(in))) {
            String line;
            while ((line = read.readLine()) != null) {
                reta.add(stringToDoubles(line));
            }
        } catch (IOException e) {
            NeuralNetwork.say("ERROR EXCEPTION THINGY");
            e.printStackTrace();
        }
        double[][] ret = new double[reta.size()][reta.get(0).length];
        for(int i = 0; i<reta.size(); i++){
            ret[i] = reta.get(i);
        }
        return ret;
    }
    //</editor-fold>
    
    
    
    /**
     * Returns a Matrix array loaded from File @f.
     * 
     * @param f the File to load the Matrices from 
     * @return A Matrix array loaded from File @f.
     */
    public static Matrix[] loadMatrices(File f){
        //reads in the file
        ArrayList<String> lines = new ArrayList<>();
        try{
            BufferedReader read = new BufferedReader(new FileReader(f));
            String line;
            while( (line=read.readLine()) != null){
                lines.add(line);
            }
        }catch(IOException e){}
        
        //builds the matrices
        ArrayList<Matrix> mats = new ArrayList<>();
        int width = lines.get(0).split(",").length;
        if(width==0)width++;
        Matrix m = new Matrix(1,width);
        for (int i = 0; i<lines.size(); i++) {
            if(lines.get(i).equals("*")){
                m.removeRow(0);
                mats.add(m);
                width = lines.get(i+1).split(",").length;
                if(width==0)width++;
                m = new Matrix(1,width);
            }else{
                //adds each row to the Matrix
                double[] d;
                if(lines.get(i).contains(",")){
                    String[] data = lines.get(i).split(",");
                    d = new double[data.length];
                    for(int j = 0; j<data.length; j++){
                        d[j] = Double.parseDouble(data[j]);
                    }
                }else{
                    d = new double[1];
                    d[0] = Double.parseDouble(lines.get(i));
                }
                m.addRow(new Matrix(d,Matrix.ROW), m.getRows()); 
            }
        }
        m.removeRow(0);
        mats.add(m);
        
        Matrix[] ret = new Matrix[mats.size()];
        for(int i = 0; i<mats.size(); i++){
            ret[i] = mats.get(i);
        }
        return ret;
    }
    
    
    
    
    
    /**
     * Generates a list of CubePiece training data based off of the variables 
     * listed at the top of the NeuralNetwork class and saves this data to a file on the Desktop.
     */
    public static void generateCubeData() {
        NeuralNetwork.say("Generating random cube data.");
        NeuralNetwork.cubeDump.delete();
        try {
            NeuralNetwork.cubeDump.createNewFile();
        } catch (IOException e) {
            NeuralNetwork.say("ERROS");
        }
        ArrayList<String> r = new ArrayList<>();

        //size: 1
        CubePiece.generateCubeData(1, 1, true, true).stream().forEach((s) -> {
            r.add(s);
        });
        NeuralNetwork.say("Done generating size: 1");

        //size: 2
        CubePiece.generateCubeData(5, 2, true, true).stream().forEach((s) -> {
            r.add(s);
        });
        NeuralNetwork.say("Done generating size: 2");

        //size: 3
        CubePiece.generateCubeData(15, 3, true, true).stream().forEach((s) -> {
            r.add(s);
        });
        CubePiece.generateCubeData(15, 3, true, false).stream().forEach((s) -> {
            r.add(s);
        });
        NeuralNetwork.say("Done generating size: 3");

        //size: 3+
        for (int i = 4; i <= NeuralNetwork.BASE * NeuralNetwork.BASE; i++) {
            for (String s : CubePiece.generateCubeData(NeuralNetwork.minorCube, i, true, false)) {
                r.add(s);
            }
            for (String s : CubePiece.generateCubeData(NeuralNetwork.minorCube, i, true, true)) {
                r.add(s);
            }
            NeuralNetwork.say("Done generating size: " + i);
        }

        //differing size
        CubePiece.generateCubeData(NeuralNetwork.majorCube, 1, false, false).stream().forEach((s) -> {
            r.add(s);
        });
        NeuralNetwork.say("Done generating random sizes.");

        BufferedWriter write;
        try {
            write = new BufferedWriter(new FileWriter(NeuralNetwork.cubeDump));
            for (String s : r) {
                write.write(s);
                write.newLine();
            }
            write.flush();
            NeuralNetwork.say("Successfully created and saved "+r.size()+" CubePieces.");
        } catch (IOException e) {
            NeuralNetwork.say("ERRORTHING");
        }
    }
}
