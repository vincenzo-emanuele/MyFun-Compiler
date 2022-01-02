package nodes;

public class ParamDeclListOp extends SyntaxNode{
    public ParamDeclListOp(){
        super("ParamDeclListOp");
    }
    public ParamDeclListOp(NonEmptyParamDeclListOp list){
        super("ParamDeclListOp");
        add(list);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
