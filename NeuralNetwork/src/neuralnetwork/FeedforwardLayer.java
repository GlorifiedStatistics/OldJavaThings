package neuralnetwork;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class FeedforwardLayer {
    
    public FeedforwardLayer(int size, FeedforwardNetwork net) {
        if (size > 0) {
            this.size = size;
            this.net = net;
        } else {
            NeuralNetwork.say("Cannot create a size: " + size + " FeedforwardLayer.");
        }
    }

    //the number of neurons in this network.
    private int size;
    
    //the neural network this is contained in
    private FeedforwardNetwork net;

    //the weight matrix of this layer
    private Matrix weightMatrix;
    
    //the previous weight changes to this layer's weight matrix
    private Matrix deltaWeights;

    //the last input to this Layer
    private Matrix input;
    
    //the partial derivatives used in training
    public Matrix partD;
    

    /**
     * Returns the number of neurons in this network.
     *
     * @return The number of neurons in this network.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Resets this weightMatrix to completely random values
     * 
     * @param in the number of incoming neurons
     */
    public void resetWeightMatrix(int in) {
        this.weightMatrix = Matrix.generateRandomMatrix(in, this.size, NeuralNetwork.minRandom, NeuralNetwork.maxRandom, false);
    }

    /**
     * Presents an array of doubles between -1 and 1 to this layer and returns the output.
     * <p>
     * Fills in empty spots with 0's if a shortened array is passed.
     *
     * @param data the data to present
     * @return The output from this layer.
     */
    public double[] present(double[] data) {
        if (data.length != this.weightMatrix.getRows()) {
            NeuralNetwork.say("Sizes do not match up in FeedforwardLayer! Data size: "
                    + data.length + ", WeightMatrix Size: " + this.weightMatrix.getRows());
        }
        input = new Matrix(data, Matrix.COLUMN);
        Matrix m = new Matrix(data, Matrix.ROW);

        //returning the values
        double[] ret = new double[this.getSize()];

        for (int i = 0; i < this.getSize(); i++) {
            double r = Matrix.dotProduct(m, this.weightMatrix.getCol(i));
            if(this.net.activationFunction==FeedforwardNetwork.TANH){
                r = Math.tanh(r);
            }else if(this.net.activationFunction==FeedforwardNetwork.SIG){
                r = 1/(1+Math.exp(-r));
            }
            ret[i] = r;
        }

        return ret;
    }
    
    
    
    /**
     * Presents an array of doubles between -1 and 1 to this layer and returns the output.
     * <p>
     * This method takes advantage of multithreading.
     * <p>
     * Fills in empty spots with 0's if a shortened array is passed.
     *
     * @param data the data to present
     * @return The output from this layer.
     */
    public double[] presentMultiThread(double[] data){
        if (data.length != this.weightMatrix.getRows()) {
            NeuralNetwork.say("Sizes do not match up in FeedforwardLayer! Data size: "
                    + data.length + ", WeightMatrix Size: " + this.weightMatrix.getRows());
        }
        input = new Matrix(data, Matrix.COLUMN);
        answerForMultiThread = new double[this.getSize()];
        NeuralNetwork.threadPool = Executors.newFixedThreadPool(NeuralNetwork.numberOfThreads);
        for(int i = 0; i<NeuralNetwork.numberOfThreads; i++){
            NeuralNetwork.threadPool.submit(new DotThread(this,data));
        }
        try{
            NeuralNetwork.threadPool.shutdown();
            NeuralNetwork.threadPool.awaitTermination(10, TimeUnit.HOURS);
        }catch(Exception e){}
        
        currentIndex = 0;
        return answerForMultiThread;
    }
    public double[] answerForMultiThread;
    public int currentIndex = 0;
    
    /**
     * This is for the multithread capabilities.
     * 
     * @param ans the answer
     * @param index the index of the answer
     * @return The next dotProduct to find.
     */
    public synchronized DotLayer getNextThreadCalculation(double ans, int index){
        if(ans!=NeuralNetwork.THREAD_START){
            answerForMultiThread[index] = ans;
        }
        
        if(currentIndex<this.getSize()){
            DotLayer ret = new DotLayer(this.getWeightMatrix().getColAsArray(currentIndex),currentIndex);
            currentIndex++;
            return ret;
        }else{
            return null;
        }
    }
    
    

    /**
     * Returns a reference to this layer's weight Matrix.
     *
     * @return A reference to this layer's weight Matrix.
     */
    public Matrix getWeightMatrix() {
        return this.weightMatrix;
    }

    /**
     * Sets this layer's weight Matrix.
     *
     * @param data this layer's weight Matrix.
     */
    public void setWeightMatrix(Matrix data) {
        this.weightMatrix = data.copy();
    }
    
    
    
    /**
     * Sets the index in this layer's weight Matrix to @val.
     * 
     * @param i the row index
     * @param j the column index
     * @param val the value to set
     */
    public void setWeightMatrixValue(int i, int j, double val){
        this.weightMatrix.set(i, j, val);
    }
    
    

    /**
     * Returns the last input to this layer.
     *
     * @return The last input to this layer.
     */
    public Matrix getLastInput() {
        return this.input;
    }
    
    
    /**
     * Returns the network this layer is contained in.
     * 
     * @return The network this layer is contained in.
     */
    public FeedforwardNetwork getNetwork(){
        return this.net;
    }
    
    
    /**
     * Resets the Matrix holding the previous weight change
     * values to all 0's.
     */
    public void resetDeltaMatrix(){
        this.deltaWeights = new Matrix(this.weightMatrix.getRows(), this.weightMatrix.getCols());
    }
    
    
    /**
     * Returns this layer's Matrix holding the previous weight changes.
     * @return This layer's Matrix holding the previous weight changes.
     */
    public Matrix getDeltaMatrix(){
        return this.deltaWeights;
    }
    
    
    /**
     * Sets the value at the specified index to the specified value.
     * 
     * @param col the column index
     * @param row the row index
     * @param val the value to set as
     */
    public void setDeltaMatrixValue(int row, int col, double val){
        this.deltaWeights.set(row, col, val);
    }
}



//add into present in feedforwardnetwork if you want multithreading

/*
    
*/