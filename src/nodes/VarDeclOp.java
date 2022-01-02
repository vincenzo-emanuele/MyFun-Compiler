package nodes;

public class VarDeclOp extends SyntaxNode {
    public VarDeclOp(TypeOp t, IdListInitOp idListInitOp){
        super("VarDeclOp");
        add(t);
        add(idListInitOp);
    }

    public VarDeclOp(IdListInitObblOp idListInitObblOp){
        super("VarDeclOp");
        add(idListInitObblOp);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
