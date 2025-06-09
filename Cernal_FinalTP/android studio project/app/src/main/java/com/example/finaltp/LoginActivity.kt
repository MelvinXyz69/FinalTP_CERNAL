// LoginActivity.kt
package com.example.finaltp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class LoginActivity : AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameField = findViewById(R.id.usernameInput)
        passwordField = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            loginUser(usernameField.text.toString(), passwordField.text.toString())
        }
    }

    private fun loginUser(username: String, password: String) {
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://192.168.0.16:8000/api/login/") // use your local network IP
            .post(requestBody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle network error
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val json = JSONObject(body ?: "{}")
                    val token = json.getString("token")

                    // ✅ Save the token to SharedPreferences
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                    sharedPreferences.edit().putString("auth_token", token).apply()

                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Login Success!", Toast.LENGTH_SHORT).show()

                        // ✅ Move to InboxActivity
                        val intent = Intent(this@LoginActivity, InboxActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Login Failed: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })


    }
}
