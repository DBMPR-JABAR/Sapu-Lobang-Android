package id.go.jabarprov.dbmpr.surveisapulubang.app

import android.app.Application
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SurveySapuJalanApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ArcGISRuntimeEnvironment.setApiKey("AAPK2021e3c0ade243ac91fc03c5cc16af553UoLz7PP3cuznJsJw2hQOU6G-m47W2PWSfHujOs9JYI-UmZOtUw7TvgwWHUSIDPI")
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud2278956643,none,4N5X0H4AH5G0F5KHT053")
        FirebaseApp.initializeApp(this)
    }
}