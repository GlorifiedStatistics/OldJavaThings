package bigcomplextester;

import java.math.BigDecimal;
import java.util.Random;

/**
 * A collection of utility functions involving BigComplex numbers
 * @author GlorifiedStatistics
 */
public class BigComplexUtils {
    
    private static Random rand = new Random();
    
    /**
     * Seed the RNG
     * @param seed the seed
     */
    public static void seedRNG(long seed){
        rand.setSeed(seed);
    }
    
    /**
     * @return a BigComplex array of random complex numbers
     * @param minr the minimum real component
     * @param maxr the maximum real component
     * @param minc the minimum complex component
     * @param maxc the maximum complex component
     * @param accuracy the number of decimal places to go down to
     * @param n the number of elements to return
     */
    public static BigComplex[] randomBigComplex(BigDecimal minr, BigDecimal maxr,
            BigDecimal minc, BigDecimal maxc, int accuracy, int n){
        
        /* Check n is nice */
        if(n <= 0){
            throw new IllegalArgumentException("n must be > 0. n: " + n);
        }
        
        BigComplex[] ret = new BigComplex[n];
        for(int i = 0; i<n; i++){
            ret[i] = randomBigComplex(minr, maxr, minc, maxc, accuracy);
        }
        return ret;
    }
    
    /**
     * @return a random BigComplex number within the given bounds
     * @param minr the minimum real component
     * @param maxr the maximum real component
     * @param minc the minimum complex component
     * @param maxc the maximum complex component
     * @param accuracy the number of decimal places to go down to
     */
    public static BigComplex randomBigComplex(BigDecimal minr, BigDecimal maxr,
            BigDecimal minc, BigDecimal maxc, int accuracy){
        
        /* Set the accuracies of the BigDecimals */
        minr = minr.setScale(accuracy);
        maxr = maxr.setScale(accuracy);
        minc = minc.setScale(accuracy);
        maxc = maxc.setScale(accuracy);
        
        /* Check to make sure accuracy is nice */
        if(accuracy < 1){
            throw new IllegalArgumentException(
                    "Accuracy must be >= 0. Accuracy: " + accuracy);
        }
        
        /* Check minr, maxr, minc, and maxc are nice */
        else if(minr.compareTo(maxr) > 0){
            throw new IllegalArgumentException("minr must be <= maxr");
        }
        else if(minc.compareTo(maxc) > 0){
            throw new IllegalArgumentException("minc must be <= maxc");
        }
        
        /* The size of the random string */
        int rs1 = minr.precision() - minr.scale();
        int rs2 = maxr.precision() - maxr.scale();
        int cs1 = minc.precision() - minc.scale();
        int cs2 = maxc.precision() - maxc.scale();
        
        /* Generate random string of characters */
        String rString = randomNumbers(Math.max(rs1, rs2) + accuracy);
        String cString = randomNumbers(Math.max(cs1, cs2) + accuracy);
            
        /* Make the BigComplex boi */
        BigDecimal r = new BigDecimal("0." + rString);
        r = minr.add( (maxr.subtract(minr)).multiply(r) );
           
        BigDecimal c = new BigDecimal("0." + cString);
        c = minc.add( (maxc.subtract(minc)).multiply(c) );
            
        return new BigComplex(r, c, accuracy);
    }
    
    /**
     * @return a string of uniform random numbers from 0-9
     * @param size the size of the string
     */
    public static String randomNumbers(int size){
        String ret = "";
        for(int i = 0; i<size; i++){
            ret += rand.nextInt(10);
        }
        return ret;
    }
    
}
