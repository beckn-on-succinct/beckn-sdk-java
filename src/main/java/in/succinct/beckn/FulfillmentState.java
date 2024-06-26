package in.succinct.beckn;

import java.util.Date;

public class FulfillmentState extends BecknObject{
    public FulfillmentState() {
    }
    public Descriptor getDescriptor(){
        return get(Descriptor.class, "descriptor");
    }
    public Descriptor getDescriptor(boolean create){
        return get(Descriptor.class, "descriptor",create);
    }
    public void setDescriptor(Descriptor descriptor){
        set("descriptor",descriptor);
    }

    public Date getUpdatedAt(){
        return getTimestamp("updated_at");
    }
    public void setUpdatedAt(Date updated_at){
        set("updated_at",updated_at,TIMESTAMP_FORMAT);
    }

    public String getUpdatedBy(){
        return get("updated_by");
    }
    public void setUpdatedBy(String updated_by){
        set("updated_by",updated_by);
    }
}
