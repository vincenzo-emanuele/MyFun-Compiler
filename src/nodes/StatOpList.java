package nodes;

public class StatOpList extends SyntaxNode{
    public StatOpList(){
        super("StatOpList");
    }
    public StatOpList(StatOp statOp, StatOpList statOpList){
        super("StatOpList");
        add(statOp);
        add(statOpList);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
