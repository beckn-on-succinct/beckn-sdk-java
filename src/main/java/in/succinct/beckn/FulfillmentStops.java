package in.succinct.beckn;

import org.json.simple.JSONArray;

public class FulfillmentStops extends BecknObjectsWithId<FulfillmentStop>{
    public FulfillmentStops() {
    }

    public FulfillmentStops(JSONArray value) {
        super(value);
    }

    public FulfillmentStops(String payload) {
        super(payload);
    }
}
