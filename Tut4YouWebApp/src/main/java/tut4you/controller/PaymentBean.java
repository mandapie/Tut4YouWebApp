package tut4you.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import tut4you.model.Payment;
import tut4you.model.Session;
import tut4you.model.Tut4YouApp;

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

    @EJB
    private Tut4YouApp tut4youapp;

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
        //paymentList = tut4youapp.getPaymentList();
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public void payForTutoringSession(String email, double hourlyRate, Session session) {
        String payKey;
        System.out.println(session);
        payKey = tut4youapp.payForTutoringSession(email, hourlyRate);
        payment = tut4youapp.createPayment(payKey, session);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String url = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey=" + payKey;
        try {
            externalContext.redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
