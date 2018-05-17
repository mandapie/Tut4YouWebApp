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
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Table(name = "Responses")
@NamedQueries({
    @NamedQuery(name = Responses.FIND_RESPONSES_BY_QUESTION, query = "SELECT r FROM Responses r JOIN r.question q WHERE q.title = :title")
})
@Entity
public class Responses implements Serializable {

    /**
     * JPQL Query to find Requests by user email
     */
    public static final String FIND_RESPONSES_BY_QUESTION = "Responses.findResponsesByQuestion";
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String explanation;
    private String answer;
    
    @ManyToOne
    private Question question;
    
    @ManyToOne
    private Tutor tutor;

    public Responses() {
    
    }
    
    public Responses(String explanation, String answer, Tutor tutor) {
        this.explanation = explanation;
        this.answer = answer;
        this.tutor = tutor;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getExplanation() {
        return explanation;
    }
    
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer){
        this.answer = answer;
    }
    
    public Tutor getTutor(){
        return tutor;
    }
    
    public void setTutor(Tutor tutor){
        this.tutor = tutor;
    }
    
    public Question getQuestion(){
        return question;
    }
    
    public void setQuestion(Question question){
        this.question = question;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Responses)) {
            return false;
        }
        Responses other = (Responses) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tut4you.model.Responses[ id=" + id + " ]";
    }
    
}