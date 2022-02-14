package cubepuzzle;

import java.util.ArrayList;

public class CubePuzzle {
    public static void say(Object s){
        if(s instanceof int[]){
            String sa = "";
            for(int i : (int[])s){
                sa += i+",";
            }
            System.out.println(sa.substring(0, sa.length()-1));
        }
        System.out.println(s);
    }
    
    public static ArrayList<CubeState> states;
    
    public static void main(String[] args) {
        //white starting
        Vector3[] p1 = {new Vector3(0,0,0),new Vector3(1,0,0),new Vector3(1,0,-1)
        ,new Vector3(1,0,-2),new Vector3(1,0,-3),new Vector3(0,0,-2),new Vector3(2,0,-2)
        ,new Vector3(1,1,-3),new Vector3(1,2,-3),new Vector3(1,2,-2)};
        CubePiece white = new CubePiece(p1);
        
        //yellow
        Vector3[] p2 = {new Vector3(0,0,0),new Vector3(-1,0,0),new Vector3(-2,0,0),
        new Vector3(-2,0,-1),new Vector3(-1,0,-1),new Vector3(-1,1,-1),new Vector3(-1,1,-2),
        new Vector3(-1,1,-3),new Vector3(-2,1,-3),};
        CubePiece yellow = new CubePiece(p2);
        
        //orange
        Vector3[] p3 = {new Vector3(0,0,0),new Vector3(0,0,-1),new Vector3(0,0,-2),
        new Vector3(1,0,-1),new Vector3(1,0,-2)};
        CubePiece orange = new CubePiece(p3);
        
        //small blue
        Vector3[] p4 = {new Vector3(0,0,0),new Vector3(0,0,-1),new Vector3(0,0,-2)};
        CubePiece blues = new CubePiece(p4);
        
        //large blue
        Vector3[] p5 = {new Vector3(0,0,0),new Vector3(1,0,0),new Vector3(2,0,0)
        ,new Vector3(3,0,0),new Vector3(3,1,0),new Vector3(2,0,1),new Vector3(2,0,2)};
        CubePiece bluel = new CubePiece(p5);
        
        //red
        Vector3[] p6 = {new Vector3(0,0,0),new Vector3(0,0,1),new Vector3(0,0,2)
        ,new Vector3(1,0,2),new Vector3(1,0,3),new Vector3(1,1,3),new Vector3(2,1,3)};
        CubePiece red = new CubePiece(p6);
        
        //large green
        Vector3[] p7 = {new Vector3(0,0,0),new Vector3(0,0,1),new Vector3(0,0,2)
        ,new Vector3(1,0,0),new Vector3(1,0,1),new Vector3(1,0,2),new Vector3(1,0,3)
        ,new Vector3(1,1,1),new Vector3(2,0,2)};
        CubePiece greenl = new CubePiece(p7);
        
        //small green
        Vector3[] p8 = {new Vector3(0,0,0),new Vector3(1,0,0),new Vector3(1,0,-1)
        ,new Vector3(1,0,-2),new Vector3(1,1,-2)};
        CubePiece greens = new CubePiece(p8);
        
        //black
        Vector3[] p9 = {new Vector3(0,0,0),new Vector3(1,0,0),new Vector3(1,0,-1)
        ,new Vector3(1,0,-2),new Vector3(1,0,-3)};
        CubePiece black = new CubePiece(p9);
        
        //brown
        Vector3[] p10 = {new Vector3(0,0,0),new Vector3(1,0,0),new Vector3(1,0,-1)
        ,new Vector3(1,0,-2)};
        CubePiece brown = new CubePiece(p10);
        
        
        
        CubePiece[] pieces = {yellow, bluel, red, greenl, black, greens, orange, brown, blues};
        
        
        states = new ArrayList<>();
        CubePiece[] ca = {white};
        states.add(new CubeState(ca));
        
        for(int i = 0; i<pieces.length; i++){
            say(states.size());
            states = getNextPossibleStates(states, pieces[i]);
        }
        
        say(states.size());
        for(CubeState cs : states){
            say(cs);
        }

    }
    
    
    
    
    public static ArrayList<CubeState> getNextPossibleStates(ArrayList<CubeState> states, CubePiece c1){
        ArrayList<CubeState> ret = new ArrayList<>();
        CubePiece[] ca = getAllRotations(c1);
        for(CubeState cs : states){
            for(int x = -1; x<4; x++){
                for(int y = -1; y<4; y++){
                    for(int z = 0; z>-4; z--){
                        Vector3 orient = new Vector3(x,y,z);
                        boolean contained = false;
                        for(Vector3 v : cs.getCubes()){
                            if(orient.equals(v)){
                                contained = true;
                            }
                        }
                        if(!contained){
                            for(CubePiece c : ca){
                                if(!c.containsOrigin()){
                                    say("badness");
                                }
                                CubePiece copyPiece = c.copy();
                                copyPiece.orient(orient);
                                CubeState copyState = cs.copy();
                                copyState.addCubePiece(copyPiece);
                                if(!copyState.isSelfIntersecting()&&copyState.maxLength()<=4){
                                    ret.add(copyState);
                                }
                            }
                        }
                    }
                }
            }
        }
        ArrayList<CubeState> newRet = new ArrayList<>();
        for(int i = 0; i<ret.size(); i++){
            boolean contained = false;
            for(int j = i+1; j<ret.size(); j++){
                if(ret.get(i).equals(ret.get(j))){
                    contained = true;
                    break;
                }
            }
            if(!contained){
                newRet.add(ret.get(i));
            }
        }
        return newRet;
    }
    
    public static CubePiece[] getAllRotations(CubePiece c){
        CubePiece[] ret = new CubePiece[25];
        ret[0] = c;
        
        int index = 1;
        
        //z = 0, x's and y's 
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {
                c.rotate(CubePiece.X);
                ret[index] = c.copy();
                index++;
            }
            c.rotate(CubePiece.X);
            c.rotate(CubePiece.Y);
            ret[index] = c.copy();
            index++;
        }
        
        //z = 1
        c.rotate(CubePiece.Y);
        c.rotate(CubePiece.Z);
        ret[index] = c.copy();
        index++;
        
        //z = 1, y = 0, x's
        for (int x = 0; x < 3; x++) {
            c.rotate(CubePiece.X);
            ret[index] = c.copy();
            index++;
        }
        
        //z = 1; y = 2
        c.rotate(CubePiece.X);
        c.rotate(CubePiece.Y);
        c.rotate(CubePiece.Y);
        ret[index] = c.copy();
        index++;
        
        //z = 1, y = 2, x's
        for (int x = 0; x < 3; x++) {
            c.rotate(CubePiece.X);
            ret[index] = c.copy();
            index++;
        }
        
        
        return ret;
    }
    
}

/*
ArrayList<CubePiece> seenList = new ArrayList<>();
        white.rotations = "0Z";
        seenList.add(white.copy());
        
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                for(int k = 0; k<4; k++){
                    white.rotate(CubePiece.X);
                    white.rotations = i+"Z "+j+"Y "+(k+1)+"X";
                    doThing(seenList, white.copy());
                }
                white.rotations = i+"Z "+(j+1)+"Y";
                white.rotate(CubePiece.Y);
                doThing(seenList, white.copy());
            }
            white.rotations = (i+1)+"Z";
            white.rotate(CubePiece.Z);
            doThing(seenList, white.copy());
        }
        
        for(CubePiece c : seenList){
            say(c.rotations);
        }
        say(seenList.size());
    }
    
    public static void doThing(ArrayList<CubePiece> seenList, CubePiece c){
        for(CubePiece c1 : seenList){
            if(c.equals(c1))return;
        }
        seenList.add(c);
    }
*/