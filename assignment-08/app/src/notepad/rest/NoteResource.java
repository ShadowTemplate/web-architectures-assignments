package notepad.rest;

import notepad.common.NoteTO;
import notepad.common.NotepadBeanI;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.function.Function;

@ApplicationPath("rest")
@Path("note")
public class NoteResource extends Application {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public NoteTO createNote(@QueryParam("content") String content) {
        return tryFunction(notepadBeanI -> notepadBeanI.create(content));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<NoteTO> listNotes() {
        System.out.println("\n\nLIST REQ");
        List<NoteTO> noteTOs = tryFunction(NotepadBeanI::list);
        System.out.println(noteTOs);
        return noteTOs;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public NoteTO updateNote(@QueryParam("id") Integer id, @QueryParam("content") String content) {
        return tryFunction(notepadBeanI -> notepadBeanI.update(id, content));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Integer deleteNote(@QueryParam("id") Integer id) {
        return tryFunction(notepadBeanI -> notepadBeanI.delete(id));
    }

    private <T> T tryFunction(Function<NotepadBeanI, T> function) {
        try {
            return function.apply(RemoteProxy.getNotepad());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
