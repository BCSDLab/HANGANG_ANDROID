package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.R
import `in`.hangang.core.databinding.DialogListViewBinding
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.ui.lecturereview.adapter.ListDialogRecyclerViewAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView

class TimeTableListDialog(val recyclerviewAdapter: ListDialogRecyclerViewAdapter, val dialogClickListener: TimeTableListDialogClickListener ) : DialogFragment() {
    private lateinit var binding: DialogListViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.RoundedCornersDialog)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogListViewBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        binding.listDialogRecyclerview.adapter = recyclerviewAdapter
    }
    private fun initEvent() {
        binding.listDialogButtonNegative.setOnClickListener {
            dialogClickListener.onCancelClick(this)
            //dismiss()
        }
        binding.listDialogButtonPositive.setOnClickListener {
            dialogClickListener.onConfirmClick(this)
            //dismiss()
        }
    }
    interface TimeTableListDialogClickListener {
        fun onConfirmClick(view: DialogFragment)
        fun onCancelClick(view: DialogFragment)
    }

}