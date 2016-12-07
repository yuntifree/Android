package wireless.libs.bean.vo;

import java.util.List;

/**
 * Created by hzg on 16/12/6.
 */

public class Service {
    public String title; //分类名称
    public String icon; //分类图标
    public List<ServiceItem> items;

    public class ServiceItem {
        public int sid; //服务id
        public String title; //标题
        public String dst; //跳转地址
    }
}
