package `in`.hangang.hangang.util

class ClassLectureTimeUtil {

    companion object{
        fun getClassLectureTime(time: Int, minTime: Int, maxTime: Int): String{
            var classLectureTime = ""
            classLectureTime = classLectureTime.plus("${getDay(time)} ")
            classLectureTime = classLectureTime.plus("${getDuration(minTime)}~")
            classLectureTime = classLectureTime.plus("${getDuration(maxTime)}")
            return classLectureTime
        }
        fun getDay(time: Int): String{
            return when (time){
                in 0..17 -> "월"
                in 100..117 -> "화"
                in 200..217 -> "수"
                in 300..317 -> "목"
                in 400..417 -> "금"
                in 500..517 -> "토"
                in 600..617 -> "일"
                else ->"unknown"
            }
        }
        fun getDuration(time: Int): String{
            return when(time){
                0,100,200,300,400,500,600 -> "01A"
                1,101,201,301,401,501,601 -> "01B"
                2,102,202,302,402,502,602 -> "02A"
                3,103,203,303,403,503,603 -> "02B"
                4,104,204,304,404,504,604 -> "03A"
                5,105,205,305,405,505,605 -> "03B"
                6,106,206,306,406,506,606 -> "04A"
                7,107,207,307,407,507,607 -> "04B"
                8,108,208,308,408,508,608 -> "05A"
                9,109,209,309,409,509,609 -> "05B"
                10,110,210,310,410,510,610 -> "06A"
                11,111,211,311,411,511,611 -> "06B"
                12,112,212,312,412,512,612 -> "07A"
                13,113,213,313,413,513,613 -> "07B"
                14,114,214,314,414,514,614 -> "08A"
                15,115,215,315,415,515,615 -> "08B"
                16,116,216,316,416,516,616 -> "09A"
                17,117,217,317,417,517,617 -> "09B"
                else ->"unknown"
            }
        }
        fun getSemester(id: Int): String{
            return when(id){
                1 -> "2019년 1학기"
                2 -> "2019년 2학기"
                3 -> "2020년 1학기"
                4 -> "2020년 2학기"
                5 -> "2021년 1학기"
                6 -> "2021년 2학기"
                7 -> "2022년 1학기"
                8 -> "2022년 2학기"
                else -> "unknown"
            }
        }
    }
}