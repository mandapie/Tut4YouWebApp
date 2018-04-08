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

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;
import tut4you.model.Tut4YouApp;
import tut4you.model.Tutor;

/**
 * BookConverter converts between an id of a book and the Book object found in the DB
 */
@Named
@RequestScoped
public class TutorConverter implements Converter {
   // Services -----------------------------------------------------------------------------------

   @EJB
   private Tut4YouApp tut4youapp;

   // Actions ------------------------------------------------------------------------------------

   @Override
   public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
      if (modelValue == null) {
         return "";
      }

      if (modelValue instanceof Tutor) {
         return String.valueOf(((Tutor) modelValue).getEmail());
      } else {
         throw new ConverterException(new FacesMessage(modelValue + " is not a valid Tutor entity"));
      }
   }

   @Override
   public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
      Tutor theBook = null;
      if (submittedValue != null && !submittedValue.isEmpty()) {
         try {
            theBook = tut4youapp.findTutor(submittedValue);
         } catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage(submittedValue + " is not a valid Tutor ID"), e);
         }

         if (theBook == null)
            throw new ConverterException(new FacesMessage("There is no book with ID of " + submittedValue));
      }

      return theBook;
   }
}
