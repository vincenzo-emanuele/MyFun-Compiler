package nodes;

public class IfStatOp extends SyntaxNode {
    public IfStatOp(ExprNode exprNode, VarDeclOpList varDeclOpList, StatOpList statOpList, ElseOp elseOp){
        super("IfStatOp");
        add(exprNode);
        add(varDeclOpList);
        add(statOpList);
        add(elseOp);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
