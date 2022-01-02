package nodes;

public class ElseOp extends SyntaxNode{
    public ElseOp(){
        super("ElseOp");
    }
    public ElseOp(VarDeclOpList varDeclOpList, StatOpList statOpList){
        super("ElseOp");
        add(varDeclOpList);
        add(statOpList);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
