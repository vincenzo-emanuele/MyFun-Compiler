package scope;

public class SymbolType {
    public SymbolType(String typeDef, String type) {
        this.typeDef = typeDef;
        this.type = type;
    }

    public String getTypeDef() {
        return typeDef;
    }

    public void setTypeDef(String typeDef) {
        this.typeDef = typeDef;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SymbolType{" +
                "typeDef='" + typeDef + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    private String typeDef;
    private String type;
}
