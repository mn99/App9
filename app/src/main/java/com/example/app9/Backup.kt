package com.example.app9

class Backup(
    val baseNotes: List<BaseNote>,
    val deletedNotes: List<BaseNote>,
    val archivedNotes: List<BaseNote>,
    val labels: HashSet<String>
)