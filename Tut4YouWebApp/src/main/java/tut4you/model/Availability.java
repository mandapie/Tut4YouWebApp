/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Keith
 */
@Entity
@NamedQueries ({
    @NamedQuery(name = Availability.FIND_AVAILABILITY_BY_TUTOR, query = "SELECT a FROM Availability a JOIN a.tutor s WHERE s.email = :email")
})
public class Availability implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String FIND_AVAILABILITY_BY_TUTOR = "Availability.findAvailabilityByTutor";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    private String dayOfWeek;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String startPeriod;
    private String endPeriod;
    
    @ManyToOne
    private Tutor tutor;
    
    public Availability() {
        
    }
    public Availability(String dayOfWeek, int startHour, int startMinute, String startPeriod, int endHour, int endMinute, String endPeriod) {
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.startPeriod = startPeriod;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.endPeriod = endPeriod;
    }
    public String getDayOfWeek(){
        return dayOfWeek;
    }
    public void setDayOfWeek(String dayOfWeek){
        this.dayOfWeek = dayOfWeek;
    }
    public int getStartHour() {
        return startHour;
    }
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }
    public int getStartMinute() {
        return startMinute;
    }
    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }
    public String getStartPeriod(){
        return startPeriod;
    }
    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }
    public int getEndHour() {
        return endHour;
    }
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
    public int getEndMinute(){
        return endMinute;
    }
    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }
    public String getEndPeriod(){
        return endPeriod;
    }
    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    
    public Tutor getTutor(){
        return tutor;
    }
    
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
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
        if (!(object instanceof Availability)) {
            return false;
        }
        Availability other = (Availability) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tut4you.entities.Availability[ id=" + id + " ]";
    }
}