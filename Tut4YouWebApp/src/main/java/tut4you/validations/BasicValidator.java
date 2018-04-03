/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to find a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.
 *  
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.validations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import tut4you.controller.RequestBean;
import tut4you.model.Tut4YouApp;

/**
 * Validates (basic) correct format.
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 */
@Named
@RequestScoped
public class BasicValidator {

    @EJB
    private Tut4YouApp tut4YouApp;

    /**
     * Creates a new instance of BasicValidator
     */
    public BasicValidator() {
    }

    /**
     * validates an e-mail address to be in the (basic) correct format.
     * @param context the FacesContext
     * @param toValidate the UIComponent being validated (e-mail field)
     * @param value the value (email address) of the component
     * @throws ValidatorException the Exception to throw b/c the value is not an e-mail address
     * 
     * TODO: currently only checks for @ symbol, need to implement a more sophisticated validator
     */
    public void validateEmail(FacesContext context, UIComponent toValidate, Object value) throws ValidatorException {
        String emailStr = (String) value;
        if (-1 == emailStr.indexOf("@")) {
            FacesMessage message = new FacesMessage("Invalid email address");
            throw new ValidatorException(message);
        }
        for(String str : tut4YouApp.getUserEmails())
            if(emailStr.equals(str)) {
                FacesMessage message = new FacesMessage("email address already in use");
                throw new ValidatorException(message);
            }
    }
    /**
     * https://stackoverflow.com/questions/42104546/java-regular-expressions-to-validate-phone-numbers
     * 
     * @param context the FacesContext
     * @param toValidate the UIComponent being validated (e-mail field)
     * @param value the value (email address) of the component
     */
    public void validatePhoneNumber(FacesContext context, UIComponent toValidate, Object value) {
        String phoneNum = (String) value;
        String pattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        if (phoneNum.matches(pattern)) {     
            
        } else {     
            FacesMessage message = new FacesMessage("Invalid phone number");
            throw new ValidatorException(message);
        }
    }
    
    public void validateLaterTime(FacesContext context, UIComponent component, Object value) throws ValidatorException, ParseException {
        java.util.Date laterTime = (java.util.Date) value;
        //System.out.println("endTime: " + endTime);
        //Object otherValue = component.getAttributes().get("otherValue");
        RequestBean requestBean = new RequestBean();
        java.util.Date currentTime = requestBean.getCurrentTime();
        //System.out.println("startTime: " + startTime);
        if(laterTime.before(currentTime)) {
            FacesMessage message = new FacesMessage("Invalid later time input");
            throw new ValidatorException(message);
        }
    }


}