package scope;

import nodes.*;

import java.util.ArrayList;
import java.util.Collections;

public class ScopeVisitor implements Visitor {
    @Override
    public Object visit(AssignStatOp statOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) statOp.getParent()).getSymbolNode();
        statOp.setSymbolNode(parentSymbolNode);
        IdOp idOp = (IdOp) statOp.getChildAt(0);
        idOp.accept(this);
        ExprNode exprNode = (ExprNode) statOp.getChildAt(1);
        exprNode.accept(this);
        return null;
    }

    @Override
    public Object visit(CallFunOp callFunOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) callFunOp.getParent()).getSymbolNode();
        callFunOp.setSymbolNode(parentSymbolNode);

        FunctionNameOp functionNameOp = (FunctionNameOp) callFunOp.getChildAt(0);
        functionNameOp.accept(this);
        if(callFunOp.getChildCount() == 2){
            ExprListOp exprListOp = (ExprListOp) callFunOp.getChildAt(1);
            exprListOp.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(CallParam callParam) {
        SymbolNode parentSymbolNode = ((SyntaxNode) callParam.getParent()).getSymbolNode();
        callParam.setSymbolNode(parentSymbolNode);
        ExprNode exprNode = (ExprNode) callParam.getChildAt(0);
        exprNode.accept(this);
        InOutOp inOutOp = (InOutOp) callParam.getChildAt(1);
        inOutOp.accept(this);
        return null;
    }

    @Override
    public Object visit(ConstOp constOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) constOp.getParent()).getSymbolNode();
        constOp.setSymbolNode(parentSymbolNode);
        return constOp.getUserObject();
    }

    @Override
    public Object visit(ElseOp elseOp) {
        //Dal momento che ElseOp non deve vedere lo scope creato da IfStaOp, viene preso quello del nonno
        SymbolNode parentSymbolNode = ((SyntaxNode) elseOp.getParent().getParent()).getSymbolNode();
        SymbolNode newSymbolNode = new SymbolNode(parentSymbolNode);
        elseOp.setSymbolNode(newSymbolNode);
        //parentSymbolNode.add(newSymbolNode);
        if(elseOp.getChildCount() != 0){
            VarDeclOpList varDeclOpList = (VarDeclOpList) elseOp.getChildAt(0);
            StatOpList statOpList = (StatOpList) elseOp.getChildAt(1);
            ArrayList<Record> varDeclOpListRecords = (ArrayList<Record>) varDeclOpList.accept(this);
            statOpList.accept(this);
            Collections.reverse(varDeclOpListRecords);
            for(Record r : varDeclOpListRecords){
                if(!newSymbolNode.add(new SymbolType(r.getLexeme(), "Var"), r.getType())){
                    throw new AlreadyDeclaredException("Variabile " + r.getLexeme() + " gia' dichiarata!");
                }
            }
        }
        return null;
    }

    @Override
    public Object visit(ExprListOp exprListOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) exprListOp.getParent()).getSymbolNode();
        exprListOp.setSymbolNode(parentSymbolNode);
        InOutOp inOutOp = (InOutOp) exprListOp.getChildAt(0);
        inOutOp.accept(this);
        if(exprListOp.getChildAt(1) instanceof ExprNode){
            ExprNode exprNode = (ExprNode) exprListOp.getChildAt(1);
            exprNode.accept(this);
        } else {
            IdOp idOp = (IdOp) exprListOp.getChildAt(1);
            idOp.accept(this);
        }
        if(exprListOp.getChildCount() == 3){
            ExprListOp exprListOp1 = (ExprListOp) exprListOp.getChildAt(2);
            exprListOp1.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ExprNode exprNode) {
        SymbolNode parentSymbolNode = ((SyntaxNode) exprNode.getParent()).getSymbolNode();
        exprNode.setSymbolNode(parentSymbolNode);
        if(exprNode.getChildCount() == 2){
            ExprNode e1 = (ExprNode) exprNode.getChildAt(0);
            ExprNode e2 = (ExprNode) exprNode.getChildAt(1);
            e1.accept(this);
            e2.accept(this);
        } else if(exprNode.getChildCount() != 0 && exprNode.getChildAt(0) instanceof ExprNode){
            ExprNode e1 = (ExprNode) exprNode.getChildAt(0);
            e1.accept(this);
        } else if(exprNode.getChildCount() != 0 && exprNode.getChildAt(0) instanceof CallFunOp){
            CallFunOp callFunOp = (CallFunOp) exprNode.getChildAt(0);
            callFunOp.accept(this);
        } else if(exprNode.getUserObject() instanceof IdOp){
            IdOp idOp = (IdOp) exprNode.getUserObject();
            idOp.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(FunctionNameOp functionNameOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) functionNameOp.getParent()).getSymbolNode();
        functionNameOp.setSymbolNode(parentSymbolNode);
        return null;
    }

    @Override
    public Object visit(FunOp funOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) funOp.getParent()).getSymbolNode();
        SymbolNode newSymbolNode = new SymbolNode(parentSymbolNode);
        //parentSymbolNode.add(newSymbolNode);
        funOp.setSymbolNode(newSymbolNode);
        IdOp idOp = (IdOp) funOp.getChildAt(0);
        String id = ((Record) idOp.accept(this)).getLexeme();
        ParamDeclListOp paramDeclListOp = (ParamDeclListOp) funOp.getChildAt(1);
        ArrayList<Record> paramDeclListIds = (ArrayList<Record>) paramDeclListOp.accept(this);
        VarDeclOpList varDeclOpList;
        StatOpList statOpList;
        Record outRecord = new Record();
        String formattedType = "";
        for(Record r : paramDeclListIds){
            formattedType += r.getType() + " ";
        }
        outRecord.setLexeme(id);
        if(funOp.getChildAt(2) instanceof TypeOp){
            TypeOp typeOp = (TypeOp) funOp.getChildAt(2);
            String type = (String) typeOp.accept(this);
            formattedType += "-> " + type;
            //outRecord = new Record(id, formattedType);
            //parentSymbolNode.add(id, new SymbolType("Fun", formattedType));
            varDeclOpList = (VarDeclOpList) funOp.getChildAt(3);
            statOpList = (StatOpList) funOp.getChildAt(4);
            statOpList.accept(this);
        } else {
            varDeclOpList = (VarDeclOpList) funOp.getChildAt(2);
            statOpList = (StatOpList) funOp.getChildAt(3);
            statOpList.accept(this);
        }
        outRecord.setType(formattedType);
        ArrayList<Record> varDeclIds = (ArrayList<Record>) varDeclOpList.accept(this);
        //ArrayList<Record> statOpIds = (ArrayList<Record>) statOpList.accept(this);
        ArrayList<Record> outIds = new ArrayList<>();
        Collections.reverse(varDeclIds);
        outIds.addAll(paramDeclListIds);
        outIds.addAll(varDeclIds);
        //outIds.addAll(statOpIds);
        for(Record r : outIds){
            if(!newSymbolNode.add(new SymbolType(r.getLexeme(), "Var"), r.getType())){
                throw new AlreadyDeclaredException("Variabile " + r.getLexeme() + " gia' dichiarata!");
            }
        }
        return outRecord;
    }

    @Override
    public Object visit(FunOpList funOpList) {
        SymbolNode symbolNode = ((SyntaxNode) funOpList.getParent()).getSymbolNode();
        funOpList.setSymbolNode(symbolNode);
        ArrayList<Record> ids = new ArrayList<>();
        if(funOpList.getChildCount() != 0){
            FunOp funOp = (FunOp) funOpList.getChildAt(0);
            Record funOpRecord = (Record) funOp.accept(this);
            FunOpList funOpList1 = (FunOpList) funOpList.getChildAt(1);
            ids = (ArrayList<Record>) funOpList1.accept(this);
            ids.add(funOpRecord);
        }
        return ids;
    }

    @Override
    public Object visit(IdListInitObblOp idListInitObblOp) {
        SymbolNode symbolNode = ((SyntaxNode) idListInitObblOp.getParent()).getSymbolNode();
        idListInitObblOp.setSymbolNode(symbolNode);
        if(idListInitObblOp.getChildAt(0) instanceof IdOp){
            IdOp idOp = (IdOp) idListInitObblOp.getChildAt(0);
            Record record = (Record) idOp.accept(this);
            ConstOp constOp = (ConstOp) idListInitObblOp.getChildAt(1);
            Object constant = constOp.accept(this);
            if(constant instanceof Boolean){
                record.setType("BOOLEAN");
            } else if(constant instanceof Integer){
                record.setType("INTEGER");
            } else if(constant instanceof Double){
                record.setType("REAL");
            } else {
                record.setType("STRING");
            }
            ArrayList<Record> ids = new ArrayList<Record>();
            ids.add(record);
            return ids;
        } else {
            IdListInitObblOp idListInitObblOp1 = (IdListInitObblOp) idListInitObblOp.getChildAt(0);
            ArrayList<Record> ids = (ArrayList<Record>) idListInitObblOp1.accept(this);
            IdOp idOp = (IdOp) idListInitObblOp.getChildAt(1);
            Record record = (Record) idOp.accept(this);
            ConstOp constOp = (ConstOp) idListInitObblOp.getChildAt(2);
            Object constant = constOp.accept(this);
            if(constant instanceof Boolean){
                record.setType("BOOLEAN");
            } else if(constant instanceof Integer){
                record.setType("INTEGER");
            } else if(constant instanceof Double){
                record.setType("REAL");
            } else {
                record.setType("STRING");
            }
            ids.add(record);
            return ids;
        }
    }

    @Override
    public Object visit(IdListInitOp idListInitOp) {
        SymbolNode symbolNode = ((SyntaxNode) idListInitOp.getParent()).getSymbolNode();
        idListInitOp.setSymbolNode(symbolNode);
        if(idListInitOp.getChildAt(0) instanceof IdOp){
            if (idListInitOp.getChildCount() == 2) {
                ExprNode exprNode = (ExprNode) idListInitOp.getChildAt(1);
                exprNode.accept(this);
            }
            IdOp idOp = (IdOp) idListInitOp.getChildAt(0);
            Record record = (Record) idOp.accept(this);
            ArrayList<Record> ids = new ArrayList<Record>();
            ids.add(record);
            return ids;
        } else {
            IdListInitOp idListInitOp1 = (IdListInitOp) idListInitOp.getChildAt(0);
            ArrayList<Record> ids = (ArrayList<Record>) idListInitOp1.accept(this);
            IdOp idOp = (IdOp) idListInitOp.getChildAt(1);
            Record record = (Record) idOp.accept(this);
            if (idListInitOp.getChildCount() == 3) {
                ExprNode exprNode = (ExprNode) idListInitOp.getChildAt(2);
                exprNode.accept(this);
            }
            ids.add(record);
            return ids;
        }
    }

    @Override
    public Object visit(IdListOp idListOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) idListOp.getParent()).getSymbolNode();
        idListOp.setSymbolNode(parentSymbolNode);
        IdOp idOp = (IdOp) idListOp.getChildAt(0);
        idOp.accept(this);
        if(idListOp.getChildCount() == 2){
            IdListOp idListOp1 = (IdListOp) idListOp.getChildAt(1);
            idListOp1.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(IdOp idOp) {
        SymbolNode symbolNode = ((SyntaxNode) idOp.getParent()).getSymbolNode();
        idOp.setSymbolNode(symbolNode);
        Record r = new Record((String) idOp.getUserObject());
        return r;
    }

    @Override
    public Object visit(IfStatOp ifStatOp) {    //IfStatOp crea un nuovo scope
        SymbolNode parentSymbolNode = ((SyntaxNode) ifStatOp.getParent()).getSymbolNode();
        ifStatOp.setSymbolNode(parentSymbolNode);
        //Si assegna a ExprNode la tabella del padre
        ExprNode exprNode = (ExprNode) ifStatOp.getChildAt(0);
        exprNode.accept(this);
        //A questo punto si può creare il nuovo scope
        SymbolNode newSymbolNode = new SymbolNode(parentSymbolNode);
        ifStatOp.setSymbolNode(newSymbolNode);
        //parentSymbolNode.add(newSymbolNode);
        VarDeclOpList varDeclOpList = (VarDeclOpList) ifStatOp.getChildAt(1);
        StatOpList statOpList = (StatOpList) ifStatOp.getChildAt(2);
        ElseOp elseOp = (ElseOp) ifStatOp.getChildAt(3);
        ArrayList<Record> varDeclOpListRecords = (ArrayList<Record>) varDeclOpList.accept(this);
        statOpList.accept(this);
        elseOp.accept(this);
        ArrayList<Record> records = new ArrayList<>();
        records.addAll(varDeclOpListRecords);
        Collections.reverse(records);
        //records.addAll(statOpListRecords);
        //records.addAll(elseOpRecords);
        for(Record r : records){
            if(!newSymbolNode.add(new SymbolType(r.getLexeme(), "Var"), r.getType())){
                throw new AlreadyDeclaredException("Variabile " + r.getLexeme() + " gia' dichiarata!");
            }
        }
        return null;
    }

    @Override
    public Object visit(InOutOp inOutOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) inOutOp.getParent()).getSymbolNode();
        inOutOp.setSymbolNode(parentSymbolNode);
        return null;
    }

    @Override
    public Object visit(MainOp mainOp) {
        SymbolNode symbolNode = ((SyntaxNode) mainOp.getParent()).getSymbolNode();
        SymbolNode newSymbolNode = new SymbolNode(symbolNode);
        mainOp.setSymbolNode(newSymbolNode);
        //symbolNode.add(newSymbolNode);
        VarDeclOpList varDeclOpList = (VarDeclOpList) mainOp.getChildAt(0);
        ArrayList<Record> records = (ArrayList<Record>) varDeclOpList.accept(this);
        Collections.reverse(records);
        for(Record r : records){
            if(!newSymbolNode.add(new SymbolType(r.getLexeme(), "Var"), r.getType())){
                throw new AlreadyDeclaredException("Variabile " + r.getLexeme() + " gia' dichiarata!");
            }
        }
        StatOpList statOpList = (StatOpList) mainOp.getChildAt(1);
        statOpList.accept(this);
        return null;
    }

    @Override
    public Object visit(NonEmptyParamDeclListOp nonEmptyParamDeclListOp) {
        SymbolNode symbolNode = ((SyntaxNode) nonEmptyParamDeclListOp.getParent()).getSymbolNode();
        nonEmptyParamDeclListOp.setSymbolNode(symbolNode);
        if(nonEmptyParamDeclListOp.getChildAt(0) instanceof ParDeclOp){
            ParDeclOp parDeclOp = (ParDeclOp) nonEmptyParamDeclListOp.getChildAt(0);
            ArrayList<Record> parDeclIds = new ArrayList<>();
            Record parDeclId = (Record) parDeclOp.accept(this);
            parDeclIds.add(parDeclId);
            return parDeclIds;
        } else {
            NonEmptyParamDeclListOp nonEmptyParamDeclListOp1 = (NonEmptyParamDeclListOp) nonEmptyParamDeclListOp.getChildAt(0);
            ArrayList<Record> nonEmptyParamDeclListIds = (ArrayList<Record>) nonEmptyParamDeclListOp1.accept(this);
            ParDeclOp parDeclOp = (ParDeclOp) nonEmptyParamDeclListOp.getChildAt(1);
            Record parDeclId = (Record) parDeclOp.accept(this);
            nonEmptyParamDeclListIds.add(parDeclId);
            return nonEmptyParamDeclListIds;
        }
    }

    @Override
    public Object visit(ParamDeclListOp paramDeclListOp) {
        SymbolNode symbolNode = ((SyntaxNode) paramDeclListOp.getParent()).getSymbolNode();
        paramDeclListOp.setSymbolNode(symbolNode);
        ArrayList<Record> paramDeclListIds = new ArrayList<>();
        if(paramDeclListOp.getChildCount() != 0){
            NonEmptyParamDeclListOp nonEmptyParamDeclListOp = (NonEmptyParamDeclListOp) paramDeclListOp.getChildAt(0);
            paramDeclListIds = (ArrayList<Record>) nonEmptyParamDeclListOp.accept(this);
        }
        return paramDeclListIds;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        SymbolNode symbolNode = ((SyntaxNode) parDeclOp.getParent()).getSymbolNode();
        parDeclOp.setSymbolNode(symbolNode);
        InOutOp inOutOp = (InOutOp) parDeclOp.getChildAt(0);
        String mode = (String) inOutOp.getUserObject();
        TypeOp typeOp = (TypeOp) parDeclOp.getChildAt(1);
        IdOp idOp = (IdOp) parDeclOp.getChildAt(2);
        String type = (String) typeOp.accept(this);
        String id = ((Record) idOp.accept(this)).getLexeme();
        if(mode.equals("out")){
            type = "out ".concat(type);
        }
        return new Record(id, type);
    }

    @Override
    public Object visit(ProgramOp programOp) {
        SymbolNode symbolNode = new SymbolNode(null);
        programOp.setSymbolNode(symbolNode);
        if(programOp.getChildAt(0) != null){    //VarDeclOpList
            VarDeclOpList varDeclOpList = (VarDeclOpList) programOp.getChildAt(0);
            ArrayList<Record> records = (ArrayList<Record>) varDeclOpList.accept(this);
            Collections.reverse(records);
            for(Record r : records){
                if(!symbolNode.add(new SymbolType(r.getLexeme(), "Var"), r.getType())){
                    throw new AlreadyDeclaredException("Variabile " + r.getLexeme() + " gia' dichiarata");
                }
            }
            //visit(varDeclOpList);
        }
        if(programOp.getChildAt(1) != null){
            FunOpList funOpList = (FunOpList) programOp.getChildAt(1);
            ArrayList<Record> funOpListRecords = (ArrayList<Record>) funOpList.accept(this);
            Collections.reverse(funOpListRecords);
            for(Record r : funOpListRecords){
                if(!symbolNode.add(new SymbolType(r.getLexeme(), "Fun"), r.getType())){
                    throw new AlreadyDeclaredException("Funzione " + r.getLexeme() + " gia' dichiarata!");
                }
            }
            //visit(funOpList);
        }
        MainOp mainOp = (MainOp) programOp.getChildAt(2);
        mainOp.accept(this);
        //visit(mainOp);
        return symbolNode;
    }

    @Override
    public Object visit(ReadStatOp readStatOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) readStatOp.getParent()).getSymbolNode();
        readStatOp.setSymbolNode(parentSymbolNode);
        IdListOp idListOp = (IdListOp) readStatOp.getChildAt(0);
        idListOp.accept(this);
        if(readStatOp.getChildCount() == 2){
            ExprNode exprNode = (ExprNode) readStatOp.getChildAt(1);
            exprNode.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        SymbolNode symbolNode = ((SyntaxNode) returnOp.getParent()).getSymbolNode();
        returnOp.setSymbolNode(symbolNode);
        return null;
    }

    @Override
    public Object visit(StatOp statOp) {
        SymbolNode symbolNode = ((SyntaxNode) statOp.getParent()).getSymbolNode();
        statOp.setSymbolNode(symbolNode);
        if (statOp.getChildAt(0) instanceof IfStatOp){
            IfStatOp ifStatOp = (IfStatOp) statOp.getChildAt(0);
            ifStatOp.accept(this);
        } else if(statOp.getChildAt(0) instanceof WhileStatOp){
            WhileStatOp whileStatOp = (WhileStatOp) statOp.getChildAt(0);
            whileStatOp.accept(this);
        } else if(statOp.getChildAt(0) instanceof ReadStatOp){
            ReadStatOp readStatOp = (ReadStatOp) statOp.getChildAt(0);
            readStatOp.accept(this);
        } else if(statOp.getChildAt(0) instanceof WriteStatOp){
            WriteStatOp writeStatOp = (WriteStatOp) statOp.getChildAt(0);
            writeStatOp.accept(this);
        } else if(statOp.getChildAt(0) instanceof AssignStatOp){
            AssignStatOp assignStatOp = (AssignStatOp) statOp.getChildAt(0);
            assignStatOp.accept(this);
        } else if(statOp.getChildAt(0) instanceof CallFunOp){
            CallFunOp callFunOp = (CallFunOp) statOp.getChildAt(0);
            callFunOp.accept(this);
        } else {
            ReturnOp returnOp = (ReturnOp) statOp.getChildAt(0);
            returnOp.accept(this);
            ExprNode exprNode = (ExprNode) statOp.getChildAt(1);
            exprNode.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(StatOpList statOpList) {
        SymbolNode symbolNode = ((SyntaxNode) statOpList.getParent()).getSymbolNode();
        statOpList.setSymbolNode(symbolNode);
        ArrayList<Record> outRecords = new ArrayList<>();
        if(statOpList.getChildCount() != 0){
            StatOp statOp = (StatOp) statOpList.getChildAt(0);
            statOp.accept(this);
            StatOpList statOpList1 = (StatOpList) statOpList.getChildAt(1);
            statOpList1.accept(this);
            //outRecords.add(r);
        }
        return null;
    }

    public Object visit(SyntaxNode syntaxNode){
        //never called
        return null;
    }

    @Override
    public Object visit(TypeOp typeOp) {
        SymbolNode symbolNode = ((SyntaxNode) typeOp.getParent()).getSymbolNode();
        typeOp.setSymbolNode(symbolNode);
        return typeOp.getUserObject();
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        SymbolNode symbolNode = ((SyntaxNode) varDeclOp.getParent()).getSymbolNode();
        varDeclOp.setSymbolNode(symbolNode);
        if(varDeclOp.getChildAt(0) instanceof IdListInitObblOp){
            IdListInitObblOp idListInitObblOp = (IdListInitObblOp) varDeclOp.getChildAt(0);
            ArrayList<Record> ids = (ArrayList<Record>) idListInitObblOp.accept(this);
            return ids;
        } else {
            TypeOp typeOp = (TypeOp) varDeclOp.getChildAt(0);
            String type = (String) typeOp.getUserObject();
            IdListInitOp idListInitOp = (IdListInitOp) varDeclOp.getChildAt(1);
            ArrayList<Record> ids = (ArrayList<Record>) idListInitOp.accept(this);
            Collections.reverse(ids);
            for(Record record : ids){
                record.setType(type);
            }
            return ids;
        }
    }

    @Override
    public Object visit(VarDeclOpList varDeclOpList) {
        SymbolNode symbolNode = ((SyntaxNode) varDeclOpList.getParent()).getSymbolNode();
        varDeclOpList.setSymbolNode(symbolNode);
        if(varDeclOpList.children().hasMoreElements()) {
            VarDeclOp varDeclOp = (VarDeclOp) varDeclOpList.getChildAt(0);
            VarDeclOpList varDeclOpList1 = (VarDeclOpList) varDeclOpList.getChildAt(1);
            ArrayList<Record> listVarDeclOp = (ArrayList<Record>) varDeclOp.accept(this);
            ArrayList<Record> listVarDeclOpList = (ArrayList<Record>) varDeclOpList1.accept(this);
            listVarDeclOpList.addAll(listVarDeclOp);
            return listVarDeclOpList;
        } else {
            return new ArrayList<Record>();
        }
    }

    @Override
    public Object visit(WhileStatOp whileStatOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) whileStatOp.getParent()).getSymbolNode();
        SymbolNode newSymbolNode = new SymbolNode(parentSymbolNode);
        whileStatOp.setSymbolNode(newSymbolNode);
        //parentSymbolNode.add(newSymbolNode);
        ExprNode exprNode = (ExprNode) whileStatOp.getChildAt(0);
        exprNode.accept(this);
        VarDeclOpList varDeclOpList = (VarDeclOpList) whileStatOp.getChildAt(1);
        ArrayList<Record> records = (ArrayList<Record>) varDeclOpList.accept(this);
        Collections.reverse(records);
        for(Record r : records){
            if(!newSymbolNode.add(new SymbolType(r.getLexeme(), "Var"), r.getType())){
                throw new AlreadyDeclaredException("Variabile " + r.getLexeme() + " gia' dichiarata!");
            }
        }
        StatOpList statOpList = (StatOpList) whileStatOp.getChildAt(2);
        statOpList.accept(this);
        return null;
    }

    @Override
    public Object visit(WriteStatOp writeStatOp) {
        SymbolNode symbolNode = ((SyntaxNode) writeStatOp.getParent()).getSymbolNode();
        writeStatOp.setSymbolNode(symbolNode);
        ExprNode exprNode = (ExprNode) writeStatOp.getChildAt(0);
        exprNode.accept(this);
        return null;
    }
}
