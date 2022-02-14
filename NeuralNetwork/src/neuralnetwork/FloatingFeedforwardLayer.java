package neuralnetwork;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class FloatingFeedforwardLayer {
    
    public FloatingFeedforwardLayer(int size, FloatingFeedforwardNetwork net) {
        if (size > 0) {
            this.size = size;
            this.net = net;
        } else {
            NeuralNetwork.say("Cannot create a size: " + size + " FloatingFeedforwardLayer.");
        }
    }

    //the number of neurons in this network.
    private int size;
    
    //the neural network this is contained in
    private FloatingFeedforwardNetwork net;

    //the weight matrix of this layer
    private FloatMatrix weightFloatMatrix;
    
    //the previous weight changes to this layer's weight matrix
    private FloatMatrix deltaWeights;

    //the last input to this Layer
    private FloatMatrix input;
    
    //the partial derivatives used in training
    public FloatMatrix partD;
    

    /**
     * Returns the number of neurons in this network.
     *
     * @return The number of neurons in this network.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Resets this weightFloatMatrix to completely random values
     * 
     * @param in the number of incoming neurons
     */
    public void resetWeightFloatMatrix(int in) {
        this.weightFloatMatrix = FloatMatrix.generateRandomFloatMatrix(in, this.size, (float)NeuralNetwork.minRandom, (float)NeuralNetwork.maxRandom, false);
    }

    /**
     * Presents an array of floats between -1 and 1 to this layer and returns the output.
     * <p>
     * Fills in empty spots with 0's if a shortened array is passed.
     *
     * @param data the data to present
     * @return The output from this layer.
     */
    public float[] present(float[] data) {
        if (data.length != this.weightFloatMatrix.getRows()) {
            NeuralNetwork.say("Sizes do not match up in FloatingFeedforwardLayer! Data size: "
                    + data.length + ", WeightFloatMatrix Size: " + this.weightFloatMatrix.getRows());
        }
        input = new FloatMatrix(data, FloatMatrix.COLUMN);
        FloatMatrix m = new FloatMatrix(data, FloatMatrix.ROW);

        //returning the values
        float[] ret = new float[this.getSize()];

        for (int i = 0; i < this.getSize(); i++) {
            float r = FloatMatrix.dotProduct(m, this.weightFloatMatrix.getCol(i));
            if(this.net.activationFunction==FloatingFeedforwardNetwork.TANH){
                r = (float)Math.tanh(r);
            }else if(this.net.activationFunction==FloatingFeedforwardNetwork.SIG){
                r = (float)(1/(1+Math.exp(-r)));
            }
            ret[i] = r;
        }

        return ret;
    }
    
    
    
    
    
    

    /**
     * Returns a reference to this layer's weight FloatMatrix.
     *
     * @return A reference to this layer's weight FloatMatrix.
     */
    public FloatMatrix getWeightFloatMatrix() {
        return this.weightFloatMatrix;
    }

    /**
     * Sets this layer's weight FloatMatrix.
     *
     * @param data this layer's weight FloatMatrix.
     */
    public void setWeightFloatMatrix(FloatMatrix data) {
        this.weightFloatMatrix = data.copy();
    }
    
    
    
    /**
     * Sets the index in this layer's weight FloatMatrix to @val.
     * 
     * @param i the row index
     * @param j the column index
     * @param val the value to set
     */
    public void setWeightFloatMatrixValue(int i, int j, float val){
        this.weightFloatMatrix.set(i, j, val);
    }
    
    

    /**
     * Returns the last input to this layer.
     *
     * @return The last input to this layer.
     */
    public FloatMatrix getLastInput() {
        return this.input;
    }
    
    
    /**
     * Returns the network this layer is contained in.
     * 
     * @return The network this layer is contained in.
     */
    public FloatingFeedforwardNetwork getNetwork(){
        return this.net;
    }
    
    
    /**
     * Resets the FloatMatrix holding the previous weight change
     * values to all 0's.
     */
    public void resetDeltaFloatMatrix(){
        this.deltaWeights = new FloatMatrix(this.weightFloatMatrix.getRows(), this.weightFloatMatrix.getCols());
    }
    
    
    /**
     * Returns this layer's FloatMatrix holding the previous weight changes.
     * @return This layer's FloatMatrix holding the previous weight changes.
     */
    public FloatMatrix getDeltaFloatMatrix(){
        return this.deltaWeights;
    }
    
    
    /**
     * Sets the value at the specified index to the specified value.
     * 
     * @param col the column index
     * @param row the row index
     * @param val the value to set as
     */
    public void setDeltaFloatMatrixValue(int row, int col, float val){
        this.deltaWeights.set(row, col, val);
    }
}



//add into present in feedforwardnetwork if you want multithreading

/*
    
*/