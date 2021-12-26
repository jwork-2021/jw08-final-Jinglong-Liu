package app.base.request;

public class LoginResponse implements Response{
    private static final long serialVersionUID = -1001L;
    private int type;
    private String id;
    public LoginResponse(String id,int type){
        this.id = id;
        this.type = type;
    }
    @Override
    public int getMask() {
        return Response.Response_Login;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static final int LOGIN_SUCCEED = 0;
    public static final int LOGIN_LIMIT = 1;
    public static final int LOGIN_ALREADY = 2;
    public static final int LOGIN_LOSE = 3;
}
