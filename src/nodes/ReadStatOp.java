package nodes;

public class ReadStatOp extends SyntaxNode{
    public ReadStatOp(IdListOp idListOp, ExprNode expr){
        super("ReadStatOp");
        add(idListOp);
        add(expr);
    }

    public ReadStatOp(IdListOp idListOp){
        super("ReadStatOp");
        add(idListOp);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
