package app.base.request;

public class StateRequest implements SendAble{
    private static final long serialVersionUID = 14L;
    public StateRequest(){

    }
    @Override
    public int getMask() {
        return 0;
    }
}
