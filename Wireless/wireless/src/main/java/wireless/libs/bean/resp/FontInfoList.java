package wireless.libs.bean.resp;

import java.io.Serializable;
import java.util.List;

import wireless.libs.bean.vo.BannerVo;
import wireless.libs.bean.vo.UserConnectVo;

/**
 * Created by stephon on 2016/12/7.
 */

public class FontInfoList implements Serializable {
    public UserConnectVo user;
    public List<BannerVo> banner;
}
