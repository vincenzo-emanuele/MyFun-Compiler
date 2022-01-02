package nodes;

public class IdListOp extends SyntaxNode{
    public IdListOp(ExprNode expr){
        super("IdListOp");
        add(expr);
    }

    public IdListOp(ExprNode expr, IdListOp list){
        super("IdListOp");
        add(expr);
        add(list);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
