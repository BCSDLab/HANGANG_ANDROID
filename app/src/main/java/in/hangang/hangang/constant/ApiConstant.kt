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

const val LECTURE_BANKS = "/lecture-banks"
const val LECTURE_BANKS_HIT = "$LECTURE_BANKS/hit"
const val LECTURE_BANKS_PURCHASE = "$LECTURE_BANKS/purchase"
const val LECTURE_BANKS_PURCHASE_CHECK = "$LECTURE_BANKS_PURCHASE/check"
const val LECTURE_BANKS_REPORT = "$LECTURE_BANKS/report"
const val LECTURE_BANKS_SCRAP = "$LECTURE_BANKS/scrap"
const val LECTURE_BANKS_COMMENTS = "$LECTURE_BANKS/{id}/comments"
const val LECTURE_BANKS_REPORT_COMMENT = "$LECTURE_BANKS_REPORT/comment"
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