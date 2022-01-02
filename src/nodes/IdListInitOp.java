package nodes;

public class IdListInitOp extends SyntaxNode{

    public IdListInitOp(IdOp idOp){
        super("IdListInitOp");
        add(idOp);
    }

    public IdListInitOp(IdListInitOp list, IdOp idOp){
        super("IdListInitOp");
        add(list);
        add(idOp);
    }

    public IdListInitOp(IdOp idOp, ExprNode e){
        super("IdListInitOp");
        add(idOp);
        add(e);
    }

    public IdListInitOp(IdListInitOp list, IdOp idOp, ExprNode e){
        super("IdListInitOp");
        add(list);
        add(idOp);
        add(e);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
