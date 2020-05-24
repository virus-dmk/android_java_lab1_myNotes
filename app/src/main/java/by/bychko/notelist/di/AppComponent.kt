package by.bychko.notelist.di

import by.bychko.notelist.mvp.presenters.MainPresenter
import by.bychko.notelist.mvp.presenters.NotePresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NoteDaoModule::class])
interface AppComponent {
    fun inject(mainPresenter: MainPresenter)

    fun inject(notePresenter: NotePresenter)
}