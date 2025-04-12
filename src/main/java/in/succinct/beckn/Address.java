package in.succinct.beckn;

import com.venky.core.util.ObjectUtil;

import java.util.StringTokenizer;

public class Address extends BecknObject {
    public Address() {
        super();
    }

    public String getDoor(){
        return get("door","");
    }
    public void setDoor(String door){
        set("door",door);
    }
    public String getName(){
        return get("name","");
    }
    public void setName(String name){
        set("name",name);
    }
    public String getBuilding(){
        return get("building","");
    }
    public void setBuilding(String building){
        set("building",building);
    }
    public String getStreet(){
        return get("street","");
    }
    public void setStreet(String street){
        set("street",street);
    }

    public String getLocality(){
        return get("locality","");
    }
    public void setLocality(String locality){
        set("locality",locality);
    }
    
    public String getLandmark(){
        return get("landmark");
    }
    public void setLandmark(String landmark){
        set("landmark",landmark);
    }
    
    public String getWard(){
        return get("ward","");
    }
    public void setWard(String ward){
        set("ward",ward);
    }

    public String getCity(){
        return get("city");
    }
    public void setCity(String city){
        set("city",city);
    }
    public String getState(){
        return get("state");
    }
    public void setState(String state){
        set("state",state);
    }
    public String getCountry(){
        return get("country");
    }
    public void setCountry(String country){
        set("country",country);
    }
    public String getPinCode(){
        return get("area_code");
    }
    public void setPinCode(String area_code){
        set("area_code",area_code);
    }

    public String getZipCode(){
        return getPinCode();
    }
    public void setZipCode(String zipCode){
        setPinCode(zipCode);
    }

    public String getAreaCode(){
        return getPinCode();
    }
    public void setAreaCode(String areaCode){
        setPinCode(areaCode);
    }


    public String flatten() {
        StringBuilder s = new StringBuilder();
        s.append(_flat(getDoor(), !s.isEmpty() ?  "," : ""));
        s.append(_flat(getBuilding(), !s.isEmpty() ?  "," : ""));
        s.append(_flat(getStreet(), !s.isEmpty() ?  "," : ""));
        s.append(_flat(getLocality(), !s.isEmpty() ?  "," : ""));
        s.append(_flat(getLandmark(), !s.isEmpty() ?  "," : ""));
        return s.toString();
    }

    public String[] _getAddressLines(int numLines){
        if (numLines == 2) {
            StringBuilder line1 = new StringBuilder();
            StringBuilder line2 = new StringBuilder();
            
            line1.append(_flat(getDoor(), !line1.isEmpty() ? "," : ""));
            line1.append(_flat(getBuilding(), !line1.isEmpty() ? "," : ""));
            
            line2.append(_flat(getStreet(), !line2.isEmpty() ? "," : ""));
            line2.append(_flat(getLocality(), !line2.isEmpty() ? "," : ""));
            line2.append(_flat(getLandmark(), !line2.isEmpty() ? "," : ""));
            return new String[]{line1.toString(), line2.toString()};
        }else if (numLines == 3){
            StringBuilder line1 = new StringBuilder();
            StringBuilder line2 = new StringBuilder();
            StringBuilder line3 = new StringBuilder();
            
            line1.append(_flat(getDoor(), !line1.isEmpty() ? "," : ""));
            line1.append(_flat(getBuilding(), !line1.isEmpty() ? "," : ""));
            
            line2.append(_flat(getStreet(), !line2.isEmpty() ? "," : ""));
            line2.append(_flat(getLocality(), !line2.isEmpty() ? "," : ""));
            
            line3.append(_flat(getLandmark(), !line3.isEmpty() ? "," : ""));
            return new String[]{line1.toString(),line2.toString(),line3.toString()};
        }else if (numLines == 4){
            StringBuilder line1 = new StringBuilder();
            StringBuilder line2 = new StringBuilder();
            StringBuilder line3 = new StringBuilder();
            StringBuilder line4 = new StringBuilder();
            
            line1.append(_flat(getDoor(), !line1.isEmpty() ? "," : ""));
            line1.append(_flat(getBuilding(), !line1.isEmpty() ? "," : ""));
            
            line2.append(_flat(getStreet(), !line2.isEmpty() ? "," : ""));
            line3.append(_flat(getLocality(), !line3.isEmpty() ? "," : ""));
            line4.append(_flat(getLandmark(), !line4.isEmpty() ? "," : ""));
            return new String[]{line1.toString(),line2.toString(),line3.toString(), line4.toString()};
        }else {
            throw new IllegalArgumentException("numLines="+numLines);
        }
    }
    public void _setAddressLines(String... lines){
        int numLines = lines.length;
        StringTokenizer line1Tokens  = new StringTokenizer(lines[0],", ");
        if (line1Tokens.hasMoreTokens()) {
            setDoor(line1Tokens.nextToken());
        }
        StringBuilder building  = new StringBuilder();
        while(line1Tokens.hasMoreTokens()){
            if(!building.isEmpty()){
                building.append(" ");
            }
            building.append(line1Tokens.nextToken());
        }
        setBuilding(building.toString());
        
        
        if (numLines == 2) {
            StringTokenizer line2Tokens  = new StringTokenizer(lines[1],",");
            if (line2Tokens.hasMoreTokens()){
                setStreet(line2Tokens.nextToken());
            }
            if (line2Tokens.hasMoreTokens()){
                setLocality(line2Tokens.nextToken());
            }
            if (line2Tokens.hasMoreTokens()){
                setLandmark(line2Tokens.nextToken());
            }
        }else if (numLines == 3){
            StringTokenizer line2Tokens  = new StringTokenizer(lines[1],", ");
            if (line2Tokens.hasMoreTokens()) {
                setStreet(line2Tokens.nextToken());
            }
            StringBuilder locality  = new StringBuilder();
            while(line2Tokens.hasMoreTokens()){
                if(!locality.isEmpty()){
                    locality.append(" ");
                }
                locality.append(line2Tokens.nextToken());
            }
            
            setLocality(locality.toString());
            setLandmark(lines[2]);
        }else if (numLines == 4){
            setStreet(lines[1]);
            setLocality(lines[2]);
            setLandmark(lines[3]);
        }else {
            throw new IllegalArgumentException("numLines="+numLines);
        }
    }



}
