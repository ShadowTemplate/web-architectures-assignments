package notepad.common;

import java.util.List;

public interface NotepadBeanI {

    List<NoteTO> list();
    NoteTO create(String content);
    NoteTO update(Integer id, String content);
    Integer delete(Integer id);
}
