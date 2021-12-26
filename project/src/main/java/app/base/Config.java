package app.base;

import java.io.Serializable;

public class Config implements Serializable {
    private static final long serialVersionUID = 10088L;
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    public Config(String host,int port){
        this.host = host;
        this.port = port;
    }
    public Config(){}
}
