@file:Suppress("unused")

package com.baiyu.androidx.basicmodule.di

import com.baiyu.androidx.basicmodule.BaseConstant
import com.baiyu.androidx.basicmodule.base.BaseApp
import com.baiyu.androidx.basicmodule.ext.logD
import com.baiyu.androidx.basicmodule.interceptor.CacheInterceptor
import com.baiyu.androidx.basicmodule.interceptor.CacheNetworkInterceptor
import com.baiyu.androidx.basicmodule.network.ApiService
import com.baiyu.androidx.basicmodule.network.CoroutineCallAdapterFactory
import com.baiyu.androidx.basicmodule.util.MMKVUtil
import com.google.gson.GsonBuilder
import com.tencent.mmkv.MMKV
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @Author:BaiYu
 * @Email:baiyu@autoai.com
 * @Time:2020/7/20 8:54
 * @description：Koin注入
 */
val baseModule = module {
    single {
        MMKVUtil(get())
    }
    single<MMKV> {
        MMKV.mmkvWithID(
            BaseConstant.MMKV_ID,
            MMKV.MULTI_PROCESS_MODE,
            BaseConstant.MMKV_CRYPT_KEY
        )
    }
}

val repositoryModule = module {

}

val netWorkModule = module {
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .cache(Cache(File(BaseApp.CONTEXT.cacheDir, "okhttp_cache"), 1024 * 1024 * 256L))
            .addInterceptor(CacheInterceptor())
            .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    "msg=$message".logD("OkHttp")
                }
            }).apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            })    // 日志拦截器
            .addNetworkInterceptor(CacheNetworkInterceptor())
            .connectTimeout(BaseConstant.HTTP_WRITE_TIME, TimeUnit.SECONDS)
            .readTimeout(BaseConstant.HTTP_READ_TIME, TimeUnit.SECONDS)
            .writeTimeout(BaseConstant.HTTP_CONNECT_TIME, TimeUnit.SECONDS)
            .build()
    }
    single<ApiService> {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BaseConstant.BASE_URL)
            .build().create(ApiService::class.java)
    }
}
