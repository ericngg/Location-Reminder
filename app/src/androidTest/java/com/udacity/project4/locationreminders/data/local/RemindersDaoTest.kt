package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

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
     *   Initialize the database
     */
    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    /**
     *   Saves a reminder and checks if the saved reminder is the same
     */
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

    /**
     *   Clears the reminders from the database and checks if the database is empty
     */
    @Test
    fun clearReminders() = runBlocking {
        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)

        assertThat(database.reminderDao().getReminders().size,`is` (2))
        database.reminderDao().deleteAllReminders()
        assertThat(database.reminderDao().getReminders().size,`is` (0))
    }

    /**
     *   Resets the database
     */
    @After
    fun closeDb() {
        database.close()
    }

}