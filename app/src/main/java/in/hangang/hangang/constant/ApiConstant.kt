package `in`.hangang.hangang.constant

const val STAGE_SERVER_BASE_URL = "https://api.hangang.in"
const val PRODUCTION_SERVER_BASE_URL = "https://api.hangang.in"

const val AUTH_TEST = "/user/auth-test"
const val SIGN_UP = "/user/sign-up"
const val REFRESH = "/user/refresh"
const val PASSWORD_FIND = "/user/password-find"
const val CHECK_NICKNAME = "/user/nickname-check"
const val USER_ME = "/user/me"
const val LOGIN = "/user/login"
const val SEND_EMAIL = "/user/email"
const val CONFIG_EMAIL = "/user/email/config"
const val SEND_PASSWORD_FIND_EMAIL = "/user/email"
const val SEND_PASSWORD_CONFIG_EMAIL = "/user/email/config"
const val LECTURES_RANKING = "/lectures"
const val LECTURES = "/lectures"
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

const val API_ERROR_CODE_TIMETABLE_EXCEED = 24

const val API_TIMETABLE_DEFAULT_LIMIT = 10
const val API_TIMETABLE_DEFAULT_PAGE = 1

const val LECTURE_BANKS = "/lecture-banks"
const val LECTURE_BANKS_HIT = "/lecture-banks/hit"
const val LECTURE_BANKS_PURCHASE = "/lecture-banks/purchase/{id}"
const val LECTURE_BANKS_PURCHASE_CHECK = "/lecture-banks/purchase/check/{id}"
const val LECTURE_BANKS_REPORT = "/lecture-banks/report/"
const val LECTURE_BANKS_SCRAP = "/lecture-banks/scrap"
const val LECTURE_BANKS_COMMENTS = "/lecture-banks/{id}/comments"
const val LECTURE_BANKS_COMMENT = "/lecture-banks/{id}/comment"
const val LECTURE_BANKS_COMMENT_WITH_ID = "/lecture-banks/{id}/comment/{commentId}"
const val LECTURE_BANKS_REPORT_COMMENT = "/lecture-banks/report/comment"
const val LECTURE_BANKS_FILE = "/lecture-banks/file/download/{id}"
const val LECTURE_BANKS_FILES = "/lecture-banks/files"
const val LECTURE_BANKS_CATEGORY_PREVIOUS = "기출자료"
const val LECTURE_BANKS_CATEGORY_WRITING = "필기자료"
const val LECTURE_BANKS_CATEGORY_ASSIGNMENT = "과제자료"
const val LECTURE_BANKS_CATEGORY_LECTURE = "강의자료"
const val LECTURE_BANKS_CATEGORY_ETC = "기타자료"
const val LECTURE_BANKS_ORDER_BY_ID = "id"
const val LECTURE_BANKS_ORDER_BY_HITS = "hits"
const val LECTURE_BANKS_REPORT_ABUSEMENT = 1
const val LECTURE_BANKS_REPORT_COPYRIGHT = 2
const val LECTURE_BANKS_REPORT_FAKE = 3
const val LECTURE_BANKS_REPORT_ADVERTISEMENT = 4
const val LECTURE_BANKS_REPORT_PORNOGRAPHY = 5
