package in.succinct.beckn;

import org.json.simple.JSONArray;

import javax.sound.midi.Receiver;
import java.util.Date;

public class Conversation extends BecknObjectWithId{
    public String getSubject(){
        return get("subject");
    }
    public void setSubject(String subject){
        set("subject",subject);
    }
    
    public Date getDate(){
        return getDate("date");
    }
    public void setDate(Date date){
        set("date",date, BecknObject.DATE_FORMAT);
    }
    
    
    public String getText(){
        return get("text");
    }
    public void setText(String text){
        set("text",text);
    }

    public User getSender(){
        return get(User.class, "sender");
    }
    public void setSender(User sender){
        set("sender",sender);
    }
    
    
    public String getParentId(){
        return get("parent_id");
    }
    public void setParentId(String parent_id){
        set("parent_id",parent_id);
    }
    
    public static class Conversations extends BecknObjectsWithId<Conversation>{
        public Conversations() {
            super(true);
        }
        
        public Conversations(String payload) {
            super(payload,true);
        }
        
        public Conversations(JSONArray array) {
            super(array, true);
        }
    }
}
