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
        return null;
    }

    @Override
    public Object visit(FunOpList funOpList) {
        return null;
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
                record.setType("Boolean");
            } else if(constant instanceof Integer){
                record.setType("Integer");
            } else if(constant instanceof Double){
                record.setType("Double");
            } else {
                record.setType("String");
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
                record.setType("Boolean");
            } else if(constant instanceof Integer){
                record.setType("Integer");
            } else if(constant instanceof Double){
                record.setType("Double");
            } else {
                record.setType("String");
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
        return null;
    }

    @Override
    public Object visit(ParamDeclListOp paramDeclListOp) {
        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        return null;
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
            funOpList.accept(this);
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
        return null;
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
