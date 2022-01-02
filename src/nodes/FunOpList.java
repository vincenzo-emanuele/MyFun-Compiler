package nodes;

public class FunOpList extends SyntaxNode {

    public FunOpList(){
        super("FunOpList");
    }

    public FunOpList(FunOp funOp, FunOpList funOpList){
        super("FunOpList");
        add(funOp);
        add(funOpList);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }


}
