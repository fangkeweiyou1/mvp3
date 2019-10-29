package com.wushiyi.mvp.net.converter;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by zhangyuncai on 2018/9/11.
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Application application;
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Application application, Gson gson, TypeAdapter<T> adapter) {
        this.application = application;
        this.gson = gson;
        this.adapter = adapter;
    }


    /**
     * 返回数据统一处理
     *
     * @param value ResponseBody
     * @return value 返回异常
     * @throws IOException 返回数据统一处理
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            T resultModel = adapter.read(jsonReader);
            if (application instanceof IResultListener) {
                ((IResultListener) application).result(resultModel);
            }
            return resultModel;
        } finally {
            value.close();
        }
    }
}