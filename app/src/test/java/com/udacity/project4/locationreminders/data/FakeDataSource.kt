package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminderDTO: MutableList<ReminderDTO>? = mutableListOf()) : ReminderDataSource {

    private var shouldReturnError = false

    private fun returnError(bol: Boolean) {
        shouldReturnError = bol
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError) {
            return Result.Error(
                "Test: Forced Error"
            )
        }

        reminderDTO?.let {
            return Result.Success(it)
        }

        return Result.Error(
            "Test: Reminders not found"
        )
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderDTO?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) {
            return Result.Error(
                "Test: Forced Error"
            )
        }

        val reminder = reminderDTO?.find { it ->
            it.id == id
        }

        if (reminder != null) {
            return Result.Success(reminder)
        }

        return Result.Error (
            "Test: Reminder not found with id: $id"
        )
    }

    override suspend fun deleteAllReminders() {
        reminderDTO?.clear()
    }


}