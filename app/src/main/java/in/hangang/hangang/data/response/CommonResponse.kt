package `in`.hangang.hangang.data.response

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException

open class CommonResponse {
    @SerializedName("className")
    val className: String? = null

    @SerializedName("errorMessage")
    val errorMessage: String? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("code")
    val code: Int? = null

    @SerializedName("httpStatus")
    val httpStatus: String? = null

    @SerializedName("errorTrace")
    val errorTrace: String? = null
}

fun Throwable.toCommonResponse(): CommonResponse {
    if (this !is HttpException) return CommonResponse()
    val body = this.response()?.errorBody()
    val gson = Gson()
    val adapter: TypeAdapter<CommonResponse> = gson.getAdapter(CommonResponse::class.java)
    return try {
        val errorMessage: CommonResponse = adapter.fromJson(body?.string())
        errorMessage
    } catch (e Exception) {
        CommonResponse()
    }
}