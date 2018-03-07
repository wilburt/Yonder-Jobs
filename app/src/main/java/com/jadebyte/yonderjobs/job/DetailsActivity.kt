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

package com.jadebyte.yonderjobs.job

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.jadebyte.yonderjobs.R
import com.jadebyte.yonderjobs.common.BaseActivity
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : BaseActivity() {

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val isDoublePane = resources.getBoolean(R.bool.isDoublePane)
        if (isDoublePane) {
            finish()
            return
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        applyFont(rootView)
        job = intent.getParcelableExtra(JOB)
        supportFragmentManager.beginTransaction().replace(R.id.fragDetContainer, DetailsFragment.newInstance(job)).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_apply -> {
                applyToJob(job)
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val JOB = "JOB"
    }

}
