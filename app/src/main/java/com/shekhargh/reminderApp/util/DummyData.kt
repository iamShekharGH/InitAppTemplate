package com.shekhargh.reminderApp.util

import com.shekhargh.reminderApp.db.Priority
import com.shekhargh.reminderApp.db.Reminder
import java.time.LocalDateTime

object SampleData {
    val reminders = listOf(
        Reminder(1, "Buy Groceries", "Milk, eggs, bread, and butter", false, Priority.MEDIUM, LocalDateTime.now().plusDays(1)),
        Reminder(2, "Gym Session", "Leg day - don't skip it", false, Priority.HIGH, LocalDateTime.now().plusHours(2)),
        Reminder(3, "Finish Project Report", "Submit to the manager by EOD", true, Priority.HIGH, LocalDateTime.now().minusDays(1)),
        Reminder(4, "Call Mom", "", false, Priority.LOW, LocalDateTime.now().plusDays(3)),
        Reminder(5, "Car Service", "Check oil and tires", false, Priority.MEDIUM, LocalDateTime.now().plusWeeks(1)),
        Reminder(6, "Doctor Appointment", "Annual checkup", false, Priority.HIGH, LocalDateTime.now().plusDays(5)),
        Reminder(7, "Read 20 pages", "Atomic Habits", true, Priority.LOW, LocalDateTime.now().minusHours(5)),
        Reminder(8, "Pay Electricity Bill", "Due before the 15th", false, Priority.HIGH, LocalDateTime.now().plusDays(2)),
        Reminder(9, "Water Plants", "", false, Priority.LOW, LocalDateTime.now().plusHours(12)),
        Reminder(10, "Clean Apartment", "Vacuum and dust everything", false, Priority.MEDIUM, LocalDateTime.now().plusDays(4)),
        Reminder(11, "Prep for Interview", "Review Android system design", false, Priority.HIGH, LocalDateTime.now().plusDays(1)),
        Reminder(12, "Update Portfolio", "Add the Reminder app to GitHub", false, Priority.MEDIUM, LocalDateTime.now().plusWeeks(2)),
        Reminder(13, "Buy Gift for Sarah", "Birthday is next Friday", false, Priority.MEDIUM, LocalDateTime.now().plusDays(6)),
        Reminder(14, "Laundry", "Don't forget the whites", true, Priority.LOW, LocalDateTime.now().minusDays(2)),
        Reminder(15, "Team Sync", "Prepare the sprint updates", false, Priority.HIGH, LocalDateTime.now().plusHours(1)),
        Reminder(16, "Mow the Lawn", "", false, Priority.LOW, LocalDateTime.now().plusDays(10)),
        Reminder(17, "Fix UI Bug", "The padding on the list item is off", false, Priority.MEDIUM, LocalDateTime.now().plusHours(4)),
        Reminder(18, "Dinner with Alex", "Italian place at 7 PM", false, Priority.LOW, LocalDateTime.now().plusDays(2)),
        Reminder(19, "Meditation", "10 minutes of mindfulness", true, Priority.LOW, LocalDateTime.now().minusMinutes(30)),
        Reminder(20, "This is a very long title to test how the UI handles text wrapping in the reminder list item", "Testing description overflow as well to ensure the UI doesn't break when the user is wordy.", false, Priority.MEDIUM, LocalDateTime.now().plusDays(1))
    )
}