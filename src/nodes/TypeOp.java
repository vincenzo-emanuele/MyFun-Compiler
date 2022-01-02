package nodes;

public class TypeOp extends SyntaxNode{
    public TypeOp(String type){
        super(type);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
