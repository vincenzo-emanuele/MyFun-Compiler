package nodes;

public class MainOp extends SyntaxNode{
    public MainOp(VarDeclOpList list, StatOpList statOpList){
        super("MainOp");
        add(list);
        add(statOpList);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
