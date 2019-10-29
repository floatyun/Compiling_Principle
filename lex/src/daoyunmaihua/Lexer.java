package daoyunmaihua;

import java.io.IOException;
import java.util.*;

public class Lexer {
    /***
     * @param filepath
     * &#x4ece;filepath&#x4e2d;&#x8bfb;&#x53d6;sourceCode
     */
    Lexer(String filepath) throws IOException {
        loadBasicIds(); // 载入保留字
        sourceCode = new SourceCode();
        sourceCode.getSourceCode(filepath);
        cursor = sourceCode.getCursor();
    }

    Lexer(SourceCode sourceCode) {
        loadBasicIds();
        this.sourceCode = sourceCode;
        this.cursor = this.sourceCode.getCursor();
    }

    public ArrayList<Object> analysisAll() {
        ArrayList<Object> resultsList = new ArrayList<>();
        while (true) {
            try {
                resultsList.add(analysisOne());
            } catch (ReachEndException e) {
                System.out.println("分析已经到文件末尾");
                break;
            } catch (LexException e) {
                System.out.println(e);
                resultsList.add(e);
            }
        }
        return resultsList;
    }

    /**
     * 仅仅从当前位置出发，读取一个word（标识符、算符、界符都算一个单词）
     *
     * @return 读取的word的信息，如果是新的标识符
     * @throws LexException
     */
    public LexicalAnalysisResult analysisOne() throws LexException {
        LexicalAnalysisResult result;
        char c = 0;
        // 不断读取，直到遇到第一个非空白符
        while (!cursor.isEnd()) {
            c = cursor.getChar();
            if (Character.isWhitespace(c))
                cursor.next();
            else
                break;
        }
        if (cursor.isEnd())
            throw new ReachEndException(cursor.clone());
        StringBuilder word = new StringBuilder();
        String wordStr;
        word.append(c); // 将第一个非空白字符附加到word中
        cursor.next(); // 让cursor指向首字符的后一个字符
        // 查看第一个非空白字符的情况
        if (Character.isLetter(c)) { // 标识符（包含保留字和常量标识符、变量标识符）首字符
            // 首字符是空白符，提取标识符
            // readId
            while (!cursor.isEnd()) {
                c = cursor.getChar();
                if (Character.isLetterOrDigit(c))
                    word.append(c);
                else
                    break;
                cursor.next();
            }
            // 此时，已经获取了整个标识符word
            wordStr = word.toString();
            result = idNamesMap.get(wordStr);
            if (null != result)
                return result;
            // 新的标识符，idNames和idNamesMap增加成员了
            result = new LexicalAnalysisResult(
                    WordType.Id,
                    idNames.size()
            );
            idNames.add(wordStr);
            idNamesMap.put(wordStr,result);
        } else if (Character.isDigit(c)) { // Integer首字符
            // readInteger
            while (!cursor.isEnd()) {
                c = cursor.getChar();
                if (Character.isDigit(c))
                    word.append(c);
                else
                    break;
                cursor.next();
            }
            // 此时已经提取了整个整数字符串word
            if (!cursor.isEnd() && Character.isLetter(c))
                throw new IlegalIntegerOrIdLexException(cursor.clone());
            wordStr = word.toString();
            result = new LexicalAnalysisResult(
                    WordType.Integer,
                    Integer.parseInt(wordStr)
            );
        } else if (c == '(') {
            return new LexicalAnalysisResult(WordType.LPar, -1);
        } else if (c == ')') {
            return new LexicalAnalysisResult(WordType.RPar, -1);
        } else if (c == ';') {
            return new LexicalAnalysisResult(WordType.Semicolon, -1);
        } else if (c == ',') {
            return new LexicalAnalysisResult(WordType.Comma, -1);
        } else if (c == '+') {
            return new LexicalAnalysisResult(WordType.Plus, -1);
        } else if (c == '-') {
            return new LexicalAnalysisResult(WordType.Minus, -1);
        } else if (c == '*') {
            return new LexicalAnalysisResult(WordType.Mlt, -1);
        } else if (c == '/') {
            return new LexicalAnalysisResult(WordType.Div, -1);
        } else if (c == '=') { // Equal
            return new LexicalAnalysisResult(WordType.Equal, -1);
        } else if (c == ':') { // 赋值运算符Assign首字符
            if (cursor.isEnd()) {
                SourceCode.Cursor errorCursor = cursor.clone();
                errorCursor.prev(); // 指向“:”
                throw new AssignLexException(errorCursor,"冒号':'无字符，源文件已经结束。");
            }
            c = cursor.getChar();
            if (c != '=') {
                SourceCode.Cursor errorCursor = cursor.clone();
                errorCursor.prev(); // 指向“:”
                throw new AssignLexException(errorCursor,"冒号'：'后面是"+"'"+c+"'");
            }
            cursor.next(); // 使cursor指向赋值号":="的后一个字符
            result = new LexicalAnalysisResult(
                    WordType.Assign,
                    -1
            );
        } else if (c == '<') { // Less LessOrEqual NotEqual首字符
            // 现在cursor指向"<"后一个字符
            if (cursor.isEnd()) {
                result = new LexicalAnalysisResult(
                        WordType.Less,
                        -1
                );
            } else {
                c = cursor.getChar();
                if (c == '=') {
                    cursor.next(); // 使得cursor指向小于等于号“<=”后一个字符
                    result = new LexicalAnalysisResult(
                            WordType.LessOrEqual,
                            -1
                    );
                } else if (c == '>') {
                    cursor.next(); // 使得cursor指向不等号“<>”后一个字符
                    result = new LexicalAnalysisResult(
                            WordType.NotEqual,
                            -1
                    );
                } else {
                    // cursor依旧在"<"后一个字符
                    result = new LexicalAnalysisResult(
                            WordType.Less,
                            -1
                    );
                }
            }
            // 始终维持cursor在读出的word最后一个字符的后一个位置
        } else if (c == '>') { // Greater GreaterOrEqual首字符
            // 现在cursor指向">"后一个字符
            if (cursor.isEnd()) {
                result = new LexicalAnalysisResult(
                        WordType.Greater,
                        -1
                );
            } else {
                c = cursor.getChar();
                if (c == '=') {
                    cursor.next(); // 使得cursor指向小于等于号“>=”后一个字符
                    result = new LexicalAnalysisResult(
                            WordType.GreaterOrEqual,
                            -1
                    );
                } else {
                    // cursor依旧在">"后一个字符
                    result = new LexicalAnalysisResult(
                            WordType.Greater,
                            -1
                    );
                }
            }
            // 始终维持cursor在读出的word最后一个字符的后一个位置
        } else {
            SourceCode.Cursor errorCursor = cursor.clone();
            errorCursor.prev(); // 指针回退1步指向非法字符
            throw new IlegalCharacterLexException(errorCursor,c);
        }
        return result;
    }

    /**
     * @brief 事先将保留字加到idsName并依次设置idNameToIndex
     */
    private void loadBasicIds() {
        idNamesMap = new HashMap<>();
        idNames = new ArrayList<>();
        String []basicIdsNames = {
                "program",
                "const",
                "var",
                "procedure",
                "begin",
                "end",
                "if",
                "then",
                "else",
                "while",
                "do",
                "call",
                "read",
                "write",
                "odd"
        };
        WordType []basicIdsWordTypes = {
                WordType.Program,
                WordType.ConstDeclare,
                WordType.VarDeclare,
                WordType.Procedure,
                WordType.Begin,
                WordType.End,
                WordType.If,
                WordType.Then,
                WordType.Else,
                WordType.While,
                WordType.Do,
                WordType.Call,
                WordType.Read,
                WordType.Write,
                WordType.Odd
        };
        assert(basicIdsNames.length == basicIdsWordTypes.length);
        basicIdsCount = basicIdsNames.length;
        for (int i = 0; i < basicIdsCount; ++i) {
            idNames.add(basicIdsNames[i]);
            idNamesMap.put(basicIdsNames[i],
                    new LexicalAnalysisResult(
                            basicIdsWordTypes[i], i
                    ));
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入源程序文件名");
        String filepath = sc.nextLine();
        Lexer lexer;
        try {
            lexer = new Lexer(filepath);
        } catch (IOException e) {
            System.out.println("文件IO错误");
            return;
        }
        lexer.sourceCode.print();
        ArrayList<Object> results = lexer.analysisAll();
        for (Object result : results) {
            System.out.println(result);
        }
    }

    private SourceCode sourceCode;
    private SourceCode.Cursor cursor;

    private HashMap<String, LexicalAnalysisResult> idNamesMap;
    private ArrayList<String> idNames;

    public int getBasicIdsCount() {
        return basicIdsCount;
    }

    private int basicIdsCount;
}