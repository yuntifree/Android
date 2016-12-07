package wireless.libs.model;

import wireless.libs.bean.vo.User;

/**
 * Created by Stephen on 2016/9/9.
 */
public interface IUserModel {

    interface onValidateCodeListener{
        void onValidateCodeSuccess();
    }

    interface onRegisterListener{
        void onRegisterSuccess(User userVo);
    }

    /****
     * 获取短信验证码
     * @param listener
     */
    void getValidateCode(int type,String phone, onValidateCodeListener listener);

    /***
     * 注册
     * @param
     * @param
     */
    void register(String username,String code,onRegisterListener listener);
}
