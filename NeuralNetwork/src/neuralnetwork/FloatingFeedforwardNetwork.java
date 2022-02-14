package neuralnetwork;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FloatingFeedforwardNetwork {

    public static final int TANH = 0;
    public static final int SIG = 1;
    public static final int BACKPROPOGATION = 0;
    public static final int SIMULATED_ANNEALING = 1;
    private final ArrayList<FloatingFeedforwardLayer> layers;

    public FloatingFeedforwardNetwork(int inputSize) {
        this.layers = new ArrayList<>();
        this.activationFunction = SIG;
        this.learningRate = 1;
        this.inputSize = inputSize;
        this.learningType = BACKPROPOGATION;
    }
    
    public int activationFunction;
    public int learningType;
    public float lastError = 100;
    public float highestError = 100;
    public int numberReallyWrong = 0;
    public boolean focusedTraining = false;
    public boolean higherRate = false;
    private float learningRate;
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
    public void setLearningRate(float rate){
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
     * Adds a FloatingFeedforwardLayer to this network of size @size.
     *
     * @param size the size of the FloatingFeedforwardLayer to add
     */
    public void addLayer(int size) {
        FloatingFeedforwardLayer layer = new FloatingFeedforwardLayer(size, this);
        this.layers.add(layer);
    }

    
    
    /**
     * Returns the FloatingFeedforwardLayer at index @index.
     *
     * @param index the index of the layer to return
     * @return The FloatingFeedforwardLayer at index @index.
     */
    public FloatingFeedforwardLayer getLayer(int index) {
        return this.layers.get(index);
    }

    
    
    /**
     * Initializes the FloatingFeedforwardNetwork with all new random weight Matrices.
     */
    public final void initialize() {
        if (this.getSize() < 2) {
            NeuralNetwork.say("FloatingFeedforwardNetwork needs more FloatingFeedforwardLayers, size: " + this.getSize());
        } else {
            for (int i = 0; i < this.getSize(); i++) {
                int in = inputSize;
                if(i>0)in = this.layers.get(i-1).getSize();
                this.layers.get(i).resetWeightFloatMatrix(in);
                this.layers.get(i).resetDeltaFloatMatrix();
            }
        }
    }

    
    
    /**
     * Trains the Network with the given BiPolar float Array.
     *
     * @param data the data to train this Network with.
     */
    public void train(float[][] data) {
        highestError = 0;
        NeuralNetwork.say("Training FloatingFeedforwardNetwork with array of size: " + data.length);

        //sets up the matrix to store the partial derivative data for each layer.
        for (int i = 0; i < this.getSize(); i++) {
            this.getLayer(i).partD = new FloatMatrix(3, this.getLayer(i).getSize());
        }
        
        int displyIndex = NeuralNetwork.display;
        
        numberReallyWrong = 0;
        NeuralNetwork.reallyWrongs = new ArrayList<>();
        float totalError = 0;

        for (float[] d : data) {
            //converts the data and sends it to the input layer
            float[] pres = new float[d.length - 1];
            System.arraycopy(d, 0, pres, 0, pres.length);
            float[] m = this.getLayer(0).present(pres);
            
            //sends the subsequent data to the other layers
            for (int i = 1; i < this.getSize(); i++) {
                m = this.getLayer(i).present(m);
            }
            
            //the output is now a 1x1 array m
            float ans = m[0];
            float correct = d[d.length - 1];
            float error = Math.abs(ans-correct);
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
                NeuralNetwork.reallyWrongFloats.add(d);
            }
            float adder = 0;
            if(NeuralNetwork.higherRateOnLargeErrors&&error>0.9999){
                adder = 1-learningRate;
            }

            //adjusts the weights if applicable
            if( (NeuralNetwork.learningType==NeuralNetwork.REINFORCED_LEARNING)||(error>=NeuralNetwork.reinforceGap)
                    ||(NeuralNetwork.learningType==NeuralNetwork.ADJUSTED_LEARNING&&error>=(NeuralNetwork.adjustedLearningError)) ){
                for (int layer = this.getSize() - 1; layer >= 0; layer--) {
                    this.getLayer(layer).partD = new FloatMatrix(3,this.getLayer(layer).getSize());
                    FloatMatrix mat = this.getLayer(layer).getWeightFloatMatrix().copy();
                    for (int i = 0; i < mat.getRows(); i++) {
                        for (int j = 0; j < mat.getCols(); j++) {
                            mat.set(i, j, adjustWeight(i, j, layer, mat.get(i, j), ans, correct, adder+learningRate));
                        }
                    }
                    this.getLayer(layer).setWeightFloatMatrix(mat);
                }
            }
            
           
        }
        
        totalError /= data.length;
        this.lastError = totalError;
        NeuralNetwork.say("End of Epoch, Total Error: "+totalError+"  Highest Error: "+highestError);
    }
    
    
    

    
    
    /**
     * Returns the number of FloatingFeedforwardLayers in this Network.
     *
     * @return The number of FloatingFeedforwardLayers in this Network.
     */
    public int getSize() {
        return this.layers.size();
    }

    
    
    /**
     * Returns the new weight for a weight FloatMatrix.
     *
     * @param i the row index IE: the connection
     * @param j the column index IE: the neuron
     * @param layer the current layer
     * @param w the current weight
     * @param ans the answer generated by the network
     * @param correct the correct answer to the input
     * @param learningRate the learning rate multiplier
     * @return The new weight for a weight FloatMatrix.
     */
    public float adjustWeight(int i, int j, int layer, float w, float ans, float correct, float learningRate) {
        FloatingFeedforwardLayer fl = this.getLayer(layer);
        //E: ∂E/∂Oj     O: ∂Oj/∂NETj        N: ∂NETj/∂Wij
        float E, O, N;

        //Solve O
        float in = ans;
        if (layer != this.getSize() - 1) {
            in = this.getLayer(layer + 1).getLastInput().get(j, 0);
        }
        if (this.activationFunction == TANH) {
            O = (float)(1 - Math.tanh(in) * Math.tanh(in));
        } else {
            O = in * (1 - in);
        }
        
        
        //Solve N
        N = fl.getLastInput().get(i, 0);
        
        
        
        //Solve E
        if(layer==this.getSize()-1){
            E = ans-correct;
        }else{
            FloatingFeedforwardLayer next = this.getLayer(layer + 1);
            E = 0;
            for(int index = 0; index<next.getSize(); index++){
                E += next.partD.get(0, index)*next.partD.get(1, index)*next.getWeightFloatMatrix().get(j, index);
            }
        }
        
        
        
        
        //Set the values of this neuron
        fl.partD.set(0, j, E);
        fl.partD.set(1, j, O);
        fl.partD.set(2, j, N);

        
        //return the new value
        float delta = (float)((-1*learningRate*E*O*N) + NeuralNetwork.learningMomentum*this.getLayer(layer).getDeltaFloatMatrix().get(i,j));
        this.getLayer(layer).setDeltaFloatMatrixValue(i,j,delta);
        return w+delta;
    }
    
    
    
    
    /**
     * Presents this data set to the network and returns the output
     * 
     * @param data the data to present
     * @return The output of the network.
     */
    public float present(float[] data){
        float[] m = this.getLayer(0).present(data);
        for(int i = 1; i<this.getSize(); i++){
            if(this.getLayer(i).getSize()>3&&NeuralNetwork.multiThread){
                
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
                FloatMatrix m = this.getLayer(i).getWeightFloatMatrix();
                for(int j = 0; j<m.getRows(); j++){
                    String s = "";
                    for(float d : m.getRowAsArray(j)){
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
     * Loads the presented array of weight matrices into the FloatingFeedforwardLayers
     * 
     * @param mats the Matrices to load.
     */
    public void loadMatrices(FloatMatrix[] mats){
        for(int i = 0; i<mats.length; i++){
            this.getLayer(i).setWeightFloatMatrix(mats[i]);
        }
    }
}
