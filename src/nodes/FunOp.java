package nodes;

public class FunOp extends SyntaxNode {

    public FunOp(IdOp id, ParamDeclListOp parDeclOp, TypeOp typeOp, VarDeclOpList varDeclOpList, StatOpList statOpList){
        super("FunOp");
        add(id);
        add(parDeclOp);
        add(typeOp);
        add(varDeclOpList);
        add(statOpList);
    }

    public FunOp(IdOp id, ParamDeclListOp parDeclOp, VarDeclOpList varDeclOpList, StatOpList statOpList){
        super("FunOp");
        add(id);
        add(parDeclOp);
        add(varDeclOpList);
        add(statOpList);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }


}
