package wireless.libs.api;

/**
 * Created by stephon on 2016/5/27.
 */
public class HttpCode {
    //网络请求状态码
    public static final int HTTP_OK = 0;    //请求成功
    public static final int E_MISS_PARAM = 1;  //缺少参数
    public static final int E_INVAL_PARAM = 2; //参数异常
    public static final int E_DATABASE = 3; //数据库错误
    public static final int E_INNER = 4; //服务器内部错误
    public static final int E_SERVER = 100; //服务器错误
    public static final int E_TOKEN = 101; // token验证失败
    public static final int E_CODE = 102; //短信验证码错误
    public static final int E_GET_CODE = 103; //获取短信验证码失败
    public static final int E_USED_PHONE = 104; //手机号重复注册
}
