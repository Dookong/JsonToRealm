package dk.jsontorealm

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.amn.easysharedpreferences.EasySharedPreference
import io.realm.Realm
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection.HTTP_OK
import java.net.MalformedURLException


@Suppress("DEPRECATION")
class Task(val context: Context) : AsyncTask<String, Void, String>() {
    val apiKey = "zLBEvakblaPD4nxcX2BZEbKLy2jVRUlynBfGMVMfHPsehsI3NsNNn%2BLJwS7C6Jdz0g1m7GYx6y%2FVPCQ8KVYgKg%3D%3D"
    val xPos = 127.0331892
    val yPos = 37.5585146
    val radius = 5000

    lateinit var realm: Realm
    lateinit var progressDialog: ProgressDialog

    override fun onPreExecute() {
        super.onPreExecute()
        realm = Realm.getDefaultInstance()

        progressDialog = ProgressDialog(context)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("초기 DB를 구성하는 중입니다..")
        progressDialog.show()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        jsonToRealm(result!!)

        if(progressDialog.isShowing)
            progressDialog.dismiss()

        EasySharedPreference.putBoolean("needDB", false)
    }

    override fun doInBackground(vararg p0: String?): String? {
        var receiveMsg: String? = null
        val url: URL?

        try{
            url = URL("http://apis.data.go.kr/B551182/hospInfoService/getHospBasisList?ServiceKey=$apiKey&xPos=$xPos&yPos=$yPos&radius=$radius&numOfRows=3671&_type=json")

            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")

            if (conn.responseCode == HTTP_OK) {
                val tmp = InputStreamReader(conn.inputStream, "UTF-8")
                val reader = BufferedReader(tmp)
                val buffer = StringBuffer()
                var str: String?
                do{
                    str = reader.readLine()
                    buffer.append(str)
                }
                while (str != null)

                receiveMsg = buffer.toString()
                Log.i("receiveMsg : ", receiveMsg)

                reader.close()
            } else {
                Log.i("통신 결과", conn.responseCode.toString() + "에러")
            }
        }catch (e: MalformedURLException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return receiveMsg
    }

    private fun jsonToRealm(jsonString: String) {
        try {
            val jarray = JSONObject(jsonString)
                .getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items")
                .getJSONArray("item")

            realm.executeTransaction {
                it.createAllFromJson(Model::class.java, jarray)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}