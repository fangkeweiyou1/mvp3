package com.wushiyi.mvp.net.converter;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * Created by zhangyuncai on 2018/9/11.
 */
public class ResponseConvertFactory extends Converter.Factory {

    public static ResponseConvertFactory create(Application application) {
        Gson gson = new GsonBuilder().setLenient().create();
        return create(application, gson);
    }

    public static ResponseConvertFactory create(Application application, Gson gson) {
        return new ResponseConvertFactory(application, gson);
    }

    private final Application application;
    private final Gson gson;

    private ResponseConvertFactory(Application application, Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.application = application;
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(application, gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }

}
