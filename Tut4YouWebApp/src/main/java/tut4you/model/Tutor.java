package tut4you.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Keith
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Tutor.FIND_TUTORS_BY_COURSE, query = "SELECT t FROM Tutor t JOIN t.courses c WHERE c.courseName = :coursename")
})
public class Tutor extends Student implements Serializable {   
    private static final long serialVersionUID = 1L;
    
    public static final String FIND_TUTORS_BY_COURSE = "Tutor.findTutorsByCourse";
    
    @Column(nullable = true)
    @Temporal( TemporalType.DATE )
    private Date dateJoined;
    
    @Column(nullable = true)
    private int numPeopleTutored;

    //@Column(nullable = true)
    private int priceRate;
    
    //relationship between student and subject
    @ManyToMany(mappedBy="tutors", cascade=CascadeType.ALL)
    private Collection<Course> courses;
    
    @ManyToMany(mappedBy="tutors", cascade=CascadeType.ALL)
    private Collection<Group> groups;
    
    @OneToMany(mappedBy="tutor", cascade=CascadeType.ALL)
    private Collection<Availability> availability;
    
    //default constructor
    public Tutor()
    {
    }
    //constructor
    public Tutor(Date dateJoined, int numPeopleTutored, int priceRate) {
        this.dateJoined = dateJoined;
        this.numPeopleTutored = numPeopleTutored;
        this.priceRate = priceRate;
        groups = new HashSet<>();
    }
    
    public Tutor(String email, String firstName, String lastName, String userName, String phoneNumber, String password, Date dateJoined, int numPeopleTutored, int priceRate) {
        super(email, firstName, lastName, userName, phoneNumber, password);
        this.dateJoined = dateJoined;
        this.numPeopleTutored = numPeopleTutored;
        this.priceRate = priceRate;
        groups = new HashSet<>();
    }
    
    public Collection<Availability> getAvailability() {
        return availability;
    }

    public void setAvailability(Collection<Availability> availability) {
        this.availability = availability;
    }
    
    public void addAvailability(Availability availability) {
        if (this.availability == null)
            this.availability = new HashSet();
        this.availability.add(availability);
    }
    
    public void setTimeWorked(Date dateJoined) {
        this.dateJoined = dateJoined;
    }
    
    public Date getDateJoined() {
        return dateJoined;
    }
    
    public void setNumTutored(int numPeopleTutored) {
        this.numPeopleTutored = numPeopleTutored;
    }
    
    public int getNumTutored() {
        return numPeopleTutored;
    }
    
    public void setPriceRate(int priceRate) {
        this.priceRate = priceRate;
    }
    
    public int getPriceRate() {
        return priceRate;
    }
    
    public Collection<Course> getCourses() {
        return courses;
    }
    
    public void setCourse(Collection<Course> couses) {
        this.courses = courses;
    }
    
    @Override
    public Collection<Group> getGroups(){
        return groups;
    }
    
    @Override
    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }

    /**
     * Add a group to the user's set of groups
     * @param group to be added
     */
    @Override
    public void addGroup(Group group) {
        if (this.groups == null)
            this.groups = new HashSet();
        this.groups.add(group);
    }
    
    public void addCourse(Course course) {
        if (this.courses == null)
            this.courses = new HashSet();
        this.courses.add(course);
    }
    
//    public void setSubjects(Collection<Subject> subjects) {
//        this.subjects = subjects;
//    }
//
//    public void addSubject(Subject subject) {
//        if (this.subjects == null)
//            this.subjects = new HashSet();
//        this.subjects.add(subject);
//    }
//    
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (tutor_id != null ? tutor_id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Tutor)) {
//            return false;
//        }
//        Tutor other = (Tutor) object;
//        if ((this.tutor_id == null && other.tutor_id != null) || (this.tutor_id != null && !this.tutor_id.equals(other.tutor_id))) {
//            return false;
//        }
//        return true;
//    }
//
//   @Override
//    public String toString() {
//        return "entities.tutor[ id=" + tutor_id + " ]";
//    }
//    
}