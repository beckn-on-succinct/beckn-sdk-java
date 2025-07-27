package in.succinct.beckn;

import com.sun.net.httpserver.Headers;
import com.venky.core.collections.IgnoreCaseMap;
import org.json.simple.JSONObject;

import java.util.Map;

public class VisibilityEvent extends BecknObject{
    public VisibilityEvent() {
    }
    
    public VisibilityEvent(String payload) {
        super(payload);
    }
    
    public VisibilityEvent(JSONObject object) {
        super(object);
    }
    
    public Request getRequest(){
        String payload = get("request");
        Request request = getObjectCreator().create(Request.class);
        request.setPayload(payload);
        return request;
    }
    public void setRequest(Request request){
        set("request",request.toString());
    }
    
    public Headers getHeaders(){
        return get(Headers.class, "headers");
    }
    public void setHeaders(Headers headers){
        set("headers",headers);
    }
    
    public static class Headers extends BecknObject{
        public Headers() {
        }
        
        public Headers(String payload) {
            super(payload);
        }
        
        public Headers(JSONObject object) {
            super(object);
        }
        public Headers(Map<String,String> object){
            set(object);
        }
        
        public void set(Map<String,String> object) {
            object.forEach((k,v)->{
                set(k,v);
            });
        }
        public Map<String,String> get(){
            Map<String,String> map = new IgnoreCaseMap<>();
            getInner().forEach((k,v)->{
                map.put((String)k,(String)v);
            });
            return map;
        }
        
        
        @Override
        public boolean hasAdditionalProperties(){
            return true;
        }
        
    }
}
