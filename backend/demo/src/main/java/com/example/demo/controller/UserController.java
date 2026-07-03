package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.JwtTokenUtil;
import com.example.demo.common.Result;
import com.example.demo.dto.LoginVO;
import com.example.demo.dto.ProfileVO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

/**
 * 用户控制层
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public Result<User> register(@Valid @RequestBody RegisterDTO registerDTO) {
        if (userService.existsByPhone(registerDTO.getPhone())) {
            return Result.conflict("手机号已注册");
        }
        return Result.success("创建成功", userService.save(registerDTO));
    }

    @GetMapping("/profile")
    public Result<ProfileVO> updateProfile(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userPhon = jwtTokenUtil.extractUserPhon(token);
        return Result.success(userService.findUserInfo(userPhon));
    }

    @PutMapping("/{id}")
    public Result<User> update(@PathVariable Long id, @RequestBody User user) {
        return userService.findById(id)
                .map(existing -> {
                    user.setId(id);
                    return Result.success("更新成功", userService.update(user));
                })
                .orElse(Result.notFound("用户不存在"));
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
            return Result.success("删除成功");
        }
        return Result.notFound("用户不存在");
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(Result::success)
                .orElse(Result.notFound("用户不存在"));
    }

    @GetMapping
    public Result<List<User>> list() {
        return Result.success(userService.findAll());
    }

    @GetMapping("/by-username")
    public Result<User> getByUsername(@RequestParam String username) {
        return userService.findByUsername(username)
                .map(Result::success)
                .orElse(Result.notFound("用户不存在"));
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody RegisterDTO loginDTO) {
        try {
            User user = userService.login(loginDTO.getPhone(), loginDTO.getPassword());
            String token = jwtTokenUtil.generateToken(user.getPhone());
            LoginVO loginVO = LoginVO.builder()
                    .token(token).username(user.getUsername())
                    .build();
            return Result.success("登录成功", loginVO);
        } catch (RuntimeException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
