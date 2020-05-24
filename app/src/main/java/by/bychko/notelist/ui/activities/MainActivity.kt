package by.bychko.notelist.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.pawegio.kandroid.onQueryChange
import by.bychko.notelist.R
import by.bychko.notelist.mvp.model.Note
import by.bychko.notelist.mvp.presenters.MainPresenter
import by.bychko.notelist.mvp.views.MainView
import by.bychko.notelist.ui.adapters.NotesAdapter
import by.bychko.notelist.ui.commons.ItemClickSupport
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.list.listItems
import com.arellomobile.mvp.MvpAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter
    private var noteContextDialog: MaterialDialog? = null
    private var noteDeleteDialog: MaterialDialog? = null
    private var noteInfoDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(ItemClickSupport.addTo(notesList)) {
            setOnItemClickListener { _, position, _ -> presenter.openNote(position) }
            setOnItemLongClickListener { _, position, _ -> presenter.showNoteContextDialog(position); true }
        }

        newNoteFab.setOnClickListener { presenter.openNewNote() }
    }

    override fun onNotesLoaded(notes: List<Note>) {
        notesList.adapter = NotesAdapter(notes)
        updateView()
    }

    override fun updateView() {
        notesList.adapter?.notifyDataSetChanged()
        if (notesList.adapter!!.itemCount == 0) {
            notesList.visibility = View.GONE
            tvNotesIsEmpty.visibility = View.VISIBLE
        } else {
            notesList.visibility = View.VISIBLE
            tvNotesIsEmpty.visibility = View.GONE
        }
    }

    override fun onNoteDeleted() {
        updateView()
        Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show()
    }

    override fun onAllNotesDeleted() {
        updateView()
        Toast.makeText(this, R.string.all_notes_deleted, Toast.LENGTH_SHORT).show()
    }

    override fun onSearchResult(notes: List<Note>) {
        notesList.adapter = NotesAdapter(notes)
    }

    override fun showNoteContextDialog(notePosition: Int) {
        noteContextDialog = MaterialDialog(this).show {
            listItems(R.array.main_note_context) { dialog, index, text ->
                onContextDialogItemClick(index, notePosition)
                presenter.hideNoteContextDialog()
            }
            setOnCancelListener {
                presenter.hideNoteContextDialog()
            }
        }
    }

    override fun hideNoteContextDialog() {
        noteContextDialog?.dismiss()
    }

    override fun showNoteDeleteDialog(notePosition: Int) {
        noteDeleteDialog = MaterialDialog(this).show {
            title(R.string.note_deletion_title)
            message {
                getString(R.string.note_deletion_message)
            }
            positiveButton(R.string.yes) { dialog ->
                presenter.deleteNoteByPosition(notePosition)
                noteInfoDialog?.dismiss()
            }
            negativeButton(R.string.no) { dialog ->
                presenter.hideNoteDeleteDialog()
            }
            onCancel { presenter.hideNoteDeleteDialog() }
        }

    }

    override fun hideNoteDeleteDialog() {
        noteDeleteDialog?.dismiss()
    }

    override fun showNoteInfoDialog(noteInfo: String) {
        noteInfoDialog = MaterialDialog(this).show {
            title(R.string.note_info)
            positiveButton(R.string.ok) {
                presenter.hideNoteInfoDialog()
            }
            message(text = noteInfo)
            setOnCancelListener{
                presenter.hideNoteInfoDialog()
            }

        }
    }

    override fun hideNoteInfoDialog() {
        noteInfoDialog?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        initSearchView(menu)
        return true
    }

    override fun openNoteScreen(noteId: Long) {
        startActivity(NoteActivity.buildIntent(this, noteId))
    }

    private fun initSearchView(menu: Menu) {
        val searchViewMenuItem = menu.findItem(R.id.action_search)
        val searchView = searchViewMenuItem.actionView as SearchView
        searchView.onQueryChange { query -> presenter.search(query) }
        searchView.setOnCloseListener { presenter.search(""); false }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuDeleteAllNotes -> presenter.deleteAllNotes()
            R.id.menuSortByName -> presenter.sortNotesBy(MainPresenter.SortNotesBy.NAME)
            R.id.menuSortByDate -> presenter.sortNotesBy(MainPresenter.SortNotesBy.DATE)
            R.id.infodev -> presenter.showDevInfo("Разработчик: Бычко Дмитрий")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onContextDialogItemClick(contextItemPosition: Int, notePosition: Int) {
        when (contextItemPosition) {
            0 -> presenter.openNote(notePosition)
            1 -> presenter.showNoteInfo(notePosition)
            2 -> presenter.showNoteDeleteDialog(notePosition)
        }
    }

}
