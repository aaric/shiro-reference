package com.bcd.config.shiro.anno;

import com.bcd.base.config.shiro.anno.RequiresUserInfo;
import com.bcd.sys.bean.UserBean;
import com.bcd.sys.util.ShiroUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class UserInfoAnnotationHandler extends AuthorizingAnnotationHandler {

    public UserInfoAnnotationHandler() {
        super(RequiresUserInfo.class);
    }

    @Override
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        long[] ids=((RequiresUserInfo)a).id();
        String[] usernames=((RequiresUserInfo)a).username();
        Logical logical=((RequiresUserInfo)a).logical();
        UserBean userBean= ShiroUtil.getCurrentUser();
        if(logical==Logical.AND){
            if((ids.length==0||Arrays.stream(ids).mapToObj(e->e==userBean.getId()).allMatch(e->e)) &&
                    (usernames.length==0||Arrays.stream(usernames).map(e->e.equals(userBean.getUsername())).allMatch(e->e))){
                throw new AuthorizationException("");
            }
        }else if(logical==Logical.OR){
            if((ids.length==0||Arrays.stream(ids).mapToObj(e->e==userBean.getId()).anyMatch(e->e)) &&
                    (usernames.length==0||Arrays.stream(usernames).map(e->e.equals(userBean.getUsername())).anyMatch(e->e))){
                throw new AuthorizationException("");
            }
        }

    }

}