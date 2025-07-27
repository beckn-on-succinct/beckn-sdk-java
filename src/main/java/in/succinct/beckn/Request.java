package in.succinct.beckn;

import com.venky.core.collections.IgnoreCaseMap;
import com.venky.core.collections.SequenceMap;
import com.venky.core.security.Crypt;
import com.venky.core.util.ObjectHolder;
import com.venky.core.util.ObjectUtil;
import com.venky.extension.Registry;
import org.json.simple.JSONObject;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request extends BecknObject {
    public Request() {
        this(new JSONObject());
    }
    public Request(String payLoad){
        super(payLoad);
    }
    public Request(JSONObject request){
        super(request);
    }

    public Context getContext() {
        return get(Context.class, "context");
    }
    public void setContext(Context context) {
        set("context",context);
    }


    public Message getMessage() {
        return get(Message.class, "message");
    }
    public void setMessage(Message message) {
        set("message",message);
    }

    public Error getError(){
        return get(Error.class,"error");
    }
    public void setError(Error error){
        set("error",error);
    }

    public RatingCategories getRatingCategories(){
        return get(RatingCategories.class, "rating_categories");
    }
    public void setRatingCategories(RatingCategories rating_categories){
        set("rating_categories",rating_categories);
    }





    public CancellationReasons getCancellationReasons(){
        return get(CancellationReasons.class, "cancellation_reasons");
    }
    public void setCancellationReasons(CancellationReasons cancellation_reasons){
        set("cancellation_reasons",cancellation_reasons);
    }

    public ReturnReasons getReturnReasons(){
        return get(ReturnReasons.class, "return_reasons");
    }
    public void setReturnReasons(ReturnReasons return_reasons){
        set("return_reasons",return_reasons);
    }








    public static final String CALLBACK_URL = "callback_url";

    private boolean suppressed = false;
    public boolean isSuppressed() {
        return suppressed;
    }
    public void setSuppressed(boolean suppressed){
        this.suppressed = suppressed;
    }
    
    @Override
    public Long getTtl(){
        return getContext() == null ? super.getTtl() : getContext().getTtl();
    }
    @Override
    public void setTtl(Long ttl){
        if (getContext() != null){
            getContext().setTtl(ttl);
        }
    }
}
