/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

/**
 *
 * @author jeann
 */
public class DotLayer {
    public double[] data;
    public int index;
    public DotLayer(double[] data, int index){
        this.data = data;
        this.index = index;
    }
    
    public double[] getData(){
        return this.data;
    }
    
    public int getIndex(){
        return this.index;
    }
}
