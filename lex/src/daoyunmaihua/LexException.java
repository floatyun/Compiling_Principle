package daoyunmaihua;

public abstract class LexException extends Exception {
    private int row, col;

    public LexException() {
        super();
    }

    @Override
    public String toString() {
        return String.format("(%d行,%d列)\t",row+1,col+1)+super.getMessage();
    }

    public LexException(String msg, int row, int col) {
        super(msg);
        this.row = row;
        this.col = col;
    }

    public LexException(String msg, SourceCode.Cursor cursor) {
        super(msg);
        this.row = cursor.getRow();
        this.col = cursor.getCol();
    }
}
class ReachEndException extends LexException {
    public ReachEndException(SourceCode.Cursor cursor) {
        super("文件末尾，没有单词了", cursor);
    }
}

class AssignLexException extends LexException {

    public AssignLexException(SourceCode.Cursor cursor, String detail) {
        super("赋值号错误：':'后面必须且仅可以跟随'='构成赋值运算符。但是" + detail,cursor);
    }
}

class IlegalCharacterLexException extends LexException {
    public IlegalCharacterLexException(SourceCode.Cursor cursor, char c) {
        super("含有非法字符错误：代码中含有PL/0语言未定义的字符'"+c+"'",cursor);
    }
}

class IlegalIntegerOrIdLexException extends LexException {

    public IlegalIntegerOrIdLexException(SourceCode.Cursor cursor) {
        super("非法整数或者非法标识符：整数后面跟着字母。如果想表示一个整数，后面不可跟着字母；如果想表示一个标识符，则不可以用数字作为开头。",cursor);
    }
}