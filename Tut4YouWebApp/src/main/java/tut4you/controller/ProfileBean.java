/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import tut4you.model.*;
import javax.inject.Inject;

/**
 * Shows profile page of a user.
 * @author Amanda
 */
@Named
@RequestScoped
public class ProfileBean {
    @EJB
    private Tut4YouApp tut4youapp;
    
    @Inject
    UserBean userBean;
    
    private String userName;
    private User user;
    
    /**
     * Creates a new instance of ProfileBean
     */
    @PostConstruct
    public void createProfileBean() {
        String userEmail = userBean.returnUserEmailFromSession();
        if (userEmail.equals(user.getEmail())) {
            userName = user.getUserName();
        }
    }
    
    @PreDestroy
    public void destroyProfileBean() {
        user = null;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public void showUserName() {
        userName = userBean.returnUserEmailFromSession();
    }
//    public void getUsernameFromSession() {
//        FacesContext context = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//        String userEmail = request.getRemoteUser();
//        user = tut4youapp.find(userEmail);
//        userName = user.getUserName();
//        //return userName;
//        //ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext().;
//        context.getApplication().getNavigationHandler().handleNavigation(context, null,"success");
//    }
//    public String getPassedParameter() {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        this.userName = (String) facesContext.getExternalContext().getRequestParameterMap().get("username");
//        return this.userName;
//    }
//    public void setPassedParameter(String passedParameter) {
//        this.userName = passedParameter;
//    }
}