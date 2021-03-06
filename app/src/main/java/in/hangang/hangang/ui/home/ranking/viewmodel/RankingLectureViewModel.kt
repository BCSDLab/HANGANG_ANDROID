package `in`.hangang.hangang.ui.home.ranking.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.LectureRepository
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
    var majorArrayList = ArrayList<String>()

    fun getRankingLectureByTotalRating(majors: ArrayList<String>) {
        lectureRepository.getLectureRankingByTotalRating(majors, 1)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .map { it -> it.result }
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