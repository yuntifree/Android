package wireless.libs.bean.vo;

import java.util.List;

/**
 * Created by hzg on 16/12/6.
 */

public class HotInfo {

    public int id;//新闻/视频id
    public int seq;//分页用
    public String title; //标题
    public List<String> images; //图片url
    public String source; //来源
    public String ctime; //时间
    public String dst; //源地址
    public int stype; // 1-广告
    public int play; //播放次数
    public boolean isClickColor;

}
