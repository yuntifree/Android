package wireless.libs.bean.resp;

import java.io.Serializable;
import java.util.List;

import wireless.libs.bean.vo.MainNewsVo;
import wireless.libs.bean.vo.WeatherVo;

/**
 * Created by stephon on 2016/12/7.
 */

public class WeatherNewsList implements Serializable{
    public WeatherVo weather;
    public List<MainNewsVo> news;
}
