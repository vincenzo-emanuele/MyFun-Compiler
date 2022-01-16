package nodes;

/**Questa classe è inutile
 * @deprecated
 */
public class CallParam extends SyntaxNode{

    public CallParam(ExprNode expr, InOutOp mode) {
        super("CallParam");
        add(expr);
        add(mode);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
