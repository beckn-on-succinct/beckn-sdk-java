package in.succinct.beckn;


import in.succinct.beckn.Fulfillment.FulfillmentStatus;
import in.succinct.beckn.Fulfillment.FulfillmentStatus.FulfillmentStatusConvertor;
import in.succinct.beckn.Payment.PaymentStatus.PaymentStatusConvertor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Payment extends BecknObjectWithId implements TagGroupHolder {
    public Payment(){
        super();
    }
    public Payment(String payload){
        super(payload);
    }
    public Payment(JSONObject payment){
        super(payment);
    }
    public String getUri(){
        return get("uri");
    }
    public void setUri(String uri){
        set("uri",uri);
    }
    public String getTlMethod(){
        return get("tl_method");
    }
    static final Set<String> TL_METHODS = new HashSet<>(){{
       add("http/get");
       add("http/post");
       add("upi");
       add("payto");
    }};
    public void setTlMethod(String tl_method){
        if  (tl_method == null || TL_METHODS.contains(tl_method)) {
            set("tl_method", tl_method);
        }else {
            throw new IllegalArgumentException();
        }
    }
    

    public enum CollectedBy {
        BAP,
        BPP
    }
    public CollectedBy getCollectedBy(){
        String s = get("collected_by");
        return s == null ? null : CollectedBy.valueOf(s);
    }
    
    public void setCollectedBy(CollectedBy collected_by){
        set("collected_by",collected_by == null ? null : collected_by.toString());
    }
    

    
    public Params getParams(){
        return get(Params.class,"params");
    }
    
    public void setParams(Params params){
        set("params",params);
    }
    
    
    public enum PaymentStatus {
        AUTHORIZED("AUTHORIZED"), //Buyer has money and is blocked by buyer's bank.
        PENDING, SOURCE_DEBITED ("PENDING"), //Buyer has approved and money is credited to the collector's bank.
        NOT_PAID, TARGET_NOT_CREDITED("NOT_PAID"), //Some failure to credit the collector's account.
        PAID, TARGET_CREDITED("PAID"), // Collector account is credited by collector's bank
        CREDIT_NOTE_ISSUED,
        CREDIT_NOTE_ACCEPTED,
        CREDIT_NOTE_REJECTED;
        
        final String lit;
        PaymentStatus(){
            this.lit = this.name();
        }
        PaymentStatus(String lit){
            this.lit = lit;
        }
        
        public String toString(){
            return lit.replace('_','-');
        }
        
        public String literal(){
            return lit;
        }
        public static class PaymentStatusConvertor extends EnumConvertor<PaymentStatus> {
            public String toString(PaymentStatus value){
                return value == null ? null : value.literal().replace('_','-');
            }
        }
    }
    
    public static class PaymentTransaction extends Params {
        
        
        public PaymentMethod getPaymentMethod(){
            return get(PaymentMethod.class, "payment_method");
        }
        public void setPaymentMethod(PaymentMethod payment_method){
            set("payment_method",payment_method);
        }
        
        public String getTransactionId(){
            return get("transaction_id");
        }
        public void setTransactionId(String transaction_id){
            set("transaction_id",transaction_id);
        }
        public Date getDate(){
            return getDate("date");
        }
        public void setDate(Date date){
            set("date",date, BecknObject.DATE_FORMAT);
        }
        public String getRemarks(){
            return get("remarks");
        }
        public void setRemarks(String remarks){
            set("remarks",remarks);
        }
        public BankAccount getSource(){
            return new BankAccount(this.getInner(),"source");
        }
        public void setSource(BankAccount source){
            new BankAccount(getInner(),"source").update(source);
        }
        public PaymentStatus getPaymentStatus(){
            return getEnum(PaymentStatus.class, "status");
        }
        public void setPaymentStatus(PaymentStatus payment_status){
            setEnum("status",payment_status);
        }
        
        public static class PaymentTransactions extends BecknObjects<PaymentTransaction> {
            public PaymentTransactions() {
            }
            
            public PaymentTransactions(JSONArray value) {
                super(value);
            }
            
            public PaymentTransactions(String payload) {
                super(payload);
            }
        }
        
    }
    
    public PaymentStatus getStatus(){
        return getEnum(PaymentStatus.class, "status",new PaymentStatusConvertor());
    }
    public void setStatus(PaymentStatus payment_status){
        setEnum("status",payment_status,new PaymentStatusConvertor());
    }
    
    
    public static class Params extends BankAccount{
        
        public String getCurrency(){
            return get("currency");
        }
        public void setCurrency(String currency){
            set("currency",currency);
        }
        
        public double getAmount(){
            return getDouble("amount");
        }
        public void setAmount(double amount){
            set("amount",amount);
        }
        // Accepted payment methods
        public BecknStrings getPaymentMethods(){
            return get(BecknStrings.class, "payment_method_ids");
        }
        public void setPaymentMethods(BecknStrings payment_method_ids){
            set("payment_method_ids",payment_method_ids);
        }
        
        
        @Override
        public boolean hasAdditionalProperties() {
            return true;
        }
    }


    public boolean isExtendedAttributesDisplayed(){
        return true;
    }
    
    
    
    
    
    // Not null
    public String getFulfillmentId(){
        return extendedAttributes.get("fulfillment_id");
    }
    public void setFulfillmentId(String fulfillment_id){
        extendedAttributes.set("fulfillment_id",fulfillment_id);
    }
    
    public Fee getBuyerFinderFee(){
        return extendedAttributes.get(Fee.class, "buyer_finder_fee");
    }
    public void setBuyerFinderFee(Fee buyer_finder_fee){
        extendedAttributes.set("buyer_finder_fee",buyer_finder_fee);
    }
    
    //Old Payment Types
    public static final String ON_ORDER = "PRE-ORDER";
    public static final String ON_FULFILLMENT = "ON-FULFILLMENT";
    public static final String PRE_FULFILLMENT = "PRE-FULFILLMENT";
    public static final String POST_FULFILLMENT = "POST-FULFILLMENT";
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public String getPaymentType(){
        return get("payment_type",POST_FULFILLMENT);
    }
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public void setPaymentType(String payment_type){
        set("payment_type",payment_type);
        switch (payment_type){
            case ON_ORDER -> {
                setInvoiceEvent(FulfillmentStatus.Created);
            }
            case PRE_FULFILLMENT -> {
                setInvoiceEvent(FulfillmentStatus.Prepared);
            }
            default -> {
                setInvoiceEvent(FulfillmentStatus.Completed);
            }
        }
    }
    
    
    //Invoice Event
    public FulfillmentStatus getInvoiceEvent(){
        FulfillmentStatus status =  extendedAttributes.getEnum(FulfillmentStatus.class,"invoice_event", new FulfillmentStatusConvertor());
        if (status == null){
            setPaymentType(getPaymentType()); // Derived from payment type for bc.
            status = extendedAttributes.getEnum(FulfillmentStatus.class,"invoice_event", new FulfillmentStatusConvertor());
        }
        return status;
    }
    
    public void setInvoiceEvent(FulfillmentStatus event){
        extendedAttributes.setEnum("invoice_event", event, new FulfillmentStatusConvertor());
    }
    
    
    /**
     * Duration from invoice event to make payment to the collector of the invoice amount
      * @return
     */
    
    public Duration getPaymentWindow(){
        String window = extendedAttributes.get("payment_window");
        return window == null ? null : Duration.parse(window);
    }
    public void setPaymentWindow(Duration window){
        extendedAttributes.set("payment_window",window == null ?null : window.toString());
    }

    
    // Duration from max(invoice_event,fulfillment_completed_event to dispute the invoice)
    // check Dispute
    public Duration getDisputeWindow(){
        String s = extendedAttributes.get("dispute_window");
        return s == null ? null: Duration.parse(s);
    }
    public void setDisputeWindow(Duration window){
        extendedAttributes.set("dispute_window",window == null ? null : window.toString());
    }
    
    
    // Duration from max(invoice_event,fulfillment_completed_event to settle all counter party payments)
    // check Dispute
    public Duration getSettlementWindow(){
        String sw = extendedAttributes.get("settlement_window");
        return sw == null ? null : Duration.parse(sw);
    }
    public void setSettlementWindow(Duration settlement_window){
        extendedAttributes.set("settlement_window",settlement_window == null ? null : settlement_window.toString());
    }
    
    
    //From the fulfillment time
    
    
    @Override
    public TagGroups getTags() {
        return TagGroupHolder.super.getTags();
    }

    @Override
    public void setTags(TagGroups tags) {
        TagGroupHolder.super.setTags(tags);
    }


}
