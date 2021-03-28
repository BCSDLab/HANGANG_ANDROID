package `in`.hangang.hangang.data.entity

import com.google.gson.annotations.SerializedName

data class LectureTimeTable(
        val id: Int,
        @SerializedName("lecture_id")
        val lectureId: Int,
        @SerializedName("is_custom")
        val isCustom: Boolean,
        @SerializedName("semester_date")
        val semesterDate: String?,
        val code: String?,
        val name: String?,
        val classification: String?,
        val grades: String?,
        val classNumber: String?,
        @SerializedName("regular_number")
        val regularNumber: String?,
        val department: String?,
        val target: String?,
        val professor: String?,
        @SerializedName("is_english")
        val isEnglish: String?,
        @SerializedName("design_score")
        val designScore: String?,
        @SerializedName("is_elearning")
        val isElearning: String?,
        @SerializedName("class_time")
        val classTime: String?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        val rating: Double
) {
        override fun equals(other: Any?): Boolean {
                return this.lectureId == ((other as? LectureTimeTable)?.lectureId ?: -1)
        }

        override fun hashCode(): Int {
                return lectureId.hashCode()
        }

        fun contains(keyword: CharSequence): Boolean =
            (semesterDate?.contains(keyword, true) ?: false) ||
                    (code?.contains(keyword, true) ?: false) ||
                    (name?.contains(keyword, true) ?: false) ||
                    (classification?.contains(keyword, true) ?: false) ||
                    (grades?.contains(keyword, true) ?: false) ||
                    (classNumber?.contains(keyword, true) ?: false) ||
                    (regularNumber?.contains(keyword, true) ?: false) ||
                    (department?.contains(keyword, true) ?: false) ||
                    (target?.contains(keyword, true) ?: false) ||
                    (designScore?.contains(keyword, true) ?: false) ||
                    (isElearning?.contains(keyword, true) ?: false) ||
                    (classTime?.contains(keyword, true) ?: false) ||
                    (createdAt?.contains(keyword, true) ?: false) ||
                    (updatedAt?.contains(keyword, true) ?: false)

}