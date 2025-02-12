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



    public String getSigningString(long created_at, long expires_at) {
        StringBuilder builder = new StringBuilder();
        builder.append("(created): ").append(created_at);
        builder.append("\n(expires): ").append(expires_at);
        builder.append("\n").append("digest: BLAKE-512=").append(hash());
        System.out.println( "---------Signing String:\n" +builder + "\n------------------");
        return builder.toString();
    }

    public boolean verifySignature(String header,Map<String,String> httpRequestHeaders){
        return verifySignature(header,httpRequestHeaders,true);
    }
    public boolean verifySignature(Map<String,String> params, boolean headerMandatory){
        if (params.isEmpty()) {
            return !headerMandatory;
        }

        String signature = params.get("signature");
        String created = params.get("created");
        String expires = params.get("expires");
        String keyId = params.get("keyId");
        String subscriberId = params.get("subscriber_id");
        String pub_key_id  = params.get("pub_key_id");


        String signingString = getSigningString(Long.parseLong(created),Long.parseLong(expires));
        return verifySignature(signature,signingString,getPublicKey(subscriberId,pub_key_id));
    }
    public boolean verifySignature(String header,Map<String,String> httpRequestHeaders, boolean headerMandatory){
        Map<String,String> params = extractAuthorizationParams(header,httpRequestHeaders);
        return verifySignature(params,headerMandatory);
    }

    public static String getPublicKey(String subscriber_id, String keyId ) {
        ObjectHolder<String> publicKeyHolder = new ObjectHolder<>(null);
        Registry.instance().callExtensions("beckn.public.key.get",subscriber_id,keyId,publicKeyHolder);
        return publicKeyHolder.get();
    }
    public static String getPrivateKey(String subscriber_id, String keyId) {
        ObjectHolder<String> privateKeyHolder = new ObjectHolder<>(null);
        Registry.instance().callExtensions("beckn.private.key.get",subscriber_id,keyId,privateKeyHolder);
        return privateKeyHolder.get();
    }

    public String generateAuthorizationHeader(String  subscriberId, String pub_key_id){
        Map<String,String> map = generateAuthorizationParams(subscriberId,pub_key_id);
        StringBuilder auth = new StringBuilder();
        map.forEach((k,v)-> {
            if (auth.length() == 0){
                auth.append("Signature ");
            }else {
                auth.append(",");
            }
            auth.append(k).append("=\"").append(v).append("\"");
        });
        return auth.toString();
    }

    public Map<String, String> extractAuthorizationParams(String header, Map<String, String> httpRequestHeaders) {
        Map<String,String> params = new IgnoreCaseMap<>();
        if (!httpRequestHeaders.containsKey(header)){
            return params;
        }
        String authorization = httpRequestHeaders.get(header).trim();
        String signatureToken  = "Signature ";

        if (authorization.startsWith(signatureToken)){
            authorization = authorization.substring(signatureToken.length());
        }

        Matcher matcher = Pattern.compile("([A-z]+)(=)[\"]*([^\",]*)[\"]*[, ]*").matcher(authorization);
        matcher.results().forEach(mr->{
            params.put(mr.group(1),mr.group(3));
        });

        if (!params.isEmpty()) {
            String keyId = params.get("keyId");
            if (!ObjectUtil.isVoid(keyId)){
                StringTokenizer keyTokenizer = new StringTokenizer(keyId, "|");
                String subscriberId = keyTokenizer.nextToken();
                String pub_key_id = keyTokenizer.nextToken();
                params.put("subscriber_id",subscriberId);
                params.put("pub_key_id",pub_key_id);
            }
        }

        return params;
    }
    public Map<String,String> generateAuthorizationParams(String subscriberId,String pub_key_id){
        Map<String,String> map = new SequenceMap<>();
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(subscriberId).append('|')
                .append(pub_key_id).append('|').append("ed25519");

        map.put("keyId",keyBuilder.toString());
        map.put("algorithm","ed25519");
        long created_at = System.currentTimeMillis()/1000L;
        long expires_at = created_at + (getContext() == null ? 10 : getContext().getTtl())*1000L;
        map.put("created",Long.toString(created_at));
        map.put("expires",Long.toString(expires_at));
        map.put("headers","(created) (expires) digest");
        //map.put("signature",generateSignature(generateBlakeHash(getSigningString(created_at,expires_at)),getPrivateKey(subscriberId,pub_key_id)));
        map.put("signature",generateSignature(getSigningString(created_at,expires_at),getPrivateKey(subscriberId,pub_key_id)));
        return map;
    }


    public static String SIGNATURE_ALGO = "Ed25519";
    public static int SIGNATURE_ALGO_KEY_LENGTH = 256;

    public static String ENCRYPTION_ALGO = "X25519";
    public static int ENCRYPTION_ALGO_KEY_LENGTH = 256;

    public static String generateSignature(String req, String privateKey) {

        PrivateKey key = Crypt.getInstance().getPrivateKey(SIGNATURE_ALGO,privateKey);
        String signature =  Crypt.getInstance().generateSignature(req,SIGNATURE_ALGO,key);
        System.out.println(String.format("--\nSignature : %s\nPayload: %s\n",signature,req));
        return signature;
    }


    public static boolean verifySignature(String sign, String requestData, String b64PublicKey) {
        System.out.println(String.format("--\nSignature : %s\nPayload: %s\n Public key: %s\n--",sign,requestData,b64PublicKey));
        PublicKey key = getSigningPublicKey(b64PublicKey);
        return Crypt.getInstance().verifySignature(requestData,sign,SIGNATURE_ALGO,key);
    }

    public static String getSubscriberId(Map<String,String> authParams){
        if (!authParams.isEmpty()){
            String keyId = authParams.get("keyId");
            StringTokenizer keyTokenizer = new StringTokenizer(keyId,"|");
            String subscriberId = keyTokenizer.nextToken();
            return subscriberId;
        }
        return null;
    }
    
    public static PublicKey getSigningPublicKey(String keyFromRegistry){
        return Crypt.getInstance().getPublicKey(Request.SIGNATURE_ALGO, keyFromRegistry);
    }
    public static PublicKey getEncryptionPublicKey(String keyFromRegistry){
        return Crypt.getInstance().getPublicKey(Request.ENCRYPTION_ALGO, keyFromRegistry);
    }

    public static String getRawSigningKey(PublicKey publicKey){
        return Crypt.getInstance().getBase64Encoded(publicKey,false);
    }
    public static String getRawEncryptionKey(PublicKey publicKey){
        return Crypt.getInstance().getBase64Encoded(publicKey,false);
    }

    public static String getPemSigningKey(String keyFromRegistry){
        return Base64.getEncoder().encodeToString(getSigningPublicKey(keyFromRegistry).getEncoded());
    }
    public static String getPemEncryptionKey(String keyFromRegistry){
        return Base64.getEncoder().encodeToString(getEncryptionPublicKey(keyFromRegistry).getEncoded());
    }
    public static String getRawSigningKey(String keyFromRegistry){
        return getRawSigningKey(getSigningPublicKey(keyFromRegistry));
    }
    public static String getRawEncryptionKey(String keyFromRegistry){
        return getRawEncryptionKey(getEncryptionPublicKey(keyFromRegistry));
    }

    public static final String CALLBACK_URL = "callback_url";

    private boolean suppressed = false;
    public boolean isSuppressed() {
        return suppressed;
    }
    public void setSuppressed(boolean suppressed){
        this.suppressed = suppressed;
    }
}
