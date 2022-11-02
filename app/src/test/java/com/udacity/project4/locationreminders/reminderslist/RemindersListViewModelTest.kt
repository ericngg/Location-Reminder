package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    private lateinit var reminderListViewModel: RemindersListViewModel
    private lateinit var fakeRepository: FakeDataSource

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val reminder1 = ReminderDTO(
        title = "school",
        description = "this is a school",
        location = "poi",
        latitude = 47.61824,
        longitude = -122.16475
    )

    private val reminder2 = ReminderDTO(
        title = "family home",
        description = "this is a family home",
        location = "poi",
        latitude = 47.61702,
        longitude = -122.16645
    )

    /**
     *   Initilaize a fake repository and reminderListViewModel
     */
    @Before
    fun init() {
        fakeRepository = FakeDataSource()
        reminderListViewModel = RemindersListViewModel(getApplicationContext(), fakeRepository)
    }

    /**
     *   Checks the repository is empty
     */
    @Test
    fun emptyRepository() {
        reminderListViewModel.loadReminders()
        assertThat(reminderListViewModel.showNoData.value, `is`(true))
    }

    /**
     *   Adds a reminder and check if it was successful with the loading icon
     */
    @Test
    fun addReminders() = runBlockingTest {
        fakeRepository.saveReminder(reminder1)
        mainCoroutineRule.pauseDispatcher()
        reminderListViewModel.loadReminders()

        assertThat(reminderListViewModel.showLoading.value, `is` (true))

        mainCoroutineRule.resumeDispatcher()
    }

    /**
     *   Checks if an error will be returned forcefully with the snackbar message
     */
    @Test
    fun addRemindersError() = runBlockingTest {
        fakeRepository.saveReminder(reminder2)
        fakeRepository.returnError(true)
        reminderListViewModel.loadReminders()

        assertThat(reminderListViewModel.showSnackBar.value,`is`("Test: Forced Error"))
    }

    /**
     *   Checks if the reminders were deleted and the database is empty with the showNoData screen
     */
    @Test
    fun deleteReminders() = runBlockingTest {
        fakeRepository.deleteAllReminders()
        reminderListViewModel.loadReminders()

        assertThat(reminderListViewModel.showNoData.value, `is` (true))
    }
}