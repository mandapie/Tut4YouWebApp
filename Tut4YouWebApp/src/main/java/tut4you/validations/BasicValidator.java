/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 * 
 *  2013-2017 Alvaro Monge <alvaro.monge@csulb.edu>
 * 
 */


package tut4you.validations;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 *
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 */
@Named
@RequestScoped
public class BasicValidator {

    /** Creates a new instance of BasicValidator */
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
    }

}
