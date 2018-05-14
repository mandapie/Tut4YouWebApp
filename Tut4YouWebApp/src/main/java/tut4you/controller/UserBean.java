/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to findUser a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.
 
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import tut4you.model.*;

/**
 * UserBean checks if a user is authenticated.
 *
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 * Modified by Amanda Pan <daikiraidemodaisuki@gmail.com>
 * Modified by Andrew Kaichi <ahkaichi@gmail.com>
 */
@Named
@SessionScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("UserBean");

    @EJB
    private Tut4YouApp tut4youapp;

    private String email;
    private String pass; // loggin password
    private User user;
    private String oldPassword;
    private String newPassword;
    int tabIndex;
    boolean condition;
    private double hourlyRate; // need double type for <f:convertNumber> tag to work in jsf page
    private String hRate; // need a String type for regex validation to work
    private String currentZip;
    private FlaggedUser flaggedUser;

    /**
     * Creates a new instance of UserIdentity
     */
    @PostConstruct
    public void createUserBean() {
        user = null;
        condition = true;
    }

    /**
     * Destroys a new instance of UserIdentity
     */
    @PreDestroy
    public void destroyUserBean() {
    }

    public FlaggedUser getFlaggedUser() {
        return flaggedUser;
    }

    public void setFlaggedUser(FlaggedUser flaggedUser) {
        this.flaggedUser = flaggedUser;
    }

    public double getHourlyRate() {
        hourlyRate = tut4youapp.getHourlyRate();
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String gethRate() {
        hRate = Double.toString(hourlyRate);
        return hRate;
    }

    public void sethRate(String hRate) {
        this.hRate = hRate;
    }

    public String getCurrentZip() {
        return currentZip;
    }

    public void setCurrentZip(String currentZip) {
        this.currentZip = currentZip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * get boolean condition
     *
     * @return condition
     */
    public boolean isCondition() {
        return condition;
    }

    /**
     * set boolean condition
     *
     * @param condition
     */
    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    /**
     * Gets the Tutor object
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the User object
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the field of the oldPassword
     *
     * @return the field of the old Password
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets the value in the old password to check if password match
     *
     * @param oldPassword the password matching the typed password
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * Gets the field of the new Password
     *
     * @return the field of the new Password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the value of the new password
     *
     * @param newPassword
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Gets the index of the tab
     *
     * @return tabIndex
     */
    public int getTabIndex() {
        return tabIndex;
    }

    /**
     * Sets the index of the tab
     *
     * @param tabIndex
     */
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Called the EJB to switch the state of doNotDisturb
     *
     * @param d
     */
    public void switchDoNotDisturb(Boolean d) {
        tut4youapp.switchDoNotDisturb(d);
    }

    /**
     * Determine if the user is authenticated and if so, make sure the session
     * scope includes the User object for the authenticated user
     *
     * @return true if the user making a request is authenticated, false
     * otherwise.
     */
    public boolean isIsUserAuthenticated() {
        boolean isAuthenticated = true;
        if (null == this.user) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String userName = request.getRemoteUser();
            if (userName == null) {
                isAuthenticated = false;
            } else {
                this.user = tut4youapp.findUser(userName);
                isAuthenticated = (this.user != null);
            }
        }
        return isAuthenticated;
    }

    /**
     * Determine if current authenticated user has the role of tutor
     *
     * @return true if user has role of tutor, false otherwise.
     */
    public boolean isIsTutor() {
        boolean isTutor = false;
        if (this.isIsUserAuthenticated()) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            isTutor = request.isUserInRole("tut4youapp.tutor");
        }
        return isTutor;
    }

    /**
     * Determine if current authenticated user has the role of moderator
     *
     * @return
     */
    public boolean isIsModerator() {
        boolean isModerator = false;
        if (this.isIsUserAuthenticated()) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            isModerator = request.isUserInRole("tut4youapp.moderator");
        }
        return isModerator;
    }

    /**
     * login method to check user is a registered user who is
     *
     * @return result
     * @throws java.text.ParseException
     */
    public String login() throws ParseException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String result = "failure";
        try {
            // check user credentials
            List<String> users = tut4youapp.getUserEmails();
            if (!users.contains(email)) {
                context.addMessage("login:email", new FacesMessage("This email does not exsist"));
                result = "login";
            } else {
                String hashedpass = tut4you.controller.HashPassword.getSHA512Digest(pass);
                User loginUser = tut4youapp.getUser(email);
                if (!hashedpass.equals(loginUser.getPassword())) {
                    context.addMessage("login:pass", new FacesMessage("Incorrect password"));
                    result = "login";
                }
                else if (checkIfSuspended(getCurrentDate()) == true) {
                    context.addMessage("login:error", new FacesMessage("User is currently suspended"));
                    result = "login";
                }
                else {
                    request.login(email, pass); //log user in
                    result = "success";
                }
            }
        } catch (ServletException e) {
            FacesContext.getCurrentInstance().addMessage("login:error", new FacesMessage("Login Failed"));
            result = "login";
        }
        return result;
    }

    /**
     * Logout the student and invalidate the session
     *
     * @return success if student is logged out and session invalidated, failure
     * otherwise.
     */
    public String logout() {
        String result = "failure";
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            if (isIsTutor()) {
                tut4youapp.updateCurrentZip(null);
            }
            request.logout();
            user = null;
            result = "success";
        } catch (ServletException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        return result;
    }

    /**
     * Gets a logged in username by getting the username from the session.
     *
     * @return username Source:
     * https://dzone.com/articles/liferay-jsf-how-get-current-lo Had further
     * help by Subject2Change group.
     */
    public String getEmailFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getRemoteUser();
    }

    /**
     * updates current zip
     */
    public void updateCurrentZip() {
        if (currentZip != null) {
            tut4youapp.updateCurrentZip(currentZip);
        }
        condition = false;
    }

    /**
     * Updates a User's information
     *
     * @param user User or Tutor object
     * @return result
     */
    public String updateUser(User user) {
        String result = "updateProfile";
        double hr = 0;
        if (hRate != null) {
            hr = Double.parseDouble(hRate);
        }
        tut4youapp.updateUser(user, hr);
        return result;
    }

    /**
     * https://stackoverflow.com/questions/33098603/convert-localtime-java-8-to-date
     * gets the current date which is used for date joined attribute in tutor
     *
     * @return date joined
     * @throws ParseException
     */
    public Date getCurrentDate() throws ParseException {
        LocalTime d = LocalTime.now();
        Instant instant = d.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        Date time = Date.from(instant);
        return time;
    }

    /**
     * confirms if the user entered the correct password and if so allows them
     * to change their password
     *
     * @return result
     */
    public String changePassword() {
        FacesContext context = FacesContext.getCurrentInstance();
        String confirmPassword = tut4you.controller.HashPassword.getSHA512Digest(oldPassword);
        String result = "failure";
        String currentPassword = user.getPassword();
        if (confirmPassword.equalsIgnoreCase(currentPassword)) {
            tut4youapp.changePassword(tut4you.controller.HashPassword.getSHA512Digest(newPassword));
            result = "updateProfile";
        } else {
            context.addMessage("change-password:currentpassword", new FacesMessage("Password entered does not match your current password"));
            result = "changePassword";
        }
        return result;
    }

    public boolean isSubmittedTranscript() {
        return tut4youapp.hasSubmittedTranscript();
    }

    public boolean checkIfSuspended(Date logInTime) {
        flaggedUser = findFlaggedUser(email);
        if (flaggedUser != null) {
            double diff = logInTime.getTime() - flaggedUser.getDateFlagged().getTime();
            double minutes = (diff / 1000) / 60;
            int count = flaggedUser.getCount();
            if (count == 1 && minutes < 1) {
                return true;
            } else if (count == 2 && minutes < 3) {
                return true;
            } else if (count == 3 && minutes < 5) {
                return true;
            } else {
                return count == 4;
            }
        } else {
            return false;
        }
    }

    public FlaggedUser findFlaggedUser(String email) {
        flaggedUser = tut4youapp.checkFlaggedUserLogIn(email);
        return flaggedUser;
    }
}
