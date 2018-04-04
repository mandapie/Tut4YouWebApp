/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

/**
 *
 * @author Keith
 */
public class DataList {
    private String County;

    private String State;

    private String Latitude;

    private String Longitude;

    private String Code;

    private String City;

    public String getCounty ()
    {
        return County;
    }

    public void setCounty (String County)
    {
        this.County = County;
    }

    public String getState ()
    {
        return State;
    }

    public void setState (String State)
    {
        this.State = State;
    }

    public String getLatitude ()
    {
        return Latitude;
    }

    public void setLatitude (String Latitude)
    {
        this.Latitude = Latitude;
    }

    public String getLongitude ()
    {
        return Longitude;
    }

    public void setLongitude (String Longitude)
    {
        this.Longitude = Longitude;
    }

    public String getCode ()
    {
        return Code;
    }

    public void setCode (String Code)
    {
        this.Code = Code;
    }

    public String getCity ()
    {
        return City;
    }

    public void setCity (String City)
    {
        this.City = City;
    }

    @Override
    public String toString()
    {
        //return "ClassPojo [County = "+County+", State = "+State+", Latitude = "+Latitude+", Longitude = "+Longitude+", Code = "+Code+", City = "+City+"]";
        return Code;
    }
}