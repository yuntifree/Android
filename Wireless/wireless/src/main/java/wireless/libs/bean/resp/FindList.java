package wireless.libs.bean.resp;

import java.io.Serializable;
import java.util.List;

import wireless.libs.bean.vo.Service;

/**
 * Created by stephen on 2017/2/26.
 */

public class FindList implements Serializable {
    public List<FindBannerVo> banners;//广告
    public List<CityServiceVo> urbanservices;//城市服务
    public List<RecommendVo> recommends;//精品推荐
    public List<Service> services;//服务
    public class FindBannerVo implements Serializable {
        public int id;//report_click使用 type=10
        public String img;
        public String dst;
        public int type;//跳转： 0-网页 1-APP内部
    }

    public class CityServiceVo implements Serializable {
        public int id;
        public String img;
        public String title;
        public String dst;
    }

    public class RecommendVo implements Serializable {
        public int id;
        public String img;
        public String dst;
    }
}
