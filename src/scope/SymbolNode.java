package scope;

import java_cup.runtime.Symbol;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class SymbolNode extends DefaultMutableTreeNode {

    public SymbolNode(SymbolNode father){
        super("SymbolNode");
        payload = new LinkedHashMap<>();
        if(father != null){
            this.setParent(father);
        }
    }

    public HashMap<SymbolType, String> getPayload() {
        return payload;
    }

    public void setPayload(LinkedHashMap<SymbolType, String> payload) {
        this.payload = payload;
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

    public String lookup(SymbolType key){
        SymbolNode temp = this;
        String out;
        while(temp != null){
            out = temp.getPayload().get(key);
            if(out != null){
                return out;
            }
            temp = (SymbolNode) temp.getParent();
        }
        return null;
    }

    public String partialLookup(SymbolType lval, SymbolType rval, SymbolType context){
        if(payload.containsKey(rval)){
            System.out.println("CONTAINS");
            Object[] arr = payload.keySet().toArray();
            int lvalIndex = 0;
            int rvalIndex = 0;
            //Controllo se l'rval è stato dichiarato nella tabella locale
            for(int i = 0; i < arr.length; i++){
                if(arr[i].equals(rval)){
                    rvalIndex = i;
                    break;
                }
            }
            //Cerco l'lval
            for(int i = 0; i < arr.length; i++){
                if(arr[i].equals(lval)){
                    lvalIndex = i;
                    break;
                }
            }
            System.out.println("LVAL INDEX: " + lvalIndex);
            System.out.println("RVAL INDEX: " + rvalIndex);
            if(lvalIndex > rvalIndex){
                return payload.get(rval);
            }
        }
        //Cerco rval nelle tabelle degli antenati
        SymbolNode temp = (SymbolNode) this.getParent();
        boolean found = false;
        int keyIndex = 0;
        int contextIndex = -1;
        while(temp != null){
            Object[] arr = temp.getPayload().keySet().toArray();
            for(int i = 0; i < arr.length; i++){
                if(arr[i].equals(rval)){
                    keyIndex = i;
                    found = true;
                    break;
                }
            }
            if(!found) {
                temp = (SymbolNode) temp.getParent();
            } else {
                break;
            }
        }
        if(temp == null){
            System.out.println("NON TROVATO L'RVAL");
            return null;
        }
        //Cerco il context
        Object[] arr = temp.getPayload().keySet().toArray();
        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(context)){
                contextIndex = i;
                break;
            }
        }
        if(contextIndex == -1) return temp.getPayload().get(rval);
        if(contextIndex > keyIndex){
            return temp.getPayload().get(rval);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "SymbolNode{" +
                "payload=" + payload +
                '}';
    }

    private LinkedHashMap<SymbolType, String> payload;

}
