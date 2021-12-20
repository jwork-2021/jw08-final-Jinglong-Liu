package app.base.request;

/**
 * ALREADY:
 * LIMIT:online number == limit number.
 */

public class LoginFailResponse implements SendAble{
    private static final long serialVersionUID = 111114L;
    private String id;
    private String errorType;
    public LoginFailResponse(String id,String type){
        this.id = id;
        this.errorType = type;
    }

    public String getId() {
        return id;
    }

    public String type(){
        return errorType;
    }
    @Override
    public int getMask() {
        return 0;
    }
}
