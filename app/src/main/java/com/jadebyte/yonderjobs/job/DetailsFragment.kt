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
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jadebyte.yonderjobs.R
import com.jadebyte.yonderjobs.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : BaseFragment(), OnMapReadyCallback {
    private lateinit var job: Job
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            job = arguments!!.getParcelable(JOB)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapJobFrag) as  SupportMapFragment
        mapFragment.getMapAsync(this)
        setupViews()
    }

    private fun setupViews() {
        setLeftDrawable(timeStamp, R.drawable.ic_access_time_white_24dp)
        val formattedDate = DateUtils.getRelativeTimeSpanString(job.timeStamp, System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString()
        timeStamp.text = getString(R.string.posted_by_x, formattedDate)
        company.text = job.compName
        jobType.text = job.type
        numToHire.text = job.num
        technologies.text = job.techs
        experienceLevel.text = job.experience
        salaryOption.text = job.salary
        workingArrangement.text = job.workArrangement
        extraInfo.text = if (job.info.isNullOrEmpty()) getString(R.string.none) else job.info
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        // Add a marker in Lagos and move the camera
        val location = LatLng(job.lat, job.long)
        map?.addMarker(MarkerOptions().position(location).title(job.city))
        map?.moveCamera(CameraUpdateFactory.newLatLng(location))
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
    }

    companion object {
        private const val JOB = "job"
        fun newInstance(job: Job): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putParcelable(JOB, job)
            fragment.arguments = args
            return fragment
        }
    }
}
