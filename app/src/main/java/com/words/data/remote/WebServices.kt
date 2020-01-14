package com.words.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.words.data.model.DictionaryWord
import com.words.ui.application.CustomApplication
import com.words.utils.Constant
import io.reactivex.Single
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface WebServices {
    companion object {
        fun hasNetwork(context: Context): Boolean? {
            var isConnected: Boolean? = false // Initial Value
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected)
                isConnected = true
            return isConnected
        }
        val cacheSize = (5 * 1024 * 1024).toLong()
        val context = CustomApplication.context
        val myCache = Cache(context.cacheDir, cacheSize)

        val instance: WebServices by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
                .cache(myCache)
                .readTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if(hasNetwork(context)!!){
                        request.newBuilder().header("Cache-Control","public, max-age" + 5)
                            .removeHeader("Pragma")
                            .build()
                    }
                    else{request.newBuilder().header("Cache-Control","public, only-if-cached,max-stale=" + 60 * 60 *24 * 7)
                        .removeHeader("Pragma")
                        .build()}
                    chain.proceed(request)
                }
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(WebServices::class.java)
        }
    }

    @GET(Constant.wordUrl)
    fun findDefinition(@Query("term") word: String, @Header(Constant.headerHost) headerHost: String = Constant.host,
                       @Header(Constant.headerApiKey) apiKey: String = Constant.apiKey): Single<DictionaryWordResponse>
}