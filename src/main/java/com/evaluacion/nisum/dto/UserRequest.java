package com.evaluacion.nisum.dto;

import com.evaluacion.nisum.model.Phone;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
}