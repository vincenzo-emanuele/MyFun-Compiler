package nodes;

public class IdOp extends SyntaxNode{
    public IdOp(String label){
        super(label);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
