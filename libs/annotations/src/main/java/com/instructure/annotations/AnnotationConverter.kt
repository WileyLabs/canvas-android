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

import android.graphics.PointF
import android.graphics.RectF
import com.instructure.canvasapi2.models.canvadocs.CanvaDocAnnotation
import com.instructure.canvasapi2.models.canvadocs.CanvaDocCoordinate
import java.util.*

fun createCommentReplyAnnotation(contents: String, inReplyTo: String, canvaDocId: String, userId: String, page: Int): CanvaDocAnnotation {
    return CanvaDocAnnotation(
            annotationId = "",
            ctxId = "",
            userId = userId,
            userName = "",
            createdAt = "",
            documentId = canvaDocId,
            subject = CanvaDocAnnotation.COMMENT_REPLY_SUBJECT,
            page = page,
            context = "",
            annotationType = CanvaDocAnnotation.AnnotationType.COMMENT_REPLY,
            contents = contents,
            inReplyTo = inReplyTo,
            isEditable = true)
}
//endregion


fun listOfRectsToListOfListOfFloats(rects: List<RectF>?): ArrayList<ArrayList<Float>>? {
    if (rects == null || rects.isEmpty())
        return null

    val listOfLists = ArrayList<ArrayList<Float>>()
    listOfLists.add(arrayListOf(rects.minBy { it.left }?.left ?: 0f, rects.minBy { it.bottom }?.bottom ?: 0f))
    listOfLists.add(arrayListOf(rects.maxBy { it.right }?.right ?: 0f, rects.maxBy { it.top }?.top ?: 0f))

    return listOfLists
}

fun coordsToListOfRectfs(coords: List<List<List<Float>>>?) : MutableList<RectF> {
    val rectList = mutableListOf<RectF>()
    coords?.let {
        it.forEach {
            val tempRect = RectF(it[0][0], it[0][1], it[3][0], it[3][1])
            rectList.add(tempRect)
        }
    }

    return rectList
}

fun convertListOfRectsToListOfListOfListOfFloats(rects: MutableList<RectF>?): ArrayList<ArrayList<ArrayList<Float>>>? {
    if (rects == null || rects.isEmpty()) {
        return null
    }
    // The distance between the top of the line and the bottom
    val rectList: ArrayList<ArrayList<ArrayList<Float>>> = ArrayList()
    rects.forEach {
        val posList = arrayListOf<ArrayList<Float>>()

        val bottomLineLeftTop = arrayListOf(it.left, it.bottom)
        val bottomLineRightBottom = arrayListOf(it.right, it.bottom)
        posList.add(bottomLineLeftTop)
        posList.add(bottomLineRightBottom)

        val topLineLeftTop = arrayListOf(it.left, it.top)
        val topLineRightBottom = arrayListOf(it.right, it.top)
        posList.add(topLineLeftTop)
        posList.add(topLineRightBottom)

        rectList.add(posList)
    }
    return rectList
}

fun convertListOfPointFToCanvaDocCoordinates(linesList: List<List<PointF>>): ArrayList<ArrayList<CanvaDocCoordinate>> {
    val newList = ArrayList<ArrayList<CanvaDocCoordinate>>()
    for((position, list) in linesList.withIndex()) {
        newList.add(ArrayList())
        for(point in list) {
            newList[position].add(CanvaDocCoordinate(point.x, point.y))
        }
    }

    return newList
}

fun Annotation.colorToHexString() = String.format("#%06X", 0xFFFFFF)

fun generateAnnotationId() = UUID.randomUUID().toString()

private fun getStampSubjectFromColorHex(color: String?): String {
    return when (color) {
        blackStampHex -> blackStampSubject
        blueStampHex -> blueStampSubject
        brownStampHex -> brownStampSubject
        greenStampHex -> greenStampSubject
        navyStampHex -> navyStampSubject
        orangeStampHex -> orangeStampSubject
        pinkStampHex -> pinkStampSubject
        purpleStampHex -> purpleStampSubject
        redStampHex -> redStampSubject
        yellowStampHex -> yellowStampSubject
        else -> blueStampSubject
    }
}

private fun getColorHexFromStampSubject(subject: String?): String {
    return when (subject) {
        blackStampSubject -> blackStampHex
        blueStampSubject -> blueStampHex
        brownStampSubject -> brownStampHex
        greenStampSubject -> greenStampHex
        navyStampSubject -> navyStampHex
        orangeStampSubject -> orangeStampHex
        pinkStampSubject -> pinkStampHex
        purpleStampSubject -> purpleStampHex
        redStampSubject -> redStampHex
        yellowStampSubject -> yellowStampHex
        else -> blueStampHex
    }
}

// Stamp file asset names
const val blackStampFile = "stamps/blackpoint.pdf"
const val blueStampFile = "stamps/bluepoint.pdf"
const val brownStampFile = "stamps/brownpoint.pdf"
const val greenStampFile = "stamps/greenpoint.pdf"
const val navyStampFile = "stamps/navypoint.pdf"
const val orangeStampFile = "stamps/orangepoint.pdf"
const val pinkStampFile = "stamps/pinkpoint.pdf"
const val purpleStampFile = "stamps/purplepoint.pdf"
const val redStampFile = "stamps/redpoint.pdf"
const val yellowStampFile = "stamps/yellowpoint.pdf"

// Stamp subject names
const val blackStampSubject = "BlackStamp"
const val blueStampSubject = "BlueStamp"
const val brownStampSubject = "BrownStamp"
const val greenStampSubject = "GreenStamp"
const val navyStampSubject = "NavyStamp"
const val orangeStampSubject = "OrangeStamp"
const val pinkStampSubject = "PinkStamp"
const val purpleStampSubject = "PurpleStamp"
const val redStampSubject = "RedStamp"
const val yellowStampSubject = "YellowStamp"

// Stamp hex color codes
const val blackStampHex = "#363636"
const val blueStampHex = "#008EE2"
const val brownStampHex = "#8D6437"
const val greenStampHex = "#00AC18"
const val navyStampHex = "#234C9F"
const val orangeStampHex = "#FC5E13"
const val pinkStampHex = "#C31FA8"
const val purpleStampHex = "#741865"
const val redStampHex = "#EE0612"
const val yellowStampHex = "#FCB900"

// Free Text Annotation Font sizes
const val smallFont = 10f
const val mediumFont = 18f
const val largeFont = 32f