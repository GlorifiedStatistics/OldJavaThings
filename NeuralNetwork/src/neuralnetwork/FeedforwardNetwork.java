package neuralnetwork;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FeedforwardNetwork {

    public static final int TANH = 0;
    public static final int SIG = 1;
    public static final int BACKPROPOGATION = 0;
    public static final int SIMULATED_ANNEALING = 1;
    private final ArrayList<FeedforwardLayer> layers;

    public FeedforwardNetwork(int inputSize) {
        this.layers = new ArrayList<>();
        this.activationFunction = SIG;
        this.learningRate = 1;
        this.inputSize = inputSize;
        this.learningType = BACKPROPOGATION;
    }
    
    public int activationFunction;
    public int learningType;
    public double lastError = 100;
    public double highestError = 100;
    public int numberReallyWrong = 0;
    public boolean focusedTraining = false;
    public boolean higherRate = false;
    private double learningRate;
    private final int inputSize;
    
    
    
    /**
     * Sets this network's learning type to the specified type.
     * 
     * @param type set the network to this learning type
     */
    public void setLearningType(int type){
        this.learningType = type;
    }
    
    
    /**
     * Sets this network's learning rate. A higher learning rate learns faster,
     * but a smaller learning rate learns more precisely.
     * 
     * @param rate the new learning rate
     */
    public void setLearningRate(double rate){
        this.learningRate = rate;
    }
    
    
    /**
     * Sets this network's activation function
     *
     * @param act the activation function to set as
     */
    public void setActivationFunction(int act) {
        activationFunction = act;
    }

    
    
    /**
     * Adds a FeedforwardLayer to this network of size @size.
     *
     * @param size the size of the FeedforwardLayer to add
     */
    public void addLayer(int size) {
        FeedforwardLayer layer = new FeedforwardLayer(size, this);
        this.layers.add(layer);
    }

    
    
    /**
     * Returns the FeedforwardLayer at index @index.
     *
     * @param index the index of the layer to return
     * @return The FeedforwardLayer at index @index.
     */
    public FeedforwardLayer getLayer(int index) {
        return this.layers.get(index);
    }

    
    
    /**
     * Initializes the FeedforwardNetwork with all new random weight Matrices.
     */
    public final void initialize() {
        if (this.getSize() < 2) {
            NeuralNetwork.say("FeedforwardNetwork needs more FeedforwardLayers, size: " + this.getSize());
        } else {
            for (int i = 0; i < this.getSize(); i++) {
                int in = inputSize;
                if(i>0)in = this.layers.get(i-1).getSize();
                this.layers.get(i).resetWeightMatrix(in);
                this.layers.get(i).resetDeltaMatrix();
            }
        }
    }

    
    
    /**
     * Trains the Network with the given BiPolar double Array.
     *
     * @param data the data to train this Network with.
     */
    public void train(double[][] data) {
        highestError = 0;
        NeuralNetwork.say("Training FeedforwardNetwork with array of size: " + data.length);

        //sets up the matrix to store the partial derivative data for each layer.
        for (int i = 0; i < this.getSize(); i++) {
            this.getLayer(i).partD = new Matrix(3, this.getLayer(i).getSize());
        }
        
        int displyIndex = NeuralNetwork.display;
        
        numberReallyWrong = 0;
        NeuralNetwork.reallyWrongs = new ArrayList<>();
        double totalError = 0;

        for (double[] d : data) {
            //converts the data and sends it to the input layer
            double[] pres = new double[d.length - 1];
            System.arraycopy(d, 0, pres, 0, pres.length);
            double[] m = this.getLayer(0).present(pres);
            
            //sends the subsequent data to the other layers
            for (int i = 1; i < this.getSize(); i++) {
                m = this.getLayer(i).present(m);
            }
            
            //the output is now a 1x1 array m
            double ans = m[0];
            double correct = d[d.length - 1];
            double error = Math.abs(ans-correct);
            if((displyIndex==NeuralNetwork.display)||(error>0.999&&NeuralNetwork.displayErrors)){
                NeuralNetwork.say("Output: " + ans + "  Expected: " + correct + "  Error: " + error);
                displyIndex = 0;
            }else{
                displyIndex++;
            }
            totalError += error;
            if(error>highestError)highestError = error;
            if(error>0.9){
                this.numberReallyWrong++;
                if(NeuralNetwork.focusOnReallyWrongs)
                NeuralNetwork.reallyWrongs.add(d);
            }
            double adder = 0;
            if(NeuralNetwork.higherRateOnLargeErrors&&error>0.9999){
                adder = 1-learningRate;
            }

            //adjusts the weights if applicable
            if( (NeuralNetwork.learningType==NeuralNetwork.REINFORCED_LEARNING)||(error>=NeuralNetwork.reinforceGap)
                    ||(NeuralNetwork.learningType==NeuralNetwork.ADJUSTED_LEARNING&&error>=(NeuralNetwork.adjustedLearningError)) ){
                for (int layer = this.getSize() - 1; layer >= 0; layer--) {
                    this.getLayer(layer).partD = new Matrix(3,this.getLayer(layer).getSize());
                    Matrix mat = this.getLayer(layer).getWeightMatrix().copy();
                    for (int i = 0; i < mat.getRows(); i++) {
                        for (int j = 0; j < mat.getCols(); j++) {
                            mat.set(i, j, adjustWeight(i, j, layer, mat.get(i, j), ans, correct, adder+learningRate));
                        }
                    }
                    this.getLayer(layer).setWeightMatrix(mat);
                }
            }
            
           
        }
        
        totalError /= data.length;
        this.lastError = totalError;
        NeuralNetwork.say("End of Epoch, Total Error: "+totalError+"  Highest Error: "+highestError);
    }
    
    
    

    
    
    /**
     * Returns the number of FeedforwardLayers in this Network.
     *
     * @return The number of FeedforwardLayers in this Network.
     */
    public int getSize() {
        return this.layers.size();
    }

    
    
    /**
     * Returns the new weight for a weight Matrix.
     *
     * @param i the row index IE: the connection
     * @param j the column index IE: the neuron
     * @param layer the current layer
     * @param w the current weight
     * @param ans the answer generated by the network
     * @param correct the correct answer to the input
     * @param learningRate the learning rate multiplier
     * @return The new weight for a weight Matrix.
     */
    public double adjustWeight(int i, int j, int layer, double w, double ans, double correct, double learningRate) {
        FeedforwardLayer fl = this.getLayer(layer);
        //E: ∂E/∂Oj     O: ∂Oj/∂NETj        N: ∂NETj/∂Wij
        double E, O, N;

        //Solve O
        double in = ans;
        if (layer != this.getSize() - 1) {
            in = this.getLayer(layer + 1).getLastInput().get(j, 0);
        }
        if (this.activationFunction == TANH) {
            O = 1 - Math.tanh(in) * Math.tanh(in);
        } else {
            O = in * (1 - in);
        }
        
        
        //Solve N
        N = fl.getLastInput().get(i, 0);
        
        
        
        //Solve E
        if(layer==this.getSize()-1){
            E = ans-correct;
        }else{
            FeedforwardLayer next = this.getLayer(layer + 1);
            E = 0;
            for(int index = 0; index<next.getSize(); index++){
                E += next.partD.get(0, index)*next.partD.get(1, index)*next.getWeightMatrix().get(j, index);
            }
        }
        
        
        
        
        //Set the values of this neuron
        fl.partD.set(0, j, E);
        fl.partD.set(1, j, O);
        fl.partD.set(2, j, N);

        
        //return the new value
        double delta = (-1*learningRate*E*O*N) + NeuralNetwork.learningMomentum*this.getLayer(layer).getDeltaMatrix().get(i,j);
        this.getLayer(layer).setDeltaMatrixValue(i,j,delta);
        return w+delta;
    }
    
    
    
    
    /**
     * Presents this data set to the network and returns the output
     * 
     * @param data the data to present
     * @return The output of the network.
     */
    public double present(double[] data){
        double[] m = this.getLayer(0).present(data);
        for(int i = 1; i<this.getSize(); i++){
            if(this.getLayer(i).getSize()>3&&NeuralNetwork.multiThread){
                m = this.getLayer(i).presentMultiThread(data);
            }else{
               m = this.getLayer(i).present(m); 
            }
        }
        return m[0];
    }
    
    
    
    
    /**
     * Saves all of the weight Matrices to the File @f.
     * 
     * @param f the file to save to.
     */
    public void saveMatrices(File f){
        BufferedWriter write;
        try{
            f.delete();
            f.createNewFile();
            write = new BufferedWriter(new FileWriter(f));
            for(int i = 0; i<this.getSize(); i++){
                Matrix m = this.getLayer(i).getWeightMatrix();
                for(int j = 0; j<m.getRows(); j++){
                    String s = "";
                    for(double d : m.getRowAsArray(j)){
                        s = s+d+",";
                    }
                    s = s.substring(0,s.length()-1);
                    write.write(s);
                    if(j!=m.getRows()-1)
                    write.newLine();
                }
                if(i!=this.getSize()-1){
                    write.newLine();
                    write.write('*'+"");
                    write.newLine();
                }
            }
            write.flush();
            write.close();
        }catch(IOException e){
            
        }
    }
    
    
    
    
    
    /**
     * Loads the presented array of weight matrices into the FeedforwardLayers
     * 
     * @param mats the Matrices to load.
     */
    public void loadMatrices(Matrix[] mats){
        for(int i = 0; i<mats.length; i++){
            this.getLayer(i).setWeightMatrix(mats[i]);
        }
    }
}
