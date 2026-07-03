package com.example.demo.dto;

import com.example.demo.entity.User;
import com.example.demo.entity.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileVO {
    private User user;
    private UserInfo userInfo;
}
