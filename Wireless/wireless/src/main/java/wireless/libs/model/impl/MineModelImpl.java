package wireless.libs.model.impl;

import wireless.libs.bean.resp.ServerTip;
import wireless.libs.bean.vo.ImageTokenVo;
import wireless.libs.bean.vo.ImageUploadVo;
import wireless.libs.bean.vo.UserInfoVo;
import wireless.libs.model.IMineModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephen on 2017/2/24.
 */

public class MineModelImpl implements IMineModel {

    @Override
    public void applyImageUpload(int size, String format, final onImageUploadListener listener) {
        NetWorkWarpper.applyImageUpload(size, format, new HttpHandler<ImageUploadVo>() {
            @Override
            public void onSuccess(ServerTip serverTip, ImageUploadVo o) {
                listener.onImageUploadSuccess(o);
            }
        });
    }

    @Override
    public void getUserInfo(int tuid, final onGetUserInfoListener listener) {
        NetWorkWarpper.getUserInfo(tuid, new HttpHandler<UserInfoVo>() {
            @Override
            public void onSuccess(ServerTip serverTip, UserInfoVo o) {
                listener.onGetUserInfoSuccess(o);
            }
        });
    }
}
