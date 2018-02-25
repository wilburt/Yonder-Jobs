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

package com.jadebyte.yonderjobs


import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.jadebyte.yonderjobs.common.App
import com.jadebyte.yonderjobs.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_post_job.*




class PostJobFragment : BaseFragment(), View.OnClickListener {

    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var placeArrayAdapter: PlaceArrayAdapter
    private var locationLatLng: LatLng? = null
    private var job: Job? = null
    private var jobPostedListener: OnJobPostedListener? = null

    companion object {
        fun newInstance(): PostJobFragment {
            return PostJobFragment()
        }

        private const val GOOGLE_API_CLIENT_ID = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTechIcon.setOnClickListener(this)
        postButton.setOnClickListener(this)
        setupCityEditText()
        applyFont(view as ViewGroup)
    }

    override fun onStart() {
        super.onStart()
        googleApiClient = GoogleApiClient.Builder(activity!!)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(activity!!, GOOGLE_API_CLIENT_ID, connectionFailedListener)
                .addConnectionCallbacks(connectionCallbacks)
                .build()
    }

    private fun setupCityEditText() {
        val filter = AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build()

        cityEditText.threshold = 3
        cityEditText.onItemClickListener = autoCompleteClickListener
        cityEditText.setLoadingIndicator(progressBar)

        placeArrayAdapter = PlaceArrayAdapter(activity!!, R.layout.location_list_item, filter)

        cityEditText.setAdapter(placeArrayAdapter)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.addTechIcon -> {
                val view = LayoutInflater.from(activity).inflate(R.layout.technology_container, null, false)
                ((view as ViewGroup).getChildAt(0) as TextView).typeface = App.customFont
                addedTechnologies.addView(view)
            }

            R.id.postButton -> {
                if (allFieldsAreValid()) {
                    postJob()
                }
            }
        }
    }

    private fun postJob() {
        val dbRef = FirebaseDatabase.getInstance().reference.child("Jobs")
        // Create a new node and get the key
        val  newKey = dbRef.push().key
        dbRef.child(newKey).setValue(job)
        jobPostedListener?.onJobPosted()
    }

    private fun allFieldsAreValid(): Boolean {
        val compName = companyValue.text.toString()
        if (compName.isEmpty()) {
            showError(companyValue, R.string.company_s_name)
            return false
        }

        val city = cityEditText.text.toString()

        if (locationLatLng == null && city.isEmpty()) {
            showError(cityEditText, R.string.city)
            return false
        }

        val email = companyEmail.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(companyEmail, R.string.company_s_email)
            return false
        }

        if (jobTypesSpinner.selectedItemPosition == 0) {
            showError(null, R.string.who_do_you_want_to_hire)
            return false
        }
        val jobType = jobTypesSpinner.selectedItem.toString()

        val num = numToHire.text.toString()
        if (num.isEmpty()) {
            showError(numToHire, R.string.how_many_do_you_want_to_hire)
            return false
        }

        val techs = getTechs()
        if (techs.isEmpty()) {
            showError(((firstTech as ViewGroup).getChildAt(0) as EditText), R.string.technologies)
            return false
        }

        if (levelSpinner.selectedItemPosition == 0) {
            showError(null, R.string.experience_level)
            return false
        }
        val experience = levelSpinner.selectedItem.toString()

        if (salaryOptionSpinner.selectedItemPosition == 0) {
            showError(null, R.string.salary_option)
            return false
        }
        val salary = salaryOptionSpinner.selectedItem.toString()

        if (workingArrangementSpinner.selectedItemPosition == 0) {
            showError(null, R.string.working_arrangement)
            return false
        }
        val workingArrangement = workingArrangementSpinner.selectedItem.toString()

        val extraInfo =  extraInfo.text.toString()

        job = Job(compName, jobType, num, techs, experience, salary, workingArrangement, locationLatLng!!.latitude, locationLatLng!!.longitude,
                extraInfo, city, email)
        return true
    }


    private fun getTechs(): String {
        val builder = StringBuilder()
        for (i in 0 until addedTechnologies.childCount) {
            val viewGroup = addedTechnologies.getChildAt(i) as ViewGroup
            val editText = viewGroup.getChildAt(0) as EditText
            val feature = editText.text.toString()

            //Don't add empty tech
            if (!TextUtils.isEmpty(feature)) {
                builder.append(feature)
                // Making sure the comma is not added after the last feature
                if (i != addedTechnologies.childCount - 1) {
                    builder.append(",")
                }
            }
        }
        return builder.toString()
    }

    private fun showError(editText: EditText?, stringRes: Int) {
        val errorStr = getString(R.string.please_enter_x, getString(stringRes))
        if (editText != null) {
            editText.error = errorStr
            val scrollTo = (editText.parent as View).top + editText.top
            rootView.smoothScrollTo(0, scrollTo)

        } else {
            Snackbar.make(rootView, errorStr, Snackbar.LENGTH_SHORT).show()
        }
    }

    private val connectionFailedListener = GoogleApiClient.OnConnectionFailedListener {
        cityEditText.setText("")
        Snackbar.make(rootView, R.string.error_getting_suggestion, Snackbar.LENGTH_SHORT).show() }

    private val connectionCallbacks = object: GoogleApiClient.ConnectionCallbacks {
        override fun onConnected(p0: Bundle?) {
            placeArrayAdapter.setGoogleApiClient(googleApiClient)
        }

        override fun onConnectionSuspended(p0: Int) {
            cityEditText.setText("")
            placeArrayAdapter.setGoogleApiClient(null)
        }
    }


    private val autoCompleteClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        val item = placeArrayAdapter.getItem(position)
        val placeId = item!!.placeId.toString()

        val placeResult = Places.GeoDataApi
                .getPlaceById(googleApiClient, placeId)
        placeResult.setResultCallback(placeDetailsCallback)
    }

    private val placeDetailsCallback: ResultCallback<in PlaceBuffer> = ResultCallback {

        if (it.status.isSuccess && it.count > 0) {

            // Selecting the first object buffer.
            val place = it.get(0)
            locationLatLng = place.latLng

        } else {
           Snackbar.make(rootView, R.string.error_getting_loc_details, Snackbar.LENGTH_SHORT).show()
            cityEditText.setText("")
        }

        it.release()
    }

    override fun onPause() {
        super.onPause()
        googleApiClient.stopAutoManage(activity!!)
        googleApiClient.disconnect()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnJobPostedListener) {
            jobPostedListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnJobPostedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        jobPostedListener = null
    }

    interface OnJobPostedListener {
        fun onJobPosted()
    }
}
