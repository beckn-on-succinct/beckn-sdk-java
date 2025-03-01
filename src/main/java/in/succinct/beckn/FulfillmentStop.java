package in.succinct.beckn;

import org.json.simple.JSONObject;

public class FulfillmentStop extends BecknObjectWithId {

    public FulfillmentStop() {
        super();
    }

    public FulfillmentStop(JSONObject stop) {
        super(stop);
    }
    public FulfillmentStop(String payload){
        super(payload);
    }


    public String getParentStopId(){
        return get("parent_stop_id");
    }
    public void setParentStopId(String parent_stop_id){
        set("parent_stop_id",parent_stop_id);
    }

    public String getType(){
        return get("type");
    }
    public void setType(String type){
        set("type",type);
    }

    public Location getLocation(){
        return get(Location.class,"location");
    }
    public void setLocation(Location location){
        set("location",location);
    }

    public Time getTime(){
        return get(Time.class, "time");
    }
    public void setTime(Time time){
        set("time",time);
    }
    public Descriptor getInstructions(){
        return get(Descriptor.class,"instructions");
    }
    public void setInstructions(Descriptor instructions){
        set("instructions",instructions);
    }
    public Contact getContact(){
        return get(Contact.class,"contact");
    }
    public void setContact(Contact contact){
        set("contact",contact);
    }

    public Person getPerson(){
        return get(Person.class, "person");
    }
    public void setPerson(Person person){
        set("person",person);
    }

    public Authorization getAuthorization(){
        return get(Authorization.class, "authorization");
    }
    public void setAuthorization(Authorization authorization){
        set("authorization",authorization);
    }




}
