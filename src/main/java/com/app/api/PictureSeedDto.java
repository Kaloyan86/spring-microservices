package com.app.api;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class PictureSeedDto {

    @Expose
    @Size(min = 2, max = 20)
    private String name;
    @Expose
    private String dateAndTime;
    @Expose
    private Long car;
}
