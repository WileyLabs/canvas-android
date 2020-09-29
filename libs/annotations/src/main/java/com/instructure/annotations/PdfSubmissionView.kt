/*
 * Copyright (C) 2018 - present Instructure, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.instructure.annotations

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.instructure.canvasapi2.models.ApiValues
import com.instructure.canvasapi2.models.DocSession
import com.instructure.canvasapi2.models.canvadocs.CanvaDocAnnotation
import com.instructure.pandautils.utils.onClick
import com.instructure.pandautils.views.ProgressiveCanvasLoadingView
import java.util.*

@SuppressLint("ViewConstructor")
abstract class PdfSubmissionView(context: Context) : FrameLayout(context) {

    protected lateinit var docSession: DocSession
    protected lateinit var apiValues: ApiValues
    protected val commentRepliesHashMap: HashMap<String, ArrayList<CanvaDocAnnotation>> = HashMap()
    protected val supportFragmentManager: FragmentManager = (context as AppCompatActivity).supportFragmentManager

    @get:ColorRes
    abstract val progressColor: Int
    abstract val commentsButton: ImageView
    abstract val loadingContainer: FrameLayout
    abstract val progressBar: ProgressiveCanvasLoadingView

    abstract fun setFragment(fragment: Fragment)
    abstract fun showNoInternetDialog()
    abstract fun disableViewPager()
    abstract fun enableViewPager()
    abstract fun setIsCurrentlyAnnotating(boolean: Boolean)
    abstract fun showAnnotationComments(commentList: ArrayList<CanvaDocAnnotation>, headAnnotationId: String, docSession: DocSession, apiValues: ApiValues)
    abstract fun showFileError()

    open fun logOnAnnotationSelectedAnalytics() {}

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initializeSubmissionView()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unregisterPdfFragmentListeners()
    }

    protected fun unregisterPdfFragmentListeners() {
    }

    /**
     * THIS HAS TO BE CALLED
     */
    @Throws(UninitializedPropertyAccessException::class)
    protected fun initializeSubmissionView() {
        configureCommentView(commentsButton)
    }

    open fun configureCommentView(commentsButton: ImageView) {
        //we want to offset the comment button by the height of the action bar
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)
        val typedArray = context.obtainStyledAttributes(typedValue.resourceId, intArrayOf(android.R.attr.actionBarSize))
        val actionBarDp = typedArray.getDimensionPixelSize(0, -1)
        typedArray.recycle()

        val marginDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, context.resources.displayMetrics)
        val layoutParams = commentsButton.layoutParams as LayoutParams
        commentsButton.drawable.setTint(Color.WHITE)
        layoutParams.gravity = Gravity.END or Gravity.TOP
        layoutParams.topMargin = marginDp.toInt() + actionBarDp
        layoutParams.rightMargin = marginDp.toInt()

        commentsButton.onClick {
            openComments()
        }
    }

    protected fun openComments() {
        // Get current annotation in both forms
    }

    open fun setupPSPDFKit(uri: Uri) {
        attachDocListener()
    }

    @Suppress("EXPERIMENTAL_FEATURE_WARNING")
    protected fun handlePdfContent(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(url), "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        context.startActivity(intent)
    }

    open fun attachDocListener() {
    }

    @Suppress("EXPERIMENTAL_FEATURE_WARNING")
    protected fun load(url: String?, onFinished: (Uri) -> Unit) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(url), "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        context.startActivity(intent)
    }

    //region Annotation Manipulation
    @Suppress("EXPERIMENTAL_FEATURE_WARNING")
    fun createNewAnnotation(annotation: Annotation) {
    }

}
