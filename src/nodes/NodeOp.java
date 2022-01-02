package nodes;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.List;

public class NodeOp extends DefaultMutableTreeNode implements Visitable {

    public NodeOp(DefaultMutableTreeNode node1, DefaultMutableTreeNode node2, String tag) {
        super(tag);
        this.add(node1);
        this.add(node2);
    }

    public NodeOp(int tag) {
        super(tag);
    }

    public List<TreeNode> getChildren(){
        return this.children;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        /*
        Object obj = getUserObject();
        if(obj instanceof Integer){
            return "num: " + obj;
        } else if(obj instanceof String) {
            return "op: " + obj;
        }
         */
        return "" + getUserObject();
    }

}
