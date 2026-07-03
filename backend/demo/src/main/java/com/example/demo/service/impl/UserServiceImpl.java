package com.example.demo.service.impl;

import com.example.demo.common.CommonUtils;
import com.example.demo.dto.ProfileVO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

/**
 * 用户业务实现
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public User save(RegisterDTO registerDTO) {
        User user = new User();
        user.setPhone(registerDTO.getPhone());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        boolean existsByPhone = userRepository.existsByPhone(registerDTO.getPhone());

        if (existsByPhone) {
            throw new RuntimeException("手机号已注册");
        }

        user.setUsername(CommonUtils.randomNickname());

        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public User login(String phone, String password) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("手机号未注册"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public ProfileVO findUserInfo(String phone) {
        User user = this.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        UserInfo userInfo = userInfoRepository.findByUserId(user.getId())
                .orElseGet(UserInfo::new);

        return new ProfileVO(user, userInfo);
    }
}
