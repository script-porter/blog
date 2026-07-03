package com.example.demo.dto;

import com.example.demo.common.ValidUtils.Phone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "手机号不能为空")
    @Phone(message = "手机号格式不正确")
    private String phone;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度在6-20位之间")
    private String password;
}
