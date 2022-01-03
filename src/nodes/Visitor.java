package nodes;

public interface Visitor {
    public Object visit(AssignStatOp statOp);
    public Object visit(CallFunOp callFunOp);
    public Object visit(CallParam callParam);
    public Object visit(ConstOp constOp);
    public Object visit(ElseOp elseOp);
    public Object visit(ExprListOp exprListOp);
    public Object visit(ExprNode exprNode);
    public Object visit(FunctionNameOp functionNameOp);
    public Object visit(FunOp funOp);
    public Object visit(FunOpList funOpList);
    public Object visit(IdListInitObblOp idListInitObblOp);
    public Object visit(IdListInitOp idListInitOp);
    public Object visit(IdListOp idListOp);
    public Object visit(IdOp idOp);
    public Object visit(IfStatOp ifStatOp);
    public Object visit(InOutOp inOutOp);
    public Object visit(MainOp mainOp);
    public Object visit(NonEmptyParamDeclListOp nonEmptyParamDeclListOp);
    public Object visit(ParamDeclListOp paramDeclListOp);
    public Object visit(ParDeclOp parDeclOp);
    public Object visit(ProgramOp programOp);
    public Object visit(ReadStatOp readStatOp);
    public Object visit(ReturnOp returnOp);
    public Object visit(StatOp statOp);
    public Object visit(StatOpList statOpList);
    public Object visit(SyntaxNode syntaxNode);
    public Object visit(TypeOp typeOp);
    public Object visit(VarDeclOp varDeclOp);
    public Object visit(VarDeclOpList varDeclOpList);
    public Object visit(WhileStatOp whileStatOp);
    public Object visit(WriteStatOp writeStatOp);
}
