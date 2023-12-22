package com.bangkit.upcycle.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.bangkit.upcycle.MainActivity
import com.bangkit.upcycle.R
import com.bangkit.upcycle.login.LoginActivity
import com.bangkit.upcycle.preferences.UserPreferences
import com.bangkit.upcycle.preferences.dataStore
import com.bangkit.upcycle.register.SignUpActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {
    private lateinit var pref : UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<Button>(R.id.signupButton)
        pref = UserPreferences.getInstance(this.dataStore)

        lifecycleScope.launch {
            val user = pref.getTokenKey().first()

            if (user.isLogin) {
                val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {

            }
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}