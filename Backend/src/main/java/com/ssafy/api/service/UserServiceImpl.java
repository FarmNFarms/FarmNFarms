package com.ssafy.api.service;

import com.ssafy.api.request.UserLoginPostReq;
import com.ssafy.api.request.UserInfoChangePutReq;
import com.ssafy.api.request.UserInfoChangePutReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.response.UserInfoChangePutRes;
import com.ssafy.api.response.UserInfoGetRes;
import com.ssafy.domain.user.User;
import com.ssafy.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean createUser(UserRegisterPostReq userRegisterInfo) {
        if (userRepository.findByPhone(userRegisterInfo.getPhone()) != null) {
            return false;
        }

        User user = new User();
        user.setPhone(userRegisterInfo.getPhone());
        user.setPhone_auth(false);
        user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
        user.setName(userRegisterInfo.getName());
        user.setAddress(userRegisterInfo.getAddress());
        user.setAccount(userRegisterInfo.getAccount());
        user.setAbout_me("자기소개를 입력해주세요");
        user.setGrade(0);
        user.setData_create(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Override
    public User getUserByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        return user;
    }

    @Override
    public boolean deleteUser(UserLoginPostReq deleteInfo) {
        if(passwordEncoder.matches(deleteInfo.getPassword(), userRepository.findByPhone(deleteInfo.getPhone()).getPassword())){
            userRepository.deleteByPhone(deleteInfo.getPhone());
            return true;
        }
        return false;
    }


    @Override
    public UserInfoGetRes getUserInfo(String phone) {

        User user = userRepository.findByPhone(phone);

        return new UserInfoGetRes(user.getPhone(), user.getAccount(), user.getAddress());

//        return UserInfoGetRes.builder()
//                .phone(user.getPhone())
//                .account(user.getAccount())
//                .address(user.getAddress())
//                .build();
    }

    @Override
    public boolean updateUserInfo(UserInfoChangePutReq data, String phone) {

        try {
            if (data.getAccount() == null || data.getAddress() == null) {
                throw new IllegalArgumentException();
            }
            User user = userRepository.findByPhone(phone);

            user.setAccount(data.getAccount());
            user.setAddress(data.getAddress());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
