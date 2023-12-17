package com.bangkit.upcycle.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit.upcycle.MainActivity
import com.bangkit.upcycle.R
import com.bangkit.upcycle.ViewModelFactory
import com.bangkit.upcycle.databinding.ActivityLoginBinding
import com.bangkit.upcycle.databinding.ActivitySignUpBinding
import com.bangkit.upcycle.preferences.User
import com.bangkit.upcycle.register.SignUpActivity
import com.bangkit.upcycle.register.SignUpViewModel
import com.bangkit.upcycle.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        binding.signup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            viewModel.isLoading.observe(this) {
                showLoading(it)
            }

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty()) {
                binding.emailEditText.error = getString(R.string.email_empty)
            } else if (password.isEmpty()) {
                binding.passwordEditText.error = getString(R.string.password_empty)
            } else {
                viewModel.login(email, password)

                viewModel.loginResponse.observe(this) { loginResponse ->
                    loginResponse?.token?.let { token ->
                        save(User(token, true))
                    }
                }
            }
        }
    }

    private fun save(session: User) {
        lifecycleScope.launch {
            viewModel.saveSession(session)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ViewModelFactory.clearInstance()
            startActivity(intent)
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !isLoading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}