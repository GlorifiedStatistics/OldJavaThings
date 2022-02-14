package cubepuzzle;

public class CubePiece {

    public static void say(Object s) {
        CubePuzzle.say(s);
    }
    public static final int X = 1, AX = 2, Y = 3, AY = 4, Z = 5, AZ = 6;

    public Vector3[] cubes;

    public CubePiece(Vector3[] cubes) {
        this.cubes = cubes;
    }

    public void orient(Vector3 o) {
        for (Vector3 v : cubes) {
            v.add(o);
        }
    }

    public CubePiece copy() {
        Vector3[] newCubes = new Vector3[cubes.length];
        for(int i = 0; i<cubes.length; i++){
            newCubes[i] = cubes[i].copy();
        }
        CubePiece ret = new CubePiece(newCubes);
        return ret;
    }
    
    public boolean containsOrigin(){
        for(Vector3 v : cubes){
            if(v.equals(Vector3.ZERO)){
                return true;
            }
        }
        return false;
    }

    public void rotate(int dir) {
        Vector3[] newPieces = new Vector3[cubes.length];
        for (int i = 0; i < cubes.length; i++) {
            newPieces[i] = new Vector3(0, 0, 0);
            switch (dir) {
                case X:
                    newPieces[i].y = cubes[i].z;
                    newPieces[i].z = -cubes[i].y;
                    newPieces[i].x = cubes[i].x;
                    break;
                case AX:
                    newPieces[i].y = -cubes[i].z;
                    newPieces[i].z = cubes[i].y;
                    newPieces[i].x = cubes[i].x;
                    break;
                case Y:
                    newPieces[i].x = -cubes[i].z;
                    newPieces[i].z = cubes[i].x;
                    newPieces[i].y = cubes[i].y;
                    break;
                case AY:
                    newPieces[i].x = cubes[i].z;
                    newPieces[i].z = -cubes[i].x;
                    newPieces[i].y = cubes[i].y;
                    break;
                case Z:
                    newPieces[i].x = cubes[i].y;
                    newPieces[i].y = -cubes[i].x;
                    newPieces[i].z = cubes[i].z;
                    break;
                case AZ:
                    newPieces[i].x = -cubes[i].y;
                    newPieces[i].y = cubes[i].x;
                    newPieces[i].z = cubes[i].z;
                    break;
            }
        }
        this.cubes = newPieces;
    }

    @Override
    public String toString() {
        String ret = "";
        for (Vector3 v : cubes) {
            ret += v + ",";
        }
        return ret.substring(0, ret.length() - 1);
    }

    public boolean equalsExactly(CubePiece c) {
        if (c.cubes.length != cubes.length) {
            return false;
        }
        for (int i = 0; i < cubes.length; i++) {
            boolean equals = false;
            for (int j = 0; j < c.cubes.length; j++) {
                if (cubes[i].equals(c.cubes[j])) {
                    equals = true;
                    break;
                }
            }
            if (!equals) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(CubePiece c) {
        CubePiece temp = c.copy();
        if (temp.equalsExactly(this)) {
            return true;
        }
        
        //z = 0, x's and y's 
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {
                temp.rotate(CubePiece.X);
                if (temp.equalsExactly(this)) {
                    return true;
                }
            }
            temp.rotate(CubePiece.X);
            temp.rotate(CubePiece.Y);
            if (temp.equalsExactly(this)) {
                return true;
            }
        }
        
        //z = 1
        temp.rotate(Y);
        temp.rotate(Z);
        if (temp.equalsExactly(this)) {
            return true;
        }
        
        //z = 1, y = 0, x's
        for (int x = 0; x < 3; x++) {
            temp.rotate(CubePiece.X);
            if (temp.equalsExactly(this)) {
                return true;
            }
        }
        
        //z = 1; y = 2
        temp.rotate(CubePiece.X);
        temp.rotate(CubePiece.Y);
        temp.rotate(CubePiece.Y);
        if (temp.equalsExactly(this)) {
            return true;
        }
        
        //z = 1, y = 2, x's
        for (int x = 0; x < 3; x++) {
            temp.rotate(CubePiece.X);
            if (temp.equalsExactly(this)) {
                return true;
            }
        }
        
        return false;
    }
}
