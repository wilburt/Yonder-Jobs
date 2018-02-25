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
import android.os.Handler
import android.os.Message
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar

/**
 * Created by Wilberforce on 2/24/18 at 5:34 PM.
 */

class DelayAutoCompleteTextView(context: Context, attrs: AttributeSet) : AppCompatAutoCompleteTextView(context, attrs) {

    private var autocompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY
    private var progressBar: ProgressBar? = null

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super@DelayAutoCompleteTextView.performFiltering(msg.obj as CharSequence, msg.arg1)
        }
    }

    fun setLoadingIndicator(progressBar: ProgressBar) {
        this.progressBar = progressBar
    }

    fun setAutoCompleteDelay(autoCompleteDelay: Int) {
        autocompleteDelay = autoCompleteDelay
    }

    override fun performFiltering(text: CharSequence, keyCode: Int) {
        if (progressBar != null) {
            progressBar!!.visibility = View.VISIBLE
        }
        handler.removeMessages(MESSAGE_TEXT_CHANGED)
        handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_TEXT_CHANGED, text), autocompleteDelay.toLong())
    }

    override fun onFilterComplete(count: Int) {
        if (progressBar != null) {
            progressBar?.visibility = View.GONE
        }
        super.onFilterComplete(count)
    }

    companion object {

        private const val MESSAGE_TEXT_CHANGED = 100
        private const val DEFAULT_AUTOCOMPLETE_DELAY = 750
    }

}