package in.succinct.beckn;

import java.util.Date;

public class Organization extends BecknObject implements AddressHolder , TagGroupHolder {

    public Descriptor getDescriptor(){
        return get(Descriptor.class, "descriptor");
    }
    public void setDescriptor(Descriptor descriptor){
        set("descriptor",descriptor);
    }
    public Descriptor getDescriptor(boolean create){
        return get(Descriptor.class, "descriptor",create);
    }
    @Override
    public TagGroups getTags() {
        return TagGroupHolder.super.getTags();
    }
    
    @Override
    public void setTags(TagGroups tags) {
        TagGroupHolder.super.setTags(tags);
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


    public State getState(){
        return get(State.class, "state");
    }
    public void setState(State state){
        set("state",state);
    }

    public City getCity(){
        return get(City.class, "city");
    }
    public void setCity(City city){
        set("city",city);
    }

    public Contact getContact(boolean create){
        return get(Contact.class, "contact", create);
    }
    public Contact getContact(){
        return get(Contact.class, "contact");
    }
    public void setContact(Contact contact){
        set("contact",contact);
    }

    public Address getAddress(){
        return AddressHolder.super.getAddress();
    }
    public void setAddress(Address address){
        AddressHolder.super.setAddress(address);
    }
    public String getName(){
        return get("name");
    }
    public void setName(String name){
        set("name",name);
    }
    public String getEmail(){
        return get("email");
    }
    public void setEmail(String email){
        set("email",email);
    }

    public BecknStrings getPhones(){
        return get(BecknStrings.class,"phones");
    }
    public boolean isExtendedAttributesDisplayed(){
        return true;
    }





    public String getIncomeTaxId(){
        return extendedAttributes.get("income_tax_id");
    }
    public void setIncomeTaxId(String income_tax_id){
        extendedAttributes.set("income_tax_id",income_tax_id);
    }

    public String getSalesTaxId(){
        return extendedAttributes.get("sales_tax_id");
    }
    public void setSalesTaxId(String sales_tax_id){
        extendedAttributes.set("sales_tax_id",sales_tax_id);
    }

    public Date getDateOfIncorporation(){
        return extendedAttributes.getDate("date_of_incorporation");
    }
    public void setDateOfIncorporation(Date date_of_incorporation){
        extendedAttributes.set("date_of_incorporation",date_of_incorporation, BecknObject.DATE_FORMAT);
    }

    public User getAuthorizedSignatory(){
        return extendedAttributes.get(User.class, "authorized_signatory");
    }
    public void setAuthorizedSignatory(User authorized_signatory){
        extendedAttributes.set("authorized_signatory",authorized_signatory);
    }
    
    public String getFaqUrl(){
        return extendedAttributes.get("faq_url");
    }
    public void setFaqUrl(String faq_url){
        extendedAttributes.set("faq_url",faq_url);
    }


}
