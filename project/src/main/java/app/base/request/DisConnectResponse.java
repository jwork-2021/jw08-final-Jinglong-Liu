package app.base.request;

public class DisConnectResponse implements SendAble{
    private static final long serialVersionUID = 18L;
    public DisConnectResponse(){

    }
    @Override
    public int getMask() {
        return 0;
    }
}
