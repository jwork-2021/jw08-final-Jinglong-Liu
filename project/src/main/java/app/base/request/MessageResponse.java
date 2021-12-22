package app.base.request;

public class MessageResponse implements SendAble{
    private static final long serialVersionUID = 111116L;
    private String message;
    public MessageResponse(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    @Override
    public int getMask() {
        return 16;
    }
}
