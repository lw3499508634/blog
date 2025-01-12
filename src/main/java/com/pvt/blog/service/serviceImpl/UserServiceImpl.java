package com.pvt.blog.service.serviceImpl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.pvt.blog.common.RoleConstant;
import com.pvt.blog.filter.CustomWebAuthenticationDetails;
import com.pvt.blog.pojo.Role;
import com.pvt.blog.pojo.User;
import com.pvt.blog.pojo.dto.UserDTO;
import com.pvt.blog.pojo.vo.UserVO;
import com.pvt.blog.repository.RoleRepository;
import com.pvt.blog.repository.UserRepository;
import com.pvt.blog.enums.ResultEnum;

import com.pvt.blog.service.IUserService;
import com.pvt.blog.utils.JwtTokenProvider;
import com.pvt.blog.utils.RedisUtil;
import com.pvt.blog.utils.ResultResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author LW
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private JwtTokenProvider jwtTokenProvider;
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleRepository roleRepository;

    @Override
    public ResultResponse<UserVO> userLogin(UserDTO userDto) {
        try {
            // valid email
            if (!Validator.isEmail(userDto.getEmail())) {
                return ResultResponse.error(ResultEnum.FAIL_EMAIL_FORMAT);
            }

            // get user information
            User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new RuntimeException(ResultEnum.FAIL_USER_NOT_EXIST.getMessage()));

            // get authentication
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), userDto.getPassword()));

            // add authentication to SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // generate authorization
            String authorization = jwtTokenProvider.generateToken(authentication);

            return ResultResponse.success(ResultEnum.SUCCESS, new UserVO(user.getUsername(), user.getEmail(), authorization, user.getAvatar()));

        } catch (Exception e) {
            return ResultResponse.error(ResultEnum.FAIL_USER_NOT_EXIST);
        }
    }

    /*
        1. 判断用户输入的用户名是否为邮箱
        2. 检查数据库中是否存在该用户（用户名重复）
        3. 创建该用户并保存进数据库
     */
    @Override
    public ResultResponse<String> userRegister(UserDTO user) {
        if (!Validator.isEmail(user.getEmail())) {
            // email 格式不正确
            return ResultResponse.error(ResultEnum.FAIL_EMAIL_FORMAT);
        }

        if (userRepository.existsUserByEmail(user.getEmail())) {
            // 注册的用户已存在
            return ResultResponse.error(ResultEnum.FAIL_USER_EXIST);
        }

        User newUser = new User();

        newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        newUser.setEmail(user.getEmail());

        // generating a random username
        newUser.setUsername(StrUtil.uuid());

        newUser.setStatus(0);

        Optional<Role> role = roleRepository.findByName(RoleConstant.GUEST);
        // role.orElse(null):表示如果 role 为空，则返回括号中的内容，否则就返回实体
        newUser.setRoles(Collections.singleton(role.orElse(null)));
        userRepository.save(newUser);
        return ResultResponse.success(ResultEnum.SUCCESS_USER_REGISTER, null);
    }

    @Override
    public ResultResponse<Long> getUserCount() {

        return ResultResponse.success(ResultEnum.SUCCESS, userRepository.count());
    }

    @Override
    public ResultResponse<String> deleteUser(Long id) {
        userRepository.deleteById(id);
        return ResultResponse.success(ResultEnum.SUCCESS, null);
    }

    /*
        获取所有用户
     */
    @Override
    public ResultResponse<List<User>> getAllUser() {
        List<User> userList = userRepository.findAll();
        return new ResultResponse<>(ResultEnum.SUCCESS, userList);
    }

    public String getStatues(Integer state) {
        switch (state) {
            case 0:
                return "active";
            case 1:
                return "paused";
            case 2:
                return "vacation";
            default:
                break;
        }
        return null;
    }
//
//    @Override
//    public User1 getUserById(String id) {
//        Optional<User1> byId = userRepository.findById(Long.parseLong(id));
//        return byId.orElse(null);
//    }

}
