package scope;

import java.util.Objects;

public class SymbolType {

    public SymbolType(String lexeme, String typeDef) {
        this.lexeme = lexeme;
        this.typeDef = typeDef;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return "SymbolType{" +
                "lexeme='" + lexeme + '\'' +
                ", typeDef='" + typeDef + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolType temp = (SymbolType) o;
        return temp.getLexeme().equals(lexeme) && temp.getTypeDef().equals(typeDef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexeme, typeDef);
    }

    public String getTypeDef() {
        return typeDef;
    }

    public void setTypeDef(String typeDef) {
        this.typeDef = typeDef;
    }

    private String lexeme;
    private String typeDef;
}
