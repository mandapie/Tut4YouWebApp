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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * EqualsValidator extends the Validator class to determine if two fields are duplicates
 * @author Brian Leathem in StackOverflow
 * Modified by Syed Haider <shayder426@gmail.com>
 * http://stackoverflow.com/questions/2909021/jsf-2-0-validate-equality-of-2-inputsecret-fields-confirm-password-without-wri
 */
@FacesValidator(value = "equalsValidator")
public class EqualsValidator implements Validator {

    /**
     * Validates the equality of two form fields in a form.
     * @param context the Faces context
     * @param component the UIComponent that is being validated
     * @param value the value of the UIComponent to be compared for equality
     * @throws ValidatorException exception thrown when the fields do not match in value
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Object otherValue = component.getAttributes().get("otherValue");

        if (value == null || otherValue == null) {
            return; // Let required="true" handle.
        }

        if (!value.equals(otherValue)) {
            throw new ValidatorException(new FacesMessage("Values are not equal."));
        }
    }
}