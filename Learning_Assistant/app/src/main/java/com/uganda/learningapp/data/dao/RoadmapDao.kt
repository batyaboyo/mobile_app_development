package com.uganda.learningapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.uganda.learningapp.data.entity.ModuleEntity
import com.uganda.learningapp.data.entity.TaskEntity
import com.uganda.learningapp.data.entity.WeekUnitEntity
import com.uganda.learningapp.data.entity.ProjectEntity
import com.uganda.learningapp.data.entity.QuizEntity
import com.uganda.learningapp.data.entity.ResourceEntity
import com.uganda.learningapp.data.entity.BadgeEntity
import com.uganda.learningapp.data.entity.UserSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoadmapDao {
    // ===== MODULES =====
    @Query("SELECT * FROM modules ORDER BY id ASC")
    fun getAllModules(): Flow<List<ModuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModule(module: ModuleEntity)

    // ===== WEEKS =====
    @Query("SELECT * FROM weeks WHERE moduleId = :moduleId ORDER BY id ASC")
    fun getWeeksForModule(moduleId: Int): Flow<List<WeekUnitEntity>>

    @Query("SELECT * FROM weeks ORDER BY id ASC")
    fun getAllWeeks(): Flow<List<WeekUnitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeek(week: WeekUnitEntity)

    @Query("UPDATE weeks SET isCompleted = :isCompleted WHERE id = :weekId")
    suspend fun updateWeekCompletion(weekId: Int, isCompleted: Boolean)

    @Query("SELECT COUNT(*) FROM weeks WHERE isCompleted = 1")
    fun getCompletedWeeksCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM weeks")
    fun getTotalWeeksCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM weeks WHERE moduleId = :moduleId AND isCompleted = 1")
    fun getCompletedWeeksCountForModule(moduleId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM weeks WHERE moduleId = :moduleId")
    fun getTotalWeeksCountForModule(moduleId: Int): Flow<Int>

    // ===== TASKS =====
    @Query("SELECT * FROM tasks WHERE weekId = :weekId")
    fun getTasksForWeek(weekId: Int): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean)

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    fun getCompletedTasksCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks")
    fun getTotalTasksCount(): Flow<Int>

    // ===== QUIZZES =====
    @Query("SELECT * FROM quizzes WHERE weekId = :weekId")
    fun getQuizzesForWeek(weekId: Int): Flow<List<QuizEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizEntity)

    @Query("SELECT COUNT(*) FROM quizzes")
    fun getTotalQuizzesCount(): Flow<Int>

    // ===== PROJECTS =====
    @Query("SELECT * FROM projects ORDER BY createdDate DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectById(projectId: Int): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("UPDATE projects SET isCompleted = :isCompleted, completedDate = :completedDate WHERE id = :projectId")
    suspend fun updateProjectCompletion(projectId: Int, isCompleted: Boolean, completedDate: Long?)

    @Query("SELECT COUNT(*) FROM projects WHERE isCompleted = 1")
    fun getCompletedProjectsCount(): Flow<Int>

    // ===== RESOURCES =====
    @Query("SELECT * FROM resources ORDER BY phaseId ASC, title ASC")
    fun getAllResources(): Flow<List<ResourceEntity>>

    @Query("SELECT * FROM resources WHERE phaseId = :phaseId ORDER BY title ASC")
    fun getResourcesByPhase(phaseId: Int): Flow<List<ResourceEntity>>

    @Query("SELECT * FROM resources WHERE topic = :topic ORDER BY title ASC")
    fun getResourcesByTopic(topic: String): Flow<List<ResourceEntity>>

    @Query("SELECT * FROM resources WHERE difficulty = :difficulty ORDER BY title ASC")
    fun getResourcesByDifficulty(difficulty: String): Flow<List<ResourceEntity>>

    @Query("SELECT * FROM resources WHERE phaseId = :phaseId AND topic = :topic ORDER BY title ASC")
    fun getResourcesByPhaseAndTopic(phaseId: Int, topic: String): Flow<List<ResourceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResource(resource: ResourceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResources(resources: List<ResourceEntity>)

    // ===== BADGES =====
    @Query("SELECT * FROM badges ORDER BY isUnlocked DESC, name ASC")
    fun getAllBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM badges WHERE isUnlocked = 1 ORDER BY unlockedDate DESC")
    fun getUnlockedBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT COUNT(*) FROM badges WHERE isUnlocked = 1")
    fun getUnlockedBadgesCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadge(badge: BadgeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)

    @Query("UPDATE badges SET isUnlocked = 1, unlockedDate = :unlockedDate WHERE id = :badgeId")
    suspend fun unlockBadge(badgeId: String, unlockedDate: Long)

    // ===== USER SETTINGS =====
    @Query("SELECT * FROM user_settings WHERE id = 1")
    fun getUserSettings(): Flow<UserSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSettings(settings: UserSettingsEntity)

    @Query("UPDATE user_settings SET darkModeEnabled = :enabled WHERE id = 1")
    suspend fun updateDarkMode(enabled: Boolean)

    @Query("UPDATE user_settings SET notificationsEnabled = :enabled WHERE id = 1")
    suspend fun updateNotifications(enabled: Boolean)

    @Query("UPDATE user_settings SET userName = :name WHERE id = 1")
    suspend fun updateUserName(name: String)
}
