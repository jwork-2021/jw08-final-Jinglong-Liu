package app.base.request;

public class LoginRequest implements SendAble{
    private static final long serialVersionUID = 1001L;
    private String id;

    public String getId() {
        return id;
    }
    public LoginRequest(String id){
        this.id = id;
    }
    @Override
    public int getMask() {
        return 1;
    }
}
