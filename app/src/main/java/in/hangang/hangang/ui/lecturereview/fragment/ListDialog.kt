package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.R
import `in`.hangang.core.databinding.DialogListViewBinding
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.data.entity.TimeTable
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

class ListDialog(val recyclerviewAdapter: ListDialogRecyclerViewAdapter): DialogFragment() {
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
        binding.listDialogButtonNegative.setOnClickListener {
            dismiss()
        }
        binding.listDialogRecyclerview.adapter = recyclerviewAdapter
    }

}