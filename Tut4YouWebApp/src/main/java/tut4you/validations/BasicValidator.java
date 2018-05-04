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
 *  
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.validations;

import java.text.ParseException;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import tut4you.controller.RequestBean;
import tut4you.model.Tut4YouApp;
import tut4you.model.User;

/**
 * Validates (basic) correct format.
 *
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 * Modified by Keith Tran <keithtran25@gmail.com>
 * Modified by Syed Haider <shayder426@gmail.com>
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
     * validates an e-mail address to be in the (basic) correct format and
     * validate that it is not already in the database
     *
     * @param context the FacesContext
     * @param toValidate the UIComponent being validated (e-mail field)
     * @param value the value (email address) of the component
     * @throws ValidatorException the Exception to throw b/c the value is not an
     * e-mail address Modified by Amanda Pan: made regex pattern to validate
     * email format source:
     * https://howtodoinjava.com/regex/java-regex-validate-email-address/
     */
    public void validateEmail(FacesContext context, UIComponent toValidate, Object value) {
        String emailStr = (String) value;
        String pattern = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+$";
        if (!emailStr.matches(pattern)) {
            FacesMessage message = new FacesMessage("Invalid email address");
            throw new ValidatorException(message);
        }
        for (String str : tut4YouApp.getUserEmails()) {
            if (emailStr.equals(str)) {
                FacesMessage message = new FacesMessage("email address already in use");
                throw new ValidatorException(message);
            }
        }
    }

    /**
     * validate username is unique
     *
     * @param context
     * @param toValidate
     * @param value
     * @throws ValidatorException
     */
    public void validateUserName(FacesContext context, UIComponent toValidate, Object value) {
        String userName = (String) value;
        for (String str : tut4YouApp.getUserUserNames()) {
            if (userName.equals(str)) {
                FacesMessage message = new FacesMessage("username already in use");
                throw new ValidatorException(message);
            }
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
        if (!phoneNum.matches(pattern)) {
            FacesMessage message = new FacesMessage("Invalid format");
            throw new ValidatorException(message);
        }
    }

    /**
     * validates hourly rate to range between 0.00 - 99.99
     *
     * @param context
     * @param toValidate
     * @param value
     * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
     */
    public void validateHourlyRate(FacesContext context, UIComponent toValidate, Object value) {
        String rate = (String) value;
        String pattern = "^\\d{0,2}(\\.\\d{1,2})?";
        if (!rate.matches(pattern)) {
            FacesMessage message = new FacesMessage("Invalid input");
            throw new ValidatorException(message);
        }
    }

    /**
     * https://stackoverflow.com/questions/9043551/regex-match-integer-only
     * validate zip is in integers
     *
     * @param context
     * @param toValidate
     * @param value Modified by Amanda: combined integer and length validations
     * for zipcode
     */
    public void validateZipcode(FacesContext context, UIComponent toValidate, Object value) {
        String zip = (String) value;
        String pattern = "^\\d{5}";
        if (!zip.matches(pattern)) {
            FacesMessage message = new FacesMessage("zipcodes contains 5 digits");
            throw new ValidatorException(message);
        }
    }

    /**
     * EqualsValidator extends the Validator class to determine if two fields
     * are duplicates
     *
     * @author Brian Leathem in StackOverflow Modified by Syed Haider
     * <shayder426@gmail.com>
     * http://stackoverflow.com/questions/2909021/jsf-2-0-validate-equality-of-2-inputsecret-fields-confirm-password-without-wri
     * @param context the Faces context
     * @param component the UIComponent that is being validated
     * @param value the value of the UIComponent to be compared for equality
     */
    public void validatePassword(FacesContext context, UIComponent component, Object value) {
        Object otherValue = component.getAttributes().get("otherValue");
        if (value == null || otherValue == null) {
            return; // Let required="true" handle.
        }
        if (!value.equals(otherValue)) {
            throw new ValidatorException(new FacesMessage("Passwords are not matching"));
        }
    }

    /**
     * validate that later time in request is not before the actual time
     *
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     * @throws ParseException
     */
    public void validateLaterTime(FacesContext context, UIComponent component, Object value) throws ParseException {
        Date laterTime = (Date) value;
        RequestBean requestBean = new RequestBean();
        Date currentTime = requestBean.getCurrentTime();
        if (laterTime.before(currentTime)) {
            FacesMessage message = new FacesMessage("Invalid later time input");
            throw new ValidatorException(message);
        }
    }

    public void validateRating(FacesContext context, UIComponent component, Object value) {
        int rating = (int) value;
        if (rating <= 0) {
            FacesMessage message = new FacesMessage("Rating is required.");
            throw new ValidatorException(message);
        }
    }

  
}
