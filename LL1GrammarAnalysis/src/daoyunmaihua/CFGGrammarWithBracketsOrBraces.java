package daoyunmaihua;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * CFGGrammarWithBracketsOrBraces中的上下文无关文法的产生式的右部仅仅式字符串形式
 * 并且右部含有[]{}运算符
 */
public class CFGGrammarWithBracketsOrBraces {
    /**
     * 读入与Products.text格式相同的文件。变元和终结符都用$id表示，id是以字母开头，后面跟任意数量的字母或者数字。
     * 其中大写字母开头的表示是终结符，小写字母开头的表示是变元。变元后面加上冒号表示它是上下文无关文法的左部。
     * $Empty表示的空串。
     * 紧跟着的若干行则描述该变元的所有候选项。每行是一个候选项。最后是一个空行表示该变元所有候选的结束。
     * 候选项中可能含有[xxx]表示xxx可以出现0次或者1次。
     * 也可能含有{xxx}表示xxx可以出现任意次（包括0次）。
     * 额，暂时不考虑带上下标的{}运算符。
     * @param filepath 读入的文件路径
     * @throws FileNotFoundException filepath指示的文件不存在
     */
    public void readProducts(String filepath) throws FileNotFoundException {
        variables = new ArrayList<>();
        products = new HashMap<>();
        File file = new File(filepath);
        Scanner sc = new Scanner(file);
        String line;
        while (sc.hasNext()) {
            line = sc.nextLine().trim();
            if (line.endsWith(":")) {
                String variable;
                variable = line.substring(0,line.length()-1);
                ArrayList<String> candidates = new ArrayList<>();
                while (sc.hasNext()) {
                    line = sc.nextLine().trim();
                    if (line.isEmpty()) break;
                    candidates.add(line);
                }
                variables.add(variable);
                products.put(variable,candidates);
            }
        }
    }

    /**
     * 将[]{}去掉。
     * 前提：产生式是合法的产生式。
     */
    public void simlify() {
        int vSize = variables.size();
        for (int vi = 0;vi < vSize; ++vi) { // 不要改成foreach形式，因为generateNewVariable将会执行add操作。
            String v = variables.get(vi);
            ArrayList<String> candidates = products.get(v);
            int sz = candidates.size();
            for (int pos = 0; pos < sz; ++pos) {
                String candidate = candidates.get(pos);
                LinkedList<Character> stack = new LinkedList<>();
                int len = candidate.length();
                boolean flag = false; // 如果候选项中存在[]或者{}flag将变成true

                /**
                 * 去除[]{}
                 */
                for (int i = len-1;i >= 0; --i) {
                    char c = candidate.charAt(i);
                    if (c != '[' && c != '{') {
                        stack.addLast(c);
                    } else if (c == '[') {
                        flag = true;
                        String newVar = generateNewVariable(); // 新的变元名称。
                        StringBuilder inBracket;
                        inBracket = new StringBuilder();
                        char t;
                        while (true) {
                            try {
                                t = stack.removeLast();
                                if (t == ']') break;
                                inBracket.append(t);
                            } catch (NoSuchElementException e) {
                                throw new IllegalArgumentException("输入的产生式非法"+v+"->"+candidate);
                            }
                        }
                        pushStrIntoStack(newVar,stack);
                        ArrayList<String> newCandidates = products.get(newVar);
                        //if (!newCandidates.contains("$Empty"))
                        newCandidates.add("$Empty");
                        String tmp = inBracket.toString();
                        //if (!newCandidates.contains(tmp))
                        newCandidates.add(tmp);
                    } else { // c == '{'
                        flag = true;
                        String newVar = generateNewVariable(); // 新的变元名称。
                        StringBuilder inBrace;
                        inBrace = new StringBuilder();
                        char t;
                        while (true) {
                            try {
                                t = stack.removeLast();
                                if (t == '}') break;
                                inBrace.append(t);
                            } catch (NoSuchElementException e) {
                                throw new IllegalArgumentException("输入的产生式非法"+v+"->"+candidate);
                            }
                        }
                        pushStrIntoStack(newVar,stack);
                        ArrayList<String> newCandidates = products.get(newVar);
                        //if (!newCandidates.contains("$Empty"))
                            newCandidates.add("$Empty");
                        String tmp = inBrace.toString()+newVar;
                        //if (!newCandidates.contains(tmp))
                        newCandidates.add(tmp);
                    }
                }
                if (!flag) continue;
                StringBuilder result = new StringBuilder();
                while (!stack.isEmpty())
                    result.append(stack.removeLast());
                candidates.set(pos,result.toString());
            }
        }
    }

    private void pushStrIntoStack(String str, LinkedList<Character> stack) {
        int l = str.length();
        for (int i = l-1; i >= 0; --i)
            stack.addLast(str.charAt(i));
    }

    public String generateNewVariable() {
        int count = variables.size();
        String variable = "$var"+count;
        ArrayList<String> tmp = new ArrayList<String>();
        variables.add(variable);
        products.put(variable,tmp);
        return variable;
    }

    public ArrayList<String> getVariables() {
        return variables;
    }

    public HashMap<String, ArrayList<String>> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        char br = '\n';
        for (String v : variables) {
            s.append(v); s.append(":"); s.append(br);
            List<String> candiates = products.get(v);
            for (String c : candiates) {
                s.append(c); s.append(br);
            }
            s.append(br);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        CFGGrammarWithBracketsOrBraces obj = new CFGGrammarWithBracketsOrBraces();
        try {
            obj.readProducts("Products.text");
            String filePath = "Products_2.text";
            File destFile = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(destFile);
            //System.out.println(obj);
            obj.simlify();
            fileOutputStream.write(obj.toString().getBytes());
            fileOutputStream.close();
            System.out.println(obj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> variables;
    private HashMap<String,ArrayList<String>> products;
}
