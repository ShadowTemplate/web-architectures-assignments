package notepad.common;

import java.io.Serializable;

public class NoteTO implements Serializable {
    private int id;
    private String created;
    private String lastModified;
    private String content;

    public NoteTO() {

    }

    public NoteTO(Integer id, String created, String lastModified, String content) {
        this.id = id;
        this.created = created;
        this.lastModified = lastModified;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteTO note = (NoteTO) o;

        return id == note.id;
    }

    @Override
    public String toString() {
        return "NoteTO{" +
                "id=" + id +
                ", created='" + created + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return id;
    }
}

