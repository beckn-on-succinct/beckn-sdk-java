package in.succinct.beckn;

import org.json.simple.JSONArray;

public class Requests extends BecknObjects<Request>{
    public Requests() {
    }
    
    public Requests(JSONArray value) {
        super(value);
    }
    
    public Requests(String payload) {
        super(payload);
    }
}
