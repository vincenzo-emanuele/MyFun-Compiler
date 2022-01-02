package nodes;

public class AssignStatOp extends SyntaxNode{
    public AssignStatOp(IdOp lval, ExprNode rval){
        super("AssignStatOp");
        add(lval);
        add(rval);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
