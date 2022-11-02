package com.udacity.project4.locationreminders.savereminder

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var fakeRepository: FakeDataSource

    private val noTitleReminder = ReminderDataItem(
        title = "",
        description = "this is a school",
        location = "poi",
        latitude = 47.61824,
        longitude = -122.16475
    )

    private val noLocationReminder = ReminderDataItem(
        title = "family home",
        description = "this is a family home",
        location = "",
        latitude = 47.61702,
        longitude = -122.16645
    )

    private val reminder = ReminderDataItem(
        title = "family home",
        description = "this is a family home",
        location = "poi",
        latitude = 47.61702,
        longitude = -122.16620
    )

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    /**
     *   Initialize a fake repository and saveReminderViewModel
     */
    @Before
    fun init() {
        fakeRepository = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(getApplicationContext(), fakeRepository)
    }

    /**
     *   Checks saving reminder with no location with the snackbar value
     */
    @Test
    fun saveNoLocation() {
        saveReminderViewModel.validateAndSaveReminder(noLocationReminder)
        assertThat(saveReminderViewModel.showSnackBarInt.value,`is`(notNullValue()))
    }

    /**
     *   Checks saving reminder with no title with the snackbar value
     */
    @Test
    fun saveNoTitle() {
        saveReminderViewModel.validateAndSaveReminder(noTitleReminder)
        assertThat(saveReminderViewModel.showSnackBarInt.value,`is` (notNullValue()))
    }

    /**
     *   Checks saving reminder and it was successfully added to the database
     */
    @Test
    fun saveReminder() {
        saveReminderViewModel.saveReminder(reminder)
        assertThat(saveReminderViewModel.showLoading.value,`is` (false))
    }
}