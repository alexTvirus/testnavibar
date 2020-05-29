package cc.xpcas.nettysocks.upstream;

import cc.xpcas.nettysocks.config.Address;
import io.netty.channel.Channel;

/**
 * @author xp
 */
public abstract class Upstream<T extends Channel> {

    public static final String HANDLER_NAME = "proxy";

    public Address address;

    public String user = "";

    public String pass = "";
    
    public String TypeProtocol = "";

    public String getTypeProtocol() {
        return TypeProtocol;
    }

    public void setTypeProtocol(String TypeProtocol) {
        this.TypeProtocol = TypeProtocol;
    }

    
    
    protected Upstream setAddress(Address address
    ) {
        this.address = address;
        return this;
    }

    public Upstream setUser(String user
    ) {
        this.user = user;
        return this;
    }
    
    public Upstream setPass(String pass
    ) {
        this.pass = pass;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
    
  

    public abstract void initChannel(T channel);
}
