package com.wushiyi.mvp.di

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.wushiyi.mvp.MvpInit
import com.wushiyi.mvp.net.converter.ResponseConvertFactory
import com.wushiyi.mvp.sApplication
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import timber.log.Timber
import java.io.File
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by zhangyuncai on 2019/6/28.
 * 提供通用对象
 */
@Module
class AppModule(val application: Application, val baseUrl: String) {

    companion object {

        val LINE_SEPARATOR = System.getProperty("line.separator")

        private val TIMEOUT_CONNECT = (30 * 1000).toLong()

        fun newRetrofit(baseUrl: String): Retrofit {
            val appModule = AppModule(sApplication, baseUrl)
            val loggingInterceptor = appModule.providesHttpLoggingInterceptor()
            val okHttpClient = appModule.provideOkHttpClient(loggingInterceptor, sApplication)
            val retrofit1 = appModule.providesRetrofit(okHttpClient, sApplication)
            return retrofit1
        }
    }

    @Provides
    fun providesApplication(): Application {
        return application
    }

    @Provides
    fun providesContext(): Context {
        return application.applicationContext
    }


    /**
     * 打印日志
     *
     * @param msg
     */
    fun printJson(msg: String) {

        var message: String

        try {
            if (msg.startsWith("{")) {
                val jsonObject = JSONObject(msg)
                message = jsonObject.toString(4)//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (msg.startsWith("[")) {
                val jsonArray = JSONArray(msg)
                message = jsonArray.toString(4)
            } else {
                message = msg
            }
        } catch (e: JSONException) {
            message = msg
        }

        Timber.d("╔═══════════════════════════════════════════════════════════════════════════════════════")
        message = LINE_SEPARATOR!! + message
        val lines = message.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            //            LogUtil.d("", line);
            Timber.d(line)
        }
        Timber.d("╚═══════════════════════════════════════════════════════════════════════════════════════")
    }

    /**
     * 构造日志类
     *
     * @return
     */
    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            if (!TextUtils.isEmpty(message)) {
                val s = message.substring(0, 1)

                if (message.contains("http")) {
                    //                        LogUtil.e("httpRequest:"+message);
                }
                //如果收到响应是json才打印
                if ("{" == s || "[" == s) {
                    printJson(message)
                    //                        LogUtil.e("httpRequest json:"+message);
                    return@Logger
                }
            }
            Timber.d("" + message)
        })
        //        if (GlobalValue.isDebug) {
        if (true) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }


    /**
     * 构造OkHttpClient
     *
     * @param httpLoggingInterceptor
     * @param application
     * @return
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor,
                            application: Application): OkHttpClient {
        val httpCacheDirectory = File(application.cacheDir, "a5b_app_cache")
        val cache = Cache(httpCacheDirectory, (10 * 1024 * 1024).toLong())//10m
        val okHttpBuilder = OkHttpClient.Builder()

        try {
            okHttpBuilder.sslSocketFactory(addSSL())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        okHttpBuilder.hostnameVerifier { hostname, session -> true }

        okHttpBuilder.connectTimeout(TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
        okHttpBuilder.writeTimeout(TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
        okHttpBuilder.readTimeout(TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
        //        okHttpBuilder.retryOnConnectionFailure(true);

        // Adds authentication headers when required in network calls
//        okHttpBuilder.addNetworkInterceptor(AuthenticationInterceptor())
        //拦截器 日志,所有的请求响应都看到
        okHttpBuilder.addInterceptor(httpLoggingInterceptor)

        //拦截器 添加公共参数
        //        okHttpBuilder.addInterceptor(addQueryParameterInterceptor());//自定义拦截器 请求体 的 公共参数 拦截
        //重写拦截器  Interceptor  设置缓存
        //        okHttpBuilder.addNetworkInterceptor(addCacheInterceptor(application));//自定义拦截器 网络缓存 拦截
        //错误重连
        okHttpBuilder.retryOnConnectionFailure(true)
        //okhttp 添加缓存
        okHttpBuilder.cache(cache)
        /*if (GlobalValue.isCom()) {//正式环境
            try {
                //添加https 证书
                HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(new InputStream[]{new Buffer().writeUtf8(GlobalValue.SIGN_INFO).inputStream()}, null, null);
                okHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
            okHttpBuilder.hostnameVerifier((hostname, session) -> true);
        }*/
        return okHttpBuilder.build()
    }

    @Throws(Exception::class)
    private fun addSSL(): SSLSocketFactory {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager

        return sslContext.socketFactory
    }

    /**
     * 构造Retrofit
     *
     * @param okHttpClient
     * @param application
     * @return
     */
    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, application: Application): Retrofit {
        //创建缓存文件 指定路径 缓存名称

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                //                .validateEagerly(false)// Fail early: check Retrofit configuration at creation time in Debug build.
                //                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addConverterFactory(ResponseConvertFactory.create(application))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }
}

/*
添加静态head参数,这个经过实践可行
@Headers("type:android")
    @GET("/zheshijiade")
    fun getHa(@Query("hahah") hahah:String):Observable<ResponseBody>
 */

/**
//添加头部信息
okHttpBuilder.addInterceptor(chain -> {
Request request = chain.request()
.newBuilder()
.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
.addHeader("Accept-Encoding", "gzip, deflate")
.addHeader("Connection", "keep-alive")
.addHeader("Accept", "* / *")//把中间的去掉
.addHeader("syt_app_t", "android")
.addHeader("syt_app_v", AppUtils.getVersionName(application))
.addHeader("syt_m_id", GlobalValue.MB_ID)
.addHeader("syt_app_c", android.os.Build.SERIAL == null ? "" : android.os.Build.SERIAL)
.build();
return chain.proceed(request);
});
 */

/*
//这个经过实践可行
@HeaderMap Map<String, String> headers 这个可以动态  key-value
        @GET("/")
Call<ResponseBody> foo(@Header("Accept-Language") String lang);
         */