package neuralnetwork;



public class Matrix {
    
    /////////////////////////////////////////////
    //                Inits                    //
    /////////////////////////////////////////////
    
    
    //<editor-fold defaultstate="collapsed" desc="Inits">
    
    private double[][] mat;
    
    //used in Matrix initializions with one dimensional arrays
    public static int ROW = 0;
    public static int COLUMN = 1;
    
    
    //false matrix for impossible values
    public static Matrix NaM = new Matrix(0,0);
    
    
    public Matrix(int row, int col){
        if(  (row>0) && (col>0)  ){
            mat = new double[row][col];
            for(double[] da : mat){
                for(double d : da){
                    d = 0;
                }
            }
        }
    }
    
    
    public Matrix(double[][] vals){
        mat = vals;
    }
    
    
    public Matrix(double[] vals, int rowOrColumn){
        if(rowOrColumn==Matrix.ROW){
            mat = new double[1][vals.length];
            mat[0] = vals;
        }else if(rowOrColumn==Matrix.COLUMN){
            mat = new double[vals.length][1];
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
        for(double[] da : mat){
            for(double d : da){
                if((d+"").length()>length)length = (d+"").length();
            }
        }
        
        //writes the string
        String ret = "";
        int padding = 1;//the extra padding between the numbers
        for(double[] da : mat){
            ret = ret+"|";
            for(double d : da){
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
     * Returns a completely different Matrix that is equal to this Matrix.
     * 
     * @return A copy of this Matrix.
     */
    public Matrix copy(){
        Matrix ret = new Matrix(this.getRows(),this.getCols());
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                ret.set(i, j, this.get(i, j));
            }
        }
        return ret;
    }
    
    /**
     * Sets the row and column of the Matrix to the specified double value.
     * 
     * @param row the row index
     * @param col the column index
     * @param val the value to set as
     */
    public void set(int row, int col, double val){
        mat[row][col] = val;
    }
    
    
    /**
     * This returns the double at the specified index of the Matrix
     * 
     * @param row the row index
     * @param col the column index
     * @return The double value at the specified index
     */
    public double get(int row, int col){
        return mat[row][col];
    }
    
    
    /**
     * This returns the specified row of this Matrix as a Matrix
     * <p>
     * If @row is greater than the highest index in this Matrix,
     * an error is shown.
     * 
     * @param row the row index
     * @return The Matrix value of the specified row of this Matrix
     */
    public Matrix getRow(int row){
        //In case the row isn't actually in the matrix
        if(row>=this.getRows()){
            NeuralNetwork.say("Row is too large, row: "+row+"  mat length: "+this.getRows());
            return null;
        }
        
        Matrix ret = new Matrix(1,this.getCols());
        for(int i = 0; i<this.getCols(); i++){
            ret.set(0, i, this.get(row, i));
        }
        return ret;
    }
    
    
    /**
     * This returns the specified row of this Matrix as an array
     * <p>
     * If @row is greater than the highest index in this Matrix,
     * an error is shown.
     * 
     * @param row the row index
     * @return The array value of the specified row of this Matrix
     */
    public double[] getRowAsArray(int row){
        return this.getRow(row).toPackedArray();
    }
    
    
    /**
     * Sets the row of this Matrix to the specified double array.
     * <p>
     * If the length of @vals is smaller than the number of columns in this 
     * array, the remaining spots are filled with "0.0".
     * <p>
     * If the length of @vals is larger than the number of columns in this
     * array, the extra spots are discarded.
     * 
     * @param row the row index
     * @param vals the values to set the specified row in this Matrix to
     */
    public void setRow(int row, double[] vals){
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
     * Sets the row of this Matrix to the specified Matrix.
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
    public void setRow(int row, Matrix m){
        if(m.getRows()==1){
            this.setRow(row, m.getRowAsArray(0));
        }else{
            NeuralNetwork.say("Cannot set as row, matrix has too many rows: \n"+m);
        }
    }
    
    
    
    
    /**
     * Sets the column of this Matrix to the specified double array.
     * <p>
     * If the length of @vals is smaller than the number of rows in this 
     * array, the remaining spots are filled with "0.0".
     * <p>
     * If the length of @vals is larger than the number of rows in this
     * array, the extra spots are discarded.
     * 
     * @param col the column index
     * @param vals the values to set the specified row in this Matrix to
     */
    public void setCol(int col, double[] vals){
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
     * Sets the column of this Matrix to the specified Matrix.
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
    public void setCol(int col, Matrix m){
        if(m.getCols()==1){
            this.setCol(col, m.getColAsArray(0));
        }else{
            NeuralNetwork.say("Cannot set as col, matrix has too many cols: \n"+m);
        }
    }
    
    
    
    /**
     * This returns the specified row of this Matrix as a Matrix
     * <p>
     * If @col is greater than the highest index in this Matrix,
     * an error is shown.
     * 
     * @param col the column index
     * @return The Matrix value of the specified row of this Matrix
     */
    public Matrix getCol(int col){
        if(col>this.getCols()){
            NeuralNetwork.say("Row is too large, row: "+col+"  mat length: "+this.getCols());
            return null;
        }
        
        Matrix ret = new Matrix(this.getRows(),1);
        for(int i = 0; i<this.getRows(); i++){
            ret.set(i, 0, this.get(i,col));
        }
        return ret;
    }
    
    
    
    /**
     * This returns the specified column of this Matrix as an array
     * <p>
     * If @col is greater than the highest index in this Matrix,
     * an error is shown.
     * 
     * @param col the row index
     * @return The array value of the specified row of this Matrix
     */
    public double[] getColAsArray(int col){
        return this.getCol(col).toPackedArray();
    }
    
    
    
    
    
    /**
     * Sets this Matrix to the specified array.
     * 
     * @param mat the array to set this Matrix as
     */
    public void setMatrix(double[][] mat){
        this.mat = mat;
    }
    
    
    
    
    
    
    /**
     * Returns this Matrix as an array.
     * 
     * @return The array value of this Matrix.
     */
    public double[][] getAsArray(){
        return mat;
    }
    
    
    
    
    /**
     * Returns this Matrix as a single array. If the Matrix
     * has more than one row, the remaining rows are added
     * on to the array from top to bottom.
     * 
     * @return The single array value of this Matrix.
     */
    public double[] toPackedArray(){
        double[] ret = new double[this.getTotalLength()];
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                ret[i+j] = this.get(i, j);
            }
        }
        return ret;
    }
    
    
    
    
    /**
     * Returns the number of rows in this Matrix.
     * 
     * @return The number of rows in this Matrix.
     */
    public int getRows(){
        return mat.length;
    }
    
    
    
    
    /**
     * Returns the number of columns in this Matrix.
     * 
     * @return The number of columns in this Matrix.
     */
    public int getCols(){
        return mat[0].length;
    }
    
    
    
    /**
     * Returns the total length of this Matrix, equivalent to:
     * <p>
     * row * col
     * 
     * @return The total length of this Matrix.
     */
    public int getTotalLength(){
        return this.getCols()*this.getRows();
    }
    
    
    
    /**
     * Returns the sum of every value in the Matrix.
     * 
     * @return The sum of the entire matrix.
     */
    public double getSum(){
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
     * Resets this Matrix to the default, zero-filled Matrix.
     */
    public void clear(){
        this.toEachMultiply(0);
    }
    
    
    
    
    
    /**
     * Removes the specified row from this Matrix.
     * <p>
     * If @row is not within the bounds of this Matrix, no
     * operations are performed, and an error is shown.
     * 
     * @param row the row to remove from this Matrix
     */
    public void removeRow(int row){
        if( row<0 || row>=this.getRows() ){
            NeuralNetwork.say("Cannot remove row: "+row+" in Matrix: \n"+this);
        }else{
            double[][] d = new double[this.getRows()-1][this.getCols()];
            int index = 0;
            for(int i = 0; i<this.getRows(); i++){
                if(i!=row) d[i-index] = this.getRowAsArray(i);
                else index++;
            }
            this.mat = d;
        }
    }
    
    
    
    
    
    
    /**
     * Adds the Matrix @row to this Matrix.
     * <p>
     * If @row is not a Row, an error is shown, and nothing is done.
     * <p>
     * If @index is not within the bounds of this Matrix, an error is shown,
     * and nothing is done.
     * <p>
     * @row is added using this.setRow(index).
     * 
     * @param row the row to add
     * @param index the index to add @row to
     */
    public void addRow(Matrix row, int index){
        if(index<=this.getRows()&&index>=0&&row.getRows()==1){
            Matrix ret = new Matrix(this.getRows()+1,this.getCols());
            int change = 0;
            for(int i = 0; i<ret.getRows(); i++){
                if(i==index){
                    ret.setRow(i, row);
                    change = 1;
                }else{
                    ret.setRow(i, this.getRow(i-change));
                }
            }
            this.setMatrix(ret.getAsArray());
        }else{
            NeuralNetwork.say("Cannot add row:\n"+row+"to index: "+index+" in Matrix:\n"+this);
        }
    }
    
    
    
    
    
    /**
     * Adds the Matrix @col to this Matrix.
     * <p>
     * If @row is not a Column, an error is shown, and nothing is done.
     * <p>
     * If @index is not within the bounds of this Matrix, an error is shown,
     * and nothing is done.
     * <p>
     * @col is added using this.setCol(index).
     * 
     * @param col the row to add
     * @param index the index to add @row to
     */
    public void addCol(Matrix col, int index){
        if(index<=this.getCols()&&index>=0&&col.getCols()==1){
            Matrix ret = new Matrix(this.getRows(),this.getCols()+1);
            int change = 0;
            for(int i = 0; i<ret.getCols(); i++){
                if(i==index){
                    ret.setCol(i, col);
                    change = 1;
                }else{
                    ret.setCol(i, this.getCol(i-change));
                }
            }
            this.setMatrix(ret.getAsArray());
        }else{
            NeuralNetwork.say("Cannot add col:\n"+col+"to index: "+index+" in Matrix:\n"+this);
        }
    }
    
    
    
    
    
    /**
     * Removes the specified column from this Matrix.
     * <p>
     * If @col is not within the bounds of this Matrix, no
     * operations are performed, and an error is shown.
     * 
     * @param col the column to remove from this Matrix
     */
    public void removeCol(int col){
        if( col<0 || col>=this.getCols() ){
            NeuralNetwork.say("Cannot remove row: "+col+" in Matrix: \n"+this);
        }else{
            double[][] d = new double[this.getRows()][this.getCols()-1];
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
     * Adds the specified value to every value in the Matrix.
     * 
     * @param val the value to add 
     */
    public void toEachAdd(double val){
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                this.set(i, j, this.get(i, j)+val);
            }
        }
    }
    
    
    
    
    /**
     * Subtracts the specified value from every value in the Matrix.
     * 
     * @param val the value to subtract 
     */
    public void toEachSubtract(double val){
        this.toEachAdd(-val);
    }
    
    
    
    
    /**
     * Multiplies every value in the Matrix by the specified value.
     * 
     * @param val the value to multiply by
     */
    public void toEachMultiply(double val){
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                this.set(i, j, this.get(i, j)*val);
            }
        }
    }
    
    
    
    
    
    /**
     * Divides every value in the Matrix by the specified value.
     * 
     * @param val the value to divide by 
     */
    public void toEachDivide(double val){
        for(int i = 0; i<this.getRows(); i++){
            for(int j = 0; j<this.getCols(); j++){
                this.set(i, j, this.get(i, j)/val);
            }
        }
    }
    
    
    
    
    /**
     * Negates this Matrix. Ie: multiplies every value by -1.
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
     * Returns true if this Matrix is exactly the same as @m.
     * 
     * @param m the Matrix to test equivalency to this Matrix.
     * @return True if this Matrix is exactly the same as @m.
     */
    public boolean equals(Matrix m){
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
     * Returns true if every row in this Matrix is equal to any row in Matrix @m.
     * 
     * @param m the Matrix to test equivalency to this Matrix.
     * @return True if every row in this Matrix is equal to any row in Matrix @m.
     */
    public boolean rowsEqual(Matrix m){
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
     * Returns true if every col in this Matrix is equal to any col in Matrix @m.
     * 
     * @param m the Matrix to test equivalency to this Matrix.
     * @return True if every col in this Matrix is equal to any col in Matrix @m.
     */
    public boolean colsEqual(Matrix m){
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
     * Returns true if this Matrix has only one row, or only
     * one column. Ie: if this Matrix can be categorized as a Vector.
     * 
     * @return True if this Matrix has only one row or column.
     */
    public boolean isVector(){
        return this.getCols()==1 || this.getRows()==1;
    }
    
    
    
    /**
     * Returns true if every value in this Matrix is exactly zero.
     * 
     * @return True if every value in this Matrix is zero.
     */
    public boolean isZero(){
        return this.equals(new Matrix(this.getRows(),this.getCols()));
    }
    
    
    
    
    /**
     * Returns true if this Matrix is an identity Matrix.
     * 
     * @return True if this Matrix is an identity Matrix.
     */
    public boolean isIdentityMatrix(){
        return this.equals(Matrix.identityMatrix(this.getCols()));
    }
    
    
    
    /**
     * Returns true if this Matrix contains @row.
     * 
     * @param row the row to check for
     * @return True if thisMatrix contains @row.
     */
    public boolean containsRow(Matrix row){
        for(int i = 0; i<this.getRows(); i++){
            if(row.equals(this.getRow(i)))return true;
        }
        return false;
    }
    
    
    
    
    
    /**
     * Returns true if this Matrix contains @col.
     * 
     * @param col the column to check for
     * @return True if thisMatrix contains @col.
     */
    public boolean containsCol(Matrix col){
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
     * @param A first Matrix to add
     * @param B second Matrix to add
     * @return @A+@B
     */
    public static Matrix add(Matrix A, Matrix B){
        if( (A.getCols()==B.getCols()) && (A.getRows()==B.getRows()) ){
            
            Matrix ret = new Matrix(A.getRows(),A.getCols());
            
            for(int i = 0; i<A.getRows(); i++){
                for(int j = 0; j<A.getCols(); j++){
                    ret.set(i, j,   A.get(i, j) + B.get(i, j)  );
                }
            }
            
            return ret;
            
        }else{
            NeuralNetwork.say("Matrix A is not the same size as Matrix B. Matrix A: "
                    +A.getRows()+","+A.getCols()+"    Matrix B: "+B.getRows()+","+B.getCols());
        }
        return NaM;
    }
    
    
    
    
    /**
     * Returns the subtraction of the two Matrices: @A-@B;
     * <p>
     * If @A and @B are not the same size, then an error is shown.
     * 
     * @param A the first Matrix
     * @param B the Matrix to subtract from @A
     * @return @A-@B
     */
    public static Matrix subtract(Matrix A, Matrix B){
        B.negate();
        return Matrix.add(A, B);
    }
    
    
    
    
    
    
    /**
     * Returns the multiplication of the two Matrices: @A*@B;
     * <p>
     * If the number of columns in @A does not equal the number of rows in @B, an error is shown.
     * 
     * @param A the first Matrix
     * @param B the Matrix to multiply @A by
     * @return @A*@B
     */
    public static Matrix multiply(Matrix A, Matrix B){
        if(  A.getCols()==B.getRows()  ){
            Matrix ret = new Matrix(A.getRows(),B.getCols());
            for(int i = 0; i<ret.getRows(); i++){
                for(int j = 0; j<ret.getCols(); j++){
                    ret.set(i, j,  Matrix.dotProduct(A.getRow(i), B.getCol(j)) );
                }
            }
            return ret;
        }else{
            NeuralNetwork.say("Cannot multiply these Matrices, Matrix A: "+
                    A.getRows()+","+A.getCols()+"   Matrix B: "+B.getRows()+","+B.getCols());
        }
        return NaM;
    }
    
    
    
    
    
    /**
     * Returns the division of the two Matrices: @A/@B;
     * <p>
     * If the number of columns in @A does not equal the number of rows in @B,
     * or vice versa, an error is shown.
     * 
     * @param A the first Matrix
     * @param B the Matrix to multiply @A by
     * @return @A*@B
     */
    public static Matrix divide(Matrix A, Matrix B){
        if(  (A.getCols()==B.getRows()) && (A.getRows()==B.getCols())  ){
            return Matrix.multiply(A, Matrix.inverse(B));
        }else{
            NeuralNetwork.say("Cannot divide these Matrices, Matrix A: "+
                    A.getRows()+","+A.getCols()+"   Matrix B: "+B.getRows()+","+B.getCols());
        }
        return NaM;
    }
    
    
    
    
    /**
     * Returns the dot product of the two matrices: A⋅B.
     * <p>
     * If Matrices A and B are not both vectors, an error is shown.
     * <p>
     * If the lengths of each Matrix are not equal, an error is shown.
     * 
     * @param A the first Matrix
     * @param B the second Matrix
     * @return A⋅B
     */
    public static double dotProduct(Matrix A, Matrix B){
        if(  A.isVector() && B.isVector() && (A.getTotalLength()==B.getTotalLength())  ){
            double[] a = A.toPackedArray();
            double[] b = B.toPackedArray();
            double ret = 0;
            for(int i = 0; i<a.length; i++){
                ret += a[i]*b[i];
            }
            return ret;
        }else{
            NeuralNetwork.say("Cannot perfom a dot product on these Matrices, Matrix A: "+
                    A.getRows()+","+A.getCols()+"   Matrix B: "+B.getRows()+","+B.getCols());
        }
        return Double.NaN;
    }
    
    
    
    
    /**
     * Returns the identity matrix of size @size.
     * <p>
     * If @size is less than or equal to zero, an error is shown.
     * 
     * @param size the size of the identity matrix
     * @return the identity matrix of size @size.
     */
    public static Matrix identityMatrix(int size){
        if(size>0){
            Matrix ret = new Matrix(size,size);
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
    public static double vectorLength(Matrix m){
        if(m.isVector()){
            double ret = 0;
            for(double d : m.toPackedArray()){
                ret += d*d;
            }
            return Math.sqrt(ret);
        }else{
            NeuralNetwork.say("Cannot find length of Matrix m, it is not a vector. m: "
            +m.getRows()+","+m.getCols());
        }
        return Double.NaN;
    }
    
    
    
    
    
    
    
    /**
     * Returns the transposed Matrix of @m. This operation flips all 
     * of the columns and rows in @m and returns that matrix.
     * 
     * @param m the matrix to transpose
     * @return the transposed matrix of @m
     */
    public static Matrix transpose(Matrix m){
        Matrix ret = new Matrix(m.getCols(),m.getRows());
        for(int i = 0; i<m.getCols(); i++){
            ret.setRow(i, m.getCol(i).toPackedArray());
        }
        return ret;
    }
    
    
    
    
    
    /**
     * Returns the inverse of a Matrix, equal to "adj(@m) * det(@m)" where det(@m)
     * is the determinate of @m, and adj(@m) is the adjoint of @m.
     * <p>
     * If the number of rows does not equal the number of columns, an error is shown.
     * <p>
     * If the determinate is 0, an error is shown.
     * 
     * @param m the Matrix to take the inverse of.
     * @return The inverse of @m.
     */
    public static Matrix inverse(Matrix m){
        if(m.getRows()!=m.getCols()){
            NeuralNetwork.say("Cannot take the inverse of Matrix m: "+m.getCols()+","+m.getRows());
            return NaM;
        }
        if(m.isIdentityMatrix()){
            return m;
        }
        
        double det = Matrix.determinate(m);
        
        if(det==0){
            NeuralNetwork.say("There is no inverse, the determinate is 0. Matrix: \n"+m);
            return NaM;
        }
        
        Matrix ret = Matrix.adjoint(m);
        ret.toEachMultiply(1/det);
        return ret;
    }
    
    
    
    
    /**
     * Returns the determinate of @m.
     * <p>
     * If the number of rows in @m does not equal the number of columns, an
     * error is shown.
     * 
     * @param m the Matrix to find the determinate of
     * @return The determinate of @m.
     */
    public static double determinate(Matrix m){
        if(m.getCols()!=m.getRows()){
            NeuralNetwork.say("Cannot find the determinate of Matrix m: "+m.getRows()+","+m.getCols());
            return Double.NaN;
        }
        
        //the determinate of [ab/cd] is equal to: a*d-b*c
        if(m.getCols()==2){
            return m.get(0, 0)*m.get(1, 1)-m.get(0, 1)*m.get(1, 0);
        }
        
        //if m is not a 2x2 matrix, return {m*adj(m)}[0][0], where adj(m) is the adjoint of Matrix m
        //and [0][0] is the value at index (0,0)
        return Matrix.multiply(m, Matrix.adjoint(m)).get(0,0);
    }
    
    
    
    
    
    /**
     * Returns the adjoint of Matrix @m, equal to the transpose of the cofactor Matrix.
     * <p>
     * If the rows of Matrix @m does not equal the columns, an error is shown.
     * 
     * @param m the matrix to find the adjoint of.
     * @return The adjoint of Matrix @m.
     */
    public static Matrix adjoint(Matrix m){
        if(m.getRows()!=m.getCols()){
            NeuralNetwork.say("Cannot find the adjoint of Matrix m: "+m.getRows()+","+m.getCols());
        }
        return Matrix.transpose(Matrix.cofactorMatrix(m));
    }
    
    
    
    
    
    /**
     * Returns the cofactor Matrix of Matrix @m, equal to the Matrix of all cofactors.
     * <p>
     * If the rows of Matrix @m does not equal the columns, an error is shown.
     * 
     * @param m the matrix to find the cofactor matrix
     * @return the cofactor Matrix of @m.
     */
    public static Matrix cofactorMatrix(Matrix m){
        if(m.getRows()!=m.getCols()){
            NeuralNetwork.say("Cannot find the cofactor Matrix of Matrix m: "+m.getRows()+","+m.getCols());
        }
        
        Matrix ret = new Matrix(m.getRows(),m.getCols());
        
        for(int i = 0; i<m.getRows(); i++){
            for(int j = 0; j<m.getCols(); j++){
                ret.set(i, j, Matrix.cofactor(m,i,j));
            }
        }
        
        return ret;
    }
    
    
    
    
    /**
     * Returns the cofactor of Matrix @m, equal to (-1)^(@i+@j) * det(m.removeRow(i).removeCol(j)),
     * where det(m) is the determinate of Matrix m.
     * 
     * @param m the matrix to find the cofactor of
     * @param i the index of the row to remove in Matrix @m
     * @param j the index of the column to remove in Matrix @m
     * @return The cofactor of Matrix @m.
     */
    public static double cofactor(Matrix m, int i, int j){
        Matrix temp = m.copy();
        temp.removeRow(i);
        temp.removeCol(j);
        int mult = -1;
        if(  (i+j)%2==0  )mult = 1;
        return Matrix.determinate(temp)*mult;
    }
    
    
    
    
    
    /**
     * Generates a random Matrix of size @width by @length.
     * <p>
     * If @low equals @high, a Matrix of size @width by @length and values @low is returned.
     * <p>
     * If size is less than 1, an error is shown.
     * <p>
     * If @low is larger than @high, an error is shown.
     * 
     * @param width the width of the Matrix
     * @param length the length of the Matrix
     * @param low the lowest possible random value
     * @param high the highest possible random value
     * @param useInts if true, the returned Matrix will only have integer values
     * @return A random Matrix of size @size.
     */
    public static Matrix generateRandomMatrix(int width, int length, double low, double high, boolean useInts){
        if(width<=0||length<=0){
            NeuralNetwork.say("Cannot generate a random matrix of size: "+width+" ,"+length);
            return NaM;
        }
        if(low>high){
            NeuralNetwork.say("Cannot generate a random matrix with low: "+low+"  and high: "+high);
            return NaM;
        }
        if(low==high){
            Matrix ret = new Matrix(width,length);
            ret.toEachAdd(low);
            return ret;
        }
        Matrix ret = new Matrix(width,length);
        for(int i = 0; i<width; i++){
            for(int j = 0; j<length; j++){
                double rand = (high-low)*Math.random()+low;
                if(useInts)rand = (int)Math.round(rand);
                ret.set(i, j, rand);
            }
        }
        return ret;
    }
    
    
    
    
    
    
    
    /**
     * Generates a random square Matrix of size @size with specified possible values.
     * <p>
     * If size is less than 1, an error is shown.
     * 
     * @param size the size of the square Matrix
     * @param vals the possible values to use in the random Matrix
     * @return A random Matrix of size @size.
     */
    public static Matrix generateRandomMatrix(int size, double[] vals){
        if(size<=0){
            NeuralNetwork.say("Cannot generate a random matrix of size: "+size);
            return NaM;
        }
        Matrix ret = new Matrix(size,size);
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                int rand = (int)Math.round( (vals.length-1)*Math.random() );
                ret.set(i, j, vals[rand]);
            }
        }
        return ret;
    }
    
    
    
    
    
    /**
     * Generates a random square Matrix of size @size with only two possible values.
     * <p>
     * If size is less than 1, an error is shown.
     * 
     * @param size the size of the square Matrix
     * @param fal the value for false
     * @param tru the value for true
     * @return A random Matrix of size @size.
     */
    public static Matrix generateRandomBinaryMatrix(int size, int fal, int tru){
        double[] vals = {fal,tru};
        return Matrix.generateRandomMatrix(size, vals);
    }
    
    
    
    
    //</editor-fold>
    
    
    
}





// to do: fix Matrix(double[][] vals) init in case some weird double[][] is passed in