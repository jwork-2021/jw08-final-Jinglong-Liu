package app.base.request;

public class LoginResponse implements SendAble{
    private static final long serialVersionUID = -1001L;
    private int type;
    private String id;
    public LoginResponse(String id,int type){
        this.id = id;
        this.type = type;
    }
    @Override
    public int getMask() {
        return type;
    }

    public String getId() {
        return id;
    }
}
