package in.succinct.beckn;

import in.succinct.beckn.Fulfillment.FulfillmentStatus.FulfillmentStatusConvertor;

import java.time.Duration;

public class Fulfillment extends BecknObjectWithId implements TagGroupHolder{

    public Fulfillment() {
        super();
    }

    public Fulfillment(String payload) {
        super(payload);
    }

    public String getType() {
        String s = get("type");
        return s == null ? null : s.replace('-','_').toLowerCase();
    }

    public FulfillmentStops getFulfillmentStops(){
        return get(FulfillmentStops.class, "stops");
    }
    public void setFulfillmentStops(FulfillmentStops fulfillment_stops){
        set("stops",fulfillment_stops);
    }
    public void setType(String type) {
        set("type", type == null ? null : type.replace('_','-').toUpperCase());
    }

    public enum RetailFulfillmentType {
        home_delivery(1),
        store_pickup(2),
        store_pickup_and_home_delivery(1|2),
        return_to_origin(4),
        cancel(8);

        final int bits;
        RetailFulfillmentType(int bits){
            this.bits = bits;
        }
        public boolean matches(RetailFulfillmentType other){
            return (other == null || (other.bits & bits) > 0);
        }

    }

    public FulfillmentStop getStart() {
        return get(FulfillmentStop.class, "start");
    }

    public void setStart(FulfillmentStop start) {
        set("start", start);
    }

    public FulfillmentStop getEnd() {
        return get(FulfillmentStop.class, "end");
    }

    public void setEnd(FulfillmentStop end) {
        set("end", end);
    }

    public boolean getTracking() {
        return getBoolean("tracking");
    }

    public void setTracking(boolean tracking) {
        set("tracking", tracking);
    }

    public Agent getAgent() {
        return get(Agent.class,"agent");
    }


    public void setAgent(Agent agent) {
        set("agent", agent);
    }

    public FulfillmentState getState() {
        return getState(false);
    }

    public FulfillmentState getState(boolean create) {
        return get(FulfillmentState.class, "state", create);
    }

    public void setState(FulfillmentState state) {
        set("state", state);
    }

    private static final FulfillmentStatusConvertor convertor = new FulfillmentStatusConvertor();
    public EnumConvertor<FulfillmentStatus> getFulfillmentStatusConvertor(){
        return convertor;
    }
    public void setFulfillmentStatus(FulfillmentStatus state) {
        if (state != null ) {
            getState(true).getDescriptor(true).setEnum("code", state, getFulfillmentStatusConvertor());
        }else {
            FulfillmentState s = getState();
            if (s != null){
                Descriptor d  = s.getDescriptor();
                if (d != null){
                    d.setEnum("code",null);
                }

            }
        }
    }
    public FulfillmentStatus getFulfillmentStatus(){
        FulfillmentState s = getState();
        if (s != null){
            Descriptor d  = s.getDescriptor();
            if (d != null){
                return d.getEnum(FulfillmentStatus.class,"code", getFulfillmentStatusConvertor());
            }
        }
        return null;
        //return getState(true).getDescriptor(true).getEnum(FulfillmentStatus.class,"code", new FulfillmentStatusConvertor());
    }

    public User getCustomer() {
        return get(User.class, "customer");
    }

    public void setCustomer(User customer) {
        set("customer", customer);
    }

    public Vehicle getVehicle() {
        return get(Vehicle.class, "vehicle");
    }

    public void setVehicle(Vehicle vehicle) {
        set("vehicle", vehicle);
    }

    public boolean isRateable() {
        return getBoolean("rateable");
    }

    public void setRateable(boolean rateable) {
        set("rateable", rateable);
    }

    public String getProviderId() {
        return extendedAttributes.get("provider_id");
    }

    public void setProviderId(String provider_id) {
        extendedAttributes.set("provider_id", provider_id);
    }

    public Integer getRating() {
        return getInteger("rating",null);
    }

    public void setRating(Integer rating) {
        set("rating", rating);
    }

    public Contact getContact() {
        return get(Contact.class, "contact");
    }

    public void setContact(Contact contact) {
        set("contact", contact);
    }

    //Extended attributes rationalized from networks

    public boolean isExtendedAttributesDisplayed(){
        return true;
    }
    public String getCategory(){
        return extendedAttributes.get("category");
    }
    public void setCategory(String category){
        extendedAttributes.set("category",category);
    }

    public Duration getTAT(){
        String tat =  extendedAttributes.get("tat");
        return tat == null ? null : Duration.parse(tat);
    }
    public void setTAT(Duration tat){
        extendedAttributes.set("tat",tat == null ? null : tat.toString());
    }

    public String getProviderName(){
        return extendedAttributes.get("provider_name");
    }
    public void setProviderName(String provider_name){
        extendedAttributes.set("provider_name",provider_name);
    }


    public SettlementDetails getSettlementDetails(){
        return extendedAttributes.get(SettlementDetails.class, "settlement_details");
    }
    public void setSettlementDetails(SettlementDetails settlement_details){
        extendedAttributes.set("settlement_details",settlement_details);
    }


    public enum FulfillmentStatus {
        Serviceable,
        Pending,
        Packed,
        Order_picked_up,
        Out_for_delivery,
        Order_delivered,
        Cancelled,
        Return_Initiated,
        Return_Liquidated,
        Return_Approved,
        Return_Rejected,
        Return_Delivered;
        public static class FulfillmentStatusConvertor extends EnumConvertor<FulfillmentStatus>{}
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


