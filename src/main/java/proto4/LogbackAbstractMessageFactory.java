package proto4;


public class LogbackAbstractMessageFactory {


     public String withArg(final String message, final Object p0){
         String msg=message+p0;
         return msg;
     }

    public String withNoArgs(final String message){
        String msg=message;
        return msg;
    }


     public String withArgs(String message, Object... params){
         String msg=message+params;
         return msg;
     }
}
