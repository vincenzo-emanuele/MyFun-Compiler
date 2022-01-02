package nodes;

public class ExprListOp extends SyntaxNode{
    public ExprListOp(InOutOp mode, ExprNode e){
        super("ExprListOp");
        add(mode);
        add(e);
    }

    public ExprListOp(InOutOp mode, IdOp e){
        super("ExprListOp");
        add(mode);
        add(e);
    }

    public ExprListOp(InOutOp mode, ExprNode e, ExprListOp list){
        super("ExprListOp");
        add(mode);
        add(e);
        add(list);
    }

    public ExprListOp(InOutOp mode, IdOp e, ExprListOp list){
        super("ExprListOp");
        add(mode);
        add(e);
        add(list);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }


}
