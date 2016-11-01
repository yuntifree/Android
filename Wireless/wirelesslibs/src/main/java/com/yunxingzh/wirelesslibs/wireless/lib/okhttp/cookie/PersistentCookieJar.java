/*
 * Copyright (C) 2016 Francisco José Montiel Navarro.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yunxingzh.wirelesslibs.wireless.lib.okhttp.cookie;

import android.content.Context;

import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.cookie.cache.CookieCache;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.cookie.cache.SetCookieCache;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.cookie.persistence.CookiePersistor;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.cookie.persistence.SharedPrefsCookiePersistor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieJar implements ClearAbleCookieJar {

    private CookieCache cache;
    private CookiePersistor persistor;

    public PersistentCookieJar(Context context) {
        this(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }

    public PersistentCookieJar(CookieCache cache, CookiePersistor persistor) {
        this.cache = cache;
        this.persistor = persistor;
        try {
            this.cache.addAll(persistor.loadAll());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        try {
            cache.addAll(cookies);
            persistor.saveAll(cookies);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    synchronized public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> removedCookies = new ArrayList<Cookie>();
        List<Cookie> validCookies = new ArrayList<Cookie>();
        try {
            for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
                Cookie currentCookie = it.next();

                if (isCookieExpired(currentCookie)) {
                    removedCookies.add(currentCookie);
                    it.remove();

                } else if (currentCookie.matches(url)) {
                    validCookies.add(currentCookie);
                }
            }
            persistor.removeAll(removedCookies);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return validCookies;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    synchronized public void clear() {
        try {
            cache.clear();
            persistor.clear();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
