package neuralnetwork;


public class HopfieldNeuralNetwork{
    
    public HopfieldNeuralNetwork(int size){
        this.weightMatrix = new Matrix(size,size);
    }
    
    
    //the weight matrrix of this neural network.
    private Matrix weightMatrix;
    
    /**
     * Presents a pattern to the HopfieldNeuralNetwork and returns
     * the resulting pattern.
     * 
     * @param pattern the pattern to present
     * @return The result from the HopfieldNeuralNetwork.
     */
    public boolean[] present(boolean[] pattern){
        boolean[] output = new boolean[pattern.length];
        Matrix inputMatrix = new Matrix(Util.booleanToBipolar(pattern),Matrix.ROW);
        
        for(int col = 0; col<inputMatrix.getCols(); col++){
            
            Matrix columnMatrix = this.weightMatrix.getCol(col);
            columnMatrix = Matrix.transpose(columnMatrix);
            double dotProduct = Matrix.dotProduct(inputMatrix, columnMatrix);
            
            output[col] = dotProduct>0;
                
        }
        
        return output;
    }
    
    
    
    
    /**
     * Trains this HopfieldNeuralNetwork to recognize
     * the specified pattern.
     * 
     * @param pattern the pattern to recognize.
     */
    public void train(boolean[] pattern){
        Matrix m2 = new Matrix(Util.booleanToBipolar(pattern),Matrix.ROW);
        Matrix m1 = Matrix.transpose(m2);
        Matrix m3 = Matrix.multiply(m2, m1);
        Matrix identity = Matrix.identityMatrix(m3.getRows());
        
        Matrix m4 = Matrix.subtract(m3, identity);
        
        this.weightMatrix = Matrix.add(this.getMatrix(), m4);
    }
    
    
    
    
    
    /**
     * Returns the Matrix of the HopfieldNeuralNetwork.
     * 
     * @return The Matrix of the HopfieldNeuralNetwork.
     */
    public Matrix getMatrix(){
        return this.weightMatrix;
    }
    
    
    
    
    
    /**
     * Returns the size of the HopfieldNeuralNetwork - the number
     * of neurons that actually make up this neural network.
     * 
     * @return The size of the HopfieldNeuralNetwork.
     */
    public int getSize(){
        return this.weightMatrix.getCols();
    }
}
