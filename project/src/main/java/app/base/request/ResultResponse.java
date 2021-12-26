package app.base.request;

import java.util.HashMap;

/**
 *
 */
public class ResultResponse implements Response{
    private static final long serialVersionUID = 111111L;
    private String id;
    private int type;
    public ResultResponse(String id,int type){
        this.id = id;
        this.type = type;
    }
    public int getType(){
        return type;
    }
    public static final int Result_PLAY = 0;
    public static final int Result_LOSE = 1;
    public static final int Result_WIN = 2;
    @Override
    public int getMask() {
        return Response.Response_Result;
    }
}
