package wireless.libs.model;

import wireless.libs.bean.vo.ImageTokenVo;
import wireless.libs.bean.vo.ImageUploadVo;
import wireless.libs.bean.vo.UserInfoVo;

/**
 * Created by stephen on 2017/2/24.
 */

public interface IMineModel {

    interface onImageUploadListener{
        void onImageUploadSuccess(ImageUploadVo imageUploadVo);
    }

    interface onGetUserInfoListener{
        void onGetUserInfoSuccess(UserInfoVo userInfoVo);
    }

    interface onUpdateUserInfoListener{
        void onUpdateUserInfoSuccess();
    }

    /***
     * 申请上传头像（图片）
     * @param listener
     */
    void applyImageUpload(int size, String format, onImageUploadListener listener);

    /***
     * 获取我（个人中心）信息
     * @param listener
     */
    void getUserInfo(int tuid, onGetUserInfoListener listener);

    /***
     * 修改用户信息
     * @param listener
     */
    void updateUserInfo(String headurl, String nickname, onUpdateUserInfoListener listener);
}
