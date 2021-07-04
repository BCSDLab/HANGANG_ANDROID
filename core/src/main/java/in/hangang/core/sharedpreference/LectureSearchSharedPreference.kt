package `in`.hangang.core.sharedpreference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONException


object LectureSearchSharedPreference {
    private const val DATA = "DATA"
    val SEARCH_LIST_KEY = "SEARCH_LIST_KEY"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    var data: String
        get() = preferences.getString(DATA, "") ?: ""
        set(value) = preferences.edit {
            putString(DATA, value)
        }
    var querys: ArrayList<String>
        get() = getStringArray(SEARCH_LIST_KEY)
        set(value) = preferences.edit{
            remove(SEARCH_LIST_KEY)
            var a = JSONArray()
            for(item in value){
                a.put(item)
            }
            if(!value.isEmpty()){
                putString(SEARCH_LIST_KEY, a.toString())
            } else{
                putString(SEARCH_LIST_KEY, null)
            }
            apply()
        }
    fun getStringArray(key: String): ArrayList<String>{

        val json = preferences.getString(key, null)
        var urls = ArrayList<String>()
        if (json != null) {
            try {
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val url = a.optString(i)
                    urls.add(url)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return urls

    }

    fun setSearchList(query: ArrayList<String>) {
        putStringArray(SEARCH_LIST_KEY, query)
    }
    fun putStringArray(key: String, value: ArrayList<String>) {

        val e = preferences.edit()
        var a = JSONArray()
        for(item in value){
            a.put(item)
        }
        if(!value.isEmpty()){
            preferences.edit{putString(key, a.toString())}
        } else{
            preferences.edit{putString(key, null)}
        }

        e.apply()
    }
}
