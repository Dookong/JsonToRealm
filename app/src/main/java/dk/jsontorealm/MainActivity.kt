package dk.jsontorealm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.amn.easysharedpreferences.EasySharedPreference
import io.realm.Realm


class MainActivity : AppCompatActivity() {

    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()

        if(EasySharedPreference.getBoolean("needDB", true))
            Task(this).execute()
        check()
    }

    private fun check(){
        val str = realm.where(Model::class.java)
            .equalTo("clCdNm", "상급종합")
            .findFirst()?.yadmNm

        val str2 = realm.where(Model::class.java)
            .equalTo("clCdNm", "한의원")
            .findFirst()?.yadmNm

        Toast.makeText(this, "상급종합 병원: $str\n한의원: $str2", Toast.LENGTH_SHORT).show()
    }
}
