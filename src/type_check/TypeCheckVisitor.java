package type_check;

import nodes.*;
import scope.SymbolNode;
import scope.SymbolType;

public class TypeCheckVisitor implements Visitor {
    public SymbolType context;
    @Override
    public Object visit(AssignStatOp statOp) {
        IdOp lval = (IdOp) statOp.getChildAt(0);
        if(lval.getSymbolNode().lookup(new SymbolType((String) lval.getUserObject(), "Var")) == null){
            statOp.setType("ERROR");
            throw new TypeCheckException("Assegnamento a variabile " + lval.getUserObject() + " non dichiarata!");
        }
        lval.accept(this);
        String lvalType = lval.getType();
        ExprNode rval = (ExprNode) statOp.getChildAt(1);
        rval.accept(this);
        if(rval.getChildCount() != 0 && rval.getChildAt(0) instanceof IdOp) { //è sicuramente una variabile nel type environment
            IdOp rvalId = (IdOp) rval.getChildAt(0);
            String type = statOp.getSymbolNode().lookup(new SymbolType((String) rvalId.getUserObject(), "Var"));
            if(type == null){
                statOp.setType("ERROR");
                throw new TypeCheckException("Variabile " + rvalId.getUserObject() + " non dichiarata!");
            }
        } else if(rval.getChildCount() != 0 && rval.getChildAt(0) instanceof CallFunOp){ //è sicuramente una chiamata a funzione
            CallFunOp callFunOp = (CallFunOp) rval.getChildAt(0);
            String functionName = (String) ((FunctionNameOp) callFunOp.getChildAt(0)).getUserObject();
            String type = statOp.getSymbolNode().callFunLookup(new SymbolType(functionName, "Fun"), context);
            if(type == null){
                statOp.setType("ERROR");
                throw new TypeCheckException("Funzione " + functionName + " non dichiarata!");
            }

        }
        String rvalType = rval.getType();
        if(rvalType.contains("out")){
            rvalType = rvalType.split("out ")[1].trim();
        }
        if(lvalType.contains("out")) {
            lvalType = lvalType.split("out ")[1].trim();
        }
        if(lvalType.equals(rvalType)){
            statOp.setType("NOTYPE");
        } else {
            statOp.setType("ERROR");
            throw new TypeCheckException("Assegnamento non valido per " + rval.getUserObject() + " !");
        }
        return statOp.getType();
    }

    @Override
    public Object visit(CallFunOp callFunOp) {
        FunctionNameOp functionNameOp = (FunctionNameOp) callFunOp.getChildAt(0);
        functionNameOp.accept(this);
        String functionName = (String) functionNameOp.getUserObject();
        SymbolType symbolType = new SymbolType(functionName, "Fun");
        SymbolNode symbolNode = callFunOp.getSymbolNode();
        String functionType = symbolNode.callFunLookup(symbolType, context);
        if (functionType == null) {
            throw new TypeCheckException("Funzione " + symbolType.getLexeme() + " non trovata");
        }
        String inputTypes;
        String outTypes = "NOTYPE";
        if(functionType.contains("->")){
            inputTypes = functionType.split("->")[0].trim();
            outTypes = functionType.split("->")[1].trim();
        } else {
            inputTypes = functionType.trim();
        }
        if(callFunOp.getChildCount() == 2){
            ExprListOp exprListOp = (ExprListOp) callFunOp.getChildAt(1);
            String callTypes = (String) exprListOp.accept(this);
            if(callTypes.equals(inputTypes)){
                callFunOp.setType(outTypes.trim());
            } else {
                callFunOp.setType("ERROR");
                throw new TypeCheckException("Chiamata a funzione non valida!");
            }
        } else {
            if(inputTypes.equals("")){
                callFunOp.setType(outTypes);
            } else {
                callFunOp.setType("ERROR");
                throw new TypeCheckException("Chiamata a funzione non valida!");
            }
        }
        return callFunOp.getType();
    }

    @Override
    public Object visit(CallParam callParam) {
        //inutile
        return null;
    }

    @Override
    public Object visit(ConstOp constOp) {
        Object constant = constOp.getUserObject();
        String type = "NOTYPE";
        if(constant instanceof Integer){
            type = "INTEGER";
        } else if(constant instanceof Double){
            type = "REAL";
        } else if(constant instanceof String){
            type = "STRING";
        } else if(constant instanceof Boolean){
            type = "BOOLEAN";
        }
        constOp.setType(type);
        return constOp.getType();
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
        InOutOp inOutOp = (InOutOp) exprListOp.getChildAt(0);
        inOutOp.accept(this);
        String mode = (String) inOutOp.getUserObject();
        if(exprListOp.getChildCount() == 2){
            if(exprListOp.getChildAt(1) instanceof ExprNode){
                ExprNode exprNode = (ExprNode) exprListOp.getChildAt(1);
                exprNode.accept(this);
                if(mode.equals("out")){
                    exprListOp.setType("out " + exprNode.getType());
                } else {
                    exprListOp.setType(exprNode.getType());
                }
            } else {
                IdOp idOp = (IdOp) exprListOp.getChildAt(1);
                idOp.accept(this);
                if(idOp.getType() == null){
                    exprListOp.setType("ERROR");
                    throw new TypeCheckException("Variabile " + idOp.getUserObject() + " non dichiarata!");
                }
                if(mode.equals("out")){
                    exprListOp.setType("out " + idOp.getType());
                } else {
                    exprListOp.setType(idOp.getType());
                }
            }
        } else {
            ExprListOp exprListOp1 = (ExprListOp) exprListOp.getChildAt(2);
            exprListOp1.accept(this);
            if(exprListOp.getChildAt(1) instanceof ExprNode){
                ExprNode exprNode = (ExprNode) exprListOp.getChildAt(1);
                exprNode.accept(this);
                if(mode.equals("out")){
                    exprListOp.setType("out " + exprNode.getType() + " " + exprListOp1.getType());
                } else {
                    exprListOp.setType(exprNode.getType() + " " + exprListOp1.getType());
                }
            } else {
                IdOp idOp = (IdOp) exprListOp.getChildAt(1);
                idOp.accept(this);
                if(mode.equals("out")){
                    exprListOp.setType("out " + idOp.getType() + " " + exprListOp1.getType());
                } else {
                    exprListOp.setType(idOp.getType() + " " + exprListOp1.getType());
                }
            }
        }
        return exprListOp.getType();
    }

    @Override
    public Object visit(ExprNode exprNode) {
        if(exprNode.getChildCount() == 0){
            Object tag = exprNode.getUserObject();
            if(tag instanceof String){
                exprNode.setType("STRING");
            } else if(tag instanceof Integer){
                exprNode.setType("INTEGER");
            } else if(tag instanceof Double){
                exprNode.setType("REAL");
            } else {
                exprNode.setType("BOOLEAN");
            }
        } else if(exprNode.getChildCount() == 1){ //Operatore unario
            if(exprNode.getChildAt(0) instanceof ExprNode) {
                String tag = (String) exprNode.getUserObject();
                if (tag.equals("UMINUS")) {
                    ExprNode exprNode1 = (ExprNode) exprNode.getChildAt(0);
                    exprNode1.accept(this);
                    if (exprNode1.getType().equals("INTEGER")) {
                        exprNode.setType("INTEGER");
                    } else if (exprNode1.getType().equals("REAL")) {
                        exprNode.setType("REAL");
                    } else {
                        exprNode1.setType("ERROR");
                        throw new TypeCheckException("Meno unario non valido");
                    }
                } else { //Operatore NOT
                    ExprNode exprNode1 = (ExprNode) exprNode.getChildAt(0);
                    exprNode1.accept(this);
                    if (exprNode1.getType().equals("BOOLEAN")) {
                        exprNode.setType("BOOLEAN");
                    } else {
                        exprNode1.setType("ERROR");
                        throw new TypeCheckException("NOT non valido");
                    }
                }
            } else if(exprNode.getChildAt(0) instanceof CallFunOp){
                CallFunOp callFunOp = (CallFunOp) exprNode.getChildAt(0);
                callFunOp.accept(this);
                exprNode.setType(callFunOp.getType());
            } else {
                IdOp idOp = (IdOp) exprNode.getChildAt(0);
                String type = (String) idOp.accept(this);
                if(type == null){
                    exprNode.setType("ERROR");
                    throw new TypeCheckException("Variabile " + idOp.getUserObject() + " non dichiarata");
                }
                //SymbolNode symbolNode = exprNode.getSymbolNode();
                idOp.setType(type);
                exprNode.setType(type);
            }
        } else { //Operatore binario
            String operator = (String) exprNode.getUserObject();
            ExprNode e1 = (ExprNode) exprNode.getChildAt(0);
            ExprNode e2 = (ExprNode) exprNode.getChildAt(1);
            e1.accept(this);
            e2.accept(this);
            String type1 = e1.getType();
            String type2 = e2.getType();
            if(type1 == null || type2 == null){
                exprNode.setType("ERROR");
                throw new TypeCheckException("Variabile non dichiarata");
            }
            if (type1.contains("out")) {
                type1 = type1.split("out ")[1].trim();
            }
            if (type2.contains("out")) {
                type2 = type2.split("out ")[1].trim();
            }
            switch (operator){
                case "+":
                case "-":
                case "*":
                case "/":
                case "^":
                    if(type1.equals("INTEGER") && type2.equals("INTEGER")){
                        exprNode.setType("INTEGER");
                    } else if(type1.equals("REAL") && type2.equals("REAL")){
                        exprNode.setType("REAL");
                    } else if(type1.equals("INTEGER") && type2.equals("REAL")){
                        exprNode.setType("REAL");
                    } else if(type1.equals("REAL") && type2.equals("INTEGER")){
                        exprNode.setType("REAL");
                    } else {
                        exprNode.setType("ERROR");
                        throw new TypeCheckException("Operazione binaria non definita!");
                    }
                    break;
                case "DivInt":
                    if(type1.equals("INTEGER") && type2.equals("INTEGER")){
                        exprNode.setType("INTEGER");
                    } else {
                        exprNode.setType("ERROR");
                        throw new TypeCheckException("Divisione intera consentita solo tra due interi!");
                    }
                    break;
                case "AND":
                case "OR":
                    if(type1.equals("BOOLEAN") && type2.equals("BOOLEAN")){
                        exprNode.setType("BOOLEAN");
                    } else {
                        exprNode.setType("ERROR");
                        throw new TypeCheckException("AND e OR consentiti solo tra due booleani!");
                    }
                    break;
                case "&":
                    exprNode.setType("STRING");
                    break;
                case ">":
                case ">=":
                case "<":
                case "<=":
                    if(type1.equals("INTEGER") && type2.equals("INTEGER") ||
                            type1.equals("REAL") && type2.equals("REAL") ||
                            type1.equals("INTEGER") && type2.equals("REAL") ||
                            type1.equals("REAL") && type2.equals("INTEGER")){
                        exprNode.setType("BOOLEAN");
                    } else {
                        exprNode.setType("ERROR");
                        throw new TypeCheckException("Comparazione tra tipi non compatibili!");
                    }
                    break;
                case "=":
                case "!=":
                    if(type1.equals("INTEGER") && type2.equals("INTEGER") ||
                        type1.equals("REAL") && type2.equals("REAL") ||
                        type1.equals("INTEGER") && type2.equals("REAL") ||
                        type1.equals("REAL") && type2.equals("INTEGER") ||
                        type1.equals("STRING") && type2.equals("STRING") ||
                        type1.equals("BOOLEAN") && type2.equals("BOOLEAN")){
                        exprNode.setType("BOOLEAN");
                    } else {
                        exprNode.setType("ERROR");
                        throw new TypeCheckException("Comparazione tra tipi non compatibili!");
                    }
                    break;
            }
        }
        return exprNode.getType();
    }

    @Override
    public Object visit(FunctionNameOp functionNameOp) {
        functionNameOp.setType("NOTYPE");
        return functionNameOp.getType();
    }

    @Override
    public Object visit(FunOp funOp) {
        IdOp idOp = (IdOp) funOp.getChildAt(0);
        idOp.accept(this);
        context = new SymbolType((String) idOp.getUserObject(), "Fun");
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
    public Object visit(IdListInitObblOp idListInitObblOp) { //Lista di id su cui fare inferenza
        if(idListInitObblOp.getChildCount() == 2){
            IdOp idOp = (IdOp) idListInitObblOp.getChildAt(0);
            idOp.accept(this);
            ConstOp constOp = (ConstOp) idListInitObblOp.getChildAt(1);
            constOp.accept(this);
            if(!idOp.getType().equals(constOp.getType())){
                idListInitObblOp.setType("ERROR");
                throw new TypeCheckException("Assegnamento tra tipi non compatibili");
            }
        } else {
            IdListInitObblOp idListInitObblOp1 = (IdListInitObblOp) idListInitObblOp.getChildAt(0);
            idListInitObblOp1.accept(this);
            IdOp idOp = (IdOp) idListInitObblOp.getChildAt(1);
            idOp.accept(this);
            ConstOp constOp = (ConstOp) idListInitObblOp.getChildAt(2);
            constOp.accept(this);
            if(!idOp.getType().equals(constOp.getType())){
                idListInitObblOp.setType("ERROR");
                throw new TypeCheckException("Assegnamento tra tipi non compatibili");
            }
        }
        idListInitObblOp.setType("NOTYPE");
        return null;
    }

    @Override
    public Object visit(IdListInitOp idListInitOp) { //Lista di id che si stanno dichiarano con inzializzazione (no inferenza)
        if(idListInitOp.getChildCount() == 1){
            IdOp idOp = (IdOp) idListInitOp.getChildAt(0);
            idOp.accept(this);
        } else {
            IdOp idOp;
            ExprNode exprNode;
            if (idListInitOp.getChildCount() == 2 && idListInitOp.getChildAt(0) instanceof IdOp) { //Siamo alla fine della lista oppure è solo una variabile
                idOp = (IdOp) idListInitOp.getChildAt(0);
                exprNode = (ExprNode) idListInitOp.getChildAt(1);
            } else if(idListInitOp.getChildCount() == 2 && idListInitOp.getChildAt(0) instanceof IdListInitOp){
                IdListInitOp idListInitOp1 = (IdListInitOp) idListInitOp.getChildAt(0);
                idListInitOp1.accept(this);
                idOp = (IdOp) idListInitOp.getChildAt(1);
                idOp.accept(this);
                idListInitOp.setType("NOTYPE");
                return idListInitOp.getType();
            } else { //Dobbiamo scorrere la lista e fare ad ogni inizializzazione i controlli
                IdListInitOp idListInitOp1 = (IdListInitOp) idListInitOp.getChildAt(0);
                idListInitOp1.accept(this);
                idOp = (IdOp) idListInitOp.getChildAt(1);
                exprNode = (ExprNode) idListInitOp.getChildAt(2);
            }
            idOp.accept(this);
            exprNode.accept(this);
            if(exprNode.getChildCount() != 0 && exprNode.getChildAt(0) instanceof CallFunOp){ //inizializziamo con una chiamata
                FunctionNameOp functionNameOp = (FunctionNameOp) exprNode.getChildAt(0).getChildAt(0);
                String callFunName = (String) functionNameOp.getUserObject();
                String callFunType = idListInitOp.getSymbolNode().callFunLookup(new SymbolType(callFunName, "Fun"), context);
                if(callFunType == null){
                    idListInitOp.setType("ERROR");
                    throw new TypeCheckException("Funzione " + callFunName + " non dichiarata!");
                }
                CallFunOp callFunOp = (CallFunOp) exprNode.getChildAt(0);
                callFunType = callFunOp.getType();
                if(!idOp.getType().equals(callFunType)){
                    idListInitOp.setType("ERROR");
                    throw new TypeCheckException("Assegnamento non valido!");
                }
            } else if(exprNode.getChildCount() != 0 && exprNode.getChildAt(0) instanceof IdOp) { //Inizializziamo con un altra variabile
                IdOp rval = (IdOp) exprNode.getChildAt(0);
                String rvalType = idListInitOp.getSymbolNode().varDeclLookup(new SymbolType((String) idOp.getUserObject(), "Var"), new SymbolType((String) rval.getUserObject(), "Var"), context);
                if(rvalType == null){
                    idListInitOp.setType("ERROR");
                    throw new TypeCheckException("Variabile " + rval.getUserObject() + " non dichiarata!");
                }
                if (rvalType.contains("out")) {
                    rvalType = rvalType.split("out ")[1].trim();
                }
                if(!idOp.getType().equals(rvalType)){
                    idListInitOp.setType("ERROR");
                    throw new TypeCheckException("Assegnamento non valido!");
                }
            } else { //Inizializziamo con un'espressione
                String exprType = exprNode.getType();
                if(!idOp.getType().equals(exprType)){
                    idListInitOp.setType("ERROR");
                    throw new TypeCheckException("Assegnamento non valido!");
                }
            }
        }
        idListInitOp.setType("NOTYPE");
        return idListInitOp.getType();
    }

    @Override
    public Object visit(IdListOp idListOp) { //Non dobbiamo fare controlli, sono solo una lista di variabili
        IdOp idOp = (IdOp) idListOp.getChildAt(0);
        idOp.accept(this);
        if(idOp.getType() == null){
            idListOp.setType("ERROR");
            throw new TypeCheckException("Variabile " + idOp.getUserObject() + " non dichiarata!");
        }
        if(idListOp.getChildCount() == 2){
            IdListOp idListOp1 = (IdListOp) idListOp.getChildAt(1);
            idListOp1.accept(this);
        }
        idListOp.setType("NOTYPE");
        return idListOp.getType();
    }

    @Override
    public Object visit(IdOp idOp) {
        SymbolNode symbolNode = idOp.getSymbolNode();
        String output;
        if(idOp.getParent() instanceof FunOp){
            output = symbolNode.lookup(new SymbolType((String) idOp.getUserObject(), "Fun"));
        } else {
            output = symbolNode.lookup(new SymbolType((String) idOp.getUserObject(), "Var"));
        }
        idOp.setType(output);
        return idOp.getType();
    }

    @Override
    public Object visit(IfStatOp ifStatOp) {
        ExprNode exprNode = (ExprNode) ifStatOp.getChildAt(0);
        exprNode.accept(this);
        if(!exprNode.getType().equals("BOOLEAN")){
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
        return ifStatOp.getType();
    }

    @Override
    public Object visit(InOutOp inOutOp) {
        inOutOp.setType("NOTYPE");
        return inOutOp.getType();
    }

    @Override
    public Object visit(MainOp mainOp) {
        context = new SymbolType("main", "Fun");
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
        InOutOp inOutOp = (InOutOp) parDeclOp.getChildAt(0);
        TypeOp typeOp = (TypeOp) parDeclOp.getChildAt(1);
        IdOp idOp = (IdOp) parDeclOp.getChildAt(2);

        inOutOp.accept(this);
        typeOp.accept(this);
        idOp.accept(this);

        parDeclOp.setType("NOTYPE");
        return parDeclOp.getType();
    }

    @Override
    public Object visit(ProgramOp programOp) {
        context = null;
        VarDeclOpList varDeclOpList = (VarDeclOpList) programOp.getChildAt(0);
        FunOpList funOpList = (FunOpList) programOp.getChildAt(1);
        MainOp mainOp = (MainOp) programOp.getChildAt(2);

        varDeclOpList.accept(this);
        funOpList.accept(this);
        mainOp.accept(this);

        programOp.setType("NOTYPE");
        return programOp.getType();
    }

    @Override
    public Object visit(ReadStatOp readStatOp) {
        IdListOp idListOp = (IdListOp) readStatOp.getChildAt(0);
        idListOp.accept(this);

        if (readStatOp.getChildCount() == 2) {
            ExprNode exprNode = (ExprNode) readStatOp.getChildAt(1);
            exprNode.accept(this);
            if(!exprNode.getType().equals("STRING")){
                readStatOp.setType("ERROR");
                throw new TypeCheckException("L'espressione della Read deve essere una stringa!");
            }
        }

        readStatOp.setType("NOTYPE");
        return readStatOp.getType();
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        returnOp.setType("NOTYPE");
        return returnOp.getType();
    }

    @Override
    public Object visit(StatOp statOp) {
        SyntaxNode node = (SyntaxNode) statOp.getChildAt(0);

        if (node instanceof IfStatOp) {
            IfStatOp ifStatOp = (IfStatOp) node;
            ifStatOp.accept(this);
        } else if (node instanceof WhileStatOp){
            WhileStatOp whileStatOp = (WhileStatOp) node;
            whileStatOp.accept(this);
        } else if (node instanceof ReadStatOp) {
            ReadStatOp readStatOp = (ReadStatOp) node;
            readStatOp.accept(this);
        } else if (node instanceof WriteStatOp) {
            WriteStatOp writeStatOp = (WriteStatOp) node;
            writeStatOp.accept(this);
        } else if (node instanceof AssignStatOp) {
            AssignStatOp assignStatOp = (AssignStatOp) node;
            assignStatOp.accept(this);
        } else if (node instanceof CallFunOp) {
            CallFunOp callFunOp = (CallFunOp) node;
            callFunOp.accept(this);
        } else {
            ReturnOp returnOp = (ReturnOp) node;
            ExprNode exprNode = (ExprNode) statOp.getChildAt(1);

            returnOp.accept(this);
            exprNode.accept(this);
        }
        statOp.setType("NOTYPE");
        return statOp.getType();
    }

    @Override
    public Object visit(StatOpList statOpList) {
        if (statOpList.getChildCount() != 0) {
            StatOp statOp = (StatOp) statOpList.getChildAt(0);
            StatOpList statOpList1 = (StatOpList) statOpList.getChildAt(1);

            statOp.accept(this);
            statOpList1.accept(this);
        }
        statOpList.setType("NOTYPE");
        return statOpList.getType();
    }

    @Override
    //Inutile
    public Object visit(SyntaxNode syntaxNode) {
        return null;
    }

    @Override
    public Object visit(TypeOp typeOp) {
        typeOp.setType("NOTYPE");
        return typeOp.getType();
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        if (varDeclOp.getChildAt(0) instanceof TypeOp) {
            TypeOp typeOp = (TypeOp) varDeclOp.getChildAt(0);
            IdListInitOp idListInitOp = (IdListInitOp) varDeclOp.getChildAt(1);
            typeOp.accept(this);
            idListInitOp.accept(this);
        } else {
            IdListInitObblOp idListInitObblOp = (IdListInitObblOp) varDeclOp.getChildAt(0);
            idListInitObblOp.accept(this);
        }
        varDeclOp.setType("NOTYPE");
        return varDeclOp.getType();
    }

    @Override
    public Object visit(VarDeclOpList varDeclOpList) {
        if (varDeclOpList.getChildCount() == 0) {
            varDeclOpList.setType("NOTYPE");
            return varDeclOpList.getType();
        }
        VarDeclOp varDeclOp = (VarDeclOp) varDeclOpList.getChildAt(0);
        varDeclOp.accept(this);
        VarDeclOpList varDeclOpList1 = (VarDeclOpList) varDeclOpList.getChildAt(1);
        varDeclOpList1.accept(this);
        varDeclOpList.setType("NOTYPE");
        return varDeclOpList.getType();
    }

    @Override
    public Object visit(WhileStatOp whileStatOp) {
        ExprNode exprNode = (ExprNode) whileStatOp.getChildAt(0);
        VarDeclOpList varDeclOpList = (VarDeclOpList) whileStatOp.getChildAt(1);
        StatOpList statOpList = (StatOpList) whileStatOp.getChildAt(2);

        exprNode.accept(this);
        String exprType = exprNode.getType();
        if (!exprType.equals("BOOLEAN")) {
            whileStatOp.setType("ERROR");
            throw new TypeCheckException("La condizione del While deve essere booleana");
        }
        varDeclOpList.accept(this);
        statOpList.accept(this);
        whileStatOp.setType("NOTYPE");
        return whileStatOp.getType();
    }

    @Override
    public Object visit(WriteStatOp writeStatOp) {
        ExprNode exprNode = (ExprNode) writeStatOp.getChildAt(0);
        exprNode.accept(this);

        writeStatOp.setType("NOTYPE");
        return writeStatOp.getType();
    }
}
