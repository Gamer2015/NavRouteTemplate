package at.stefan_kreiner.apps.collection_album_manager.ui.composables

import android.content.Context
import at.stefan_kreiner.apps.collection_album_manager.R

private val TAG = "Ads"

typealias AdUnitId = String

fun loadAds(context: Context) {
    loadInterstitial(context, context.getString(R.string.ad_mob_interstitial_id__edit_and_navigate_up))
}