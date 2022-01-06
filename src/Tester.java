// Circuit.java

import nodes.*;
import scope.ScopeVisitor;
import scope.SymbolNode;
import scope.SymbolType;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.io.FileReader;

public class Tester {
    public static void main(String[] args) throws Exception {
        String filePath;
        String separator = File.separator;
        if(args.length != 2) {
            System.out.println("Controlla gli argomenti");
            return ;
        }
        if(separator.equals("/")){
            filePath = args[0] + "/" + args[1];
        } else {
            filePath = args[0] + "\\" + args[1];
        }
        FileReader fr = new FileReader(filePath);
        parser p = new parser(new Yylex(fr));
        //System.out.println("Output: " + p.debug_parse().value); // l'uso di p.debug_parse() al posto di p.parse() produce tutte le azioni del parser durante il riconoscimento
        JFrame frame = new JFrame("Manual Nodes");
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) p.parse().value;
        ScopeVisitor scopeVisitor = new ScopeVisitor();
        SymbolNode symbolNode2 = (SymbolNode) scopeVisitor.visit((ProgramOp) root);
        DefaultMutableTreeNode symbTable = (DefaultMutableTreeNode) scopeVisitor.visit((ProgramOp) root);
        JTree tree = new JTree(root);
        //WhileStatOp mainOp = ((WhileStatOp) root.getChildAt(2).getChildAt(1).getChildAt(1).getChildAt(1).getChildAt(1).getChildAt(1).getChildAt(0).getChildAt(0));
        ProgramOp programOp = (ProgramOp) root;
        System.out.println("PROGRAMOP: " + programOp.getSymbolNode());
        IfStatOp ifStatOp = (IfStatOp) root.getChildAt(1).getChildAt(1).getChildAt(0).getChildAt(4).getChildAt(0).getChildAt(0);
        System.out.println("IF: " + ifStatOp.getSymbolNode());
        String out = ifStatOp.getSymbolNode().partialLookup(new SymbolType("result", "Var"), new SymbolType("doppio", "Var"), new SymbolType("sommac", "Fun"));
        SymbolNode symbolNode = programOp.getSymbolNode();
        //System.out.println(symbolNode.getParent().getParent().getParent().getParent());
        /*while (symbolNode.getParent() != null){
            System.out.println(symbolNode);
            symbolNode = (SymbolNode) symbolNode.getParent();
        }*/
        JScrollPane scrollPane = new JScrollPane(tree);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setSize(300, 150);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
// End of file
