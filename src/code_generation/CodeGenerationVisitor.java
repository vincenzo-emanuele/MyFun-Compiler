package code_generation;

import nodes.*;
import scope.Type;

public class CodeGenerationVisitor implements Visitor {

    @Override
    public Object visit(AssignStatOp statOp) {
        String code;
        IdOp lval = (IdOp) statOp.getChildAt(0);
        ExprNode rval = (ExprNode) statOp.getChildAt(1);
        String lvalCode = (String) lval.accept(this);
        String rvalCode = (String) rval.accept(this);
        if(lval.getType().contains("out")){
            lvalCode = "*" + lvalCode;
        }
        if(rval.getType().contains("out")){
            rvalCode = "*" + rvalCode;
        }
        code = lvalCode + " = " + rvalCode;
        return code;
    }

    @Override
    public Object visit(CallFunOp callFunOp) {
        String code = "";
        FunctionNameOp functionNameOp = (FunctionNameOp) callFunOp.getChildAt(0);
        String functionNameOpCode = (String) functionNameOp.accept(this);
        code += functionNameOpCode + "(";
        if(callFunOp.getChildCount() == 2){
            ExprListOp exprListOp = (ExprListOp) callFunOp.getChildAt(1);
            String exprListOpCode = (String) exprListOp.accept(this);
            code += exprListOpCode;
        }
        code += ")";
        return code;
    }

    @Override
    //inutile
    public Object visit(CallParam callParam) {
        return null;
    }

    @Override
    public Object visit(ConstOp constOp) {
        String code;
        if(constOp.getUserObject() instanceof Boolean){
            boolean bool = (Boolean) constOp.getUserObject();
            if(bool){
                code = "1";
            } else {
                code = "0";
            }
        } else if(constOp.getUserObject() instanceof String){
            code = "\"" + constOp.getUserObject().toString() + "\"";
        } else{
            code = constOp.getUserObject().toString();
        }
        return code;
    }

    @Override
    public Object visit(ElseOp elseOp) {
        String code;
        if(elseOp.getChildCount() == 0){
            code = "";
        } else {
            VarDeclOpList varDeclOpList = (VarDeclOpList) elseOp.getChildAt(0);
            StatOpList statOpList = (StatOpList) elseOp.getChildAt(1);
            String varDeclOpListCode = (String) varDeclOpList.accept(this);
            String statOpListCode = (String) statOpList.accept(this);
            code = " else {\n" + varDeclOpListCode + statOpListCode + "\n}";
        }
        return code;
    }

    @Override
    public Object visit(ExprListOp exprListOp) {
        String code = "";
        InOutOp inOutOp = (InOutOp) exprListOp.getChildAt(0);
        String inOutOpCode = (String) inOutOp.accept(this);
        if(exprListOp.getChildCount() == 2){
            if(exprListOp.getChildAt(1) instanceof ExprNode){   //Se è un ExprNode può solo essere un parametro di input
                ExprNode exprNode = (ExprNode) exprListOp.getChildAt(1);
                String exprNodeCode = (String) exprNode.accept(this);
                code += exprNodeCode;
            } else if(exprListOp.getChildAt(1) instanceof IdOp){
                IdOp idOp = (IdOp) exprListOp.getChildAt(1);
                String idOpCode = (String) idOp.accept(this);
                if(inOutOpCode.equals("out")){
                    code += "&";
                }
                code += idOpCode;
            }
        } else {
            if(exprListOp.getChildAt(1) instanceof ExprNode){
                ExprNode exprNode = (ExprNode) exprListOp.getChildAt(1);
                ExprListOp exprListOp1 = (ExprListOp) exprListOp.getChildAt(2);
                String exprNodeCode = (String) exprNode.accept(this);
                String exprListOp1Code = (String) exprListOp1.accept(this);
                code += exprNodeCode + ", " + exprListOp1Code;
            } else {
                IdOp idOp = (IdOp) exprListOp.getChildAt(1);
                ExprListOp exprListOp1 = (ExprListOp) exprListOp.getChildAt(2);
                String idOpCode = (String) idOp.accept(this);
                String exprListOp1Code = (String) exprListOp1.accept(this);
                if(inOutOpCode.equals("out")){
                    code += "&";
                }
                code += idOpCode + ", " + exprListOp1Code;
            }
        }
        return code;
    }

    @Override
    //TODO: gestire passaggio IdOp come out nelle CallFunOp
    public Object visit(ExprNode exprNode) {
        String code;
        if(exprNode.getChildCount() == 2){
            ExprNode exprNode1 = (ExprNode) exprNode.getChildAt(0);
            ExprNode exprNode2 = (ExprNode) exprNode.getChildAt(1);
            String exprNode1Code = (String) exprNode1.accept(this);
            String exprNode2Code = (String) exprNode2.accept(this);
            String op = (String) exprNode.getUserObject();
            switch (op){
                case "AND":
                    code = exprNode1Code + " && " + exprNode2Code;
                    break;
                case "OR":
                    code = exprNode1Code + " || " + exprNode2Code;
                    break;
                case "DIVINT":
                    code = exprNode1Code + "/" + exprNode2Code;
                    break;
                case "&":
                    if(exprNode1.getType().equals("INTEGER")){
                        exprNode1Code = "fromIntegerToString(" + exprNode1Code + ")";
                    } else if(exprNode1.getType().equals("REAL")){
                        exprNode1Code = "fromRealToString(" + exprNode1Code + ")";
                    } else if(exprNode1.getType().equals("BOOLEAN")){
                        exprNode1Code = "fromBooleanToString(" + exprNode1Code + ")";
                    }
                    if(exprNode2.getType().equals("INTEGER")){
                        exprNode2Code = "fromIntegerToString(" + exprNode2Code + ")";
                    } else if(exprNode2.getType().equals("REAL")){
                        exprNode2Code = "fromRealToString(" + exprNode2Code + ")";
                    } else if(exprNode2.getType().equals("BOOLEAN")){
                        exprNode2Code = "fromBooleanToString(" + exprNode2Code + ")";
                    }
                    code = "customConcat(" + exprNode1Code + ", " + exprNode2Code + ")";
                    break;
                case "=":
                    if(exprNode1.getType().equals("STRING") && exprNode2.getType().equals("STRING")){
                        code = "!strcmp(" + exprNode1Code + ", " + exprNode2Code + ")";
                    } else {
                        code = exprNode1Code + " == " + exprNode2Code;
                    }
                    break;
                case "^":
                    code = "pow(" + exprNode1Code + ", " + exprNode2Code + ")";
                    break;
                default:
                    code = exprNode1Code + " " + op + " " + exprNode2Code;
                    break;
            }
        } else if(exprNode.getChildCount() == 1){
            if(exprNode.getChildAt(0) instanceof CallFunOp){
                CallFunOp callFunOp = (CallFunOp) exprNode.getChildAt(0);
                String callFunOpCode = (String) callFunOp.accept(this);
                code = callFunOpCode;
            } else if(exprNode.getChildAt(0) instanceof ExprNode){
                ExprNode exprNode1 = (ExprNode) exprNode.getChildAt(0);
                String exprNode1Code = (String) exprNode1.accept(this);
                String op = (String) exprNode.getUserObject();
                code = op + exprNode1Code;
            } else {
                IdOp idOp = (IdOp) exprNode.getChildAt(0);
                String idOpCode = (String) idOp.accept(this);
                if(idOp.getType().contains("out") && !(exprNode.getParent() instanceof ExprListOp)){
                    idOpCode = "*" + idOpCode;
                }
                code = idOpCode;
            }
        } else {
            if(exprNode.getUserObject() instanceof Boolean){
                boolean bool = (Boolean) exprNode.getUserObject();
                if(bool){
                    code = "1";
                } else {
                    code = "0";
                }
            } else if(exprNode.getUserObject() instanceof String){
                code = "\"" + exprNode.getUserObject().toString() + "\"";
            } else{
                code = exprNode.getUserObject().toString();
            }
        }
        return code;
    }

    @Override
    public Object visit(FunctionNameOp functionNameOp) {
        return functionNameOp.getUserObject();
    }

    @Override
    public Object visit(FunOp funOp) {
        String code;
        IdOp idOp = (IdOp) funOp.getChildAt(0);
        ParamDeclListOp paramDeclListOp = (ParamDeclListOp) funOp.getChildAt(1);
        String idOpCode = (String) idOp.accept(this);
        String paramDeclListOpCode = (String) paramDeclListOp.accept(this);
        VarDeclOpList varDeclOpList;
        StatOpList statOpList;
        String varDeclOpListCode;
        String statOpListCode;
        String typeCode;
        if(funOp.getChildCount() == 4){
            typeCode = "void";
            varDeclOpList = (VarDeclOpList) funOp.getChildAt(2);
            statOpList = (StatOpList) funOp.getChildAt(3);
        } else {
            TypeOp typeOp = (TypeOp) funOp.getChildAt(2);
            typeCode = (String) typeOp.accept(this);
            varDeclOpList = (VarDeclOpList) funOp.getChildAt(3);
            statOpList = (StatOpList) funOp.getChildAt(4);
        }
        varDeclOpListCode = (String) varDeclOpList.accept(this);
        statOpListCode = (String) statOpList.accept(this);
        code = typeCode + " " + idOpCode + "(" + paramDeclListOpCode + ")" + "{\n" + varDeclOpListCode + statOpListCode + "\n}";
        return code;
    }

    @Override
    public Object visit(FunOpList funOpList) {
        String code;
        if(funOpList.getChildCount() == 0){
            code = "";
        } else {
            FunOp funOp = (FunOp) funOpList.getChildAt(0);
            FunOpList funOpList1 = (FunOpList) funOpList.getChildAt(1);
            String funOpCode = (String) funOp.accept(this);
            String funOpList1Code = (String) funOpList1.accept(this);
            code = funOpCode + "\n" + funOpList1Code;
        }
        return code;
    }

    @Override
    public Object visit(IdListInitObblOp idListInitObblOp) {
        String code;
        if(idListInitObblOp.getChildCount() == 2){
            IdOp idOp = (IdOp) idListInitObblOp.getChildAt(0);
            ConstOp constOp = (ConstOp) idListInitObblOp.getChildAt(1);
            String idOpCode = (String) idOp.accept(this);
            String constOpCode = (String) constOp.accept(this);
            String idOpType = idOp.getType();
            String cType = "";
            switch (idOpType){
                case "INTEGER":
                case "BOOLEAN":
                    cType = "int";
                    break;
                case "REAL":
                    cType = "double";
                    break;
                case "STRING":
                    cType = "char *";
                    break;
            }
            code = cType + " " + idOpCode + " = " + constOpCode + ";\n";
        } else {
            IdListInitObblOp idListInitObblOp1 = (IdListInitObblOp) idListInitObblOp.getChildAt(0);
            IdOp idOp = (IdOp) idListInitObblOp.getChildAt(1);
            ConstOp constOp = (ConstOp) idListInitObblOp.getChildAt(2);
            String idListInitObblOp1Code = (String) idListInitObblOp1.accept(this);
            String idOpType = idOp.getType();
            String idOpCode = (String) idOp.accept(this);
            String constOpCode = (String) constOp.accept(this);
            String cType = "";
            switch (idOpType){
                case "INTEGER":
                case "BOOLEAN":
                    cType = "int";
                    break;
                case "REAL":
                    cType = "double";
                    break;
                case "STRING":
                    cType = "char *";
                    break;
            }
            code = idListInitObblOp1Code + cType + " " + idOpCode + " = " + constOpCode + ";\n";
        }
        return code;
    }

    @Override
    public Object visit(IdListInitOp idListInitOp) {
        String code = "";
        if(idListInitOp.getChildCount() == 1){
          IdOp idOp = (IdOp) idListInitOp.getChildAt(0);
          String idOpcode = (String) idOp.accept(this);
          code = idOpcode;
        } else if(idListInitOp.getChildCount() == 2){
            if(idListInitOp.getChildAt(0) instanceof IdListInitOp){
                IdListInitOp idListInitOp1 = (IdListInitOp) idListInitOp.getChildAt(0);
                IdOp idOp = (IdOp) idListInitOp.getChildAt(1);
                String idListInitOp1Code = (String) idListInitOp1.accept(this);
                String idOpCode = (String) idOp.accept(this);
                if(idOp.getType().equals("STRING")){
                    idOpCode = "*" + idOpCode;
                }
                code = idListInitOp1Code + ", " + idOpCode;
            } else {
                IdOp idOp = (IdOp) idListInitOp.getChildAt(0);
                ExprNode exprNode = (ExprNode) idListInitOp.getChildAt(1);
                String idOpCode = (String) idOp.accept(this);
                String exprNodeCode = (String) exprNode.accept(this);
                code = idOpCode + " = " + exprNodeCode;
            }
        } else {
            IdListInitOp idListInitOp1 = (IdListInitOp) idListInitOp.getChildAt(0);
            IdOp idOp = (IdOp) idListInitOp.getChildAt(1);
            ExprNode exprNode = (ExprNode) idListInitOp.getChildAt(2);
            String idListInitOp1Code = (String) idListInitOp1.accept(this);
            String idOpCode = (String) idOp.accept(this);
            String exprNodeCode = (String) exprNode.accept(this);
            if(idOp.getType().equals("STRING")){
                idOpCode = "*" + idOpCode;
            }
            code = idListInitOp1Code + ", " + idOpCode + " = " + exprNodeCode;
        }
        return code;
    }

    @Override
    public Object visit(IdListOp idListOp) {
        String code;
        IdOp idOp = (IdOp) idListOp.getChildAt(0);
        String idOpCode = (String) idOp.accept(this);
        String scanCode = "";
        if(idOp.getType().equals("STRING")){
            scanCode = idOpCode + " = getln();\n";
        } else if(idOp.getType().equals("INTEGER") || idOp.getType().equals("BOOLEAN")){
            scanCode = "scanf(\"%d\", " + "&" + idOpCode + ");\ngetchar();\n";
        } else {
            scanCode = "scanf(\"%lf\", " + "&" + idOpCode + ");\ngetchar();\n";
        }
        code = scanCode;
        if(idListOp.getChildCount() == 2){
            IdListOp idListOp1 = (IdListOp) idListOp.getChildAt(1);
            String idListOp1Code = (String) idListOp1.accept(this);
            code += idListOp1Code;
        }
        return code;
    }

    @Override
    public Object visit(IdOp idOp) {
        return idOp.getUserObject();
    }

    @Override
    public Object visit(IfStatOp ifStatOp) {
        String code;
        ExprNode exprNode = (ExprNode) ifStatOp.getChildAt(0);
        VarDeclOpList varDeclOpList = (VarDeclOpList) ifStatOp.getChildAt(1);
        StatOpList statOpList = (StatOpList) ifStatOp.getChildAt(2);
        ElseOp elseOp = (ElseOp) ifStatOp.getChildAt(3);
        String exprNodeCode = (String) exprNode.accept(this);
        String varDeclOpListCode = (String) varDeclOpList.accept(this);
        String statOpListCode = (String) statOpList.accept(this);
        String elseOpCode = (String) elseOp.accept(this);
        code = "if(" + exprNodeCode + "){\n" + varDeclOpListCode + statOpListCode + "\n}";
        if(!elseOpCode.equals("")){
            code += elseOpCode;
        }
        return code;
    }

    @Override
    public Object visit(InOutOp inOutOp) {
        return inOutOp.getUserObject();
    }

    @Override
    public Object visit(MainOp mainOp) {
        String code;
        VarDeclOpList varDeclOpList = (VarDeclOpList) mainOp.getChildAt(0);
        StatOpList statOpList = (StatOpList) mainOp.getChildAt(1);
        String varDeclOpListCode = (String) varDeclOpList.accept(this);
        String statOpListCode = (String) statOpList.accept(this);
        code = "int main(void){\n" + varDeclOpListCode + statOpListCode + "return 0;\n}\n";
        return code;
    }

    @Override
    public Object visit(NonEmptyParamDeclListOp nonEmptyParamDeclListOp) {
        String code;
        if(nonEmptyParamDeclListOp.getChildCount() == 1){
            ParDeclOp parDeclOp = (ParDeclOp) nonEmptyParamDeclListOp.getChildAt(0);
            code = (String) parDeclOp.accept(this);
        } else {
            NonEmptyParamDeclListOp nonEmptyParamDeclListOp1 = (NonEmptyParamDeclListOp) nonEmptyParamDeclListOp.getChildAt(0);
            ParDeclOp parDeclOp = (ParDeclOp) nonEmptyParamDeclListOp.getChildAt(1);
            String nonEmptyParamDeclListOp1Code = (String) nonEmptyParamDeclListOp1.accept(this);
            String parDeclOpCode = (String) parDeclOp.accept(this);
            code = nonEmptyParamDeclListOp1Code + ", " + parDeclOpCode;
        }
        return code;
    }

    @Override
    public Object visit(ParamDeclListOp paramDeclListOp) {
        String code;
        if(paramDeclListOp.getChildCount() == 0){
            code = "void";
        } else {
            NonEmptyParamDeclListOp nonEmptyParamDeclListOp = (NonEmptyParamDeclListOp) paramDeclListOp.getChildAt(0);
            code = (String) nonEmptyParamDeclListOp.accept(this);
        }
        return code;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        String code;
        InOutOp inOutOp = (InOutOp) parDeclOp.getChildAt(0);
        TypeOp typeOp = (TypeOp) parDeclOp.getChildAt(1);
        IdOp idOp = (IdOp) parDeclOp.getChildAt(2);
        String inOutOpCode = (String) inOutOp.accept(this);
        String typeOpCode = (String) typeOp.accept(this);
        String idOpCode = (String) idOp.accept(this);
        code = typeOpCode + " ";
        if(inOutOpCode.equals("out")){
            code += "* ";
        }
        code += idOpCode;
        return code;
    }

    @Override
    public Object visit(ProgramOp programOp) {
        VarDeclOpList varDeclOpList = (VarDeclOpList) programOp.getChildAt(0);
        FunOpList funOpList = (FunOpList) programOp.getChildAt(1);
        MainOp mainOp = (MainOp) programOp.getChildAt(2);
        String include = "#include <stdio.h>\n#include <stdlib.h>\n#include <math.h>\n#include <string.h>\n";
        String getLnFunction = "char *getln(){ char *line = NULL, *tmp = NULL; int size = 0, index = 0; int ch = EOF; while (ch) { ch = getc(stdin); /* Check if we need to stop. */ if (ch == EOF || ch == '\\n') ch = 0; /* Check if we need to expand. */ if (size <= index) { size += 1; tmp = realloc(line, size); if (!tmp) { free(line); line = NULL; break; } line = tmp; } /* Actually store the thing. */ line[index++] = ch; } return line; }\n";
        String customConcatFunction = "char *customConcat(char *str1, char *str2){ char *newStr1 = malloc(strlen(str1) + 1); char *newStr2 = malloc(strlen(str2) + 1); strcpy(newStr1, str1); strcpy(newStr2, str2); char *outStr = strcat(newStr1, newStr2); return outStr; }\n";
        String fromRealToStringFunction = "char *fromRealToString(double input){ char *buffer = malloc(64*sizeof(char)); snprintf(buffer, 64, \"%lf\", input); return buffer; }\n";
        String fromIntegerToStringFunction = "char *fromIntegerToString(int input){ char *buffer = malloc(64*sizeof(char)); snprintf(buffer, 64, \"%d\", input); return buffer; }\n";
        String fromBooleanToStringFunction = "char *fromBooleanToString(int input){ if(input == 0){ return \"false\"; } else { return \"true\"; } }\n";
        String varDeclOpListCode = (String) varDeclOpList.accept(this);
        String funOpListCode = (String) funOpList.accept(this);
        String mainOpCode = (String) mainOp.accept(this);
        return include + getLnFunction + customConcatFunction + varDeclOpListCode + fromRealToStringFunction + fromIntegerToStringFunction + fromBooleanToStringFunction + funOpListCode + mainOpCode;
    }

    @Override
    public Object visit(ReadStatOp readStatOp) {
        String code = "";
        if(readStatOp.getChildCount() == 2){
            ExprNode exprNode = (ExprNode) readStatOp.getChildAt(1);
            String exprNodeCode = (String) exprNode.accept(this);
            code = "printf(\"%s\\n\", " + exprNodeCode + ");\n";
        }
        IdListOp idListOp = (IdListOp) readStatOp.getChildAt(0);
        String idListOpCode = (String) idListOp.accept(this);
        code += idListOpCode;
        return code;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        return "return";
    }

    @Override
    public Object visit(StatOp statOp) {
        String code;
        if(statOp.getChildAt(0) instanceof IfStatOp){
            IfStatOp ifStatOp = (IfStatOp) statOp.getChildAt(0);
            code = (String) ifStatOp.accept(this);
        } else if(statOp.getChildAt(0) instanceof WhileStatOp){
            WhileStatOp whileStatOp = (WhileStatOp) statOp.getChildAt(0);
            code = (String) whileStatOp.accept(this);
        } else if(statOp.getChildAt(0) instanceof ReadStatOp){
            ReadStatOp readStatOp = (ReadStatOp) statOp.getChildAt(0);
            code = (String) readStatOp.accept(this);
        } else if (statOp.getChildAt(0) instanceof WriteStatOp) {
            WriteStatOp writeStatOp = (WriteStatOp) statOp.getChildAt(0);
            code = (String) writeStatOp.accept(this) + ";";
        } else if(statOp.getChildAt(0) instanceof AssignStatOp){
            AssignStatOp assignStatOp = (AssignStatOp) statOp.getChildAt(0);
            code = (String) assignStatOp.accept(this) + ";";
        } else if(statOp.getChildAt(0) instanceof CallFunOp) {
            CallFunOp callFunOp = (CallFunOp) statOp.getChildAt(0);
            code = (String) callFunOp.accept(this) + ";";
        } else {
            ReturnOp returnOp = (ReturnOp) statOp.getChildAt(0);
            ExprNode exprNode = (ExprNode) statOp.getChildAt(1);
            String returnOpCode = (String) returnOp.accept(this);
            String exprNodeCode = (String) exprNode.accept(this);
            code = returnOpCode + " " + exprNodeCode + ";";
        }
        return code;
    }

    @Override
    public Object visit(StatOpList statOpList) {
        String code;
        if(statOpList.getChildCount() == 0){
            code = "";
        } else {
            StatOp statOp = (StatOp) statOpList.getChildAt(0);
            StatOpList statOpList1 = (StatOpList) statOpList.getChildAt(1);
            String statOpCode = (String) statOp.accept(this);
            String statOpList1Code = (String) statOpList1.accept(this);
            code = statOpCode + "\n" + statOpList1Code;
        }
        return code;
    }

    @Override
    //inutile
    public Object visit(SyntaxNode syntaxNode) {
        return null;
    }

    @Override
    public Object visit(TypeOp typeOp) {
        String type = (String) typeOp.getUserObject();
        String cType = "";
        switch (type){
            case "INTEGER":
            case "BOOLEAN":
                cType = "int";
                break;
            case "REAL":
                cType = "double";
                break;
            case "STRING":
                cType = "char *";
                break;
        }
        return cType;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        String code;
        if(varDeclOp.getChildCount() == 1){
            IdListInitObblOp idListInitObblOp = (IdListInitObblOp) varDeclOp.getChildAt(0);
            String idListInitObblOpCode = (String) idListInitObblOp.accept(this);
            code = idListInitObblOpCode;
        } else {
            TypeOp typeOp = (TypeOp) varDeclOp.getChildAt(0);
            IdListInitOp idListInitOp = (IdListInitOp) varDeclOp.getChildAt(1);
            String typeCode = (String) typeOp.accept(this);
            String idListInitOpCode = (String) idListInitOp.accept(this);
            code = typeCode + " " + idListInitOpCode + ";\n";
        }
        return code;
    }

    @Override
    public Object visit(VarDeclOpList varDeclOpList) {
        String code = "";
        if(varDeclOpList.getChildCount() == 0){
            return code;
        }
        VarDeclOp varDeclOp = (VarDeclOp) varDeclOpList.getChildAt(0);
        VarDeclOpList varDeclOpList1 = (VarDeclOpList) varDeclOpList.getChildAt(1);
        String varDeclOpCode = (String) varDeclOp.accept(this);
        String varDeclOpListCode = (String) varDeclOpList1.accept(this);
        code = varDeclOpCode + varDeclOpListCode;
        return code;
    }

    @Override
    public Object visit(WhileStatOp whileStatOp) {
        String code;
        ExprNode exprNode = (ExprNode) whileStatOp.getChildAt(0);
        VarDeclOpList varDeclOpList = (VarDeclOpList) whileStatOp.getChildAt(1);
        StatOpList statOpList = (StatOpList) whileStatOp.getChildAt(2);
        String exprNodeCode = (String) exprNode.accept(this);
        String varDeclOpListCode = (String) varDeclOpList.accept(this);
        String statOpListCode = (String) statOpList.accept(this);
        code = "while(" + exprNodeCode + "){\n" + varDeclOpListCode + statOpListCode + "\n}";
        return code;
    }

    @Override
    public Object visit(WriteStatOp writeStatOp) {
        String code;
        ExprNode exprNode = (ExprNode) writeStatOp.getChildAt(0);
        String exprNodeCode = (String) exprNode.accept(this);
        if(exprNode.getType().equals("INTEGER") || exprNode.getType().equals("BOOLEAN")){
            code = "printf(\"%d";
        } else if(exprNode.getType().equals("REAL")){
            code = "printf(\"%lf";
        } else {
            code = "printf(\"%s";
        }
        String label = (String) writeStatOp.getUserObject();
        if(label.equals("WRITE")){
            code += "\", " + exprNodeCode + ")";
        } else if(label.equals("WRITELN")){
            code += "\\n\", " + exprNodeCode + ")";
        } else if(label.equals("WRITEB")){
            code += " \", " + exprNodeCode + ")";
        } else {
            code += "\\t\", " + exprNodeCode + ")";
        }
        return code;
    }
}
