package com.cheterz.fuckinggreatadvice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

private const val URL = "https://fucking-great-advice.ru/api/random"

class MainActivity : AppCompatActivity() {

    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        run(URL)
        tv_advice.setOnClickListener {
            run(URL)
        }


    }

    fun run(url: String) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val advice = response?.body?.string() ?: R.string.advice.toString()
                val gson = GsonBuilder().create()
                val adviceFeed = gson.fromJson(advice, Advice::class.java)
                this@MainActivity.runOnUiThread {
                    this@MainActivity.tv_advice.text = adviceFeed.text
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                tv_advice.text = R.string.advice.toString()
            }
        })

    }
}

data class Advice(
    val id: String,
    val text: String,
    val sound: String
)
