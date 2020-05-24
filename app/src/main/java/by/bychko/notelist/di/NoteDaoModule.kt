package by.bychko.notelist.di

import by.bychko.notelist.mvp.model.NoteDAO
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NoteDaoModule {
    @Provides
    @Singleton
    fun provideNoteDao(): NoteDAO = NoteDAO()
}