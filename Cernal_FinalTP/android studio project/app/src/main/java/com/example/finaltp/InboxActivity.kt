// InboxActivity.kt
package com.example.finaltp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class InboxActivity : AppCompatActivity() {

    private lateinit var inboxList: RecyclerView
    private lateinit var inboxAdapter: MessageAdapter
    private val messages = mutableListOf<ChatMessage>()
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        inboxList = findViewById(R.id.inboxRecyclerView)
        inboxAdapter = MessageAdapter(messages)
        inboxList.layoutManager = LinearLayoutManager(this)
        inboxList.adapter = inboxAdapter

        fetchMessages()
    }

    private fun fetchMessages() {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token == null) {
            Toast.makeText(this, "No token found. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        val request = Request.Builder()
            .url("http://192.168.0.16:8000/api/messages/")
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@InboxActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val jsonArray = JSONArray(responseData)

                    messages.clear()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val sender = obj.getString("sender")
                        val content = obj.getString("content")
                        val timestamp = obj.getString("timestamp")

                        messages.add(ChatMessage(sender, content, timestamp))
                    }

                    runOnUiThread {
                        inboxAdapter.notifyDataSetChanged()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@InboxActivity, "Failed to load messages: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }


}
