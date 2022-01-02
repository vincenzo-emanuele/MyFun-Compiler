package nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class NodeValue extends DefaultMutableTreeNode implements Visitable {

    public NodeValue(int label){
        super(label);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
