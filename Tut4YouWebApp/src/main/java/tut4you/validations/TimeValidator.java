/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.validations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Keith
 */
@FacesValidator(value = "timeValidator")
public class TimeValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        java.util.Date endTime = (java.util.Date) value;
        //System.out.println("endTime: " + endTime);
        Object otherValue = component.getAttributes().get("otherValue");
        java.util.Date startTime = (java.util.Date) otherValue;
        //System.out.println("startTime: " + startTime);
        if(startTime.after(endTime)) {
            FacesMessage message = new FacesMessage("Invalid time inputs");
            throw new ValidatorException(message);
        }
        
    }
}

