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
public class ZipCodeAPI {
    
    private DataList[] DataList;

    public DataList[] getDataList ()
    {
        return DataList;
    }

    public void setDataList (DataList[] DataList)
    {
        this.DataList = DataList;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [DataList = "+DataList+"]";
    }
}
