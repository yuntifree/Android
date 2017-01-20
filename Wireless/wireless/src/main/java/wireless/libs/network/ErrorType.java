package wireless.libs.network;

/**
 * 公共返回码
 *
 * @author jerry
 * @date 2016/03/21
 */
public class ErrorType {

    public static final int E_LOCAL = -1; //本地网络框架报告的异常
    public static final int E_NETWORK = -2;  //网络错误
    public static final int E_JSON_PARSE = -3; //json解析错误
    public static final int E_IO_EXCEPTION = -4; //json解析错误

    public static final int E_OK = 0; //正常
    public static final int E_MISS_PARAM = 1;  //缺少参数
    public static final int E_INVAL_PARAM = 2; //参数异常
    public static final int E_DATABASE = 3; //数据库错误
    public static final int E_INNER = 4; //服务器内部错误
    public static final int E_TOKEN = 101; // token验证失败
    public static final int E_NO_PERM = 102; // 无操作权限
    public static final int E_NOT_EXISTS = 103; // 资源不存在
    public static final int E_INSUFFICIENT_BALANCE = 104; //余额不足
    public static final int E_IMAGE_SIZE = 105; //图片大小异常（太小或太大>4M)
    public static final int E_LIMIT = 106; //超出限制
    public static final int E_NO_ACCOUNT = 107; //提现未绑定支付宝账号
    public static final int E_UNCHECKED = 108; //第三方账号登录验证失败
    public static final int E_NO_HEAD = 109; //没有头像
    public static final int E_PHONE_USED = 110; //注册已有的手机号
    public static final int E_PASSWD = 111; //登录密码错误
    public static final int E_PAY = 112;  //支付失败
    public static final int E_GOODS_ADD_LIMIT = 113; //超过商品限制
    public static final int E_DELETED_USER = 114; //用户已被拉
    public static final int E_NO_HEAD_AUDIT = 115; //头像审核中，且无老头像

    /**
     * 需要提示给用户
     * errno 112:表示应用版本为最新; -4:网络不可用（无法上网）
     * @param errno
     * @return
     */
    public static boolean isNeedTipToUser(int errno) {
        return errno != 0 && errno != E_IO_EXCEPTION && errno != 112;
    }
}
