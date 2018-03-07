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

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuItem
import com.jadebyte.yonderjobs.R
import com.jadebyte.yonderjobs.common.BaseActivity
import com.jadebyte.yonderjobs.common.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), PostJobFragment.OnJobPostedListener, JobsFragment.OnJobListCallbacks {

    private var job: Job? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment: BaseFragment? = null
        when (item.itemId) {
            R.id.navigation_jobs -> {
                fragment = JobsFragment.newInstance()
            }
            R.id.navigation_post -> {
               fragment = PostJobFragment.newInstance()
            }

        }
        if (fragment == null) {
            return@OnNavigationItemSelectedListener false
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.fragListContainer, fragment).commitAllowingStateLoss()
            return@OnNavigationItemSelectedListener true
        }

    }

    private fun selectNavItem(id: Int) {
        val jobsItem = navigation.menu.findItem(id)
        onNavigationItemSelectedListener.onNavigationItemSelected(jobsItem)
        jobsItem.isChecked = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        applyFont(rootView)
        selectNavItem(R.id.navigation_jobs)
    }

    override fun onJobPosted() {
        selectNavItem(R.id.navigation_jobs)
    }

    override fun onJobClicked(job: Job) {
        this.job = job
        val isDoublePane = resources.getBoolean(R.bool.isDoublePane)
        if (isDoublePane) {
            supportFragmentManager.beginTransaction().replace(R.id.fragDetContainer, DetailsFragment.newInstance(job)).commit()

        } else {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.JOB, job)
            startActivity(intent)
        }
    }

    override fun onPostJobClick() {
        selectNavItem(R.id.navigation_post)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val isDoublePane = resources.getBoolean(R.bool.isDoublePane)
        menuInflater.inflate(R.menu.details, menu)
        return isDoublePane
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_apply -> {
                applyToJob(job)
            }
        }
        return true
    }
}
