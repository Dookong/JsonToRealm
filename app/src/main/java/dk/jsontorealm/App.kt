package dk.jsontorealm

import android.app.Application
import android.content.Context
import com.amn.easysharedpreferences.EasySharedPreferenceConfig
import io.realm.Realm

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        EasySharedPreferenceConfig.initDefault(EasySharedPreferenceConfig.Builder()
            .inputFileName("check_init")
            .inputMode(Context.MODE_PRIVATE)
            .build())
    }
}