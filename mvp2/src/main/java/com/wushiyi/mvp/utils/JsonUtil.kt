package com.wushiyi.mvp.utils

import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * Created by zhangyuncai on 2019/6/28.
 * json字符串解析
 */
object JsonUtil {
    private val gson = Gson()

    /**
     * 把对象转成json字符串
     * exp:
     * val list = mutableListOf<Student>()
    list.add(Student("xiaoming",10))
    list.add(Student("xiaoming",10))
    list.add(Student("xiaoming",10))
    list.add(Student("xiaoming",10))
    val json = JsonUtil.anyToJson(list)
     */
    fun anyToJson(src: Any): String {
        var jsonStr = ""
        try {
            jsonStr = gson.toJson(src)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jsonStr
    }

    /**
     *解析json字符串,返回对象
     * todo 解析单个对象最好用这个,因为gson解析,最好是具体到类而不能使用泛型,否则报错com.google.gson.internal.LinkedTreeMap cannot be cast to com.wushiyi.jitmvp.Dog
     */
    fun <T> jsonToAny(src: String, clazz: Class<T>): T? {
        try {
            val t = gson.fromJson(src, clazz)
            return t
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * todo 解析对象和集合都没有问题
     *解析json字符串,返回对象
     * exp:
     * val type = object : TypeToken<MutableList<Student>>() {}.type
    val fromJson = Gson().fromJson<MutableList<Student>>(json, type)
     */
    fun <T> jsonToAny(src: String, type: Type): T? {
        try {
            val t = gson.fromJson<T>(src, type)
            return t
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}