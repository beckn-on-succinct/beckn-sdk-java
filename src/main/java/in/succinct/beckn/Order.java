package in.succinct.beckn;

import in.succinct.beckn.Fulfillment.RetailFulfillmentType;
import in.succinct.beckn.Order.Status.StatusConverter;
import in.succinct.beckn.ReturnReasons.ReturnRejectReason;
import org.json.simple.JSONArray;

import java.util.Date;

public class Order extends BecknObjectWithId implements TagGroupHolder{
    public Order() {
        super();
    }
    public Order(String payload){
        super(payload);
    }

    public Provider getProvider(){
        return get(Provider.class,"provider");
    }
    public void setProvider(Provider provider){
        set("provider",provider);
    }

    public Location getProviderLocation(){
        return extendedAttributes.get(Location.class,"provider_location");
    }

    public void setProviderLocation(Location location){
        extendedAttributes.set("provider_location",location);
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


    public Fulfillment getFulfillment(){
        return extendedAttributes.get(Fulfillment.class,"fulfillment");
    }
    public void setFulfillment(Fulfillment fulfillment){
        extendedAttributes.set("fulfillment",fulfillment);
    }

    public Fulfillment getPrimaryFulfillment(){
        Fulfillments fulfillments = getFulfillments();
        if (fulfillments == null || fulfillments.isEmpty()){
            return null;
        }
        for (Fulfillment f : fulfillments){
            try {
                if (f.getType() != null && (RetailFulfillmentType.valueOf(f.getType()).matches(RetailFulfillmentType.store_pickup) || RetailFulfillmentType.valueOf(f.getType()).matches(RetailFulfillmentType.home_delivery))) {
                    return f;
                }
            }catch (Exception ex){
                return f;
            }
        }
        return null;
    }
    public void setPrimaryFulfillment(Fulfillment primaryFulfillment){
        Fulfillments fulfillments = getFulfillments();
        if (fulfillments == null ){
            fulfillments = new Fulfillments();
            setFulfillments(fulfillments);
        }
        fulfillments.add(primaryFulfillment,true);
    }

    public Fulfillments getFulfillments(){
        return extendedAttributes.get(Fulfillments.class, "fulfillments");
    }
    public void setFulfillments(Fulfillments fulfillments){
        extendedAttributes.set("fulfillments",fulfillments);
    }

    public Quote getQuote(){
        return get(Quote.class,"quote");
    }
    public void setQuote(Quote quote){
        set("quote",quote);
    }

    public Payment getPayment(){
        return extendedAttributes.get(Payment.class,"payment");
    }
    public void setPayment(Payment payment){
        extendedAttributes.set("payment",payment);
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
    public void setState(Status state){
        setEnum("state", state, new StatusConverter());
    }
    public Status getState(){
        return getEnum(Status.class, "state", new StatusConverter());
    }





    public Documents getDocuments(){
        return get(Documents.class,"documents");
    }
    public void setDocuments(Documents documents){
        set("documents",documents);
    }

    public boolean isExtendedAttributesDisplayed(){
        return true;
    }

    public Cancellation getCancellation(){
        return extendedAttributes.get(Cancellation.class, "cancellation");
    }
    public void setCancellation(Cancellation cancellation){
        extendedAttributes.set("cancellation",cancellation);
    }



    public enum Status {
        Created,
        Awaiting_Acceptance,
        Accepted,
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
    // Fields added for Reconcilliation and Settlements
    public String getInvoiceNo(){
        return extendedAttributes.get("invoice_no");
    }
    public void setInvoiceNo(String invoice_no){
        extendedAttributes.set("invoice_no",invoice_no);
    }

    public Amount getIncomeTax(){
        return extendedAttributes.get(Price.class, "income_tax");
    }
    public void setIncomeTax(Amount income_tax){
        extendedAttributes.set("income_tax",income_tax);
    }

    public Amount getGst(){
        return extendedAttributes.get(Amount.class, "gst");
    }
    public void setGst(Amount gst){
        extendedAttributes.set("gst",gst);
    }

    public Amount getIncomeTaxWithheld(){
        return extendedAttributes.get(Amount.class, "income_tax_withheld");
    }
    public void setIncomeTaxWithheld(Amount income_tax_withheld){
        extendedAttributes.set("income_tax_withheld",income_tax_withheld);
    }

    public Amount getGstWithheld(){
        return extendedAttributes.get(Amount.class, "gst_withheld");
    }
    public void setGstWithheld(Amount gst_withheld){
        extendedAttributes.set("gst_withheld",gst_withheld);
    }

    public OrderReconStatus getOrderReconStatus(){
        return extendedAttributes.getEnum(OrderReconStatus.class, "order_recon_status");
    }
    public void setOrderReconStatus(OrderReconStatus order_recon_status){
        extendedAttributes.setEnum("order_recon_status",order_recon_status);
    }

    public SettlementCorrection getSettlementCorrection(){
        return extendedAttributes.get(SettlementCorrection.class, "settlement_correction");
    }
    public void setSettlementCorrection(SettlementCorrection settlement_correction){
        extendedAttributes.set("settlement_correction",settlement_correction);
    }


    public String getCollectorSubscriberId(){
        return extendedAttributes.get("collector_subscriber_id");
    }
    public void setCollectorSubscriberId(String collector_subscriber_id){
        extendedAttributes.set("collector_subscriber_id",collector_subscriber_id);
    }

    public String getReceiverSubscriberId(){
        return extendedAttributes.get("receiver_subscriber_id");
    }
    public void setReceiverSubscriberId(String receiver_subscriber_id){
        extendedAttributes.set("receiver_subscriber_id",receiver_subscriber_id);
    }


    public Payer getPayer(){
        return extendedAttributes.get(Payer.class, "payer");
    }
    public void setPayer(Payer payer){
        extendedAttributes.set("payer",payer);
    }


    public enum SettlementReasonCode {
        ORDER_PAYMENT,
        PART_REFUND,
        REFUND,
        CORRECTION,
        BUYER_APP_FEE_PAYMENT,
        BUYER_APP_FEE_GST_PAYMENT;

        public static EnumConvertor<SettlementReasonCode> convertor = new OrdinalBasedEnumConvertor<>(SettlementReasonCode.class);
    }

    public SettlementReasonCode getSettlementReasonCode(){
        return extendedAttributes.getEnum(SettlementReasonCode.class, "settlement_reason_code",SettlementReasonCode.convertor);
    }
    public void setSettlementReasonCode(SettlementReasonCode settlement_reason_code){
        extendedAttributes.setEnum("settlement_reason_code",settlement_reason_code,SettlementReasonCode.convertor);
    }

    public String getCollectionTransactionId(){
        return extendedAttributes.get("collection_transaction_id");
    }
    public void setCollectionTransactionId(String transaction_id){
        extendedAttributes.set("collection_transaction_id",transaction_id);
    }

    public String getSettlementId(){
        return extendedAttributes.get("settlement_id");
    }
    public void setSettlementId(String settlement_id){
        extendedAttributes.set("settlement_id",settlement_id);
    }
    
    public String getSettlementReference(){
        return extendedAttributes.get("settlement_reference");
    }
    public void setSettlementReference(String settlement_reference){
        extendedAttributes.set("settlement_reference",settlement_reference);
    }


    public enum ReconStatus {
        PAID,
        OVER_PAID,
        UNDER_PAID,
        NOT_PAID;

        public static EnumConvertor<ReconStatus> convertor = new OrdinalBasedEnumConvertor<>(ReconStatus .class);

    }

    public ReconStatus getReconStatus(){
        return extendedAttributes.getEnum(ReconStatus.class, "recon_status" , ReconStatus.convertor);
    }
    public void setReconStatus(ReconStatus recon_status){
        extendedAttributes.setEnum("recon_status",recon_status, ReconStatus.convertor);
    }

    public Double getDiffAmount(){
        return extendedAttributes.getDouble("diff_amount", null);
    }
    public void setDiffAmount(Double diff_amount){
        extendedAttributes.set("diff_amount",diff_amount);
    }


    public ReconStatus getCounterpartyReconStatus(){
        return extendedAttributes.getEnum(ReconStatus.class, "counterparty_recon_status");
    }
    public void setCounterpartyReconStatus(ReconStatus counterparty_recon_status){
        extendedAttributes.setEnum("counterparty_recon_status",counterparty_recon_status);
    }



    public Amount getCounterpartyDiffAmount(){
        return get(Amount.class, "counterparty_diff_amount");
    }
    public void setCounterpartyDiffAmount(Amount counterparty_diff_amount){
        set("counterparty_diff_amount",counterparty_diff_amount);
    }

    public Descriptor getReconMessage(){
        return extendedAttributes.get(Descriptor.class, "message");
    }
    public void setReconMessage(Descriptor recon_message){
        extendedAttributes.set("message",recon_message);
    }


    public enum OrderReconStatus {
        PROVISIONAL,
        FINALE,
        DEEMED_SETTLED;

        public static EnumConvertor<OrderReconStatus> convertor = new OrdinalBasedEnumConvertor<>(OrderReconStatus.class);

    }





    public Returns getReturns(){
        return extendedAttributes.get(Returns.class, "returns");
    }
    public void setReturns(Returns r){
        extendedAttributes.set("returns",r);
    }

    public Refunds getRefunds(){
        return extendedAttributes.get(Refunds.class, "refunds");
    }
    public void setRefunds(Refunds refunds){
        extendedAttributes.set("refunds",refunds);
    }

    public static class Returns extends BecknObjectsWithId<Return>{
        public Returns() {
            super();
        }

        public Returns(JSONArray array) {
            super(array);
        }

        public Returns(String payload) {
            super(payload);
        }
    }

    public static class Return extends BecknObjectWithId{
        public Return() {
        }


        public boolean isOpen(){
            ReturnStatus returnStatus = getReturnStatus();
            return returnStatus != null && returnStatus.compareTo(ReturnStatus.OPEN) <= 0;
        }

        public boolean isRefunded(){
            ReturnStatus returnStatus = getReturnStatus();
            return returnStatus  == ReturnStatus.REFUNDED || getRefundId() != null;
        }

        public boolean isClosed(){
            return !isOpen();
        }

        public String getFulfillmentId(){
            return get("fulfillment_id");
        }
        public void setFulfillmentId(String fulfillment_id){
            set("fulfillment_id",fulfillment_id);
        }

        public String getReturnMessageId(){
            return get("return_message_id");
        }
        public void setReturnMessageId(String return_message_id){
            set("return_message_id",return_message_id);
        }
        public Return(String payload) {
            super(payload);
        }

        public ReturnStatus getReturnStatus(){
            return getEnum(ReturnStatus.class, "return_status");
        }
        public void setReturnStatus(ReturnStatus return_status){
            setEnum("return_status",return_status);
        }

        public ReturnRejectReason getReturnRejectReason(){
            return getEnum(ReturnRejectReason.class, "return_reject_reason");
        }
        public void setReturnRejectReason(ReturnRejectReason return_reject_reason){
            setEnum("return_reject_reason",return_reject_reason);
        }


        public Amount getRefund(){
            return get(Amount.class, "refund");
        }
        public void setRefund(Amount refund){
            set("refund",refund);
        }

        public NonUniqueItems getItems(){
            return get(NonUniqueItems.class, "items");
        }
        public void setItems(NonUniqueItems items){
            set("items",items);
        }

        public Date getCreatedAt(){
            return getTimestamp("created_at");
        }
        public void setCreatedAt(Date created_at){
            set("created_at",created_at,TIMESTAMP_FORMAT);
        }

        public boolean isLiquidated(){
            return getBoolean("liquidated");
        }
        public void setLiquidated(boolean liquidated){
            set("liquidated",liquidated);
        }

        public enum ReturnStatus {
            REQUESTED,
            OPEN,
            DECLINED,
            REFUNDED,
            CLOSED,
            CANCELED;

            public static EnumConvertor<ReturnStatus> convertor = new EnumConvertor<>(ReturnStatus.class);
        }

        public String getRefundId(){
            return get("refund_id");
        }
        public void setRefundId(String refund_id){
            set("refund_id",refund_id);
        }
    }

    public static class Refunds extends BecknObjectsWithId<Refund> {
        public Refunds() {
        }

        public Refunds(String payload) {
            super(payload);
        }

        public Refunds(JSONArray array) {
            super(array);
        }
    }

    public static class Refund extends BecknObjectWithId{

        public Date getCreatedAt(){
            return getTimestamp("created_at");
        }
        public void setCreatedAt(Date created_at){
            set("created_at",created_at,TIMESTAMP_FORMAT);
        }

        public String getFulfillmentId(){
            return get("fulfillment_id");
        }
        public void setFulfillmentId(String fulfillment_id){
            set("fulfillment_id",fulfillment_id);
        }

        public NonUniqueItems getItems(){
            return get(NonUniqueItems.class, "items");
        }
        public void setItems(NonUniqueItems items){
            set("items",items);
        }

    }

    
    public String getBppTaxNumber(){
        return extendedAttributes.get("bpp_tax_number");
    }
    public void setBppTaxNumber(String bpp_tax_number){
        extendedAttributes.set("bpp_tax_number",bpp_tax_number);
    }
    public String getBapTaxNumber(){
        return extendedAttributes.get("bap_tax_number");
    }
    public void setBapTaxNumber(String bap_tax_number){
        extendedAttributes.set("bap_tax_number",bap_tax_number);
    }

    public BecknStrings getRefOrderIds(){
        return get(BecknStrings.class,"ref_order_ids",true);
    }
    public void setRefOrderIds(BecknStrings ref_order_ids){
        set("ref_order_ids",ref_order_ids);
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
