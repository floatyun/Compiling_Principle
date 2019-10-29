package daoyunmaihua;

public class LexicalAnalysisResult {
    public WordType getWordType() {
        return wordType;
    }

    public int getValue() {
        return value;
    }

    LexicalAnalysisResult(WordType wordType, int value) {
        this.wordType = wordType;
        this.value = value;
    }

    private WordType wordType;
    /**
     * 对于12345之类的常量是，直接使用计算机内部数12345
     * 对于标识符，value是其在idsName的下标
     * 对于算符、界符 value无定义（任意）
     */
    private int value;

    @Override
    public String toString() {

        switch (wordType) {
            case Id:
                return wordType.toString()+value;
            case Integer:
                return "$"+value;
            default:
                return wordType.toString();
        }
    }
}
