package at.stefan_kreiner.apps.collection_album_manager.ui.composable

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import at.stefan_kreiner.apps.collection_album_manager.ui.ads.AdUnitId
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback

private val TAG = "InterstitialAds"

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

private val loadedInterstitialAds: MutableMap<AdUnitId, AdManagerInterstitialAd?> = mutableMapOf()

fun showInterstitial(
    context: Context,
    adUnitId: AdUnitId,
    onAdClicked: () -> Unit = {},
    onAdFailedToShowFullScreenContent: (AdError) -> Unit = {},
    onAdImpression: () -> Unit = {},
    onAdShowedFullScreenContent: () -> Unit = {},
    onAdDismissedFullScreenContent: () -> Unit = {},
) {
    val activity = context.findActivity()

    val mAdManagerInterstitialAd = loadedInterstitialAds[adUnitId]

    if (mAdManagerInterstitialAd == null) {
        loadInterstitial(
            context = context,
            adUnitId = adUnitId,
            onAdFailedToLoad = onAdFailedToShowFullScreenContent
        ) {
            showInterstitial(
                context = context,
                adUnitId = adUnitId,
                onAdClicked = onAdClicked,
                onAdFailedToShowFullScreenContent = onAdFailedToShowFullScreenContent,
                onAdImpression = onAdImpression,
                onAdShowedFullScreenContent = onAdShowedFullScreenContent,
                onAdDismissedFullScreenContent = onAdDismissedFullScreenContent,
            )
        }
    } else if (activity != null) {
        mAdManagerInterstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when ad is clicked.
                Log.d(TAG, "Ad clicked.")
                onAdClicked()
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                loadedInterstitialAds[adUnitId] = null
                loadInterstitial(context = context, adUnitId = adUnitId)
                onAdDismissedFullScreenContent()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                loadedInterstitialAds[adUnitId] = null
                loadInterstitial(context = context, adUnitId = adUnitId)
                onAdFailedToShowFullScreenContent(p0)
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
                onAdImpression()
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
                onAdShowedFullScreenContent()
            }
        }
        mAdManagerInterstitialAd.show(activity)
    }
}

fun loadInterstitial(
    context: Context,
    adUnitId: AdUnitId,
    onAdFailedToLoad: (LoadAdError) -> Unit = {},
    onAdLoaded: (AdManagerInterstitialAd) -> Unit = {},
) {
    val mAdManagerInterstitialAd = loadedInterstitialAds[adUnitId]
    if(mAdManagerInterstitialAd != null) {
        return onAdLoaded(mAdManagerInterstitialAd)
    }
    AdManagerInterstitialAd.load(context,
        adUnitId,
        AdManagerAdRequest.Builder().build(),
        object : AdManagerInterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, "Ad failed to load: ${adError}")
                loadedInterstitialAds[adUnitId] = null
                onAdFailedToLoad(adError)
            }

            override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                loadedInterstitialAds[adUnitId] = interstitialAd
                onAdLoaded(interstitialAd)
            }
        })
}
//
//private var mAdManagerInterstitialAd: AdManagerInterstitialAd? = null
//fun loadInterstitial(
//    context: Context,
//    onAdFailedToLoad: (LoadAdError) -> Unit = {},
//    onAdLoaded: (AdManagerInterstitialAd) -> Unit = {},
//) {
//    AdManagerInterstitialAd.load(context,
//        context.getString(R.string.ad_mob_interstitial_id),
//        AdManagerAdRequest.Builder().build(),
//        object : AdManagerInterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.d(TAG, "Ad failed to load: ${adError}")
//                mAdManagerInterstitialAd = null
//                onAdFailedToLoad(adError)
//            }
//
//            override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
//                Log.d(TAG, "Ad was loaded.")
//                mAdManagerInterstitialAd = interstitialAd
//                onAdLoaded(interstitialAd)
//            }
//        })
//}