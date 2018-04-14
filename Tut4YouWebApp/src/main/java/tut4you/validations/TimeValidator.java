/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.validations;

import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import tut4you.model.Availability;

/**
 *
 * @author Syed Haider
 */
@FacesValidator(value = "timeValidator")
public class TimeValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Date endTime = (Date) value;
        Object otherValue = component.getAttributes().get("otherValue");
        Date startTime = (Date) otherValue;
        String day = (String) component.getAttributes().get("day");
        Object listValue = component.getAttributes().get("AvailabilityList");
        List<Availability> list = (List<Availability>) listValue;

        for (Availability avail : list) {
          if (day.equals(avail.getDayOfWeek()) && (startTime.equals(avail.getStartTime()) && endTime.equals(avail.getEndTime()))) {
                FacesMessage message = new FacesMessage("You currently have this date and time set for your availability.");
                throw new ValidatorException(message);
            }
           else if (startTime.after(endTime)) {
                FacesMessage message = new FacesMessage("Start Time must be before End Time");
                throw new ValidatorException(message);
            } else if (day.equals(avail.getDayOfWeek()) && (startTime.after(avail.getStartTime()) && startTime.before(avail.getEndTime()))) {
                FacesMessage message = new FacesMessage("Start time is in between start/end time ");
                throw new ValidatorException(message);
            } else if (day.equals(avail.getDayOfWeek()) && (endTime.after(avail.getStartTime()) && endTime.before(avail.getEndTime()))) {
                FacesMessage message = new FacesMessage("End time is in between start/end time. Re-enter");
                throw new ValidatorException(message);
            } else if (day.equals(avail.getDayOfWeek()) && (startTime.before(avail.getStartTime()) && endTime.after(avail.getEndTime()))) {
                FacesMessage message = new FacesMessage("You have set your availability for this time already. Re-enter.");
                throw new ValidatorException(message);
            } else {
                break;
            }
        }
    }
}
