package nodes;

public interface Visitable {
    public Object accept(Visitor visitor);
}
