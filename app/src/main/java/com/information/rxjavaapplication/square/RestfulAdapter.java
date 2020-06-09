package com.information.rxjavaapplication.square;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestfulAdapter {
    private static final String BASE_URL = "https://api.github.com";

    private RestfulAdapter() { }
    private static class Singleton {
        private static final RestfulAdapter instance = new RestfulAdapter();
    }
    public static RestfulAdapter getInstance() {
        return Singleton.instance;
    }

    /**
     * Retrofit에 포함된 OKHttpClient클래스를 사용
     * @return
     */
    public GitHubServiceApi getSimpleApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GitHubServiceApi.class);
    }

    /**
     * 직접 OkHttpClient 객체를 구성해서 로그를 위한 인터셉터를 설정
     * 인터셉터를 설정하면 네트워크를 통해 이동하는 데이터나 에러 메시지를 실시간으로 확인 가능
     * @return
     */
    public GitHubServiceApi getServiceApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
                .build();

        return retrofit.create(GitHubServiceApi.class);
    }
}
