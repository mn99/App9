package com.example.app9

import android.app.Application

class MakeListModel(app: Application) : MainHelperModel(app) {

    val items = ArrayList<ListItem>()

    override fun getBaseNote(): BaseNote {
        val filteredItems = items.filter { (body) -> body.isNotBlank() }
        return BaseNote.createList(id, folder, title, pinned, timestamp, labels, filteredItems)
    }

    override fun setStateFromBaseNote(baseNote: BaseNote) {
        super.setStateFromBaseNote(baseNote)
        items.clear()
        items.addAll(baseNote.items)
    }
}