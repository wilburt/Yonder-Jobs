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

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jadebyte.yonderjobs.common.App
import com.jadebyte.yonderjobs.common.BaseFragment
import kotlinx.android.synthetic.main.item_job.view.*

/**
 * Created by Wilberforce on 2/25/18 at 4:02 PM.
 */
class JobsViewHolder(itemView: View, fragment: BaseFragment, private val jobItemCallbacks: JobItemCallbacks) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
    init {
        this.itemView.itemContainer.setOnClickListener(this)
        applyFont(itemView as ViewGroup)
        fragment.setLeftDrawable(this.itemView.location, R.drawable.ic_location_on_red_24dp)
        fragment.setLeftDrawable(this.itemView.timeStamp, R.drawable.ic_access_time_grey_24dp)
    }

    fun bindModel(job: Job) {
        itemView.jobTitle.text = job.type
        itemView.companyValue.text = job.compName
        itemView.location.text = job.city

        val formattedDate = DateUtils.getRelativeTimeSpanString(job.timeStamp, System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString()
        itemView.timeStamp.text = formattedDate
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.itemContainer -> {
                jobItemCallbacks.onJobClick(adapterPosition)
            }
        }
    }

    private fun applyFont(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val view1 = viewGroup.getChildAt(i)
            if (view1 is TextView) {
                view1.typeface = if (view1.getId() == R.id.jobTitle) App.customFontBold else App.customFont
            } else if (view1 is ViewGroup) {
                applyFont(view1)
            }
        }
    }

}