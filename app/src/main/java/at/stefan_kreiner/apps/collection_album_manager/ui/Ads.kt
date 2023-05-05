package at.stefan_kreiner.apps.collection_album_manager.ui.composables

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import at.stefan_kreiner.apps.collection_album_manager.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import java.time.Duration
import java.time.Instant

@Composable
fun BannerAd(
    adUnitId: String = stringResource(id = R.string.ad_mob_banner_id),
) {
    AndroidView(factory = { context ->
        AdView(context).apply {
            setAdSize(AdSize.FLUID)
            this.adUnitId = adUnitId
            loadAd(AdRequest.Builder().build())
        }
    })
}

private val TAG = "Ads"

private var nextInterstitialTimestamp: Instant = Instant.now()
private val interstitialAdInterval = Duration.ofMinutes(0)

private var mAdManagerInterstitialAd: AdManagerInterstitialAd? = null
fun loadInterstitial(context: Context) {
    AdManagerInterstitialAd.load(context,
        context.getString(R.string.ad_mob_interstitial_video_id),
        AdManagerAdRequest.Builder().build(),
        object : AdManagerInterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mAdManagerInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mAdManagerInterstitialAd = interstitialAd
            }
        })
}


fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun showInterstitial(
    context: Context,
    onAdClicked: () -> Unit = {},
    onAdDismissedFullScreenContent: () -> Unit = {},
    onAdFailedToShowFullScreenContent: (AdError) -> Unit = {},
    onAdImpression: () -> Unit = {},
    onAdShowedFullScreenContent: () -> Unit = {},
) {
    val activity = context.findActivity()

    Log.d(TAG, "ad available: ${mAdManagerInterstitialAd != null}")
    Log.d(TAG, "activity available: ${activity != null}")

    if (mAdManagerInterstitialAd != null && activity != null && nextInterstitialTimestamp < Instant.now()) {
        mAdManagerInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                onAdClicked()
                // Called when ad is dismissed.
                Log.d(TAG, "Ad clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                mAdManagerInterstitialAd = null
                loadInterstitial(context)
                onAdDismissedFullScreenContent()
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mAdManagerInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                mAdManagerInterstitialAd = null
                loadInterstitial(context)
                onAdFailedToShowFullScreenContent(p0)
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
            }

            override fun onAdImpression() {
                onAdImpression()
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
                nextInterstitialTimestamp = Instant.now() + interstitialAdInterval
            }

            override fun onAdShowedFullScreenContent() {
                onAdShowedFullScreenContent()
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
        mAdManagerInterstitialAd?.show(activity)
    }
}

fun removeInterstitial() {
    mAdManagerInterstitialAd?.fullScreenContentCallback = null
    mAdManagerInterstitialAd = null
}