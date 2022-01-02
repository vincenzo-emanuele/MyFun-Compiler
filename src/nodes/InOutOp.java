package nodes;

public class InOutOp extends SyntaxNode{

    public InOutOp(String mode) {
        super(mode);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
