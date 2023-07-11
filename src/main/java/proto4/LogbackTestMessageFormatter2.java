package proto4;


public class LogbackTestMessageFormatter2 extends LogbackAbstractMessageFactory{
    private static final long serialVersionUID = 1L;
    //실제 출력할 메시지를 설정하는 메서드
    public String withNoArgs(String message) {
        // TODO Auto-generated method stub
        return super.withNoArgs("[prefix2] "+message+" [postfix2]");
    }

    public String withArgs(String message, Object... params) {
        // TODO Auto-generated method stub
        return super.withArgs("[prefix2] "+message+" [postfix2]",params);
    }
}
