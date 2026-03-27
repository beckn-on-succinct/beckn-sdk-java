package in.succinct.beckn;

import in.succinct.beckn.Descriptor.Descriptors;

import java.util.Date;

public class Person extends BecknObjectWithId implements TagGroupHolder{
    public Person() {
        super();
    }
    public String getName(){
        return get("name");
    }
    
    
    public String getFirstName(){
        String name = getName();
        int i =  name.indexOf(" ");
        return i < 0 ? name : getName().substring(0,i);
    }
    public String getLastName(){
        int i =  getName().lastIndexOf(" ");
        return getName().substring(i+1);
    }
    
    
    
    public void setName(String name){
        set("name",name);
    }

    public Date getDob(){
        return getDate("dob");
    }
    public void setDob(Date date){
        set("dob",date,DATE_FORMAT);
    }

    public String getGender(){
        return get("gender");
    }
    public void setGender(String gender){
        set("gender",gender);
    }

    public String getUrl(){
        return get("url");
    }
    public void setUrl(String url){
        set("url",url);
    }

    public Descriptors getCredentials(){
        return get(Descriptors.class, "creds");
    }
    public void setCredentials(Descriptors credentials){
        set("creds",credentials);
    }

    public Descriptors getLanguages(){
        return get(Descriptors.class, "languages");
    }
    public void setLanguages(Descriptors languages){
        set("languages",languages);
    }


    @Override
    public TagGroups getTags() {
        return TagGroupHolder.super.getTags();
    }

    @Override
    public void setTags(TagGroups tags) {
        TagGroupHolder.super.setTags(tags);
    }




}
