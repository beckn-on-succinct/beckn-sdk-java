package in.succinct.beckn;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PaymentMethod extends BecknObjectWithId{
    public static final PaymentMethod CASH = new PaymentMethod(){{
        setId("CASH");
        setDepositedInBank(false);
        setTransactionOnline(false);
        setTransactionRemarksRequired(true);
    }};
    public static final PaymentMethod CHEQUE = new PaymentMethod(){{
        setId("CHEQUE");
        setDepositedInBank(true);
        setTransactionOnline(false);
        setTransactionRemarksRequired(true);
    }};
    public static final PaymentMethod ONLINE_TRANSFER = new PaymentMethod(){{
        setId("ONLINE_TRANSFER");
        setDepositedInBank(true);
        setTransactionOnline(true);
        setTransactionRemarksRequired(true);
    }};
    public static final PaymentMethod CREDIT_NOTE = new PaymentMethod(){{
        setId("CREDIT_NOTE");
        setDepositedInBank(false);
        setTransactionOnline(false);
        setTransactionRemarksRequired(true);
    }};
    
    public PaymentMethod() {
    }
    
    public PaymentMethod(String payload) {
        super(payload);
    }
    
    public PaymentMethod(JSONObject object) {
        super(object);
    }
    
    
    public boolean isDepositedInBank(){
        return getBoolean("deposited_in_bank");
    }
    public void setDepositedInBank(boolean deposited_in_bank){
        set("deposited_in_bank",deposited_in_bank);
    }
    
    public boolean isTransactionOnline(){
        return getBoolean("transaction_online");
    }
    public void setTransactionOnline(boolean transaction_online){
        set("transaction_online",transaction_online);
    }
    
    public boolean isPaymentTransactionIdRequired(){
        return getBoolean("payment_transaction_id_required");
    }
    public void setPaymentTransactionIdRequired(boolean payment_transaction_id_required){
        set("payment_transaction_id_required",payment_transaction_id_required);
    }
    
    
    
    public boolean isTransactionRemarksRequired(){
        return getBoolean("transaction_remarks_required");
    }
    public void setTransactionRemarksRequired(boolean transaction_remarks_required){
        set("transaction_remarks_required",transaction_remarks_required);
    }
    
    public static class PaymentMethods extends BecknObjectsWithId<PaymentMethod>{
        public PaymentMethods() {
            super(true);
        }
        
        public PaymentMethods(String payload) {
            super(payload,true);
        }
        
        public PaymentMethods(JSONArray array) {
            super(array,true);
        }
        
    }
    public static PaymentMethods WELL_KNOWN_PAYMENT_METHODS = new PaymentMethods(){{
       add(CASH);
       add(CHEQUE);
       add(ONLINE_TRANSFER);
       add(CREDIT_NOTE);
    }};
}
