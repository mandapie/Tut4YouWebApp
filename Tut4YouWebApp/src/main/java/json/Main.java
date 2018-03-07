/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import okhttp3.Request;
import okhttp3.Response;


/**
 *
 * @author Keith
 */

public class Main {
    private static OkHttpClient client = new OkHttpClient();
    public static void main(String[] args)
    {   
        Scanner scan = new Scanner(System.in); 
        System.out.print("Enter zip code: ");
        int zipCode = scan.nextInt();
        System.out.print("Enter max radius: ");
        int maxRadius = scan.nextInt();
        for(String str : getData(maxRadius, zipCode) ) {
            System.out.println(str);
            List<String> result = Arrays.asList(str.substring(1, str.length() - 1).split(","));
            System.out.print(result);
        }
        
    }
    public static String getJSON(String url) throws IOException {
        Request request = new Request.Builder()
        .url(url)
        .build();
        
        Response response = client.newCall(request).execute();
     
        return response.body().string(); 

    }
    public static String[] getData(int maxRadius, int zipCode) {
        String json = null;
        try {
            json = getJSON("http://api.zip-codes.com/ZipCodesAPI.svc/1.0/FindZipCodesInRadius?zipcode="+zipCode+"&minimumradius=0&maximumradius="+maxRadius+"&key=MCK3KZ9AEI25BGAIE0IF");
        } catch(IOException e) {
            
        }
        
        Gson gson = new Gson();
        ZipCodeAPI zipCodeAPI = gson.fromJson(json, ZipCodeAPI.class);
        
        return new String[]{
            Arrays.toString(zipCodeAPI.getDataList())
         };
    }
}
