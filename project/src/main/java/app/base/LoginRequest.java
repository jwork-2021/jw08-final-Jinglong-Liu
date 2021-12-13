package app.base;

public class LoginRequest implements SendAble{
    private static final long serialVersionUID = 12L;
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
