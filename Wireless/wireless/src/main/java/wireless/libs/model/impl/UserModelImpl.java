package wireless.libs.model.impl;

import wireless.libs.bean.vo.User;
import wireless.libs.bean.resp.ServerTip;
import wireless.libs.model.IUserModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by Stephen on 2016/9/9.
 */
public class UserModelImpl implements IUserModel {

    @Override
    public void getValidateCode(int type, String phone, final onValidateCodeListener listener) {
        NetWorkWarpper.validateCode(type,phone,new HttpHandler<Object>() {
            @Override
            public void onSuccess(ServerTip serverTip, Object o) {
                listener.onValidateCodeSuccess();
            }
        });
    }

    @Override
    public void register(String username,String code, final onRegisterListener listener) {
        NetWorkWarpper.register(username,code,new HttpHandler<User>() {
            @Override
            public void onSuccess(ServerTip serverTip, User userVo) {
                listener.onRegisterSuccess(userVo);
            }
        });
    }
}
