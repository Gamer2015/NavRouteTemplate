package at.stefan_kreiner.apps.collection_album_manager.ui.ads

import android.util.Log
import androidx.activity.ComponentActivity
import at.stefan_kreiner.apps.collection_album_manager.ui.composables.loadAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

private const val TAG = "AdMobActivity"

open class AdMobActivity : ComponentActivity() {
    protected lateinit var consentInformation: ConsentInformation
    protected lateinit var consentForm: ConsentForm

    fun prepareAdMob(
        requestConfiguration : RequestConfiguration = RequestConfiguration.Builder().build(),
        consentRequestParameters : ConsentRequestParameters = ConsentRequestParameters.Builder().build(),
    ) {
        // Set tag for under age of consent. false means users are not under age.
        Log.d(TAG, "initialize ad mob")
        MobileAds.initialize(this)
        MobileAds.setRequestConfiguration(requestConfiguration)

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(this, consentRequestParameters, {
            // The consent information state was updated.
            // You are now ready to check if a form is available.
            Log.d(TAG, "Form available")
            if (consentInformation.isConsentFormAvailable) {
                loadConsentForm()
                Log.d(TAG, "Form available")
            }
        }, {
            // Handle the error.
            Log.d(TAG, "ConsentInfoUpdateRequest failed: ${it.message}")
        })
    }

    private fun loadConsentForm() {
        // Loads a consent form. Must be called on the main thread.
        UserMessagingPlatform.loadConsentForm(this, {
            this.consentForm = it
            if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                consentForm.show(this) {
                    Log.d("Consent", "form error: ${it.toString()}")
                    Log.d(
                        "Consent",
                        "consentInformation.consentStatus: ${consentInformation.consentStatus}"
                    )
                    Log.d(
                        "Consent",
                        "consentInformation.isConsentFormAvailable: ${consentInformation.isConsentFormAvailable}"
                    )
                    if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.OBTAINED) {
                        // App can start requesting ads.
                        Log.d("Consent", "agreed")
                        loadAds(this)
                    } else {
                        // Handle dismissal by reloading form.
                        Log.d("Consent", "dismissed")
                        loadConsentForm()
                    }
                }
            }
        }, {
            // Handle the error.
            Log.d("Consent", "consent request error: ${it.message}")
        })
    }
}