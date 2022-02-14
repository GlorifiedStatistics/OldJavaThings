package cubepuzzle;

public class Vector3 {
    public static void say(Object s){
        CubePuzzle.say(s);
    }
    
    public static final Vector3 ZERO = new Vector3(0,0,0);
    
    public int x, y, z;
    public Vector3(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public String toString(){
        return "("+x+","+y+","+z+")";
    }
    
    public void add(Vector3 v){
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }
    
    public void subtract(Vector3 v){
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }
    
    public boolean equals(Vector3 v){
        return x==v.x&&y==v.y&&z==v.z;
    }
    
    public Vector3 copy(){
        return new Vector3(x,y,z);
    }
}
