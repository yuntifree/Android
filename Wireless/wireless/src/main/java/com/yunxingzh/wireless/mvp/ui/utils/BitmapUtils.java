package com.yunxingzh.wireless.mvp.ui.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 文件操作工具类
 *
 */
public class BitmapUtils {

    /**
     * 图片裁剪
     *
     * @param bitmap
     * @return
     */
    public static Bitmap ImageCrop(Bitmap bitmap) {
        //首先得到图片的宽高,现在明显是太宽了,要裁掉
        int w = bitmap.getWidth();
        //裁掉之后高不变
        int h = bitmap.getHeight();
        //那么就计算裁掉之后的宽是多少
        float floatw = (float) ((90 / 111.0) * h);
        //转成int类型
        int cropW = (int) floatw;
        int retX = (w - cropW) / 2;//基于原图，图片左右两边各裁掉一半
        int retY = 0;

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, cropW, h, null, false);
    }


    /**
     * 图片资源转bitmap
     *
     * @param path
     * @param w
     * @param h
     * @return
     */
    public static Bitmap convertToBitmap(String path, int w, int h) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }


    /**
     * Bitmap缩小的方法
     */
    public static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }


    /**
     * 对图片进行镜像处理
     *
     * @param bmp
     * @return
     */
    public static Bitmap convertBmp(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1); // 镜像水平翻转
        Bitmap convertBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);

        return convertBmp;
    }

    private static Bitmap scaleBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth() / 2;
        int h = bitmap.getHeight() / 2;

        return Bitmap.createScaledBitmap(bitmap, w, h, false);

    }


       /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
