package bigcomplextester;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Some utility functions based on BigDecimal
 * @author GlorifiedStatistics
 */
public class BigDecimalUtils {
    
    /* How much to increase the accuracy to ensure correctness to the given
     * accuracy */
    public static final int ACCURACY_INCREASE = 10;
    
    /* Tells BigDecimal to round half-up */
    public static final int ROUND = BigDecimal.ROUND_HALF_UP;
    
    /* BigDecimals with exact values to make code nicer */
    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal TWO = new BigDecimal("2");
    public static final BigDecimal THREE = new BigDecimal("3");
    public static final BigDecimal FOUR = new BigDecimal("4");
    public static final BigDecimal FIVE = new BigDecimal("5");
    public static final BigDecimal SIX = new BigDecimal("6");
    public static final BigDecimal SEVEN = new BigDecimal("7");
    public static final BigDecimal EIGHT = new BigDecimal("8");
    public static final BigDecimal NINE = new BigDecimal("9");
    public static final BigDecimal TEN = BigDecimal.TEN;
    
    
    /* An approximate value for logbase2 of 10 for use in calculations */
    public static final double LOG2_OF_10 = 3.5;
    
    
    /**
     * Returns true if this BigDecimal is actually an integer.
     * Checks the number of digits in b. If this number is greater than
     * 13, then the algorithm for checking integeriness is:
     *      b - (int)b == 0
     * Otherwise, for smaller-digit numbers, the algorithm is:
     *      - strip the trailing zeros from b
     *      - return true if b's scale is <= 0 (check BigDecimal docs for
     *          info on scale)
     * @param b the BigDecimal to check integeriness
     * @return true if b is an integer
     */
    public static boolean isInteger(BigDecimal b){
        if(b.precision() + b.scale() > 13)
            return b.subtract(b.setScale(0, BigDecimal.ROUND_DOWN))
                .compareTo(ZERO) == 0;
        return b.stripTrailingZeros().scale() <= 0;
    }
    
    
    /**
     * Calculates the natural log of the given BigDecimal to the given
     * accuracy. Algorithm found here:
     * https://en.wikipedia.org/wiki/Natural_logarithm#High_precision
     * 
     * @param d the BigDecimal to calculate natural logarithm of
     * @param accuracy the number of decimal places of accuracy
     * @return log(d)
     */
    public static BigDecimal log(BigDecimal d, int accuracy){
        
        /* An accuracy deeper than the given accuracy */
        int biggerAcc = accuracy + ACCURACY_INCREASE;
        
        /* The number of bits of accuracy. biggerAcc will be accurate to
         * 10^(biggerAcc) < 2^(3.5)^(biggerAcc) */
        double p = LOG2_OF_10 * biggerAcc;
        
        /* We want to chose m such that x*2^m > 2^(p/2). This simplifies to
         * m > p/( 2 * logbase2(x) ). To calculate an approximate (within a 
         * couple orders of 2-magnitude) logbase2 of x, we just calculate
         * 3.5 * (x.precision() - x.scale). */
        int m = (int)(p /(2 * LOG2_OF_10 * (d.precision() - d.scale()))) + 1;
        
        BigDecimal b = TWO.multiply(
                agm(ONE, FOUR.divide(d.multiply(TWO.pow(m))), biggerAcc));
        
        return pi(biggerAcc).divide(b)
                .subtract(BigDecimal.valueOf(m).multiply(ln2(biggerAcc)));
    }
    
    /**
     * Calculates the arithmetic-geometric mean between the two number to the 
     * given accuracy. This is used to quickly calculate logs, exponentials, 
     * trig functions, and math constants.
     * 
     * Algorithm: x_new = (x+y)/2 (arithmetic mean), y_new = sqrt(x, y)
     *      (geometric mean). The algorithm is done again with (x_new, y_new)
     *      and this process repeats. x and y are guaranteed to get
     *      progressively closer to each other after each iteration, and after
     *      an infinite number of iterations, they will reach the same number.
     *      This is the value of the function. The number of similar digits
     *      between x and y is guaranteed to increase after each iteration
     *      (approximately doubling each iteration), so the current number of
     *      similar digits is determined to be the current accuracy.
     * 
     * See: https://en.wikipedia.org/wiki/Logarithm#Calculation
     * and: https://en.wikipedia.org/wiki/Arithmetic%E2%80%93geometric_mean
     * 
     * @param x the x of the ag mean
     * @param y the y of the ag mean
     * @param accuracy the number of digits of accuracy past the decimal point
     * @return the arithmetic-geometric mean of x and y = M(x, y)
     */
    public static BigDecimal agm(BigDecimal x, BigDecimal y, int accuracy){
        
        /* Check x and y are good values */
        if(x.compareTo(ZERO) < 0 || y.compareTo(ZERO) < 0)
            throw new ArithmeticException("a-g mean can not be computed with"
                +" negative numbers");
        
        /* An accuracy deeper than the given accuracy for rounding errors */
        int biggerAcc = accuracy + 1;
        
        /* Temporary holder */
        BigDecimal temp;
        
        while(true){
            /* Check if x and y are equal up to accuracy digits */
            if(x.setScale(accuracy, ROUND)
                    .compareTo(y.setScale(accuracy, ROUND)) == 0)
                break;
            
            temp = x.add(y).divide(TWO, biggerAcc, ROUND);
            
            y = sqrt(x.multiply(y).setScale(biggerAcc, ROUND),
                    biggerAcc);
            
            x = temp;
        }
        
        return y.setScale(accuracy, ROUND);
    }
    
    
    /**
     * Calculates the square root of the given BigDecimal d. If d < 0, an
     * exception is thrown.
     * 
     * This algorithm is the Bakhshali method. First an approximation of
     * sqrt(d) called x is calculated to be 10^[ (d.precision() - d.scale())/2 ]
     * We calculate a as follows:
     * a = (d - x^2)/(2d)
     * And our x_new = x + a - a^2/(2b)
     * This x_new is fed back in to the algorithm and the process repeats until
     * the desired accuracy. 
     * 
     * The algorithm is quartically convergent, meaning
     * the number of correct decimal places approximately quadruples with
     * each iteration.
     * 
     * See: https://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Bakhshali_method
     * 
     * All of the other algorithms listed here are either slower, not precise,
     * or hard to implement using BigDecimal without direct access to its array
     * of integers
     * 
     * @param d the BigDecimal to find square root of
     * @param accuracy the number of digits to be accurate to
     * @return the square root of d
     */
    public static BigDecimal sqrt(BigDecimal d, int accuracy){
        
        /* Check for some default values */
        if(d.compareTo(ZERO) == 0 || d.compareTo(ONE) == 0)
            return d.setScale(accuracy, ROUND);
        if(d.compareTo(ZERO) < 0)
            throw new ArithmeticException("D cannot be negative");
        
        /* Rounding errors do not accumulate, no need to increase accuracy */
        BigDecimal x_new = TEN.pow( (d.precision() - d.scale())/2 );
        BigDecimal a, b, x_old;
        
        do{
            x_old = x_new;
            a = d.subtract(x_old.multiply(x_old))
                    .divide(x_old.multiply(TWO), accuracy, ROUND);
            b = x_old.add(a);
            x_new = b.subtract(a.multiply(a)
                    .divide(b.multiply(TWO), accuracy, ROUND));
        }
        /* Do this algorithm until x_new and x_old and x_new have the same
         * value, and thus is accurate to accuracy */
        while(x_new.compareTo(x_old) != 0);
        
        return x_new;
    }
    
    
    /**
     * Calculates the cube root of the given decimal using Newton's method.
     * See here:
     * https://en.wikipedia.org/wiki/Cube_root#Numerical_methods
     * 
     * @param d the BigDecimal to find the cube root of
     * @param accuracy the number of decimal digits of accuracy
     * @return cube root of d to given accuracy
     */
    public static BigDecimal cbrt(BigDecimal d, int accuracy){
        
        /* Check for some default values */
        if(d.compareTo(ZERO) == 0 || d.compareTo(ONE) == 0)
            return d.setScale(accuracy, ROUND);
        if(d.compareTo(ZERO) < 0)
            return cbrt(d.negate(), accuracy).negate();
        
        /* Rounding errors do not accumulate, no need to increase accuracy */
        BigDecimal x_new = TEN.pow( (d.precision() - d.scale())/3 );
        BigDecimal x_old;
        
        do{
            x_old = x_new;
            x_new = x_old.multiply(TWO)
                    .add(d.divide(x_old.multiply(x_old), accuracy, ROUND))
                    .divide(THREE, accuracy, ROUND);
            
        }
        /* Do this algorithm until x_new and x_old and x_new have the same
         * value, and thus is accurate to accuracy */
        while(x_new.compareTo(x_old) != 0);
        
        return x_new;
    }
    
    /**
     * Calculates the nth root of the given decimal using Newton's method.
     * See here:
     * https://en.wikipedia.org/wiki/Nth_root#nth_root_algorithm
     * 
     * @param d the BigDecimal to find the cube root of
     * @param n the root to take
     * @param accuracy the number of decimal digits of accuracy
     * @return cube root of d to given accuracy
     */
    public static BigDecimal nthRoot(BigDecimal d, int n, int accuracy){
        
        /* Some basic checks values are ok */
        if(n < 1)
            throw new ArithmeticException("Cannot take a root less than 1");
        if(d.compareTo(ZERO) < 0)
            if(n % 2 == 0)
                throw new ArithmeticException("Cannot take the even root of "
                    + "a negative number");
            else return nthRoot(d.negate(), n, accuracy).negate();
        
        if(n == 1)
            return d.setScale(accuracy, ROUND);
        if(n == 2)
            return sqrt(d, accuracy);
        if(n == 3)
            return cbrt(d, accuracy);
        
        /* Rounding errors do not accumulate, no need to increase accuracy */
        BigDecimal x_new = TEN.pow( (d.precision() - d.scale())/n );
        BigDecimal x_old;
        BigDecimal dnm1 = BigDecimal.valueOf(n - 1);
        BigDecimal dn = BigDecimal.valueOf(n);
        
        do{
            x_old = x_new;
            x_new = x_old.multiply(dnm1)
                    .add(d.divide(x_old.pow(n - 1), accuracy, ROUND))
                    .divide(dn, accuracy, ROUND);
            
        }
        /* Do this algorithm until x_new and x_old and x_new have the same
         * value, and thus is accurate to accuracy */
        while(x_new.compareTo(x_old) != 0);
        
        return x_new;
    }
    
    
    
    
    
    
    
    
    
    
    /*********************/
    /* Bernoulli Numbers */
    /*********************/
    
    /**
     * Calculates the nth Bernoulli number to the given accuracy
     * 
     * Uses an algorithm found here:
     * https://wstein.org/edu/fall05/168/projects/kevin_mcgown/bernproj.pdf
     * 
     * @param m the index of the Bernoulli number to return
     * @param accuracy the number of decimal places to be accurate to
     * @return B_m to the given accuracy
     */
    public static BigDecimal nthBernoulli(int m, int accuracy){
        
        /* Check if m is a valid index */
        if(m < 0)
            throw new ArithmeticException("There is no negative indexed "
                    + "Bernoulli number! m = " + m);
        
        /* Check if m is small or odd */
        if(m == 0) return ONE;
        if(m == 1) return new BigDecimal("-0.5");
        if(m % 2 == 1) return ZERO;
        
        /* Otherwise normal calculation */
        /* acc is a smaller accuracy used in the intermediate calculations */
        int acc = 100;
        
        /* K := 2m!/(2pi)^m */
        BigDecimal K = TWO.multiply(new BigDecimal(IntegerUtils.factorial(m)))
                .divide(TWO.multiply(pi(acc)).pow(m), acc, ROUND);
        
        /* d := product of all primes p such that p-1 | m */
        int[] primes = IntegerUtils.listPrimesUpTo(m/2 + 1);
        BigDecimal d = ONE;
        for(int p : primes){
            if( m % (p - 1) == 0 )
                d = d.multiply(new BigDecimal(p));
        }
        if(IntegerUtils.isPrime(m + 1))
            d = d.multiply(new BigDecimal(m + 1));
        
        /* N := ceil( (kd) ^ (1 / (m-1)) ) */
        BigDecimal N = nthRoot(K.multiply(d), m - 1, acc)
                .setScale(0, BigDecimal.ROUND_CEILING);
        
        /* z := product over all primes p <= N of (1 - p^(-m)) ^ -1 */
        primes = IntegerUtils.listPrimesUpTo(N.intValue());
        BigDecimal z = ONE;
        for(int p : primes){
            BigDecimal sub = ONE.divide(BigDecimal.valueOf(p).pow(m),
                                            acc, ROUND);
            z = z.multiply(ONE.subtract(sub));
        }
        z = ONE.divide(z, acc, ROUND);
        
        /* a := (-1)^(m/2 + 1) * ceil(dKz) */
        BigDecimal a = d.multiply(K).multiply(z).setScale(0, 
                BigDecimal.ROUND_CEILING);
        if(m % 4 == 0) a = a.negate();
        
        /* Return the new value B_m = a/d */
        return a.divide(d, accuracy, ROUND);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /***************************/
    /* Trigonometric Functions */
    /***************************/
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Changes the given radian value to an equivalent one within [0, 2*pi)
     * with the given accuracy
     * 
     * @param d the BigDecimal in radians to constrain
     * @param accuracy the number of decimal places to be accurate to
     * @return an equivalent radian value to d within [0, 2*pi)
     */
    public static BigDecimal constrainRadian(BigDecimal d, int accuracy){
        BigDecimal twoPi = pi(accuracy).multiply(TWO);
        if(d.compareTo(twoPi) >= 0)
            d = d.subtract(d.divide(twoPi, 0, BigDecimal.ROUND_DOWN)
                    .multiply(twoPi));
        else if(d.compareTo(ZERO) < 0)
            d = d.subtract(d.divide(twoPi, 0, BigDecimal.ROUND_DOWN).subtract(ONE)
                    .multiply(twoPi));
        
        if(d.compareTo(twoPi) == 0)
            return ZERO;
        else return d.setScale(accuracy, ROUND);
    }
    
    
    
    
    
    /**
     * Calculates the sine of the given BigDecimal using the taylor
     * series expansion sin(x) = x - x^3/3! + x^5/5! - x^7/7! + ...
     * 
     * @param d the BigDecimal to calculate sine of
     * @param accuracy the number of decimal digits to be accurate to
     * @return sin(d)
     */
    public static BigDecimal sin(BigDecimal d, int accuracy){
        /* A larger accuracy for rounding errors */
        int biggerAcc = accuracy + 10;
        
        /* Change d to be within [0, 2*pi) */
        d = constrainRadian(d, biggerAcc);
        
        BigDecimal ret = ZERO, add, t = d, b = ONE;
        
        int n = 1;
        do{
            add = t.divide(b, biggerAcc, ROUND);
            
            if(n % 2 == 0)add = add.negate();
            
            ret = ret.add(add);
            
            t = t.multiply(d).multiply(d).setScale(biggerAcc, ROUND);
            b = b.multiply(BigDecimal.valueOf(2*n))
                    .multiply(BigDecimal.valueOf(2*n + 1));
            
            n++;
            
        }while(add.setScale(biggerAcc, ROUND).compareTo(ZERO) != 0);
        
        return ret.setScale(accuracy, ROUND);
    }
    
    /**
     * Calculates the cosine of the given BigDecimal using the taylor
     * series expansion cos(x) = 1 - x^2/2! + x^4/4! - x^6/6! + ...
     * 
     * @param d the BigDecimal to calculate sine of
     * @param accuracy the number of decimal digits to be accurate to
     * @return cos(d)
     */
    public static BigDecimal cos(BigDecimal d, int accuracy){
        
        /* A larger accuracy for rounding errors */
        int biggerAcc = accuracy + 10;
        
        /* Change d to be within [0, 2*pi) */
        d = constrainRadian(d, biggerAcc);
        
        BigDecimal ret = ZERO, add, t = ONE, b = ONE;
        
        int n = 1;
        do{
            add = t.divide(b, biggerAcc, ROUND);
            
            if(n % 2 == 0)add = add.negate();
            
            ret = ret.add(add);
            
            t = t.multiply(d).multiply(d).setScale(biggerAcc, ROUND);
            b = b.multiply(BigDecimal.valueOf(2*n - 1))
                    .multiply(BigDecimal.valueOf(2*n));
            
            n++;
            
        }while(add.setScale(biggerAcc, ROUND).compareTo(ZERO) != 0);
        
        return ret.setScale(accuracy, ROUND);
    }
    
    /**
     * Calculates the tangent of the given BigDecimal. I could not find any
     * power series, or any other way of calculating tangent (other than a
     * maclauren series that requires the use of Bernoulli numbers), so tan
     * is just calculated as sin(d)/cos(d)
     * 
     * @param d the BigDecimal to calculate tangent of
     * @param accuracy the number of decimal places to be accurate to
     * @return tan(d) = sin(d)/cos(d)
     */
    public static BigDecimal tan(BigDecimal d, int accuracy){
        return sin(d, accuracy).divide(cos(d, accuracy), accuracy, ROUND);
    }
    
    
    
    
    
    
    
    /** 
     * Calculates the arc tangent of the given BigDecimal to the given
     * accuracy.
     * 
     * Uses the algorithm found here:
     * https://en.wikipedia.org/wiki/Inverse_trigonometric_functions#Infinite_series
     * 
     * @param d the BigDecimal to find arctan of
     * @param accuracy the number of decimal digits to be accurate to
     * @return arctan(d) 
     */
    public static BigDecimal atan(BigDecimal d, int accuracy){
        
        /* Check if |d| < 0.8, if so, go to atanSmall */
        if(d.abs().compareTo(new BigDecimal("0.9")) < 0){
            return atanSmall(d, accuracy);
        }
        
        /* Check if |d| > 1, if so, return pi/2 - atan(1/x) */
        if(d.abs().compareTo(ONE) > 0){
            return pi(accuracy).divide(TWO)
                    .subtract(atan(ONE.divide(d, accuracy + 2, ROUND), accuracy))
                    .setScale(accuracy, ROUND);
        }
        
        /* A larger accuracy */
        int biggerAcc = accuracy + 10;
        
        BigDecimal ret = ZERO;
        BigDecimal t1, t2, b1, b2, add, nFac = ONE, twoNFac = ONE;
        
        int n = 0;
        
        do{
            t1 = TWO.pow(2*n).multiply(nFac.pow(2));
            b1 = twoNFac;
            t2 = d.pow(2*n + 1);
            b2 = d.pow(2).add(ONE).pow(n + 1);
            
            add = t1.divide(b1, biggerAcc, ROUND)
                    .multiply(t2.divide(b2, biggerAcc, ROUND));
            
            nFac = BigDecimal.valueOf(n+1).multiply(nFac);
            twoNFac = BigDecimal.valueOf(2*n + 3)
                    .multiply(BigDecimal.valueOf(2*n + 2))
                    .multiply(twoNFac);
            
            ret = ret.add(add);
            n++;
        }while(add.setScale(biggerAcc, ROUND).compareTo(ZERO) != 0);
        
        return ret.setScale(accuracy, ROUND);
    }
    
    /**
     * Returns the arc tangent of d, only works when |d| < 1, works best
     * when |d| < 0.8 (value found experimentally)
     * @param d the bigdecimal to find arctan of
     * @param accuracy the number of decimal places to be accurate to
     * @return arctan(d)
     */
    public static BigDecimal atanSmall(BigDecimal d, int accuracy){
        
        /* A larger accuracy */
        int biggerAcc = accuracy + 10;
        
        BigDecimal ret = ZERO, add, t=d, a=ONE;
        
        boolean neg = true;
        
        do{
            add = t.divide(a, biggerAcc, ROUND);
            t = t.multiply(d).multiply(d);
            a = a.add(TWO);
            
            if(neg = !neg)add = add.negate();
            
            ret = ret.add(add);
        }while(add.setScale(biggerAcc, ROUND).compareTo(ZERO) != 0);
        
        return ret.setScale(accuracy, ROUND);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*************************/
    /* Calculating Constants */
    /*************************/
    
    
    
    
    
    
    
    
    
    
    
    
    
    /* Hold currently calculated value of ln 2 and its accuracy */
    private static BigDecimal LN2 = ln2(100);
    private static int ln2_acc;
    
    /**
     * Calculates the natural log of 2 to the given precision
     * 
     * Uses a BBP-type algorithm found here:
     * https://en.wikipedia.org/wiki/Natural_logarithm_of_2#BBP-type_representations
     * 
     * After testing every algorithm on this page (excluding
     * 
     * @param accuracy the number of decimal digits to be accurate to
     * @return ln 2
     */
    public static BigDecimal ln2(int accuracy){
        
        /* Check if we have precalculated enough LN2 */
        if(accuracy <= ln2_acc){
            return LN2.setScale(accuracy, ROUND);
        }
        
        BigDecimal total1 = ZERO, total2 = ZERO, total3 = ZERO;
        int biggerAcc = accuracy + 10;
        int n = 0;
        BigDecimal a, b = ONE, mult1, mult2, mult3;
        
        BigDecimal s = BigDecimal.valueOf(961);
        
        do{
            a = ONE.divide(b.multiply(BigDecimal.valueOf(2*n+1)), biggerAcc, ROUND);
            
            b = b.multiply(s);
            total1 = total1.add(a);
            n++;
        }while(a.setScale(biggerAcc).compareTo(ZERO) != 0);
        
        n = 0;
        b = ONE;
        s = BigDecimal.valueOf(25921);
        do{
            a = ONE.divide(b.multiply(BigDecimal.valueOf(2*n+1)), biggerAcc, ROUND);
            
            b = b.multiply(s);
            total2 = total2.add(a);
            n++;
        }while(a.setScale(biggerAcc).compareTo(ZERO) != 0);
        
        n = 0;
        b = ONE;
        s = BigDecimal.valueOf(2401);
        do{
            a = ONE.divide(b.multiply(BigDecimal.valueOf(2*n+1)), biggerAcc, ROUND);
            
            b = b.multiply(s);
            total3 = total3.add(a);
            n++;
        }while(a.setScale(biggerAcc).compareTo(ZERO) != 0);
        
        mult1 = BigDecimal.valueOf(14)
                .divide(BigDecimal.valueOf(31), biggerAcc, ROUND);
        mult2 = BigDecimal.valueOf(6)
                .divide(BigDecimal.valueOf(161), biggerAcc, ROUND);
        mult3 = BigDecimal.valueOf(10)
                .divide(BigDecimal.valueOf(49), biggerAcc, ROUND);
        
        LN2 = total1.multiply(mult1)
                .add(total2.multiply(mult2))
                .add(total3.multiply(mult3));
        ln2_acc = accuracy;
        
        return LN2.setScale(accuracy, ROUND);
    }
    
    
    
    
    
    
    
    /* Variables to hold currently calculated values of e */
    private static BigInteger e_P = BigInteger.ZERO, e_Q = BigInteger.ONE;
    private static BigDecimal e_e;
    private static int e_lastK = 0, e_acc = 0;
    
    /**
     * Returns the mathematical constant e (Euler's Number) accurate to the
     * given number of decimal places.
     * 
     * Uses a binary splitting algorithm to quickly calculate e based on the
     * well-known formula
     *      e = sum( 1 / k! )  for k = 0 -> infinity
     * 
     * See info on binary splitting here:
     * http://numbers.computation.free.fr/Constants/Algorithms/splitting.html
     * 
     * Because of the binary splitting algorithm, we can calculate e as a final
     * ratio of two integers, thus only one division is needed at the end. This
     * means we can keep track of our previous calculations, expanding on them
     * instead of needing to recalculate everything for any call where the new
     * accuracy is larger than the previously computed accuracy.
     * 
     * @param accuracy the number of decimal places of accuracy
     * @return e to the given accuracy
     */
    public static BigDecimal e(int accuracy){
        /* If accuracy is negative, recalculate everything */
        if(accuracy < 0){
            e_lastK = 0;
            e_acc = 0;
            return e(-accuracy);
        }
        
        /* If we have precomputed enough already */
        if(accuracy <= e_acc)
            return e_e.setScale(accuracy, ROUND);
        
        /* Otherwise we must compute more, Find the correct K value */
        int K = Math.max(e_lastK, (int) (accuracy / Math.log(accuracy)));
        BigInteger factVal = e_Q.multiply(IntegerUtils.Q(e_lastK, K));
        while(factVal.bitLength() < 3.5 * accuracy){
            factVal = factVal.multiply(IntegerUtils.Q(K, 2*K));
            K *= 2;
        }
        
        /* If the this K is smaller than the last K, then recalculate e_e to 
           this new accuracy and return */
        if(K <= e_lastK){
            e_e = ONE.add(new BigDecimal(e_P)
                    .divide(new BigDecimal(e_Q), accuracy, ROUND));
            return e_e.setScale(accuracy, ROUND);
        }
        
        /* Can save this value, Q already computed */
        e_Q = factVal;
        
        /* Make and store the new value for P */
        e_P = e_P.multiply(IntegerUtils.Q(e_lastK, K))
                .add(IntegerUtils.P(e_lastK, K));
        
        /* Store new last_K value */
        e_lastK = K;
        
        /* Make e_e = 1 + e_P/e_Q and store */
        e_e = ONE.add(new BigDecimal(e_P).divide(
                                    new BigDecimal(e_Q), accuracy, ROUND));
        
        return e_e;
    }
    
    /**
     * Returns Euler's constant e to the given accuracy
     * 
     * Uses a compressed version of the well known infinite sum of e
     * found here:
     * http://www.brotherstechnology.com/docs/icnsae_(cmj0104-300dpi).pdf
     * 
     * Also saves the calculated value of e to reduce future calculations
     * 
     * @param accuracy the number of decimal places to be accurate to
     * @return e to the given accuracy
     * @deprecated This function is deprecated because the other method is 
     * much faster than this one. The only reason I keep this method is as a
     * remembrance to the time I spent coming up with this algorithm. This is
     * essentially a much slower, and more complex version of the binary splitting
     * algorithm used in calculating e. This is life I guess? You spend hours
     * coming up with a cool new algorithm all by yourself only to find out 
     * others have thought of your idea long ago, and made it much faster,
     * simpler, and all around better looking.
     */
//    public static BigDecimal e(int accuracy){
//        
//        /* Check we have computed enough values */
//        if(e_acc >= accuracy){
//            return e.setScale(accuracy, ROUND);
//        }
//        
//        /* Otherwise calculate e until b != 0 */
//        int biggerAcc = accuracy + 20;
//        
//        /* The degree of polynomial to use for calculating */
//        int p = Math.max(biggerAcc/10, 2);
//        if(biggerAcc > 400000)p = biggerAcc/100;
//        
//        BigDecimal b = ONE, ret = ONE, add;
//        
//        long k = 1;
//        
//        do{
//            long t = p * k;
//            for(int i = 0; i<p; i+=2)
//                b = b.multiply(new BigDecimal( (t-i) * (t-i-1) ));
//            add = e_num(p, k).divide(b, biggerAcc, ROUND);
//            
//            ret = ret.add(add);
//            k ++;
//        }while(add.compareTo(ZERO) > 0);
//        
//        e = ret;
//        e_acc = accuracy;
//        
//        return ret.setScale(accuracy, ROUND);
//    }
    
    
    /**
     * A helper method to calculate the numerator of the fraction when
     * calculating e. This formula was derived by myself based on the 
     * work in this paper:
     * http://www.brotherstechnology.com/docs/icnsae_(cmj0104-300dpi).pdf
     * 
     * ASSUMES p IS EVEN > 0
     * 
     * @param p the TC to use
     * @param k the current value of n while calculating e
     * @return the numerator of the fraction
     * @deprecated See the old deprecated e
     */
//    private static BigDecimal e_num(int p, long k){
//        BigDecimal ret = ZERO;
//        long t = p * k;
//        
//        ret = ret.add(new BigDecimal(t + 1));
//            
//        BigDecimal curr = ONE;
//        for(int i = 0; i<p/2-1; i++){
//            int d = 2 * i;
//            curr = curr.multiply(new BigDecimal( (t-d) * (t-d-1) ));
//            ret = ret.add(curr.multiply(new BigDecimal(t-d-1)));
//        }
//            
//        return ret;
//    }
    
    
    
    
    
    
    /******************/
    /* Calculating pi */
    /******************/
    
    
    
    /*****************************************************
    *                                                    *
    *               3.141592653589793238462643383279     *
    *         5028841971693993751058209749445923         *
    *        07816406286208998628034825342117067         *
    *        9821    48086         5132                  *
    *       823      06647        09384                  *
    *      46        09550        58223                  *
    *      17        25359        4081                   *
    *                2848         1117                   *
    *                4502         8410                   *
    *                2701         9385                   *
    *               21105        55964                   *
    *               46229        48954                   *
    *               9303         81964                   *
    *               4288         10975                   *
    *              66593         34461                   *
    *             284756         48233                   *
    *             78678          31652        71         *
    *            2019091         456485       66         *
    *           9234603           48610454326648         *
    *          2133936            0726024914127          *
    *          3724587             00660631558           *
    *          817488               152092096            *
    *                                                    *
    ******************************************************/
    
    
    /* Keep track of current pi and its accuracy */
    public static BigDecimal PI = pi(100);
    private static int pi_acc = 0;
    
    
    /**
     * Calculates pi to the given accuracy
     * Uses the quadratic convergence (found to be the fastest) here:
     * https://en.wikipedia.org/wiki/Borwein%27s_algorithm#Iterative_algoithms
     * 
     * @param accuracy the number of decimal places to be accurate to
     * @return pi to the given accuracy
     */
    public static BigDecimal pi(int accuracy){
        
        /* Check if we already have enough pi computed */
        if(accuracy <= pi_acc)
            return PI.setScale(accuracy, ROUND);
        
        int biggerAcc = accuracy + 10;
        
        BigDecimal a = sqrt(TWO, biggerAcc);
        BigDecimal b = ZERO;
        BigDecimal p = TWO.add(a);
        
        BigDecimal old_p, ta, root_a;
        
        do{
            old_p = p;
            
            root_a = sqrt(a, biggerAcc);
            ta = root_a.add(ONE.divide(root_a, biggerAcc, ROUND))
                    .divide(TWO, biggerAcc, ROUND);
            b = ONE.add(b).multiply(root_a).divide(a.add(b), biggerAcc, ROUND);
            a = ta;
                    
            p = ONE.add(a).multiply(p).multiply(b)
                    .divide(ONE.add(b), biggerAcc, ROUND);
        }while(old_p.setScale(accuracy, ROUND)
                .compareTo(p.setScale(accuracy, ROUND)) != 0);
        
        /* Save new value of pi */
        PI = p.setScale(accuracy, ROUND);
        pi_acc = accuracy;
        return PI;
    }
    
    
}
