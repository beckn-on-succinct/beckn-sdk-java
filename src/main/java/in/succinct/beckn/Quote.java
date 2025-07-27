package in.succinct.beckn;

import org.json.simple.JSONObject;

import java.time.Duration;

public class Quote extends BecknObjectWithId {
    public Quote() {
        super();
    }



    public void setPrice(Price price) {
        set("price",price);
    }
    public  Price getPrice(){
        return get(Price.class,"price");
    }


    public void setBreakUp(BreakUp breakUp) {
        set("breakup",breakUp);
    }
    public BreakUp getBreakUp(){
        return get(BreakUp.class,"breakup");
    }
}
