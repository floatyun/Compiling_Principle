package daoyunmaihua;
import java.util.*;
import java.io.*;

public class SourceCode {
    private ArrayList<char[]> source;

    public Cursor getCursor() {
        return cursor;
    }

    private Cursor cursor;

    public class Cursor implements Cloneable{
        private int row, col; // 指示当前字符的行列
        private char[] currLine; // 当前字符所在行的字符串
        private char currChar; // 当前指示器的前一个字符

        @Override
        public Cursor clone() {
            Cursor cursor = null;
            try{
                cursor = (Cursor)super.clone();
            } catch (CloneNotSupportedException e) {
                System.out.println("Cursor在clone时发生错误。");
                e.printStackTrace();
            }
            return cursor;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        private void updateCurrChar() {
            currChar = (currLine == null) ? (char)-1 : currLine[col];
        }

        private Cursor(){
            reset();
        }

        /**
         * 重新设置cursor到初始位置
         */
        public void reset() {
            row = 0; col = 0;
            if (source.isEmpty())
                currLine = null;
            else
                currLine = source.get(0);
            updateCurrChar();
        }

        /**
         * @brief 判断指示器是否已经到源码结束位置。
         * 注意最后源码的最后一个字符不是结束位置，后一个位置才是结束位置。
         */
        public boolean isEnd() {
            return currLine == null;
        }

        /**
         * @brief 指示器移到下一个字符
         * @pre isEnd()==false
         */
        public void next() {
            ++col;
            if (col == currLine.length) {
                ++row; col = 0;
                if (row < source.size())
                    currLine = source.get(row);
                else
                    currLine = null;
            }
            updateCurrChar();
        }

        /**
         * @brief 指示器移到下一个字符
         * @pre isEnd()==false
         */
        public void prev() {
            --col;
            if (col < 0) {
                --row;
                if (row >= 0 && row < source.size()) {
                    currLine = source.get(row);
                    col = currLine.length-1;
                } else {
                    currLine = null;
                    col = 0;
                }
            }
            updateCurrChar();
        }

        /**
         * @brief 获取当前指示器所指示的字符
         */
        public char getChar()  {
            return currChar;
        }
    }

    public boolean isEnd() {return cursor.isEnd();}
    public void next() {cursor.next();}
    public void prev() {cursor.prev();}
    public char getChar() {return cursor.getChar();}


    /**
     * @brief 从文件读取代码
     * 每一行的代码都不是空，因为至少有一个字符。
     * @param file 源文件的文件对象
     * @throws IOException
     */
    public void getSourceCode(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        source = new ArrayList<char[]>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line += '\n';
            source.add(line.toCharArray());
        }
        cursor = new Cursor();
        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();
    }

    /**
     *
     * @param filepath 文件名
     */
    public void getSourceCode(String filepath) throws IOException {
        File file = new File(filepath);
        if (!file.exists()) {
            System.out.println(filepath+"文件不存在。");
            throw new IOException();
        } else if (!file.canRead()) {
            System.out.println(filepath+"文件不可读。");
            throw new IOException();
        }
        getSourceCode(file);
    }


    public void print() {
        for (char[] line : source)
            System.out.print(String.valueOf(line));
    }

    public static void main(String[] args) {
        SourceCode sourceCode = new SourceCode();
        try {
            sourceCode.getSourceCode("test.pas");
        } catch (IOException e)  {
            System.out.println("文件IO错误：" + e);
        }

        sourceCode.print();
    }
}
