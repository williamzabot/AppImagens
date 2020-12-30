package com.williamzabot.appimagens.ui.main

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.williamzabot.appimagens.R
import com.williamzabot.appimagens.ui.imagens.ImagensFragment
import com.williamzabot.appimagens.ui.main.viewpager.ViewPagerAdapter
import com.williamzabot.appimagens.ui.upload.UploadFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewPager = findViewById(R.id.viewPager_fotos)
        tabLayout = findViewById(R.id.tablayout_fotos)
        initViewPager()
    }

    private fun initViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(UploadFragment(), "Upload Imagens")
        viewPagerAdapter.addFragment(ImagensFragment(), "Lista Imagens")
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }


}