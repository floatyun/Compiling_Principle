package daoyunmaihua;
import java.util.*;

public enum WordType {
    // 程序的保留字
    Program         (1),
    ConstDeclare    (2),
    VarDeclare      (3),
    Procedure       (4),
    Begin           (5),
    End             (6),
    If              (7),
    Then            (8),
    Else            (9),
    While           (10),
    Do              (11),
    Call            (12),
    Read            (13),
    Write           (14),
    // 程序的各种算符

    // 赋值号
    Assign          (15),
    // 加减乘除
    Plus            (16),
    Minus           (17),
    Mlt             (18),
    Div             (19),
    // 六种比较运算符
    Greater         (20),
    Less            (21),
    Equal           (22),
    NotEqual        (23),
    GreaterOrEqual  (24),
    LessOrEqual     (25),
//    // 正负号
//    PlusSign        (26), // 将正号+和二元运算符+视作不同的WordType
//    MinusSign       (27),

    // 界符及括号之类的
    LPar            (28),
    RPar            (29),
    Comma           (30),
    Semicolon       (31),

    // 表示类型是常量、标识符
//    Const           (32),
//    VarId           (33);
    Id              (34),
    Integer         (35),
    Odd             (36);

    private int code;

    public int getCode() {
        return code;
    }

    WordType(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return '$'+super.toString();
    }

    public static WordType getWordType(String s) {
        for (WordType i : WordType.values())
            if (s == i.toString())
                return i;
        throw new IllegalArgumentException("没有"+s+"的WordType");
    }
}
