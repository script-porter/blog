package com.example.demo.service;

import com.example.demo.dto.ProfileVO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * 用户业务接口
 */
public interface UserService {

    User save(RegisterDTO registerDTO);

    User update(User user);

    void deleteById(Long id);

    Optional<User> findById(Long id);

    ProfileVO findUserInfo(String phone);

    List<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> findByPhone(String phone);

    User login(String phone, String password);

    boolean existsByPhone(String phone);
}
