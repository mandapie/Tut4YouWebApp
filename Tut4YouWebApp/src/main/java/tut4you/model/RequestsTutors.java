//IN PROGRESS


///*
// * Licensed under the Academic Free License (AFL 3.0).
// *     http://opensource.org/licenses/AFL-3.0
// * 
// *  This code has been developed by a group of CSULB students working on their 
// *  Computer Science senior project called Tutors4You.
// *  
// *  Tutors4You is a web application that students can utilize to find a tutor and
// *  ask them to meet at any location of their choosing. Students that struggle to understand 
// *  the courses they are taking would benefit from this peer to peer tutoring service.
// 
// *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
// *  2017 Andrew Kaichi <ahkaichi@gmail.com>
// *  2017 Keith Tran <keithtran25@gmail.com>
// *  2017 Syed Haider <shayder426@gmail.com>
// */
//package tut4you.model;
//
//import java.io.Serializable;
//import java.util.Collection;
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//
///**
// * Association class for one Tutor can receive many pending Requests and
// * one Request can be sent to many available Tutors
// * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
// * https://www.thoughts-on-java.org/many-relationships-additional-properties/
// */
//@Table(name="Requests_tutors")
//@Entity
//public class RequestsTutors implements Serializable {  
//    enum RequestStatus {
//        NOTSENT,
//        SENT
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @OneToOne
//    private RequestsTutors id;
//    
//    @OneToMany(mappedBy="pendingRequest", cascade=CascadeType.ALL)
//    private Collection<Request> pendingRequests;
//
//    @OneToMany(mappedBy="availableTutor", cascade=CascadeType.ALL)
//    private Collection<Tutor> availableTutors;
//
//    public Collection<Request> getPendingRequests() {
//        return pendingRequests;
//    }
//
//    public void setPendingRequests(Collection<Request> pendingRequests) {
//        this.pendingRequests = pendingRequests;
//    }
//
//    public Collection<Tutor> getAvailableTutors() {
//        return availableTutors;
//    }
//
//    public void setAvailableTutors(Collection<Tutor> availableTutors) {
//        this.availableTutors = availableTutors;
//    }
//    
//    @Column
//    private RequestStatus requestStatus;
//
//    public RequestsTutors getId() {
//        return id;
//    }
//
//    public void setId(RequestsTutors id) {
//        this.id = id;
//    }
//
//    public RequestStatus getRequestStatus() {
//        return requestStatus;
//    }
//
//    public void setRequestStatus(RequestStatus requestStatus) {
//        this.requestStatus = requestStatus;
//    }
//}
