package com.instructure.student.ui.interaction

import androidx.test.espresso.Espresso
import com.instructure.canvas.espresso.mockCanvas.MockCanvas
import com.instructure.canvas.espresso.mockCanvas.addUserPermissions
import com.instructure.canvas.espresso.mockCanvas.init
import com.instructure.panda_annotations.FeatureCategory
import com.instructure.panda_annotations.Priority
import com.instructure.panda_annotations.TestCategory
import com.instructure.panda_annotations.TestMetaData
import com.instructure.student.ui.utils.StudentTest
import com.instructure.student.ui.utils.tokenLogin
import org.junit.Test

class ProfileSettingsInteractionTest : StudentTest() {

    override fun displaysPageObjects() = Unit // Not used for interaction tests

    @Test
    @TestMetaData(Priority.P1, FeatureCategory.SETTINGS, TestCategory.INTERACTION)
    fun testProfileSettings_changeUsername() {
        val data = MockCanvas.init(studentCount = 1, teacherCount = 1, courseCount = 1, favoriteCourseCount = 1)
        val student = data.students[0]
        val newUserName = "New User Name"

        data.addUserPermissions(userId = student.id, canUpdateName = true, canUpdateAvatar = true)

        val token = data.tokenFor(student)!!
        tokenLogin(data.domain, token, student)

        dashboardPage.launchSettingsPage()
        settingsPage.launchProfileSettings()
        profileSettingsPage.changeUserNameTo(newUserName)

        Espresso.pressBack() // to settings page
        Espresso.pressBack() // to dashboard

        dashboardPage.assertUserLoggedIn(newUserName)
    }

    @Test
    @TestMetaData(Priority.P1, FeatureCategory.SETTINGS, TestCategory.INTERACTION)
    fun testProfileSettings_disabledIfNoPermissions() {
        val data = MockCanvas.init(studentCount = 1, teacherCount = 1, courseCount = 1, favoriteCourseCount = 1)
        val student = data.students[0]

        val token = data.tokenFor(student)!!
        tokenLogin(data.domain, token, student)

        dashboardPage.launchSettingsPage()
        settingsPage.launchProfileSettings()
        profileSettingsPage.assertSettingsDisabled() // No permissions granted
    }
}