package com.bibek.generics.pojo;

import com.bibek.generics.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalApiResponse implements Serializable {
    private ResponseStatus status;
    private String message;
    private Object data;

    public void setResponse(String message, ResponseStatus status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

}