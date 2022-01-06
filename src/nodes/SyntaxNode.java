package nodes;

import scope.SymbolNode;

import javax.swing.tree.DefaultMutableTreeNode;

public class SyntaxNode extends DefaultMutableTreeNode implements Visitable{

    public SyntaxNode(){
        super();
    }

    public SyntaxNode(Object obj){
        super(obj);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public SymbolNode getSymbolNode() {
        return symbolNode;
    }

    public void setSymbolNode(SymbolNode symbolNode) {
        this.symbolNode = symbolNode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private SymbolNode symbolNode;
}
