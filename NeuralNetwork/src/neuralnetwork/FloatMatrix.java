package neuralnetwork;



public class FloatMatrix {
    
    /////////////////////////////////////////////
    //                Inits                    //
    /////////////////////////////////////////////
    
    
    //<editor-fold defaultstate="collapsed" desc="Inits">
    
    private float[][] mat;
    
    //used in FloatMatrix initializions with one dimensional arrays
    public static int ROW = 0;
    public static int COLUMN = 1;
    
    
    //false matrix for impossible values
    public static FloatMatrix NaM = new FloatMatrix(0,0);
    
    
    public FloatMatrix(int row, int col){
        if(  (row>0) && (col>0)  ){
            mat = new float[row][col];
            for(float[] da : mat){
                for(float d : da){
                    d = 0;
                }
            }
        }
    }
    
    
    public FloatMatrix(float[][] vals){
        mat = vals;
    }
    
    
    public FloatMatrix(float[] vals, int rowOrColumn){
        if(rowOrColumn==FloatMatrix.ROW){
            mat = new float[1][vals.length];
            mat[0] = vals;
        }else if(rowOrColumn==FloatMatrix.COLUMN){
            mat = new float[vals.length][1];
            for(int i = 0; i<vals.length; i++){
                mat[i][0] = vals[i];
            }
        }else{
            NeuralNetwork.say("rowOrColumn is not a know choice, value: "+rowOrColumn);
        }
    }
    
    
    @Override
    public String toString(){
        //gets the longest number in the matrix
        int length = 1;
        for(float[] da : mat){
            for(float d : da){
                if((d+"").length()>length)length = (d+"").length();
            }
        }
        
        //writes the string
        String ret = "";
        int padding = 1;//the extra padding between the numbers
        for(float[] da : mat){
            ret = ret+"|";
            for(float d : da){
                int l = length - (d+"").length()+padding;
                ret = ret+d;
                while(l>=0){
                    ret = ret+" ";
                    l--;
                }
            }
            ret = ret.trim()+"|\n";
        }
        return ret;
    }
    //</editor-fold>
    
    
    
    
    /////////////////////////////////////////////
    //          Setters and Getters            //
    /////////////////////////////////////////////
    
    
    //<editor-fold defaultstate="collapsed" desc="Setters and Getters">
    
    
    /**
     * Returns a completely different FloatMatrix that is equal to this FloatMatrix.
     * 
     * @return A copy of this FloatMatrix.
     */
    public FloatMatrix copy(){
        FloatMatrix ret = new FloatMatrix(this.getRows(),this.getCols());
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                ret.set(i, j, this.get(i, j));
            }
        }
        return ret;
    }
    
    /**
     * Sets the row and column of the FloatMatrix to the specified float value.
     * 
     * @param row the row index
     * @param col the column index
     * @param val the value to set as
     */
    public void set(int row, int col, float val){
        mat[row][col] = val;
    }
    
    
    /**
     * This returns the float at the specified index of the FloatMatrix
     * 
     * @param row the row index
     * @param col the column index
     * @return The float value at the specified index
     */
    public float get(int row, int col){
        return mat[row][col];
    }
    
    
    /**
     * This returns the specified row of this FloatMatrix as a FloatMatrix
     * <p>
     * If @row is greater than the highest index in this FloatMatrix,
     * an error is shown.
     * 
     * @param row the row index
     * @return The FloatMatrix value of the specified row of this FloatMatrix
     */
    public FloatMatrix getRow(int row){
        //In case the row isn't actually in the matrix
        if(row>=this.getRows()){
            NeuralNetwork.say("Row is too large, row: "+row+"  mat length: "+this.getRows());
            return null;
        }
        
        FloatMatrix ret = new FloatMatrix(1,this.getCols());
        for(int i = 0; i<this.getCols(); i++){
            ret.set(0, i, this.get(row, i));
        }
        return ret;
    }
    
    
    /**
     * This returns the specified row of this FloatMatrix as an array
     * <p>
     * If @row is greater than the highest index in this FloatMatrix,
     * an error is shown.
     * 
     * @param row the row index
     * @return The array value of the specified row of this FloatMatrix
     */
    public float[] getRowAsArray(int row){
        return this.getRow(row).toPackedArray();
    }
    
    
    /**
     * Sets the row of this FloatMatrix to the specified float array.
     * <p>
     * If the length of @vals is smaller than the number of columns in this 
     * array, the remaining spots are filled with "0.0".
     * <p>
     * If the length of @vals is larger than the number of columns in this
     * array, the extra spots are discarded.
     * 
     * @param row the row index
     * @param vals the values to set the specified row in this FloatMatrix to
     */
    public void setRow(int row, float[] vals){
        int max = this.getCols();
        if(vals.length>max)max = vals.length;
        for(int i = 0; i<max; i++){
            if(i<this.getCols()){
                if(i<vals.length){
                    this.set(row, i, vals[i]);
                }else{
                    this.set(row, i, 0);
                }
            }
        }
    }
    
    
    /**
     * Sets the row of this FloatMatrix to the specified FloatMatrix.
     * <p>
     * If the @m does not have exactly one row, an error is shown.
     * <p>
     * If the length of @m is smaller than the number of columns in this 
     * array, the remaining spots are filled with "0.0".
     * <p>
     * If the length of @m is larger than the number of columns in this
     * array, the extra spots are discarded.
     * 
     * @param row the row index
     * @param m the matrix to set the specified row to
     */
    public void setRow(int row, FloatMatrix m){
        if(m.getRows()==1){
            this.setRow(row, m.getRowAsArray(0));
        }else{
            NeuralNetwork.say("Cannot set as row, matrix has too many rows: \n"+m);
        }
    }
    
    
    
    
    /**
     * Sets the column of this FloatMatrix to the specified float array.
     * <p>
     * If the length of @vals is smaller than the number of rows in this 
     * array, the remaining spots are filled with "0.0".
     * <p>
     * If the length of @vals is larger than the number of rows in this
     * array, the extra spots are discarded.
     * 
     * @param col the column index
     * @param vals the values to set the specified row in this FloatMatrix to
     */
    public void setCol(int col, float[] vals){
        int max = this.getRows();
        if(vals.length>max)max = vals.length;
        for(int i = 0; i<max; i++){
            if(i<this.getRows()){
                if(i<vals.length){
                    this.set(i, col, vals[i]);
                }else{
                    this.set(i, col, 0);
                }
            }
        }
    }
    
    
    
    /**
     * Sets the column of this FloatMatrix to the specified FloatMatrix.
     * <p>
     * If the @m does not have exactly one column, an error is shown.
     * <p>
     * If the length of @m is smaller than the number of rows in this 
     * array, the remaining spots are filled with "0.0".
     * <p>
     * If the length of @m is larger than the number of rows in this
     * array, the extra spots are discarded.
     * 
     * @param col the column index
     * @param m the matrix to set the specified row to
     */
    public void setCol(int col, FloatMatrix m){
        if(m.getCols()==1){
            this.setCol(col, m.getColAsArray(0));
        }else{
            NeuralNetwork.say("Cannot set as col, matrix has too many cols: \n"+m);
        }
    }
    
    
    
    /**
     * This returns the specified row of this FloatMatrix as a FloatMatrix
     * <p>
     * If @col is greater than the highest index in this FloatMatrix,
     * an error is shown.
     * 
     * @param col the column index
     * @return The FloatMatrix value of the specified row of this FloatMatrix
     */
    public FloatMatrix getCol(int col){
        if(col>this.getCols()){
            NeuralNetwork.say("Row is too large, row: "+col+"  mat length: "+this.getCols());
            return null;
        }
        
        FloatMatrix ret = new FloatMatrix(this.getRows(),1);
        for(int i = 0; i<this.getRows(); i++){
            ret.set(i, 0, this.get(i,col));
        }
        return ret;
    }
    
    
    
    /**
     * This returns the specified column of this FloatMatrix as an array
     * <p>
     * If @col is greater than the highest index in this FloatMatrix,
     * an error is shown.
     * 
     * @param col the row index
     * @return The array value of the specified row of this FloatMatrix
     */
    public float[] getColAsArray(int col){
        return this.getCol(col).toPackedArray();
    }
    
    
    
    
    
    /**
     * Sets this FloatMatrix to the specified array.
     * 
     * @param mat the array to set this FloatMatrix as
     */
    public void setFloatMatrix(float[][] mat){
        this.mat = mat;
    }
    
    
    
    
    
    
    /**
     * Returns this FloatMatrix as an array.
     * 
     * @return The array value of this FloatMatrix.
     */
    public float[][] getAsArray(){
        return mat;
    }
    
    
    
    
    /**
     * Returns this FloatMatrix as a single array. If the FloatMatrix
     * has more than one row, the remaining rows are added
     * on to the array from top to bottom.
     * 
     * @return The single array value of this FloatMatrix.
     */
    public float[] toPackedArray(){
        float[] ret = new float[this.getTotalLength()];
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                ret[i+j] = this.get(i, j);
            }
        }
        return ret;
    }
    
    
    
    
    /**
     * Returns the number of rows in this FloatMatrix.
     * 
     * @return The number of rows in this FloatMatrix.
     */
    public int getRows(){
        return mat.length;
    }
    
    
    
    
    /**
     * Returns the number of columns in this FloatMatrix.
     * 
     * @return The number of columns in this FloatMatrix.
     */
    public int getCols(){
        return mat[0].length;
    }
    
    
    
    /**
     * Returns the total length of this FloatMatrix, equivalent to:
     * <p>
     * row * col
     * 
     * @return The total length of this FloatMatrix.
     */
    public int getTotalLength(){
        return this.getCols()*this.getRows();
    }
    
    
    
    /**
     * Returns the sum of every value in the FloatMatrix.
     * 
     * @return The sum of the entire matrix.
     */
    public float getSum(){
        int ret = 0;
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                ret += this.get(i, j);
            }
        }
        return ret;
    }
    //</editor-fold>
    
    
    
    
    
    
    
    /////////////////////////////////////////////
    //          Non-Static Operators           //
    /////////////////////////////////////////////
    
    
    //<editor-fold defaultstate="collapsed" desc="Non-Static Operators">
    
    
    /**
     * Resets this FloatMatrix to the default, zero-filled FloatMatrix.
     */
    public void clear(){
        this.toEachMultiply(0);
    }
    
    
    
    
    
    /**
     * Removes the specified row from this FloatMatrix.
     * <p>
     * If @row is not within the bounds of this FloatMatrix, no
     * operations are performed, and an error is shown.
     * 
     * @param row the row to remove from this FloatMatrix
     */
    public void removeRow(int row){
        if( row<0 || row>=this.getRows() ){
            NeuralNetwork.say("Cannot remove row: "+row+" in FloatMatrix: \n"+this);
        }else{
            float[][] d = new float[this.getRows()-1][this.getCols()];
            int index = 0;
            for(int i = 0; i<this.getRows(); i++){
                if(i!=row) d[i-index] = this.getRowAsArray(i);
                else index++;
            }
            this.mat = d;
        }
    }
    
    
    
    
    
    
    /**
     * Adds the FloatMatrix @row to this FloatMatrix.
     * <p>
     * If @row is not a Row, an error is shown, and nothing is done.
     * <p>
     * If @index is not within the bounds of this FloatMatrix, an error is shown,
     * and nothing is done.
     * <p>
     * @row is added using this.setRow(index).
     * 
     * @param row the row to add
     * @param index the index to add @row to
     */
    public void addRow(FloatMatrix row, int index){
        if(index<=this.getRows()&&index>=0&&row.getRows()==1){
            FloatMatrix ret = new FloatMatrix(this.getRows()+1,this.getCols());
            int change = 0;
            for(int i = 0; i<ret.getRows(); i++){
                if(i==index){
                    ret.setRow(i, row);
                    change = 1;
                }else{
                    ret.setRow(i, this.getRow(i-change));
                }
            }
            this.setFloatMatrix(ret.getAsArray());
        }else{
            NeuralNetwork.say("Cannot add row:\n"+row+"to index: "+index+" in FloatMatrix:\n"+this);
        }
    }
    
    
    
    
    
    /**
     * Adds the FloatMatrix @col to this FloatMatrix.
     * <p>
     * If @row is not a Column, an error is shown, and nothing is done.
     * <p>
     * If @index is not within the bounds of this FloatMatrix, an error is shown,
     * and nothing is done.
     * <p>
     * @col is added using this.setCol(index).
     * 
     * @param col the row to add
     * @param index the index to add @row to
     */
    public void addCol(FloatMatrix col, int index){
        if(index<=this.getCols()&&index>=0&&col.getCols()==1){
            FloatMatrix ret = new FloatMatrix(this.getRows(),this.getCols()+1);
            int change = 0;
            for(int i = 0; i<ret.getCols(); i++){
                if(i==index){
                    ret.setCol(i, col);
                    change = 1;
                }else{
                    ret.setCol(i, this.getCol(i-change));
                }
            }
            this.setFloatMatrix(ret.getAsArray());
        }else{
            NeuralNetwork.say("Cannot add col:\n"+col+"to index: "+index+" in FloatMatrix:\n"+this);
        }
    }
    
    
    
    
    
    /**
     * Removes the specified column from this FloatMatrix.
     * <p>
     * If @col is not within the bounds of this FloatMatrix, no
     * operations are performed, and an error is shown.
     * 
     * @param col the column to remove from this FloatMatrix
     */
    public void removeCol(int col){
        if( col<0 || col>=this.getCols() ){
            NeuralNetwork.say("Cannot remove row: "+col+" in FloatMatrix: \n"+this);
        }else{
            float[][] d = new float[this.getRows()][this.getCols()-1];
            for(int i = 0; i<this.getRows(); i++){
                int index = 0;
                for(int j = 0; j<this.getCols(); j++){
                    if(j!=col) d[i][j-index] = this.get(i,j);
                    else index++;
                }
            }
            this.mat = d;
        }
    }
    
    
    /**
     * Adds the specified value to every value in the FloatMatrix.
     * 
     * @param val the value to add 
     */
    public void toEachAdd(float val){
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                this.set(i, j, this.get(i, j)+val);
            }
        }
    }
    
    
    
    
    /**
     * Subtracts the specified value from every value in the FloatMatrix.
     * 
     * @param val the value to subtract 
     */
    public void toEachSubtract(float val){
        this.toEachAdd(-val);
    }
    
    
    
    
    /**
     * Multiplies every value in the FloatMatrix by the specified value.
     * 
     * @param val the value to multiply by
     */
    public void toEachMultiply(float val){
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                this.set(i, j, this.get(i, j)*val);
            }
        }
    }
    
    
    
    
    
    /**
     * Divides every value in the FloatMatrix by the specified value.
     * 
     * @param val the value to divide by 
     */
    public void toEachDivide(float val){
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                this.set(i, j, this.get(i, j)/val);
            }
        }
    }
    
    
    
    
    /**
     * Negates this FloatMatrix. Ie: multiplies every value by -1.
     * 
     */
    public void negate(){
        this.toEachMultiply(-1);
    }
    
    //</editor-fold>
    
    
    
    
    
    /////////////////////////////////////////////
    //          Non-Static Booleans            //
    /////////////////////////////////////////////
    
    
    //<editor-fold defaultstate="collapsed" desc="Non-Static Booleans">
    
    
    /**
     * Returns true if this FloatMatrix is exactly the same as @m.
     * 
     * @param m the FloatMatrix to test equivalency to this FloatMatrix.
     * @return True if this FloatMatrix is exactly the same as @m.
     */
    public boolean equals(FloatMatrix m){
        if( (this.getRows()!=m.getRows()) || (this.getCols()!=m.getCols()) ){
            return false;
        }
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                if(this.get(i, j)!=m.get(i, j))return false;
            }
        }
        return true;
    }
    
    
    
    
    
    
    /**
     * Returns true if every row in this FloatMatrix is equal to any row in FloatMatrix @m.
     * 
     * @param m the FloatMatrix to test equivalency to this FloatMatrix.
     * @return True if every row in this FloatMatrix is equal to any row in FloatMatrix @m.
     */
    public boolean rowsEqual(FloatMatrix m){
        for(int i = 0; i<this.getRows(); i++){
            boolean cont = false;
            for(int j = 0; j<m.getRows(); j++){
                if(this.getRow(i).equals(m.getRow(j))){
                    cont = true;
                    break;
                }
            }
            if(!cont)return false;
        }
        return true;
    }
    
    
    
    
    
    
    
    /**
     * Returns true if every col in this FloatMatrix is equal to any col in FloatMatrix @m.
     * 
     * @param m the FloatMatrix to test equivalency to this FloatMatrix.
     * @return True if every col in this FloatMatrix is equal to any col in FloatMatrix @m.
     */
    public boolean colsEqual(FloatMatrix m){
        for(int i = 0; i<this.getCols(); i++){
            boolean cont = false;
            for(int j = 0; j<m.getCols(); j++){
                if(this.getCol(i).equals(m.getCol(j))){
                    cont = true;
                    break;
                }
            }
            if(!cont)return false;
        }
        return true;
    }
    
    
    
    /**
     * Returns true if this FloatMatrix has only one row, or only
     * one column. Ie: if this FloatMatrix can be categorized as a Vector.
     * 
     * @return True if this FloatMatrix has only one row or column.
     */
    public boolean isVector(){
        return this.getCols()==1 || this.getRows()==1;
    }
    
    
    
    /**
     * Returns true if every value in this FloatMatrix is exactly zero.
     * 
     * @return True if every value in this FloatMatrix is zero.
     */
    public boolean isZero(){
        return this.equals(new FloatMatrix(this.getRows(),this.getCols()));
    }
    
    
    
    
    /**
     * Returns true if this FloatMatrix is an identity FloatMatrix.
     * 
     * @return True if this FloatMatrix is an identity FloatMatrix.
     */
    public boolean isIdentityFloatMatrix(){
        return this.equals(FloatMatrix.identityFloatMatrix(this.getCols()));
    }
    
    
    
    /**
     * Returns true if this FloatMatrix contains @row.
     * 
     * @param row the row to check for
     * @return True if thisFloatMatrix contains @row.
     */
    public boolean containsRow(FloatMatrix row){
        for(int i = 0; i<this.getRows(); i++){
            if(row.equals(this.getRow(i)))return true;
        }
        return false;
    }
    
    
    
    
    
    /**
     * Returns true if this FloatMatrix contains @col.
     * 
     * @param col the column to check for
     * @return True if thisFloatMatrix contains @col.
     */
    public boolean containsCol(FloatMatrix col){
        for(int i = 0; i<this.getCols(); i++){
            if(col.equals(this.getCol(i)))return true;
        }
        return false;
    }
    
    
    
    //</editor-fold>
    
    
    
    
    
    
    /////////////////////////////////////////////
    //            Static Operators             //
    /////////////////////////////////////////////
    
    
    //<editor-fold defaultstate="collapsed" desc="Static Operators">
    
    /**
     * Returns the addition of the two Matrices.
     * <p>
     * If @A and @B are not the same size, then an error is shown.
     * 
     * @param A first FloatMatrix to add
     * @param B second FloatMatrix to add
     * @return @A+@B
     */
    public static FloatMatrix add(FloatMatrix A, FloatMatrix B){
        if( (A.getCols()==B.getCols()) && (A.getRows()==B.getRows()) ){
            
            FloatMatrix ret = new FloatMatrix(A.getRows(),A.getCols());
            
            for(int i = 0; i<A.getRows(); i++){
                for(int j = 0; j<A.getCols(); j++){
                    ret.set(i, j,   A.get(i, j) + B.get(i, j)  );
                }
            }
            
            return ret;
            
        }else{
            NeuralNetwork.say("FloatMatrix A is not the same size as FloatMatrix B. FloatMatrix A: "
                    +A.getRows()+","+A.getCols()+"    FloatMatrix B: "+B.getRows()+","+B.getCols());
        }
        return NaM;
    }
    
    
    
    
    /**
     * Returns the subtraction of the two Matrices: @A-@B;
     * <p>
     * If @A and @B are not the same size, then an error is shown.
     * 
     * @param A the first FloatMatrix
     * @param B the FloatMatrix to subtract from @A
     * @return @A-@B
     */
    public static FloatMatrix subtract(FloatMatrix A, FloatMatrix B){
        B.negate();
        return FloatMatrix.add(A, B);
    }
    
    
    
    
    
    
    /**
     * Returns the multiplication of the two Matrices: @A*@B;
     * <p>
     * If the number of columns in @A does not equal the number of rows in @B, an error is shown.
     * 
     * @param A the first FloatMatrix
     * @param B the FloatMatrix to multiply @A by
     * @return @A*@B
     */
    public static FloatMatrix multiply(FloatMatrix A, FloatMatrix B){
        if(  A.getCols()==B.getRows()  ){
            FloatMatrix ret = new FloatMatrix(A.getRows(),B.getCols());
            for(int i = 0; i<ret.getRows(); i++){
                for(int j = 0; j<ret.getCols(); j++){
                    ret.set(i, j,  FloatMatrix.dotProduct(A.getRow(i), B.getCol(j)) );
                }
            }
            return ret;
        }else{
            NeuralNetwork.say("Cannot multiply these Matrices, FloatMatrix A: "+
                    A.getRows()+","+A.getCols()+"   FloatMatrix B: "+B.getRows()+","+B.getCols());
        }
        return NaM;
    }
    
    
    
    
    
    /**
     * Returns the division of the two Matrices: @A/@B;
     * <p>
     * If the number of columns in @A does not equal the number of rows in @B,
     * or vice versa, an error is shown.
     * 
     * @param A the first FloatMatrix
     * @param B the FloatMatrix to multiply @A by
     * @return @A*@B
     */
    public static FloatMatrix divide(FloatMatrix A, FloatMatrix B){
        if(  (A.getCols()==B.getRows()) && (A.getRows()==B.getCols())  ){
            return FloatMatrix.multiply(A, FloatMatrix.inverse(B));
        }else{
            NeuralNetwork.say("Cannot divide these Matrices, FloatMatrix A: "+
                    A.getRows()+","+A.getCols()+"   FloatMatrix B: "+B.getRows()+","+B.getCols());
        }
        return NaM;
    }
    
    
    
    
    /**
     * Returns the dot product of the two matrices: A⋅B.
     * <p>
     * If Matrices A and B are not both vectors, an error is shown.
     * <p>
     * If the lengths of each FloatMatrix are not equal, an error is shown.
     * 
     * @param A the first FloatMatrix
     * @param B the second FloatMatrix
     * @return A⋅B
     */
    public static float dotProduct(FloatMatrix A, FloatMatrix B){
        if(  A.isVector() && B.isVector() && (A.getTotalLength()==B.getTotalLength())  ){
            float[] a = A.toPackedArray();
            float[] b = B.toPackedArray();
            float ret = 0;
            for(int i = 0; i<a.length; i++){
                ret += a[i]*b[i];
            }
            return ret;
        }else{
            NeuralNetwork.say("Cannot perfom a dot product on these Matrices, FloatMatrix A: "+
                    A.getRows()+","+A.getCols()+"   FloatMatrix B: "+B.getRows()+","+B.getCols());
        }
        return Float.NaN;
    }
    
    
    
    
    /**
     * Returns the identity matrix of size @size.
     * <p>
     * If @size is less than or equal to zero, an error is shown.
     * 
     * @param size the size of the identity matrix
     * @return the identity matrix of size @size.
     */
    public static FloatMatrix identityFloatMatrix(int size){
        if(size>0){
            FloatMatrix ret = new FloatMatrix(size,size);
            for(int i = 0; i<size; i++){
                ret.set(i, i, 1);
            }
            return ret;
        }else{
            NeuralNetwork.say("Cannot create identity matrix of size: "+size);
        }
        return NaM;
    }
    
    
    
    
    /**
     * Returns the length of the vector @m.
     * <p>
     * If @m is not a vector, an error is shown.
     * 
     * @param m the vector
     * @return The length of vector @m.
     */
    public static float vectorLength(FloatMatrix m){
        if(m.isVector()){
            float ret = 0;
            for(float d : m.toPackedArray()){
                ret += d*d;
            }
            return (float)Math.sqrt(ret);
        }else{
            NeuralNetwork.say("Cannot find length of FloatMatrix m, it is not a vector. m: "
            +m.getRows()+","+m.getCols());
        }
        return Float.NaN;
    }
    
    
    
    
    
    
    
    /**
     * Returns the transposed FloatMatrix of @m. This operation flips all 
     * of the columns and rows in @m and returns that matrix.
     * 
     * @param m the matrix to transpose
     * @return the transposed matrix of @m
     */
    public static FloatMatrix transpose(FloatMatrix m){
        FloatMatrix ret = new FloatMatrix(m.getCols(),m.getRows());
        for(int i = 0; i<m.getCols(); i++){
            ret.setRow(i, m.getCol(i).toPackedArray());
        }
        return ret;
    }
    
    
    
    
    
    /**
     * Returns the inverse of a FloatMatrix, equal to "adj(@m) * det(@m)" where det(@m)
     * is the determinate of @m, and adj(@m) is the adjoint of @m.
     * <p>
     * If the number of rows does not equal the number of columns, an error is shown.
     * <p>
     * If the determinate is 0, an error is shown.
     * 
     * @param m the FloatMatrix to take the inverse of.
     * @return The inverse of @m.
     */
    public static FloatMatrix inverse(FloatMatrix m){
        if(m.getRows()!=m.getCols()){
            NeuralNetwork.say("Cannot take the inverse of FloatMatrix m: "+m.getCols()+","+m.getRows());
            return NaM;
        }
        if(m.isIdentityFloatMatrix()){
            return m;
        }
        
        float det = FloatMatrix.determinate(m);
        
        if(det==0){
            NeuralNetwork.say("There is no inverse, the determinate is 0. FloatMatrix: \n"+m);
            return NaM;
        }
        
        FloatMatrix ret = FloatMatrix.adjoint(m);
        ret.toEachMultiply(1/det);
        return ret;
    }
    
    
    
    
    /**
     * Returns the determinate of @m.
     * <p>
     * If the number of rows in @m does not equal the number of columns, an
     * error is shown.
     * 
     * @param m the FloatMatrix to find the determinate of
     * @return The determinate of @m.
     */
    public static float determinate(FloatMatrix m){
        if(m.getCols()!=m.getRows()){
            NeuralNetwork.say("Cannot find the determinate of FloatMatrix m: "+m.getRows()+","+m.getCols());
            return Float.NaN;
        }
        
        //the determinate of [ab/cd] is equal to: a*d-b*c
        if(m.getCols()==2){
            return m.get(0, 0)*m.get(1, 1)-m.get(0, 1)*m.get(1, 0);
        }
        
        //if m is not a 2x2 matrix, return {m*adj(m)}[0][0], where adj(m) is the adjoint of FloatMatrix m
        //and [0][0] is the value at index (0,0)
        return FloatMatrix.multiply(m, FloatMatrix.adjoint(m)).get(0,0);
    }
    
    
    
    
    
    /**
     * Returns the adjoint of FloatMatrix @m, equal to the transpose of the cofactor FloatMatrix.
     * <p>
     * If the rows of FloatMatrix @m does not equal the columns, an error is shown.
     * 
     * @param m the matrix to find the adjoint of.
     * @return The adjoint of FloatMatrix @m.
     */
    public static FloatMatrix adjoint(FloatMatrix m){
        if(m.getRows()!=m.getCols()){
            NeuralNetwork.say("Cannot find the adjoint of FloatMatrix m: "+m.getRows()+","+m.getCols());
        }
        return FloatMatrix.transpose(FloatMatrix.cofactorFloatMatrix(m));
    }
    
    
    
    
    
    /**
     * Returns the cofactor FloatMatrix of FloatMatrix @m, equal to the FloatMatrix of all cofactors.
     * <p>
     * If the rows of FloatMatrix @m does not equal the columns, an error is shown.
     * 
     * @param m the matrix to find the cofactor matrix
     * @return the cofactor FloatMatrix of @m.
     */
    public static FloatMatrix cofactorFloatMatrix(FloatMatrix m){
        if(m.getRows()!=m.getCols()){
            NeuralNetwork.say("Cannot find the cofactor FloatMatrix of FloatMatrix m: "+m.getRows()+","+m.getCols());
        }
        
        FloatMatrix ret = new FloatMatrix(m.getRows(),m.getCols());
        
        for(int i = 0; i<m.getRows(); i++){
            for(int j = 0; j<m.getCols(); j++){
                ret.set(i, j, FloatMatrix.cofactor(m,i,j));
            }
        }
        
        return ret;
    }
    
    
    
    
    /**
     * Returns the cofactor of FloatMatrix @m, equal to (-1)^(@i+@j) * det(m.removeRow(i).removeCol(j)),
     * where det(m) is the determinate of FloatMatrix m.
     * 
     * @param m the matrix to find the cofactor of
     * @param i the index of the row to remove in FloatMatrix @m
     * @param j the index of the column to remove in FloatMatrix @m
     * @return The cofactor of FloatMatrix @m.
     */
    public static float cofactor(FloatMatrix m, int i, int j){
        FloatMatrix temp = m.copy();
        temp.removeRow(i);
        temp.removeCol(j);
        int mult = -1;
        if(  (i+j)%2==0  )mult = 1;
        return FloatMatrix.determinate(temp)*mult;
    }
    
    
    
    
    
    /**
     * Generates a random FloatMatrix of size @width by @length.
     * <p>
     * If @low equals @high, a FloatMatrix of size @width by @length and values @low is returned.
     * <p>
     * If size is less than 1, an error is shown.
     * <p>
     * If @low is larger than @high, an error is shown.
     * 
     * @param width the width of the FloatMatrix
     * @param length the length of the FloatMatrix
     * @param low the lowest possible random value
     * @param high the highest possible random value
     * @param useInts if true, the returned FloatMatrix will only have integer values
     * @return A random FloatMatrix of size @size.
     */
    public static FloatMatrix generateRandomFloatMatrix(int width, int length, float low, float high, boolean useInts){
        if(width<=0||length<=0){
            NeuralNetwork.say("Cannot generate a random matrix of size: "+width+" ,"+length);
            return NaM;
        }
        if(low>high){
            NeuralNetwork.say("Cannot generate a random matrix with low: "+low+"  and high: "+high);
            return NaM;
        }
        if(low==high){
            FloatMatrix ret = new FloatMatrix(width,length);
            ret.toEachAdd(low);
            return ret;
        }
        FloatMatrix ret = new FloatMatrix(width,length);
        for(int i = 0; i<width; i++){
            for(int j = 0; j<length; j++){
                float rand = (float)((high-low)*Math.random()+low);
                if(useInts)rand = (int)Math.round(rand);
                ret.set(i, j, rand);
            }
        }
        return ret;
    }
    
    
    
    
    
    
    
    /**
     * Generates a random square FloatMatrix of size @size with specified possible values.
     * <p>
     * If size is less than 1, an error is shown.
     * 
     * @param size the size of the square FloatMatrix
     * @param vals the possible values to use in the random FloatMatrix
     * @return A random FloatMatrix of size @size.
     */
    public static FloatMatrix generateRandomFloatMatrix(int size, float[] vals){
        if(size<=0){
            NeuralNetwork.say("Cannot generate a random matrix of size: "+size);
            return NaM;
        }
        FloatMatrix ret = new FloatMatrix(size,size);
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                int rand = (int)Math.round( (vals.length-1)*Math.random() );
                ret.set(i, j, vals[rand]);
            }
        }
        return ret;
    }
    
    
    
    
    
    /**
     * Generates a random square FloatMatrix of size @size with only two possible values.
     * <p>
     * If size is less than 1, an error is shown.
     * 
     * @param size the size of the square FloatMatrix
     * @param fal the value for false
     * @param tru the value for true
     * @return A random FloatMatrix of size @size.
     */
    public static FloatMatrix generateRandomBinaryFloatMatrix(int size, int fal, int tru){
        float[] vals = {fal,tru};
        return FloatMatrix.generateRandomFloatMatrix(size, vals);
    }
    
    
    
    
    //</editor-fold>
    
    
    
}





// to do: fix FloatMatrix(float[][] vals) init in case some weird float[][] is passed in