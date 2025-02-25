package in.succinct.beckn;


import com.venky.core.util.Bucket;
import in.succinct.beckn.Invoice.Dispute.Credit;
import in.succinct.beckn.Invoice.Dispute.Credit.Credits;
import in.succinct.beckn.Invoice.Dispute.Disputes;
import in.succinct.beckn.Payment.PaymentTransaction;
import in.succinct.beckn.Payment.PaymentTransaction.PaymentTransactions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Date;

public class Invoice extends BecknObjectWithId{
    public Invoice() {
    }
    
    public Invoice(String payload) {
        super(payload);
    }
    
    public Invoice(JSONObject object) {
        super(object);
    }
    
    public String getFulfillmentId(){
        return get("fulfillment_id");
    }
    public void setFulfillmentId(String fulfillment_id){
        set("fulfillment_id",fulfillment_id);
    }
    
    
    public Date getDate(){
        return getDate("date");
    }
    public void setDate(Date date){
        set("date",date, BecknObject.DATE_FORMAT);
    }
    
    public int getAmount(){
        return getInteger("amount");
    }
    public void setAmount(int amount){
        set("amount",amount);
    }
    
    public BreakUp getBreakUp(){
        return get(BreakUp.class, "break_up");
    }
    public void setBreakUp(BreakUp break_up){
        set("break_up",break_up);
    }
    
    public Disputes getDisputes(){
        return get(Disputes.class, "disputes");
    }
    public void setDisputes(Disputes disputes){
        set("disputes",disputes);
    }
    
    public PaymentTransactions getPaymentTransactions(){
        return get(PaymentTransactions.class, "payment_transactions");
    }
    public void setPaymentTransactions(PaymentTransactions payment_transactions){
        set("payment_transactions",payment_transactions);
    }
    
    public static class Dispute extends BecknObjectWithId {
        public Dispute() {
        }
        
        public Dispute(String payload) {
            super(payload);
        }
        
        public Dispute(JSONObject object) {
            super(object);
        }
        
        
        
        public String getReason(){
            return get("reason");
        }
        public void setReason(String reason){
            set("reason",reason);
        }
        
        public Status getStatus(){
            return getEnum(Status.class, "status");
        }
        public void setStatus(Status status){
            setEnum("status",status);
        }
        
        public enum Status {
            Open,
            Closed,
            PartialAmountAuthorized,
            AmountAuthorized, // Is used when payment is to be made after dispute resolution.
        }
        
        
        public double getDisputeAmount(){
            return getDouble("dispute_amount");
        }
        public void setDisputeAmount(double dispute_amount){
            set("dispute_amount",dispute_amount);
        }
        
        public double getAuthorizedAmount(){
            return getDouble("authorized_amount");
        }
        public void setAuthorizedAmount(double authorized_amount){
            set("authorized_amount",authorized_amount);
        }
        
        
        public Credits getCredits(){
            return get(Credits.class, "credits");
        }
        public void setCredits(Credits credits){
            set("credits",credits);
        }
        
        
        public static class Credit extends Payment.PaymentTransaction {
            
            public static class Credits extends BecknObjects<Credit> {
                public Credits() {
                }
                
                public Credits(JSONArray value) {
                    super(value);
                }
                
                public Credits(String payload) {
                    super(payload);
                }
            }
        }

        public static class Disputes extends BecknObjects<Dispute> {
            public Disputes() {
            }
            
            public Disputes(JSONArray value) {
                super(value);
            }
            
            public Disputes(String payload) {
                super(payload);
            }
        }
        
    }
    
    
    public static class Invoices extends BecknObjectsWithId<Invoice> {
        public Invoices() {
        }
        
        public Invoices(JSONArray value) {
            super(value);
        }
        
        public Invoices(String payload) {
            super(payload);
        }
    }
    
    public Bucket getUnpaidAmount() {
        Invoice invoice = this;
        Bucket unpaidAmount = new Bucket(invoice.getAmount());
        for (PaymentTransaction paymentTransaction : invoice.getPaymentTransactions()){
            switch (paymentTransaction.getPaymentStatus()){
                case PAID,TARGET_CREDITED-> {
                    unpaidAmount.decrement(paymentTransaction.getAmount());
                }
            }
        }
        
        Bucket openDisputeAmount = new Bucket();
        for (Dispute dispute : invoice.getDisputes()){
            switch (dispute.getStatus()){
                case PartialAmountAuthorized,AmountAuthorized,Closed -> {
                    Bucket authAmount = new Bucket(dispute.getAuthorizedAmount());
                    for (Credit credit : dispute.getCredits()){
                        switch (credit.getPaymentStatus()){
                            case TARGET_CREDITED, PAID, CREDIT_NOTE_ACCEPTED, CREDIT_NOTE_ISSUED , PENDING, SOURCE_DEBITED->{
                                authAmount.decrement(credit.getAmount());
                            }
                        }
                    }
                    openDisputeAmount.increment(authAmount.intValue());
                }
                case Open -> {
                    openDisputeAmount.increment(dispute.getDisputeAmount());
                }
            }
        }
        if (unpaidAmount.intValue() == 0){
            return openDisputeAmount;
        }else {
            return unpaidAmount;
        }
    }
}
