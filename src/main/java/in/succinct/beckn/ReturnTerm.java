package in.succinct.beckn;

import org.json.simple.JSONArray;

public class ReturnTerm extends BecknObject{
    public FulfillmentState getFulfillmentState(){
        return get(FulfillmentState.class, "fulfillment_state");
    }
    public void setFulfillmentState(FulfillmentState fulfillment_state){
        set("fulfillment_state",fulfillment_state);
    }
    public boolean isReturnEligible(){
        return getBoolean("return_eligible");
    }
    public void setReturnEligible(boolean return_eligible){
        set("return_eligible",return_eligible);
    }

    public Time getReturnTime(){
        return get(Time.class, "return_time");
    }
    public void setReturnTime(Time return_time){
        set("return_time",return_time);
    }

    public Location getReturnLocation(){
        return get(Location.class, "return_location");
    }
    public void setReturnLocation(Location return_location){
        set("return_location",return_location);
    }
    
    public FulfillmentMangedBy getFulfillmentMangedBy(){
        return getEnum(FulfillmentMangedBy.class, "fulfillment_manged_by");
    }
    public void setFulfillmentMangedBy(FulfillmentMangedBy fulfillment_manged_by){
        setEnum("fulfillment_manged_by",fulfillment_manged_by);
    }

    public enum FulfillmentMangedBy {
        BUYER,
        PROVIDER;

        public static EnumConvertor<FulfillmentMangedBy> convertor = new EnumConvertor<>(FulfillmentMangedBy.class);
    }


    public static class ReturnTerms extends BecknObjects<ReturnTerm> {
        public ReturnTerms() {
        }

        public ReturnTerms(JSONArray value) {
            super(value);
        }

        public ReturnTerms(String payload) {
            super(payload);
        }
    }

}
