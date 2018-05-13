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
 
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * After each session, a tutor can be paid for their services
 * by a student.
 *
 * @author Syed Haider <shayder426@gmail.com>
 */
@Entity
@Table(name = "Payment")
@NamedQueries({
    @NamedQuery(name = Payment.FIND_PAYMENTS_BY_EMAIL, query = "SELECT p FROM Payment p"),
        @NamedQuery(name = Payment.FIND_PAYMENTS_BY_PAYKEY, query = "SELECT p FROM Payment p WHERE p.payKey = :payKey"),
})
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * JPQL query to find the payments based on the specific user's email
     */
    public static final String FIND_PAYMENTS_BY_EMAIL = "Payment.findPaymentsByEmail";

    /**
     * JPQ: query to find the payments based on the paykey that is generated
     */
    public static final String FIND_PAYMENTS_BY_PAYKEY = "Payment.findPaymentsByPaykey";

    /**
     * Each session has one payment made for it. 
     */
    @OneToOne
    @JoinColumn(name = "session_id")
    private Session session;

    /**
     * Multiple payments can be received by a tutor
     */
    @ManyToOne
    @JoinColumn(name = "tutor", nullable = false)
    private Tutor tutor;

    @Id
    private String payKey;

    private String paymentId;
    private String timeOfPayment;
    private double paymentAmount;
    private String paymentStatus;

    /**
     * Default Payment constructor
     */
    public Payment() {
    }

    /**
     * Payment overloaded constructor
     *
     * @param payKey randomly generated payKey for each payment
     * @param paymentId a confirmation number
     * @param timeOfPayment the time the payment was processed
     * @param paymentAmount the cost of the tutoring session
     * @param paymentStatus the status of the payment (APPROVED,COMPLETED,CREATED)
     */
    public Payment(String payKey, String paymentId, String timeOfPayment, double paymentAmount, String paymentStatus) {
        this.payKey = payKey;
        this.paymentId = paymentId;
        this.timeOfPayment = timeOfPayment;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;

    }

    /**
     * Gets the current session
     * @return session (tutoring session)
     */
    public Session getSession() {
        return session;
    }

    /**
     * Sets the session
     * @param session (tutoring session)
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * Gets the payKey that is generated for each payment
     * @return payKey - key that is appended to URL for user to make a payment
     */
    public String getPayKey() {
        return payKey;
    }

    /**
     * Sets the payKey that is generated for each payment
     * @param payKey - key that is appended to URL for user to make a payment
     */
    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }

    /**
     * Gets the payment id for the payment
     * @return paymentId - id that can be used to identify a payment
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * Sets the payment id for the payment
     * @param paymentId - id that can be used to identify a payment
     */
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * Gets the time of the payment
     * @return timeOfPayment - time of the payment
     */
    public String getTimeOfPayment() {
        return timeOfPayment;
    }

    /**
     * Sets the time of the payment
     * @param timeOfPayment - time of the payment
     */
    public void setTimeOfPayment(String timeOfPayment) {
        this.timeOfPayment = timeOfPayment;
    }

    /**
     * Gets the cost of the payment
     * @return paymentAmount - amount of payment
     */
    public double getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the cost of the payment
     * @param paymentAmount - amount of payment
     */
    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the status of the payment
     * @return paymentStatus - status of payment
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Sets the status of the payment
     * @param paymentStatus - status of payment
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * Gets the tutor of the session/payment
     * @return tutor - tutor of payment
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Sets the tutor of the session/payment
     * @param tutor - tutor of payment
     */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    
    /**
     * Override hashCode
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (payKey != null ? payKey.hashCode() : 0);
        return hash;
    }

        /**
     * Overrides the equals method
     *
     * @param object
     * @return true if object is Payment, else false
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        return !((this.payKey == null && other.payKey != null) || (this.payKey != null && !this.payKey.equals(other.payKey)));
    }

    /**
     * Override toString
     *
     * @return payKey attributes
     */
    @Override
    public String toString() {
        return "tut4you.model.Payment[ payKey=" + payKey + " ]";
    }

}
