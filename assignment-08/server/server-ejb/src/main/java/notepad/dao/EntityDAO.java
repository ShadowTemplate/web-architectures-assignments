package notepad.dao;

import notepad.db.Note;
import notepad.common.NoteTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityDAO {

    private EntityManager entityManager;

    public EntityDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<NoteTO> listNotes() {
        String query = "SELECT n FROM Note n";
        List<Note> notes = entityManager.createQuery(query).getResultList();
        return notes.stream().map(TOFactory::buildTO).collect(Collectors.toList());
    }

    public NoteTO createNote(String content) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Note newNote = new Note(date, date, content);
        entityManager.persist(newNote);
        return TOFactory.buildTO(newNote);
    }

    public NoteTO updateNote(Integer id, String content) {
        String query = "SELECT n FROM Note n WHERE n.id=:arg1";
        Map<String, Object> params = new HashMap<>();
        params.put("arg1", id);
        Note note = (Note) buildQuery(entityManager, query, params).getSingleResult();
        note.setContent(content);
        note.setLastModified(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        entityManager.persist(note);
        return TOFactory.buildTO(note);
    }

    public Integer deleteNote(Integer id) {
        String query = "SELECT n FROM Note n WHERE n.id=:arg1";
        Map<String, Object> params = new HashMap<>();
        params.put("arg1", id);
        Note note = (Note) buildQuery(entityManager, query, params).getSingleResult();
        note = entityManager.merge(note);
        entityManager.remove(note);
        return id;
    }

    private Query buildQuery(EntityManager entityManager, String query, Map<String, Object> parameters) {
        Query q = entityManager.createQuery(query);
        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
        }
        return q;
    }
}
