# DOit App

## Description
The Task Management App is a comprehensive solution for managing your daily tasks efficiently. It enables users to create, update, and delete tasks with features like reminders and priority-based organization. The app ensures you stay on top of your schedule with timely notifications and a user-friendly interface.

### Key Features:
- **Task Creation and Management:** Add tasks with titles, priorities, and execution times.
- **Priority-Based Task Sorting:** Automatically organizes tasks based on priority (High, Medium, Low).
- **Reminders and Notifications:** Set reminders to get timely notifications about tasks.
- **Edit and Delete Options:** Update or remove tasks as needed.
- **Persistent Data Storage:** Tasks are stored locally using a Room database.
- **Splash Screen:** Provides a visually appealing entry point to the app.

## Components Used and Their Roles

### 1. **Activities**
- **`MainActivity`**: Displays the list of tasks and provides options to add new tasks.
- **`CreateCard`**: Allows users to create a new task by specifying its details.
- **`UpdateCard`**: Enables users to update or delete an existing task.
- **`SplashScreen`**: Acts as the introductory screen with a 2-second delay before navigating to the main activity.

### 2. **Database**
- **`Room Database` (`AppDatabase`, `Task`, `TaskDao`)**: 
  - Stores all tasks persistently on the device.
  - Provides methods for adding, updating, retrieving, and deleting tasks.
  - Ensures efficient and structured data handling.

### 3. **ViewModel and Repository**
- **`TaskViewModel`**: Manages UI-related data and ensures data survives configuration changes.
- **`TaskRepository`**: Acts as a single source of truth for data, abstracting access to the database.

### 4. **RecyclerView and Adapter**
- **`TaskAdapter`**:
  - Binds task data to views in a RecyclerView for display.
  - Handles user interactions with individual tasks.

### 5. **Background Services**
- **`NotificationWorker`**:
  - Uses `WorkManager` to schedule notifications for tasks at their designated execution times.
  - Runs in the background, ensuring tasks are completed even when the app is not active.

### 6. **Broadcast Receiver**
- **`ReminderBroadcastReceiver`**:
  - Intercepts alarms and displays notifications for tasks.
  - Ensures users are reminded even if the app is not running in the foreground.

### 7. **Notifications**
- **`NotificationManager` (via `NotificationWorker` and `ReminderBroadcastReceiver`)**:
  - Creates and displays notifications to remind users of their tasks.
  - Utilizes a notification channel to ensure compatibility with newer Android versions.

### 8. **XML Layouts**
- **`activity_main.xml`**: Layout for the main screen, featuring the task list and add button.
- **`activity_create_card.xml`**: Layout for creating a new task.
- **`activity_update_card.xml`**: Layout for updating or deleting tasks.
- **`view.xml`**: Defines the design of individual task cards in the RecyclerView.
- **`activity_splash_screen.xml`**: Layout for the splash screen.

### 9. **Intents**
- Used extensively for:
  - Navigating between activities.
  - Scheduling alarms via the `ReminderBroadcastReceiver`.

## GitHub Repository
[Link to GitHub Repository](https://github.com/takdeerSM/Doit)

Feel free to clone, modify, and enhance the project!

