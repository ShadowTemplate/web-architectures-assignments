package notepad.server;

import notepad.common.NoteTO;
import notepad.common.NotepadBeanI;
import notepad.dao.EntityDAO;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.function.Function;

@Stateless
@Remote(NotepadBeanI.class)
public class NotepadBean implements NotepadBeanI {

    @PersistenceContext(unitName = "NotepadPU")
    private EntityManager entityManager;

    public NotepadBean() {

    }

    @Override
    public List<NoteTO> list() {
        return tryFunction(EntityDAO::listNotes);
    }

    @Override
    public NoteTO create(String content) {
        return tryFunction(entityDAO -> entityDAO.createNote(content));
    }

    @Override
    public NoteTO update(Integer id, String content) {
        return tryFunction(entityDAO -> entityDAO.updateNote(id, content));
    }

    @Override
    public Integer delete(Integer id) {
        return tryFunction(entityDAO -> entityDAO.deleteNote(id));
    }

    private <T> T tryFunction(Function<EntityDAO, T> function) {
        try {
            return function.apply(new EntityDAO(entityManager));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
