package in.succinct.beckn;

import org.json.simple.JSONObject;

public class BankAccount extends BecknObject{
    public BankAccount() {
        super();
    }
    
    public BankAccount(JSONObject object , String prefix) {
        super(object);
        this.setPrefix(prefix);
    }
    
    String prefix;
    public String getPrefix(){
        return this.prefix;
    }
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
    
    private String key(String name){
        String p = getPrefix();
        if (p == null){
            return name;
        }else {
            return String.format("%s_%s",p,name);
        }
    }
    
    
    public String getBankAccountName(){
        return get(key("bank_account_name"));
    }
    public void setBankAccountName(String bank_account_name){
        set(key("bank_account_name"),bank_account_name);
    }
    
    public String getBankAccountNumber() {
        return get(key("bank_account_number"));
    }

    public void setBankAccountNumber(String account_no) {
        set(key("bank_account_number"), account_no);
    }

    public String getBankCode() {
        return get(key("bank_code"));
    }

    public void setBankCode(String bank_code) {
        set(key("bank_code"), bank_code);
    }

    public String getBankName(){
        return get(key("bank_name"));
    }
    public void setBankName(String bank_name){
        set(key("bank_name"),bank_name);
    }
    
    public String getBranchCode(){
        return get(key("branch_code"));
    }
    public void setBranchCode(String branch_code){
        set(key("branch_code"),branch_code);
    }

    public String getBranchName(){
        return get(key("branch_name"));
    }
    public void setBranchName(String branch_name){
        set(key("branch_name"),branch_name);
    }
    

    
    public String getVirtualPaymentAddress(){
        return get(key("virtual_payment_address"));
    }
    public void setVirtualPaymentAddress(String virtual_payment_address){
        set(key("virtual_payment_address"),virtual_payment_address);
    }
    
}