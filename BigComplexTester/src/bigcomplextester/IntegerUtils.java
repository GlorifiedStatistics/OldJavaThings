package bigcomplextester;

import java.math.BigInteger;

/**
 * A collection of utility functions based on integers and BigIntegers
 * @author GlorifiedStatistics
 */
public class IntegerUtils {
    
    /* Some constants */
    public static final BigInteger ZERO = BigInteger.valueOf(0);
    public static final BigInteger ONE = BigInteger.valueOf(1);
    public static final BigInteger TWO = BigInteger.valueOf(2);
    public static final BigInteger THREE = BigInteger.valueOf(3);
    public static final BigInteger FOUR = BigInteger.valueOf(4);
    public static final BigInteger FIVE = BigInteger.valueOf(5);
    public static final BigInteger SIX = BigInteger.valueOf(6);
    public static final BigInteger SEVEN = BigInteger.valueOf(7);
    public static final BigInteger EIGHT = BigInteger.valueOf(8);
    public static final BigInteger NINE = BigInteger.valueOf(9);
    public static final BigInteger TEN = BigInteger.valueOf(10);
    
    /**
     * Calculates the factorial of n (n!) using a method known as binary 
     * splitting, which is much faster than the traditional for-loop
     * multiplication method
     * 
     * This treats the factorials of negative integers an undefined. While
     * analytic continuations of the factorial function that provide meaningful
     * values do exist (such as Hadamard's Gamma Function), I feel it would be
     * confusing (not to mention difficult) to throw in some random analytical
     * continuation when most people take the convention n! is undefined when
     * n < 0
     * 
     * See info on binary splitting here:
     * http://numbers.computation.free.fr/Constants/Algorithms/splitting.html
     * 
     * @param n the number to take factorial of
     * @return n!
     */
    public static BigInteger factorial(long n){
        if(n < 0){
            throw new ArithmeticException("Can not take the factorial of"
                    + "negative numbers");
        }
        return Q(0, n);
    }
    
    /**
     * @return the double factorial of n = n!! = n * (n - 2) * (n - 4) *...
     * @param n the number to take the double factorial of
     */
    public static BigInteger doubleFactorial(int n){
        return multifactorial(n, 2);
    }
    
    /**
     * Returns n * (n - k) * (n - 2k) * (n - 3k) * ... * 1
     * 
     * Assumes k > 0
     * @param n the number to take the k-factorial of
     * @param k the number to skip after each factorial
     * @return the k-factorial of n
     */
    public static BigInteger multifactorial(int n, int k){
        if(n < 0){
            throw new ArithmeticException("Cant take factorial of negative"
                    + "primitive integer");
        }
        
        if(k < 1){
            throw new ArithmeticException("Cant take the multifactorial when"
                    + "k < 1");
        }
        
        BigInteger ret = ONE;
        for(; n > 1; n -= k){
            ret = ret.multiply(BigInteger.valueOf(n));
        }
        
        return ret;
    }
    
    
    /**
     * A helper method for binary splitting of constants and algorithms such
     * as e
     * 
     * Uses a binary splitting algorithm to calculate:
     *      P(a,b) = b(b-1)...(a+2) + b(b-1)...(a+3) + ... + b(b-1) + b + 1
     * 
     * See info on binary splitting here:
     * http://numbers.computation.free.fr/Constants/Algorithms/splitting.html
     * 
     * If a >= b, BigInteger.ONE is returned
     * 
     * @param a the start value
     * @param b the end value
     * @return b(b-1)...(a+2) + b(b-1)...(a+3) + ... + b(b-1) + b + 1  OR 
     *      BigInteger.ONE if a + 1 >= b
     */
    public static BigInteger P(long a, long b){
        if(a + 1 >= b) return ONE;
        long m = (a + b)/2;
        return P(a, m).multiply(Q(m, b)).add(P(m, b));
    }
    
    /**
     * A helper method for binary splitting of constants and algorithms such
     * as e, factorial, 
     * 
     * Uses a binary splitting algorithm to calculate:
     *      Q(a,b) = (a+1)(a+2)...(b-1)(b)
     * 
     * See info on binary splitting here:
     * http://numbers.computation.free.fr/Constants/Algorithms/splitting.html
     * 
     * If a >= b, BigInteger.ONE is returned
     * 
     * @param a the start value
     * @param b the end value
     * @return (a+1)(a+2)...(b-1)(b)  OR  BigInteger.ONE if a >= b
     */
    public static BigInteger Q(long a, long b){
        if(a + 1 == b) return BigInteger.valueOf(b);
        if(a >= b) return ONE;
        long m = (a + b)/2;
        return Q(a, m).multiply(Q(m, b));
    }
    
    
    
    
    
    
    
    
    
    /**********/
    /* Primes */
    /**********/
    
    
    
    
    
    
    
    
    
    /* The current list of primes */
    private static int[] primes = {2,3,5,7,11,13};
    
    /**
     * Returns a list of all of the primes up to a given n. Be careful about
     * how much memory this could use for large n!
     * 
     * Uses the sieve of eratosthenes to calculate these primes, stores them
     * into the array primes to prevent having to calculate again.
     * 
     * @param n the max number (inclusive) to list to
     * @return an array of prime ints up to n
     */
    public static int[] listPrimesUpTo(int n){
        
        /* Check n is nice */
        if(n < 2)return new int[0];
        
        /* The last currently computed prime */
        int lp = primes[primes.length-1];
        
        /* If we have computed enough, return the already computed primes up to n */
        if(n <= lp){
            
            /* Find the index of the closest prime below n */
            int i = 0;
            for(; i<primes.length - 1; i++)
                if(primes[i] <= n && primes[i + 1] > n)break;

            /* Return primes up to that index */
            int[] ret = new int[i + 1];
            System.arraycopy(primes, 0, ret, 0, i + 1);
            return ret;
        }
        
        /* Otherwise compute enough primes */
        /* Make a new sieve */
        int[] sieve = new int[n - lp];
        
        /* Empty all of the already computed primes */
        for(int p : primes){
            
            /* Stop when p > n/2 + 1 */
            if(p > n/2 + 1) break;
            
            /* The starting number for the sieve:
             * the next closest multiple of p after lp - lp */
            int index = p * (lp / p + 1) - lp - 1;
            
            /* Remove all indexes in the sieve that are evenly divisible by p */
            while(index < sieve.length){
                sieve[index] = -1;
                index += p;
            }
            
        }
        
        /* All of the previously calculated primes have removed thier respective
         * indicies in the sieve, now continue through the sieve removing all
         * multiples of the newly found primes */
        for(int i = 0; i<sieve.length/2 + 1; i++){
            if(sieve[i] == -1)continue;
            
            /* The increment is the index + lp */
            int inc = i + lp + 1;
            int index = i + inc;
            while(index < sieve.length){
                sieve[index] = -1;
                index += inc;
            }
            
        }
        
        /* Now all of sieve's indicies that do not have -1 in them are primes
         * (one you add lp to them). Make a new array and set it as the newly
         * calculated primes */
        int count = 0;
        for(int i = 0; i<sieve.length; i++){
            if(sieve[i] != -1)count++;
        }
        
        int[] newPrimes = new int[primes.length + count];
        
        System.arraycopy(primes, 0, newPrimes, 0, primes.length);
        
        int index = primes.length;
        for(int i = 0; i<sieve.length; i++){
            if(sieve[i] != -1){
                newPrimes[index] = i + lp + 1;
                index ++;
            }
        }
        
        primes = newPrimes;
        
        return newPrimes;
    }
    
    /**
     * Returns true if the given n is prime, false otherwise. You only need
     * to check every prime number up to sqrt(n).
     * Negative numbers are never treated as prime, and neither is 1.
     * 
     * @param n the int to check prime-ness
     * @return true if n is prime, false otherwise
     */
    public static boolean isPrime(int n){
        
        /* Check for bad values of n */
        if(n < 2)return false;
        
        for(int p : listPrimesUpTo((int)Math.sqrt(n) + 2)){
            if(p == n) return true;
            if(n % p == 0)return false;
        }
        return true;
    }
}
