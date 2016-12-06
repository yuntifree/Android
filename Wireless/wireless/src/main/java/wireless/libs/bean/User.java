package wireless.libs.bean;

import java.io.Serializable;

/**
 * Created by hzg on 16/12/6.
 */

public class User implements Serializable {
    public int uid;
    public String token;
    public String privdata;
    public int expire;
}
