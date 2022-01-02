package nodes;

public class NonEmptyParamDeclListOp extends SyntaxNode{
    public NonEmptyParamDeclListOp(ParDeclOp parDeclOp){
        super("NonEmptyParamDeclListOp");
        add(parDeclOp);
    }

    public NonEmptyParamDeclListOp(NonEmptyParamDeclListOp list, ParDeclOp p){
        super("NonEmptyParamDeclListOp");
        add(list);
        add(p);
    }

}
