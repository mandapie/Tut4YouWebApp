
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The Payment will keep track of the cost of each tutoring session
 *
 * @author Syed Haider <shayder426@gmail.com>
 */
@Entity
@Table(name = "Payment")
@NamedQueries({
    @NamedQuery(name = Payment.FIND_PAYMENTS_BY_EMAIL, query = "SELECT p FROM Payment p")
})
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String FIND_PAYMENTS_BY_EMAIL = "Payment.findPaymentsByEmail";

    @OneToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @Id
    private String payKey;
    
    private String transactionId;
    private String timeOfTransaction;
    private double transactionAmount;
    private String paymentStatus;

    /**
     * Default Payment constructor
     */
    public Payment() {
    }

    /**
     * Payment overloaded constructor
     *
     * @param payKey
     * @param transactionId
     * @param timeOfTransaction
     * @param transactionAmount
     */
    public Payment(String payKey, String transactionId, String timeOfTransaction, double transactionAmount, String paymentStatus) {
        this.payKey = payKey;
        this.transactionId = transactionId;
        this.timeOfTransaction = timeOfTransaction;
        this.transactionAmount = transactionAmount;
        this.paymentStatus = paymentStatus;

    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    /**
     *
     * @return
     */
    public String getPayKey() {
        return payKey;
    }

    /**
     *
     * @param payKey
     */
    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }

    /**
     *
     * @return
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     *
     * @param transactionId
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     *
     * @return
     */
    public String getTimeOfTransaction() {
        return timeOfTransaction;
    }

    /**
     *
     * @param timeOfTransaction
     */
    public void setTimeOfTransaction(String timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
    }

    /**
     *
     * @return
     */
    public double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     *
     * @param transactionAmount
     */
    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (payKey != null ? payKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        return !((this.payKey == null && other.payKey != null) || (this.payKey != null && !this.payKey.equals(other.payKey)));
    }

    @Override
    public String toString() {
        return "tut4you.model.Payment[ payKey=" + payKey + " ]";
    }

}
