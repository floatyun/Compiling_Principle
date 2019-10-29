package daoyunmaihua;

import java.io.IOException;
import java.util.*;

public class LL1GrammarAnalysis {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入源程序文件名");
        String filepath = sc.nextLine();
        SourceCode sourceCode = new SourceCode();
        Lexer lexer;
        try {
            sourceCode.getSourceCode(filepath);
            lexer = new Lexer(sourceCode);
        } catch (IOException e) {
            System.out.println("文件IO错误");
            return;
        }
        sourceCode.print();
        ArrayList<Object> results = lexer.analysisAll();
        // 从文件读入语法规则
        // 去除左递归
        // 提取左因子
        // 求first集和follow集
        // 构造LL 1 预测分析表
        // 自顶向下分析
    }
}
