package nodes;

public class ParDeclOp extends SyntaxNode{

    public ParDeclOp(InOutOp inOutOp, TypeOp type, IdOp idOp){
        super("ParDeclOp");
        add(inOutOp);
        add(type);
        add(idOp);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
