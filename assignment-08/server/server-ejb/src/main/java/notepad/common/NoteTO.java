package notepad.common;

import java.io.Serializable;

public class NoteTO implements Serializable {
    private int id;
    private String created;
    private String lastModified;
    private String content;

    public NoteTO(Integer id, String created, String lastModified, String content) {
        this.id = id;
        this.created = created;
        this.lastModified = lastModified;
        this.content = content;
    }
}

