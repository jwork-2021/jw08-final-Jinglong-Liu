package app.base.request;

public class SimpleRequest implements SendAble{
    private static final long serialVersionUID = 10L;
    private String request;
    public SimpleRequest(String request){
        this.request = request;
    }
    @Override
    public int getMask() {
        return 0;
    }

    public String getRequest() {
        return request;
    }
}
