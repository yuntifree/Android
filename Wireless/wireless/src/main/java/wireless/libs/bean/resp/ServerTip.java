package wireless.libs.bean.resp;

/**
 * 网络failure统一封装
 *
 * @author jerry
 * @date 2016/03/21
 */
public class ServerTip {
    public int errno;
    public String desc;

    public ServerTip() {

    }

    public ServerTip(int errno, String desc) {
        this.errno = errno;
        this.desc = desc;
    }

    public int errno() {
        return errno;
    }

    public String desc() {
        return desc;
    }

    public void errno(int errno) {
        this.errno = errno;
    }

    public void desc(String desc) {
        this.desc = desc;
    }

}
