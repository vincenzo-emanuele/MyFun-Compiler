package nodes;

public class ConstOp extends SyntaxNode{
    public ConstOp(Object constant){
        super(constant);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
