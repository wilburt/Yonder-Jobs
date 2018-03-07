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

package com.jadebyte.yonderjobs.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.StrictMode
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatDelegate
import com.google.firebase.database.FirebaseDatabase
import com.jadebyte.yonderjobs.BuildConfig
import com.jadebyte.yonderjobs.R
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import timber.log.Timber

/**
 * Created by Wilberforce on 2/20/18 at 3:07 PM.
 */
class App : Application() {


    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        refWatcher = LeakCanary.install(this)
        appContext = applicationContext
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext)

        val customFontStr = getString(R.string.customFont)
        val customFontBoldStr = getString(R.string.customFontBold)
        customFont = Typeface.createFromAsset(assets, customFontStr)
        customFontBold = Typeface.createFromAsset(assets, customFontBoldStr)

        enableStrictMode()
    }

    private fun enableStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())

            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }
    }



    companion object {

        lateinit var sharedPreferences: SharedPreferences
            private set
        lateinit var appContext: Context
            private set
        lateinit var refWatcher: RefWatcher
            private set
        lateinit var customFont: Typeface
            private set
        lateinit var customFontBold: Typeface
            private set

    }

}
