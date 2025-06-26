package in.succinct.events;

import in.succinct.beckn.BecknObject;
import org.json.simple.JSONObject;

import java.util.StringTokenizer;

public class PaymentStatusEvent extends BecknObject {
    /**
     * put("txn_reference", link.getTxnReference());
     *      *                 put("status", link.getStatus());
     *      *                 put("active",link.isActive());
     *      *                 put("uri", link.getLinkUri());
     */
    public PaymentStatusEvent() {
    }
    
    public PaymentStatusEvent(String payload) {
        super(payload);
    }
    
    public PaymentStatusEvent(JSONObject object) {
        super(object);
    }
    public String getTxnReference(){
        return get("txn_reference");
    }
    public void setTxnReference(String txn_reference){
        set("txn_reference",txn_reference);
    }
    
    public String getTransactionId(){
        if (getTxnReference() == null){
            return null;
        }else {
            StringTokenizer tokenizer  = new StringTokenizer(getTxnReference(),"/");
            return tokenizer.nextToken();
        }
    }
    public String getStatus(){
        return get("status");
    }
    public void setStatus(String status){
        set("status",status);
    }
    
    public boolean isActive(){
        return getBoolean("active");
    }
    public void setActive(boolean active){
        set("active",active);
    }
    
    public String getUri(){
        return get("uri");
    }
    public void setUri(String uri){
        set("uri",uri);
    }
    public double getAmountPaid(){
        return getDouble("amount_paid");
    }
    public void setAmountPaid(double amount_paid){
        set("amount_paid",amount_paid);
    }
}
