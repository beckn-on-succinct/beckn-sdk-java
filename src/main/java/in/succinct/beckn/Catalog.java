package in.succinct.beckn;

import java.time.Duration;
import java.util.Date;

public class Catalog extends BecknObjectWithId {
    public Catalog(){
        super();
    }
    public Catalog(String payload){
        super(payload);
    }

    public Descriptor getDescriptor(){
        return get(Descriptor.class,"bpp/descriptor");
    }
    public void setDescriptor(Descriptor descriptor){
        set("bpp/descriptor", descriptor);
    }

    public Providers getProviders(){
        return get(Providers.class,"bpp/providers");
    }

    public void setProviders(Providers providers){
        set("bpp/providers",providers);
    }

    public Fulfillments getFulfillments(){
        return get(Fulfillments.class, "bpp/fulfillments");
    }
    public void setFulfillments(Fulfillments fulfillments){
        set("bpp/fulfillments",fulfillments);
    }

    public Categories getCategories(){
        return get(Categories.class, "bpp/categories");
    }
    public void setCategories(Categories categories){
        set("bpp/categories",categories);
    }

    public Payments getPayments(){
        return get(Payments.class, "bpp/payments");
    }
    public void setPayments(Payments payments){
        set("bpp/payments",payments);
    }

    public Date getExp(){
        return getTimestamp("exp");
    }
    public void setExp(Date exp){
        set("exp",exp,TIMESTAMP_FORMAT);
    }


}
