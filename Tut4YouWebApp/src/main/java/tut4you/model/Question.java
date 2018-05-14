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
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Table(name = "Question")
@Entity
@NamedQueries({
    @NamedQuery(name = Question.FIND_QUESTION_BY_COURSE, query = "SELECT q FROM Question q JOIN q.course c WHERE c.courseName = :name")
})
public class Question implements Serializable {
    /**
     * JPQL Query to find questions by their course name
     */
    public static final String FIND_QUESTION_BY_COURSE = "Question.findQuestionByCourse";

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private User student;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private Collection<Response> response;
    
    @OneToOne
    private Course course;

    public Question() {
    
    }
    
    public Question(String title, String description, Collection<Response> response) {
        this.title = title;
        this.description = description;
        this.response = response;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public Collection<Response> getResponse(){
        return response;
    }
    
    public void setResponse(Collection<Response> response){
        this.response = response;
    }
    
    public Course getCourse(){
        return course;
    }
    
    public void setCourse(Course course){
        this.course = course;
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
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tut4you.model.Question[ id=" + id + " ]";
    }
    
}