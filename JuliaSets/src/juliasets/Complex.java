package juliasets;

import static juliasets.Utils.*;

public class Complex {
    
    //Adds c1 onto this
    public Complex add(Complex c2){
        return new Complex(this.r+c2.r, this.c+c2.c);
    }
    
    //Adds d onto this
    public Complex add(double d){
        return new Complex(this.r+d, this.c);
    }
    
    //The negation of this = -1*this
    public Complex neg(){
        return new Complex(-this.r, -this.c);
    }
    
    //Multiply this by c2
    public Complex mult(Complex c2){
        return new Complex(this.r*c2.r-this.c*c2.c, this.r*c2.c+this.c*c2.r);
    }
    
    //Multiply this by d
    public Complex mult(double d){
        return new Complex(this.r*d, this.c*d);
    }
    
    //Returns this-c2
    public Complex sub(Complex c2){
        return new Complex(this.r-c2.r, this.c-c2.c);
    }
    
    //this-d
    public Complex sub(double d){
        return new Complex(this.r-d, this.c);
    }
    
    //returns this/d
    public Complex div(double d){
        if(d==0)return new Complex(Double.NaN, Double.NaN);
        return new Complex(this.r/d, this.c/d);
    }
    
    //this/c2
    public Complex div(Complex c2){
        if(c2.r==0 && c2.c==0)return new Complex(Double.NaN, Double.NaN);
        if(this.r==0 && c2.c==0)return new Complex(0,0);
        if(c2.c==0)return this.div(c2.r);
        return this.mult(c2.conj()).div(c2.r*c2.r+c2.c*c2.c);
    }
    
    //this^2
    public Complex square(){
        return this.mult(this);
    }
    
    //this^pow
    public Complex pow(int pow){
        if(pow==0)return new Complex(1,0);
        if(pow==1)return new Complex(this);
        if(pow==2)return this.square();
        if(pow < 0){
            return new Complex(1, 0).div(this.pow(-pow));
        }
        
        //Recursively call this function
        int factor = pow/2;
        Complex ret = this.pow(factor);
        ret = ret.square();
        
        //Add the extra in case it is odd
        if(pow % 2 != 0){
            ret = ret.mult(this);
        }
        
        return ret;
    }
    
    //this^d
    public Complex pow(double d){
        if(d==0)return new Complex(1, 0);
        if(this.r==0 && this.c==0)return new Complex(0, 0);
        if(this.c==0 && isInteger(d)){
            return new Complex(Utils.pow(this.r, (int)d), 0);
        }
        if(this.c==0){
            return new Complex(Math.pow(this.r, d), 0);
        }
        //Check if integer
        if(isInteger(d)){
            return this.pow((int)Math.floor(d));
        }
        return new Complex(d,0).mult(this.log()).exp();
    }
    
    //this^c2
    public Complex pow(Complex c2){
        if(c2.r==0 && c2.c==0)return new Complex(1, 0);
        if(c2.c==0)return this.pow(c2.r);
        return c2.mult(this.log()).exp();
    }
    
    //e^this
    public Complex exp(){
        if(this.c==0&&this.r==0)return new Complex(1, 0);
        if(this.c==0)return new Complex(Math.exp(this.r), 0);
        if(this.r==0)return new Complex(Math.cos(this.c), Math.sin(this.c));
        return new Complex(Math.exp(this.r) * Math.cos(this.c),
                           Math.exp(this.r) * Math.sin(this.c));
    }
    
    
    //The complex conjugate
    public Complex conj(){
        return new Complex(this.r, -this.c);
    }
    
    //The argument of this, defined as atan(b/a) for complex number a+bi
    //Restricted to within (-pi, pi)
    public double arg(){
        if(this.r < 0 && this.c < 0){
            return -Math.PI + new Complex(-this.r, -this.c).arg();
        }else if(this.r < 0){
            return Math.PI - new Complex(-this.r, this.c).arg();
        }
        return Math.atan(this.c/this.r);
    }
    
    //The magnitude of this
    public double mag(){
        return Math.sqrt(this.r*this.r+this.c*this.c);
    }
    
    //Natural log of this
    //Using def: ln(z) = ln|z| + i*arg(z)
    public Complex log(){
        if(this.c==0)return new Complex(Math.log(this.r), 0);
        return new Complex(Math.log(this.mag()), this.arg());
    }
    
    //Logs of different bases
    public static Complex logBase(double a, Complex c1){
        if(a == 1)return new Complex(Double.POSITIVE_INFINITY,
                                     Double.POSITIVE_INFINITY);
        if(a<=0)return new Complex(Double.NaN, Double.NaN);
        if(a==Math.E)return c1.log();
        if(c1.r==0 && c1.c==0)return new Complex(Double.NEGATIVE_INFINITY, 0);
        if(c1.c==0)return new Complex(Utils.logBase(a, c1.r), 0);
        return c1.log().div(Math.log(a));
    }
    
    //Logs of complex bases
    public static Complex logBase(Complex c1, Complex c2){
        if(c1.c==0)return logBase(c1.r, c2);
        return c2.log().div(c1.log());
    }
    
    
    
    
    
    //////////////////////////////////////////////
    //Non-static
    public double r, c;
    public Complex(double real, double complex){
        this.r=real;
        this.c=complex;
    }
    public Complex(Complex c){
        this.r = c.r;
        this.c = c.c;
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
    public boolean isCloseTo(Complex c2, double distance){
        return Math.abs(this.r-c2.r) < distance && Math.abs(this.c-c2.c) < distance;
    }
    
    public boolean isCloseTo(Complex c2){
        return this.isCloseTo(c2, 0.000001);
    }
    
}
