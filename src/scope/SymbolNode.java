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

    public boolean add(SymbolType symbolType, String type){
        String val = payload.get(symbolType);
        if(val == null){
            payload.put(symbolType, type);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "SymbolNode{" +
                "payload=" + payload +
                '}';
    }

    private HashMap<SymbolType, String> payload;

}
