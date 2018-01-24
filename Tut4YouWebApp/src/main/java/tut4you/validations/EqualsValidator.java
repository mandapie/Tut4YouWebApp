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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * EqualsValidator extends the Validator class to determine if two fields are duplicates
 * @author Brian Leathem in StackOverflow
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
