package `in`.hangang.hangang.data.entity.timetable

import com.google.gson.annotations.SerializedName

data class LectureTimeTable(
        val id: Int = 0,
        @SerializedName("lecture_id")
        val lectureId: Int = 0,
        @SerializedName("lecture_timetable_id")
        val lectureTimetableId: Int? = null,
        @SerializedName("is_custom")
        val isCustom: Boolean = false,
        @SerializedName("semester_date")
        val semesterDate: String? = null,
        val code: String? = null,
        val name: String? = null,
        val classification: String? = null,
        val grades: String? = null,
        val classNumber: String? = null,
        @SerializedName("regular_number")
        val regularNumber: String? = null,
        val department: String? = null,
        val target: String? = null,
        val professor: String? = null,
        @SerializedName("is_english")
        val isEnglish: String? = null,
        @SerializedName("design_score")
        val designScore: String? = null,
        @SerializedName("is_elearning")
        val isElearning: String? = null,
        @SerializedName("class_time")
        val classTime: String? = null,
        @SerializedName("created_at")
        val createdAt: String? = null,
        @SerializedName("updated_at")
        val updatedAt: String? = null,
        val rating: Double = 0.0
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