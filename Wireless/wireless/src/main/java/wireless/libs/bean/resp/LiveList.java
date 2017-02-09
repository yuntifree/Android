package wireless.libs.bean.resp;

import java.io.Serializable;
import java.util.List;

import wireless.libs.bean.vo.LiveVo;

/**
 * Created by stephen on 2017/2/8.
 */

public class LiveList implements Serializable {
    public int offset;
    public int more;
    public List<LiveVo> list;
}
