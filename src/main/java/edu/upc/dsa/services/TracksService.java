package edu.upc.dsa.services;


import edu.upc.dsa.TracksManager;
import edu.upc.dsa.TracksManagerImpl;
import edu.upc.dsa.models.Track;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/tracks", description = "Endpoint to Track Service")
@Path("/tracks")
public class TracksService {

    private TracksManager tm;

    public TracksService() {
        this.tm = TracksManagerImpl.getInstance();
        if (tm.size()==0) {
            this.tm.addTrack("La Barbacoa", "Georgie Dann");
            this.tm.addTrack("Despacito", "Luis Fonsi");
            this.tm.addTrack("Enter Sandman", "Metallica");
        }


    }

    @GET
    @ApiOperation(value = "get all Track", notes = "Get all tracks from our library")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Track.class, responseContainer="List"),
    })
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks() {

        List<Track> tracks = this.tm.findAll();

        GenericEntity<List<Track>> entity = new GenericEntity<List<Track>>(tracks) {};
        return Response.status(201).entity(entity).build()  ;

    }

    @GET
    @ApiOperation(value = "consigue una canción", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Exitoso", response = Track.class),
            @ApiResponse(code = 404, message = "Track no encontrado")
    })
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrack(@PathParam("id") String id) {
        Track t = this.tm.getTrack(id);
        if (t == null) return Response.status(404).build();
        else  return Response.status(201).entity(t).build();
    }

    @DELETE
    @ApiOperation(value = "borra un Track", notes = "Elimina de la biblio un track")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Finalizado con exito"),
            @ApiResponse(code = 404, message = "Track no encontrado")
    })
    @Path("/{id}")
    public Response deleteTrack(@PathParam("id") String id) {
        Track t = this.tm.getTrack(id);
        if (t == null) return Response.status(404).build();
        else this.tm.deleteTrack(id);
        return Response.status(201).build();
    }

    @PUT
    @ApiOperation(value = "Actualiza un Track", notes = "Actualiza la info si ecnuentra el artista")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Track actualizado"),
            @ApiResponse(code = 404, message = "Track not found")
    })
    @Path("/update")
    public Response updateTrack(Track track) {

        Track t = this.tm.updateTrack(track);

        if (t == null) return Response.status(404).build();

        return Response.status(201).build();
    }



    @POST
    @ApiOperation(value = "crea un nuevo Track", notes = "Nueva canción")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=Track.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })

    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newTrack(Track track) {

        if (track.getSinger()==null || track.getTitle()==null)  return Response.status(500).entity(track).build();
        this.tm.addTrack(track);
        return Response.status(201).entity(track).build();
    }

}