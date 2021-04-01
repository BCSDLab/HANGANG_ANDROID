package `in`.hangang.core.progressdialog

interface IProgressDialog {
    fun showProgressDialog()
    fun hideProgressDialog()
}

fun IProgressDialog.progressState(isShowing: Boolean) {
    if(isShowing) showProgressDialog()
    else hideProgressDialog()
}