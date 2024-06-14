package com.soti.workout.restapi;

import android.text.TextUtils;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final Builder sRetrofitBuilder = new Builder()
            .baseUrl(ServerApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private ServiceGenerator() {


    }

    public static <S> S createService(Class<S> serviceClass, String authToken) {
        OkHttpClient okHttpClient;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor("Bearer " + authToken);

            okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder()
                                .addHeader("Accept", "application/json")
                                .build();
                        return chain.proceed(request);
                    }).addNetworkInterceptor(logging).build();

        }


        sRetrofitBuilder.client(okHttpClient);
        return sRetrofitBuilder.build().create(serviceClass);

    }


}
