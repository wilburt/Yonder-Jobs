/*
 * Copyright (c) 2018 Wilberforce Uwadiegwu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jadebyte.yonderjobs.intro

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.jadebyte.yonderjobs.GlideApp
import com.jadebyte.yonderjobs.R
import com.jadebyte.yonderjobs.common.App
import com.jadebyte.yonderjobs.common.BaseActivity
import com.jadebyte.yonderjobs.job.MainActivity
import com.jadebyte.yonderjobs.utils.Constants
import com.jadebyte.yonderjobs.utils.Utils
import kotlinx.android.synthetic.main.activity_intro.*
import timber.log.Timber

class IntroActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        val isFirstLaunch = App.sharedPreferences.getBoolean(Constants.Keys.IS_FIRST_LAUNCH, true)
        Timber.i("onCreate: %s", isFirstLaunch)
        if (!isFirstLaunch) {
            startMainActivity()
            return
        }
        setContentView(R.layout.activity_intro)

        GlideApp.with(this)
                .load(R.drawable.vancouver)
                .centerCrop()
                .into(introBgImg)

        applyFont(rootView)

        // Make the pager items peep-able
        val paddingVertical = Utils.dpToPx(30)
        val paddingHorizontal = Utils.dpToPx(15)
        introPager.clipToPadding = false
        introPager.setPadding(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal)
        introPager.pageMargin = Utils.dpToPx(15)

        introPager.addOnPageChangeListener(pageChangeListener)
        introPager.adapter = IntroAdapter()

        doneButton.setOnClickListener(this)

    }

    private val pageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(scrollState: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(page: Int) {
            when (page) {
                0, 1 -> {
                    doneButton.visibility = View.GONE
                    introIndicator.visibility = View.VISIBLE
                }

                2 -> {
                    introIndicator.visibility = View.GONE
                    doneButton.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.doneButton -> {
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        App.sharedPreferences.edit().putBoolean(Constants.Keys.IS_FIRST_LAUNCH, false).apply()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        introPager?.removeOnPageChangeListener(pageChangeListener)
    }
}
