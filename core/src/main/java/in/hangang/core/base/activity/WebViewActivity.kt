package `in`.hangang.core.base.activity

import `in`.hangang.core.R
import `in`.hangang.core.databinding.ActivityWebViewBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Xml
import android.webkit.WebChromeClient
import android.webkit.WebSettings

class WebViewActivity : ViewBindingActivity<ActivityWebViewBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_web_view
    lateinit var URL: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        URL = intent.getStringExtra("url")
        initWebview()
    }
    private fun initWebview() {
        binding.hangangWebview.apply {
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            webViewClient = HangangWebViewClient(this@WebViewActivity)
            settings.loadWithOverviewMode = true    //메타태그 허용 여부
            settings.useWideViewPort = true         //화면 사이즈 맞추기 허용 여부
            settings.javaScriptCanOpenWindowsAutomatically = false //자바스크립트 새창 띄우기 허용 여
            settings.setSupportMultipleWindows(false)   //새창 띄우기 허용 여부
            settings.setDomStorageEnabled(true);        //로컬저장소 허용 여부
            settings.cacheMode = WebSettings.LOAD_NO_CACHE //브라우저 캐시 허용 여부
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING //컨텐츠 사이즈 맞추
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            settings.setSupportZoom(true)          //화면 줌 허용 여부
            settings.builtInZoomControls = true    //화면 확대 축소 허용 여부
            settings.defaultTextEncodingName =
                Xml.Encoding.UTF_8.name // 한글이 깨지지 않게 UTF8로 읽어들일수 있게 설정
        }
        binding.hangangWebview.loadUrl(URL)
    }
}