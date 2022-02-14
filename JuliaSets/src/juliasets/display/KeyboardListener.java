package juliasets.display;

import java.util.HashMap;

/**
 * Handles all of the keystrokes so as to not make the code as messy in the
 * JFrame.
 * @author Babynado
 */
public class KeyboardListener {
    
    public HashMap values;
    
    //What actions will be taken
    public KeyAction[] actions;
    
    /**
     * Constructor with a HashMap that holds values that will change
     * @param values 
     */
    public KeyboardListener(HashMap values){
        this.values = values;
    }
    
    /**
     * No HashMap, just override new actions
     */
    public KeyboardListener(){}
    
    /**
     * This method should be called any time a key is pressed. Attempts to 
     * update the KeyActions.
     * @param key 
     */
    public void update(int key){
        for(KeyAction ka : actions){
            ka.update(key);
        }
    }
    
    
}


