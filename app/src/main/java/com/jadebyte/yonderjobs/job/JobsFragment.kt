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

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jadebyte.yonderjobs.R
import com.jadebyte.yonderjobs.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_jobs.*

class JobsFragment : BaseFragment(), View.OnClickListener, JobItemCallbacks {

    private var adapter: JobsAdapter? = null
    private val jobList: MutableList<Job> = ArrayList()
    private var onJobListCallbacks: OnJobListCallbacks? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jobs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyFont(rootView)
        tintProgressBar(progressBar)
        initViews()
        postButton.setOnClickListener(this)
        getJobs()
    }

    private fun initViews() {
        adapter = JobsAdapter(this, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    private fun getJobs() {
        infoContainer.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        val dbRef = FirebaseDatabase.getInstance().reference.child("Jobs")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

                jobList.clear()
                adapter?.updateJobs(null)
                setViewsVisibility()
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                jobList.clear()
                snapshot?.children?.forEach {
                    val job = it.getValue(Job::class.java)!!
                    jobList.add(job)
                }

                adapter?.updateJobs(jobList)

                setViewsVisibility()
            }
        })
    }

    private fun setViewsVisibility() {
        if (recyclerView == null) {
            return
        }
        progressBar.visibility = View.GONE
        if (jobList.count() == 0) {
            recyclerView.visibility = View.GONE
            infoContainer.visibility = View.VISIBLE

        } else {
            infoContainer.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }


    override fun onJobClick(position: Int) {
        onJobListCallbacks?.onJobClicked(jobList[position])
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.postButton -> {
                onJobListCallbacks?.onPostJobClick()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnJobListCallbacks) {
            onJobListCallbacks = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnJobListCallbacks")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onJobListCallbacks = null
    }

    interface OnJobListCallbacks {
        fun onJobClicked(job: Job)
        fun onPostJobClick()
    }

    companion object {
        fun newInstance(): JobsFragment {
            val fragment = JobsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
