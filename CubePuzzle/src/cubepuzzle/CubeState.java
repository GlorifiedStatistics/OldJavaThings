package cubepuzzle;

import java.util.ArrayList;

public class CubeState {
    public static void say(Object s){
        CubePuzzle.say(s);
    }
    public CubePiece[] pieces;
    public CubeState(CubePiece[] pieces){
        this.pieces = pieces;
    }
    
    public boolean isSelfIntersecting(){
        ArrayList<Vector3> locations = new ArrayList<>();
        
        for(CubePiece c : pieces){
            for(Vector3 v : c.cubes){//for every vector in each piece
                for(Vector3 v2 : locations){//for each vector in known locations
                    if(v2.equals(v)){
                        return true;
                    }
                }
                locations.add(v);
            }
        }
        
        return false;
    }
    
    public void addCubePiece(CubePiece c){
        CubePiece[] ca = new CubePiece[pieces.length+1];
        for(int i = 0; i<pieces.length; i++){
            ca[i] = pieces[i];
        }
        ca[ca.length-1] = c;
        pieces = ca;
    }
    
    public ArrayList<Vector3> getCubes(){
        ArrayList<Vector3> ret = new ArrayList<>();
        
        for(CubePiece c : pieces){
            for(Vector3 v : c.cubes){
                ret.add(v);
            }
        }
        
        return ret;
    }
    
    public int maxLength(){
        ArrayList<Vector3> cubes = getCubes();
        int max = 0;
        
        for(Vector3 v1 : cubes){
            for(Vector3 v2 : cubes){
                int x = Math.abs(v1.x-v2.x), y = Math.abs(v1.y-v2.y), z = Math.abs(v1.z-v2.z);
                if(x>max)max = x;
                if(y>max)max = y;
                if(z>max)max = z;
            }
        }
        
        return max+1;
    }
    
    public CubeState copy(){
        CubePiece[] newPieces = (CubePiece[])pieces.clone();
        return new CubeState(newPieces);
    }
    
    public boolean equals(CubeState cs){
        for(CubePiece c1 : pieces){
            boolean contained = false;
            for(CubePiece c2 : cs.pieces){
                if(c1.equalsExactly(c2)){
                    contained = true;
                    break;
                }
            }
            if(!contained){
                return false;
            }
        }
        return true;
    }
    
    public boolean containsCube(Vector3 cube){
        for(Vector3 v : getCubes()){
            if(v.equals(cube)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString(){
        String ret = "";
        for(CubePiece c : pieces){
            ret+=c+"\n    ";
        }
        return ret;
    }
}
