package com.batyaboyo.bibleapp.data

import android.content.Context
import com.batyaboyo.bibleapp.model.Prayer
import com.batyaboyo.bibleapp.model.QuizQuestion
import com.batyaboyo.bibleapp.model.Story
import org.json.JSONArray

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetRepository(private val context: Context) {

    suspend fun loadStories(): List<Story> = withContext(Dispatchers.IO) {
        val raw = context.assets.open("stories.json").bufferedReader().use { it.readText() }
        val arr = JSONArray(raw)
        val items = mutableListOf<Story>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            
            val contentArr = obj.optJSONArray("content") ?: JSONArray()
            val content = buildList {
                for (j in 0 until contentArr.length()) {
                    val p = contentArr.getJSONObject(j)
                    add(com.batyaboyo.bibleapp.model.StoryPage(
                        title = p.optString("title").takeIf { it.isNotEmpty() },
                        text = p.getString("text")
                    ))
                }
            }
            
            val kvObj = obj.optJSONObject("keyVerse")
            val keyVerse = kvObj?.let {
                com.batyaboyo.bibleapp.model.StoryKeyVerse(
                    text = it.getString("text"),
                    ref = it.getString("ref")
                )
            }

            items += Story(
                id = obj.getString("id"),
                title = obj.getString("title"),
                testament = obj.getString("testament"),
                icon = obj.optString("icon"),
                content = content,
                moral = obj.optString("moral").takeIf { it.isNotEmpty() },
                keyVerse = keyVerse
            )
        }
        return@withContext items
    }

    suspend fun loadQuizQuestions(): List<QuizQuestion> = withContext(Dispatchers.IO) {
        val raw = context.assets.open("quiz.json").bufferedReader().use { it.readText() }
        val arr = JSONArray(raw)
        val items = mutableListOf<QuizQuestion>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            val optionsArray = obj.getJSONArray("options")
            val options = buildList {
                for (j in 0 until optionsArray.length()) add(optionsArray.getString(j))
            }
            items += QuizQuestion(
                category = obj.getString("category"),
                question = obj.getString("question"),
                options = options,
                answerIndex = obj.getInt("answerIndex"),
                reference = obj.optString("reference")
            )
        }
        return@withContext items
    }

    suspend fun loadPrayers(): List<Prayer> = withContext(Dispatchers.IO) {
        val raw = context.assets.open("prayers.json").bufferedReader().use { it.readText() }
        val arr = JSONArray(raw)
        val items = mutableListOf<Prayer>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            items += Prayer(
                type = obj.getString("type"),
                title = obj.getString("title"),
                verse = obj.getString("verse"),
                verseRef = obj.getString("verseRef"),
                text = obj.getString("text"),
                closing = obj.getString("closing")
            )
        }
        return@withContext items
    }
}
