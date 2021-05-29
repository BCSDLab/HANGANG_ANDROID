package `in`.hangang.hangang.constant

const val STAGE_SERVER_BASE_URL = "https://api.hangang.in"
const val PRODUCTION_SERVER_BASE_URL = "https://api.hangang.in"

const val AUTH_TEST = "/user/auth-test"
const val SIGN_UP = "/user/sign-up"
const val REFRESH = "/user/refresh"
const val PASSWORD_FIND = "/user/password-find"
const val CHECK_NICKNAME = "/user/nickname-check"
const val LOGIN = "/user/login"
const val SEND_EMAIL = "/user/email"
const val CONFIG_EMAIL = "/user/email/config"
const val SEND_PASSWORD_FIND_EMAIL = "/user/email"
const val SEND_PASSWORD_CONFIG_EMAIL = "/user/email/config"
const val LECTURES_RANKING = "/lectures"
const val SORT_BY_TOTAL_RATING = "평점순"
const val SORT_BY_REVIEW_COUNT = "평가순"
const val SORT_BY_LATEST_REVIEW = "최신순"
const val LECTURE_SCRAPED = "/scrap/lecture"
const val EVALUATION_RATING = "/evaluation/rating/{id}"
const val EVALUTATION_TOTAL = "/evaluation/total/{id}"
const val CLASS_LECTURES = "/class/lectures/{id}"
const val LECTURE_DOCUMENTS = "/lecture-banks"
const val LECTURE_REVIEWS = "/reviews/lectures/{id}"
const val LECTURE_REVIEW = "/reviews/{id}"
const val LECTURE_SEMESTER = "/semesterdates/lectures/{id}"
const val TIMETABLE = "/timetable"
const val TIMETABLE_LECTURE = "/timetable/lecture"
const val REVIEW_RECOMMEND = "/review/recommend"
const val TIMETABLE_CUSTOM_LECTURE = "/timetable/custom/lecture"
const val TIMETABLE_LECTURE_LIST = "/timetable/lecture/list"
const val TIMETABLE_MAIN = "/timetable/main/lecture"
const val TIMETABLE_MEMO = "/memo"
const val TIMETABLE_SCRAP = "/timetable/scrap"

const val LECTURES = "/lectures"

const val API_ERROR_CODE_TIMETABLE_EXCEED = 24

const val API_TIMETABLE_DEFAULT_LIMIT = 10
const val API_TIMETABLE_DEFAULT_PAGE = 1