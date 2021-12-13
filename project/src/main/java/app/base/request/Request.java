package app.base.request;

import app.base.Thing;

public class Request implements SendAble{

    public Request(){

    }
    @Override
    public int getMask() {
        return 0;
    }
    Thing thing = null;
}
