package scope;

public class Record {

    public Record(String lexeme){
        this.lexeme = lexeme;
    }

    public Record(String lexeme, String type) {
        this.lexeme = lexeme;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String lexeme;
    private String type;

}
