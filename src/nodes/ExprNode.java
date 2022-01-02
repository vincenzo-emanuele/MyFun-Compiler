package nodes;

public class ExprNode extends SyntaxNode{

    public ExprNode(ExprNode e1, ExprNode e2, String tag){
        super(tag);
        add(e1);
        add(e2);
    }

    public ExprNode(ExprNode e1, String tag){
        super(tag);
        add(e1);
    }

    public ExprNode(Object tag){
        super(tag);
    }

    public ExprNode(CallFunOp c){
        super("ExprNode");
        add(c);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
