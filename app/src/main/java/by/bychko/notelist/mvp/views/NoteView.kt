package by.bychko.notelist.mvp.views


import by.bychko.notelist.mvp.model.Note
import com.arellomobile.mvp.MvpView

interface NoteView : MvpView {

    fun showNote(note: Note)

    fun onNoteSaved()

    fun onNoteDeleted()

    fun showNoteInfoDialog(noteInfo: String)

    fun hideNoteInfoDialog()

    fun showNoteDeleteDialog()

    fun hideNoteDeleteDialog()

}
