package org.example.passsystem.webapp;

import org.example.passsystem.service.PassService;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/check")
public class PassWebapp {
    @Context private ServletContext context;
    private static PassService passService;

    @GET
    public Response checkAccess(@QueryParam("roomId") int roomId, @QueryParam("entrance") boolean isEntrance, @QueryParam("keyId") int keyId) {
        try {
            if (passService == null) {
                this.Init();
            }
            passService.Check(roomId, isEntrance, keyId);
            return Response.status(Response.Status.OK).build();
        } catch (WebApplicationException e) {
            return e.getResponse();
        }
    }

    private void Init() {
        String logConfig = this.context.getRealPath("/")+this.context.getInitParameter("log4j-config-location");
        passService = new PassService(logConfig);
    }
}