package scope;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;

public class SymbolNode extends DefaultMutableTreeNode {

    public SymbolNode(SymbolNode father){
        super("SymbolNode");
        payload = new HashMap<>();
        if(father != null){
            //this.setParent(father);
        }
    }

    public boolean add(String name, SymbolType type){
        if(payload.containsKey(name)) return false;
        payload.put(name, type);
        return true;
    }

    @Override
    public String toString() {
        return "SymbolNode{" +
                "payload=" + payload +
                '}';
    }

    private HashMap<String, SymbolType> payload;

}
