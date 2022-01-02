package nodes;

public class WhileStatOp extends SyntaxNode {
    public WhileStatOp(ExprNode exprNode, VarDeclOpList varDeclOpList, StatOpList list){
        super("WhileStatOp");
        add(exprNode);
        add(varDeclOpList);
        add(list);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
