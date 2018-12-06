package com.bcd.config.shiro.anno;

import com.bcd.base.config.shiro.RequiresUrlPermission;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;
import java.util.Set;
@SuppressWarnings("unchecked")
public class UrlPermissionAnnotationHandler extends AuthorizingAnnotationHandler {

    public UrlPermissionAnnotationHandler() {
        super(RequiresUrlPermission.class);
    }

    private ThreadLocal<Set<String>>  actionPermission = new ThreadLocal<>();
    public void setActionPermission(Set<String> permission){
        actionPermission.set(permission);
    }
    public Set<String> getActionPermission(){
        return actionPermission.get();
    }

    @Override
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        Subject subject = getSubject();
        Set<String> permissionSet=getActionPermission();
        if(permissionSet==null||permissionSet.size()==0){
            return;
        }
        boolean[] flag=new boolean[]{false};
        permissionSet.forEach(e->{
            try {
                subject.checkPermission(e);
                flag[0]=true;
                return;
            }catch (AuthorizationException ex){

            }
        });
        if(!flag[0]){
            throw new AuthorizationException("No Url Permission!");
        }
    }

}