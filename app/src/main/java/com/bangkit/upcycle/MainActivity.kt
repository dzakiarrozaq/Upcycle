package com.bangkit.upcycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bangkit.upcycle.databinding.ActivityMainBinding
import com.bangkit.upcycle.home.HomeFragment
import com.bangkit.upcycle.profil.ProfilFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragment(HomeFragment())

        binding.navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> fragment(HomeFragment())
                R.id.profil -> fragment(ProfilFragment())

                else -> {

                }
            }
            true
        }
    }
    private fun fragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val transtionFragment = fragmentManager.beginTransaction()
        transtionFragment.replace(R.id.frameLayout,fragment)
        transtionFragment.commit()
    }
}