package nodes;

public class VarDeclOpList extends SyntaxNode {

    public VarDeclOpList(){
        super("VarDeclOpList");
    }

    public VarDeclOpList(VarDeclOp varDeclOp, VarDeclOpList varDeclOpList){
        super("VarDeclOpList");
        add(varDeclOp);
        add(varDeclOpList);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
