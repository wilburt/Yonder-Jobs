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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jadebyte.yonderjobs.R
import com.jadebyte.yonderjobs.common.BaseFragment

/**
 * Created by Wilberforce on 2/25/18 at 4:16 PM.
 */
class JobsAdapter(private val jobItemCallbacks: JobItemCallbacks, private val fragment: BaseFragment) : RecyclerView.Adapter<JobsViewHolder>() {
    private var jobsList: List<Job>? = null

    fun updateJobs(jobsList: List<Job>?) {
        this.jobsList = jobsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): JobsViewHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_job, parent, false)
        return JobsViewHolder(view, fragment, jobItemCallbacks)
    }

    override fun getItemCount(): Int {
        return jobsList?.size ?: 0
    }

    override fun onBindViewHolder(holder: JobsViewHolder?, position: Int) {
        if (jobsList != null) {
            holder!!.bindModel(jobsList!![position])
        }
    }

}