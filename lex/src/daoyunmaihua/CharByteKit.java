package daoyunmaihua;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/*
* Authors: areful, daoyunmaihua
* Date: 2019-10-23
 */
public class CharByteKit {
    public static byte[] getBytes(char[] chars, int len) {
        Charset cs = Charset.forName("UTF-8");
        len = Math.min(len,chars.length);
        CharBuffer cb = CharBuffer.allocate(len);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    public static char[] getChars(byte[] bytes, int len) {
        Charset cs = Charset.forName("UTF-8");
        len = Math.min(len,bytes.length);
        ByteBuffer bb = ByteBuffer.allocate(len);
        bb.put(bytes).flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    public static char byteToChar(byte[] b) {
        int hi = (b[0] & 0xFF) << 8;
        int lo = b[1] & 0xFF;
        return (char) (hi | lo);
    }

    public static void main(String[] args) {
        // char[] <===> byte[]
        char[] c = getChars(new byte[]{65, 2, 3}, 100); // 100不小于传入的byte数组的大小
        System.out.println(Arrays.toString(c));
        byte[] b = getBytes(c, c.length);
        System.out.println(Arrays.toString(b));

        // char <===> byte[]
        byte[] b2 = charToByte('A');
        System.out.println(Arrays.toString(b2));
        char c2 = byteToChar(b2);
        System.out.println(c2);
    }
}