// Circuit.java

import code_generation.CodeGenerationVisitor;
import nodes.*;
import scope.ScopeVisitor;
import scope.SymbolNode;
import scope.SymbolType;
import type_check.TypeCheckVisitor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

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
        JFrame frame = new JFrame("Manual Nodes");
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) p.parse().value;
        ScopeVisitor scopeVisitor = new ScopeVisitor();
        SymbolNode symbolNode2 = (SymbolNode) scopeVisitor.visit((ProgramOp) root);
        DefaultMutableTreeNode symbTable = (DefaultMutableTreeNode) scopeVisitor.visit((ProgramOp) root);
        TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor();
        ((ProgramOp) root).accept(typeCheckVisitor);
        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        String code = (String) codeGenerationVisitor.visit((ProgramOp) root);
        filePath = "temp" + File.separator + "test.c";
        FileWriter file = new FileWriter(filePath);
        file.write(code);
        file.close();
        JTree tree = new JTree(root);
        JScrollPane scrollPane = new JScrollPane(tree);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setSize(700, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
// End of file
