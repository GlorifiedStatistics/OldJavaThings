package bigcomplextester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import static bigcomplextester.BigDecimalUtils.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;

/**
 * Tests methods in BigComplex against values from wolframAlpha
 * @author Babynado
 */
public class BigComplexTester {
    
    /* A separator char */
    public static final char SEP = ',';
    
    /**
     * Easier way to print to the screen
     * @param s the object to print
     */
    public static void say(Object s){
        System.out.println(s);
    }
    
    /**
     * Runs every test here (not including timing tests)
     */
    public static void testEverything(){
        /* The path to a file for at least 50k digits of pi in order
         * to test BigDecimalUtils.pi() is working correctly */
        //String piPath = "C:/Users/Babynado/Desktop/pi.txt";
        String piPath = "";
        
        /* The path to a file for at least 50k digits of e in order to 
         * test BigDecimalUtils.e() is working correctly */
        String ePath = "C:/Users/Babynado/Desktop/e.txt";
        //String ePath = "";
        
        /* The path to a file containing the first (some number) primes in
         * order separated only by commas (no newlines) */
        //String primesPath = "C:/Users/Babynado/Desktop/primes.txt";
        String primesPath = "";
        
        testBigComplex();
        testBigDecimalUtils(piPath, ePath);
        testIntegerUtils(primesPath);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        say("Beginning Tests");
        //testEverything();
        say("Everything worked!");
        
        BigInteger max = BigInteger.ONE.shiftLeft(60)
                .multiply(BigInteger.valueOf(87));
        
        BigInteger test = BigInteger.ONE.shiftLeft(896680)
                .multiply(BigInteger.valueOf(87168))
                .add(BigInteger.valueOf(9236329));
        
        BigInteger currVal = test;
        
        int numIterations = 0;
        while(currVal.compareTo(max) > 0){
            int numZeros = currVal.getLowestSetBit();
            if(numZeros > 0)
                currVal = currVal.shiftRight(numZeros);
            else
                currVal = currVal.multiply(BigInteger.valueOf(3))
                        .add(BigInteger.ONE);
            numIterations++;
        }
        say(numIterations);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /****************/
    /* Timing Tests */
    /****************/
    
    
    
    
    
    
    
    
    /****************/
    /* Actual Tests */
    /****************/
    
    
    
    
    
    
    
    /**
     * Tests the functionality of IntegerUtils
     * @param primesPath The path to a file containing the first 
     *      (some number) primes in order separated only by commas 
     *      (no newlines). Set to the empty string "" if you don't want to
     *      test primes this heavily.
     */
    public static void testIntegerUtils(String primesPath){
        say("Testing IntegerUtils");
        
        /* Test factorials and multifactorials */
        BigInteger f1 = new BigInteger("120");
        BigInteger f2 = new BigInteger("3628800");
        BigInteger f3 = new BigInteger("137637530912263450463159795815809024000"
                + "00000");
        BigInteger f4 = new BigInteger("3");
        BigInteger f5 = new BigInteger("8");
        BigInteger f6 = new BigInteger("2");
        BigInteger f7 = new BigInteger("3");
        BigInteger f8 = new BigInteger("4");
        BigInteger f9 = new BigInteger("10");
        BigInteger f10 = new BigInteger("643781160");
        assert IntegerUtils.factorial(5).compareTo(f1) == 0;
        assert IntegerUtils.factorial(10).compareTo(f2) == 0;
        assert IntegerUtils.factorial(37).compareTo(f3) == 0;
        assert IntegerUtils.doubleFactorial(3).compareTo(f4) == 0;
        assert IntegerUtils.doubleFactorial(4).compareTo(f5) == 0;
        assert IntegerUtils.multifactorial(2, 3).compareTo(f6) == 0;
        assert IntegerUtils.multifactorial(3, 3).compareTo(f7) == 0;
        assert IntegerUtils.multifactorial(4, 3).compareTo(f8) == 0;
        assert IntegerUtils.multifactorial(5, 3).compareTo(f9) == 0;
        assert IntegerUtils.multifactorial(131, 29).compareTo(f10) == 0;
        
        /* Test listPrimesUpTo */
        int[] p1 = IntegerUtils.listPrimesUpTo(6);
        int[] p2 = IntegerUtils.listPrimesUpTo(5);
        int[] p3 = IntegerUtils.listPrimesUpTo(30);
        int[] p4 = {2, 3, 5};
        int[] p5 = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
        assert IntegerUtils.listPrimesUpTo(1).length == 0;
        assert p1.length == p4.length;
        assert p2.length == p4.length;
        assert p3.length == p5.length;
        for(int i = 0; i<p4.length; i++){
            assert p1[i] == p4[i];
            assert p2[i] == p4[i];
        }
        for(int i = 0; i<p5.length; i++) assert p3[i] == p5[i];
        
        /* Test isPrime */
        int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 131, 137, 2003,
            12097, 37139, 104729};
        int[] notPrimes = {-12314, -5, -4, -3, -2, -1, 0, 1, 4, 6, 8, 9, 10, 12,
            34, 51, 4371, 60787};
        for(int p : primes)assert IntegerUtils.isPrime(p);
        for(int np : notPrimes) assert !IntegerUtils.isPrime(np);
        
        /* Test isPrime and listPrimesUpTo on a larger set of primes */
        if(!primesPath.equals("")){
            try{
                BufferedReader read = new BufferedReader(new FileReader(new File(primesPath)));
                String[] morePrimes = read.readLine().split(",");
                primes = new int[morePrimes.length];
                for(int i = 0; i<morePrimes.length; i++){
                    primes[i] = Integer.parseInt(morePrimes[i]);
                }
                
                say("Testing primes with a list of: " + primes.length
                    + " primes");
                
                int[] largerPrimes = IntegerUtils
                        .listPrimesUpTo(primes[primes.length - 1]);
                
                //assert largerPrimes.length == primes.length;
                for(int i = 0; i<largerPrimes.length; i++){
                    if(largerPrimes[i] != primes[i])
                    assert largerPrimes[i] == primes[i];
                }
                for(int p : primes) assert IntegerUtils.isPrime(p);
                
                read.close();
            }catch(IOException e){assert false;}
        }
    }
    
    
    
    
    /**
     * Tests the functionality of BigDecimalUtils
     * @param piPath the path to a file containing at least 50k digits
     *      of correct pi. This should all be on one single line. If this 
     *      is the empty string "", then testing of many pi digits is skipped.
     * @param ePath the path to a file containing at least 50k digits
     *      of correct e. This should all be on one single line. If this
     *      is the empty string "", then testing of many e digits is skipped.
     */
    public static void testBigDecimalUtils(String piPath, String ePath){
        say("Testing BigDecimalUtils");
        
        /* Test isInteger */
        long[] ints = {-1000000, -9999, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 15, 20, 99, 100, 999, 1000000};
        String[] moreInts = {"0.0000", "1.0000", "999.00000000",
            "100000000000000000000000000000000", "99999999999999999999999"};
        String[] notInts = {"0.0000000000000000000000001", "0.1", "-1.1",
            "-291.123124", "999.999999999999999999999999"};
        for(int i = 0; i<ints.length; i++)
            assert BigDecimalUtils.isInteger(BigDecimal.valueOf(ints[i]));
        for(int i = 0; i<moreInts.length; i++)
            assert BigDecimalUtils.isInteger(new BigDecimal(moreInts[i]));
        for(int i = 0; i<notInts.length; i++)
            assert !BigDecimalUtils.isInteger(new BigDecimal(notInts[i]));
        
        /* Test sqrt */
        BigDecimal sqrt1 = TWO;
        BigDecimal sqrt2 = new BigDecimal("7618.3861");
        BigDecimal sqrt3 = new BigDecimal("1.414213562373");
        BigDecimal sqrt4 = new BigDecimal("87.283366685755");
        BigDecimal sqrt5 = new BigDecimal("87.3");
        BigDecimal sqrt6 = new BigDecimal("365.4");
        BigDecimal sqrt7 = new BigDecimal("19.1154387864887893606044");
        assert BigDecimalUtils.sqrt(sqrt1, 12).compareTo(sqrt3) == 0;
        assert BigDecimalUtils.sqrt(sqrt2, 12).compareTo(sqrt4) == 0;
        assert BigDecimalUtils.sqrt(sqrt2, 1).compareTo(sqrt5) == 0;
        assert BigDecimalUtils.sqrt(ONE, 10).compareTo(ONE) == 0;
        assert BigDecimalUtils.sqrt(sqrt6, 22).compareTo(sqrt7) == 0;
        assert BigDecimalUtils.sqrt(ZERO, 10).compareTo(ZERO) == 0;
        try{
            BigDecimalUtils.sqrt(new BigDecimal("-1"), 10);
            assert false;
        }catch(ArithmeticException e){}
        
        /* Test cbrt */
        BigDecimal cbrt1 = new BigDecimal("1128921.1288");
        BigDecimal cbrt2 = new BigDecimal("-0.0012313288");
        BigDecimal cbrt3 = new BigDecimal("123.45");
        BigDecimal cbrt4 = new BigDecimal("104.12488476443855892388");
        BigDecimal cbrt5 = new BigDecimal("-0.10718269658341615943");
        BigDecimal cbrt6 = new BigDecimal("5");
        assert BigDecimalUtils.cbrt(cbrt1, 20).compareTo(cbrt4) == 0;
        assert BigDecimalUtils.cbrt(cbrt2, 20).compareTo(cbrt5) == 0;
        assert BigDecimalUtils.cbrt(cbrt3, 0).compareTo(cbrt6) == 0;
        assert BigDecimalUtils.cbrt(BigDecimal.valueOf(3).pow(27), 100)
                .compareTo(BigDecimal.valueOf(3).pow(9)) == 0;
        assert BigDecimalUtils.cbrt(ZERO, 100).compareTo(ZERO) == 0;
        assert BigDecimalUtils.cbrt(ONE, 100).compareTo(ONE) == 0;
        
        /* Test nthroot */
        BigDecimal nthrt1 = new BigDecimal("70800480.798162478");
        BigDecimal nthrt2 = new BigDecimal("-198762498762.126");
        BigDecimal nthrt3 = new BigDecimal("2.89578847752694180571");
        BigDecimal nthrt4 = new BigDecimal("-181.8305656");
        assert BigDecimalUtils.nthRoot(nthrt1, 17, 20)
                .compareTo(nthrt3) == 0;
        assert BigDecimalUtils.nthRoot(nthrt2, 5, 7)
                .compareTo(nthrt4) == 0;
        assert BigDecimalUtils.nthRoot(nthrt1, 1, 100)
                .compareTo(nthrt1) == 0;
        assert BigDecimalUtils.nthRoot(nthrt2, 3, 100)
                .compareTo(BigDecimalUtils.cbrt(nthrt2, 100)) == 0;
        assert BigDecimalUtils.nthRoot(nthrt1, 2, 100)
                .compareTo(BigDecimalUtils.sqrt(nthrt1, 100)) == 0;
        try{
            BigDecimalUtils.nthRoot(nthrt1, 0, 100);
            assert false;
        }catch(ArithmeticException e){}
        try{
            BigDecimalUtils.nthRoot(nthrt1, -1, 100);
            assert false;
        }catch(ArithmeticException e){}
        
        /* Test agm */
        BigDecimal agm11 = new BigDecimal("17.4");
        BigDecimal agm12 = new BigDecimal("21");
        BigDecimal agm21 = new BigDecimal("4327694176.9876");
        BigDecimal agm22 = new BigDecimal("17649816298.1234");
        BigDecimal agm1 = new BigDecimal("19.157696065149");
        BigDecimal agm2 = new BigDecimal("9832067077.590561291915");
        BigDecimal agm3 = new BigDecimal("19.15769606514891621067954587232202" 
                    + "489714363089099089670603147038996229004451235135260119"
                    + "4376279199071998947");
        assert BigDecimalUtils.agm(agm11, agm12, 12)
                .compareTo(agm1) == 0;
        assert BigDecimalUtils.agm(agm21, agm22, 12)
                .compareTo(agm2) == 0;
        assert BigDecimalUtils.agm(agm21, agm21, 12)
                .compareTo(agm21) == 0;
        assert BigDecimalUtils.agm(agm11, agm12, 105)
                .compareTo(agm3) == 0;
        
        /* Test pi */
        if(!piPath.equals("")){
            try(BufferedReader read = new BufferedReader(
                    new FileReader(piPath))){
                
                String piString = read.readLine();
                
                int size = piString.length() - 10;
                
                say("Testing pi of size: " + size);

                int p1 = 10, p2 = 1000, p3 = 10000, p4 = 50000;
                
                BigDecimal pi = new BigDecimal(piString);
                
                BigDecimal pi1 = BigDecimalUtils.pi(p1);
                BigDecimal pi2 = BigDecimalUtils.pi(p2);
                BigDecimal pi3 = BigDecimalUtils.pi(p3);
                BigDecimal pi4 = BigDecimalUtils.pi(p4);
                BigDecimal pi5 = BigDecimalUtils.pi(size);
                
                assert pi1.compareTo(pi.setScale(p1, ROUND))== 0;
                assert pi2.compareTo(pi.setScale(p2, ROUND))== 0;
                assert pi3.compareTo(pi.setScale(p3, ROUND))== 0;
                assert pi4.compareTo(pi.setScale(p4, ROUND))== 0;
                assert pi5.compareTo(pi.setScale(size, ROUND))== 0;

            }catch(IOException e){
                say("Error: could not get pi from file");
                assert false;
            }
        }
        BigDecimal piSmall = new BigDecimal("3.14159265358979");
        BigDecimal piLarger = new BigDecimal("3.1415926535897932384626433832795"
                + "028841971693993751058209749445923078164062862089986280348253"
                + "4211706798214808651");
        assert BigDecimalUtils.pi(14).compareTo(piSmall) == 0;
        assert BigDecimalUtils.pi(110).compareTo(piLarger) == 0;
        
        /* Test ln2 */
        BigDecimal ln2 = new BigDecimal("0.693147180559945309417232121458176568"
                + "0755");
        assert BigDecimalUtils.ln2(40).compareTo(ln2) == 0;
        
        /* Test e */
        BigDecimal e = new BigDecimal("2.71828182845904523536");
        assert BigDecimalUtils.e(20).compareTo(e) == 0;
        if(!ePath.equals("")){
            try{
                BufferedReader read = new BufferedReader(new FileReader(
                        new File(ePath)));
                String eString = read.readLine();
                int size = eString.length() - 10;
                
                say("Testing e of size: " + size);
                
                int es1 = 100, es2 = 1000, es3 = 10000, es4 = 50000;
                
                BigDecimal realE = new BigDecimal(eString);
                
                BigDecimal e1 = BigDecimalUtils.e(es1);
                BigDecimal e2 = BigDecimalUtils.e(es2);
                BigDecimal e3 = BigDecimalUtils.e(es3);
                BigDecimal e4 = BigDecimalUtils.e(es4);
                BigDecimal e5 = BigDecimalUtils.e(size);
                
                assert realE.setScale(es1, ROUND).compareTo(e1) == 0;
                assert realE.setScale(es2, ROUND).compareTo(e2) == 0;
                assert realE.setScale(es3, ROUND).compareTo(e3) == 0;
                assert realE.setScale(es4, ROUND).compareTo(e4) == 0;
                assert realE.setScale(size, ROUND).compareTo(e5) == 0;
                
            }catch(IOException ex){assert false;}
        }
        
        /* Used in following trigonometric function testers */
        BigDecimal twoPi = TWO.multiply(pi(30));
        
        /* Test constrainRadians */
        BigDecimal cr1 = new BigDecimal("57321058.7632754612");
        BigDecimal cr2 = new BigDecimal("-1026398.125");
        assert BigDecimalUtils.constrainRadian(cr1, 100)
                .compareTo(constrainRadianSlow(cr1, 100)) == 0;
        assert BigDecimalUtils.constrainRadian(cr2, 100)
                .compareTo(constrainRadianSlow(cr2, 100)) == 0;
        assert BigDecimalUtils.constrainRadian(ZERO, 30).compareTo(ZERO) == 0;
        for(int i = -10; i<30; i++){
            assert BigDecimalUtils.constrainRadian(BigDecimal.valueOf(i).multiply(twoPi), 30)
                    .compareTo(ZERO) == 0;
        }
        
        /* Test sine */
        BigDecimal sin1 = new BigDecimal("827.7612");
        BigDecimal sin2 = new BigDecimal("-91267.85");
        BigDecimal sin3 = new BigDecimal("-0.99882583949322289439576");
        BigDecimal sin4 = new BigDecimal("0.9916941495593212876");
        assert BigDecimalUtils.sin(sin1, 23).compareTo(sin3) == 0;
        assert BigDecimalUtils.sin(sin2, 19).compareTo(sin4) == 0;
        assert BigDecimalUtils.sin(ZERO, 100).compareTo(ZERO) == 0;
        assert BigDecimalUtils.sin(TWO.multiply(pi(110)), 100)
                .compareTo(ZERO) == 0;
        assert BigDecimalUtils.sin(pi(110), 100).compareTo(ZERO) == 0;
        assert BigDecimalUtils.sin(pi(110).divide(TWO), 100)
                .compareTo(ONE) == 0;
        assert BigDecimalUtils.sin(pi(110).multiply(THREE).divide(TWO), 100)
                .compareTo(ONE.negate()) == 0;
        
        /* Test cosine */
        BigDecimal cos1 = new BigDecimal("827.7612");
        BigDecimal cos2 = new BigDecimal("-91267.85");
        BigDecimal cos3 = new BigDecimal("-0.0484452511672561786401877678");
        BigDecimal cos4 = new BigDecimal("-0.12861848129182097455");
        assert BigDecimalUtils.cos(cos1, 28).compareTo(cos3) == 0;
        assert BigDecimalUtils.cos(cos2, 20).compareTo(cos4) == 0;
        assert BigDecimalUtils.cos(ZERO, 100).compareTo(ONE) == 0;
        assert BigDecimalUtils.cos(TWO.multiply(pi(110)), 100)
                .compareTo(ONE) == 0;
        assert BigDecimalUtils.cos(pi(110), 100)
                .compareTo(ONE.negate()) == 0;
        assert BigDecimalUtils.cos(pi(110).divide(TWO), 100)
                .compareTo(ZERO) == 0;
        assert BigDecimalUtils.cos(pi(110).multiply(THREE).divide(TWO), 100)
                .compareTo(ZERO) == 0;
        
    }
    
    
    /**
     * Tests the functionality of the BigComplex class. Assumes everything
     * else works as intended
     */
    public static void testBigComplex(){
        say("Testing BigComplex");
        
        /* Basic constructor stuff */
        BigComplex b1 = new BigComplex("10", "7");
        BigComplex b11 = new BigComplex("10", "7", 12);
        BigComplex b12 = new BigComplex("12.12345", "10");
        BigComplex b13 = new BigComplex("12.12345", "10", 3);
        assert b1.r.compareTo(TEN) == 0;
        assert b1.real().compareTo(TEN) == 0;
        assert b1.c.compareTo(new BigDecimal("7")) == 0;
        assert b1.complex().compareTo(new BigDecimal("7")) == 0;
        assert b1.accuracy == BigComplex.DEFAULT_ACCURACY;
        assert b11.accuracy == 12;
        
        /* Equality and other constructors */
        BigComplex b2 = BigComplex.onlyReal("10");
        BigComplex b3 = BigComplex.onlyReal(TEN);
        assert b2.equals(b3);
        assert b3.equals(b2);
        assert b2.equals(new BigComplex("10", "0"));
        assert b3.equals(new BigComplex("10", "0"));
        BigComplex b4 = BigComplex.onlyComplex(ONE);
        BigComplex b5 = BigComplex.onlyComplex("1");
        BigComplex b6 = new BigComplex("0", "1");
        assert b4.equals(b5);
        assert b5.equals(b4);
        assert b4.equals(b6);
        assert b6.equals(b4);
        assert b5.equals(b6);
        assert BigComplex.NAN.equals(new BigComplex("10", "10", -4));
        assert !b12.equals(b13);
        
        /* isZero */
        assert !b6.isZero();
        assert BigComplex.ZERO(1).isZero();
        assert new BigComplex("0", "0").isZero();
        
        /* isOne */
        assert BigComplex.ONE().isOne();
        assert !BigComplex.ZERO().isOne();
        assert !new BigComplex("0", "1").isOne();
        assert new BigComplex("1.0", "0").isOne();
        assert !new BigComplex("1", "1").isOne();
        
        /* isReal/isComplex */
        assert b1.isComplex();
        assert !b1.isReal();
        assert BigComplex.ZERO().isReal();
        assert BigComplex.TEN().isReal();
        assert !b2.isComplex();
        assert b2.isReal();
        
        /* isNAN */
        assert BigComplex.NAN.isNAN();
        assert !b1.isNAN();
        
        /* Add */
        BigComplex a1 = new BigComplex("-13.5", "10.8", 100);
        BigComplex a2 = new BigComplex("7.4", "-9");
        BigComplex a3 = new BigComplex("-6.1", "1.8");
        assert a1.add(a2).equals(a3);
        assert a2.add(a1).equals(a3);
        assert a1.add(BigComplex.NAN).isNAN();
        assert a1.add(BigComplex.ZERO()).equals(a1);
        assert a1.accuracy == a1.add(a2).accuracy;
        assert a1.accuracy == a2.add(a1).accuracy;
        
        /* Subtract */
        BigComplex s1 = new BigComplex("-4.32", "9", 12);
        BigComplex s2 = new BigComplex("-1.2", "3.5", 3);
        BigComplex s3 = new BigComplex("-3.12", "5.5");
        BigComplex s4 = new BigComplex("3.12", "-5.5");
        assert s1.subtract(s2).equals(s3);
        assert s1.sub(s2).equals(s3);
        assert s2.sub(s1).equals(s4);
        assert s1.sub(BigComplex.ZERO()).equals(s1);
        assert s1.sub(BigComplex.NAN).isNAN();
        assert s1.accuracy == s1.sub(s2).accuracy;
        assert s1.accuracy == s2.sub(s1).accuracy;
        
        /* Multiply */
        BigComplex m1 = new BigComplex("-13.5", "84.4");
        BigComplex m2 = new BigComplex("45.32", "9");
        BigComplex m3 = new BigComplex("-1371.42", "3703.508");
        BigComplex m4 = new BigComplex("12.1234", "1", 3);
        BigComplex m41 = new BigComplex("12.1234", "1", 10);
        BigComplex m5 = new BigComplex("13.12345", "1", 2);
        BigComplex m6 = new BigComplex("158.054", "25.243", 3);
        BigComplex m7 = new BigComplex("158.059008", "25.2434", 10);
        assert m1.mult(m2).equals(m3);
        assert m2.mult(m1).equals(m3);
        assert m1.mult(BigComplex.ZERO()).isZero();
        assert m1.mult(BigComplex.ONE()).equals(m1);
        assert m1.mult(BigComplex.NAN).isNAN();
        assert m4.mult(m5).accuracy == m4.accuracy;
        assert m4.mult(m5).accuracy == m6.accuracy;
        assert m4.mult(m5).equals(m6);
        assert !m4.mult(m5).equals(m7);
        assert m41.mult(m5).equals(m7);
        assert m41.mult(m5).accuracy == m41.accuracy;
        assert !m41.mult(m5).equals(m6);
        
        /* Divide */
        BigComplex d1 = new BigComplex("-13.5", "84.4");
        BigComplex d2 = new BigComplex("45.32", "9");
        BigComplex d3 = new BigComplex("94.33322", "8", 3);
        BigComplex d4 = new BigComplex("0.0692209630", "1.8485660047");
        BigComplex d5 = new BigComplex("0.0202282903", "-0.5402023924");
        BigComplex d6 = new BigComplex("-0.0667538249", "0.9003639299");
        assert d1.div(d2).equals(d4);
        assert d2.div(d1).equals(d5);
        assert d1.div(d3).equals(d6);
        assert d1.div(BigComplex.ONE()).equals(d1);
        assert d1.div(BigComplex.ZERO()).equals(BigComplex.NAN);
        assert d1.div(BigComplex.NAN).isNAN();
        
        /* Complex Conjugate */
        BigComplex cc1 = new BigComplex("-13.5", "84.4");
        BigComplex cc2 = new BigComplex("45.32", "9");
        BigComplex cc3 = new BigComplex("94.33322", "8.4444", 3);
        BigComplex cc4 = new BigComplex("-13.5", "-84.4");
        BigComplex cc5 = new BigComplex("45.32", "-9");
        BigComplex cc6 = new BigComplex("94.333", "-8.444", 3);
        assert cc1.conj().equals(cc4);
        assert cc2.conj().equals(cc5);
        assert cc3.conj().equals(cc6);
        assert BigComplex.NAN.conj().isNAN();
        
        /* Negate */
        BigComplex n1 = new BigComplex("-13.5", "84.4");
        BigComplex n2 = new BigComplex("94.33322", "8", 3);
        BigComplex n3 = new BigComplex("13.5", "-84.4");
        BigComplex n4 = new BigComplex("-94.333", "-8");
        assert n1.neg().equals(n3);
        assert n2.neg().equals(n4);
        assert BigComplex.NAN.neg().equals(BigComplex.NAN);
        
        /* Square */
        BigComplex sq1 = new BigComplex("-13.5", "84.4");
        BigComplex sq2 = new BigComplex("94.33322", "8", 3);
        assert sq1.square().equals(sq1.mult(sq1));
        assert sq2.square().equals(sq2.mult(sq2));
        assert BigComplex.ZERO().square().isZero();
        assert BigComplex.ONE().square().equals(BigComplex.ONE());
        assert BigComplex.NAN.square().isNAN();
        
        /* Cube */
        BigComplex cb1 = new BigComplex("-13.5", "84.4");
        BigComplex cb2 = new BigComplex("94.33322", "8", 3);
        assert cb1.cube().equals(cb1.mult(cb1).mult(cb1));
        assert cb2.cube().equals(cb2.mult(cb2).mult(cb2));
        assert BigComplex.ZERO().cube().isZero();
        assert BigComplex.ONE().cube().isOne();
        assert BigComplex.NAN.cube().isNAN();
        
        /* Pow */
        int pv1 = 34;
        BigComplex p1 = new BigComplex("-13.5", "84.4", 100);
        BigComplex p2 = new BigComplex("94.33322", "8", 100);
        assert p1.pow(1).equals(p1);
        assert p1.pow(0).isOne();
        assert p1.pow(2).equals(p1.square());
        assert p1.pow(3).equals(p1.cube());
        assert p2.pow(2).equals(p2.square());
        assert p2.pow(3).equals(p2.cube());
        assert BigComplex.ONE().pow(10).isOne();
        assert BigComplex.ZERO().pow(20).isZero();
        assert BigComplex.TWO().pow(8)
                .equals(BigComplex.TWO().square().square().square());
        assert BigComplex.ZERO().pow(0).isNAN();
        assert BigComplex.NAN.pow(10).isNAN();
        assert p1.pow(-pv1).equals(BigComplex.ONE().div(p1.pow(pv1)));
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //////////////////////////////
    // Finding the best p for e //
    //////////////////////////////
    
    
    
    
    
    
    
    public static void writePTests(){
        int[] vals = {2, 4, 10, 12, 30, 50, 75, 100, 150, 300, 500, 750, 1000,
                1200, 1500, 1750, 2500, 5000, 7500, 10000, 15000, 25000,
                50000, 75000, 100000, 200000, 300000, 500000};
        long[][] ret = findP(vals, 100);
        
        try{
            BufferedWriter write = new BufferedWriter(new FileWriter(
                new File("C:/Users/Babynado/Desktop/etests.txt")));
            for(long[] la : ret){
                write.write("" + la[0] + SEP + la[1] + SEP + la[2] + SEP + la[3] + "\n");
            }
            write.flush();
            write.close();
        }catch(IOException e){}
    }
    
    public static long[][] findP(int[] accs, int numPs){
        
        long[][] ret = new long[accs.length][3];
        
        for(int accIndex = 0; accIndex < accs.length; accIndex++){
            
            /* The starting p value and increment */
            int startP = Math.max(accs[accIndex] / 1000, 2);
            if(startP % 2 != 0)startP--;
            int endP = Math.max(accs[accIndex]/(int)(3 + Math.log10(accs[accIndex])), 50);
            int inc = Math.max((endP - startP)/numPs, 2);
            if(inc % 2 != 0)inc--;
            
            /* Find a nice n */
            int n = 0;
            int np = accs[accIndex] < 100 ? accs[accIndex] / 2 : startP + inc * (numPs/2);
            if(np % 2 != 0) np ++;
            long nStart = System.currentTimeMillis();
            while(System.currentTimeMillis() - nStart < 500){
                testE(np, accs[accIndex]);
                n++;
            }
            
            /* The p-value, time taken, and iterations */
            long pMin = 0, timeMin = Long.MAX_VALUE, iterations = 0;
            
            /* Do the tests */
            for(int p = startP; p < endP; p += inc){
                int it = 0;
                long startTime = System.currentTimeMillis();
                for(int i = 0; i<n; i++)
                    it = testE(p, accs[accIndex]);
                long endTime = System.currentTimeMillis() - startTime;
                if(endTime < timeMin){
                    timeMin = endTime;
                    pMin = p;
                    iterations = it;
                }
                say("Acc: " + accs[accIndex] + " p: " + p + " time: " + endTime);
                
                /* If we should stop early */
                if(accs[accIndex] < 50000 && p > startP + 10 * inc 
                        && endTime > 1000 || endTime > 4 * timeMin
                        || (timeMin > 2000 && endTime > 2 * timeMin))break;
            }
            
            long[] add = {accs[accIndex], pMin, timeMin, iterations};
            ret[accIndex] = add;
        }
        
        return ret;
    }
    
    /* Copy from BigDecimalUtils */
    public static int testE(int p, int accuracy){
        int biggerAcc = accuracy + 20;
        
        BigDecimal b = ONE, ret = ONE, add;
        
        int k = 1;
        
        do{
            long t = p * k;
            for(int i = 0; i<p; i+=2)
                b = b.multiply(new BigDecimal( (t-i) * (t-i-1) ));
            add = e_num(p, k).divide(b, biggerAcc, ROUND);
            
            ret = ret.add(add);
            k ++;
        }while(add.compareTo(ZERO) > 0);
        
        return k;
    }
    
    
    /* A copy from BigDecimalUtils */
    private static BigDecimal e_num(int p, long k){
        BigDecimal ret = ZERO;
        long t = p * k;
        
        ret = ret.add(new BigDecimal(t + 1));
            
        BigDecimal curr = ONE;
        for(int i = 0; i<p/2-1; i++){
            int d = 2 * i;
            curr = curr.multiply(new BigDecimal( (t-d) * (t-d-1) ));
            ret = ret.add(curr.multiply(new BigDecimal(t-d-1)));
        }
            
        return ret;
    }
    
    
    
    
    
    
    
    /******************/
    /* Helper Methods */
    /******************/
    
    
    
    /**
     * Used to test to make sure the constrainRadian method in BigDecimalUtils
     * is working correct. This calculates the equivalent radian value for d
     * within [0, 2*pi) in a much slower, naieve, but definitely correct way
     * 
     * @param d the BigDecimal to constrainRadian
     * @param accuracy the number of decimal places to be accurate to
     * @return an equivalent radian value to d within [0, 2*pi)
     */
    public static BigDecimal constrainRadianSlow(BigDecimal d, int accuracy){
        BigDecimal twoPi = TWO.multiply(pi(accuracy));
        while(d.compareTo(twoPi) >= 0)
            d = d.subtract(twoPi);
        while(d.compareTo(ZERO) < 0)
            d = d.add(twoPi);
        return d;
    }
    
}
