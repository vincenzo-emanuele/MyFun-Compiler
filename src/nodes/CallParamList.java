package nodes;

import java.util.List;

public class CallParamList extends SyntaxNode{

    public CallParamList(List<CallParam> paramList){
        super("CallParamList");
        for(CallParam param : paramList){
            add(param);
        }
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
