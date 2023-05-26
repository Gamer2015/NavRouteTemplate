package at.stefan_kreiner.apps.collection_album_manager.ui.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd(
    adUnitId: String,
) {
    AndroidView(factory = { context ->
        AdView(context).apply {
            setAdSize(AdSize.FLUID)
            this.adUnitId = adUnitId
            loadAd(AdRequest.Builder().build())
        }
    })
}