/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Amanda
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Course.FIND_ALL_COURSES, query = "SELECT c FROM Course c"),
    @NamedQuery(name = Course.FIND_COURSE_BY_SUBJECT, query = "SELECT c FROM Course c JOIN c.subject s WHERE s.subjectName = :name"),
    @NamedQuery(name = Course.FIND_COURSES_BY_TUTOR, query = "SELECT c FROM Course c JOIN c.tutors t WHERE t.email = :email")
})
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String FIND_ALL_COURSES = "Course.findAllCourses";
    public static final String FIND_COURSE_BY_SUBJECT = "Course.findCourseBySubject";
    public static final String FIND_COURSES_BY_TUTOR = "Tutor.findCoursesByTutor";
    
    @Id
    private String courseName;
    
    @ManyToOne
    @JoinColumn(name="subjectName", nullable=false)
    private Subject subject;
    
    //many to many relationship between course and tutor
    @ManyToMany
    @JoinTable(name="courses_tutors",
          joinColumns=@JoinColumn(name="coursename"),
          inverseJoinColumns=@JoinColumn(name="email"))
    private Collection<Tutor> tutors;
    
    //getters and setters for attributes created
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    /**
     * access the subject that the course is a member of
     * @return subject that the course is from
     */
    public Subject getSubject() {
        return subject;
    }
    
    /**
     * sets the subject of the course
     * @param subject is the subject the course will be set to
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
    
    public Collection<Tutor> getTutors() {
        return tutors;
    }

    public void setTutors(Collection<Tutor> tutors) {
        this.tutors = tutors;
    }
    
    public void addTutor(Tutor tutor) {
        if (this.tutors == null)
            this.tutors = new HashSet();
        this.tutors.add(tutor);
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.courseName == null && other.courseName != null) || (this.courseName != null && !this.courseName.equals(other.courseName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tut4you.model.Course[ id=" + courseName + " ]";
    }
    
}