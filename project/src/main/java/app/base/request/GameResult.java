package app.base.request;

import java.util.HashMap;

/**
 *
 */
public class GameResult implements SendAble{
    private static final long serialVersionUID = 111111L;
    HashMap<String,String>result;
    GameResult(HashMap result){
        this.result = result;
    }
    public String get(String id){
        return result.getOrDefault(id,"play");
    }
    public static GameResult winnerResult(String winner){
        HashMap result = new HashMap();
        result.put(winner,"win");
        return new GameResult(result);
    }
    public static GameResult loserResult(String loser){
        HashMap result = new HashMap();
        result.put(loser,"lose");
        return new GameResult(result);
    }
    @Override
    public int getMask() {
        return 0;
    }
}
