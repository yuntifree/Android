package wireless.libs.bean.vo;

import java.io.Serializable;

/**
 * Created by hzg on 16/12/6.
 */

public class User implements Serializable {
    public int uid;
    public String token;
    public String privdata;
    public String expiretime;
    public String headurl;
    public String nickname;
    public int pushtest;//是否需要订阅小米推送topic yuntitest

}
