package com.bangkit.upcycle.profil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.bangkit.upcycle.R
import com.bangkit.upcycle.apii.ApiService
import com.bangkit.upcycle.login.LoginActivity
import com.bangkit.upcycle.preferences.UserPreferences
import com.bangkit.upcycle.repository.UserRepository
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var userPreferences: UserPreferences

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfilFragment(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences // Add userPreferences parameter
) : Fragment() {// TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        // Find the logout button by its ID
        val btnLogOut = view.findViewById<AppCompatButton>(R.id.btnLogOut)

        // Set an OnClickListener for the logout button
        btnLogOut.setOnClickListener {
            // Call the logout function from UserRepository
            lifecycleScope.launch {
                try {
                    UserRepository.getInstance(apiService, userPreferences).logout()

                    // Explicitly start the SignInActivity
                    val signInIntent = Intent(requireContext(), LoginActivity::class.java)
                    signInIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(signInIntent)

                } catch (e: Exception) {
                    // Handle logout failure, if needed
                    Log.e("ProfilFragment", "Logout failed: ${e.message}")
                }
            }
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfilFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(apiService: ApiService, userPreferences: UserPreferences) =
            ProfilFragment(apiService, userPreferences).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}