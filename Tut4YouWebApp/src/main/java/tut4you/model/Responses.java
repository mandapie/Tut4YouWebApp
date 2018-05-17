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
 * Responses are the answers to the questions on the forum
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Table(name = "Responses")
@NamedQueries({
    @NamedQuery(name = Responses.FIND_RESPONSES_BY_QUESTION, query = "SELECT r FROM Responses r JOIN r.question q WHERE q.title = :title")
})
@Entity
public class Responses implements Serializable {

    /**
     * JPQL Query to find Response by question
     */
    public static final String FIND_RESPONSES_BY_QUESTION = "Responses.findResponsesByQuestion";
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String explanation;
    private String answer;
    /**
     * Many to one with question.
     * There can be many responses to one question
     */
    @ManyToOne
    private Question question;
    /**
     * Many to one with tutor.
     * There can be many responses from one tutor
     */
    @ManyToOne
    private Tutor tutor;
    /**
     * default responses constructor
     */
    public Responses() {
    
    }
    /**
     * responses constructor
     * @param explanation response explanation
     * @param answer response answer
     * @param tutor tutor who responded
     */
    public Responses(String explanation, String answer, Tutor tutor) {
        this.explanation = explanation;
        this.answer = answer;
        this.tutor = tutor;
    }
    /**
     * gets the id
     * @return id
     */
    public Long getId() {
        return id;
    }
    /**
     * set the id
     * @param id id to be set
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * gets the explanation
     * @return explanation
     */
    public String getExplanation() {
        return explanation;
    }
    /**
     * sets the explanation
     * @param explanation explanation to be set
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    /**
     * gets the answer
     * @return answer
     */
    public String getAnswer() {
        return answer;
    }
    /**
     * sets the answer
     * @param answer to be set
     */
    public void setAnswer(String answer){
        this.answer = answer;
    }
    /**
     * gets the tutor
     * @return tutor
     */
    public Tutor getTutor(){
        return tutor;
    }
    /**
     * sets the tutor
     * @param tutor to be set
     */
    public void setTutor(Tutor tutor){
        this.tutor = tutor;
    }
    /**
     * gets the question
     * @return question
     */
    public Question getQuestion(){
        return question;
    }
    /**
     * sets a question
     * @param question to be set
     */
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
