package `in`.hangang.hangang.ui.home.ranking.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class RankingLectureViewModel(private val lectureRepository: LectureRepository) : ViewModelBase() {
    private val _rankingLectureList = MutableLiveData<ArrayList<RankingLectureItem>>()
    val rankingLectureList: LiveData<ArrayList<RankingLectureItem>> get() = _rankingLectureList
    fun getRankingLectureByTotalRating(major: String) {
        lectureRepository.getLectureRankingByTotalRating(major)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _rankingLectureList.value = getTopFiveList(it)
            }, {
                LogUtil.e("Error in send auth number : ${it.toCommonResponse().errorMessage}")
            }).addTo(compositeDisposable)
    }

    fun getTopFiveList(list: ArrayList<RankingLectureItem>): ArrayList<RankingLectureItem> {
        var fiveList = ArrayList<RankingLectureItem>()
        for (i in 0..4) {
            fiveList.add(list.get(i))
        }
        return fiveList
    }
}