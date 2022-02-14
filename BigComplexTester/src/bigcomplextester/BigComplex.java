package bigcomplextester;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * An arbitrarily-accurate complex number. Uses BigDecimal.
 * 
 * @author GlorifiedStatistics
 * @version 1.0
 */
public class BigComplex {
    
    /* The default number of decimal places to be accurate to */
    public static int DEFAULT_ACCURACY = 10;
    
    /* Some default numbers */
    /* Each of these has default accuracy 
    public static final BigComplex ZERO = new BigComplex("0", "0");
    public static final BigComplex ONE = new BigComplex("1", "0");
    public static final BigComplex TWO = new BigComplex("2", "0");
    public static final BigComplex TEN = new BigComplex("10", "0");*/
    public static final BigComplex NAN = new BigComplex("0", "0", -1);
    
    public static BigComplex ONE(int accuracy){
        return new BigComplex("1", "0", accuracy);
    }
    
    public static BigComplex ONE(){
        return ONE(DEFAULT_ACCURACY);
    }
    
    public static BigComplex TWO(int accuracy){
        return new BigComplex("2", "0", accuracy);
    }
    
    public static BigComplex TWO(){
        return TWO(DEFAULT_ACCURACY);
    }
    
    public static BigComplex TEN(int accuracy){
        return new BigComplex("10", "0", accuracy);
    }
    
    public static BigComplex TEN(){
        return TEN(DEFAULT_ACCURACY);
    }
    
    public static BigComplex ZERO(int accuracy){
        return new BigComplex("0", "0", accuracy);
    }
    
    public static BigComplex ZERO(){
        return ZERO(DEFAULT_ACCURACY);
    }
    
    
    
    
    /**********************/
    /* Nicer constructors */
    /**********************/
    
    
    
    /**
     * @param r the real component
     * @return a BigComplex with its real component as r and 0 complex.
     */
    public static BigComplex onlyReal(String r){
        return new BigComplex(r, "0");
    }
    
    /**
     * @param r the real component
     * @return a BigComplex with its real component as r and 0 complex.
     */
    public static BigComplex onlyReal(BigDecimal r){
        return new BigComplex(r, BigDecimal.ZERO);
    }
    
    /**
     * @param c the complex component
     * @return a BigComplex with its real component as 0 and c complex.
     */
    public static BigComplex onlyComplex(String c){
        return new BigComplex("0", c);
    }
    
    /**
     * @param c the complex component
     * @return a BigComplex with its real component as 0 and c complex.
     */
    public static BigComplex onlyComplex(BigDecimal c){
        return new BigComplex(BigDecimal.ZERO, c);
    }
    
    
    
    
    
    /*****************************/
    /*  Non-static final values  */
    /*****************************/
    
    
    
    
    
    public final BigDecimal r; // The real component
    public final BigDecimal c; // The complex component
    public final int accuracy; // The number of decimal places of accuracy. 
                          //  If accuracy < 0, then this BigComplex is NAN
    
    
    
    
    
    
    /******************/
    /*  Constructors  */
    /******************/
    
    
    
    
    
    /**
     * It is highly recommended you use the string or BigDecimal constructors
     * to avoid the floating point error of doubles.
     * 
     * @param r the real component
     * @param c the complex component
     * @param accuracy the number of decimal places of accuracy
     */
    public BigComplex(String r, String c, int accuracy){
        this.accuracy = accuracy;
        this.r = scale(new BigDecimal(r));
        this.c = scale(new BigDecimal(c));
    }
    
    public BigComplex(String r, String c){
        this.accuracy = DEFAULT_ACCURACY;
        this.r = scale(new BigDecimal(r));
        this.c = scale(new BigDecimal(c));
    }
    
    public BigComplex(BigDecimal r, BigDecimal c, int accuracy){
        this.accuracy = accuracy;
        this.r = scale(r);
        this.c = scale(c);
    }
    
    public BigComplex(BigDecimal r, BigDecimal c){
        this.accuracy = DEFAULT_ACCURACY;
        this.r = scale(r);
        this.c = scale(c);
    }
    
    /*
    public BigComplex(double r, double c, int accuracy){
       this.accuracy = accuracy;
       this.r = scale(BigDecimal.valueOf(r));
       this.c = scale(BigDecimal.valueOf(c));
    }
    
    public BigComplex(double r, double c){
        this.accuracy = DEFAULT_ACCURACY;
        this.r = scale(BigDecimal.valueOf(r));
        this.c = scale(BigDecimal.valueOf(c));
    }
    */
    
    /**
     * @return a copy of this BigComplex
     */
    public BigComplex copy(){
        return new BigComplex(r, c, accuracy);
    }
    
    
    
    /****************************/
    /*  Private helper methods  */
    /****************************/
    
    
    
    
    
    
    /** 
     * Helper method to more easily round the values passed in constructors.
     * Rounds HALF_UP
     */
    private BigDecimal scale(BigDecimal d){
        return d.setScale(accuracy, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Helper method that keeps track of the highest accuracy
     * @param b the BigComplex to compare accuracies to 
     * @return Math.max(this.accuracy, b.accuracy)
     */
    private int ac(BigComplex b){
        return Math.max(accuracy, b.accuracy);
    }
    
    
    
    
    
    /**********************************/
    /*  Boolean and string functions  */
    /**********************************/
    
    
    
    
    
    /**
     * @param o the object to test equality
     * @return true if the given BigComplex equals this one.
     */
    @Override
    public boolean equals(Object o){
        /* Check o is a BigComplex */
        if(! (o instanceof BigComplex) ){
            return false;
        }
        
        BigComplex b = (BigComplex)o;
        
        /* Check if both NAN */
        if(accuracy < 0 && b.accuracy < 0) return true;
        
        /* Check if one NAN */
        if( (accuracy < 0 && b.accuracy >= 0) ||
            (accuracy >= 0 && b.accuracy < 0) ) return false;
        
        return r.compareTo(b.r) == 0 && c.compareTo(b.c) == 0;
    }

    /**
     * @return a hash of this object
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.r);
        hash = 41 * hash + Objects.hashCode(this.c);
        hash = 41 * hash + this.accuracy;
        return hash;
    }
    
    /**
     * @return true if this BigComplex is equal to 0
     */
    public boolean isZero(){
        return r.compareTo(BigDecimal.ZERO) == 0
                && c.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * @return true if this BigComplex is equal to 1
     */
    public boolean isOne(){
        return r.compareTo(BigDecimal.ONE) == 0
                && c.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * @return true if there is no complex component
     */
    public boolean isReal(){
        return c.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * @return true if the complex component is not zero
     */
    public boolean isComplex(){
        return !this.isReal();
    }
    
    /**
     * @return the real component
     */
    public BigDecimal real(){
        return this.r;
    }
    
    /**
     * @return the complex component
     */
    public BigDecimal complex(){
        return this.c;
    }
    
    /**
     * @return true if this number is NAN
     */
    public boolean isNAN(){
        return this.accuracy < 0;
    }
    
    /**
     * @return a new BigComplex with only the complex component, and same
     * accuracy
     */
    public BigComplex removeReal(){
        return new BigComplex(BigDecimal.ZERO, c, accuracy);
    }
    
    /**
     * @return a new BigComplex with only the real component, and same
     * accuracy
     */
    public BigComplex removeComplex(){
        return new BigComplex(r, BigDecimal.ZERO, accuracy);
    }
    
    /**
     * Returns "(r, s)". If the size of the string is larger than 80 characters,
     * then it will be formatted as:
     * 
     * (
     * r,
     * c
     * )
     * 
     * Removes any trailing 0's.
     * 
     * @return this BigComplex as a string
     */
    @Override
    public String toString(){
        String rs = "" + r.stripTrailingZeros();
        String cs = "" + c.stripTrailingZeros();
        
        if(rs.length() + cs.length() > 76){
            return "\n(\n" + r + ",\n" + rs + "\n)\n";
        }
        
        return "(" + r + ", " + c + ")";
    }
    
    
    
    
    /***********************/
    /*  Simple Arithemtic  */
    /***********************/
    
    
    
    
    /**
     * @param b the big complex to add
     * @return this + b
     */
    public BigComplex add(BigComplex b){
        if(this.isNAN() || b.isNAN()) return NAN;
        return new BigComplex(r.add(b.r), c.add(b.c), ac(b));
    }
    
    /**
     * @param b the big complex to subtract
     * @return this - b
     */
    public BigComplex subtract(BigComplex b){
        if(this.isNAN() || b.isNAN()) return NAN;
        return new BigComplex(r.subtract(b.r), c.subtract(b.c), ac(b));
    }
    
    /**
     * @param b the big complex to subtract
     * @return this - b
     */
    public BigComplex sub(BigComplex b){
        return this.subtract(b);
    }
    
    /**
     * Couldn't find any increase in speed from checking for whether or not
     * this or b is a real number. BigDecimal must be fast when the value 
     * being multiplied or added is 0.
     * @deprecated 
     * @param b the big complex to multiply by
     * @return this * b
     */
    public BigComplex multiplySLOW(BigComplex b){
        if(this.isNAN() || b.isNAN()) return NAN;
        
        /* If this or b is real, then there is easier multiplication */
        if(this.isReal()){
            return new BigComplex(b.r.multiply(r), b.c.multiply(r), ac(b));
        }
        else if(b.isReal()){
            return new BigComplex(r.multiply(b.r), c.multiply(b.r), ac(b));
        }
        
        /* (a+bi) * (c+di) = ac-bd + (cb+ad)i */
        return new BigComplex( r.multiply(b.r).subtract(c.multiply(b.c)),
                               b.r.multiply(c).add(r.multiply(b.c)), ac(b) );
    }
    
    /**
     * @param b the big complex to multiply by
     * @return this * b
     */
    public BigComplex multiply(BigComplex b){
        if(this.isNAN() || b.isNAN()) return NAN;
        /* (a+bi) * (c+di) = ac-bd + (cb+ad)i */
        return new BigComplex( r.multiply(b.r).subtract(c.multiply(b.c)),
                               b.r.multiply(c).add(r.multiply(b.c)), ac(b) );
    }
    
    /**
     * @param b the big complex to multiply by
     * @return this * b
     */
    public BigComplex mult(BigComplex b){
        return this.multiply(b);
    }
    
    
    /**
     * Returns this / b. If b == 0, return NAN.
     * @param b the BigComplex to divide by
     * @return this / b
     */
    public BigComplex divide(BigComplex b){
        if(this.isNAN() || b.isNAN()) return NAN;
        /* If b is zero, return NAN */
        if(b.isZero()) return NAN;
        
        /* If b is real, divide both r and c by b.r */
        if(b.isReal()){
            return new BigComplex(r.divide(b.r, ac(b), BigDecimal.ROUND_HALF_UP),
                                  c.divide(b.r, ac(b), BigDecimal.ROUND_HALF_UP),
                                  ac(b));
        }
        
        /* Otherwise, (a+bi)/(c+di) = (a+bi)(c-di)/(c^2+d^2) */
        return this.mult(b.conj()).div(
                new BigComplex(b.r.pow(2).add(b.c.pow(2)), BigDecimal.ZERO) );
    }
    
    /**
     * Returns this / b. If b == 0, return NAN.
     * @param b the BigComplex to divide by
     * @return this / b
     */
    public BigComplex div(BigComplex b){
        return this.divide(b);
    }
    
    /**
     * @return the complex conjugate of this BigComplex
     */
    public BigComplex conjugate(){
        if(this.isNAN()) return NAN;
        /* conj(a+bi) = (a-bi) */
        return new BigComplex(r, c.negate(), accuracy);
    }
    
    /**
     * @return the complex conjugate of this BigComplex
     */
    public BigComplex conj(){
        return this.conjugate();
    }
    
    /**
     * @return this * -1
     */
    public BigComplex negate(){
        if(this.isNAN()) return NAN;
        return new BigComplex(r.negate(), c.negate(), accuracy);
    }
    
    /**
     * @return this * -1
     */
    public BigComplex neg(){
        return this.negate();
    }
    
    /**
     * @return this * this
     */
    public BigComplex square(){
        return this.mult(this);
    }
    
    /**
     * @return this * this * this
     */
    public BigComplex cube(){
        return this.mult(this).mult(this);
    }
    
    /**
     * If pow is 0, and this is 0 or NAN, NAN is returned. If pow is 0 and this
     * is any other number, BigComplex.ONE(accuracy) is returned.
     * 
     * If pow is < 0, this returns 1 / this ^ -pow
     * 
     * If pow is 1, this is returned
     * 
     * If pow is small (<=5), then a sinple for loop is used to find the power
     * 
     * If pow is larger (>5), then this program recursively calls itself with
     * smaller values for pow. First, this ^ (long) sqrt(pow) is found. That is
     * then raised to the power (pow / (long) sqrt(pow)). This final value is 
     * multiplied by the remainder this ^ (pow % (long) sqrt(pow)).
     * 
     * @return this^pow
     * @param pow the power to raise to
     */
    public BigComplex pow(long pow){
        if(this.isNAN()) return NAN;
        
        /* If the power is 0, check if this BigComplex is also zero. If it is,
         * return NAN, otherwise 1
         */
        if(pow == 0){
            return (isZero() || isNAN()) ? NAN : ONE(accuracy);
        }
        
        /* If pow < 0, return 1/pow(pow.abs()) */
        if(pow < 0){
            return ONE(accuracy).div(this.pow(Math.abs(pow)));
        }
        
        /* If pow == 1, return this */
        if(pow == 1){
            return this;
        }
        
        /* If pow <= 5, just do a normal return */
        if(pow <= 5){
            BigComplex ret = this;
            for(int i = 1; i < pow; i++){
                ret = ret.mult(this);
            }
            return ret;
        }
        
        /* Otherwise pow is large, do some recursion */
        long div = (long)Math.sqrt(pow);
        long rem = pow % div;
        long val = pow / div;
        return this.pow(div).pow(val).mult(this.pow(rem));
    }
    
    
    
    
    
    /********************/
    /*  Static Methods  */
    /********************/
    
    
    
    
    
    
    
    /**
     * @return the summation of the array of BigComplex values
     * @param vals the values to sum
     */
    public static BigComplex sum(BigComplex[] vals){
        if(vals.length == 0) return NAN;
        
        BigComplex ret = ZERO();
        for (BigComplex val : vals) {
            ret = ret.add(val);
        }
        
        return ret;
    }
    
    
    
}