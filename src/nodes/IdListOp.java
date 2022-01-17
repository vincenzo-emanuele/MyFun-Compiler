package nodes;

public class IdListOp extends SyntaxNode{
    public IdListOp(IdOp idOp){
        super("IdListOp");
        add(idOp);
    }

    public IdListOp(IdOp idOp, IdListOp list){
        super("IdListOp");
        add(idOp);
        add(list);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
