// Linux: ../jflex-1.8.2/bin/jflex -d src srcjflexcup/myfun.flex
// Windows: C:\JFLEX\bin\jflex -d src srcjflexcup/myfun.flex
import java_cup.runtime.Symbol; //This is how we pass tokens to the parser
import java.util.HashMap;
%%
%unicode
%cup
%line
%column

%{
    StringBuffer string = new StringBuffer();
    HashMap<String, Symbol> symbolTable = new HashMap<>();

    	public Symbol installID(String lessema){
    		if(symbolTable.containsKey(lessema)){
    			return symbolTable.get(lessema);
    		} else {
    			Symbol token = new Symbol(sym.ID, lessema);
    			symbolTable.put(lessema, token);
    			return token;
    		}
    	}

    	public HashMap<String, Symbol> getSymbolTable(){
    		return symbolTable;
    	}
%}

whitespace = [ \r\n\t\f]
digit = [0-9]
int_const = {digit}+
real_const = {digit}+\.{digit}+
letter = [A-Za-z]
id = [$_{letter}][$_{letter}{digit}]*
//single_line_comment = ("//" | "#") [^\r\n]* (\n | \r | \r\n)?
%state STRING_SINGLE
%state STRING_DOUBLE
%state BLOCK_COMMENT
%state SINGLE_COMMENT
%%
<YYINITIAL> {
    "main" { return new Symbol(sym.MAIN); }
    "integer" { return new Symbol(sym.INTEGER); }
    "string" { return new Symbol(sym.STRING); }
    "real" { return new Symbol(sym.REAL); }
    "bool" {return new Symbol(sym.BOOL);}
    "(" {return new Symbol(sym.LPAR);}
    ")" {return new Symbol(sym.RPAR);}
    ":" {return new Symbol(sym.COLON);}
    "fun" {return new Symbol(sym.FUN);}
    "end" {return new Symbol(sym.END);}
    "if" {return new Symbol(sym.IF);}
    "then" {return new Symbol(sym.THEN);}
    "else" {return new Symbol(sym.ELSE);}
    "while" {return new Symbol(sym.WHILE);}
    "loop" {return new Symbol(sym.LOOP);}
    "%" {return new Symbol(sym.READ);}
    "?" {return new Symbol(sym.WRITE);}
    "?." {return new Symbol(sym.WRITELN);}
    "?," {return new Symbol(sym.WRITEB);}
    "?:" {return new Symbol(sym.WRITET);}
    ":=" {return new Symbol(sym.ASSIGN);}
    "+" {return new Symbol(sym.PLUS);}
    "-" {return new Symbol(sym.MINUS);}
    "*" {return new Symbol(sym.TIMES);}
    "div" {return new Symbol(sym.DIVINT);}
    "/" {return new Symbol(sym.DIV);}
    "^" {return new Symbol(sym.POW);}
    "&" {return new Symbol(sym.STR_CONCAT);}
    "=" {return new Symbol(sym.EQ);}
    "<>" {return new Symbol(sym.NE);}
    "!=" {return new Symbol(sym.NE);}
    "<" {return new Symbol(sym.LT);}
    "<=" {return new Symbol(sym.LE);}
    ">" {return new Symbol(sym.GT);}
    ">=" {return new Symbol(sym.GE);}
    "and" {return new Symbol(sym.AND);}
    "or" {return new Symbol(sym.OR);}
    "not" {return new Symbol(sym.NOT);}
    //"null" {return new Symbol(sym.NULL);}
    "true" {return new Symbol(sym.TRUE);}
    "false" {return new Symbol(sym.FALSE);}
    "var" {return new Symbol(sym.VAR);}
    "out" {return new Symbol(sym.OUT);}
    "@" {return new Symbol(sym.OUTPAR);}
    {int_const} {return new Symbol(sym.INTEGER_CONST, Integer.parseInt(yytext()));}
    {real_const} {return new Symbol(sym.REAL_CONST, Double.parseDouble(yytext()));}
    \' {string.setLength(0); yybegin(STRING_SINGLE);}
    \" {string.setLength(0); yybegin(STRING_DOUBLE);}
    ";" {return new Symbol(sym.SEMI);}
    "," {return new Symbol(sym.COMMA);}
    "return" {return new Symbol(sym.RETURN);}
    #\* {yybegin(BLOCK_COMMENT);}
    # | "//" {yybegin(SINGLE_COMMENT);}
    {id} {return new Symbol(sym.ID, yytext());}
    {whitespace} { /* ignore */ }
}

<STRING_SINGLE> {
    \' {yybegin(YYINITIAL); return new Symbol(sym.STRING_CONST, string.toString());}
    [^\'] {string.append(yytext());}
    <<EOF>> {throw new Error("\n\nStringa non terminata < "+ yytext()+" >\n");}
}

<STRING_DOUBLE> {
    \" {yybegin(YYINITIAL); return new Symbol(sym.STRING_CONST, string.toString());}
    [^\"] {string.append(yytext());}
    <<EOF>> {throw new Error("\n\nStringa non terminata < "+ yytext()+" >\n");}
}

<SINGLE_COMMENT>{
    (\n | \r | \r\n) {yybegin(YYINITIAL);}
    <<EOF>> {yybegin(YYINITIAL);}
    [^\n] {}
}

<BLOCK_COMMENT> {
    # {yybegin(YYINITIAL);}
    [^#] {}
    <<EOF>> {throw new Error("\n\nCommento non terminato < "+ yytext()+" >\n");}
}

[^]           { throw new Error("\n\nIllegal character < "+ yytext()+" >\n"); }
<<EOF>> {return new Symbol(sym.EOF);}
