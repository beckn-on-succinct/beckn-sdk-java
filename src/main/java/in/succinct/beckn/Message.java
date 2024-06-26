package in.succinct.beckn;

import in.succinct.beckn.CancellationReasons.CancellationReasonCode;
import in.succinct.beckn.Order.Orders;
import in.succinct.beckn.Rating.Ratings;

public class Message extends BecknObject {
    public Message() {
        super();
    }
    public Message(String payload){
        super(payload);
    }
    public Intent getIntent(){
        return get(Intent.class,"intent");
    }
    public void setIntent(Intent intent){
        set("intent",intent);
    }
    public Catalog getCatalog(){
        return get(Catalog.class,"catalog");
    }

   public CancellationReasonCode getCancellationReasonCode(){
       return getEnum(CancellationReasonCode.class, "cancellation_reason_id",CancellationReasonCode.convertor);
   }
   public void setCancellationReasonCode(CancellationReasonCode cancellation_reason_code){
       setEnum("cancellation_reason_id",cancellation_reason_code,CancellationReasonCode.convertor);
   }

    public String getUpdateTarget(){
        return get("update_target");
    }
    public void setUpdateTarget(String update_target){
        set("update_target",update_target);
    }


    public void setCatalog(Catalog catalog){
        set("catalog",catalog);
    }

    public Order getOrder(){
        return get(Order.class,"order");
    }

    public void setOrder(Order order){
        set("order",order);
    }


    public Orders getOrders(){
        return getExtendedAttributes().get(Orders.class, "orders");
    }
    public void setOrders(Orders orders){
        getExtendedAttributes().set("orders",orders);
    }

    public Order getInitialized(){
        return getOrder();
    }
    public void setInitialized(Order initialized){
        setOrder(initialized);
    }

    public Items getItems(){
        return get(Items.class,"items");
    }

    public void setQuote(Quote quote) {
        set("quote",quote);
    }
    public Quote getQuote(){
        return get(Quote.class,"quote");
    }

    public Order getSelected(){
        return getOrder();
    }
    public void setSelected(Order selected){
        setOrder(selected);
    }

    public Acknowledgement getAcknowledgement(){
        return get(Acknowledgement.class,"ack");
    }

    public Rating getRating(){
        return get(Rating.class, "rating");
    }
    public void setRating(Rating rating){
        set("rating",rating);
    }

    public Ratings getRatings(){
        return get(Ratings.class, "ratings");
    }
    public void setRatings(Ratings ratings){
        set("ratings",ratings);
    }



    
    public String getPhone(){
        return get("phone");
    }
    public void setPhone(String phone){
        set("phone",phone);
    }
    
    public String getEmail(){
        return get("email");
    }
    public void setEmail(String email){
        set("email",email);
    }

    public String getUrl(){
        return get("url");
    }
    public void setUrl(String url){
        set("url",url);
    }

    public Tracking getTracking(){
        return get(Tracking.class, "tracking");
    }
    public void setTracking(Tracking tracking){
        set("tracking",tracking);
    }

    public String getOrderId(){
        return get("order_id");
    }
    public void setOrderId(String order_id){
        set("order_id",order_id);
    }

    public Issue getIssue(){
        return get(Issue.class, "issue");
    }
    public void setIssue(Issue issue){
        set("issue",issue);
    }

    public Xinput getFeedbackForm(){
        return get(Xinput.class, "feedback_form");
    }
    public void setFeedbackForm(Xinput feedback_form){
        set("feedback_form",feedback_form);
    }

}
