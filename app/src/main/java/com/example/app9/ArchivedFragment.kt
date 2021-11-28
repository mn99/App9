package com.example.app9

class ArchivedFragment : MainHelperFragment() {

    override fun getBackground() = R.drawable.archive

    override fun getObservable() = model.archivedNotes

    override fun showOperations(baseNote: BaseNote) {
        val unarchive = Operation(R.string.unarchive, R.drawable.unarchive) { model.restoreBaseNote(baseNote.id) }
        showMenu(unarchive)
    }
}