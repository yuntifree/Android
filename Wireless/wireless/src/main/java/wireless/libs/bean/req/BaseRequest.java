package wireless.libs.bean.req;

import com.yunxingzh.wireless.config.AppConfig;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.utils.AppUtils;

import java.io.Serializable;

import wireless.libs.bean.vo.User;

/**
 * 请求封装实体类
 *
 * @author jerry
 * @date 2016/03/21
 * modify by andy
 * 描述: 添加三个字段udid nettype model
 */
public class BaseRequest implements Serializable {

    public long uid;
    public String token;
    public final int term = 0;
    public int version;
    public long ts;
    public String udid;   //设备ID
    public int nettype;  //网络类型
    public Object data;

    public BaseRequest() {

        User user = MainApplication.getInstance().getUser();

        if (user != null) {
            uid = user.uid;
            token = user.token;
        }
        version = AppConfig.VERSION;
        ts = System.currentTimeMillis();
        nettype = AppUtils.getNetWorkType(MainApplication.getInstance());
    }
}
