package app.base.request;

public class AlreadyLoginResponse implements SendAble{
    private static final long serialVersionUID = 111113L;
    private String id;
    public AlreadyLoginResponse(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int getMask() {
        return 123;
    }
}
