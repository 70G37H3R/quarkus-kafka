package org.acme.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ResponseObject {
    private int returnCode;
    private String message;
    private Object data;

    public void getSuccess(Object data) {
        this.setReturnCode(200);
        this.setMessage("Fetched data successfully");
        this.setData(data);
    }

    public void createSuccess(Object data) {
        this.setReturnCode(201);
        this.setMessage("Create data successfully");
        this.setData(data);
    }

    public void updateSuccess(Object data) {
        this.setReturnCode(204);
        this.setMessage("Update data successfully");
        this.setData(data);
    }

    public void deleteSuccess() {
        this.setReturnCode(204);
        this.setMessage("Delete data successfully");

    }

    public void getFailed() {
        this.setReturnCode(404);
        this.setMessage("Get data failed");
    }

    public void createFailed() {
        this.setReturnCode(409);
        this.setMessage("Create data failed");
        this.setData(data);
    }

    public void updateFailed() {
        this.setReturnCode(409);
        this.setMessage("Update data failed");

    }

    public void deleteFailed() {
        this.setReturnCode(400);
        this.setMessage("Delete data failed");

    }


}
