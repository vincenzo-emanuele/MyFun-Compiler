package scope;

import nodes.SyntaxNode;

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

    public boolean add(SymbolType symbolType, String type){
        /*String typedef;
        if(symbolType.getTypeDef().equals("Fun")){
            typedef = "Var";
        } else {
            typedef = "Fun";
        }
        SymbolType symbolType1 = new SymbolType(symbolType.getLexeme(), typedef);
        SymbolNode temp = this;
        String out;
        temp = this;
        while(temp != null){
            out = temp.getPayload().get(symbolType1);
            if(out != null){
                return false;
            }
            temp = (SymbolNode) temp.getParent();
        }*/
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

    public String varDeclLookup(SymbolType lval, SymbolType rval, SymbolType context){
        if(payload.containsKey(rval)){
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
        if(contextIndex == -1){
            if (context == null) {
                if (rval.getTypeDef().equals("Fun")) {
                    return null;
                }
            }
            return temp.getPayload().get(rval);
        }
        if(contextIndex > keyIndex){
            return temp.getPayload().get(rval);
        } else {
            return null;
        }
    }

    public String callFunLookup(SymbolType callFun, SymbolType context) {
        SymbolNode temp = this;

        while(temp.getParent() != null) {
            temp = (SymbolNode) temp.getParent();
        }

        int contextIndex = -1;
        int callFunIndex = -1;
        Object[] arr = temp.getPayload().keySet().toArray();
        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(context)){
                contextIndex = i;
                break;
            }
        }

        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(callFun)){
                callFunIndex = i;
                break;
            }
        }
        if (context != null && context.equals(new SymbolType("main", "Fun"))) {
            contextIndex = arr.length;
        }

        if (contextIndex >= callFunIndex) {
            return temp.getPayload().get(callFun);
        }
        return null;
    }

    @Override
    public String toString() {
        return "SymbolNode{" +
                "payload=" + payload +
                '}';
    }

    private LinkedHashMap<SymbolType, String> payload;

}
