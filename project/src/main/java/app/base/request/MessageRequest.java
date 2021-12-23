package app.base.request;

public class MessageRequest implements SendAble{
    private static final long serialVersionUID = 1002L;
    private String message;
    public MessageRequest(String messageToSend){
        this.message = messageToSend;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int getMask() {
        return 0;
    }
}
