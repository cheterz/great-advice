package com.cheterz.fuckinggreatadvice

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

private const val URL = "https://fucking-great-advice.ru/api/random"

class MainActivity : AppCompatActivity() {

    private var adviceText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        run(URL)
        tv_advice.setOnClickListener {
            run(URL)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sharing, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_share -> {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "F***ing Great Advice:\n" + adviceText)
                startActivity(shareIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun run(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val advice = response?.body?.string()
                if (advice != null) {
                    val gson = GsonBuilder().create()
                    val adviceFeed = gson.fromJson(advice, Advice::class.java)
                    this@MainActivity.runOnUiThread {
                        adviceText = adviceFeed?.text ?: ""
                        this@MainActivity.tv_advice.text = adviceFeed?.text ?: ""


                    }
                } else (this@MainActivity.runOnUiThread {
                    this@MainActivity.tv_advice.setText(R.string.advice)
                })
            }

            override fun onFailure(call: Call, e: IOException) {
                tv_advice.setText(R.string.advice)
            }
        })
    }
}

data class Advice(
    val id: String,
    val text: String,
    val sound: String
)
