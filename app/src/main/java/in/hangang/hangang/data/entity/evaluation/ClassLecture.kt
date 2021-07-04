package `in`.hangang.hangang.data.entity.evaluation

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class ClassLecture(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("classTime")
    val classTime: String,
    @SerializedName("classNumber")
    val classNumber: String,
    @SerializedName("professor")
    val professor: String,
    @SerializedName("selectedTableId")
    val selectedTableId: ArrayList<String>,
    @SerializedName("target")
    val target: String,



){
    fun getClassList(time: String): ArrayList<Int>{
        var classTimeItem = time.substring(1, time.length)
        classTimeItem = classTimeItem.substring(0, classTimeItem.length-1)
        var list = classTimeItem.split(", ")
        var classTimeList: ArrayList<Int> = ArrayList()
        for(i in list){
            classTimeList.add(i.toInt())
        }
        return classTimeList
    }
    fun getFilterClassNumber(s: String): String{
        return "(${classNumber})"
    }
}
