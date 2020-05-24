@file:JvmName("PrefsUtils")
package by.bychko.notelist.utils

import android.content.Context
import by.bychko.notelist.NoteListApplication

private val prefs by lazy {
    NoteListApplication.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
}

fun getNotesSortMethodName(defaultValue: String): String =
    prefs.getString("sort_method", defaultValue)!!

fun setNotesSortMethod(sortMethod: String) {
    prefs.edit().putString("sort_method", sortMethod).apply()
}
