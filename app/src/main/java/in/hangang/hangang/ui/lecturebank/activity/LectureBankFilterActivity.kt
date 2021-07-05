package `in`.hangang.hangang.ui.lecturebank.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankFilter
import `in`.hangang.hangang.databinding.ActivityLectureBankFilterBinding
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankFilterActivityContract
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox

class LectureBankFilterActivity : ViewBindingActivity<ActivityLectureBankFilterBinding>() {
    override val layoutId = R.layout.activity_lecture_bank_filter

    private val categoryMap: Map<CheckBox, String> by lazy {
        mapOf(
            binding.checkBoxLectureBankCategoryPreviousBank to LECTURE_BANKS_CATEGORY_PREVIOUS,
            binding.checkBoxLectureBankCategoryWritingBank to LECTURE_BANKS_CATEGORY_WRITING,
            binding.checkBoxLectureBankCategoryAssignmentBank to LECTURE_BANKS_CATEGORY_ASSIGNMENT,
            binding.checkBoxLectureBankCategoryLectureBank to LECTURE_BANKS_CATEGORY_LECTURE,
            binding.checkBoxLectureBankCategoryEtcBank to LECTURE_BANKS_CATEGORY_ETC
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            intent.extras?.getParcelable<LectureBankFilter>(LectureBankFilterActivityContract.INTENT_EXTRA_FILTER)
                ?.let {
                    categoryMap.filterValues { value ->
                        it.categories?.contains(value) ?: false
                    }.keys.forEach { it.isChecked = true }

                    if (it.order == LECTURE_BANKS_ORDER_BY_HITS) radioButtonSortByThumbsCount.isChecked = true
                    else radioButtonSortByRecent.isChecked = true
                }

            buttonReset.setOnClickListener {
                radioButtonSortByRecent.isChecked = true
                radioButtonSortByThumbsCount.isChecked = false

                categoryMap.keys.forEach { it.isChecked = false }
            }

            buttonClose.setOnClickListener {
                finish()
            }

            buttonApply.setOnClickListener {
                setResult(RESULT_OK, Intent().apply {
                    val filter = LectureBankFilter(
                        order = if (radioButtonSortByThumbsCount.isChecked) LECTURE_BANKS_ORDER_BY_HITS else LECTURE_BANKS_ORDER_BY_ID,
                        categories = with(categoryMap.filterKeys { it.isChecked }.values.toList()) {
                            this.ifEmpty { null }
                        }
                    )

                    putExtra(LectureBankFilterActivityContract.INTENT_EXTRA_FILTER, filter)
                })
                finish()
            }
        }
    }
}