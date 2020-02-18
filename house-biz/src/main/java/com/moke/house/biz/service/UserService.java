package com.moke.house.biz.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Lists;
import com.moke.house.biz.mapper.UserMapper;
import com.moke.house.common.constants.CommonConstants;
import com.moke.house.common.model.User;
import com.moke.house.common.utils.BeanHelper;
import com.moke.house.common.utils.HashUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private  FileService fileService;
    @Autowired
    MailService mailService;

    @Value("${domain.name}")
    private String domainName;
    @Value("${file.prefix}")
    private String imagePrefix;

    private final Cache<String, String> cache
            = CacheBuilder.newBuilder().maximumSize(100) //设置缓存大小为100
            .expireAfterAccess(15, TimeUnit.MINUTES) //数据15分钟后失效
            .removalListener(new RemovalListener<String, String>() { //数据移除时需要做的动作
                @Override
                public void onRemoval(RemovalNotification<String, String> removalNotification) {
                    userMapper.delete(removalNotification.getValue());
                }
            }).build();

    public List<User> getUsers(){
        return userMapper.selectUsers();
    }
    @Transactional(rollbackFor = Exception.class)
    public boolean addAccount(User account){
        account.setPasswd(HashUtils.encryPassword(account.getPasswd()));
        List<String> list = fileService.getImagePath(Lists.newArrayList(account.getAvatarFile()));
        if(!list.isEmpty())
            account.setAvatar(list.get(0));
        BeanHelper.setDefaultProp(account,User.class);
        BeanHelper.onInsert(account);
        account.setEnable(0);
        userMapper.insert(account);
        registerNotify(account.getEmail());
        return false;
    }

    /**
     * 1.缓存 key-email的关系
     * 2.借助spring mail 发送邮件
     * 3.借助异步框架进行异步操作
     * @param email
     */
    private void registerNotify(String email) {
        String randomKey = RandomStringUtils.randomAlphabetic(15);
        cache.put(randomKey,email);
        String url = "http://"+domainName+"/accounts/verify?key="+randomKey;

        mailService.sendMail("register confirm",url,email);
    }

    public boolean enable(String key) {
        String email = cache.getIfPresent(key);
        if(StringUtils.isBlank(email))
            return false;
        User user =  new User();
        user.setEnable(1);
        user.setEmail(email);
        userMapper.update(user);
        return true;
    }

    public User auth(String name, String pass) {
        User user = new User();
        user.setName(name);
        user.setPasswd(HashUtils.encryPassword(pass));
        user.setEnable(1);
        List<User> list  =userMapper.selectUsersByQuery(user);
        if(!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }
    public List<User> getUserByQuery(User user){
        List<User> list  = userMapper.selectUsersByQuery(user);
        list.forEach(u->{
            u.setAvatar(imagePrefix+u.getAvatar());
        });
        return list;
    }

    public void updateUser(User updateUser, String email) {
        updateUser.setEmail(email);
        BeanHelper.onUpdate(updateUser);
        userMapper.update(updateUser);

    }
}
