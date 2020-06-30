package org.example.passsystem.webapp;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;

public class PassSystemTest {
    private Client client;
    private WebTarget webTarget;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target("http://localhost:8081/check");
    }

    @Test
    public void testValidEntrance() {
        Response response = checkEntrance(3, true, 15);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testValidExit() {
        Response response = checkEntrance(3, false, 15);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testIncorrectRoomOrKey() {
        Response response = checkEntrance(10, true, 0);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testReenterAttempt() {
        Response response = checkEntrance(3, true, 333);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = checkEntrance(5, true, 333);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        checkEntrance(3, false, 333);
    }

    @Test
    public void testAccessDenied() {
        Response response = checkEntrance(4, true, 9);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void testInvalidExit() {
        Response response = checkEntrance(5, false, 100);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    private Response checkEntrance(int roomId, boolean isEntrance, int keyId) {
        return this.webTarget.queryParam("roomId", roomId).queryParam("entrance",isEntrance).queryParam("keyId", keyId).request().get();
    }
}