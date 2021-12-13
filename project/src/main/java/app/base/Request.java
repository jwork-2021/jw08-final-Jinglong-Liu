package app.base;

public class Request implements SendAble{

    public Request(){

    }
    @Override
    public int getMask() {
        return 0;
    }
    Thing thing = null;
}
