package nodes;

public class FunctionNameOp extends SyntaxNode{

    public FunctionNameOp(String name) {
        super(name);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
