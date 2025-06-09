package com.example.finaltp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finaltp.ChatMessage
import com.example.finaltp.MessageAdapter
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.net.HttpURLConnection
import java.net.URL




class ContactActivity : AppCompatActivity() {

    private lateinit var messageList: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        messageList = findViewById(R.id.messageList)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        adapter = MessageAdapter(messages)
        messageList.layoutManager = LinearLayoutManager(this)
        messageList.adapter = adapter

        sendButton.setOnClickListener {
            val content = messageInput.text.toString()
            if (content.isNotEmpty()) {
                sendMessageToApi(content)
                messageInput.text.clear()
            }
        }
    }

    private fun sendMessageToApi(content: String) {
        val thread = Thread {
            try {
                val url = URL("http://10.0.2.2:8000/api/messages/") // or your Django IP
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonBody = JSONObject()
                jsonBody.put("content", content)

                val outputStream = BufferedOutputStream(connection.outputStream)
                outputStream.write(jsonBody.toString().toByteArray())
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread {
                        messages.add(ChatMessage("You", content, "now"))
                        adapter.notifyItemInserted(messages.size - 1)
                        messageList.scrollToPosition(messages.size - 1)
                    }
                }

                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }
        }
        thread.start()
    }
}
