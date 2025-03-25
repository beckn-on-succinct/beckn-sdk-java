package in.succinct.beckn;

import com.venky.core.util.ObjectUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.Duration;
import java.util.Date;

public class Provider extends BecknObjectWithId implements TagGroupHolder {
    public Provider(){
        super();
    }
    public Provider(String payload){
        super(payload);
    }
    public Descriptor getDescriptor(){
        return get(Descriptor.class,"descriptor");
    }
    public void setDescriptor(Descriptor descriptor){
        set("descriptor",descriptor);
    }

    public Locations getLocations(){
        return getLocations(false);
    }
    public Locations getLocations(boolean createIfAbsent){
        return get(Locations.class,"locations",createIfAbsent);
    }
    public void setLocations(Locations locations){
        set("locations",locations);
    }
    public  Items getItems(){
        return get(Items.class,"items");
    }
    public void setItems(Items items){
        set("items",items);
    }

    public Categories getCategories(){
        return get(Categories.class,"categories");
    }
    public void setCategories(Categories categories){
        set("categories",categories);
    }

    public Fulfillments getFulfillments(){
        return get(Fulfillments.class, "fulfillments");
    }
    public void setFulfillments(Fulfillments fulfillments){
        set("fulfillments",fulfillments);
    }

    public Payments getPayments(){
        return get(Payments.class, "payments");
    }
    public void setPayments(Payments payments){
        set("payments",payments);
    }


    public Long getTtl(){
        String ttl = get("ttl");
        return ttl == null ? null : Duration.parse(ttl).getSeconds();
    }
    public void setTtl(Long ttl){
        set("ttl", ttl == null ? null : Duration.ofSeconds(ttl).toString());
    }


    public Offers getOffers(){
        return get(Offers.class, "offers");
    }
    public void setOffers(Offers offers){
        set("offers",offers);
    }

    public Date getExp(){
        return getTimestamp("exp");
    }
    public void setExp(Date exp){
        set("exp",exp,TIMESTAMP_FORMAT);
    }


    public String getCategoryId(){
        return get("category_id");
    }
    public void setCategoryId(String category_id){
        set("category_id",category_id);
    }

    public Integer getRating(){
        return getInteger("rating", null);
    }
    public void setRating(Integer rating){
        set("rating",rating);
    }

    public Time getTime(){
        return get(Time.class,"time");
    }
    public void setTime(Time time){
        set("time",time);
    }

    public Boolean isRateable(){
        return getBoolean("rateable",null);
    }
    public void setRateable(Boolean rateable){
        set("rateable",rateable);
    }

    public boolean isExtendedAttributesDisplayed(){
        return true;
    }
    public String getFssaiLicenceNo(){
        return getTag("licenses", "fssai");
    }
    public void setFssaiLicenceNo(String fssai_licence_no){
        setTag("licenses","fssai",fssai_licence_no);
    }
    public String getBppId(){
        return getTag("bpp","id");
    }
    public void setBppId(String bpp_id){
        setTag("bpp","id",bpp_id);
    }
    
    public Directories getDirectories(){
        Directories directories = getObjectCreator().create(Directories.class);
        TagGroups groups = getTags();
        if (groups != null) {
            for (TagGroup tag : groups) {
                if (ObjectUtil.equals(tag.getId(), "directories")) {
                    for (TagGroup tagGroup : tag.getList()) {
                        Directory directory = directories.getObjectCreator().create(Directory.class);
                        directory.setId(tagGroup.getId());
                        directory.setDescriptor(new Descriptor() {{
                            setName(tagGroup.getValue());
                        }});
                        directories.add(directory);
                    }
                }
            }
        }
        return directories;
    }
    public void setDirectories(Directories directories){
        for (Directory directory : directories) {
            setTag("directories", directory.getId(), directory.getDescriptor().getName());
        }
        TagGroup tg = getTags().get("directories");
        if (tg != null) {
            tg.setDisplay(false);
        }
    }
    
    
    
    public static class Directories extends BecknObjectsWithId<Directory> {
        public Directories() {
        }
        
        public Directories(String payload) {
            super(payload);
        }
        
        public Directories(JSONArray array) {
            super(array);
        }
    }
    public static class Directory extends BecknObjectWithId {
        public Directory() {
        }
        
        public Directory(String payload) {
            super(payload);
        }
        
        public Directory(JSONObject object) {
            super(object);
        }
        
        public Descriptor getDescriptor(){
            return get(Descriptor.class, "descriptor");
        }
        public void setDescriptor(Descriptor descriptor){
            set("descriptor",descriptor);
        }
        
    }

    @Override
    public TagGroups getTags() {
        return TagGroupHolder.super.getTags();
    }

    @Override
    public void setTags(TagGroups tags) {
        TagGroupHolder.super.setTags(tags);
    }
    
    public void rmTags(String name){
        TagGroupHolder.super.rmTags(name);
    }

    public ServiceablityTags getServiceablityTags(){
        return extendedAttributes.get(ServiceablityTags.class, "tags");
    }
    public void setServiceablityTags(ServiceablityTags tags){
        extendedAttributes.set("tags",tags);
    }

    public static class ServiceablityTags extends TagGroups {

        public ServiceablityTags() {
        }

        public ServiceablityTags(JSONArray value) {
            super(value);
        }

    }

}
