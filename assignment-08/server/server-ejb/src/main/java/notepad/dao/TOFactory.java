package notepad.dao;

import notepad.db.Note;
import notepad.common.NoteTO;

class TOFactory {

    static NoteTO buildTO(Note note) {
        return new NoteTO(note.getId(), note.getCreated(), note.getLastModified(), note.getContent());
    }
}
