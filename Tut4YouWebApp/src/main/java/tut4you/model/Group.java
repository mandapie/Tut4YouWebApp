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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Groups the different types of users for different roles purposes
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 * Modified by Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Entity(name="StudentGroup")
@Table(name="groups")
@NamedQueries ({
    @NamedQuery(name = Group.FIND_GROUP_BY_USERNAME, query = "SELECT g FROM StudentGroup g WHERE g.name = :groupname")
})
public class Group implements Serializable {    
    /**
     * Name of JPQL query to retrieve the Group given its name
     */
    public static final String FIND_GROUP_BY_USERNAME = "Group.findGroupByName";
    
    @Id
    private String name;
    
    private String description;
    
    @ManyToMany
    @JoinTable(name="groups_students",
          joinColumns=@JoinColumn(name="groupname"),
          inverseJoinColumns=@JoinColumn(name="email"))
    private Collection<User> students;
    
    @ManyToMany
    @JoinTable(name="groups_students",
          joinColumns=@JoinColumn(name="groupname"),
          inverseJoinColumns=@JoinColumn(name="email"))
    private Collection<Tutor> tutors;

    /**
     * Group Constructor
     */
    public Group() { }

    /**
     * creates a group with the given name
     * @param name is the name of the group to be created
     */
    public Group(String name) {
        this.name = name;
    }

    /**
     * gets the name of this group
     * @return the name of this group
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of this group 
     * @param name is the string that is to be the name of this group
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the description of this group
     * @return is the string describing this group
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description of this group
     * @param description is the string to be used to describe this group
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * gets the collection of students that are members of this group
     * @return the collection of students in this group
     */
    public Collection<User> getStudents() {
        return students;
    }

    /**
     * sets the collection of students in this group
     * @param students the collection of students in this group
     */
    public void setStudents(Collection<User> students) {
        this.students = students;
    }

    /**
     * adds a student to this group
     * @param student is the student to be added to this group
     */
    public void addStudent(User student) {
        if (this.students == null)
            this.students = new HashSet();
        this.students.add(student);
    }
    /**
     * gets the collection of students that are members of this group
     * @return the collection of students in this group
     */
    public Collection<Tutor> getTutors() {
        return tutors;
    }

    /**
     * sets the collection of students in this group
     * @param tutor the collection of students in this group
     */
    public void setTutors(Collection<Tutor> tutors) {
        this.tutors = tutors;
    }

    /**
     * adds a student to this group
     * @param tutor is the student to be added to this group
     */
    public void addTutor(Tutor tutor) {
        if (this.tutors == null)
            this.tutors = new HashSet();
        this.tutors.add(tutor);
    }
    
    /**
     * Override hashCode
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }
    
    /**
     * Overrides the equals method
     * @param object 
     * @return true if object is Group, else false
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the name fields are not set
        if (!(object instanceof Group)) {
            return false;
        }
        Group other = (Group) object;
        return (this.name != null || other.name == null) && (this.name == null || this.name.equals(other.name));
    }
    
    /**
     * Override toString
     * @return Group as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Group");
        sb.append("{name=").append(name);
        sb.append(", description='").append(description);
        sb.append('}');
        return sb.toString();

    }
}