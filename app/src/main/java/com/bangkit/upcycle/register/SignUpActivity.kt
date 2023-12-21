package com.bangkit.upcycle.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bangkit.upcycle.R
import com.bangkit.upcycle.ViewModelFactory
import com.bangkit.upcycle.databinding.ActivitySignUpBinding
import com.bangkit.upcycle.login.LoginActivity
import com.bangkit.upcycle.login.LoginViewModel
import com.bangkit.upcycle.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpActivity : AppCompatActivity() {

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener {
            register()
        }
        binding.signin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        viewModel.message.observe(this, Observer { message ->
            message?.let {
                runOnUiThread {
                    showLoading(false)
                    showToast("Registrasi berhasil: $it")
                }
                // Handle other UI actions if needed
            } ?: run {
                runOnUiThread {
                    showLoading(false)
                    Log.e("RegisterActivity", "Registrasi gagal: Message null")
                }
            }
        })
    }

    private fun register() {
        if (validateFields()) {
            val username = binding.NameEditText.text.toString()?.trim()
            val email = binding.emailEditText.text.toString()?.trim()
            val password = binding.passwordEditText.text.toString()?.trim()

            if (username.isNullOrBlank() || email.isNullOrBlank() || password.isNullOrBlank()) {
                showToast("Please fill in all fields.")
                return
            }

            lifecycleScope.launch {
                try {
                    val response = viewModel.registerUser(username!!, email!!, password!!)
                    Log.d("Tes","username:${username}, email:${email}, password: $password")
                    showLoading(false)
                    Log.d("RegisterActivity", "Registrasi berhasil: $response")
                    val message = "Registrasi berhasil: $response"
                    showToast(message)
                    AlertDialog.Builder(this@SignUpActivity).apply {
                        setTitle("Yei!")
                        setMessage(getString(R.string.register_succes))
                        setPositiveButton(getString(R.string.continuee)) { _, _ ->
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                } catch (e: Exception) {
                    val message = "Kesalahan saat registrasi: ${e.message}"
                    showToast(message)
                    showLoading(false)
                }
            }
        }
    }

    private fun validateFields(): Boolean {
        with(binding) {
            when {
                NameEditText.text.isNullOrBlank() -> {
                    tvRegisterName.error = "Kolom Nama Tidak Boleh Kosong"
                    tvRegisterName.requestFocus()
                    return false
                }
                emailEditText.text.isNullOrBlank() -> {
                    tvRegisterEmail.error = "Kolom Email Tidak Boleh Kosong"
                    tvRegisterEmail.requestFocus()
                    return false
                }
                passwordEditText.text.isNullOrBlank() -> {
                    tvRegisterPassword.error = "Kolom Password Tidak Boleh Kosong"
                    tvRegisterPassword.requestFocus()
                    return false
                }
                else -> return true
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarRegister.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.signupButton.isEnabled = !isLoading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}