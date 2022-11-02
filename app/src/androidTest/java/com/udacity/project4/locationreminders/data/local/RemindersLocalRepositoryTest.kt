package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private lateinit var repository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

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
     *   Initializes the database and repository
     */
    fun init() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

        repository = RemindersLocalRepository(database.reminderDao(), Dispatchers.Unconfined)
    }

    @Test
    fun saveReminder() = runBlocking {
        database.reminderDao().saveReminder(reminder1)

        val reminder = database.reminderDao().getReminderById(reminder1.id)

        assertThat(reminder == reminder1,`is` (true))
        assertThat(reminder?.title == reminder1.title,`is` (true))
        assertThat(reminder?.description == reminder1.description,`is` (true))
        assertThat(reminder?.location == reminder1.location,`is` (true))
        assertThat(reminder?.latitude == reminder1.latitude,`is` (true))
        assertThat(reminder?.longitude == reminder1.longitude,`is` (true))
    }

    @Test
    fun deleteAll() = runBlocking {
        database.reminderDao().deleteAllReminders()

        assertThat(database.reminderDao().getReminders().size,`is` (0))
    }

    @After
    fun close() {
        database.close()
    }
}