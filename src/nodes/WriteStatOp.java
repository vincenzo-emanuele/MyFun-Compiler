package nodes;

public class WriteStatOp extends SyntaxNode{
    public WriteStatOp(String label, ExprNode expr){
        super(label);
        add(expr);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
