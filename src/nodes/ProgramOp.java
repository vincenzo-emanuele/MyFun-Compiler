package nodes;

public class ProgramOp extends SyntaxNode{

    public ProgramOp(VarDeclOpList varDeclOpList, FunOpList funOpList, MainOp mainOp){
        super("ProgramOp");
        add(varDeclOpList);
        add(funOpList);
        add(mainOp);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }


}
