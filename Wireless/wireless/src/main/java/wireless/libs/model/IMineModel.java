package wireless.libs.model;

import wireless.libs.bean.vo.ImageTokenVo;
import wireless.libs.bean.vo.ImageUploadVo;

/**
 * Created by stephen on 2017/2/24.
 */

public interface IMineModel {

    interface onImageUploadListener{
        void onImageUploadSuccess(ImageUploadVo imageUploadVo);
    }

    /***
     * 申请上传头像（图片）
     * @param listener
     */
    void applyImageUpload(int size, String format, onImageUploadListener listener);
}
