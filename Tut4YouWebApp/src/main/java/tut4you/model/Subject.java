/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Amanda
 */
@Entity
@NamedQueries ({
    @NamedQuery(name = Subject.FIND_ALL_SUBJECTS, query = "SELECT s FROM Subject s")
})
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;
    //JPQL query to retrieve all subject entities
    public static final String FIND_ALL_SUBJECTS = "Subject.findAllSubjects";
    
    @Id
    private String subjectName;
    
    //one to many relationship between subject and course
    @OneToMany(mappedBy="subject", cascade={CascadeType.ALL})
    private Collection<Course> courses;
        
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public Collection<Course> getCourses() {
        return courses;
    }
    
    public void setCourses(Collection<Course> courses) {
        this.courses = courses;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subjectName != null ? subjectName.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        return "tut4you.model.Subject[ id=" + subjectName + " ]";
    }
    
}