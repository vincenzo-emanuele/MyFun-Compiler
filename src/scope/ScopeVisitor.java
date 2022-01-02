package scope;

import nodes.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ScopeVisitor implements Visitor {
    @Override
    public Object visit(AssignStatOp statOp) {
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        return null;
    }

    @Override
    public Object visit(CallFunOp callFunOp) {
        return null;
    }

    @Override
    public Object visit(CallParam callParam) {
        return null;
    }

    @Override
    public Object visit(CallParamList callParamList) {
        return null;
    }

    @Override
    public Object visit(ConstOp constOp) {
        return constOp.getUserObject();
    }

    @Override
    public Object visit(ElseOp elseOp) {
        return null;
    }

    @Override
    public Object visit(ExprListOp exprListOp) {
        return null;
    }

    @Override
    public Object visit(ExprNode exprNode) {
        return null;
    }

    @Override
    public Object visit(FunctionNameOp functionNameOp) {
        return null;
    }

    @Override
    public Object visit(FunOp funOp) {
        SymbolNode parentSymbolNode = ((SyntaxNode) funOp.getParent()).getSymbolNode();
        SymbolNode newSymbolNode = new SymbolNode(parentSymbolNode);
        parentSymbolNode.add(newSymbolNode);
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
        } else {
            varDeclOpList = (VarDeclOpList) funOp.getChildAt(2);
            statOpList = (StatOpList) funOp.getChildAt(3);
        }
        outRecord.setType(formattedType);
        ArrayList<Record> varDeclIds = (ArrayList<Record>) varDeclOpList.accept(this);
        //ArrayList<Record> statOpIds = (ArrayList<Record>) statOpList.accept(this);
        ArrayList<Record> outIds = new ArrayList<>();
        outIds.addAll(paramDeclListIds);
        outIds.addAll(varDeclIds);
        //outIds.addAll(statOpIds);
        for(Record r : outIds){
            if(!newSymbolNode.add(r.getLexeme(), new SymbolType("Var", r.getType()))){
                System.out.println("VARIABILE GIA DICHIARATA"); //TODO: gestione dell'errore
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
            //System.out.println("LISTAA " + ids);
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
                record.setType("DOUBLE");
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
                record.setType("DOUBLE");
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
            ids.add(record);
            return ids;
        }
    }

    @Override
    public Object visit(IdListOp idListOp) {
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
    public Object visit(IfStatOp ifStatOp) {
        return null;
    }

    @Override
    public Object visit(InOutOp inOutOp) {
        return null;
    }

    @Override
    public Object visit(MainOp mainOp) {
        return null;
    }

    @Override
    public Object visit(NodeOp nodeOp) {
        return null;
    }

    @Override
    public Object visit(NodeValue nodeValue) {
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
        TypeOp typeOp = (TypeOp) parDeclOp.getChildAt(1);
        IdOp idOp = (IdOp) parDeclOp.getChildAt(2);
        String type = (String) typeOp.accept(this);
        String id = ((Record) idOp.accept(this)).getLexeme();
        System.out.println("ID: " + id);
        return new Record(id, type);
    }

    @Override
    public Object visit(ProgramOp programOp) {
        SymbolNode symbolNode = new SymbolNode(null);
        programOp.setSymbolNode(symbolNode);
        if(programOp.getChildAt(0) != null){    //VarDeclOpList
            VarDeclOpList varDeclOpList = (VarDeclOpList) programOp.getChildAt(0);
            ArrayList<Record> records = (ArrayList<Record>) varDeclOpList.accept(this);
            for(Record record : records){
                symbolNode.add(record.getLexeme(), new SymbolType("Var", record.getType()));
            }
            //visit(varDeclOpList);
        }
        if(programOp.getChildAt(1) != null){
            FunOpList funOpList = (FunOpList) programOp.getChildAt(1);
            ArrayList<Record> funOpListRecords = (ArrayList<Record>) funOpList.accept(this);
            for(Record r : funOpListRecords){
                symbolNode.add(r.getLexeme(), new SymbolType("Fun", r.getType()));
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
            //visit(idListInitObblOp);
        } else {
            TypeOp typeOp = (TypeOp) varDeclOp.getChildAt(0);
            String type = (String) typeOp.getUserObject();
            IdListInitOp idListInitOp = (IdListInitOp) varDeclOp.getChildAt(1);
            ArrayList<Record> ids = (ArrayList<Record>) idListInitOp.accept(this);
            for(Record r : ids){
                r.setType(type);
            }
            return ids;
        }
    }

    @Override
    public Object visit(VarDeclOpList varDeclOpList) {
        if(varDeclOpList.children().hasMoreElements()) {
            VarDeclOp varDeclOp = (VarDeclOp) varDeclOpList.getChildAt(0);
            VarDeclOpList varDeclOpList1 = (VarDeclOpList) varDeclOpList.getChildAt(1);
            SymbolNode symbolNode = ((SyntaxNode) varDeclOpList.getParent()).getSymbolNode();
            varDeclOpList.setSymbolNode(symbolNode);
            ArrayList<Record> listVarDeclOp = (ArrayList<Record>) varDeclOp.accept(this);
            ArrayList<Record> listVarDeclOpList = (ArrayList<Record>) varDeclOpList1.accept(this);
            listVarDeclOpList.addAll(listVarDeclOp);
            return listVarDeclOpList;
            //visit(varDeclOp);
            //visit(varDeclOpList1);
        } else {
            return new ArrayList<Record>();
        }
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
