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
        //String separator = File.separator;
        if(args.length != 1) {
            System.out.println("Controllare gli argomenti (deve essere /path/to/file.txt)");
            return ;
        }
        /*if(separator.equals("/")){
            filePath = "test_files/" + args[0];
        } else {
            filePath = "test_files" + "\\" + args[0];
        }*/
        String filePath = args[0];
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
        int lastSeparatorIndex = args[0].lastIndexOf(File.separator);
        int dotIndex = args[0].lastIndexOf(".");
        String fileName = filePath.substring(lastSeparatorIndex + 1, dotIndex);
        System.out.println(fileName);
        File f = new File("test_files" + File.separator + "c_out");
        if(!f.exists()){
            if(!f.mkdirs()){
                System.out.println("Errore nella generazione delle cartelle");
                return;
            }
        }
        filePath = "test_files" + File.separator + "c_out" + File.separator + fileName + ".c";
        FileWriter file = new FileWriter(filePath);
        file.write(code);
        file.close();
        /*JTree tree = new JTree(root);
        JScrollPane scrollPane = new JScrollPane(tree);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setSize(700, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);*/
    }
}
// End of file
