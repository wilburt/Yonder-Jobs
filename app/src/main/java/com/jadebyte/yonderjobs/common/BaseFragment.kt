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

import android.graphics.PorterDuff
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

/**
 * Created by Wilberforce on 2/25/18 at 10:30 AM.
 */
open class BaseFragment : Fragment() {

    fun applyFont(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val view1 = viewGroup.getChildAt(i)
            if (view1 is TextView) {
                view1.typeface = App.customFont
            } else if (view1 is ViewGroup) {
                applyFont(view1)
            }
        }
    }

    fun applyFont(alertDialog: AlertDialog) {
        // Getting the view elements
        val textView = alertDialog.window.findViewById(android.R.id.message) as TextView
        val alertTitle = alertDialog.window.findViewById(R.id.alertTitle) as TextView
        val button1 = alertDialog.window.findViewById(android.R.id.button1) as Button
        val button2 = alertDialog.window.findViewById(android.R.id.button2) as Button

        // Setting font
        textView.typeface = App.customFont
        alertTitle.typeface = App.customFont
        button1.typeface = App.customFont
        button2.typeface = App.customFont
    }

    fun tintProgressBar(progressBar: ProgressBar) {
        progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
    }


    fun setLeftDrawable(textView: TextView, drawableRes: Int) {
        val drawableCompat = ResourcesCompat.getDrawable(resources, drawableRes, activity!!.theme)
        textView.setCompoundDrawablesWithIntrinsicBounds(drawableCompat, null, null, null)
    }
}