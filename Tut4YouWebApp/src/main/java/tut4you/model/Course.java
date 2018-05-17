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
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A course encapsulates a specific class that a tutor may teach and/or a
 * student may take.
 *
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 * @author Keith Tran <keithtran25@gmail.com>
 */
@Table(name = "Course")
@Entity
@NamedQueries({
    @NamedQuery(name = Course.FIND_COURSE_BY_SUBJECT, query = "SELECT c FROM Course c JOIN c.subject s WHERE s.subjectName = :name"),
    @NamedQuery(name = Course.FIND_COURSES_BY_TUTOR, query = "SELECT c FROM Course c JOIN c.tutors t WHERE t.email = :email"),
    @NamedQuery(name = Course.VERIFY_COURSE, query = "SELECT c from Course C WHERE c.courseName = :courseName"),
    @NamedQuery(name = Course.FIND_COURSES_BY_USERNAME, query = "SELECT c FROM Course C JOIN c.tutors t WHERE t.username = :username")
})
public class Course implements Serializable {  
    /**
     * JPQL Query to find courses by their subject name
     */
    public static final String FIND_COURSE_BY_SUBJECT = "Course.findCourseBySubject";
    /**
     * JPQL Query to find courses by tutor
     */
    public static final String FIND_COURSES_BY_TUTOR = "Tutor.findCoursesByTutor";
    /**
    * JPQL Query to find courses by username
     */
    public static final String FIND_COURSES_BY_USERNAME = "Tutor.findCoursesByUsername";

    /**
     * JPQL Query to verify course exists
     */
    public static final String VERIFY_COURSE = "Tutor.verifyCourse";

    @Id
    private String courseName;
    /**
     * One to Many relationship a Course can be associated with many questions
     */
    @OneToMany(mappedBy = "course", cascade = {CascadeType.ALL})
    private Collection<Question> question;
    /**
     * Many to One relationship Course can only be set to one Subject Subject
     * can be set to many Courses
     */
    @ManyToOne
    @JoinColumn(name = "subjectName", nullable = false)
    private Subject subject;
    /**
     * Many to many relationship Courses are tutored by many tutors Tutors Can
     * tutor many courses
     */
    @ManyToMany
    @JoinTable(name = "Courses_tutors",
            joinColumns = @JoinColumn(name = "coursename"),
            inverseJoinColumns = @JoinColumn(name = "email"))
    private Collection<Tutor> tutors;

    /**
     * gets the name of course
     *
     * @return courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * sets the name of course
     *
     * @param courseName
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * access the subject that the course is a member of
     *
     * @return subject that the course is from
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * sets the subject of the course
     *
     * @param subject is the subject the course will be set to
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * gets collection of tutors
     *
     * @return tutors is collection of tutors
     */
    public Collection<Tutor> getTutors() {
        return tutors;
    }

    /**
     * sets collection of tutors to a specific collection
     *
     * @param tutors is collection of tutors
     */
    public void setTutors(Collection<Tutor> tutors) {
        this.tutors = tutors;
    }

    /**
     * adds tutor to collection of tutors create new HashSet if tutors
     * collection is null
     *
     * @param tutor
     */
    public void addTutor(Tutor tutor) {
        if (this.tutors == null) {
            this.tutors = new HashSet();
        }
        this.tutors.add(tutor);
    }
    
    /**
     * gets a collection of questions
     * @return questions
     */
    public Collection<Question> getQuestion() {
        return question;
    }

    /**
     * Sets a collection of questions to a question
     * @param question 
     */
    public void setQuestion(Collection<Question> question) {
        this.question = question;
    }
    /**
     * adds a question to a collection of questions
     * @param question 
     */
    public void addQuestion(Question question) {
        if (this.question == null) {
            this.question = new HashSet();
        }
        this.question.add(question);
    }
    /**
     * removes tutor from the collection
     * @param tutor 
     */
    public void removeTutor(Tutor tutor) {
        tutors.remove(tutor);

    }
    /**
     * overrides equals method
     * @param object
     * @return true or false
     */
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
    /**
     * overrides default tostring
     * @return 
     */
    @Override
    public String toString() {
        return "tut4you.model.Course[ id=" + courseName + " ]";
    }
}
