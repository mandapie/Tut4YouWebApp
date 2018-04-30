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
import javax.ejb.Stateless;
import java.lang.Object;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Entity
public class Forum implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToMany
    private Question question;
    
    @OneToOne
    private Course course;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePosted;
    

    public Forum() {
    
    }
    
    public Forum(Course course, Question question, Date datePosted) {
        this.course = course;
        this.question = question;
        this.datePosted = datePosted;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public Question getQuestion() {
        return question;
    }
    
    public void setQuestion(Question question){
        this.question = question;
    }
    
    public Date getDatePosted(){
        return datePosted;
    }
    
    public void setDatePosted(Date datePosted){
        this.datePosted = datePosted;
    }
    
}
