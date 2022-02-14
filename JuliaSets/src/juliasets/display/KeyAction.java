package juliasets.display;

/**
 * The actions that will be taken when a key is pressed
 * @author Babynado
 */
public abstract class KeyAction{
    
    int[] keys;
    
    public KeyAction(int key){
        this.keys = new int[1];
        this.keys[0] = key;
    }
    
    /**
     * If multiple keys will lead to the same action
     * @param keys 
     */
    public KeyAction(int[] keys){
        this.keys = keys;
    }
    
    public abstract void doAction();
    
    /**
     * Checks to see if the given key that was pressed is a key which activates
     * this KeyAction, and if so, performs the action
     * @param key 
     */
    public void update(int key){
        for(int k : keys){
            if(k==key){
                this.doAction();
            }
        }
    }
    
}