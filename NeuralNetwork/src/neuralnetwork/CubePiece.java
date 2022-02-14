package neuralnetwork;

import java.util.ArrayList;

public class CubePiece {

    /////////////////////////////////////////////
    //                Inits                    //
    /////////////////////////////////////////////
    //<editor-fold defaultstate="collapsed" desc="Inits">
    public CubePiece(Matrix points) {
        if (points.getCols() == 3) {
            this.points = points;
        } else {
            NeuralNetwork.say("Use a 3d Matrix! Column size: " + points.getCols());
        }
    }

    //the data set of all the points
    private Matrix points;

    //Rotational values
    public static final int ROT_X = 1;
    public static final int ROT_Y = 2;
    public static final int ROT_Z = 3;

    @Override
    public String toString() {
        return this.points + "";
    }

    //</editor-fold>
    
    
    
    /////////////////////////////////////////////
    //          Setters and Getters            //
    /////////////////////////////////////////////
    //<editor-fold defaultstate="collapsed" desc="Setters and Getters">
    /**
     * Sets the points of this CubePiece to the specified Matrix.
     *
     * @param points the points to set this CubePiece as.
     */
    public void setPoints(Matrix points) {
        this.points = points;
    }

    /**
     * Gets the points of this CubePiece as a Matrix.
     *
     * @return The Matrix of the points that make up this CubePiece.
     */
    public Matrix getPoints() {
        return this.points;
    }

    /**
     * Returns the number of pieces that make up this CubePiece.
     *
     * @return the number of pieces that make up this CubePiece.
     */
    public int getLength() {
        return this.points.getRows();
    }

    //</editor-fold>
    
    
    
    /////////////////////////////////////////////
    //            Non-Static Operators         //
    /////////////////////////////////////////////
    //<editor-fold defaultstate="collapsed" desc="Non-Static Operators">
    /**
     * Rotates this CubePiece by 90 degrees around the axis @rot.
     * <p>
     * If @rot is not a recognized axis, no operation is performed, and an error
     * is shown.
     *
     * @param rot the axis to rotate around.
     */
    public void rotate(int rot) {
        int transform = 1;
        if (rot < 0) {
            transform = -1;
            rot *= -1;
        }
        if (rot == 2 * ROT_X || rot == 2 * ROT_Y || rot == 2 * ROT_Z) {
            rot /= 2;
            transform *= 2;
        }
        if (rot == ROT_X || rot == ROT_Y || rot == ROT_Z) {
            Matrix m = CubePiece.getRotationMatrix(rot, transform * Math.PI / 2);
            for (int i = 0; i < this.points.getRows(); i++) {
                this.points.setRow(i, Matrix.transpose(Matrix.multiply(m,
                        Matrix.transpose(this.points.getRow(i)))));
            }
        } else {
            NeuralNetwork.say("Cannot rotate around axis: " + rot);
        }
    }

    /**
     * Sets the specified row index of this.getPoints() as the origin.
     *
     *
     * @param row the row to set as origin.
     */
    public void setOrigin(int row) {
        Matrix mrow = this.points.getRow(row);
        for (int i = 0; i < this.points.getRows(); i++) {
            Matrix m = this.points.getRow(i);
            double[] d = {m.get(0, 0) - mrow.get(0, 0), m.get(0, 1) - mrow.get(0, 1), m.get(0, 2) - mrow.get(0, 2)};
            this.points.setRow(i, new Matrix(d, Matrix.ROW));
        }
    }

    /**
     * Compares this CubePiece to the specified CubePiece. Returns true if they
     * are the same pieces rotated and/or translated.
     *
     * @param c the CubePiece to compare to
     * @return True if the CubePieces are equal, False otherwise.
     */
    public boolean comparePiece(CubePiece c) {
        if (this.getLength() != c.getLength()) {
            return false;
        }
        for (int i = 0; i < this.getLength(); i++) {
            this.setOrigin(i);
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    if (this.getPoints().rowsEqual(c.getPoints())) {
                        return true;
                    } else {
                        this.rotate(CubePiece.ROT_Y);
                    }
                }
                this.rotate(CubePiece.ROT_Z);
            }
            this.rotate(CubePiece.ROT_X);
            for (int j = 0; j < 4; j++) {
                if (this.getPoints().rowsEqual(c.getPoints())) {
                    return true;
                } else {
                    this.rotate(CubePiece.ROT_Y);
                }
            }
            this.rotate(2 * CubePiece.ROT_X);
            for (int j = 0; j < 4; j++) {
                if (this.getPoints().rowsEqual(c.getPoints())) {
                    return true;
                } else {
                    this.rotate(CubePiece.ROT_Y);
                }
            }
            this.rotate(CubePiece.ROT_X);
        }
        return false;
    }

    //</editor-fold>
    
    
    
    /////////////////////////////////////////////
    //            Static Operators             //
    /////////////////////////////////////////////
    //<editor-fold defaultstate="collapsed" desc="Static Operators">
    /**
     * Returns the 3d rotational Matrix equivalent to the type of rotation @rot
     * rotating @radian radians.
     * <p>
     * If @rot is not an acceptable rotation, an error is shown.
     *
     * @param rot the type of rotation
     * @param radians the number of radians to rotate.
     * @return the rotational Matrix of rotation type @rot.
     */
    public static Matrix getRotationMatrix(int rot, double radians) {

        switch (rot) {
            case ROT_X:
                double[][] valX = {{1, 0, 0},
                {0, Math.round(Math.cos(radians)), Math.round(-Math.sin(radians))},
                {0, Math.round(Math.sin(radians)), Math.round(Math.cos(radians))}};
                return new Matrix(valX);
            case ROT_Y:
                double[][] valY = {{Math.round(Math.cos(radians)), 0, Math.round(Math.sin(radians))},
                {0, 1, 0},
                {Math.round(-Math.sin(radians)), 0, Math.round(Math.cos(radians))}};
                return new Matrix(valY);
            case ROT_Z:
                double[][] valZ = {{Math.round(Math.cos(radians)), Math.round(-Math.sin(radians)), 0},
                {Math.round(Math.sin(radians)), Math.round(Math.cos(radians)), 0},
                {0, 0, 1}};
                return new Matrix(valZ);
            default:
                NeuralNetwork.say("Cannot generate a rotational matrix around: " + rot);
                return Matrix.NaM;
        }
    }

    /**
     * Returns a randomly generated CubePiece of size @size.
     *
     * @param size the size of the CubePiece
     * @return A randomly generated CubePiece.
     */
    public static CubePiece generateRandomCubePiece(int size) {
        Matrix ret = new Matrix(1, 3);
        for (int i = 1; i < size; i++) {
            while (true) {
                Matrix m;
                int rand = (int) Math.round((ret.getRows() - 1) * Math.random());
                m = ret.getRow(rand);
                rand = (int) Math.round(2 * Math.random());
                double set = m.get(0, rand) + Util.doubleToBipolar(Math.round(Math.random()));
                if (Math.abs(set) > (int) NeuralNetwork.BASE / 2) {
                    continue;
                }
                m.set(0, rand, set);

                if (!ret.containsRow(m)) {
                    ret.addRow(m, ret.getRows() - 1);
                    break;
                }
            }
        }
        return new CubePiece(ret);
    }

    /**
     * Returns a randomly rotated version of the specified CubePeice.
     *
     * @param c the CubePiece to rotate
     * @return A randomly rotated version of the specified CubePeice.
     */
    public static CubePiece rotateRandomly(CubePiece c) {
        int x = (int) Math.round(3 * Math.random());
        int y = (int) Math.round(3 * Math.random());
        int z = (int) Math.round(3 * Math.random());

        for (int i = 0; i < x; i++) {
            c.rotate(ROT_X);
        }
        for (int i = 0; i < y; i++) {
            c.rotate(ROT_Y);
        }
        for (int i = 0; i < z; i++) {
            c.rotate(ROT_Z);
        }
        return c;
    }
    
    
    
    
    

    /**
     * Generates a list of random CubePiece pairs.
     *
     * @param n the number of CubePiece pairs to generate
     * @param c the max size of each CubePiece
     * @param sameSize if this is true, all of the CubePieces will be size @c
     * @param similarity whether or not each CubePiece pair should be equal.
     * @return A list of random CubePiece pairs.
     */
    public static ArrayList<String> generateCubeData(int n, int c, boolean sameSize, boolean similarity) {
        ArrayList<String> r = new ArrayList<>();
        if (similarity) {
            for (int i = 0; i < n; i++) {

                //The CubePieces
                CubePiece c1 = CubePiece.generateRandomCubePiece(c);
                CubePiece c2 = CubePiece.rotateRandomly(c1);

                String ret = Util.euclideanToBipoints(c1.getPoints()) + Util.euclideanToBipoints(c2.getPoints()) + "1";
                if (r.contains(ret)) {
                    i--;
                    continue;
                }
                r.add(ret);
            }

            return r;
        }
        for (int i = 0; i < n; i++) {

            int c1d = c;
            int c2d = c;
            if (!sameSize) {
                c1d = (int) Math.round(NeuralNetwork.BASE * NeuralNetwork.BASE * Math.random() + 1);
                c2d = (int) Math.round(NeuralNetwork.BASE * NeuralNetwork.BASE * Math.random() + 1);
            }

            //The CubePieces
            CubePiece c1 = CubePiece.generateRandomCubePiece(c1d);
            CubePiece c2 = CubePiece.generateRandomCubePiece(c2d);
            if (c1.comparePiece(c2)) {
                i--;
                continue;
            }

            String add;
            if (NeuralNetwork.act == FeedforwardNetwork.TANH) {
                add = "-1";
            } else {
                add = "0";
            }
            
            String ret = Util.euclideanToBipoints(c1.getPoints()) + Util.euclideanToBipoints(c2.getPoints()) + add;
            if (r.contains(ret)) {
                i--;
                continue;
            }
            r.add(ret);
        }

        return r;
    }

    //</editor-fold>
}
