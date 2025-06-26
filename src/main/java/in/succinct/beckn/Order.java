package in.succinct.beckn;

import com.venky.core.math.DoubleUtils;
import com.venky.core.util.Bucket;
import com.venky.core.util.ObjectUtil;
import in.succinct.beckn.CancellationTerm.CancellationTerms;
import in.succinct.beckn.Conversation.Conversations;
import in.succinct.beckn.Fulfillment.FulfillmentStatus;
import in.succinct.beckn.Invoice.Invoices;
import in.succinct.beckn.Order.Status.StatusConverter;
import in.succinct.beckn.Payment.PaymentStatus;
import in.succinct.beckn.RefundTerm.RefundTerms;
import in.succinct.beckn.ReplacementTerm.ReplacementTerms;
import in.succinct.beckn.ReturnTerm.ReturnTerms;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order extends BecknObjectWithId implements TagGroupHolder{
    public Order() {
        super();
    }
    public Order(String payload){
        super(payload);
    }
    
    public BecknStrings getRefOrderIds(){
        return get(BecknStrings.class,"ref_order_ids",true);
    }
    public void setRefOrderIds(BecknStrings ref_order_ids){
        set("ref_order_ids",ref_order_ids);
    }
    
    
    public void setStatus(Status status){
        setEnum("status", status, new StatusConverter());
    }
    public Status getStatus(){
        return getEnum(Status.class, "status", new StatusConverter());
    }
    
    public OrderType getType(){
        return getEnum(OrderType.class, "type");
    }
    public void setType(OrderType order_type){
        setEnum("type",order_type);
    }
    
    
    public enum OrderType {
        DRAFT,
        DEFAULT
    }
    
    
    public Provider getProvider(){
        return getProvider(false);
    }
    public Provider getProvider(boolean createIfAbsent){
        return get(Provider.class,"provider",createIfAbsent);
    }
    public void setProvider(Provider provider){
        set("provider",provider);
    }


    public NonUniqueItems getItems(){
        return get(NonUniqueItems.class,"items");
    }
    public void setItems(NonUniqueItems  items){
        set("items",items);
    }

    public static class NonUniqueItems extends BecknObjectsWithId<Item> {
        public NonUniqueItems() {
            super(false);
        }
    }


    public AddOns getAddOns(){
        return get(AddOns.class, "add_ons");
    }
    public void setAddOns(AddOns add_ons){
        set("add_ons",add_ons);
    }

    public Offers getOffers(){
        return get(Offers.class, "offers");
    }
    public void setOffers(Offers offers){
        set("offers",offers);
    }


    public Billing getBilling(){
        return get(Billing.class,"billing");
    }
    public void setBilling( Billing billing){
        set("billing",billing);
    }

    public Fulfillments getFulfillments(){
        return getFulfillments(false);
    }
    public Fulfillments getFulfillments(boolean createIfAbsent){
        return get(Fulfillments.class, "fulfillments",createIfAbsent);
    }
    public void setFulfillments(Fulfillments fulfillments){
        set("fulfillments",fulfillments);
    }
    public Integer getRating() {
        return getInteger("rating",null);
    }
    
    public void setRating(Integer rating) {
        set("rating", rating);
    }
    
    
    public Cancellation getCancellation(){
        return get(Cancellation.class, "cancellation");
    }
    public void setCancellation(Cancellation cancellation){
        set("cancellation",cancellation);
    }
    
    public CancellationTerms getCancellationTerms(){
        return get(CancellationTerms.class, "cancellation_terms");
    }
    public void setCancellationTerms(CancellationTerms cancellation_terms){
        set("cancellation_terms",cancellation_terms);
    }
    
    public RefundTerms getRefundTerms(){
        return get(RefundTerms.class, "refund_terms");
    }
    public void setRefundTerms(RefundTerms refund_terms){
        set("refund_terms",refund_terms);
    }
    
    public ReplacementTerms getReplacementTerms(){
        return get(ReplacementTerms.class, "replacement_terms");
    }
    public void setReplacementTerms(ReplacementTerms replacement_terms){
        set("replacement_terms",replacement_terms);
    }
    
    public ReturnTerms getReturnTerms(){
        return get(ReturnTerms.class, "return_terms");
    }
    public void setReturnTerms(ReturnTerms return_terms){
        set("return_terms",return_terms);
    }
    

    public Quote getQuote(){
        return get(Quote.class,"quote");
    }
    public void setQuote(Quote quote){
        set("quote",quote);
    }

    
    public Payments getPayments(){
        return getPayments(true);
    }
    public Payments getPayments(boolean createIfAbsent){
        return get(Payments.class, "payments",createIfAbsent);
    }
    public void setPayments(Payments payments){
        set("payments",payments);
    }


    public Date getCreatedAt(){
        return getTimestamp("created_at");
    }
    public Date getUpdatedAt(){
        return getTimestamp("updated_at");
    }
    public void setCreatedAt(Date date){
        set("created_at",date,TIMESTAMP_FORMAT);
    }
    public void setUpdatedAt(Date date){
        set("updated_at",date,TIMESTAMP_FORMAT);
    }
    
    public Xinput getXinput(){
        return get(Xinput.class, "xinput");
    }
    public void setXinput(Xinput xinput){
        set("xinput",xinput);
    }
    
    @Override
    public TagGroups getTags() {
        return TagGroupHolder.super.getTags();
    }
    
    @Override
    public void setTags(TagGroups tags) {
        TagGroupHolder.super.setTags(tags);
    }
    
    
    public boolean isExtendedAttributesDisplayed(){
        return true;
    }
    
    
    public Documents getDocuments(){
        return extendedAttributes.get(Documents.class,"documents");
    }
    public void setDocuments(Documents documents){
        extendedAttributes.set("documents",documents);
    }
    
    public Conversations getConversations(){
        return extendedAttributes.get(Conversations.class, "conversations");
    }
    public void setConversations(Conversations conversations){
        extendedAttributes.set("conversations",conversations);
    }


    public enum Status {
        Created,
        Awaiting_Acceptance,
        Accepted, // Is when fulfillment status is preparing
        Prepared, // Is based on fulfillment Status Prepared
        In_Transit, // Is based on fulfillment status
        Completed(){
            @Override
            public boolean isOpen() {
                return false;
            }
        },
        Cancelled(){
            @Override
            public boolean isOpen() {
                return false;
            }
            @Override
            public boolean isPaymentRequired() {
                return false;
            }
        };
        
        public boolean isOpen(){
            return true;
        }
        
        
        public boolean isPaymentRequired(){
            return true;
        }
        

        public static class StatusConverter extends EnumConvertor<Status> {

        }
    }
    public static class Orders extends BecknObjectsWithId<Order> {
        public Orders() {
        }

        public Orders(JSONArray array) {
            super(array);
        }

        public Orders(String payload) {
            super(payload);
        }
    }
    

    
    
    public Invoices getInvoices(){
        return get(Invoices.class, "invoices",true);
    }
    public void setInvoices(Invoices invoices){
        set("invoices",invoices);
    }
    
    
    
    public boolean isPaid(){
        Fulfillments fulfillments = getFulfillments();
        Invoices invoices = getInvoices();
        boolean paid = false;

        Payments payments  = getPayments(); //These are the payment terms
        if (!invoices.isEmpty()){
            Bucket unpaidInvoicedAmount = new Bucket();
            for (Invoice invoice : invoices){
                unpaidInvoicedAmount.increment(invoice.getUnpaidAmount().doubleValue());
            }
            paid = DoubleUtils.equals(unpaidInvoicedAmount.doubleValue() ,0);
        }else {
            for (Payment payment : payments){
                paid = false;
                switch (payment.getStatus()){
                    case TARGET_CREDITED,PAID,SOURCE_DEBITED,AUTHORIZED,COMPLETE -> {
                        paid = true;
                    }
                }
                if (!paid) {
                    break;
                }
            }
        }
        return paid;
    }
    
    
    /** Backwards compatibility */
    public Fulfillment getFulfillment(){
        Fulfillments fulfillments  = getFulfillments();
        if (fulfillments == null || fulfillments.isEmpty()){
            return null;
        }else {
            return fulfillments.get(0);
        }
    }
    public void setFulfillment(Fulfillment fulfillment){
        Fulfillments fulfillments  = getFulfillments(true);
        if (fulfillment == null){
            fulfillments.clear();
            return;
        }
        
        Fulfillment existing = fulfillments.get(fulfillment.getId());
        if (existing == null){
            fulfillments.add(fulfillment);
        }else if (existing != fulfillment || existing.getInner() != fulfillment.getInner()){
            existing.update(fulfillment);
        }
    }
    
    public Location getProviderLocation(){
        Provider provider  = getProvider();
        Locations locations = provider== null ? null : provider.getLocations();
        return locations== null || locations.isEmpty() ? null : locations.get(0);
    }
    public void setProviderLocation(Location location){
        Provider provider  = getProvider(true);
        if (location == null){
            provider.setLocations(null);
            return;
        }
        Locations locations = provider.getLocations(true);
        Location existing = locations.get(location.getId());
        if (existing == null){
            locations.add(location);
        }else if (existing != location || existing.getInner() != location.getInner()){
            existing.update(location);
        }
    }
    public Map<String,Invoice> getFulfillmentInvoiceMap(){
        Map<String,Invoice> invoiceMap = new HashMap<>();
        for (Invoice invoice : getInvoices()) {
            if (invoiceMap.containsKey(invoice.getFulfillmentId())){
                throw new RuntimeException("Multiple invoices for fulfillment!");
            }
            invoiceMap.put(invoice.getFulfillmentId(),invoice);
        }
        return invoiceMap;
    }
    public Invoice getInvoice(String fulfillmentId){
        return getFulfillmentInvoiceMap().get(fulfillmentId);
    }
    
    public double getTotalProductPrice(String fulfillmentId){
        Bucket total = new Bucket();
        getItems().forEach(i->{
            if (i.getFulfillmentIds().getInner().contains(fulfillmentId)){
                total.increment(i.getPrice().getValue() * i.getQuantity().getCount());
            }
        });
        return total.doubleValue();
    }
    
    public void move(String sourceFulfillmentId , String targetFulfillmentId, String itemId, int quantity,boolean reverseTargetStops,boolean cancelTarget){
        
        Fulfillment source = getFulfillments(true).get(sourceFulfillmentId);
        Invoice sourceInvoice  = getInvoice(source.getId());

        Fulfillment target = getFulfillments(true).get(targetFulfillmentId);
        if (target == null){
            target = getObjectCreator().create(Fulfillment.class);
            target.update(source);
            target.setId(targetFulfillmentId);
            
            if (cancelTarget) {
                target.setFulfillmentStatus(FulfillmentStatus.Cancelled);
            }else {
                target.setFulfillmentStatus(FulfillmentStatus.Created);
            }
            FulfillmentStops stops = target.getFulfillmentStops();
            if (reverseTargetStops) {
                Collections.reverse(stops.getInner());
            }
            getFulfillments().add(target);
        }

        List<Item> fulfillmentItems = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        getItems().forEach(item -> {
            if (item.getFulfillmentIds().getInner().contains(sourceFulfillmentId)){
                fulfillmentItems.add(item);
                if (ObjectUtil.equals(item.getId(),itemId)) {
                    items.add(item);
                }
            }
        });
        
        for (Item item : items) {
            if (source.getFulfillmentStatus().isOpen()){
                int existingCount = item.getQuantity().getCount();
                if (existingCount < quantity){
                    throw new RuntimeException("Not as much item quantity in fulfillment");
                }else if (existingCount == quantity){
                    item.getFulfillmentIds().clear();
                    item.getFulfillmentIds().add(targetFulfillmentId);
                }else {
                    Item newItem = item.getObjectCreator().create(Item.class);
                    newItem.update(item);
                    newItem.getFulfillmentIds().clear();
                    newItem.getFulfillmentIds().add(targetFulfillmentId);
                    newItem.getQuantity().setCount(quantity);
                    item.getQuantity().setCount(existingCount - quantity);
                    getItems().add(newItem);
                }
                
                // create a cancel fulfillment
            }else if (source.getFulfillmentStatus() == FulfillmentStatus.Completed){
                int existingCount = item.getQuantity().getCount();
                if (existingCount < quantity){
                    throw new RuntimeException("Not as much item quantity in fulfillment");
                }
                
                Item newItem = item.getObjectCreator().create(Item.class);
                newItem.update(item);
                newItem.getFulfillmentIds().clear();
                newItem.getFulfillmentIds().add(targetFulfillmentId);
                newItem.getQuantity().setCount(quantity);
                getItems().add(newItem);
                
                //Create a return fulfillment
            }else {
                throw new RuntimeException("Already cancelled!");
            }
            break;
        }
        
        
        
    }
    
}
