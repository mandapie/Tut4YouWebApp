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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A Subject contains a general subject name and will be broken down into course
 * names.
 *
 * @author Keith Tran <keithtran25@gmail.com>
 * Modified by Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Table(name = "Subject")
@Entity
@NamedQueries({
    @NamedQuery(name = Subject.FIND_ALL_SUBJECTS, query = "SELECT s FROM Subject s")
})
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * JPQL query to retrieve all subject entities
     */
    public static final String FIND_ALL_SUBJECTS = "Subject.findAllSubjects";

    /**
     * subjectName is subject's name
     */
    @Id
    private String subjectName;

    /**
     * A subject contains multiple courses. Maps a collection of courses to a
     * Subject
     */
    @OneToMany(mappedBy = "subject", cascade = {CascadeType.ALL})
    private Collection<Course> courses;

    /**
     * Gets subject name
     *
     * @return subject name
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * Sets subject name
     *
     * @param subjectName
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * Gets a collection of courses
     *
     * @return a collection of courses
     */
    public Collection<Course> getCourses() {
        return courses;
    }

    /**
     * Sets a collection of courses
     *
     * @param courses
     */
    public void setCourses(Collection<Course> courses) {
        this.courses = courses;
    }

    /**
     * adds course to collection of courses create new HashSet if courses
     * collection is null
     *
     * @param course
     */
    public void addCourse(Course course) {
        if (this.courses == null) {
            this.courses = new HashSet();
        }
        this.courses.add(course);
    }

    /**
     * Override hashCode
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subjectName != null ? subjectName.hashCode() : 0);
        return hash;
    }

    /**
     * Overrides the equals method
     *
     * @param object
     * @return true if object is Subject, else false
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the subjectName fields are not set
        if (!(object instanceof Subject)) {
            return false;
        }
        Subject other = (Subject) object;
        if ((this.subjectName == null && other.subjectName != null) || (this.subjectName != null && !this.subjectName.equals(other.subjectName))) {
            return false;
        }
        return true;
    }

    /**
     * Override toString
     *
     * @return SubjectName
     */
    @Override
    public String toString() {
        return "tut4you.model.Subject[ id=" + subjectName + " ]";
    }
}
