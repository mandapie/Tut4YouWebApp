/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to findUser a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.
 
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import static org.omnifaces.util.Faces.getServletContext;
import org.primefaces.model.UploadedFile;
import tut4you.model.Tut4YouApp;
import tut4you.model.Tutor;

/**
 * Uploads .pdf files to an Amazon bucket.
 *
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Named
@ViewScoped
public class TranscriptBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("TranscriptBean");
    @Inject
    private UserBean userbean;
    @EJB
    private Tut4YouApp tut4youApp;
    private UploadedFile file;
    private Tutor tutor;
    private String url;
    /**
     * default constructor
     */
    public TranscriptBean() {

    }
    /**
     * constructor
     * @param file 
     */
    public TranscriptBean(UploadedFile file) {
        this.file = file;
    }
    
    /**
     *sets file
     * @param file
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    /**
     *gets file
     * @return file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     *gets url
     * @return url
     */
    public String getURL(){
        return url;
    }

    /**
     *sets url
     * @param url
     */
    public void setURL(String url){
        this.url = url;
    }

    /**
     *uploads a transcript to s3 
     * @throws IOException
     */
    public void uploadTranscript() throws IOException {
        Properties prop = new Properties();
        InputStream propstream = new FileInputStream(getServletContext().getRealPath("WEB-INF/s3.properties"));
        prop.load(propstream);
        AWSCredentials credentials = new BasicAWSCredentials(
                prop.getProperty("AWSAccessKeyId"),
                prop.getProperty("AWSSecretKey"));
        String bucketName = prop.getProperty("bucketName");
        try {
            //System.out.println("Key: " + credentials.getAWSAccessKeyId() + ", Secret: " + credentials.getAWSSecretKey());
        } catch (Exception e) {
            throw new AmazonClientException(e);
        }
        //Taken from: https://stackoverflow.com/questions/4bucketName1951978/amazons3clientcredentials-is-deprecated
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Write);
        try {
            String userName = userbean.getEmailFromSession();
            tutor = tut4youApp.findTutor(userName);
            String transcriptName = tutor.getUsername();
            String filePath = "transcript/";
            String keyName = filePath.concat(transcriptName.concat(".pdf"));

            try (S3Object s3Object = new S3Object()) {
                ObjectMetadata omd = new ObjectMetadata();
                omd.setContentType(file.getContentType());
                omd.setContentLength(file.getSize());
                omd.setHeader("filename", file.getFileName());
                InputStream input = file.getInputstream();
                s3Object.setObjectContent(input);
                s3.putObject(new PutObjectRequest(bucketName, keyName, input, omd).withAccessControlList(acl));
                FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                tut4youApp.addTranscriptFileLocation(keyName);
            }
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:  " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:  " + ase.getErrorCode());
            System.out.println("Error Type:    " + ase.getErrorType());
            System.out.println("Request ID:    " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");
        }
    }

    /**
     * downloads a transcript from s3
     * @throws IOException
     */
    public void downloadTranscript() throws IOException {
        Properties prop = new Properties();
        InputStream propstream = new FileInputStream(getServletContext().getRealPath("WEB-INF/s3.properties"));
        prop.load(propstream);
        AWSCredentials credentials = new BasicAWSCredentials(prop.getProperty("AWSAccessKeyId"), prop.getProperty("AWSSecretKey"));
        String bucketName = prop.getProperty("bucketName");
        // source: https://stackoverflow.com/questions/4bucketName1951978/amazons3clientcredentials-is-deprecated
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Write);
        try {
            String userName = userbean.getEmailFromSession();
            tutor = tut4youApp.findTutor(userName);
            String keyName = tutor.getTranscriptFilePath();
            if (keyName == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No Transcript Found","No transcript uploaded yet! Please upload a transcript."));
            } else {
                String transcriptName = tutor.getUsername();
                S3Object s3Object = s3.getObject(new GetObjectRequest(bucketName, keyName));
                S3ObjectInputStream stream = s3Object.getObjectContent();
                String home = System.getProperty("user.home");
                Path path = Paths.get(home);
                File file = new File(home, transcriptName.concat(".pdf"));
                int i = 1;
                while (file.exists() == true) { //if file exists at that location increment the file name by 1 
                    file = new File(home, transcriptName.concat("(" + i + ").pdf"));
                    i++;
                }
                FileUtils.copyInputStreamToFile(stream, file);
                FacesMessage message = new FacesMessage("Succesfully downloaded file to: " + path + "/" + file);
                FacesContext.getCurrentInstance().addMessage(null, message);
                stream.close();
            }

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:  " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:  " + ase.getErrorCode());
            System.out.println("Error Type:    " + ase.getErrorType());
            System.out.println("Request ID:    " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");
        }
    }

    /**
     * generates a secure url to view s3 file
     * @return viewFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String generatePresignedUrlRequest() throws FileNotFoundException, IOException {
        String userName = userbean.getEmailFromSession();
        tutor = tut4youApp.findTutor(userName);
        String keyName = tutor.getTranscriptFilePath();
        Properties prop = new Properties();
        InputStream propstream = new FileInputStream(getServletContext().getRealPath("WEB-INF/s3.properties"));
        prop.load(propstream);
        AWSCredentials credentials = new BasicAWSCredentials(prop.getProperty("AWSAccessKeyId"),prop.getProperty("AWSSecretKey"));
        String bucketName = prop.getProperty("bucketName");
        // source: https://stackoverflow.com/questions/4bucketName1951978/amazons3clientcredentials-is-deprecated
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        Date expiration = new Date();
        long msec = expiration.getTime();
        msec += 1000 * 60 * 60; //expires in 1 hour.
        expiration.setTime(msec);
        //source:https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/model/GeneratePresignedUrlRequest.html
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, keyName);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(expiration);
        URL s = s3.generatePresignedUrl(generatePresignedUrlRequest);
        this.url = s.toString();
        return "viewFile";
    }
}
