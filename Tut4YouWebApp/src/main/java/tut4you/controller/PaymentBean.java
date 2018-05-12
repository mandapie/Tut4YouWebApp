package tut4you.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import tut4you.exception.AvailabilityExistsException;
import tut4you.model.Payment;
import tut4you.model.Session;
import tut4you.model.Tut4YouApp;
import tut4you.model.Tutor;

/**
 *
 * @author Syed haider
 */
@Named
@SessionScoped
public class PaymentBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Payment payment;
    private static final Logger LOGGER = Logger.getLogger("PaymentBean");
    private List<Payment> paymentList = new ArrayList(); //list of available tutors
    private boolean status = false;
    private Tutor tutor;

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    
    @EJB
    private Tut4YouApp tut4youApp;

    private Session session;

    public PaymentBean() {
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<Payment> getPaymentList() {
        paymentList = tut4youApp.getPaymentList();
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public void payForTutoringSession(Tutor tutor, Session session) {
        String payKey;
        String email = tutor.getEmail();
        double hourlyRate = tutor.getHourlyRate();
        this.tutor = tutor;
        payKey = tut4youApp.payForTutoringSession(email, hourlyRate);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String url = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey=" + payKey;
        payment = tut4youApp.createPayment(payKey, session, tutor);
        try {
            externalContext.redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public boolean checkCompletedStatus(String payKey)
    {
        System.out.println("PAYKEY:" + payKey);
        if(payKey != null && !payKey.isEmpty())
            return !tut4youApp.checkCompletedStatus(payKey);
        else
            return true;
    }


    /*public String generatePayKey(String email, double hourlyRate, double elapsedTimeOfSession) {
        return payPal.generatePayKey(email,hourlyRate,elapsedTimeOfSession);
    }*/
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}
