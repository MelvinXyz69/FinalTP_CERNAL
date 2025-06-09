package com.example.finaltp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var homeButton: Button
    private lateinit var aboutButton: Button
    private lateinit var contactButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeButton = findViewById(R.id.btn_home)
        aboutButton = findViewById(R.id.btn_about)
        contactButton = findViewById(R.id.btn_contact)

        homeButton.setOnClickListener {
            Toast.makeText(this, "Home clicked!", Toast.LENGTH_SHORT).show()
        }

        aboutButton.setOnClickListener {
            Toast.makeText(this, "About clicked!", Toast.LENGTH_SHORT).show()
        }

        val loginButton: Button = findViewById(R.id.btn_login)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        contactButton.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }

        val inboxButton = findViewById<Button>(R.id.btn_inbox)
        inboxButton.setOnClickListener {
            val intent = Intent(this, InboxActivity::class.java)
            startActivity(intent)
        }

    }
}
