package `in`.hangang.core.progressdialog

interface IProgressDialog {
    fun showProgressDialog()
    fun hideProgressDialog()
}

fun IProgressDialog.changeProgressState(isShowing: Boolean) {
    if (isShowing) showProgressDialog()
    else hideProgressDialog()
}