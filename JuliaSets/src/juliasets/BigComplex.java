package juliasets;

import java.math.BigDecimal;
import static juliasets.Utils.*;

public class BigComplex {
    
    //Adds c1 onto this
    public BigComplex add(BigComplex c2){
        return new BigComplex(this.r.add(c2.r), this.c.add(c2.c));
    }
    
    //Adds d onto this
    public BigComplex add(double d){
        return new BigComplex(this.r.add(new BigDecimal(d)), this.c);
    }
    
    //The negation of this = -1*this
    public BigComplex neg(){
        return new BigComplex(this.r.negate(), this.c.negate());
    }
    
    //Multiply this by c2
    public BigComplex mult(BigComplex c2){
        return new BigComplex(this.r.multiply(c2.r).subtract(this.c.multiply(c2.c)),
                this.r.multiply(c2.c).add(this.c.multiply(c2.r)));
    }
    
    //Multiply this by d
    public BigComplex mult(double d){
        return new BigComplex(this.r*d + this.c*d);
    }
    
    //Returns this-c2
    public BigComplex sub(BigComplex c2){
        return new BigComplex(this.r-c2.r, this.c-c2.c);
    }
    
    //this-d
    public BigComplex sub(double d){
        return new BigComplex(this.r-d, this.c);
    }
    
    //returns this/d
    public BigComplex div(double d){
        if(d==0)return new BigComplex(Double.NaN, Double.NaN);
        return new BigComplex(this.r/d, this.c/d);
    }
    
    //this/c2
    public BigComplex div(BigComplex c2){
        if(c2.r==0 && c2.c==0)return new BigComplex(Double.NaN, Double.NaN);
        if(this.r==0 && c2.c==0)return new BigComplex(0,0);
        if(c2.c==0)return this.div(c2.r);
        return this.mult(c2.conj()).div(c2.r*c2.r+c2.c*c2.c);
    }
    
    //this^2
    public BigComplex square(){
        return this.mult(this);
    }
    
    //this^pow
    public BigComplex pow(int pow){
        if(pow==0)return new BigComplex(1,0);
        if(pow==1)return new BigComplex(this);
        if(pow==2)return this.square();
        if(pow < 0){
            return new BigComplex(1, 0).div(this.pow(-pow));
        }
        
        //Recursively call this function
        int factor = pow/2;
        BigComplex ret = this.pow(factor);
        ret = ret.square();
        
        //Add the extra in case it is odd
        if(pow % 2 != 0){
            ret = ret.mult(this);
        }
        
        return ret;
    }
    
    //this^d
    public BigComplex pow(double d){
        if(d==0)return new BigComplex(1);
        if(this.r==0 && this.c==0)return new BigComplex(0);
        if(this.c==0 && isInteger(d)){
            return new BigComplex(Utils.pow(this.r, (int)d));
        }
        if(this.c==0){
            return new BigComplex(Math.pow(this.r, d));
        }
        //Check if integer
        if(isInteger(d)){
            return this.pow((int)Math.floor(d));
        }
        return new BigComplex(d,0).mult(this.log()).exp();
    }
    
    //this^c2
    public BigComplex pow(BigComplex c2){
        if(c2.r==0 && c2.c==0)return new BigComplex(1);
        if(c2.c==0)return this.pow(c2.r);
        return c2.mult(this.log()).exp();
    }
    
    //e^this
    public BigComplex exp(){
        if(this.c==0&&this.r==0)return new BigComplex(1);
        if(this.c==0)return new BigComplex(Math.exp(this.r));
        if(this.r==0)return new BigComplex(Math.cos(this.c), Math.sin(this.c));
        return new BigComplex(Math.exp(this.r) * Math.cos(this.c),
                           Math.exp(this.r) * Math.sin(this.c));
    }
    
    
    //The complex conjugate
    public BigComplex conj(){
        return new BigComplex(this.r, -this.c);
    }
    
    //The argument of this, defined as atan(b/a) for complex number a+bi
    //Restricted to within (-pi, pi)
    public double arg(){
        if(this.r < 0 && this.c < 0){
            return -Math.PI + new BigComplex(-this.r, -this.c).arg();
        }else if(this.r < 0){
            return Math.PI - new BigComplex(-this.r, this.c).arg();
        }
        return Math.atan(this.c/this.r);
    }
    
    //The magnitude of this
    public double mag(){
        return Math.sqrt(this.r*this.r+this.c*this.c);
    }
    
    //Natural log of this
    //Using def: ln(z) = ln|z| + i*arg(z)
    public BigComplex log(){
        if(this.c==0)return new BigComplex(Math.log(this.r));
        return new BigComplex(Math.log(this.mag()), this.arg());
    }
    
    //Logs of different bases
    public static BigComplex logBase(double a, BigComplex c1){
        if(a == 1)return new BigComplex(Double.POSITIVE_INFINITY,
                                     Double.POSITIVE_INFINITY);
        if(a<=0)return new BigComplex(Double.NaN, Double.NaN);
        if(a==Math.E)return c1.log();
        if(c1.r==0 && c1.c==0)return new BigComplex(Double.NEGATIVE_INFINITY);
        if(c1.c==0)return new BigComplex(Utils.logBase(a, c1.r));
        return c1.log().div(Math.log(a));
    }
    
    //Logs of complex bases
    public static BigComplex logBase(BigComplex c1, BigComplex c2){
        if(c1.c==0)return logBase(c1.r, c2);
        return c2.log().div(c1.log());
    }
    
    
    
    
    
    //////////////////////////////////////////////
    //Non-static
    public BigDecimal r, c;
    public BigComplex(BigDecimal real, BigDecimal complex){
        this.r=real;
        this.c=complex;
    }
    public BigComplex(BigComplex c){
        this.r = c.r;
        this.c = c.c;
    }
    public BigComplex(BigDecimal real){
        this.r = real;
        this.c = BigDecimal.ZERO;
    }
    
    @Override
    public String toString(){
        return this.r + " + " + this.c + "i";
    }
    
    //Returns true if both the real and complex components are real numebers
    public boolean isFinite(){
        return Double.isFinite(this.r) && Double.isFinite(this.c);
    }
    
    //Returns true if either the real or complex components DNE
    public boolean DNE(){
        return !isFinite();
    }
    
    //Returns true if both the real and complex part of this number is within some
    //small distance from the given complex
    public boolean isCloseTo(BigComplex c2, double distance){
        return Math.abs(this.r-c2.r) < distance && Math.abs(this.c-c2.c) < distance;
    }
    
    public boolean isCloseTo(BigComplex c2){
        return this.isCloseTo(c2, 0.0001);
    }
    
}
