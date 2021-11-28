package com.example.app9

class DisplayLabelFragment : MainHelperFragment() {

    private val label by lazy { requireNotNull(requireArguments().getString(Constants.SelectedLabel)) }

    override fun getBackground() = R.drawable.label

    override fun getObservable() = model.getNotesByLabel(label)

    override fun showOperations(baseNote: BaseNote) {}
}