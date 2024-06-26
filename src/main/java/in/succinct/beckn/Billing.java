package in.succinct.beckn;

import java.util.Date;
import java.util.StringTokenizer;

public class Billing extends BecknObject implements AddressHolder {
    public Billing(){
        super();
    }

    public String getName(){
        return get("name");
    }
    public void setName(String name){
        set("name",name);
    }

    public Organization getOrganization(){
        return get(Organization.class,"organization");
    }
    public void setOrganization(Organization organization){
        set("organization",organization);
    }



    public City getCity(){
        return get(City.class, "city");
    }
    public void setCity(City city){
        set("city",city);
    }

    public State getState(){
        return get(State.class, "state");
    }
    public void setState(State state){
        set("state",state);
    }

    public Country getCountry(){
        return get(Country.class, "country");
    }
    public void setCountry(Country country){
        set("country",country);
    }

    public String getPinCode(){
        return get("pin_code");
    }
    public void setPinCode(String pin_code){
        set("pin_code",pin_code);
    }

    public Address getAddress(){
        return AddressHolder.super.getAddress();
    }
    public void setAddress(Address address){
        AddressHolder.super.setAddress(address);
    }


    public String getEmail(){
        return get("email");
    }
    public void setEmail(String email){
        set("email",email);
    }
    public String getPhone(){
        return get("phone");
    }
    public void setPhone(String phone){
        set("phone",phone);
    }


    public String getTaxNumber(){
        return get("tax_number");
    }

    public void setTaxNumber(String taxNumber){
        set("tax_number",taxNumber);
    }

    public Time getTime(){
        return get(Time.class,"time");
    }
    public void setTime(Time time){
        set("time",time);
    }
    
    public Date getCreatedAt(){
        return getTimestamp("created_at");
    }
    public void setCreatedAt(Date created_at){
        set("created_at",created_at,TIMESTAMP_FORMAT);
    }

    public Date getUpdatedAt(){
        return getTimestamp("updated_at");
    }
    public void setUpdatedAt(Date updated_at){
        set("updated_at",updated_at,TIMESTAMP_FORMAT);
    }

}
