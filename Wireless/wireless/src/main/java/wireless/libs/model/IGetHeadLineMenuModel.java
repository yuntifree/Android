package wireless.libs.model;

import wireless.libs.bean.resp.MenuList;

/**
 * Created by stephen on 2016/12/30.
 */

public interface IGetHeadLineMenuModel {

    interface onGetHeadLineMenuListener{
        void onGetHeadLineMenuSuccess(MenuList menuList);
    }

    /***
     * 获取头条顶部菜单分类
     * @param listener
     */
    void getMenu(onGetHeadLineMenuListener listener);
}
