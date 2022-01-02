package nodes;

public class IdListInitObblOp extends SyntaxNode{

    public IdListInitObblOp(IdOp idOp, ConstOp constOp){
        super("IdListInitObblOp");
        add(idOp);
        add(constOp);
    }

    public IdListInitObblOp(IdListInitObblOp list, IdOp idOp, ConstOp constOp){
        super("IdListInitObblOp");
        add(list);
        add(idOp);
        add(constOp);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }


}
