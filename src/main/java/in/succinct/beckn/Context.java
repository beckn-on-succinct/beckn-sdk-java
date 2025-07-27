package in.succinct.beckn;

import com.venky.core.util.ObjectUtil;

import java.time.Duration;
import java.util.Date;

public class Context extends BecknObject {

    public Context() {
        super();
    }
    public Context(String payload){
        super(payload);
    }

    public String getDomain(){
        return get("domain");
    }
    public void setDomain(String domain){
        set("domain", domain);
    }

    public String getCountry(){
        if (!ObjectUtil.isVoid(getVersion()) ){
            if(getLocation() != null && getLocation().getCountry() != null ){
                return getLocation().getCountry().getCode();
            }else {
                return null;
            }
        }else {
            return get("country");
        }
    }

    public String getVersion(){
        return get("version");
    }
    public void setVersion(String version){
        set("version",version);
    }
    public String getCity(){
        if (!ObjectUtil.isVoid(getVersion()) ){
            if(getLocation() != null && getLocation().getCity() != null ){
                return getLocation().getCity().getCode();
            }else {
                return null;
            }
        }else {
            return get("city");
        }
    }

    public String getAction(){
        return get("action");
    }
    public void setAction(String action){
        set("action",action);
    }


    public String getCoreVersion() {
        return get("core_version");
    }
    public void setCoreVersion(String coreVersion){
        set("core_version",coreVersion);
    }

    public String getBapId(){
        return get("bap_id");
    }
    public void setBapId(String bap_id){
        set("bap_id",bap_id);
    }

    public String getBapUri(){
        return get("bap_uri");
    }
    public void setBapUri(String bap_uri){
        set("bap_uri",bap_uri);
    }


    public String getBppId(){
        return get("bpp_id");
    }
    public void setBppId(String bpp_id){
        set("bpp_id",bpp_id);
    }

    public String getBppUri(){
        return get("bpp_uri");
    }
    public void setBppUri(String bpp_uri){
        set("bpp_uri",bpp_uri);
    }

    public String getTransactionId(){
        return get("transaction_id");
    }
    public void setTransactionId(String transaction_id){
        set("transaction_id",transaction_id);
    }

    public String getMessageId(){
        return get("message_id");
    }
    public void setMessageId(String message_id){
        set("message_id",message_id);
    }

    public Date getTimestamp(){
        return getTimestamp("timestamp");
    }
    public void setTimestamp(Date timestamp){
        set("timestamp",timestamp,TIMESTAMP_FORMAT);
    }

    public String getKey(){
        return get("key");
    }
    public void setKey(String key){
        set("key",key);
    }
    

    public User getUser(){
        return get(User.class, "user");
    }
    public void setUser(User user){
        set("user",user);
    }

    public void setCity(String city){
        if (ObjectUtil.isVoid(getVersion())){
            set("city",city);
        }else {
            if (getLocation() == null) {
                setLocation(new Location());
            }
            getLocation().setCity(new City());
            getLocation().getCity().setCode(city);
        }
    }
    public void setCountry(String country){
        if (ObjectUtil.isVoid(getVersion())){
            set("country",country);
        }else {
            if (getLocation() == null) {
                setLocation(new Location());
            }
            getLocation().setCountry(new Country());
            getLocation().getCountry().setCode(country);
        }
    }

    public Location getLocation(){
        return get(Location.class, "location");
    }
    public void setLocation(Location location){
        set("location",location);
    }


    public String getNetworkId(){
        return extendedAttributes.get("network_id");
    }
    public void setNetworkId(String network_id){
        extendedAttributes.set("network_id",network_id);
    }
}
