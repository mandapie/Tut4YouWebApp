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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 * Validates (basic) correct format.
 *
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 */
@Named
@RequestScoped
public class PriceValidator {

    /**
     * Creates a new instance of BasicValidator
     */
    public PriceValidator() {
    }

    /**
     * validates an hourly rate to be in the (basic) correct format.
     *
     * @param context the FacesContext
     * @param toValidate the UIComponent being validated (e-mail field)
     * @param value the value (hourly rate) of the component
     * @throws ValidatorException the Exception to throw b/c the value is not a
     * valid price rate
     *

     */
    public void validate(FacesContext context, UIComponent toValidate, Object value) throws ValidatorException {
        String priceRate = (String) value;
        //String to be scanned to find the pattern.
        String pattern = "^(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$";
        //Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        Matcher m1 = r.matcher(priceRate);
        boolean matchResult = m1.matches();
        //If the regex pattern matches the string, then print out Matched
        if (!matchResult) {
            FacesMessage message = new FacesMessage("Invalid price rate. Re-enter!");
            throw new ValidatorException(message);
        }

    }

}
