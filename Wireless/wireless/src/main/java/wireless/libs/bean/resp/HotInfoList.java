package wireless.libs.bean.resp;

import java.io.Serializable;
import java.util.List;

import wireless.libs.bean.vo.HotInfo;

/**
 * Created by hzg on 16/12/6.
 */

public class HotInfoList implements Serializable {
    public List<HotInfo> infos;
    public int hasmore;
}
