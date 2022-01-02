package nodes;

public class StatOp extends SyntaxNode{

    public StatOp(IfStatOp ifStatOp){
        super("StatOp");
        add(ifStatOp);
    }

    public StatOp(WhileStatOp whileStatOp){
        super("StatOp");
        add(whileStatOp);
    }

    public StatOp(ReadStatOp readStatOp){
        super("StatOp");
        add(readStatOp);
    }

    public StatOp(WriteStatOp writeStatOp){
        super("StatOp");
        add(writeStatOp);
    }

    public StatOp(AssignStatOp assignStatOp){
        super("StatOp");
        add(assignStatOp);
    }

    public StatOp(CallFunOp callFunOp){
        super("StatOp");
        add(callFunOp);
    }

    public StatOp(ExprNode exprNode){
        super("StatOp");
        add(new ReturnOp());
        add(exprNode);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
