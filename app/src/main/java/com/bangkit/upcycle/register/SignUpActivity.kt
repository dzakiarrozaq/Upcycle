package com.bangkit.upcycle.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.upcycle.R
import com.bangkit.upcycle.ViewModelFactory
import com.bangkit.upcycle.databinding.ActivitySignUpBinding
import com.bangkit.upcycle.login.LoginActivity
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

        binding.signupButton.setOnClickListener{
            register()
        }
    }

    private fun register() {
        Log.d("Tes","Proses Register Dijalankan dengan baik ")
        if(binding.tvRegisterName.text!!.isEmpty()){
            binding.tvRegisterName.error = "Kolom Nama Tidak Boleh Kosong"
            binding.tvRegisterName.requestFocus()
            return
        } else if(binding.tvRegisterEmail.text!!.isEmpty()) {
            binding.tvRegisterEmail.error = "Kolom Email Tidak Boleh Kosong"
            binding.tvRegisterEmail.requestFocus()
            return
        }else if(binding.tvRegisterPassword.text!!.isEmpty()) {
            binding.tvRegisterPassword.error = "Kolom Password Tidak Boleh Kosong"
            binding.tvRegisterPassword.requestFocus()
            return
        }

        val name = binding.tvRegisterName.text.toString()
        val email = binding.tvRegisterEmail.text.toString()
        val password = binding.tvRegisterPassword.text.toString()

        lifecycleScope.launch {
            try {
                val response = viewModel.register(name, email, password)
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
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                showToast(errorMessage.toString())
                showLoading(false)
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBarRegister.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.signupButton.isEnabled = !isLoading
    }
}