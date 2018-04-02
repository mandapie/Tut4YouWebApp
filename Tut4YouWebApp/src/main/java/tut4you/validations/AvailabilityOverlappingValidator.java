/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.validations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import tut4you.controller.AvailabilityBean;
import tut4you.model.Availability;

/**
 *
 * @author Keith
 */
@FacesValidator(value = "AvailabilityOverlappingValidator")
public class AvailabilityOverlappingValidator implements Validator {

    AvailabilityBean bean;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Object otherValue = component.getAttributes().get("otherValue");
        java.util.Date startTime = (java.util.Date) otherValue;
        List<Availability> list = bean.getAvailabilityList();
        for (Availability e : list) {
            if (startTime.after(e.getStartTime()) && startTime.before(e.getEndTime())) {
                FacesMessage message = new FacesMessage("You cannot place a starting time for your availability"
                        + "that is between a starting time and ending time for that day.");
                throw new ValidatorException(message);
            }
        }

    }
}
