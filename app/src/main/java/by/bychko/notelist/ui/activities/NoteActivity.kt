package by.bychko.notelist.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import by.bychko.notelist.R
import by.bychko.notelist.mvp.model.Note
import by.bychko.notelist.mvp.presenters.NotePresenter
import by.bychko.notelist.mvp.views.NoteView
import by.bychko.notelist.utils.formatDate
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : MvpAppCompatActivity(), NoteView {

    companion object {
        const val NOTE_DELETE_ARG = "note_id"

        fun buildIntent(activity: Activity, noteId: Long): Intent {
            val intent = Intent(activity, NoteActivity::class.java)
            intent.putExtra(NOTE_DELETE_ARG, noteId)
            return intent
        }
    }

    @InjectPresenter
    lateinit var presenter: NotePresenter
    private var noteDeleteDialog: MaterialDialog? = null
    private var noteInfoDialog: MaterialDialog? = null

    @ProvidePresenter
    fun provideHelloPresenter(): NotePresenter? {
        val noteId = intent.extras?.getLong(NOTE_DELETE_ARG, -1)
        return noteId?.let { NotePresenter(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        etTitle.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val editText = view as EditText
                editText.setSelection((editText.text.length))
            }
        }
    }

    override fun showNote(note: Note) {
        tvNoteDate.text = formatDate(note.changedAt)
        etTitle.setText(note.title)
        etText.setText(note.text)
    }

    override fun showNoteInfoDialog(noteInfo: String) {
        noteInfoDialog = MaterialDialog(this).show {
            title(R.string.note_info)
            message(text = noteInfo)
            positiveButton(R.string.ok) {
                presenter.hideNoteInfoDialog()
            }
            setOnCancelListener {
                presenter.hideNoteInfoDialog()
            }
        }
    }

    override fun hideNoteInfoDialog() {
        noteInfoDialog?.dismiss()
    }

    override fun showNoteDeleteDialog() {
        noteDeleteDialog = MaterialDialog(this).show {
            title(R.string.note_deletion_title)
            message(R.string.note_deletion_message)
            positiveButton(R.string.yes) {
                presenter.hideNoteDeleteDialog()
                presenter.deleteNote()
            }
            negativeButton(R.string.no) { presenter.hideNoteDeleteDialog() }
            setOnCancelListener {
                presenter.hideNoteDeleteDialog()
            }
        }
    }


    override fun hideNoteDeleteDialog() {
        noteDeleteDialog?.dismiss()
    }

    override fun onNoteSaved() {
        Toast.makeText(this, "Заметка сохранена", Toast.LENGTH_SHORT).show()
    }

    override fun onNoteDeleted() {
        Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSaveNote -> presenter.saveNote(etTitle.text.toString(), etText.text.toString())

            R.id.menuDeleteNote -> presenter.showNoteDeleteDialog()

            R.id.menuNoteInfo -> presenter.showNoteInfoDialog()
        }
        return super.onOptionsItemSelected(item)
    }

}