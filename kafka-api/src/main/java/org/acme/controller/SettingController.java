package org.acme.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.response.ResponseObject;
import org.acme.service.device.Device;
import org.acme.service.setting.Setting;
import org.acme.service.setting.SettingService;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/settings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "setting", description = "Setting Operations")
@AllArgsConstructor
@Slf4j
public class SettingController {

    @Inject
    SettingService settingService;

    @GET
    @APIResponse(
            responseCode = "200",
            description = "Get All Devices",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Device.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "DB may be not connected",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getAll() {
        ResponseObject responseObject = new ResponseObject();
        try {
            List<Setting> data = settingService.findAll();
            responseObject.getSuccess(data);
            return Response.ok(responseObject).build();
        } catch (Exception e) {
            log.error("Failed to get all data", e);
            responseObject.getFailed();
            return Response.serverError().entity(responseObject).build();
        }
    }

    @GET
    @Path("/getById/{id}")
    @APIResponse(
            responseCode = "200",
            description = "Get Device by Id",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Device.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "DB may be not connected",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getById(@Parameter(name = "id", required = true) @PathParam("id") Integer settingId) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Optional<Setting> data = settingService.findById(settingId);
            responseObject.getSuccess(data);
            return Response.ok(responseObject).build();
        } catch (Exception e) {
            log.error("Failed to get data by id", e);
            responseObject.getFailed();
            return Response.serverError().entity(responseObject).build();
        }
    }

    @POST
    @APIResponse(
            responseCode = "201",
            description = "Device Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Device.class)
            )
    )
    @APIResponse(
            responseCode = "409",
            description = "Invalid Device",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response post(@NotNull @Valid Setting setting, @Context UriInfo uriInfo) {
        ResponseObject responseObject = new ResponseObject();
        try {

            settingService.save(setting);
            responseObject.createSuccess(setting);
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(setting.getId())).build();
            return Response.created(uri).entity(responseObject).build();
        } catch (Exception e) {
            log.error("Failed to get data by id", e);
            responseObject.createFailed();
            return Response.serverError().entity(responseObject).build();
        }
    }

    @PUT
    @APIResponse(
            responseCode = "204",
            description = "Device updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Device.class)
            )
    )
    @APIResponse(
            responseCode = "409",
            description = "Invalid Device",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response put( @NotNull @Valid Setting setting) {
        ResponseObject responseObject = new ResponseObject();
        try {

            settingService.update(setting);
            responseObject.updateSuccess(setting);
            return Response.ok(responseObject).build();
        } catch (Exception e) {
            log.error("Failed to get data by id", e);
            responseObject.updateFailed();
            return Response.serverError().entity(responseObject).build();
        }
    }

    @DELETE
    @APIResponse(
            responseCode = "204",
            description = "Delete All Devices",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Device.class)
            )
    )
    @APIResponse(
            responseCode = "500",
            description = "DB may be not connected",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Device.class)
            )
    )
    public Response deleteAll() {
        ResponseObject responseObject = new ResponseObject();
        try {
            settingService.deleteAll();
            responseObject.deleteSuccess();
            return Response.ok(responseObject).build();
        } catch (Exception e) {
            log.error("Failed to get data by id", e);
            responseObject.deleteFailed();
            return Response.serverError().entity(responseObject).build();
        }
    }

}
