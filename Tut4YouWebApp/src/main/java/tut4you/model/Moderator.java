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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import javax.persistence.Table;

/**
 *
 * @author Keith
 */
@Table(name = "Moderator")
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "Moderator")
@Entity
public class Moderator extends User implements Serializable{

    public Moderator() {
    }

    public Moderator(String resumeFilePath, String reason, String email, String firstName, String lastName, String userName, String phoneNumber, String password, String university, String securityQuestion, String securityAnswer) {
        super(email, firstName, lastName, userName, phoneNumber, password, university, securityQuestion, securityAnswer);
        this.resumeFilePath = resumeFilePath;
        this.reason = reason;
    }
    public Moderator(User user) {
        super(user);
    }
    
    public Moderator(String resumeFilePath, String reason) {
        this.resumeFilePath = resumeFilePath;
        this.reason = reason;
    }
    
    private static final long serialVersionUID = 1L;
    
    private String resumeFilePath;
    private String reason;
    
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResumeFilePath() {
        return resumeFilePath;
    }

    public void setResumeFilePath(String resumeFilePath) {
        this.resumeFilePath = resumeFilePath;
    }
    
    
}
