package nodes;

public class CallFunOp extends SyntaxNode{

    public CallFunOp(ExprListOp paramList, FunctionNameOp name) {
        super("CallFunOp");
        add(name);
        add(paramList);
    }

    public CallFunOp(FunctionNameOp name) {
        super("CallFunOp");
        add(name);
    }
    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
