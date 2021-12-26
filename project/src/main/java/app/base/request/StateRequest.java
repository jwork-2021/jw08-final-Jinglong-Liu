package app.base.request;

public class StateRequest implements Request{
    private static final long serialVersionUID = 999L;
    @Override
    public int getMask() {
        return Request_State;
    }
}
