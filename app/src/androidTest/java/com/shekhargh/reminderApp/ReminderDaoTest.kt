package com.shekhargh.reminderApp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.shekhargh.reminderApp.db.Reminder
import com.shekhargh.reminderApp.db.ReminderDatabase
import com.shekhargh.reminderApp.db.RemindersDao
import com.shekhargh.reminderApp.util.SampleData
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ReminderDaoTest {

    private lateinit var database: ReminderDatabase
    private lateinit var dao: RemindersDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ReminderDatabase::class.java
        ).build()

        dao = database.getReminderDao()
    }

    @After
    fun teardown() {
        database.close()
    }


    /*
        So i need to write test for
        1.  upser
        2.  delete
        3.  getItemFromId
        4.  updateCompletionStatus
        5.  getAllReminders
     */

    @Test
    fun insertReminder_validReminder_returnsReminderIdAndStoresData() = runTest {
        val original = getReminderItemFromIndex(0).copy(id = 0)
        val generatedId = dao.upsertReminder(original)
        val retrievedReminder = dao.getItemFromId(generatedId.toInt())

        assertThat(retrievedReminder).isNotNull()
        assertThat(generatedId).isAtLeast(1)
        assertThat(retrievedReminder?.copy(id = 0)).isEqualTo(original)

    }

    @Test
    fun getSingleReminder_existingReminder_returnsReminderItem() = runTest {
        val reminder1 = getReminderItemFromIndex(0)
        val reminder2 = getReminderItemFromIndex(1)

        val itemId1 = dao.upsertReminder(reminder1)
        val itemId2 = dao.upsertReminder(reminder2)

        val retrieveItem1 = dao.getItemFromId(itemId1.toInt())
        val retrieveItem2 = dao.getItemFromId(itemId2.toInt())

        assertThat(retrieveItem1).isNotNull()
        assertThat(retrieveItem2).isNotNull()
        assertThat(retrieveItem1).isEqualTo(reminder1)
        assertThat(retrieveItem1).isNotEqualTo(retrieveItem2)

        val nonExistingReminder = dao.getItemFromId(999)
        assertThat(nonExistingReminder).isNull()

    }

    @Test
    fun updateItem_validReminder_returnsRowsAffected() = runTest {


        var initialReminder1 = getReminderItemFromIndex(0)
        var initialReminder2 = getReminderItemFromIndex(1)

        val reminderID1 = dao.upsertReminder(initialReminder1)
        val reminderID2 = dao.upsertReminder(initialReminder2)

        val updatedTitle = "Updated Title"

        val updatedReminder1 = initialReminder1.copy(title = updatedTitle)
        dao.upsertReminder(updatedReminder1)

        val retrievedReminder1 = dao.getItemFromId(reminderID1.toInt())
        val retrievedReminder2 = dao.getItemFromId(reminderID2.toInt())

        assertThat(retrievedReminder1).isNotNull()
        assertThat(retrievedReminder2).isNotNull()

        assertThat(retrievedReminder1?.title).isEqualTo(updatedTitle)
        assertThat(retrievedReminder2?.title).isEqualTo(initialReminder2.title)
        assertThat(retrievedReminder2?.title).isNotEqualTo(updatedTitle)

    }

    @Test
    fun updateReminderStatus_trueFalse_returnsAffectedRows() = runTest {

        val initialReminder1 = getReminderItemFromIndex(2).copy(completionStatus = false)
        val initialReminder2 = getReminderItemFromIndex(3).copy(completionStatus = false)


        val insertItem1 = dao.upsertReminder(initialReminder1)
        val insertItem2 = dao.upsertReminder(initialReminder2)

        dao.updateCompletionStatus(isComplete = true, id = insertItem1.toInt())

        var updatedItem1 = dao.getItemFromId(insertItem1.toInt())
        var updatedItem2 = dao.getItemFromId(insertItem2.toInt())

        assertThat(updatedItem1?.completionStatus).isTrue()
        assertThat(updatedItem2?.completionStatus).isFalse()

        dao.updateCompletionStatus(isComplete = false, id = insertItem1.toInt())

        updatedItem1 = dao.getItemFromId(insertItem1.toInt())

        assertThat(updatedItem1?.completionStatus).isFalse()
        assertThat(updatedItem2?.completionStatus).isFalse()

    }

    @Test
    fun deleteReminder_existingReminder_returnsRowsAffected() = runTest {

        val initialReminder1 = getReminderItemFromIndex(4)
        val initialReminder2 = getReminderItemFromIndex(6)

        val id1 = dao.upsertReminder(initialReminder1)
        val id2 = dao.upsertReminder(initialReminder2)

        val rowsAffected = dao.deleteReminder(id1.toInt())

        assertThat(rowsAffected).isEqualTo(1)
        assertThat(dao.getItemFromId(id1.toInt())).isNull()
        assertThat(dao.getItemFromId(id2.toInt())).isNotNull()
        assertThat(dao.deleteReminder(999)).isEqualTo(0)

    }


    @Test
    fun insertReminder_givenValidReminder_ReturnsReminderId() = runTest {
        val reminder = getReminderItemFromIndex(0)

        val id: Long = dao.upsertReminder(reminder)
        val insertedItem = dao.getItemFromId(id.toInt())

        assertThat(id).isGreaterThan(0)
        assertThat(id).isEqualTo(insertedItem?.id)

        assertThat(reminder.title).isEqualTo(insertedItem?.title)
    }

    @Test
    fun getItemById_GivenValidId_returnsReminderItem() = runTest {

        val reminder = getReminderItemFromIndex(0)

        val id = dao.upsertReminder(reminder)

        val retrievedItem = dao.getItemFromId(id.toInt())

        assertThat(retrievedItem?.id).isEqualTo(id)

    }

    fun getReminderItemFromIndex(index: Int): Reminder {
        return SampleData.reminders[index]
    }

}