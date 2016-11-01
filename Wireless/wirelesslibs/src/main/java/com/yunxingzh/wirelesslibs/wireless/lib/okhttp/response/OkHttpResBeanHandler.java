package com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response;

import com.alibaba.fastjson.JSON;

import okhttp3.Response;

/**
 * Created by ldy on 2015/12/30.
 */
public abstract class OkHttpResBeanHandler<T> extends OkHttpCallback<T> {

    @Override
    protected T parseResponse(Response response) throws Throwable {
        String responseStr = responseToString(response);
//        if (response.request().url().toString().equals(Api.LED_PRODUCT_LIST)
//                || response.request().url().toString().equals(Api.LED_XDG_HOT_PRODUCT_LIST)
//                || response.request().url().toString().equals(Api.LED_XDG_GROUP_PRODUCT_LIST)
//                || response.request().url().toString().equals(Api.LED_XDG_PRODUCT_LIST_BY_CATEGORY)
//                || response.request().url().toString().equals(Api.LED_PRODUCT_DETAIL)) {
//            responseStr = responseStr.replaceAll("\"Product_", "\"");
//            responseStr = responseStr.replaceAll("\"ProductAreaMapping_", "\"AreaMapping_");
//        }
//        if (response.request().url().toString().equals(Api.LED_PURCHASE_PRODUCT_LIST)
//                || response.request().url().toString().equals(Api.LED_PURCHASE_INDEX_RECOMMEND)
//                || response.request().url().toString().equals(Api.LED_PURCHASE_INDEX_CATEGORY_RECOMMEND)
//                || response.request().url().toString().equals(Api.LED_PURCHASE_PRODUCT_DETAIL)) {
//            responseStr = responseStr.replaceAll("\"DistributorProduct_", "\"");
//            responseStr = responseStr.replaceAll("\"DistributorAreaMapping_", "\"AreaMapping_");
//        }
//        if(response.request().url().toString().equals(Api.LED_SHOPPING_LIST)){
//            responseStr = responseStr.replaceAll("\"Product_", "\"");
//            responseStr = responseStr.replaceAll("\"ProductCart_", "\"Cart_");
//            responseStr = responseStr.replaceAll("\"ProductAreaMapping_", "\"AreaMapping_");
//        }
//        if(response.request().url().toString().equals(Api.LED_DISTRIBUTOR_SHOPPING_LIST)){
//            responseStr = responseStr.replaceAll("\"DistributorProduct_", "\"");
//            responseStr = responseStr.replaceAll("\"DistributorCart_", "\"Cart_");
//            responseStr = responseStr.replaceAll("\"DistributorAreaMapping_", "\"AreaMapping_");
//        }

        return JSON.parseObject(responseStr, getGenericType());
    }
}
