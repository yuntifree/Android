package com.yunxingzh.wireless.mvp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Glide;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.config.MineHeadImg;
import com.yunxingzh.wireless.mview.alertdialog.AlertView;
import com.yunxingzh.wireless.mview.alertdialog.OnItemClickListener;
import com.yunxingzh.wireless.mvp.presenter.IMinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.MinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.DefaultHeadImgActivity;
import com.yunxingzh.wireless.mvp.ui.activity.FeedBackActivity;
import com.yunxingzh.wireless.mvp.ui.activity.NickNameActivity;
import com.yunxingzh.wireless.mvp.ui.activity.SetActivity;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IMineView;
import com.yunxingzh.wireless.utils.BitmapUtils;
import com.yunxingzh.wireless.utils.FileUtil;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.hdodenhof.circleimageview.CircleImageView;
import wireless.libs.bean.vo.ImageTokenVo;
import wireless.libs.bean.vo.ImageUploadVo;
import wireless.libs.bean.vo.UserInfoVo;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephen on 2017/2/22.
 * 我
 */

public class MineFragment extends BaseFragment implements IMineView, View.OnClickListener, OnItemClickListener {

    private static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    private static final String downLoadUrl = "http://img.yunxingzh.com";

    private static final int SELECT_IMG = 0;//相册
    private static final int CUT_IMG = 1;//裁剪
    private static final int HEAD_IMG_URL = 10;//选择默认头像requestCode

    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv, mMineNameTv, mMineCountTv, mMineMoneyTv;
    private CircleImageView mMineHeadIv;
    private IMinePresenter iMinePresenter;
    private LinearLayout mMineFeedBackLay, mMineSetLay;

    private String filePath;//相册选择的图片路径
    private ImageTokenVo imageTokenVo;
    private OSS oss;
    private ImageUploadVo uploadVo;
    private int headImgFrom = 0;//=1表示从相册选择上传
    private String mHeadUrl;
    private UserInfoVo infoVo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mTitleReturnIv = findView(view, R.id.title_return_iv);
        mTitleReturnIv.setVisibility(View.INVISIBLE);
        mTitleNameTv = findView(view, R.id.title_name_tv);
        mTitleNameTv.setText("我");
        mMineHeadIv = findView(view, R.id.mine_head_iv);
        mMineHeadIv.setOnClickListener(this);
        mMineFeedBackLay = findView(view, R.id.mine_feed_back_lay);
        mMineFeedBackLay.setOnClickListener(this);
        mMineSetLay = findView(view, R.id.mine_set_lay);
        mMineSetLay.setOnClickListener(this);
        mMineNameTv = findView(view, R.id.mine_name_tv);
        mMineNameTv.setOnClickListener(this);
        mMineCountTv = findView(view, R.id.mine_count_tv);
        mMineMoneyTv = findView(view, R.id.mine_money_tv);
    }

    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        iMinePresenter = new MinePresenterImpl(this);
        iMinePresenter.getUserInfo();
    }

    private synchronized OSS getOSSInstance() {
        if (oss == null) {
            OSSCredentialProvider credentialProvider = new OSSFederationCredentialProvider() {
                @Override
                public OSSFederationToken getFederationToken() {
                    imageTokenVo = NetWorkWarpper.getImageToken();
                    if (imageTokenVo != null) {
                        OSSFederationToken ossFederationToken = new OSSFederationToken();
                        ossFederationToken.setTempAk(imageTokenVo.accesskeyid);
                        ossFederationToken.setTempSk(imageTokenVo.accesskeysecret);
                        ossFederationToken.setSecurityToken(imageTokenVo.securitytoken);
                        ossFederationToken.setExpirationInGMTFormat(imageTokenVo.expiration);
                        return ossFederationToken;
                    }
                    return null;
                }
            };
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            OSSLog.enableLog();
            oss = new OSSClient(MainApplication.get().getApplicationContext(), endpoint, credentialProvider, conf);
        }
        return oss;
    }

    @Override
    public void onClick(View v) {
        if (mMineHeadIv == v) {
            new AlertView("修改头像", null, "取消", null,
                    new String[]{"自定义头像", "经典头像"},
                    getActivity(), AlertView.Style.ActionSheet, this).show();
        } else if (mMineFeedBackLay == v) {//反馈问题
            startActivity(FeedBackActivity.class, "");
        } else if (mMineSetLay == v) {//设置
            startActivity(SetActivity.class, "");
        } else if (mMineNameTv == v) {//修改昵称
            if (infoVo != null) {
                startActivity(NickNameActivity.class, infoVo.nickname);
            } else {
                startActivity(NickNameActivity.class, "东莞无限");
            }
        }
    }

    @Override
    public void getUserInfoSuccess(UserInfoVo userInfoVo) {
        if (userInfoVo != null) {
            infoVo = userInfoVo;
            if (!StringUtils.isEmpty(userInfoVo.nickname)) {
                mMineNameTv.setText(userInfoVo.nickname);
            } else {
                mMineNameTv.setText("东莞无限");
            }

            if (!StringUtils.isEmpty(userInfoVo.headurl)) {
                Glide.with(getActivity()).load(userInfoVo.headurl).into(mMineHeadIv);
            } else {
                Glide.with(getActivity()).load(R.drawable.my_ico_pic).into(mMineHeadIv);
            }
            MainApplication.get().setUserMine(userInfoVo);
            EventBus.getDefault().post(new MineHeadImg(Constants.USER_MINE_FLAG, "", userInfoVo));
            mMineCountTv.setText(mMineCountTv.getText().toString() + userInfoVo.total + "次，");
            mMineMoneyTv.setText(mMineMoneyTv.getText().toString() + userInfoVo.save + "元");
        }
    }

    @Override
    public void updateUserInfoSuccess() {
        if (uploadVo != null && headImgFrom == 1) {//=1表示从相册选择上传
            mHeadUrl = downLoadUrl + "/" + uploadVo.name;
            headImgFrom = 0;
        }
        if (!StringUtils.isEmpty(mHeadUrl)) {
            MainApplication.get().setHeadUrl(mHeadUrl);
            EventBus.getDefault().post(new MineHeadImg(Constants.HEAD_IMG_FLAG, mHeadUrl));
            ToastUtil.showMiddle(getActivity(), "恭喜，更换头像成功");
        } else {
            Glide.with(getActivity()).load(R.drawable.my_ico_pic).into(mMineHeadIv);
            ToastUtil.showMiddle(getActivity(), "抱歉，请稍后重试");
        }
    }

    @Subscribe
    public void onEventMainThread(MineHeadImg event) {
        if (event.getmFlag() == Constants.HEAD_IMG_FLAG) {//更换头像
            Glide.with(getActivity()).load(event.getmMsg()).into(mMineHeadIv);
        }
        if (event.getmFlag() == Constants.NICK_NAME_FLAG) {//更换昵称
            mMineNameTv.setText(event.getmMsg());
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        switch (position) {
            case SELECT_IMG://从相册选择
                //打开相册
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_IMG);
                break;
            case CUT_IMG://选择默认头像
                Intent headIntent = new Intent(getActivity(), DefaultHeadImgActivity.class);
                startActivityForResult(headIntent, HEAD_IMG_URL);
                break;
        }
    }

    @Override
    public void applyImageUploadSuccess(final ImageUploadVo imageUploadVo) {

        OSS mOss = getOSSInstance();//初始化OSSClient
        if (imageUploadVo != null) {
            uploadVo = imageUploadVo;
        }
        if (imageUploadVo != null && !StringUtils.isEmpty(filePath)) {
            // 构造上传请求
            PutObjectRequest put = new PutObjectRequest(imageUploadVo.bucket, imageUploadVo.name, filePath);

            OSSAsyncTask task = mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    Log.d("PutObject", "UploadSuccess");
                    Log.d("ETag", result.getETag());
                    Log.d("RequestId", result.getRequestId());
                    if (iMinePresenter != null) {
                        headImgFrom = 1;
                        iMinePresenter.updateUserInfo(downLoadUrl + "/" + imageUploadVo.name, "");
                    }
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace();
                    }
                    if (serviceException != null) {
                        // 服务异常
                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                    }
                    ToastUtil.showMiddle(getActivity(), "上传失败");
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap bmap = null;
            switch (requestCode) {
                case SELECT_IMG://相册选择图片
                    Uri originalUri = data.getData();
                    cropImg(originalUri);
                    break;
                case CUT_IMG://裁剪
                    // 拿到剪切数据
                    bmap = data.getParcelableExtra("data");
                    filePath = FileUtil.getPath(bmap);//保存裁剪后的图片并得到绝对路径

                    Bitmap compBitmap = BitmapUtils.small(bmap);//压缩图片

                    String lastName = BitmapUtils.getImgLastName(filePath);//获取图片后缀名
                    int size = compBitmap.getRowBytes() * compBitmap.getHeight();

                    iMinePresenter.applyImageUpload(size, lastName);
                    break;
                case HEAD_IMG_URL://选择默认头像
                    String headUrl = data.getExtras().getString("headUrl");
                    if (!StringUtils.isEmpty(headUrl) && iMinePresenter != null) {
                        mHeadUrl = headUrl;
                        iMinePresenter.updateUserInfo(headUrl, "");
                    }
                    break;
            }
        }
    }

    public void cropImg(Uri originalUri) {
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(originalUri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);// 输出图片大小
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CUT_IMG);
    }

    public void startActivity(Class activity, String value) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra("nickName", value);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iMinePresenter != null) {
            iMinePresenter.onDestroy();
        }
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
}
