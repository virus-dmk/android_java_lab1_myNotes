package by.bychko.notelist

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import by.bychko.notelist.di.AppComponent
import by.bychko.notelist.di.DaggerAppComponent
import by.bychko.notelist.di.NoteDaoModule
import by.bychko.notelist.mvp.model.AppDatabase
import by.bychko.notelist.mvp.model.Note
import com.reactiveandroid.ReActiveAndroid
import com.reactiveandroid.ReActiveConfig
import com.reactiveandroid.internal.database.DatabaseConfig

class NoteListApplication : Application() {
    companion object {
        lateinit var graph: AppComponent
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = this
        graph = DaggerAppComponent.builder().noteDaoModule(NoteDaoModule()).build()

        val appDatabaseConfig = DatabaseConfig.Builder(AppDatabase::class.java)
            .addModelClasses(Note::class.java)
            .build()

        ReActiveAndroid.init(ReActiveConfig.Builder(this)
            .addDatabaseConfigs(appDatabaseConfig)
            .build())
    }
}