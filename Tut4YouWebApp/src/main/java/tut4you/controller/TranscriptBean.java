/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import static org.omnifaces.util.Faces.getServletContext;
import org.primefaces.model.UploadedFile;
import tut4you.model.Tut4YouApp;
import tut4you.model.Tutor;

/**
 * Uploads file to amazon s3 and saves the file path in the database
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Named
@RequestScoped
public class TranscriptBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("TranscriptBean");
    
    private UserBean userbean = new UserBean();
    @EJB
    private Tut4YouApp tut4youApp;
    private UploadedFile file;
    private Tutor tutor;
    
    public TranscriptBean () {
        
    }
    public TranscriptBean(UploadedFile file) {
        this.file = file;
    }
    /**
     * Sets the transcript file
     * @param file file to be set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    /**
     * Gets the file
     * @return file
     */
    public UploadedFile getFile() {
        return file;
    }
    /**
     * This method takes a user submitted pdf and uploads it to an Amazon S3 instance; takes the s3 file path and adds it to the database 
     * Referenced: https://github.com/aws-samples/aws-java-sample/blob/master/src/main/java/com/amazonaws/samples/S3Sample.java
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
        } catch (Exception e) {
            throw new AmazonClientException(e);
        }
        
        //Taken from: https://stackoverflow.com/questions/4bucketName1951978/amazons3clientcredentials-is-deprecated
        
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Write);
        try {
            String userName = userbean.getUsernameFromSession();
            tutor = tut4youApp.findTutorUserName(userName);
            String transcriptName = tutor.getUserName();
            String keyName = transcriptName.concat(".pdf");
            
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
                URL url = s3.getUrl(bucketName, keyName);
                String filepath = url.toString();
                tut4youApp.addTranscriptFileLocation(filepath);
                
            }
            System.out.println("Uploaded successfully!");
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was "
                    + "rejected with an error response for some reason.");
            System.out.println("Error Message:  " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:  " + ase.getErrorCode());
            System.out.println("Error Type:    " + ase.getErrorType());
            System.out.println("Request ID:    " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while "
                    + "trying to communicate with S3, such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
   
}
