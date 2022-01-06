package type_check;

import nodes.*;
import scope.SymbolNode;
import scope.SymbolType;

import java.util.ArrayList;

public class TypeCheckVisitor implements Visitor {
    @Override
    public Object visit(AssignStatOp statOp) {
        IdOp lval = (IdOp) statOp.getChildAt(0);
        ExprNode rval = (ExprNode) statOp.getChildAt(1);
        lval.accept(this);
        String lvalType = lval.getType();
        rval.accept(this);
        String rvalType = rval.getType();
        if(lvalType.equals(rvalType)){
            statOp.setType("NOTYPE");
        } else {
            statOp.setType("ERROR");
            throw new TypeCheckException("Assegnamento non valido!");
        }
        return null;
    }

    @Override
    public Object visit(CallFunOp callFunOp) {
        FunctionNameOp functionNameOp = (FunctionNameOp) callFunOp.getChildAt(0);
        String functionName = (String) functionNameOp.accept(this);
        SymbolType symbolType = new SymbolType(functionName, "Fun");
        SymbolNode symbolNode = callFunOp.getSymbolNode();
        String functionType = symbolNode.lookup(symbolType);
        String[] inParams;
        String returnParam;
        if(functionType.contains("->")){
            if(functionType.charAt(0) == '-'){
                returnParam = functionName.split("->")[1].trim();
            } else {
                inParams = functionName.split(" -> ")[0].split(" ");
            }
        } else {

        }
        String[] types = functionType.split(" -> ");
        if(callFunOp.getChildCount() == 2){
            ExprListOp exprListOp = (ExprListOp) callFunOp.getChildAt(1);
            String callTypes = (String) exprListOp.accept(this);
            if(callTypes.equals(types[0])){
                if(types.length == 2){
                    callFunOp.setType(types[1]);
                } else {
                    callFunOp.setType("NOTYPE");
                }
            } else {
                callFunOp.setType("ERROR");
                throw new TypeCheckException("Chiamata a funzione non valida!");
            }
        } else {
            if(types[0].equals("")){

            }
        }
        return null;
    }

    @Override
    public Object visit(CallParam callParam) {
        return null;
    }

    @Override
    public Object visit(ConstOp constOp) {
        Object constant = constOp.getUserObject();
        String type = constant.getClass().getName().toUpperCase();
        constOp.setType(type);
        return type;
    }

    @Override
    public Object visit(ElseOp elseOp) {
        if(elseOp.getChildCount() != 0){
            VarDeclOpList varDeclOpList = (VarDeclOpList) elseOp.getChildAt(0);
            StatOpList statOpList = (StatOpList) elseOp.getChildAt(1);
            varDeclOpList.accept(this);
            statOpList.accept(this);
        }
        elseOp.setType("NOTYPE");
        return null;
    }

    @Override
    public Object visit(ExprListOp exprListOp) {
        return null;
    }

    @Override
    public Object visit(ExprNode exprNode) {
        if(exprNode.getChildCount() == 0){
            Object tag = exprNode.getUserObject();

        }
        return null;
    }

    @Override
    public Object visit(FunctionNameOp functionNameOp) {
        functionNameOp.setType("NOTYPE");
        return functionNameOp.getUserObject();
    }

    @Override
    public Object visit(FunOp funOp) {
        IdOp idOp = (IdOp) funOp.getChildAt(0);
        idOp.accept(this);
        ParamDeclListOp paramDeclListOp = (ParamDeclListOp) funOp.getChildAt(1);
        paramDeclListOp.accept(this);
        if(funOp.getChildAt(2) instanceof TypeOp){
            TypeOp typeOp = (TypeOp) funOp.getChildAt(2);
            typeOp.accept(this);
            VarDeclOpList varDeclOpList = (VarDeclOpList) funOp.getChildAt(3);
            varDeclOpList.accept(this);
            StatOpList statOpList = (StatOpList) funOp.getChildAt(4);
            statOpList.accept(this);
        } else {
            VarDeclOpList varDeclOpList = (VarDeclOpList) funOp.getChildAt(2);
            varDeclOpList.accept(this);
            StatOpList statOpList = (StatOpList) funOp.getChildAt(3);
            statOpList.accept(this);
        }
        funOp.setType(idOp.getType());
        return null;
    }

    @Override
    public Object visit(FunOpList funOpList) {
        if(funOpList.getChildCount() != 0){
            FunOp funOp = (FunOp) funOpList.getChildAt(0);
            funOp.accept(this);
            FunOpList funOpList1 = (FunOpList) funOpList.getChildAt(1);
            funOpList1.accept(this);
        }
        funOpList.setType("NOTYPE");
        return null;
    }

    @Override
    public Object visit(IdListInitObblOp idListInitObblOp) {
        if(idListInitObblOp.getChildCount() == 3){
            IdOp idOp = (IdOp) idListInitObblOp.getChildAt(0);
            idOp.accept(this);
            ConstOp constOp = (ConstOp) idListInitObblOp.getChildAt(1);
            constOp.accept(this);
        } else {
            IdListInitObblOp idListInitObblOp1 = (IdListInitObblOp) idListInitObblOp.getChildAt(0);
            idListInitObblOp1.accept(this);
            IdOp idOp = (IdOp) idListInitObblOp.getChildAt(1);
            idOp.accept(this);
            ConstOp constOp = (ConstOp) idListInitObblOp.getChildAt(2);
            constOp.accept(this);
        }
        idListInitObblOp.setType("NOTYPE");
        return null;
    }

    @Override
    public Object visit(IdListInitOp idListInitOp) {
        if(idListInitOp.getChildCount() == 1){
            IdOp idOp = (IdOp) idListInitOp.getChildAt(0);
            idOp.accept(this);
        } else if(idListInitOp.getChildCount() == 2){
            if(idListInitOp.getChildAt(0) instanceof IdOp){
                IdOp idOp = (IdOp) idListInitOp.getChildAt(0);
                idOp.accept(this);
                ExprNode exprNode = (ExprNode) idListInitOp.getChildAt(1);
                exprNode.accept(this);
            } else {
                IdListInitOp idListInitOp1 = (IdListInitOp) idListInitOp.getChildAt(0);
                idListInitOp1.accept(this);
                IdOp idOp = (IdOp) idListInitOp.getChildAt(1);
                idOp.accept(this);
            }
        } else {
            IdListInitOp idListInitOp1 = (IdListInitOp) idListInitOp.getChildAt(0);
            idListInitOp1.accept(this);
            IdOp idOp = (IdOp) idListInitOp.getChildAt(1);
            idOp.accept(this);
            ExprNode exprNode = (ExprNode) idListInitOp.getChildAt(2);
            exprNode.accept(this);
        }
        idListInitOp.setType("NOTYPE");
        return null;
    }

    @Override
    public Object visit(IdListOp idListOp) {
        ExprNode exprNode = (ExprNode) idListOp.getChildAt(0);
        exprNode.accept(this);
        if(idListOp.getChildCount() == 2){
            IdListOp idListOp1 = (IdListOp) idListOp.getChildAt(1);
            idListOp1.accept(this);
        }
        idListOp.setType("NOTYPE");
        return null;
    }

    @Override
    public Object visit(IdOp idOp) {
        //TODO: implementare la gestione del tipo (funzione vs variabile)
        SymbolNode symbolNode = idOp.getSymbolNode();
        String idType = null;
        while (symbolNode.getParent() != null){

        }
        return null;
    }

    @Override
    public Object visit(IfStatOp ifStatOp) {
        ExprNode exprNode = (ExprNode) ifStatOp.getChildAt(0);
        exprNode.accept(this);
        if(!exprNode.getType().equals("Boolean")){
            ifStatOp.setType("ERROR");
            throw new TypeCheckException("La condizione dell'if deve essere booleana");
        }
        VarDeclOpList varDeclOpList = (VarDeclOpList) ifStatOp.getChildAt(1);
        varDeclOpList.accept(this);
        StatOpList statOpList = (StatOpList) ifStatOp.getChildAt(2);
        statOpList.accept(this);
        ElseOp elseOp = (ElseOp) ifStatOp.getChildAt(3);
        elseOp.accept(this);
        ifStatOp.setType("NOTYPE");
        return null;
    }

    @Override
    public Object visit(InOutOp inOutOp) {
        inOutOp.setType("NOTYPE");
        return inOutOp.getUserObject();
    }

    @Override
    public Object visit(MainOp mainOp) {
        VarDeclOpList varDeclOpList = (VarDeclOpList) mainOp.getChildAt(0);
        varDeclOpList.accept(this);
        StatOpList statOpList = (StatOpList) mainOp.getChildAt(1);
        statOpList.accept(this);
        mainOp.setType("NOTYPE");
        return null;
    }

    @Override
    public Object visit(NonEmptyParamDeclListOp nonEmptyParamDeclListOp) {
        if(nonEmptyParamDeclListOp.getChildCount() == 2){
            NonEmptyParamDeclListOp nonEmptyParamDeclListOp1 = (NonEmptyParamDeclListOp) nonEmptyParamDeclListOp.getChildAt(0);
            nonEmptyParamDeclListOp1.accept(this);
            ParDeclOp parDeclOp = (ParDeclOp) nonEmptyParamDeclListOp.getChildAt(1);
            parDeclOp.accept(this);
        } else {
            ParDeclOp parDeclOp = (ParDeclOp) nonEmptyParamDeclListOp.getChildAt(0);
            parDeclOp.accept(this);
        }
        nonEmptyParamDeclListOp.setType("NOTYPE");
        return null;
    }

    @Override
    public Object visit(ParamDeclListOp paramDeclListOp) {
        if(paramDeclListOp.getChildCount() != 0){
            NonEmptyParamDeclListOp nonEmptyParamDeclListOp = (NonEmptyParamDeclListOp) paramDeclListOp.getChildAt(0);
            nonEmptyParamDeclListOp.accept(this);
        }
        paramDeclListOp.setType("NOTYPE");
        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        return null;
    }

    @Override
    public Object visit(ProgramOp programOp) {
        return null;
    }

    @Override
    public Object visit(ReadStatOp readStatOp) {
        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        return null;
    }

    @Override
    public Object visit(StatOp statOp) {
        return null;
    }

    @Override
    public Object visit(StatOpList statOpList) {
        return null;
    }

    @Override
    public Object visit(SyntaxNode syntaxNode) {
        return null;
    }

    @Override
    public Object visit(TypeOp typeOp) {
        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        return null;
    }

    @Override
    public Object visit(VarDeclOpList varDeclOpList) {
        return null;
    }

    @Override
    public Object visit(WhileStatOp whileStatOp) {
        return null;
    }

    @Override
    public Object visit(WriteStatOp writeStatOp) {
        return null;
    }
}
