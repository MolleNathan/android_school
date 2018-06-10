package com.example.nathan.schoolmollenathan;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nathan on 10/06/2018.
 */

public class RequestInterface {
    public static <T> T getInterface(Class<T> controller){
        // Adresse de L'api
    String base_url = "http://10.0.0.2/";
        final String user = User.getAuth_token();
        if(user == null){
            return new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(controller);
        }
        else {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("AUTHORIZATION", user)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });

            OkHttpClient client = httpClient.build();

            return new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).client(client).build().create(controller);
        }




    }
}
