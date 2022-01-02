package nodes;

public class ReturnOp extends SyntaxNode{
    public ReturnOp(){
        super("RETURN");
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
