package proto4;

import java.util.List;

public class MyLoggerType {
    private String type;
    private List<Loggers> loggersList;

    public String getType(){return this.type;}
    public List<Loggers> getLoggersList(){return this.loggersList;}
    public void setType(String type){
        this.type=type;
    }
    public void setLoggersList(List<Loggers> loggersList){
        this.loggersList=loggersList;
    }
}
