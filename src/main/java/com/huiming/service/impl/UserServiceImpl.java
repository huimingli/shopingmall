package com.huiming.service.impl;

import com.huiming.common.Const;
import com.huiming.common.ServerResponse;
import com.huiming.common.TokenCache;
import com.huiming.dao.UserMapper;
import com.huiming.pojo.User;
import com.huiming.service.IUserService;
import com.huiming.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/*
 * @author:15737
 * @createtime:2017/11/07 21:58
 * @desc:
*/
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("user has not registered");
        }
        String md5Passowrd = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Passowrd);
        if (user == null) {
            return ServerResponse.createByErrorMessage("password is wrong");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("login success", user);
    }

    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getUsername(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("register fail");
        }
        return ServerResponse.createBySuccessMessage("register success");
    }

    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage(" username exists");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage(" email exists");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("param is invalid");
        }
        return ServerResponse.createBySuccessMessage("param is valid");
    }

    public ServerResponse selectQuestion(String username){
        ServerResponse validRespose = this.checkValid(username,Const.USERNAME);
        if(validRespose.isSuccess()){
            return ServerResponse.createByErrorMessage("user does not exist");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("the question of finding password does not exist");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("answer of the question is wrong");
    }

    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        if (StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("param is wrong,need token");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token is invalid");
        }

        if (StringUtils.equals(forgetToken,token)){
            String md5password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5password);

            if(rowCount>0){
                return ServerResponse.createBySuccessMessage("reset password successfully");
            }
        }else{
            return ServerResponse.createByErrorMessage("token is wrong,please make another one");
        }
        return ServerResponse.createByErrorMessage("fail to reset password");
    }

    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("old password is wrong");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));;
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("reset password successfully");
        }
        return ServerResponse.createByErrorMessage("fail to reset password ");
    }

    public ServerResponse<User> updateInformation(User user){
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email exist,please use another email");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount>0){
            return ServerResponse.createBySuccessMessage("update user information successfully");
        }
        return ServerResponse.createByErrorMessage("fail to update user information");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("user does exist");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);

    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

}
