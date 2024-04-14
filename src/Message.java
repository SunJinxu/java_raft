import java.util.Arrays;

/**
 * 1. index序列号
 * 2. term任期号
 * 3. byte message[] 消息
 * 4. type 消息类型
 */
public class Message {
    public int index;
    public int term;
    public byte[] content;
    public int type;

    public Message(){};

    public Message(byte[] bytes) {

    }

    @Override
    public String toString() {
        return
                "index=" + index +
                ", term=" + term +
                ", content=" + Arrays.toString(content) +
                ", type=" + type;
    }
}