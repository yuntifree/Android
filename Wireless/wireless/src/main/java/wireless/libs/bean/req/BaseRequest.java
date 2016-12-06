package wireless.libs.bean.req;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.yunxingzh.wireless.config.AppConfig;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.NetUtils;

import java.io.Serializable;
import wireless.libs.bean.vo.UserInfoVo;

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

        UserInfoVo user = MainApplication.getInstance().getUser();

        if (user != null) {
            uid = user.getData().getUid();
            token = user.getData().getToken();
        }
        version = AppConfig.VERSION;
        ts = System.currentTimeMillis();

        //added by andy 后续优化最好只取一次保存
//        udid = DeviceUtils.getDeviceInfo(MainApplication.get());
//        //暂时未作无网络判断,因为无网络,请求无法到达
//        try {
//            ApplicationInfo appinfo = MainApplication.get().getPackageManager().getApplicationInfo(MainApplication.get().getPackageName(), PackageManager.GET_META_DATA);
//            channel = appinfo.metaData.get("UMENG_CHANNEL") + "";
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        model = Build.MODEL;
        nettype = AppUtils.getNetWorkType(MainApplication.getInstance());
    }
}
