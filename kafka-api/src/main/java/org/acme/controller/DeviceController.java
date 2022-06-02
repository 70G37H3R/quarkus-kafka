package org.acme.controller;
import io.smallrye.mutiny.Multi;
import org.acme.kafka.AvroDevice;
import org.acme.response.ResponseObject;
import org.acme.service.device.Device;
import org.acme.service.device.DeviceService;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;

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

@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "device", description = "Device Operations")
@AllArgsConstructor
@Slf4j
public class DeviceController {

    @Inject
    DeviceService deviceService;

    @Channel("demo-from-kafka")
    Multi<AvroDevice> avroDevice;

    @GET
    public Multi<String> stream() {
        return avroDevice.map(avroDevice -> String.format("'%s' from %s", avroDevice.getId(), avroDevice.getStatus()));
    }

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
            List<Device> data = deviceService.findAll();
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
    public Response getById(@Parameter(name = "id", required = true) @PathParam("id") Integer deviceId) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Optional<Device> data = deviceService.findById(deviceId);
            responseObject.getSuccess(data);
            return Response.ok(responseObject).build();
        } catch (Exception e) {
            log.error("Failed to get data by id", e);
            responseObject.getFailed();
            return Response.serverError().entity(responseObject).build();
        }
    }

    @GET
    @Path("/getByMac/{macAddress}")
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
            description = "Device does not exist for Mac Adress",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getByMac(@Parameter(name = "macAddress", required = true) @PathParam("macAddress") String deviceMacAddress) {
        ResponseObject responseObject = new ResponseObject();
        try {
            Optional<Device> data = deviceService.findByMac(deviceMacAddress);
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
    public Response post(@NotNull @Valid Device device, @Context UriInfo uriInfo) {
        ResponseObject responseObject = new ResponseObject();
        try {
            deviceService.validateAddress(device);
            deviceService.save(device);
            responseObject.createSuccess(device);
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(device.getId())).build();
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
    public Response put( @NotNull @Valid Device device) {
        ResponseObject responseObject = new ResponseObject();
        try {
            deviceService.validateUpdate(device);
            deviceService.update(device);
            responseObject.updateSuccess(device);
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
            deviceService.deleteAll();
            return Response.ok(responseObject).build();
        } catch (Exception e) {
            log.error("Failed to get data by id", e);
            responseObject.deleteFailed();
            return Response.serverError().entity(responseObject).build();
        }
    }

}






