package com.mdata.thirdparty.dianli.frontend.web.services.shiro;

import org.springframework.stereotype.Service;

/**
 * Created by administrator on 16/5/15.
 */
@Service
public class UserServiceImpl implements  UserService {
    public User findByLoginName(String loginName){
        User user=new User();
        user.setName("li");
        return user;
    }


}
