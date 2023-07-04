package proto4;

import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.Message;
public class TestMessageFormatter extends AbstractMessageFactory {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //실제 출력할 메시지를 설정하는 메서드
    @Override
    public Message newMessage(String message) {
        // TODO Auto-generated method stub
        return new FormattedMessage("[prefixMessageFormatter] " + message + "[postfixMessageFormatter]");
    }

    @Override
    public Message newMessage(String message, Object... params) {
        // TODO Auto-generated method stub
        return new FormattedMessage("[prefixMessageFormatter] " + message + "[postfixMessageFormatter]", params);
    }
}