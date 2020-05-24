package by.bychko.notelist.mvp.presenters


import by.bychko.notelist.NoteListApplication
import by.bychko.notelist.bus.NoteDeleteAction
import by.bychko.notelist.bus.NoteEditAction
import by.bychko.notelist.mvp.model.Note
import by.bychko.notelist.mvp.model.NoteDAO
import by.bychko.notelist.mvp.views.NoteView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.greenrobot.eventbus.EventBus
import java.util.*
import javax.inject.Inject

@InjectViewState
class NotePresenter(val noteId: Long) : MvpPresenter<NoteView>() {

    @Inject
    lateinit var noteDao: NoteDAO
    private lateinit var note: Note

    init {
        NoteListApplication.graph.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        note = noteDao.getNoteById(noteId)!!
        viewState.showNote(note)
    }

    fun saveNote(title: String, text: String) {
        note.title = title
        note.text = text
        note.changedAt = Date()
        noteDao.saveNote(note)
        EventBus.getDefault().post(NoteEditAction(note.id))
        viewState.onNoteSaved()
    }

    fun deleteNote() {
        //after deletion note id will be 0,
        // so we should save one before delete operation
        val noteId = note.id
        noteDao.deleteNote(note)
        EventBus.getDefault().post(NoteDeleteAction(noteId))
        viewState.onNoteDeleted()
    }

    fun showNoteDeleteDialog() {
        viewState.showNoteDeleteDialog()
    }

    fun hideNoteDeleteDialog() {
        viewState.hideNoteDeleteDialog()
    }

    fun showNoteInfoDialog() {
        viewState.showNoteInfoDialog(note.getInfo())
    }

    fun hideNoteInfoDialog() {
        viewState.hideNoteInfoDialog()
    }

}
