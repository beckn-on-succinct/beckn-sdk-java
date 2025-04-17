package in.succinct.beckn;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.venky.core.util.ObjectUtil;

public class Subscriber extends BecknObject {

    public static final String SUBSCRIBER_TYPE_BAP = "BAP";
    public static final String SUBSCRIBER_TYPE_BPP = "BPP";
    public static final String SUBSCRIBER_TYPE_LOCAL_REGISTRY = "LREG";
    public static final String SUBSCRIBER_TYPE_COUNTRY_REGISTRY = "CREG";
    public static final String SUBSCRIBER_TYPE_ROOT_REGISTRY = "RREG";
    public static final String SUBSCRIBER_TYPE_BG = "BG";
    public static final String SUBSCRIBER_STATUS_SUBSCRIBED = "SUBSCRIBED";
    public static final String SUBSCRIBER_STATUS_INITIATED = "INITIATED";
    public static final String SUBSCRIBER_STATUS_UNDER_SUBSCRIPTION = "UNDER_SUBSCRIPTION";
    public static final String SUBSCRIBER_STATUS_INVALID_SSL = "INVALID_SSL";
    public static final String SUBSCRIBER_STATUS_UNSUBSCRIBED = "UNSUBSCRIBED";

    public static final String[] BPP_ACTIONS = new String[]{"search", "select", "init", "confirm", "track", "issue", "issue_status",
        "cancel", "update", "status", "rating", "support", "get_cancellation_reasons", "get_return_reasons", "get_rating_categories", "get_feedback_categories"};

    public static final Set<String> BPP_ACTION_SET = Collections.unmodifiableSet(new HashSet<>() {
        {
            for (String action : BPP_ACTIONS) {
                add(action);
            }
        }
    });

    public static final String[] BAP_ACTIONS = new String[]{"on_search", "on_select", "on_init", "on_confirm", "on_track", "on_issue", "on_issue_status",
        "on_cancel", "on_update", "on_status", "on_rating", "on_support", "cancellation_reasons", "return_reasons", "rating_categories", "feedback_categories"};

    public static final Set<String> BAP_ACTION_SET = Collections.unmodifiableSet(new HashSet<>() {
        {
            for (String action : BAP_ACTIONS) {
                add(action);
            }
        }
    });

    public Subscriber() {
    }

    public Subscriber(String payload) {
        super(payload);
    }

    public Subscriber(JSONObject object) {
        super(object);
    }

    public String getCountry() {
        return get("country");
    }

    public void setCountry(String country) {
        set("country", country);
    }

    public String getCity() {
        return get("city");
    }

    public void setCity(String city) {
        set("city", city);
    }

    public String getDomain() {
        return get("domain");
    }

    public void setDomain(String domain) {
        set("domain", domain);
    }

    public String getStatus() {
        return get("status");
    }

    public void setStatus(String status) {
        set("status", status);
    }

    public String getType() {
        return get("type");
    }

    public void setType(String type) {
        set("type", type);
    }

    public String getSubscriberId() {
        return get("subscriber_id");
    }

    public void setSubscriberId(String subscriber_id) {
        set("subscriber_id", subscriber_id);
    }

    public String getSubscriberUrl() {
        return get("subscriber_url");
    }

    public void setSubscriberUrl(String subscriber_url) {
        set("subscriber_url", subscriber_url);
    }

    public String getUniqueKeyId() {
        return get("unique_key_id");
    }

    public void setUniqueKeyId(String unique_key_id) {
        set("unique_key_id", unique_key_id);
    }

    public String getPubKeyId() {
        return getUniqueKeyId();
    }

    public void setPubKeyId(String pub_key_id) {
        setUniqueKeyId(pub_key_id);
    }

    public String getSigningPublicKey() {
        return get("signing_public_key");
    }

    public void setSigningPublicKey(String signing_public_key) {
        set("signing_public_key", signing_public_key);
    }

    public String getEncrPublicKey() {
        return get("encr_public_key");
    }

    public void setEncrPublicKey(String encr_public_key) {
        set("encr_public_key", encr_public_key);
    }

    public String getNonce() {
        return get("nonce");
    }

    public void setNonce(String nonce) {
        set("nonce", nonce);
    }

    public Date getValidFrom() {
        return getTimestamp("valid_from");
    }

    public void setValidFrom(Date valid_from) {
        set("valid_from", valid_from, TIMESTAMP_FORMAT);
    }

    public Date getValidTo() {
        return getTimestamp("valid_until");
    }

    public void setValidTo(Date valid_to) {
        set("valid_until", valid_to, TIMESTAMP_FORMAT);
    }

    public Date getCreated() {
        return getTimestamp("created");
    }

    public void setCreated(Date created) {
        set("created", created, TIMESTAMP_FORMAT);
    }

    public Date getUpdated() {
        return getTimestamp("updated");
    }

    public void setUpdated(Date updated) {
        set("updated", updated, TIMESTAMP_FORMAT);
    }

    public Date getLastInteraction() {
        return getTimestamp("last_interaction");
    }

    public void setLastInteraction(Date lastInteraction) {
        set("last_interaction", lastInteraction, TIMESTAMP_FORMAT);
    }

    public Set<String> getSupportedActions() {
        if (ObjectUtil.equals(getType(), Subscriber.SUBSCRIBER_TYPE_BAP)) {
            return BAP_ACTION_SET;
        } else {
            return BPP_ACTION_SET;
        }
    }

    public Location getLocation() {
        return get(Location.class, "location");
    }

    public void setLocation(Location location) {
        set("location", location);
    }

    public boolean isExtendedAttributesDisplayed() {
        return true;
    }

    public String getAlias() {
        return extendedAttributes.get("alias", getUniqueKeyId());
    }

    public void setAlias(String alias) {
        extendedAttributes.set("alias", alias);
    }

    public Organization getOrganization() {
        return extendedAttributes.get(Organization.class, "organization");
    }

    public void setOrganization(Organization organization) {
        extendedAttributes.set("organization", organization);
    }

    public JSONObject getInner(boolean includeExtended) {
        JSONObject inner = new JSONObject();
        inner.putAll(super.getInner());
        if (!includeExtended) {
            inner.remove("extended_attributes");
        }
        return inner;
    }

    public Request getSubscribeRequest() {
        return extendedAttributes.get(Request.class, "request");
    }

    public void setSubscribeRequest(Request request) {
        extendedAttributes.set("request", request);
    }

    public boolean isMsn() {
        return extendedAttributes.getBoolean("msn");
    }

    public void setMsn(boolean msn) {
        extendedAttributes.set("msn", msn);
    }

    public Domains getDomains() {
        return extendedAttributes.get(Domains.class, "domains");
    }

    public void setDomains(Domains domains) {
        extendedAttributes.set("domains", domains);
    }

    public static class Domains extends BecknObjects<String> {

        public Domains() {
        }

        public Domains(JSONArray value) {
            super(value);
        }

        public Domains(String payload) {
            super(payload);
        }
    }

}
