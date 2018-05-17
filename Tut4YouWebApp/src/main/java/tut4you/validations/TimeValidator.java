/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.validations;

import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import tut4you.model.Availability;

/**
 *
 * @author Syed Haider
 */
@Named
@RequestScoped
public class TimeValidator {

    public TimeValidator() {
        
    }
    
    public void validateTime(FacesContext context, UIComponent component, Object value) {
        Date startTime = (Date) component.getAttributes().get("startTime");
        Date endTime = (Date) value;
        String day = (String) component.getAttributes().get("day");
        Object listValue = component.getAttributes().get("availabilityList");
        List<Availability> list = (List<Availability>) listValue;
        for (Availability avail : list) {
            if (day.equals(avail.getDayOfWeek()) && (startTime.equals(avail.getStartTime()) && endTime.equals(avail.getEndTime()))) {
                FacesMessage message = new FacesMessage("You have a duplicate availability");
                throw new ValidatorException(message);
            } else if (startTime.equals(endTime) || startTime.after(endTime)) {
                FacesMessage message = new FacesMessage("Invalid start time");
                throw new ValidatorException(message);
            } else if (day.equals(avail.getDayOfWeek()) && (startTime.after(avail.getStartTime()) && startTime.before(avail.getEndTime()))) {
                FacesMessage message = new FacesMessage("You have an overlapping start time");
                throw new ValidatorException(message);
            } else if (day.equals(avail.getDayOfWeek()) && (endTime.after(avail.getStartTime()) && endTime.before(avail.getEndTime()))) {
                FacesMessage message = new FacesMessage("You have an overlapping end time");
                throw new ValidatorException(message);
            } else if (day.equals(avail.getDayOfWeek()) && (startTime.before(avail.getStartTime()) && endTime.after(avail.getEndTime()))) {
                FacesMessage message = new FacesMessage("You have an overlapping availabilty");
                throw new ValidatorException(message);
            }
        }
    }
}
