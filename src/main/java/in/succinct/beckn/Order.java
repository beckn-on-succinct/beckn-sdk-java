package in.succinct.beckn;

import com.venky.cache.Cache;
import com.venky.core.util.Bucket;
import in.succinct.beckn.CancellationTerm.CancellationTerms;
import in.succinct.beckn.Conversation.Conversations;
import in.succinct.beckn.Fulfillment.FulfillmentStatus;
import in.succinct.beckn.Invoice.Dispute;
import in.succinct.beckn.Invoice.Dispute.Credit;
import in.succinct.beckn.Invoice.Dispute.Status;
import in.succinct.beckn.Invoice.Invoices;
import in.succinct.beckn.Note.Notes;
import in.succinct.beckn.Order.Status.StatusConverter;
import in.succinct.beckn.Payment.PaymentStatus;
import in.succinct.beckn.Payment.PaymentTransaction;
import in.succinct.beckn.RefundTerm.RefundTerms;
import in.succinct.beckn.ReplacementTerm.ReplacementTerms;
import in.succinct.beckn.ReturnTerm.ReturnTerms;
import org.json.simple.JSONArray;

import java.util.Date;
import java.util.HashMap;
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
        return get(Provider.class,"provider");
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
        return get(Fulfillments.class, "fulfillments");
    }
    public void setFulfillments(Fulfillments fulfillments){
        set("fulfillments",fulfillments);
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
        return get(Payments.class, "payments");
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
        return extendedAttributes.get(Invoices.class, "invoices");
    }
    public void setInvoices(Invoices invoices){
        extendedAttributes.set("invoices",invoices);
    }
    
    public boolean isPaid(){
        Fulfillments fulfillments = getFulfillments();
        Invoices invoices = getInvoices();
        boolean paid = false;

        Payments payments  = getPayments(); //These are the payment terms
        if (invoices != null){
            Map<String,Invoice> invoiceMap = new HashMap<>();
            invoices.forEach((i)->invoiceMap.put(i.getFulfillmentId(),i)); //Only one invoice per fulfillment Id
            
            for (Payment term : payments){
                paid = false;
                Fulfillment fulfillment  = fulfillments.get(term.getFulfillmentId());
                Invoice invoice = invoiceMap.get(term.getFulfillmentId());
                if (invoice == null && fulfillment.getFulfillmentStatus().isOpen()){
                    //Invoice is not raised. !!
                    break;
                }else if (invoice != null ){
                    Bucket unpaidAmount = invoice.getUnpaidAmount();
                    if (unpaidAmount.intValue()  == 0 ){
                        paid = true;
                    }else {
                        break;
                    }
                }
            }
        }else {
            for (Payment payment : payments){
                paid = false;
                switch (payment.getStatus()){
                    case TARGET_CREDITED,PAID -> {
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
    
    
}
