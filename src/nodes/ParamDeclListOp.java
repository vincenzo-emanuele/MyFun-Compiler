package nodes;

public class ParamDeclListOp extends SyntaxNode{
    public ParamDeclListOp(){
        super("ParamDeclListOp");
    }
    public ParamDeclListOp(NonEmptyParamDeclListOp list){
        super("ParamDeclListOp");
        add(list);
    }
}
