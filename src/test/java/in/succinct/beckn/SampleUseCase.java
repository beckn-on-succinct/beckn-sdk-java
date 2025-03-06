package in.succinct.beckn;

import com.venky.core.security.Crypt;
import com.venky.core.string.StringUtil;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.internal.asn1.edec.EdECObjectIdentifiers;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPublicKey;
import org.bouncycastle.jcajce.spec.OpenSSHPublicKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class SampleUseCase {
    @BeforeClass
    public static void setup(){
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null){
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Test
    public void timestampFormats(){
        for (DateFormat t : BecknObject.TIMESTAMP_FORMATS){
            System.out.println(((SimpleDateFormat)t).toPattern()+ "," + t.format(new Date()));
        }
    }

    @Test
    public void testTibil() throws Exception{
        Request request = new Request("{\"context\": {\"domain\": \"dsep:jobs\", \"location\": {\"city\": {\"name\": \"BLR\"}, \"country\": {\"name\": \"IND\"}}, \"action\": \"search\", \"version\": \"1.1.0\", \"bap_id\": \"tibilsolutions.com\", \"bap_uri\": \"https://beckn.tibilprojects.com/bap/\", \"transaction_id\": \"38fc772f-df93-44e8-befb-bdf33d9ac9dd\", \"message_id\": \"28653c56-6886-4954-bd7e-93fbd372be06\"}, \"message\": {\"intent\": {\"provider\": {\"locations\": [{\"city\": \"Bangalore\"}]}, \"category\": {\"descriptor\": {\"code\": \"Programming\"}}, \"item\": {\"descriptor\": {\"name\": \"Python developer\"}, \"price\": {\"minimum_value\": \"10000\"}}}}}");
        Map<String,String> headers = new HashMap<>();
        headers.put("AUTHORIZATION","Signature keyId=\"tibilsolutions.com.jobs.BAP|tibilsolutions.com|ed25519\", algorithm=\"ed25519\", created=\"1695731541\", expires=\"1695733341\", headers=\"(created) (expires) digest\", signature=\"9oKShGnJgA6EEq7TNIiK/kPmNj+9b7iy37TxIPTCNpnXwJV6FVSODkgfxOn6XMCiSzKicgQ4CU53+HcMyVbVAQ==\"");
        Map<String,String> params = request.extractAuthorizationParams("AUTHORIZATION",headers);
        Assert.assertTrue(Request.verifySignature(params.get("signature"),request.toString(),"7YuymosnCNlxvAh7D+as9X/R/BNU1XZz2+/XBsSthV0="));
    }

    @Test
    public void testBikry()throws  Exception{
        String payload = StringUtil.read(getClass().getResourceAsStream("/message.txt"));
        payload=payload.trim();
        Request request = new Request(payload);
        String privateKey = "MFECAQEwBQYDK2VwBCIEIGFb5nbGack1kjlYtbWel9LyxmXti1HGh9Oq6tFoAMZzgSEA1NOJ12n2O9pNTMjxQEOBy/Aqa4z8mfEHMpWQgV15gE4=";

        String signingString = request.getSigningString(1659888942,1659888952);
        System.out.println(signingString);
        PrivateKey privateKey1 = Crypt.getInstance().getPrivateKey(Request.SIGNATURE_ALGO,privateKey);

        //PublicKey key = Request.getSigningPublicKey("MCowBQYDK2VwAyEA1NOJ12n2O9pNTMjxQEOBy/Aqa4z8mfEHMpWQgV15gE4=");
        PublicKey key = Request.getSigningPublicKey("1NOJ12n2O9pNTMjxQEOBy/Aqa4z8mfEHMpWQgV15gE4=");

        String signature = "Nv8WY23Yv8tfSNBIVes3TMQeLW27CLIMvkEQrje9DfbR3P5B7SwTLWjr+LQfsWk0MuCAKF/EaLTp2B6SDW+vBw==";

        Assert.assertTrue(Crypt.getInstance().verifySignature(signingString,signature,Request.SIGNATURE_ALGO,key));
    }

    @Test
    public void testDateFormat() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = format.parse("2021-10-30T18:09:12.506+05:30");
        System.out.println(format.format(date));

    }

    @Test
    public void testVerifyFile(){
        String sign  = "UKt1cobK6Lr1zdSTsgmLmNOosaFIvYVgxFM8/TaoGvy5zXceH1hT7/uREY2Av06/UtRJUBe2GXBzWyGDZ1LLAw==";
        String payload = "5bdd596a-ede5-4180-803d-32f5dd311099";
        String publicKey = "Z4AbOQfPRGz5uV4TrdzoHM408q7+4SWk9b64C2FoJr0=";
        Assert.assertTrue(Request.verifySignature(sign,payload,publicKey));
    }

    @Test
    public void testIshitaPaytm(){
        String payload = "{\"context\":{\"domain\":\"nic2004:52110\",\"country\":\"IND\",\"city\":\"std:080\",\"action\":\"search\",\"core_version\":\"0.9.1\",\"bap_id\": \"ondc-staging.paytm.com\",\"bap_uri\": \"https://ondc-staging.paytm.com\",\"transaction_id\":\"txn_test\",\"message_id\":\"a2fe6d52-9fe4-4d1a-9d0b-dccb8b48522d\",\"timestamp\":\"2022-04-19T09:17:55.971Z\",\"ttl\":\"P1M\"},\"message\":{\"intent\":{\"fulfillment\":{\"start\":{\"location\":{\"gps\":\"10.108768, 76.347517\"}},\"end\":{\"location\":{\"gps\":\"10.102997, 76.353480\"}}}}}}";
        Request request = new Request(payload);
        System.out.println(request.hash());

    }
    @Test
    public void generateKeyPair() throws  Exception{
        for (String algo : new String[]{"Ed25519","X25519"}){
            KeyPair agreementKeyPair = KeyPairGenerator.getInstance(algo,BouncyCastleProvider.PROVIDER_NAME).generateKeyPair();
            String publicKey = Base64.getEncoder().encodeToString(agreementKeyPair.getPublic().getEncoded());
            String privateKey = Base64.getEncoder().encodeToString(agreementKeyPair.getPrivate().getEncoded());
            System.out.println("String B64"+algo+"PublicKey=\"" + publicKey +"\";");
            System.out.println("String B64"+algo+"PrivateKey=\"" + privateKey +"\";");
        }
        /*
            Example printed:
            String B64Ed25519PublicKey="MCowBQYDK2VwAyEAop6xU+hDn5C3oSKDkYO7uG9KVcdxm2f0mDrT6X/wuz0=";
            String B64Ed25519PrivateKey="MFECAQEwBQYDK2VwBCIEII2jkV3E8YiU78U2sjlLQSbECJvJcztdKfOIa0f4eH4bgSEAop6xU+hDn5C3oSKDkYO7uG9KVcdxm2f0mDrT6X/wuz0=";
            String B64X25519PublicKey="MCowBQYDK2VuAyEAsysTZ8NnOn86Sko0w5h8jexEkzJdbT4e/tjhHxAdIn0=";
            String B64X25519PrivateKey="MFECAQEwBQYDK2VuBCIEIFBYM49aQt9Yb3dFaUlmkHxE5z1ltk63/ueGAInNSvRFgSEAsysTZ8NnOn86Sko0w5h8jexEkzJdbT4e/tjhHxAdIn0=";
         */
    }

    @Test
    public void readPublicKeyFromB64String() throws Exception{
        String B64Ed25519PublicKey="MCowBQYDK2VwAyEAop6xU+hDn5C3oSKDkYO7uG9KVcdxm2f0mDrT6X/wuz0=";
        String B64Ed25519PrivateKey="MFECAQEwBQYDK2VwBCIEII2jkV3E8YiU78U2sjlLQSbECJvJcztdKfOIa0f4eH4bgSEAop6xU+hDn5C3oSKDkYO7uG9KVcdxm2f0mDrT6X/wuz0=";
        String B64X25519PublicKey="MCowBQYDK2VuAyEAsysTZ8NnOn86Sko0w5h8jexEkzJdbT4e/tjhHxAdIn0=";
        String B64X25519PrivateKey="MFECAQEwBQYDK2VuBCIEIFBYM49aQt9Yb3dFaUlmkHxE5z1ltk63/ueGAInNSvRFgSEAsysTZ8NnOn86Sko0w5h8jexEkzJdbT4e/tjhHxAdIn0=";

        byte[] jceBytes = Base64.getDecoder().decode(B64Ed25519PublicKey);
        PublicKey key = getPublicKey("Ed25519",jceBytes);
        Assert.assertNotNull(key);

        jceBytes = Base64.getDecoder().decode(B64X25519PublicKey);
        key = getPublicKey("X25519",jceBytes);
        Assert.assertNotNull(key);

        byte[] privateJceBytes = Base64.getDecoder().decode(B64Ed25519PrivateKey);
        PrivateKey privateKey = getPrivateKey("Ed25519",privateJceBytes);

        Assert.assertNotNull(privateKey);

        privateJceBytes = Base64.getDecoder().decode(B64X25519PrivateKey);
        privateKey = getPrivateKey("X25519",privateJceBytes);

        Assert.assertNotNull(privateKey);
    }
    @Test
    public void testJuspayPublicKey() {
        String key = "kCa4OlmRVfCPcvzjPPGik0Ljei5dRYuuj/2K6upaf1E=";
        System.out.println(Request.getPemSigningKey(key));

    }

    @Test
    public void testEncryption() throws Exception{
        /*
        Two Parties generating key pair on their own servers.
         */
        KeyPair keyPairParty1 = KeyPairGenerator.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME).generateKeyPair();
        KeyPair keyPairParty2 = KeyPairGenerator.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME).generateKeyPair();

        KeyAgreement atServer1 = KeyAgreement.getInstance("X25519",BouncyCastleProvider.PROVIDER_NAME);
        atServer1.init(keyPairParty1.getPrivate()); // Server1 uses its private key to initialize the aggreement object
        atServer1.doPhase(keyPairParty2.getPublic(),true); //Uses Server2's ppublic Key
        SecretKey key1 = atServer1.generateSecret("TlsPremasterSecret"); //derive secret at server 1. "TlsPremasterSecret" is the algorithm for secret key. It is an aes key actually.

        // Let's see what happens at server 2
        KeyAgreement atServer2 = KeyAgreement.getInstance("X25519",BouncyCastleProvider.PROVIDER_NAME);
        atServer2.init(keyPairParty2.getPrivate()); // Server2 uses its private key to initialize the aggreement object
        atServer2.doPhase(keyPairParty1.getPublic(),true); //Uses Server1's ppublic Key
        SecretKey key2 = atServer2.generateSecret("TlsPremasterSecret"); //derive secret at server 2. "TlsPremasterSecret" is the algorithm for secret key. It is an aes key actually.


        Assert.assertArrayEquals(key1.getEncoded(),key2.getEncoded());
        //Same key was derived in both places and can be used for encrypted message.

        //*Server1
        Cipher cipher1 = Cipher.getInstance("AES",BouncyCastleProvider.PROVIDER_NAME);
        cipher1.init(Cipher.ENCRYPT_MODE,key1);
        byte[] encrypted1 = cipher1.doFinal("Beckn is awesome!".getBytes(StandardCharsets.UTF_8));
        String b64Encryped1 = Base64.getEncoder().encodeToString(encrypted1);


        //At Server2
        Cipher cipher2  = Cipher.getInstance("AES",BouncyCastleProvider.PROVIDER_NAME);
        cipher2.init(Cipher.DECRYPT_MODE,key2); //Same derived key in server 2same as key1
        byte[] decrypted2 = cipher2.doFinal(Base64.getDecoder().decode(b64Encryped1)); // b64 decode the message before decrypting the bytes

        Assert.assertEquals("Beckn is awesome!", new String(decrypted2)); //Ensure that the same encrypted message is received.!


    }

    @Test
    public void testSignatureBap() throws Exception{
        String key = "Fe/QcJJIR/lu3CUWNp/tz1HwWYaRLedMD8XvuU/A2w8=";
        //String key = "Fhjwaka1Za+ld+7Nms7S0C675r24mZoyWVn8JbYTjSs=";
        PublicKey p  = Request.getSigningPublicKey(key);
        Assert.assertNotNull(p);
    }

    public String getSigningString(long created_at, long expires_at, String hash) {
        StringBuilder builder = new StringBuilder();
        builder.append("(created): ").append(created_at);
        builder.append("\n(expires): ").append(expires_at);
        builder.append("\n").append("digest: BLAKE-512=").append(hash);
        System.out.println( "---------Signing String:\n" +builder + "\n------------------");
        return builder.toString();
    }

    @Test
    public void t(){
        String name = "Venky  Mahadevan";
        String[] parts = name.split(" ");
        System.out.println(parts[0]);
        System.out.println(name.substring(parts[0].length()));
    }
    @Test
    public void testSignMapPls() throws Exception {
        /*
         curl  -H 'ACCEPT: application/json'  -H 'AUTHORIZATION: Signature keyId="preprod-wiggles.humbhionline.in|af897cfc-8047-40a9-b8e6-ea83ce7eb9ee|ed25519",algorithm="ed25519",
         created="1705483980",expires="1705484010"
         ,headers="(created) (expires) digest",signature="8lq8xQx4jlE+PQEWc0lYuKScKcbY6D+VispaOrkaM+IEfW6IZ+Ja7Vi8+vjrd7fo3Xn2V5poLRGSVnyopSdnDA=="'  -H 'CONTENT-TYPE: application/json' 'https://mappls.com/store/ondc-preprod/on_search' -d '{"context":{"transaction_id":"7n776ancb614m9magvke17s8r1093259","country":"IND","bpp_id":"preprod-wiggles.humbhionline.in","city":"std:080","message_id":"2a62878d55d25b188024_T1705483979","core_version":"1.2.0","ttl":"PT30S","bap_id":"mappls.com","bpp_uri":"https:\/\/preprod-wiggles.humbhionline.in\/bpp","domain":"ONDC:RET10","action":"on_search","bap_uri":"https:\/\/mappls.com\/store\/ondc-preprod","timestamp":"2024-01-17T09:33:00.129Z"},"message":{"catalog":{"bpp\/providers":[{"fulfillments":[{"@ondc\/org\/TAT":"PT120H","contact":{"phone":"1234567890","email":"venkatramanm@gmail.com"},"@ondc\/org\/category":"Standard Delivery","state":{"descriptor":{"code":"Serviceable"}},"id":".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment","rateable":false,"type":"Delivery","@ondc\/org\/provider_name":"Wiggles","tracking":false}],"category_id":"Pet Care","payments":[{"@ondc\/org\/buyer_app_finder_fee_type":"percent","@ondc\/org\/withholding_amount":"0","id":".\/local_retail\/ind\/1@preprod-wiggles.humbhionline.in.payment","collected_by":"BAP","type":"ON-ORDER","@ondc\/org\/buyer_app_finder_fee_amount":"3"}],"locations":[{"address":{"country":"IN","city":"Bengaluru","street":"Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli","area_code":"560076","name":"Wiggles shopify store","locality":"A302","state":"Karnataka"},"id":".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location","time":{"schedule":{"times":["0000"],"holidays":[],"frequency":"PT24H"},"holidays":[],"days":"1,2,3,4,5,6,7","label":"enable","timestamp":"2023-11-07T14:12:41.760Z"},"descriptor":{"name":"Wiggles shopify store"},"gps":"12.902848,77.599918"}],"descriptor":{"symbol":"https:\/\/cdn.shopify.com\/s\/files\/1\/0647\/3662\/4854\/files\/logo_2_1_fc405c5b-4db5-419a-8ec3-380144685e90.svg?v=1669615619","images":["https:\/\/cdn.shopify.com\/s\/files\/1\/0647\/3662\/4854\/files\/logo_2_1_fc405c5b-4db5-419a-8ec3-380144685e90.svg?v=1669615619"],"code":"preprod-wiggles.humbhionline.in","name":"Wiggles shopify store","short_desc":"Wiggles shopify store","long_desc":"Wiggles shopify store"},"time":{"label":"enable","timestamp":"2024-01-17T09:33:00.129Z"},"id":"preprod-wiggles.humbhionline.in","rateable":false,"items":[{"@ondc\/org\/cancellable":true,"@ondc\/org\/returnable":false,"quantity":{"available":{"count":"99"},"maximum":{"count":"99"},"unitized":{"measure":{"unit":"unit","computed_value":"0","value":"1","estimated_value":"0"},"count":1}},"location_ids":[".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location"],"@ondc\/org\/statutory_reqs_packaged_commodities":{"common_or_generic_name_of_commodity":"Dogs of all ages","net_quantity_or_measure_of_commodity_in_pkg":"100 gms","imported_product_country_of_origin":"IND","manufacturer_or_packer_name":"Wiggles","manufacturer_or_packer_address":"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076","month_year_of_manufacture_packing_import":"Please refer to the packaging of the product"},"@ondc\/org\/contact_details_consumer_care":"Wiggles shopify store,venkatramanm@gmail.com,1234567890","@ondc\/org\/seller_pickup_return":false,"@ondc\/org\/time_to_ship":"PT48H","descriptor":{"symbol":"https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/HoneyChicken_Front.png?v=1680605927","images":["https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/HoneyChicken_Front.png?v=1680605927","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/HoneyChicken_Back.png?v=1680605927"],"code":"Barkstix Dog Treats for Training Adult & Puppies (Honey Chicken)","name":"Barkstix Dog Treats for Training Adult & Puppies (Honey Chicken) - ( 100gm )","short_desc":"Barkstix Dog Treats for Training Adult & Puppies (Honey Chicken) - ( 100gm )","long_desc":"Barkstix Dog Treats for Training Adult & Puppies (Honey Chicken) - ( 100gm )"},"payment_ids":[".\/local_retail\/ind\/1@preprod-wiggles.humbhionline.in.payment"],"rateable":false,"location_id":".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location","fulfillment_ids":[".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment"],"tags":[{"code":"general_attributes","list":[{"code":"All","value":"true"},{"code":"Dog All","value":"true"},{"code":"Category_Senior","value":"true"},{"code":"Category_Puppy","value":"true"},{"code":"Food and Treats - Dog","value":"true"},{"code":"Product_Treats","value":"true"},{"code":"Human Grade","value":"true"},{"code":"Category_Dog","value":"true"},{"code":"No artificial enhancers","value":"true"},{"code":"Vet Approved","value":"true"},{"code":"product_id","value":"8224104808742"}]},{"code":"origin","list":[{"code":"country","value":"IND"}]},{"code":"veg_nonveg","list":[{"code":"veg","value":"no"}]}],"fulfillment_id":".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment","recommended":true,"@ondc\/org\/return_window":"PT0S","@ondc\/org\/available_on_cod":false,"category_id":"Pet Care","related":true,"price":{"listed_value":"229","offered_value":"229","currency":"INR","maximum_value":"229","value":"229"},"matched":true,"time":{"label":"enable","timestamp":"2023-11-05T18:30:00.000Z"},"id":".\/local_retail\/ind\/44773308137766@preprod-wiggles.humbhionline.in.item"},{"@ondc\/org\/cancellable":true,"@ondc\/org\/returnable":false,"quantity":{"available":{"count":"99"},"maximum":{"count":"99"},"unitized":{"measure":{"unit":"unit","computed_value":"0","value":"1","estimated_value":"0"},"count":1}},"location_ids":[".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location"],"@ondc\/org\/statutory_reqs_packaged_commodities":{"common_or_generic_name_of_commodity":"Dogs of all ages","net_quantity_or_measure_of_commodity_in_pkg":"100 gms","imported_product_country_of_origin":"IND","manufacturer_or_packer_name":"Wiggles","manufacturer_or_packer_address":"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076","month_year_of_manufacture_packing_import":"Please refer to the packaging of the product"},"@ondc\/org\/contact_details_consumer_care":"Wiggles shopify store,venkatramanm@gmail.com,1234567890","@ondc\/org\/seller_pickup_return":false,"@ondc\/org\/time_to_ship":"PT48H","descriptor":{"symbol":"https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/1_Chicken_Banana-Front.png?v=1680605266","images":["https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/1_Chicken_Banana-Front.png?v=1680605266","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Chicken_Banana-PDP_2_1.png?v=1680605266","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Chicken_Banana-PDP_3_1.png?v=1680605266"],"code":"Barkstix Dog Treats for Training Adult & Puppies - Chicken & Banana","name":"Barkstix Dog Treats for Training Adult & Puppies - Chicken & Banana - ( 100gm )","short_desc":"Barkstix Dog Treats for Training Adult & Puppies - Chicken & Banana - ( 100gm )","long_desc":"Barkstix Dog Treats for Training Adult & Puppies - Chicken & Banana - ( 100gm )"},"payment_ids":[".\/local_retail\/ind\/1@preprod-wiggles.humbhionline.in.payment"],"rateable":false,"location_id":".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location","fulfillment_ids":[".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment"],"tags":[{"code":"general_attributes","list":[{"code":"All","value":"true"},{"code":"badge--new","value":"true"},{"code":"Dog All","value":"true"},{"code":"No Artificial Enhancers","value":"true"},{"code":"Category_Senior","value":"true"},{"code":"Category_Puppy","value":"true"},{"code":"Food and Treats - Dog","value":"true"},{"code":"Product_Treats","value":"true"},{"code":"Human Grade","value":"true"},{"code":"Non GMO","value":"true"},{"code":"Category_Dog","value":"true"},{"code":"Vet Approved","value":"true"},{"code":"product_id","value":"8224077938982"}]},{"code":"origin","list":[{"code":"country","value":"IND"}]},{"code":"veg_nonveg","list":[{"code":"veg","value":"no"}]}],"fulfillment_id":".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment","recommended":true,"@ondc\/org\/return_window":"PT0S","@ondc\/org\/available_on_cod":false,"category_id":"Pet Care","related":true,"price":{"listed_value":"189","offered_value":"189","currency":"INR","maximum_value":"249","value":"189"},"matched":true,"time":{"label":"enable","timestamp":"2023-11-05T18:30:00.000Z"},"id":".\/local_retail\/ind\/44773217239334@preprod-wiggles.humbhionline.in.item"},{"@ondc\/org\/cancellable":true,"@ondc\/org\/returnable":false,"quantity":{"available":{"count":"99"},"maximum":{"count":"99"},"unitized":{"measure":{"unit":"unit","computed_value":"0","value":"1","estimated_value":"0"},"count":1}},"location_ids":[".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location"],"@ondc\/org\/statutory_reqs_packaged_commodities":{"common_or_generic_name_of_commodity":"Dogs of all ages","net_quantity_or_measure_of_commodity_in_pkg":"100 gms","imported_product_country_of_origin":"IND","manufacturer_or_packer_name":"Wiggles","manufacturer_or_packer_address":"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076","month_year_of_manufacture_packing_import":"Please refer to the packaging of the product"},"@ondc\/org\/contact_details_consumer_care":"Wiggles shopify store,venkatramanm@gmail.com,1234567890","@ondc\/org\/seller_pickup_return":false,"@ondc\/org\/time_to_ship":"PT48H","descriptor":{"symbol":"https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Barkstix_Chicken_Strawberry.png?v=1680605733","images":["https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Barkstix_Chicken_Strawberry.png?v=1680605733","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Chicken_Strawberry-PDP_2_1.png?v=1680605733","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Chicken_Strawberry-PDP_3_1.png?v=1680605733"],"code":"Barkstix Dog Treats for Training Adult & Puppies, (Chicken & Strawberry)","name":"Barkstix Dog Treats for Training Adult & Puppies, (Chicken & Strawberry) - ( 100gm )","short_desc":"Barkstix Dog Treats for Training Adult & Puppies, (Chicken & Strawberry) - ( 100gm )","long_desc":"Barkstix Dog Treats for Training Adult & Puppies, (Chicken & Strawberry) - ( 100gm )"},"payment_ids":[".\/local_retail\/ind\/1@preprod-wiggles.humbhionline.in.payment"],"rateable":false,"location_id":".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location","fulfillment_ids":[".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment"],"tags":[{"code":"general_attributes","list":[{"code":"All","value":"true"},{"code":"Dog All","value":"true"},{"code":"No Artificial Enhancers","value":"true"},{"code":"Category_Senior","value":"true"},{"code":"Category_Puppy","value":"true"},{"code":"Food and Treats - Dog","value":"true"},{"code":"Product_Treats","value":"true"},{"code":"Human Grade","value":"true"},{"code":"Non GMO","value":"true"},{"code":"Category_Dog","value":"true"},{"code":"Vet Approved","value":"true"},{"code":"product_id","value":"8224096125222"}]},{"code":"origin","list":[{"code":"country","value":"IND"}]},{"code":"veg_nonveg","list":[{"code":"veg","value":"no"}]}],"fulfillment_id":".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment","recommended":true,"@ondc\/org\/return_window":"PT0S","@ondc\/org\/available_on_cod":false,"category_id":"Pet Care","related":true,"price":{"listed_value":"229","offered_value":"229","currency":"INR","maximum_value":"229","value":"229"},"matched":true,"time":{"label":"enable","timestamp":"2023-11-05T18:30:00.000Z"},"id":".\/local_retail\/ind\/44773286805798@preprod-wiggles.humbhionline.in.item"},{"@ondc\/org\/cancellable":true,"@ondc\/org\/returnable":false,"quantity":{"available":{"count":"99"},"maximum":{"count":"99"},"unitized":{"measure":{"unit":"unit","computed_value":"0","value":"1","estimated_value":"0"},"count":1}},"location_ids":[".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location"],"@ondc\/org\/statutory_reqs_packaged_commodities":{"common_or_generic_name_of_commodity":"Dogs of all ages","net_quantity_or_measure_of_commodity_in_pkg":"100 gms","imported_product_country_of_origin":"IND","manufacturer_or_packer_name":"Wiggles","manufacturer_or_packer_address":"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076","month_year_of_manufacture_packing_import":"Please refer to the packaging of the product"},"@ondc\/org\/contact_details_consumer_care":"Wiggles shopify store,venkatramanm@gmail.com,1234567890","@ondc\/org\/seller_pickup_return":false,"@ondc\/org\/time_to_ship":"PT48H","descriptor":{"symbol":"https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Barkstix_Lamb_Peanutbutter.png?v=1680605713","images":["https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Barkstix_Lamb_Peanutbutter.png?v=1680605713","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Barkstix_Lamb_Peanutbutter_Back.png?v=1680605713","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Lamb_peanut_USPImage.png?v=1680605713"],"code":"Barkstix Dog Treats for Training Adult & Puppies, (Lamb & Peanut Butter)","name":"Barkstix Dog Treats for Training Adult & Puppies, (Lamb & Peanut Butter) - ( 100gm )","short_desc":"Barkstix Dog Treats for Training Adult & Puppies, (Lamb & Peanut Butter) - ( 100gm )","long_desc":"Barkstix Dog Treats for Training Adult & Puppies, (Lamb & Peanut Butter) - ( 100gm )"},"payment_ids":[".\/local_retail\/ind\/1@preprod-wiggles.humbhionline.in.payment"],"rateable":false,"location_id":".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location","fulfillment_ids":[".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment"],"tags":[{"code":"general_attributes","list":[{"code":"All","value":"true"},{"code":"Dog All","value":"true"},{"code":"No Artificial Enhancers","value":"true"},{"code":"Category_Senior","value":"true"},{"code":"Category_Puppy","value":"true"},{"code":"Food and Treats - Dog","value":"true"},{"code":"Product_Treats","value":"true"},{"code":"Human Grade","value":"true"},{"code":"Non GMO","value":"true"},{"code":"Category_Dog","value":"true"},{"code":"Vet Approved","value":"true"},{"code":"product_id","value":"8224095338790"}]},{"code":"origin","list":[{"code":"country","value":"IND"}]},{"code":"veg_nonveg","list":[{"code":"veg","value":"no"}]}],"fulfillment_id":".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment","recommended":true,"@ondc\/org\/return_window":"PT0S","@ondc\/org\/available_on_cod":false,"category_id":"Pet Care","related":true,"price":{"listed_value":"159","offered_value":"159","currency":"INR","maximum_value":"229","value":"159"},"matched":true,"time":{"label":"enable","timestamp":"2023-11-05T18:30:00.000Z"},"id":".\/local_retail\/ind\/44773282873638@preprod-wiggles.humbhionline.in.item"},{"@ondc\/org\/cancellable":true,"@ondc\/org\/returnable":false,"quantity":{"available":{"count":"99"},"maximum":{"count":"99"},"unitized":{"measure":{"unit":"unit","computed_value":"0","value":"1","estimated_value":"0"},"count":1}},"location_ids":[".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location"],"@ondc\/org\/statutory_reqs_packaged_commodities":{"common_or_generic_name_of_commodity":"Dogs of all ages","net_quantity_or_measure_of_commodity_in_pkg":"100 gms","imported_product_country_of_origin":"IND","manufacturer_or_packer_name":"Wiggles","manufacturer_or_packer_address":"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076","month_year_of_manufacture_packing_import":"Please refer to the packaging of the product"},"@ondc\/org\/contact_details_consumer_care":"Wiggles shopify store,venkatramanm@gmail.com,1234567890","@ondc\/org\/seller_pickup_return":false,"@ondc\/org\/time_to_ship":"PT48H","descriptor":{"symbol":"https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/BerryBlast_Front.png?v=1680605946","images":["https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/BerryBlast_Front.png?v=1680605946","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/BerryBlast-PDP_2_1.png?v=1680605946","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/BerryBlast-PDP_3_1.png?v=1680605946"],"code":"Barkstix Dog Treats for Training Adult & Puppies, 100g (Berry Blast)","name":"Barkstix Dog Treats for Training Adult & Puppies, 100g (Berry Blast) - ( 100 g )","short_desc":"Barkstix Dog Treats for Training Adult & Puppies, 100g (Berry Blast) - ( 100 g )","long_desc":"Barkstix Dog Treats for Training Adult & Puppies, 100g (Berry Blast) - ( 100 g )"},"payment_ids":[".\/local_retail\/ind\/1@preprod-wiggles.humbhionline.in.payment"],"rateable":false,"location_id":".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location","fulfillment_ids":[".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment"],"tags":[{"code":"general_attributes","list":[{"code":"All","value":"true"},{"code":"Dog All","value":"true"},{"code":"No Artificial Enhancers","value":"true"},{"code":"Category_Senior","value":"true"},{"code":"Category_Puppy","value":"true"},{"code":"Food and Treats - Dog","value":"true"},{"code":"Product_Treats","value":"true"},{"code":"Human Grade","value":"true"},{"code":"Non GMO","value":"true"},{"code":"Category_Dog","value":"true"},{"code":"Vet Approved","value":"true"},{"code":"product_id","value":"8224105529638"}]},{"code":"origin","list":[{"code":"country","value":"IND"}]},{"code":"veg_nonveg","list":[{"code":"veg","value":"no"}]}],"fulfillment_id":".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment","recommended":true,"@ondc\/org\/return_window":"PT0S","@ondc\/org\/available_on_cod":false,"category_id":"Pet Care","related":true,"price":{"listed_value":"154","offered_value":"154","currency":"INR","maximum_value":"229","value":"154"},"matched":true,"time":{"label":"enable","timestamp":"2024-01-16T17:19:43.000Z"},"id":".\/local_retail\/ind\/44773310038310@preprod-wiggles.humbhionline.in.item"},{"@ondc\/org\/cancellable":true,"@ondc\/org\/returnable":false,"quantity":{"available":{"count":"99"},"maximum":{"count":"99"},"unitized":{"measure":{"unit":"unit","computed_value":"0","value":"1","estimated_value":"0"},"count":1}},"location_ids":[".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location"],"@ondc\/org\/statutory_reqs_packaged_commodities":{"common_or_generic_name_of_commodity":"Dogs of all ages","net_quantity_or_measure_of_commodity_in_pkg":"100 gms","imported_product_country_of_origin":"IND","manufacturer_or_packer_name":"Wiggles","manufacturer_or_packer_address":"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076","month_year_of_manufacture_packing_import":"Please refer to the packaging of the product"},"@ondc\/org\/contact_details_consumer_care":"Wiggles shopify store,venkatramanm@gmail.com,1234567890","@ondc\/org\/seller_pickup_return":false,"@ondc\/org\/time_to_ship":"PT48H","descriptor":{"symbol":"https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Barkstix_Chicken_Hemp_Front.png?v=1680605739","images":["https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Barkstix_Chicken_Hemp_Front.png?v=1680605739","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Barkstix_Chicken_Hemp_back.png?v=1680605739","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Barkstix_Chicken_Hemp_USPimage.png?v=1680605739"],"code":"Barkstix Dog Treats for Training Adult & Puppies, 100g (Chicken & Hemp)","name":"Barkstix Dog Treats for Training Adult & Puppies, 100g (Chicken & Hemp) - ( 100gm )","short_desc":"Barkstix Dog Treats for Training Adult & Puppies, 100g (Chicken & Hemp) - ( 100gm )","long_desc":"Barkstix Dog Treats for Training Adult & Puppies, 100g (Chicken & Hemp) - ( 100gm )"},"payment_ids":[".\/local_retail\/ind\/1@preprod-wiggles.humbhionline.in.payment"],"rateable":false,"location_id":".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location","fulfillment_ids":[".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment"],"tags":[{"code":"general_attributes","list":[{"code":"All","value":"true"},{"code":"Dog All","value":"true"},{"code":"No Artificial Enhancers","value":"true"},{"code":"Category_Senior","value":"true"},{"code":"Category_Puppy","value":"true"},{"code":"Food and Treats - Dog","value":"true"},{"code":"Product_Treats","value":"true"},{"code":"Human Grade","value":"true"},{"code":"Non GMO","value":"true"},{"code":"Category_Dog","value":"true"},{"code":"Vet Approved","value":"true"},{"code":"product_id","value":"8224096321830"}]},{"code":"origin","list":[{"code":"country","value":"IND"}]},{"code":"veg_nonveg","list":[{"code":"veg","value":"no"}]}],"fulfillment_id":".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment","recommended":true,"@ondc\/org\/return_window":"PT0S","@ondc\/org\/available_on_cod":false,"category_id":"Pet Care","related":true,"price":{"listed_value":"229","offered_value":"229","currency":"INR","maximum_value":"229","value":"229"},"matched":true,"time":{"label":"enable","timestamp":"2023-11-05T18:30:00.000Z"},"id":".\/local_retail\/ind\/44773287657766@preprod-wiggles.humbhionline.in.item"},{"@ondc\/org\/cancellable":true,"@ondc\/org\/returnable":false,"quantity":{"available":{"count":"99"},"maximum":{"count":"99"},"unitized":{"measure":{"unit":"unit","computed_value":"0","value":"1","estimated_value":"0"},"count":1}},"location_ids":[".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location"],"@ondc\/org\/statutory_reqs_packaged_commodities":{"common_or_generic_name_of_commodity":"Dogs and Cats","net_quantity_or_measure_of_commodity_in_pkg":"100 gms","imported_product_country_of_origin":"IND","manufacturer_or_packer_name":"Wiggles","manufacturer_or_packer_address":"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076","month_year_of_manufacture_packing_import":"Please refer to the packaging of the product"},"@ondc\/org\/contact_details_consumer_care":"Wiggles shopify store,venkatramanm@gmail.com,1234567890","@ondc\/org\/seller_pickup_return":false,"@ondc\/org\/time_to_ship":"PT48H","descriptor":{"symbol":"https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Artboard1_5d274230-45d1-48a8-831b-d805187a9eae.png?v=1680605768","images":["https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Artboard1_5d274230-45d1-48a8-831b-d805187a9eae.png?v=1680605768","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Artboard1copy_d643cf77-48d2-4730-b1ea-6d44bc4489cf.png?v=1680605768","https:\/\/cdn.shopify.com\/s\/files\/1\/0739\/2454\/2758\/products\/Artboard1copy2_93e25a41-49f0-4b9f-a18c-09e59a640ed5.png?v=1680605768"],"code":"EPIBOON™ Antifungal Dusting Powder for Dogs & Cats - 100gm","name":"EPIBOON™ Antifungal Dusting Powder for Dogs & Cats - 100gm - ( 100 g )","short_desc":"EPIBOON™ Antifungal Dusting Powder for Dogs & Cats - 100gm - ( 100 g )","long_desc":"EPIBOON™ Antifungal Dusting Powder for Dogs & Cats - 100gm - ( 100 g )"},"payment_ids":[".\/local_retail\/ind\/1@preprod-wiggles.humbhionline.in.payment"],"rateable":false,"location_id":".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location","fulfillment_ids":[".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment"],"tags":[{"code":"general_attributes","list":[{"code":"All","value":"true"},{"code":"Category_Cat","value":"true"},{"code":"Fast action","value":"true"},{"code":"Category_Puppy","value":"true"},{"code":"Easy to use","value":"true"},{"code":"Category_Dog","value":"true"},{"code":"Cat All","value":"true"},{"code":"Vet Approved","value":"true"},{"code":"Category_Kitten","value":"true"},{"code":"Dog All","value":"true"},{"code":"Health & Wellness - Dog","value":"true"},{"code":"Category_Senior","value":"true"},{"code":"Health & Wellness - Cat","value":"true"},{"code":"Product_Defenders","value":"true"},{"code":"product_id","value":"8224097337638"}]},{"code":"origin","list":[{"code":"country","value":"IND"}]},{"code":"veg_nonveg","list":[{"code":"veg","value":"no"}]}],"fulfillment_id":".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment","recommended":true,"@ondc\/org\/return_window":"PT0S","@ondc\/org\/available_on_cod":false,"category_id":"Pet Care","related":true,"price":{"listed_value":"210","offered_value":"210","currency":"INR","maximum_value":"210","value":"210"},"matched":true,"time":{"label":"enable","timestamp":"2023-11-05T18:30:00.000Z"},"id":".\/local_retail\/ind\/44773290770726@preprod-wiggles.humbhionline.in.item"}],"ttl":"PT2M","tags":[{"code":"serviceability","list":[{"code":"location","value":".\/local_retail\/ind\/80777412902@preprod-wiggles.humbhionline.in.provider_location"},{"code":"category","value":"Pet Care"},{"code":"type","value":"10"},{"code":"val","value":"10000.0"},{"code":"unit","value":"km"}]}]}],"bpp\/fulfillments":[{"id":".\/local_retail\/ind\/2@preprod-wiggles.humbhionline.in.fulfillment","rateable":false,"type":"Delivery","tracking":false}],"bpp\/descriptor":{"symbol":"https:\/\/cdn.shopify.com\/s\/files\/1\/0647\/3662\/4854\/files\/logo_2_1_fc405c5b-4db5-419a-8ec3-380144685e90.svg?v=1669615619","images":["https:\/\/cdn.shopify.com\/s\/files\/1\/0647\/3662\/4854\/files\/logo_2_1_fc405c5b-4db5-419a-8ec3-380144685e90.svg?v=1669615619"],"code":"preprod-wiggles.humbhionline.in","name":"Wiggles shopify store","short_desc":"Wiggles shopify store","long_desc":"Wiggles shopify store"}}}}'

         */
        String req = "{\"context\":{\"transaction_id\":\"7n776ancb614m9magvke17s8r1093259\",\"country\":\"IND\",\"bpp_id\":\"preprod-wiggles.humbhionline.in\",\"city\":\"std:080\",\"message_id\":\"2a62878d55d25b188024_T1705483979\",\"core_version\":\"1.2.0\",\"ttl\":\"PT30S\",\"bap_id\":\"mappls.com\",\"bpp_uri\":\"https:\\/\\/preprod-wiggles.humbhionline.in\\/bpp\",\"domain\":\"ONDC:RET10\",\"action\":\"on_search\",\"bap_uri\":\"https:\\/\\/mappls.com\\/store\\/ondc-preprod\",\"timestamp\":\"2024-01-17T09:33:00.129Z\"},\"message\":{\"catalog\":{\"bpp\\/providers\":[{\"fulfillments\":[{\"@ondc\\/org\\/TAT\":\"PT120H\",\"contact\":{\"phone\":\"1234567890\",\"email\":\"venkatramanm@gmail.com\"},\"@ondc\\/org\\/category\":\"Standard Delivery\",\"state\":{\"descriptor\":{\"code\":\"Serviceable\"}},\"id\":\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\",\"rateable\":false,\"type\":\"Delivery\",\"@ondc\\/org\\/provider_name\":\"Wiggles\",\"tracking\":false}],\"category_id\":\"Pet Care\",\"payments\":[{\"@ondc\\/org\\/buyer_app_finder_fee_type\":\"percent\",\"@ondc\\/org\\/withholding_amount\":\"0\",\"id\":\".\\/local_retail\\/ind\\/1@preprod-wiggles.humbhionline.in.payment\",\"collected_by\":\"BAP\",\"type\":\"ON-ORDER\",\"@ondc\\/org\\/buyer_app_finder_fee_amount\":\"3\"}],\"locations\":[{\"address\":{\"country\":\"IN\",\"city\":\"Bengaluru\",\"street\":\"Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli\",\"area_code\":\"560076\",\"name\":\"Wiggles shopify store\",\"locality\":\"A302\",\"state\":\"Karnataka\"},\"id\":\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\",\"time\":{\"schedule\":{\"times\":[\"0000\"],\"holidays\":[],\"frequency\":\"PT24H\"},\"holidays\":[],\"days\":\"1,2,3,4,5,6,7\",\"label\":\"enable\",\"timestamp\":\"2023-11-07T14:12:41.760Z\"},\"descriptor\":{\"name\":\"Wiggles shopify store\"},\"gps\":\"12.902848,77.599918\"}],\"descriptor\":{\"symbol\":\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0647\\/3662\\/4854\\/files\\/logo_2_1_fc405c5b-4db5-419a-8ec3-380144685e90.svg?v=1669615619\",\"images\":[\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0647\\/3662\\/4854\\/files\\/logo_2_1_fc405c5b-4db5-419a-8ec3-380144685e90.svg?v=1669615619\"],\"code\":\"preprod-wiggles.humbhionline.in\",\"name\":\"Wiggles shopify store\",\"short_desc\":\"Wiggles shopify store\",\"long_desc\":\"Wiggles shopify store\"},\"time\":{\"label\":\"enable\",\"timestamp\":\"2024-01-17T09:33:00.129Z\"},\"id\":\"preprod-wiggles.humbhionline.in\",\"rateable\":false,\"items\":[{\"@ondc\\/org\\/cancellable\":true,\"@ondc\\/org\\/returnable\":false,\"quantity\":{\"available\":{\"count\":\"99\"},\"maximum\":{\"count\":\"99\"},\"unitized\":{\"measure\":{\"unit\":\"unit\",\"computed_value\":\"0\",\"value\":\"1\",\"estimated_value\":\"0\"},\"count\":1}},\"location_ids\":[\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\"],\"@ondc\\/org\\/statutory_reqs_packaged_commodities\":{\"common_or_generic_name_of_commodity\":\"Dogs of all ages\",\"net_quantity_or_measure_of_commodity_in_pkg\":\"100 gms\",\"imported_product_country_of_origin\":\"IND\",\"manufacturer_or_packer_name\":\"Wiggles\",\"manufacturer_or_packer_address\":\"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076\",\"month_year_of_manufacture_packing_import\":\"Please refer to the packaging of the product\"},\"@ondc\\/org\\/contact_details_consumer_care\":\"Wiggles shopify store,venkatramanm@gmail.com,1234567890\",\"@ondc\\/org\\/seller_pickup_return\":false,\"@ondc\\/org\\/time_to_ship\":\"PT48H\",\"descriptor\":{\"symbol\":\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/HoneyChicken_Front.png?v=1680605927\",\"images\":[\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/HoneyChicken_Front.png?v=1680605927\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/HoneyChicken_Back.png?v=1680605927\"],\"code\":\"Barkstix Dog Treats for Training Adult & Puppies (Honey Chicken)\",\"name\":\"Barkstix Dog Treats for Training Adult & Puppies (Honey Chicken) - ( 100gm )\",\"short_desc\":\"Barkstix Dog Treats for Training Adult & Puppies (Honey Chicken) - ( 100gm )\",\"long_desc\":\"Barkstix Dog Treats for Training Adult & Puppies (Honey Chicken) - ( 100gm )\"},\"payment_ids\":[\".\\/local_retail\\/ind\\/1@preprod-wiggles.humbhionline.in.payment\"],\"rateable\":false,\"location_id\":\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\",\"fulfillment_ids\":[\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\"],\"tags\":[{\"code\":\"general_attributes\",\"list\":[{\"code\":\"All\",\"value\":\"true\"},{\"code\":\"Dog All\",\"value\":\"true\"},{\"code\":\"Category_Senior\",\"value\":\"true\"},{\"code\":\"Category_Puppy\",\"value\":\"true\"},{\"code\":\"Food and Treats - Dog\",\"value\":\"true\"},{\"code\":\"Product_Treats\",\"value\":\"true\"},{\"code\":\"Human Grade\",\"value\":\"true\"},{\"code\":\"Category_Dog\",\"value\":\"true\"},{\"code\":\"No artificial enhancers\",\"value\":\"true\"},{\"code\":\"Vet Approved\",\"value\":\"true\"},{\"code\":\"product_id\",\"value\":\"8224104808742\"}]},{\"code\":\"origin\",\"list\":[{\"code\":\"country\",\"value\":\"IND\"}]},{\"code\":\"veg_nonveg\",\"list\":[{\"code\":\"veg\",\"value\":\"no\"}]}],\"fulfillment_id\":\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\",\"recommended\":true,\"@ondc\\/org\\/return_window\":\"PT0S\",\"@ondc\\/org\\/available_on_cod\":false,\"category_id\":\"Pet Care\",\"related\":true,\"price\":{\"listed_value\":\"229\",\"offered_value\":\"229\",\"currency\":\"INR\",\"maximum_value\":\"229\",\"value\":\"229\"},\"matched\":true,\"time\":{\"label\":\"enable\",\"timestamp\":\"2023-11-05T18:30:00.000Z\"},\"id\":\".\\/local_retail\\/ind\\/44773308137766@preprod-wiggles.humbhionline.in.item\"},{\"@ondc\\/org\\/cancellable\":true,\"@ondc\\/org\\/returnable\":false,\"quantity\":{\"available\":{\"count\":\"99\"},\"maximum\":{\"count\":\"99\"},\"unitized\":{\"measure\":{\"unit\":\"unit\",\"computed_value\":\"0\",\"value\":\"1\",\"estimated_value\":\"0\"},\"count\":1}},\"location_ids\":[\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\"],\"@ondc\\/org\\/statutory_reqs_packaged_commodities\":{\"common_or_generic_name_of_commodity\":\"Dogs of all ages\",\"net_quantity_or_measure_of_commodity_in_pkg\":\"100 gms\",\"imported_product_country_of_origin\":\"IND\",\"manufacturer_or_packer_name\":\"Wiggles\",\"manufacturer_or_packer_address\":\"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076\",\"month_year_of_manufacture_packing_import\":\"Please refer to the packaging of the product\"},\"@ondc\\/org\\/contact_details_consumer_care\":\"Wiggles shopify store,venkatramanm@gmail.com,1234567890\",\"@ondc\\/org\\/seller_pickup_return\":false,\"@ondc\\/org\\/time_to_ship\":\"PT48H\",\"descriptor\":{\"symbol\":\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/1_Chicken_Banana-Front.png?v=1680605266\",\"images\":[\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/1_Chicken_Banana-Front.png?v=1680605266\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Chicken_Banana-PDP_2_1.png?v=1680605266\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Chicken_Banana-PDP_3_1.png?v=1680605266\"],\"code\":\"Barkstix Dog Treats for Training Adult & Puppies - Chicken & Banana\",\"name\":\"Barkstix Dog Treats for Training Adult & Puppies - Chicken & Banana - ( 100gm )\",\"short_desc\":\"Barkstix Dog Treats for Training Adult & Puppies - Chicken & Banana - ( 100gm )\",\"long_desc\":\"Barkstix Dog Treats for Training Adult & Puppies - Chicken & Banana - ( 100gm )\"},\"payment_ids\":[\".\\/local_retail\\/ind\\/1@preprod-wiggles.humbhionline.in.payment\"],\"rateable\":false,\"location_id\":\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\",\"fulfillment_ids\":[\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\"],\"tags\":[{\"code\":\"general_attributes\",\"list\":[{\"code\":\"All\",\"value\":\"true\"},{\"code\":\"badge--new\",\"value\":\"true\"},{\"code\":\"Dog All\",\"value\":\"true\"},{\"code\":\"No Artificial Enhancers\",\"value\":\"true\"},{\"code\":\"Category_Senior\",\"value\":\"true\"},{\"code\":\"Category_Puppy\",\"value\":\"true\"},{\"code\":\"Food and Treats - Dog\",\"value\":\"true\"},{\"code\":\"Product_Treats\",\"value\":\"true\"},{\"code\":\"Human Grade\",\"value\":\"true\"},{\"code\":\"Non GMO\",\"value\":\"true\"},{\"code\":\"Category_Dog\",\"value\":\"true\"},{\"code\":\"Vet Approved\",\"value\":\"true\"},{\"code\":\"product_id\",\"value\":\"8224077938982\"}]},{\"code\":\"origin\",\"list\":[{\"code\":\"country\",\"value\":\"IND\"}]},{\"code\":\"veg_nonveg\",\"list\":[{\"code\":\"veg\",\"value\":\"no\"}]}],\"fulfillment_id\":\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\",\"recommended\":true,\"@ondc\\/org\\/return_window\":\"PT0S\",\"@ondc\\/org\\/available_on_cod\":false,\"category_id\":\"Pet Care\",\"related\":true,\"price\":{\"listed_value\":\"189\",\"offered_value\":\"189\",\"currency\":\"INR\",\"maximum_value\":\"249\",\"value\":\"189\"},\"matched\":true,\"time\":{\"label\":\"enable\",\"timestamp\":\"2023-11-05T18:30:00.000Z\"},\"id\":\".\\/local_retail\\/ind\\/44773217239334@preprod-wiggles.humbhionline.in.item\"},{\"@ondc\\/org\\/cancellable\":true,\"@ondc\\/org\\/returnable\":false,\"quantity\":{\"available\":{\"count\":\"99\"},\"maximum\":{\"count\":\"99\"},\"unitized\":{\"measure\":{\"unit\":\"unit\",\"computed_value\":\"0\",\"value\":\"1\",\"estimated_value\":\"0\"},\"count\":1}},\"location_ids\":[\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\"],\"@ondc\\/org\\/statutory_reqs_packaged_commodities\":{\"common_or_generic_name_of_commodity\":\"Dogs of all ages\",\"net_quantity_or_measure_of_commodity_in_pkg\":\"100 gms\",\"imported_product_country_of_origin\":\"IND\",\"manufacturer_or_packer_name\":\"Wiggles\",\"manufacturer_or_packer_address\":\"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076\",\"month_year_of_manufacture_packing_import\":\"Please refer to the packaging of the product\"},\"@ondc\\/org\\/contact_details_consumer_care\":\"Wiggles shopify store,venkatramanm@gmail.com,1234567890\",\"@ondc\\/org\\/seller_pickup_return\":false,\"@ondc\\/org\\/time_to_ship\":\"PT48H\",\"descriptor\":{\"symbol\":\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Barkstix_Chicken_Strawberry.png?v=1680605733\",\"images\":[\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Barkstix_Chicken_Strawberry.png?v=1680605733\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Chicken_Strawberry-PDP_2_1.png?v=1680605733\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Chicken_Strawberry-PDP_3_1.png?v=1680605733\"],\"code\":\"Barkstix Dog Treats for Training Adult & Puppies, (Chicken & Strawberry)\",\"name\":\"Barkstix Dog Treats for Training Adult & Puppies, (Chicken & Strawberry) - ( 100gm )\",\"short_desc\":\"Barkstix Dog Treats for Training Adult & Puppies, (Chicken & Strawberry) - ( 100gm )\",\"long_desc\":\"Barkstix Dog Treats for Training Adult & Puppies, (Chicken & Strawberry) - ( 100gm )\"},\"payment_ids\":[\".\\/local_retail\\/ind\\/1@preprod-wiggles.humbhionline.in.payment\"],\"rateable\":false,\"location_id\":\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\",\"fulfillment_ids\":[\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\"],\"tags\":[{\"code\":\"general_attributes\",\"list\":[{\"code\":\"All\",\"value\":\"true\"},{\"code\":\"Dog All\",\"value\":\"true\"},{\"code\":\"No Artificial Enhancers\",\"value\":\"true\"},{\"code\":\"Category_Senior\",\"value\":\"true\"},{\"code\":\"Category_Puppy\",\"value\":\"true\"},{\"code\":\"Food and Treats - Dog\",\"value\":\"true\"},{\"code\":\"Product_Treats\",\"value\":\"true\"},{\"code\":\"Human Grade\",\"value\":\"true\"},{\"code\":\"Non GMO\",\"value\":\"true\"},{\"code\":\"Category_Dog\",\"value\":\"true\"},{\"code\":\"Vet Approved\",\"value\":\"true\"},{\"code\":\"product_id\",\"value\":\"8224096125222\"}]},{\"code\":\"origin\",\"list\":[{\"code\":\"country\",\"value\":\"IND\"}]},{\"code\":\"veg_nonveg\",\"list\":[{\"code\":\"veg\",\"value\":\"no\"}]}],\"fulfillment_id\":\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\",\"recommended\":true,\"@ondc\\/org\\/return_window\":\"PT0S\",\"@ondc\\/org\\/available_on_cod\":false,\"category_id\":\"Pet Care\",\"related\":true,\"price\":{\"listed_value\":\"229\",\"offered_value\":\"229\",\"currency\":\"INR\",\"maximum_value\":\"229\",\"value\":\"229\"},\"matched\":true,\"time\":{\"label\":\"enable\",\"timestamp\":\"2023-11-05T18:30:00.000Z\"},\"id\":\".\\/local_retail\\/ind\\/44773286805798@preprod-wiggles.humbhionline.in.item\"},{\"@ondc\\/org\\/cancellable\":true,\"@ondc\\/org\\/returnable\":false,\"quantity\":{\"available\":{\"count\":\"99\"},\"maximum\":{\"count\":\"99\"},\"unitized\":{\"measure\":{\"unit\":\"unit\",\"computed_value\":\"0\",\"value\":\"1\",\"estimated_value\":\"0\"},\"count\":1}},\"location_ids\":[\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\"],\"@ondc\\/org\\/statutory_reqs_packaged_commodities\":{\"common_or_generic_name_of_commodity\":\"Dogs of all ages\",\"net_quantity_or_measure_of_commodity_in_pkg\":\"100 gms\",\"imported_product_country_of_origin\":\"IND\",\"manufacturer_or_packer_name\":\"Wiggles\",\"manufacturer_or_packer_address\":\"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076\",\"month_year_of_manufacture_packing_import\":\"Please refer to the packaging of the product\"},\"@ondc\\/org\\/contact_details_consumer_care\":\"Wiggles shopify store,venkatramanm@gmail.com,1234567890\",\"@ondc\\/org\\/seller_pickup_return\":false,\"@ondc\\/org\\/time_to_ship\":\"PT48H\",\"descriptor\":{\"symbol\":\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Barkstix_Lamb_Peanutbutter.png?v=1680605713\",\"images\":[\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Barkstix_Lamb_Peanutbutter.png?v=1680605713\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Barkstix_Lamb_Peanutbutter_Back.png?v=1680605713\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Lamb_peanut_USPImage.png?v=1680605713\"],\"code\":\"Barkstix Dog Treats for Training Adult & Puppies, (Lamb & Peanut Butter)\",\"name\":\"Barkstix Dog Treats for Training Adult & Puppies, (Lamb & Peanut Butter) - ( 100gm )\",\"short_desc\":\"Barkstix Dog Treats for Training Adult & Puppies, (Lamb & Peanut Butter) - ( 100gm )\",\"long_desc\":\"Barkstix Dog Treats for Training Adult & Puppies, (Lamb & Peanut Butter) - ( 100gm )\"},\"payment_ids\":[\".\\/local_retail\\/ind\\/1@preprod-wiggles.humbhionline.in.payment\"],\"rateable\":false,\"location_id\":\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\",\"fulfillment_ids\":[\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\"],\"tags\":[{\"code\":\"general_attributes\",\"list\":[{\"code\":\"All\",\"value\":\"true\"},{\"code\":\"Dog All\",\"value\":\"true\"},{\"code\":\"No Artificial Enhancers\",\"value\":\"true\"},{\"code\":\"Category_Senior\",\"value\":\"true\"},{\"code\":\"Category_Puppy\",\"value\":\"true\"},{\"code\":\"Food and Treats - Dog\",\"value\":\"true\"},{\"code\":\"Product_Treats\",\"value\":\"true\"},{\"code\":\"Human Grade\",\"value\":\"true\"},{\"code\":\"Non GMO\",\"value\":\"true\"},{\"code\":\"Category_Dog\",\"value\":\"true\"},{\"code\":\"Vet Approved\",\"value\":\"true\"},{\"code\":\"product_id\",\"value\":\"8224095338790\"}]},{\"code\":\"origin\",\"list\":[{\"code\":\"country\",\"value\":\"IND\"}]},{\"code\":\"veg_nonveg\",\"list\":[{\"code\":\"veg\",\"value\":\"no\"}]}],\"fulfillment_id\":\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\",\"recommended\":true,\"@ondc\\/org\\/return_window\":\"PT0S\",\"@ondc\\/org\\/available_on_cod\":false,\"category_id\":\"Pet Care\",\"related\":true,\"price\":{\"listed_value\":\"159\",\"offered_value\":\"159\",\"currency\":\"INR\",\"maximum_value\":\"229\",\"value\":\"159\"},\"matched\":true,\"time\":{\"label\":\"enable\",\"timestamp\":\"2023-11-05T18:30:00.000Z\"},\"id\":\".\\/local_retail\\/ind\\/44773282873638@preprod-wiggles.humbhionline.in.item\"},{\"@ondc\\/org\\/cancellable\":true,\"@ondc\\/org\\/returnable\":false,\"quantity\":{\"available\":{\"count\":\"99\"},\"maximum\":{\"count\":\"99\"},\"unitized\":{\"measure\":{\"unit\":\"unit\",\"computed_value\":\"0\",\"value\":\"1\",\"estimated_value\":\"0\"},\"count\":1}},\"location_ids\":[\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\"],\"@ondc\\/org\\/statutory_reqs_packaged_commodities\":{\"common_or_generic_name_of_commodity\":\"Dogs of all ages\",\"net_quantity_or_measure_of_commodity_in_pkg\":\"100 gms\",\"imported_product_country_of_origin\":\"IND\",\"manufacturer_or_packer_name\":\"Wiggles\",\"manufacturer_or_packer_address\":\"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076\",\"month_year_of_manufacture_packing_import\":\"Please refer to the packaging of the product\"},\"@ondc\\/org\\/contact_details_consumer_care\":\"Wiggles shopify store,venkatramanm@gmail.com,1234567890\",\"@ondc\\/org\\/seller_pickup_return\":false,\"@ondc\\/org\\/time_to_ship\":\"PT48H\",\"descriptor\":{\"symbol\":\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/BerryBlast_Front.png?v=1680605946\",\"images\":[\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/BerryBlast_Front.png?v=1680605946\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/BerryBlast-PDP_2_1.png?v=1680605946\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/BerryBlast-PDP_3_1.png?v=1680605946\"],\"code\":\"Barkstix Dog Treats for Training Adult & Puppies, 100g (Berry Blast)\",\"name\":\"Barkstix Dog Treats for Training Adult & Puppies, 100g (Berry Blast) - ( 100 g )\",\"short_desc\":\"Barkstix Dog Treats for Training Adult & Puppies, 100g (Berry Blast) - ( 100 g )\",\"long_desc\":\"Barkstix Dog Treats for Training Adult & Puppies, 100g (Berry Blast) - ( 100 g )\"},\"payment_ids\":[\".\\/local_retail\\/ind\\/1@preprod-wiggles.humbhionline.in.payment\"],\"rateable\":false,\"location_id\":\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\",\"fulfillment_ids\":[\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\"],\"tags\":[{\"code\":\"general_attributes\",\"list\":[{\"code\":\"All\",\"value\":\"true\"},{\"code\":\"Dog All\",\"value\":\"true\"},{\"code\":\"No Artificial Enhancers\",\"value\":\"true\"},{\"code\":\"Category_Senior\",\"value\":\"true\"},{\"code\":\"Category_Puppy\",\"value\":\"true\"},{\"code\":\"Food and Treats - Dog\",\"value\":\"true\"},{\"code\":\"Product_Treats\",\"value\":\"true\"},{\"code\":\"Human Grade\",\"value\":\"true\"},{\"code\":\"Non GMO\",\"value\":\"true\"},{\"code\":\"Category_Dog\",\"value\":\"true\"},{\"code\":\"Vet Approved\",\"value\":\"true\"},{\"code\":\"product_id\",\"value\":\"8224105529638\"}]},{\"code\":\"origin\",\"list\":[{\"code\":\"country\",\"value\":\"IND\"}]},{\"code\":\"veg_nonveg\",\"list\":[{\"code\":\"veg\",\"value\":\"no\"}]}],\"fulfillment_id\":\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\",\"recommended\":true,\"@ondc\\/org\\/return_window\":\"PT0S\",\"@ondc\\/org\\/available_on_cod\":false,\"category_id\":\"Pet Care\",\"related\":true,\"price\":{\"listed_value\":\"154\",\"offered_value\":\"154\",\"currency\":\"INR\",\"maximum_value\":\"229\",\"value\":\"154\"},\"matched\":true,\"time\":{\"label\":\"enable\",\"timestamp\":\"2024-01-16T17:19:43.000Z\"},\"id\":\".\\/local_retail\\/ind\\/44773310038310@preprod-wiggles.humbhionline.in.item\"},{\"@ondc\\/org\\/cancellable\":true,\"@ondc\\/org\\/returnable\":false,\"quantity\":{\"available\":{\"count\":\"99\"},\"maximum\":{\"count\":\"99\"},\"unitized\":{\"measure\":{\"unit\":\"unit\",\"computed_value\":\"0\",\"value\":\"1\",\"estimated_value\":\"0\"},\"count\":1}},\"location_ids\":[\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\"],\"@ondc\\/org\\/statutory_reqs_packaged_commodities\":{\"common_or_generic_name_of_commodity\":\"Dogs of all ages\",\"net_quantity_or_measure_of_commodity_in_pkg\":\"100 gms\",\"imported_product_country_of_origin\":\"IND\",\"manufacturer_or_packer_name\":\"Wiggles\",\"manufacturer_or_packer_address\":\"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076\",\"month_year_of_manufacture_packing_import\":\"Please refer to the packaging of the product\"},\"@ondc\\/org\\/contact_details_consumer_care\":\"Wiggles shopify store,venkatramanm@gmail.com,1234567890\",\"@ondc\\/org\\/seller_pickup_return\":false,\"@ondc\\/org\\/time_to_ship\":\"PT48H\",\"descriptor\":{\"symbol\":\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Barkstix_Chicken_Hemp_Front.png?v=1680605739\",\"images\":[\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Barkstix_Chicken_Hemp_Front.png?v=1680605739\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Barkstix_Chicken_Hemp_back.png?v=1680605739\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Barkstix_Chicken_Hemp_USPimage.png?v=1680605739\"],\"code\":\"Barkstix Dog Treats for Training Adult & Puppies, 100g (Chicken & Hemp)\",\"name\":\"Barkstix Dog Treats for Training Adult & Puppies, 100g (Chicken & Hemp) - ( 100gm )\",\"short_desc\":\"Barkstix Dog Treats for Training Adult & Puppies, 100g (Chicken & Hemp) - ( 100gm )\",\"long_desc\":\"Barkstix Dog Treats for Training Adult & Puppies, 100g (Chicken & Hemp) - ( 100gm )\"},\"payment_ids\":[\".\\/local_retail\\/ind\\/1@preprod-wiggles.humbhionline.in.payment\"],\"rateable\":false,\"location_id\":\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\",\"fulfillment_ids\":[\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\"],\"tags\":[{\"code\":\"general_attributes\",\"list\":[{\"code\":\"All\",\"value\":\"true\"},{\"code\":\"Dog All\",\"value\":\"true\"},{\"code\":\"No Artificial Enhancers\",\"value\":\"true\"},{\"code\":\"Category_Senior\",\"value\":\"true\"},{\"code\":\"Category_Puppy\",\"value\":\"true\"},{\"code\":\"Food and Treats - Dog\",\"value\":\"true\"},{\"code\":\"Product_Treats\",\"value\":\"true\"},{\"code\":\"Human Grade\",\"value\":\"true\"},{\"code\":\"Non GMO\",\"value\":\"true\"},{\"code\":\"Category_Dog\",\"value\":\"true\"},{\"code\":\"Vet Approved\",\"value\":\"true\"},{\"code\":\"product_id\",\"value\":\"8224096321830\"}]},{\"code\":\"origin\",\"list\":[{\"code\":\"country\",\"value\":\"IND\"}]},{\"code\":\"veg_nonveg\",\"list\":[{\"code\":\"veg\",\"value\":\"no\"}]}],\"fulfillment_id\":\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\",\"recommended\":true,\"@ondc\\/org\\/return_window\":\"PT0S\",\"@ondc\\/org\\/available_on_cod\":false,\"category_id\":\"Pet Care\",\"related\":true,\"price\":{\"listed_value\":\"229\",\"offered_value\":\"229\",\"currency\":\"INR\",\"maximum_value\":\"229\",\"value\":\"229\"},\"matched\":true,\"time\":{\"label\":\"enable\",\"timestamp\":\"2023-11-05T18:30:00.000Z\"},\"id\":\".\\/local_retail\\/ind\\/44773287657766@preprod-wiggles.humbhionline.in.item\"},{\"@ondc\\/org\\/cancellable\":true,\"@ondc\\/org\\/returnable\":false,\"quantity\":{\"available\":{\"count\":\"99\"},\"maximum\":{\"count\":\"99\"},\"unitized\":{\"measure\":{\"unit\":\"unit\",\"computed_value\":\"0\",\"value\":\"1\",\"estimated_value\":\"0\"},\"count\":1}},\"location_ids\":[\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\"],\"@ondc\\/org\\/statutory_reqs_packaged_commodities\":{\"common_or_generic_name_of_commodity\":\"Dogs and Cats\",\"net_quantity_or_measure_of_commodity_in_pkg\":\"100 gms\",\"imported_product_country_of_origin\":\"IND\",\"manufacturer_or_packer_name\":\"Wiggles\",\"manufacturer_or_packer_address\":\"Wiggles shopify store, , ,Pride Apartment 182 Bannerghatta Main Road Phase 4 Bilekahalli,A302, ,Bengaluru,Karnataka,IN,560076\",\"month_year_of_manufacture_packing_import\":\"Please refer to the packaging of the product\"},\"@ondc\\/org\\/contact_details_consumer_care\":\"Wiggles shopify store,venkatramanm@gmail.com,1234567890\",\"@ondc\\/org\\/seller_pickup_return\":false,\"@ondc\\/org\\/time_to_ship\":\"PT48H\",\"descriptor\":{\"symbol\":\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Artboard1_5d274230-45d1-48a8-831b-d805187a9eae.png?v=1680605768\",\"images\":[\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Artboard1_5d274230-45d1-48a8-831b-d805187a9eae.png?v=1680605768\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Artboard1copy_d643cf77-48d2-4730-b1ea-6d44bc4489cf.png?v=1680605768\",\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0739\\/2454\\/2758\\/products\\/Artboard1copy2_93e25a41-49f0-4b9f-a18c-09e59a640ed5.png?v=1680605768\"],\"code\":\"EPIBOON™ Antifungal Dusting Powder for Dogs & Cats - 100gm\",\"name\":\"EPIBOON™ Antifungal Dusting Powder for Dogs & Cats - 100gm - ( 100 g )\",\"short_desc\":\"EPIBOON™ Antifungal Dusting Powder for Dogs & Cats - 100gm - ( 100 g )\",\"long_desc\":\"EPIBOON™ Antifungal Dusting Powder for Dogs & Cats - 100gm - ( 100 g )\"},\"payment_ids\":[\".\\/local_retail\\/ind\\/1@preprod-wiggles.humbhionline.in.payment\"],\"rateable\":false,\"location_id\":\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\",\"fulfillment_ids\":[\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\"],\"tags\":[{\"code\":\"general_attributes\",\"list\":[{\"code\":\"All\",\"value\":\"true\"},{\"code\":\"Category_Cat\",\"value\":\"true\"},{\"code\":\"Fast action\",\"value\":\"true\"},{\"code\":\"Category_Puppy\",\"value\":\"true\"},{\"code\":\"Easy to use\",\"value\":\"true\"},{\"code\":\"Category_Dog\",\"value\":\"true\"},{\"code\":\"Cat All\",\"value\":\"true\"},{\"code\":\"Vet Approved\",\"value\":\"true\"},{\"code\":\"Category_Kitten\",\"value\":\"true\"},{\"code\":\"Dog All\",\"value\":\"true\"},{\"code\":\"Health & Wellness - Dog\",\"value\":\"true\"},{\"code\":\"Category_Senior\",\"value\":\"true\"},{\"code\":\"Health & Wellness - Cat\",\"value\":\"true\"},{\"code\":\"Product_Defenders\",\"value\":\"true\"},{\"code\":\"product_id\",\"value\":\"8224097337638\"}]},{\"code\":\"origin\",\"list\":[{\"code\":\"country\",\"value\":\"IND\"}]},{\"code\":\"veg_nonveg\",\"list\":[{\"code\":\"veg\",\"value\":\"no\"}]}],\"fulfillment_id\":\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\",\"recommended\":true,\"@ondc\\/org\\/return_window\":\"PT0S\",\"@ondc\\/org\\/available_on_cod\":false,\"category_id\":\"Pet Care\",\"related\":true,\"price\":{\"listed_value\":\"210\",\"offered_value\":\"210\",\"currency\":\"INR\",\"maximum_value\":\"210\",\"value\":\"210\"},\"matched\":true,\"time\":{\"label\":\"enable\",\"timestamp\":\"2023-11-05T18:30:00.000Z\"},\"id\":\".\\/local_retail\\/ind\\/44773290770726@preprod-wiggles.humbhionline.in.item\"}],\"ttl\":\"PT2M\",\"tags\":[{\"code\":\"serviceability\",\"list\":[{\"code\":\"location\",\"value\":\".\\/local_retail\\/ind\\/80777412902@preprod-wiggles.humbhionline.in.provider_location\"},{\"code\":\"category\",\"value\":\"Pet Care\"},{\"code\":\"type\",\"value\":\"10\"},{\"code\":\"val\",\"value\":\"10000.0\"},{\"code\":\"unit\",\"value\":\"km\"}]}]}],\"bpp\\/fulfillments\":[{\"id\":\".\\/local_retail\\/ind\\/2@preprod-wiggles.humbhionline.in.fulfillment\",\"rateable\":false,\"type\":\"Delivery\",\"tracking\":false}],\"bpp\\/descriptor\":{\"symbol\":\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0647\\/3662\\/4854\\/files\\/logo_2_1_fc405c5b-4db5-419a-8ec3-380144685e90.svg?v=1669615619\",\"images\":[\"https:\\/\\/cdn.shopify.com\\/s\\/files\\/1\\/0647\\/3662\\/4854\\/files\\/logo_2_1_fc405c5b-4db5-419a-8ec3-380144685e90.svg?v=1669615619\"],\"code\":\"preprod-wiggles.humbhionline.in\",\"name\":\"Wiggles shopify store\",\"short_desc\":\"Wiggles shopify store\",\"long_desc\":\"Wiggles shopify store\"}}}}";

        String key = "Vk/E8XmckN9wq/AvR3eN+R9aY+Vu2U9Hyv442onxXA8=";
        PublicKey p = Request.getSigningPublicKey(key);
        String hash = Request.generateBlakeHash(req);

        String sign = "8lq8xQx4jlE+PQEWc0lYuKScKcbY6D+VispaOrkaM+IEfW6IZ+Ja7Vi8+vjrd7fo3Xn2V5poLRGSVnyopSdnDA==";

        Signature signature = Signature.getInstance("Ed25519",BouncyCastleProvider.PROVIDER_NAME);
        signature.initVerify(p); //Verify using public key of sender.

        String signingString = getSigningString(1705483980L,1705484010L,hash);

        signature.update(signingString.getBytes(StandardCharsets.UTF_8)); // Same signing String passed.
        boolean verified = signature.verify(Base64.getDecoder().decode(sign)); //verify the signature bytes = B64Decoded value of the encoded string.
        Assert.assertTrue(verified);
    }
    @Test
    public void testSign() throws Exception{
        /*
        Two Parties generating key pair on their own servers.
         */
        KeyPair keyPairParty1 = KeyPairGenerator.getInstance("Ed25519", BouncyCastleProvider.PROVIDER_NAME).generateKeyPair();
        //KeyPair keyPairParty2 = KeyPairGenerator.getInstance("Ed25519", BouncyCastleProvider.PROVIDER_NAME).generateKeyPair();

        String signingString = "Hello Beckn";

        // At server 1
        Signature signature = Signature.getInstance("Ed25519", BouncyCastleProvider.PROVIDER_NAME); //
        signature.initSign(keyPairParty1.getPrivate());
        signature.update(signingString.getBytes(StandardCharsets.UTF_8));

        byte[] sign = signature.sign();
        String b64Sign = Base64.getEncoder().encodeToString(sign);

        // At Server 2 to verify Server 1 signature
        Signature signature2 = Signature.getInstance("Ed25519",BouncyCastleProvider.PROVIDER_NAME);
        signature.initVerify(keyPairParty1.getPublic()); //Verify using public key of sender.
        signature.update(signingString.getBytes(StandardCharsets.UTF_8)); // Same signing String passed.
        boolean verified = signature.verify(Base64.getDecoder().decode(b64Sign)); //verify the signature bytes = B64Decoded value of the encoded string.
        Assert.assertTrue(verified);

    }
    @Test
    public void testSign1() throws Exception{
        String signingString = "(created): 1645895834\n" +
                "(expires): 1645895854\n" +
                "digest: BLAKE-512=3UY7ZXVmybod2rFlj8/QFnpWoeeSuWnBeEwwOanf6pdu/aYyZAlG+Pq6k8MIdQ471W7/rwXyD7AVzAEjxeUn2g==";

        String passedSignature = "x+xy3NiokZfEIl3kY1PanTuNtGgW8XQC86ILOflR5y3Iad1s5IZ6zNmcoGc1E/CpffHHaG3DmfWyTr7Nq6X/CA==";

        String privateKey = "MFECAQEwBQYDK2VwBCIEIMChGqsZo7FbrwIK7tq6AuKiqtEPCm+rMx7cz+AsqhcFgSEATa9ExVX6Q1c1+4cdmZSFnh1rAR8F0TbIuCCzehTz5U0=";
        String signature = Request.generateSignature(signingString,privateKey);
        System.out.println("Computed:" + signature );
        System.out.println("Passed:" + passedSignature);
        //Assert.assertEquals(signature,passedSignature);

        //boolean verified = Request.verifySignature(signature, s,"MCowBQYDK2VwAyEATa9ExVX6Q1c1+4cdmZSFnh1rAR8F0TbIuCCzehTz5U0=");
        boolean verified = Request.verifySignature(signature, signingString,"Ta9ExVX6Q1c1+4cdmZSFnh1rAR8F0TbIuCCzehTz5U0=");
        Assert.assertTrue(verified);
    }

    @Test
    public void testSignRavi() throws Exception{
        String privateKey = "b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAMwAAAAtzc2gtZWQyNTUxOQAAACDC6beMrEFF7VmZK7EY7QW3LYbe8XTik/pEve02ezDa1wAAAIix3rwCsd68AgAAAAtzc2gtZWQyNTUxOQAAACDC6beMrEFF7VmZK7EY7QW3LYbe8XTik/pEve02ezDa1wAAAEBrrQ8SBkbBxNo6Vj9AWQi61Cs+dfycUG7bBXW+g67c+MLpt4ysQUXtWZkrsRjtBbctht7xdOKT+kS97TZ7MNrXAAAAAAECAwQF";

        String publicKey = "X+jgcPXHD4kFzvcbb3eaxWRA5UwB8Mm6IykeTPADofU=" ;
        String signingString = "(created): 1645897554\n" +
                "(expires): 1645897574\n" +
                "digest: BLAKE-512=SzSEdjOtuZVUOBeN6dpCXOZ4DF2kJTFDu9ejBAOneHtyQlAWLa5ravzbGf2wfCyIYAaxBDpaLoHFlCZ8gRh2UA==";

        String passedSignature = "ull+zVvFTdbORi3VCUsA+pL/fiVtf5CJ7nm/kf7Fif4AkZlHE9M9zqA3gwDiMfaTC65HSdgq26grk53bUpEOAQ==";

        String signature = Request.generateSignature(signingString,privateKey);
        System.out.println("Computed:" + signature );
        Assert.assertTrue(Request.verifySignature(signature, signingString,publicKey));
        Assert.assertTrue(Request.verifySignature(passedSignature,signingString,publicKey));
    }

    @Test
    public void generateBlakeHash() throws Exception{
        String stringToHash = "Beckn!";

        MessageDigest digest = MessageDigest.getInstance("BLAKE2B-512",BouncyCastleProvider.PROVIDER_NAME);
        digest.reset();
        digest.update(stringToHash.getBytes(StandardCharsets.UTF_8));
        byte[] hash = digest.digest();
        String bs64 = Base64.getEncoder().encodeToString(hash) ;
        System.out.println(bs64);
        System.out.println(toHex(hash)); // We could use bs64 or hex of the hash (Not sure why we need to hex and then base64 it!! Seems overkill.
        //https://8gwifi.org/MessageDigest.jsp

    }

    public String toHex(byte[] bytes){
        StringBuilder builder = new StringBuilder();
        for (int i = 0 ; i< bytes.length ; i++ ){
            String hex = Integer.toHexString(bytes[i]);
            if (hex.length() == 1){
                hex = "0"+hex;
            }
            hex = hex.substring(hex.length()-2);
            builder.append(hex);
        }
        return builder.toString();
    }
    @Test
    public void testUnderstandingBouncyCastleInternalApis()throws  Exception{
        Ed25519KeyPairGenerator pairGenerator = new Ed25519KeyPairGenerator();
        pairGenerator.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
        AsymmetricCipherKeyPair pair = pairGenerator.generateKeyPair();
        Ed25519PrivateKeyParameters privateKeyParameters = (Ed25519PrivateKeyParameters) pair.getPrivate();
        Ed25519PublicKeyParameters publicKeyParameters = (Ed25519PublicKeyParameters)pair.getPublic();

        byte[] bcBytes = publicKeyParameters.getEncoded();
        byte[] jceBytes = new SubjectPublicKeyInfo(new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519),bcBytes).getEncoded(); //Convert bcbytes to jcebytes.
        PublicKey key = getPublicKey("Ed25519",jceBytes); //Convert bc to jce bytes and generate PublicKey.

        Assert.assertArrayEquals(key.getEncoded(),jceBytes);

       //Converting java publicKey to BC publicKeyParams
        BCEdDSAPublicKey k = (BCEdDSAPublicKey) key;
        Field f = k.getClass().getSuperclass().getDeclaredField("eddsaPublicKey");
        f.setAccessible(true); //BC Desnot expose this hence this reflection stuff.
        Ed25519PublicKeyParameters publicKeyParameters1 = (Ed25519PublicKeyParameters) f.get(k);

        byte[] bcBytes2 = publicKeyParameters1.getEncoded();
        Assert.assertArrayEquals(bcBytes2,bcBytes);



        //Convert privateKeyParams to java PrivateKey
        byte[] privateJceBytes = new PrivateKeyInfo(new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519),
                new DEROctetString(privateKeyParameters.getEncoded())).getEncoded();

        System.out.println(Base64.getEncoder().encodeToString(privateJceBytes));
        PrivateKey privateKey = getPrivateKey("Ed25519",privateJceBytes);
        Assert.assertNotNull(privateKey);
        System.out.println(Crypt.getInstance().toBase64(privateKey.getEncoded()));
        PrivateKey pk = Crypt.getInstance().getPrivateKey(Request.SIGNATURE_ALGO,Crypt.getInstance().toBase64(privateKey.getEncoded()));
        Assert.assertNotNull(pk);

        //Convert java PrivateKey to privateKeyParams
        BCEdDSAPrivateKey privateKey1 = (BCEdDSAPrivateKey) privateKey;
        f = privateKey1.getClass().getSuperclass().getDeclaredField("eddsaPrivateKey");
        f.setAccessible(true); //BC Desnot expose this hence this reflection stuff.
        Ed25519PrivateKeyParameters privateKeyParameters1 = (Ed25519PrivateKeyParameters) f.get(privateKey1);

        Assert.assertArrayEquals(privateKeyParameters1.getEncoded(),privateKeyParameters.getEncoded());





    }
    @Test
    public void testWhatIsIt() throws Exception{
        // When you get some public/private key b64encoded . How to read it
        // Check if it is bcBytes

        String spublic = "3DQAZxWBPD8g2iTaaqveQyIXNT3h13cwx++b9+6yjN4=";
        String sprivate = "NaKRo2AEFoVPEEXt6AUX72GG4mq0EmMUmo1SJo38znvcNABnFYE8PyDaJNpqq95DIhc1PeHXdzDH75v37rKM3g==";
        byte[] bcBytes = Base64.getDecoder().decode(spublic);
        byte[] jceBytes = new SubjectPublicKeyInfo(new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519),bcBytes).getEncoded();
        PublicKey key = getPublicKey("Ed25519",jceBytes); //Convert bc to jce bytes and generate PublicKey.
        Assert.assertNotNull(key);

        PrivateKey privateKey = getPrivateKey("Ed25519",new PrivateKeyInfo(new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519),
                new DEROctetString(Base64.getDecoder().decode(sprivate))).getEncoded());
        Assert.assertNotNull(privateKey);
    }
    @Test
    public void testWhatIsIt2() throws Exception{
        // When you get some public/private key b64encoded . How to read it
        // Check if it is bcBytes
        Assert.assertThrows(InvalidKeySpecException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                //http://ed25519.herokuapp.com/ generated from here.
                String spublic = "3DQAZxWBPD8g2iTaaqveQyIXNT3h13cwx++b9+6yjN4=";
                String sprivate = "NaKRo2AEFoVPEEXt6AUX72GG4mq0EmMUmo1SJo38znvcNABnFYE8PyDaJNpqq95DIhc1PeHXdzDH75v37rKM3g==";
                byte[] jceBytes = Base64.getDecoder().decode(spublic);
                PublicKey key = getPublicKey("Ed25519", jceBytes); //Convert bc to jce bytes and generate PublicKey.
                //This Fails.
                Assert.assertNotNull(key);

                PrivateKey privateKey = getPrivateKey("Ed25519", Base64.getDecoder().decode(sprivate));
                Assert.assertNotNull(privateKey);
            }
        });
    }

    @Test
    public void testReadPem() throws Exception {
        //openssl genpkey -outform pem -algorithm Ed25519 -out private.pem
        //openssl pkey -in private.pem -outform PEM -pubout  -out public.pem
        String spublic = "-----BEGIN PUBLIC KEY-----\nMCowBQYDK2VwAyEAJLTgKuhfvnq998O53E0MfEFrmwK+X0CfoMzTiPK7c+U=\n-----END PUBLIC KEY-----";
        byte[] jceBytes = Base64.getDecoder().decode(spublic.
                replace("-----BEGIN PUBLIC KEY-----\n","").
                replace("\n-----END PUBLIC KEY-----",""));
        PublicKey key = getPublicKey("Ed25519", jceBytes);
        Assert.assertNotNull(key);
    }

    @Test
    public void testNirmal() throws Exception {
        String spublic = "AAAAC3NzaC1lZDI1NTE5AAAAIMLpt4ysQUXtWZkrsRjtBbctht7xdOKT+kS97TZ7MNrX";
        byte[] bytes = Base64.getDecoder().decode(spublic);
        PublicKey key  = Crypt.getInstance().getPublicKey("Ed25519",spublic);
        Assert.assertNotNull(key);

        PublicKey k2 = Request.getSigningPublicKey("X+jgcPXHD4kFzvcbb3eaxWRA5UwB8Mm6IykeTPADofU=");
        Assert.assertNotNull(k2);

    }
    @Test
    public  void testSandeep() throws  Exception{
        PublicKey key = Request.getSigningPublicKey("sVgSdujT4S+pZm/DwHC39QE/1zTRBScyuMKsqJzuAKM=");
        Assert.assertNotNull(key);
        boolean verified  = Crypt.getInstance().verifySignature("X", "v+k64uPcn8D7BcCgdE0HYh9qtdLgSelfIuBcyo3odc/jV68n0ZHIfi1ZAN/YhwIw65yKvg6fdjZuzlFei8n0Bw==", Request.SIGNATURE_ALGO,key) ;
        Assert.assertTrue(verified);

    }

    @Test
    public void testRawToPem() throws Exception {
        String pv = "RUp8sKmyp0C3IBapYH3BUeQi5GN7zcQgM6Hvn7Y4oGE=";
        String pb = "X+jgcPXHD4kFzvcbb3eaxWRA5UwB8Mm6IykeTPADofU=";

        PrivateKey privateKey = Crypt.getInstance().getPrivateKey("Ed25519", pv);
        Assert.assertNotNull(privateKey);

        PublicKey publicKey = Crypt.getInstance().getPublicKey("Ed25519",pb);
        Assert.assertNotNull(publicKey);

        System.out.println("Private : " + Crypt.getInstance().getBase64Encoded(privateKey));
        System.out.println("Public: " + Crypt.getInstance().getBase64Encoded(publicKey));

    }
    @Test
    public void testPemToRaw() throws Exception{
        String pv = "MC4CAQAwBQYDK2VwBCIEIEVKfLCpsqdAtyAWqWB9wVHkIuRje83EIDOh75+2OKBh";
        String pb = "MCowBQYDK2VwAyEAX+jgcPXHD4kFzvcbb3eaxWRA5UwB8Mm6IykeTPADofU=";

        
        PrivateKey privateKey = Crypt.getInstance().getPrivateKey("Ed25519", Base64.getDecoder().decode(pv));
        Assert.assertNotNull(privateKey);
        BCEdDSAPrivateKey privateKey1 = (BCEdDSAPrivateKey)privateKey;
        Field f = privateKey1.getClass().getSuperclass()
                .getDeclaredField("eddsaPrivateKey");
        f.setAccessible(true); //BC Desnot expose this hence this reflection stuff.
        Ed25519PrivateKeyParameters privateKeyParameters1 = (Ed25519PrivateKeyParameters) f.get(privateKey1);


        PublicKey publicKey = getPublicKey("Ed25519",Base64.getDecoder().decode(pb));
        Assert.assertNotNull(publicKey);
        BCEdDSAPublicKey publicKey1 = (BCEdDSAPublicKey)publicKey;
        f = publicKey1.getClass().getSuperclass().getDeclaredField("eddsaPublicKey");
        f.setAccessible(true); //BC Desnot expose this hence this reflection stuff.
        Ed25519PublicKeyParameters publicKeyParameters1 = (Ed25519PublicKeyParameters) f.get(publicKey1);

        String pbRaw = Base64.getEncoder().encodeToString(publicKeyParameters1.getEncoded());
        String pvRaw = Base64.getEncoder().encodeToString(privateKeyParameters1.getEncoded());
        
        String expectedPvRaw = "RUp8sKmyp0C3IBapYH3BUeQi5GN7zcQgM6Hvn7Y4oGE=";
        String expectedPbRaw = "X+jgcPXHD4kFzvcbb3eaxWRA5UwB8Mm6IykeTPADofU=";
        
        Assert.assertEquals(pbRaw,expectedPbRaw);
        Assert.assertEquals(pvRaw,expectedPvRaw);
        
        Assert.assertEquals(Crypt.getInstance().getBase64Encoded(publicKey,false),expectedPbRaw);
        Assert.assertEquals(Crypt.getInstance().getBase64Encoded(privateKey, false),expectedPvRaw);
        
        System.out.println("Private : " +pvRaw);
        System.out.println("Public: " + pbRaw);
    }

    @Test
    public void generatePrivateAndPublicKeyToRaw(){
        Ed25519KeyPairGenerator pairGenerator = new Ed25519KeyPairGenerator();
        pairGenerator.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
        AsymmetricCipherKeyPair pair = pairGenerator.generateKeyPair();
        Ed25519PrivateKeyParameters privateKeyParameters = (Ed25519PrivateKeyParameters) pair.getPrivate();
        Ed25519PublicKeyParameters publicKeyParameters = (Ed25519PublicKeyParameters)pair.getPublic();

        System.out.println("\nPrivate:" + Base64.getEncoder().encodeToString(privateKeyParameters.getEncoded()));
        System.out.println("\nPublic:" + Base64.getEncoder().encodeToString(publicKeyParameters.getEncoded()));

        Assert.assertNotNull(Crypt.getInstance().getPrivateKey("Ed25519",privateKeyParameters.getEncoded()));


    }

    @Test
    public void testEncryptionKeyFromRegistry(){
        String key = "lCI84I0Q0U0wQ/T+cxP25+a+9sK8sstBpulLa+4iqEY=" ;
        Assert.assertEquals(key,Request.getRawEncryptionKey(key));
        Assert.assertNotNull(Request.getEncryptionPublicKey(key));

        key = "5KHS4StYY8CG4H+1reZtiycN0vCkkUumvZW0tvv6OiQ=";
        Assert.assertEquals(key,Request.getRawEncryptionKey(key));
        Assert.assertNotNull(Request.getEncryptionPublicKey(key));

        key = "zMnN+2TOdCQWau2ck0hpjmifP9RhAF7EqLzDjO564Ec=";
        Assert.assertEquals(key,Request.getRawSigningKey(key));

        key = "H1qQWQzOm3grTCN/C3Lv9IRjkbb0Iqk+QT5858PfmIQ=";
        Assert.assertEquals(key,Request.getRawSigningKey(key));

    }

    @Test
    public void testDhirajCode() throws Exception{
        KeyPair keyPair1 = Crypt.getInstance(BouncyCastleProvider.PROVIDER_NAME).generateKeyPair(Request.ENCRYPTION_ALGO,Request.ENCRYPTION_ALGO_KEY_LENGTH);
        KeyPair keyPair2 = Crypt.getInstance(BouncyCastleProvider.PROVIDER_NAME).generateKeyPair(Request.ENCRYPTION_ALGO,Request.ENCRYPTION_ALGO_KEY_LENGTH);



        SecretKey k1 = getSecretKey(Crypt.getInstance().getBase64Encoded(keyPair1.getPrivate()),Crypt.getInstance().getBase64Encoded(keyPair2.getPublic()));
        SecretKey k2 = getSecretKey(Crypt.getInstance().getBase64Encoded(keyPair2.getPrivate()),Crypt.getInstance().getBase64Encoded(keyPair1.getPublic()));
        Assert.assertArrayEquals(k1.getEncoded(),k2.getEncoded());

        String e = encChallenge("Hello World",nsdlPrivateKey,otherPublicKey);
        System.out.println(e);
        String d = decChallenge(e,otherPrivateKey,nsdlPublicKey);
        Assert.assertEquals(d,"Hello World");

    }
    @Test
    public void testSandeepX25519() throws Exception{
         String  regstryKey = "/re98S+QQonKxutHTNsnfX3qbSjZrsiqZ/drbeLbhis=";
         String privateKEy = "SKROdhs7yeD4gdJi2fOWvHLA9rqYaBzty5v8k6bH/3Q=";
         //SecretKey key = getSecretKey(privateKEy,regstryKey);
         String secret = "0Kpkm/vWrCGE4cUBfO0+jiGoNjpAME43/Alg7kC3Su0MxHYqEi18iAMpX8HD3XBM";

         System.out.println(decChallenge(secret,privateKEy,regstryKey));
    }
    @Test
    public void testSandeepX25519OnRegistrySide() throws Exception{
        String publicKey = "MCowBQYDK2VuAyEA5ZnLsJiemxA+4xnZg9uA1rW9fxULelTIfUDT5CidMjQ=";
        String privateKey = "MFECAQEwBQYDK2VuBCIEICBh1xuhLhT1SqfZiLmRusAl2xESPr6/fnzl5Op7hOBsgSEA/re98S+QQonKxutHTNsnfX3qbSjZrsiqZ/drbeLbhis=";

        KeyAgreement keyAgreement = KeyAgreement.getInstance(ENC_DEC_ALGO_X25519, BouncyCastleProvider.PROVIDER_NAME);
        // atServer1.init(nsdlKeyPair.getPrivate()); // Server1 uses its private
        // key to initialize the aggreement object
        keyAgreement.init(getEncryptionPrivateKey(Base64.getDecoder().decode(privateKey))); // Server1 uses its private key to initialize the aggreement object
        // atServer1.doPhase(tcsKeyPair.getPublic(),true); //Uses Server2's
        // ppublic Key
        keyAgreement.doPhase(getEncryptionPublicKey(Base64.getDecoder().decode(publicKey)), true);

        SecretKey key = keyAgreement.generateSecret("TlsPremasterSecret");


        String sharedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println(sharedKey);
        SecretKey k2 = SecretKeyFactory.getInstance("AES").generateSecret(new SecretKeySpec(Base64.getDecoder().decode(sharedKey),"AES"));
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));

    }
    @Test
    public void testDhirajCodeDecrypt() throws Exception{
        //String publicKey = "5KHS4StYY8CG4H+1reZtiycN0vCkkUumvZW0tvv6OiQ=";
        //String nsdlPrivateKey = "IcjI/ozl+pbOWDcKlj5uXb10fPl+8oLO8mKeRLt5RZi1VwYQX2DVG9xHJGp/0LgLi979bDjBrnciAlFyr3mhWdoLe5YdOZJj5SOOSEElaHZXPotn3+r+L/4iekZuvTOREI5BXaMlryqx/1M02rEKUp4AkkMTSYNzYJ+kuDLu6P8=";
        System.out.println(Request.getRawEncryptionKey(nsdlPublicKey));
        System.out.println(decChallenge("Jw0vQimQ6cP3r3zHAxYi4A==",otherPrivateKey,nsdlPublicKey));
    }
    String nsdlPublicKey = "MCowBQYDK2VuAyEAqe/iT3XBu/3VfaH9muoQ7s6644LORdDc5KIcldN86wE=";
    String nsdlPrivateKey = "MFECAQEwBQYDK2VuBCIEIDi8orAdzNaZNJZ85xmwbSwBF3uN/Mup4R29f38IPOtsgSEAqe/iT3XBu/3VfaH9muoQ7s6644LORdDc5KIcldN86wE=";
    String otherPublicKey = "5KHS4StYY8CG4H+1reZtiycN0vCkkUumvZW0tvv6OiQ=";
    String otherPrivateKey = "MFECAQEwBQYDK2VuBCIEICDv0YL8eHXl3EDBsBF3YwEnWnxROe+bbtCzusVjkuZHgSEA5KHS4StYY8CG4H+1reZtiycN0vCkkUumvZW0tvv6OiQ="; //Was wrong! I had given encrypted private key.

    /*String nsdlPrivateKey = "MFECAQEwBQYDK2VuBCIEIKDpLFCYkoezrEjedy+wU9GhqYfPR+z8bs3C+d1NtVFMgSEAh0EblrXEhdqECkVdiTuG3ma+n9UCcfJD0wu9LS3UVhU=";
    String otherPublicKey = "MCowBQYDK2VuAyEA6l2nS9ILqzqFPc0ocWlTp/1T2LNdUazs1tusG1+9KUo=";

    String  nsdlPublicKey = "MCowBQYDK2VuAyEAh0EblrXEhdqECkVdiTuG3ma+n9UCcfJD0wu9LS3UVhU=";
    String  otherPrivateKey = "MFECAQEwBQYDK2VuBCIEIPBxDXHvCD1ZiW7X31kre/b0aYfPB+6l/zTTVuFGhLVpgSEA6l2nS9ILqzqFPc0ocWlTp/1T2LNdUazs1tusG1+9KUo=";
    */

    @Test
    public void testRegex(){
        System.out.println(Pattern.compile("\\d{15}").matcher("12345678901235").matches());
    }

    public SecretKey getSecretKey(String privateKey1, String publicKey2) throws Exception{
        System.out.println(String.format("Private:%s\n" ,privateKey1));
        System.out.println(String.format("OtherPublic:%s\n" ,publicKey2));

        KeyAgreement keyAgreement = KeyAgreement.getInstance(ENC_DEC_ALGO_X25519, BouncyCastleProvider.PROVIDER_NAME);
        keyAgreement.init(getEncryptionPrivateKey(Base64.getDecoder().decode(privateKey1))); // Server1 uses its private key to initialize the aggreement object
        keyAgreement.doPhase(getEncryptionPublicKey(Base64.getDecoder().decode(publicKey2)), true); // Uses Server2's ppublic Key
        SecretKey key1 = keyAgreement.generateSecret("TlsPremasterSecret "); // derive secret at server 1. "TlsPremasterSecret" is the algorithm for secret key. It is an aes key actually.
        return key1;
    }
    public static final String ENC_DEC_ALGO_X25519 = Request.ENCRYPTION_ALGO;
    public static String encChallenge(String cs, String nsdlPrivateKey, String otherPublicKey) throws Exception
    {
        KeyAgreement keyAgreement = KeyAgreement.getInstance(ENC_DEC_ALGO_X25519, BouncyCastleProvider.PROVIDER_NAME);
        // atServer1.init(nsdlKeyPair.getPrivate()); // Server1 uses its private
        // key to initialize the aggreement object
        keyAgreement.init(getEncryptionPrivateKey(Base64.getDecoder().decode(nsdlPrivateKey))); // Server1 uses its private key to initialize the aggreement object
        // atServer1.doPhase(tcsKeyPair.getPublic(),true); //Uses Server2's
        // ppublic Key
        keyAgreement.doPhase(getEncryptionPublicKey(Base64.getDecoder().decode(otherPublicKey)), true); // Uses Server2's ppublic Key
        SecretKey key1 = keyAgreement.generateSecret("TlsPremasterSecret"); // derive secret at server 1. "TlsPremasterSecret" is the algorithm for secret key. It is an aes key actually.
        // *Server1
        Cipher cipher1 = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
        cipher1.init(Cipher.ENCRYPT_MODE, key1);
        byte[] encrypted1 = cipher1.doFinal(cs.getBytes(StandardCharsets.UTF_8));
        String b64Encryped1 = Base64.getEncoder().encodeToString(encrypted1);
        System.out.println("Encrypted : " + b64Encryped1);
        return b64Encryped1;
    }


    public static String decChallenge(String cs, String otherPrivateKey, String nsdlPublicKey) throws  Exception
    {
        //KeyAgreement ka = KeyAgreement.getInstance(ENC_DEC_ALGO_X25519, BouncyCastleProvider.PROVIDER_NAME);
        //ka.init(getPrivateKey(ENC_DEC_ALGO_X25519, Base64.getDecoder().decode(otherPrivateKey)));
        //ka.doPhase(getPublicKey(ENC_DEC_ALGO_X25519, Base64.getDecoder().decode(nsdlPublicKey)), true);


        KeyAgreement keyAgreement = KeyAgreement.getInstance(ENC_DEC_ALGO_X25519, BouncyCastleProvider.PROVIDER_NAME);
        // atServer1.init(nsdlKeyPair.getPrivate()); // Server1 uses its private
        // key to initialize the aggreement object
        keyAgreement.init(getEncryptionPrivateKey(Base64.getDecoder().decode(otherPrivateKey))); // Server1 uses its private key to initialize the aggreement object
        // atServer1.doPhase(tcsKeyPair.getPublic(),true); //Uses Server2's
        // ppublic Key
        keyAgreement.doPhase(getEncryptionPublicKey(Base64.getDecoder().decode(nsdlPublicKey)), true);

        SecretKey key2 = keyAgreement.generateSecret("TlsPremasterSecret");
        Cipher cipher2 = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
        cipher2.init(Cipher.DECRYPT_MODE, key2);
        byte[] decrypted2 = cipher2.doFinal(Base64.getDecoder().decode(cs));
        //System.out.println("" + new String(decrypted2));
        //System.out.println(String.valueOf(decrypted2));
        return new String(decrypted2);
    }

    public static PublicKey getEncryptionPublicKey(byte[] bytes){
        try {
            return getPublicKey(ENC_DEC_ALGO_X25519,bytes);
        }catch (Exception ex){
            try {
                byte[] jceBytes = new SubjectPublicKeyInfo(new AlgorithmIdentifier(EdECObjectIdentifiers.id_X25519), bytes).getEncoded();
                return getPublicKey(ENC_DEC_ALGO_X25519, jceBytes);
            }catch (Exception jceEx){
                return null;
            }
        }
    }
    public static PrivateKey getEncryptionPrivateKey(byte[] bytes) {
        try {
            return getPrivateKey(ENC_DEC_ALGO_X25519,bytes);
        }catch (Exception ex){
            try {
                X25519PrivateKeyParameters privateKeyParameters = new X25519PrivateKeyParameters(bytes,0);
                byte[] jceBytes = new PrivateKeyInfo(new AlgorithmIdentifier(EdECObjectIdentifiers.id_X25519),
                        new DEROctetString(privateKeyParameters.getEncoded())).getEncoded();
                return getPrivateKey(ENC_DEC_ALGO_X25519,jceBytes);
            }catch (Exception jceEx){
                return null;
            }
        }


    }


    public static PublicKey getPublicKey(String algo, byte[] jceBytes) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(jceBytes);
        PublicKey key = KeyFactory.getInstance(algo, BouncyCastleProvider.PROVIDER_NAME)
                .generatePublic(x509EncodedKeySpec);
        return key;
    }
    public static PrivateKey getPrivateKey(String algo, byte[] jceBytes) throws Exception {
        PrivateKey key = KeyFactory.getInstance(algo, BouncyCastleProvider.PROVIDER_NAME)
                .generatePrivate(new PKCS8EncodedKeySpec(jceBytes));
        return key;
    }

    @Test
    public void testHashicorpVault(){
        String sign =  "0U0IVLl6qpy3SW7ginB3o/H1/dS1eg6Dxnu/Rqdyxlrr5+BDQEsmCyJwIXTmeNEumLQvi3Wkg2sXPck0ngGfCg==" ;//"pn5lfZ0qw41hgTayfol5HOiIffJLUdl0zmGUPjyBvZXvbEzyxYnvQxEdu5R3h9c6LEnJ1BGhydeXcaMkAP2lBw==";
        //String sign = "r3jezKeH2CSnOeS6R45CEiatM63Wd2q2PXukfPEjPHZworPdTV3ItN8iHojP09DoozN8eNC7pjbZHUcRf2hfAQ==";
        //String sign = "r3jezKeH2CSnOeS6R45CEiatM63Wd2q2PXukfPEjPHZworPdTV3ItN8iHojP09DoozN8eNC7pjbZHUcRf2hfAQ==";
        String publicKey = "M9t8uPpYwaSPk/nk03t3rJ2pMbUwwRC6+wiF7923MYE=";
        String signingString = "XX\n";
        Assert.assertTrue(Crypt.getInstance(BouncyCastleProvider.   PROVIDER_NAME).verifySignature(signingString,sign,Request.SIGNATURE_ALGO, Request.getSigningPublicKey(publicKey)));


    }

    @Test
    public void testTimestamp(){
        Context context = new Context();
        context.set("timestamp",new Date(),BecknObject.TIMESTAMP_FORMAT_WITH_MILLS);
        context.setTtl(10L);
        Request r = new Request();
        r.setContext(context);

        System.out.println(r.toString());
        r = new Request(r.toString());
        System.out.println(r.getContext().getTtl());


    }

    @Test
    public void testnsdl()throws Exception{
        String key     = "ONtluFKk9FfVIePk0FJjCZE1XlTdcXtcWWRjqzllVSw=";
        String jsonText = "{\"context\":{\"transaction_id\":\"9f010135-ace0-4c20-b749-3181d6fa7826\",\"country\":\"IND\",\"bpp_id\":\"becknify.humbhionline.in.local_retail.BPP\\/ondc\\/app1-magnolabs-in\",\"city\":\"std:022\",\"message_id\":\"e154537f-4bf6-4f84-a48d-715d4656a931\",\"ttl\":\"PT30S\",\"core_version\":\"1.0.0\",\"bap_id\":\"buyer-app.ondc.org\",\"domain\":\"nic2004:52110\",\"bpp_uri\":\"https:\\/\\/becknify.humbhionline.in\\/local_retail\\/ondc\\/app1-magnolabs-in\\/bpp\",\"action\":\"on_search\",\"bap_uri\":\"https:\\/\\/buyer-app.ondc.org\\/protocol\\/v1\",\"timestamp\":\"2023-01-03T06:52:21.026Z\"},\"message\":{\"catalog\":{\"bpp\\/providers\":[{\"@ondc\\/org\\/fssai_license_no\":\"12345678901234\",\"id\":\"ondcconnect.myshopify.com\",\"descriptor\":{\"name\":\"ONDC Connect Test Store\",\"short_desc\":\"ONDC Connect Test Store\"},\"rateable\":false,\"items\":[]}],\"bpp\\/descriptor\":{\"short_desc\":\"ONDC Connect Test Store\",\"long_desc\":\"ONDC Connect Test Store\"}}}}";
        String sign = "Az+DYUOHcQcsg2MupkTiJWO97VhyhUr2ZxF+TBlAlVU3BhcRGM+rJFMJHTV3g60F8cnqLdeVK8Tqpt2KL0fKAg==";

        String signingString = "(created): 1672728743\n" +
                "(expires): 1672728773\n" +
                "digest: BLAKE-512=kHBAd/BM+Cqxhn71oKtuTE7tpqMyaibskU6P5CqHO+KzPzlIgyL5YU/Ps6/maa6KOCf5G/4nX0nx8vRELZxZCQ==";

        Ed25519PublicKeyParameters pubKey = new Ed25519PublicKeyParameters(Base64.getDecoder().decode(key),0);
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(false,pubKey);
        byte[] signingBytes =  signingString.getBytes(StandardCharsets.UTF_8);
        signer.update(signingBytes,0,signingBytes.length);
        Assert.assertTrue(signer.verifySignature(Base64.getDecoder().decode(sign)));
    }

    @Test
    public void testChaiPoint() throws  Exception{
        String publicKey = "9xyCGZ8jpJE6vNSpHAMisJ1bx26aHeDw/Wccy6k+6nk=";
        Map<String,String> headers = new HashMap<>(){{
            put("Authorization","Signature keyId=\"ondc.chaipoint.com|386|ed25519\",algorithm=\"ed25519\",created=\"1678820623\",expires=\"1678822623\",headers=\"(created) (expires) digest\",signature=\"6xyRIxgsyTfSCFeRwJT/USqiNrJjdw1v52Sa2oASKMMAanXTPpXzWvhYZWbYPUbOgAHcygshsLWiO/5HicEPBw==\"");
        }} ;
        String payload = "{\"request\":{\"context\":{\"domain\":\"nic2004:52110\",\"country\":\"IND\",\"city\":\"std:080\",\"action\":\"on_search\",\"core_version\":\"1.1.0\",\"bap_id\":\"buyer-app-preprod.ondc.org\",\"bap_uri\":\"https://buyer-app-preprod.ondc.org/protocol/v1/on_search\",\"bpp_id\":\"ondc.chaipoint.com\",\"bpp_uri\":\"https://ondc.chaipoint.com/\",\"transaction_id\":\"6f3e78ca-fcc2-4575-927b-0d4206053fb5\",\"message_id\":\"dd22c3be-63bd-4f6d-83f8-7cbc955967bb\",\"timestamp\":\"2023-03-14T19:03:43.322Z\",\"ttl\":\"PT30S\"},\"message\":{\"catalog\":{\"bpp/descriptor\":{\"name\":\"Chai Point\",\"symbol\":\"https://images.fountain.cool/logo/CP_Teal_Logo_bg_new.png\",\"short_desc\":\"Chai Point for ONDC\",\"long_desc\":\"Chai Point is an Indian tea company and a cafe chain which focuses on tea-based beverages. As of October, 2017, the company also started selling their teas on Amazon.in, making it their first foray into distribution via the e-commerce platform.\",\"images\":[\"https://images.fountain.cool/logo/CP_Teal_Logo_bg_new.png\"]},\"bpp/providers\":[{\"id\":\"108\",\"descriptor\":{\"name\":\"Chaipoint Forum Mall\",\"code\":\"Forum Mall\",\"symbol\":\"Chai Point\",\"short_desc\":\"Forum Mall\",\"long_desc\":\"Chaipoint Forum Mall\",\"images\":[\"https://images.fountain.cool/logo/CP_Teal_Logo_bg_new.png\"]},\"category_id\":\"F&B\",\"locations\":[{\"id\":\"1063\",\"gps\":\"12.9342565,77.6043993\",\"address\":{\"name\":\"Chaipoint Forum Mall\",\"locality\":\"2nd Floor, Hosur Rd, 7th Block, Koramangala\",\"city\":\"Bangalore\",\"area_code\":\"560029\",\"street\":\"Koramangala\"}}],\"items\":[{\"id\":\"1046\",\"descriptor\":{\"name\":\"The Chennai Express\",\"code\":\"M5235\",\"symbol\":\"https://product-catalogue-itemimage-upload-prod.s3.ap-south-1.amazonaws.com/2020-09-07_14%3A10%3A09_The_Chennai_Express.jpg\",\"short_desc\":\"The Chennai Express\",\"long_desc\":\"Filter coffee Uniflask + Vada Pav + Samosa\",\"images\":[\"https://product-catalogue-itemimage-upload-prod.s3.ap-south-1.amazonaws.com/2020-09-07_14%3A10%3A09_The_Chennai_Express.jpg\"]},\"price\":{\"currency\":\"INR\",\"value\":\"179.0\",\"maximum_value\":\"179.0\"},\"category_id\":\"F&B\",\"location_id\":\"1063\",\"tags\":{\"veg\":\"yes\",\"non_veg\":\"no\"},\"quantity\":{\"available\":{\"count\":\"99.0\"},\"maximum\":{\"count\":\"30\"}},\"fullfillment_id\":\"1\",\"@ondc/org/returnable\":false,\"@ondc/org/seller_pickup_return\":false,\"@ondc/org/return_window\":\"PT30M\",\"@ondc/org/cancellable\":false,\"@ondc/org/available_on_cod\":true,\"@ondc/org/time_to_ship\":\"P45M\"},{\"id\":\"831\",\"descriptor\":{\"name\":\"Tomato Pasta + Cold Coffee\",\"code\":\"M5034\",\"symbol\":\"https://menu-img.fountain.cool/M5034.jpg\",\"short_desc\":\"Tomato Pasta + Cold Coffee\",\"long_desc\":\"Tomato Pasta with Cold Coffee\",\"images\":[\"https://menu-img.fountain.cool/M5034.jpg\"]},\"price\":{\"currency\":\"INR\",\"value\":\"240.0\",\"maximum_value\":\"240.0\"},\"category_id\":\"F&B\",\"location_id\":\"1063\",\"tags\":{\"veg\":\"yes\",\"non_veg\":\"no\"},\"quantity\":{\"available\":{\"count\":\"0\"},\"maximum\":{\"count\":\"30\"}},\"fullfillment_id\":\"1\",\"@ondc/org/returnable\":false,\"@ondc/org/seller_pickup_return\":false,\"@ondc/org/return_window\":\"PT30M\",\"@ondc/org/cancellable\":false,\"@ondc/org/available_on_cod\":true,\"@ondc/org/time_to_ship\":\"P45M\"},{\"id\":\"169\",\"descriptor\":{\"name\":\"Cold Coffee\",\"code\":\"M3400\",\"symbol\":\"https://images-qa.fountain.cool/product/1/12-39-07-images.jpeg\",\"short_desc\":\"Cold Coffee\",\"long_desc\":\"Cold Coffee\",\"images\":[\"https://images-qa.fountain.cool/product/1/12-39-07-images.jpeg\"]},\"price\":{\"currency\":\"INR\",\"value\":\"103.81\",\"maximum_value\":\"103.81\"},\"category_id\":\"F&B\",\"location_id\":\"1063\",\"tags\":{\"veg\":\"yes\",\"non_veg\":\"no\"},\"quantity\":{\"available\":{\"count\":\"0\"},\"maximum\":{\"count\":\"30\"}},\"fullfillment_id\":\"1\",\"@ondc/org/returnable\":false,\"@ondc/org/seller_pickup_return\":false,\"@ondc/org/return_window\":\"PT30M\",\"@ondc/org/cancellable\":false,\"@ondc/org/available_on_cod\":true,\"@ondc/org/time_to_ship\":\"P45M\"}],\"ttl\":\"PT30S\",\"@ondc/org/fssai_license_no\":\"TEST_FSSAI2\"},{\"id\":\"76\",\"descriptor\":{\"name\":\"Chaipoint Koramangala 3\",\"code\":\"Koramangala 3\",\"symbol\":\"Chai Point\",\"short_desc\":\"Koramangala 3\",\"long_desc\":\"Chaipoint Koramangala 3\",\"images\":[\"https://images.fountain.cool/logo/CP_Teal_Logo_bg_new.png\"]},\"category_id\":\"F&B\",\"locations\":[{\"id\":\"839\",\"gps\":\"12.930798,77.632912\",\"address\":{\"name\":\"Chaipoint Koramangala 3\",\"locality\":\"#1025, 80 feet road, 7th main road, 1st block Koramangala,\",\"city\":\"Bangalore\",\"area_code\":\"560034\",\"street\":\"1st block Koramangala,\"}}],\"items\":[{\"id\":\"1046\",\"descriptor\":{\"name\":\"The Chennai Express\",\"code\":\"M5235\",\"symbol\":\"https://product-catalogue-itemimage-upload-prod.s3.ap-south-1.amazonaws.com/2020-09-07_14%3A10%3A09_The_Chennai_Express.jpg\",\"short_desc\":\"The Chennai Express\",\"long_desc\":\"Filter coffee Uniflask + Vada Pav + Samosa\",\"images\":[\"https://product-catalogue-itemimage-upload-prod.s3.ap-south-1.amazonaws.com/2020-09-07_14%3A10%3A09_The_Chennai_Express.jpg\"]},\"price\":{\"currency\":\"INR\",\"value\":\"179.0\",\"maximum_value\":\"179.0\"},\"category_id\":\"F&B\",\"location_id\":\"839\",\"tags\":{\"veg\":\"yes\",\"non_veg\":\"no\"},\"quantity\":{\"available\":{\"count\":\"999999.0\"},\"maximum\":{\"count\":\"30\"}},\"fullfillment_id\":\"1\",\"@ondc/org/returnable\":false,\"@ondc/org/seller_pickup_return\":false,\"@ondc/org/return_window\":\"PT30M\",\"@ondc/org/cancellable\":false,\"@ondc/org/available_on_cod\":true,\"@ondc/org/time_to_ship\":\"P45M\"},{\"id\":\"1061\",\"descriptor\":{\"name\":\"Snacks Combo 2\",\"code\":\"M5250\",\"symbol\":\"https://images.fountain.cool/product/1/23-02-10-Whatsapp%20Snacks%202.jpg\",\"short_desc\":\"Snacks Combo 2\",\"long_desc\":\"Ginger Lemon Chai Mini flask + Cold Filter Coffee + 2 Freshly made Bun Omelette + 2 Samosa + Vada Pav\",\"images\":[\"https://images.fountain.cool/product/1/23-02-10-Whatsapp%20Snacks%202.jpg\"]},\"price\":{\"currency\":\"INR\",\"value\":\"519.0\",\"maximum_value\":\"519.0\"},\"category_id\":\"F&B\",\"location_id\":\"839\",\"tags\":{\"veg\":\"no\",\"non_veg\":\"yes\"},\"quantity\":{\"available\":{\"count\":\"999999.0\"},\"maximum\":{\"count\":\"30\"}},\"fullfillment_id\":\"1\",\"@ondc/org/returnable\":false,\"@ondc/org/seller_pickup_return\":false,\"@ondc/org/return_window\":\"PT30M\",\"@ondc/org/cancellable\":false,\"@ondc/org/available_on_cod\":true,\"@ondc/org/time_to_ship\":\"P45M\"},{\"id\":\"831\",\"descriptor\":{\"name\":\"Tomato Pasta + Cold Coffee\",\"code\":\"M5034\",\"symbol\":\"https://menu-img.fountain.cool/M5034.jpg\",\"short_desc\":\"Tomato Pasta + Cold Coffee\",\"long_desc\":\"Tomato Pasta with Cold Coffee\",\"images\":[\"https://menu-img.fountain.cool/M5034.jpg\"]},\"price\":{\"currency\":\"INR\",\"value\":\"240.0\",\"maximum_value\":\"240.0\"},\"category_id\":\"F&B\",\"location_id\":\"839\",\"tags\":{\"veg\":\"yes\",\"non_veg\":\"no\"},\"quantity\":{\"available\":{\"count\":\"999999.0\"},\"maximum\":{\"count\":\"30\"}},\"fullfillment_id\":\"1\",\"@ondc/org/returnable\":false,\"@ondc/org/seller_pickup_return\":false,\"@ondc/org/return_window\":\"PT30M\",\"@ondc/org/cancellable\":false,\"@ondc/org/available_on_cod\":true,\"@ondc/org/time_to_ship\":\"P45M\"},{\"id\":\"169\",\"descriptor\":{\"name\":\"Cold Coffee\",\"code\":\"M3400\",\"symbol\":\"https://images-qa.fountain.cool/product/1/12-39-07-images.jpeg\",\"short_desc\":\"Cold Coffee\",\"long_desc\":\"Cold Coffee\",\"images\":[\"https://images-qa.fountain.cool/product/1/12-39-07-images.jpeg\"]},\"price\":{\"currency\":\"INR\",\"value\":\"160.0\",\"maximum_value\":\"160.0\"},\"category_id\":\"F&B\",\"location_id\":\"839\",\"tags\":{\"veg\":\"yes\",\"non_veg\":\"no\"},\"quantity\":{\"available\":{\"count\":\"98.0\"},\"maximum\":{\"count\":\"30\"}},\"fullfillment_id\":\"1\",\"@ondc/org/returnable\":false,\"@ondc/org/seller_pickup_return\":false,\"@ondc/org/return_window\":\"PT30M\",\"@ondc/org/cancellable\":false,\"@ondc/org/available_on_cod\":true,\"@ondc/org/time_to_ship\":\"P45M\"}],\"ttl\":\"PT30S\",\"@ondc/org/fssai_license_no\":\"TEST_FSSAI1\"},{\"id\":\"971\",\"descriptor\":{\"name\":\"No terminal\",\"code\":\"No terminal\",\"symbol\":\"Chai Point\",\"short_desc\":\"No terminal\",\"long_desc\":\"No terminal\",\"images\":[\"https://images.fountain.cool/logo/CP_Teal_Logo_bg_new.png\"]},\"category_id\":\"F&B\",\"locations\":[{\"id\":\"5435\",\"gps\":\"12.9342565,77.6043993\",\"address\":{\"name\":\"No terminal\",\"locality\":\"kormangalakormangala\",\"city\":\"Bangalore\",\"area_code\":\"560029\",\"street\":\"kormangala\"}}],\"items\":[{\"id\":\"1174\",\"descriptor\":{\"name\":\"Coffee\",\"code\":\"MC0022\",\"symbol\":\"https://images-qa.fountain.cool/product/7/16-55-02-clovetea.jpeg\",\"short_desc\":\"Coffee\",\"long_desc\":\"Filter coffee\",\"images\":[\"https://images-qa.fountain.cool/product/7/16-55-02-clovetea.jpeg\"]},\"price\":{\"currency\":\"INR\",\"value\":\"20.0\",\"maximum_value\":\"20.0\"},\"category_id\":\"F&B\",\"location_id\":\"5435\",\"tags\":{\"veg\":\"yes\",\"non_veg\":\"no\"},\"quantity\":{\"available\":{\"count\":\"0\"},\"maximum\":{\"count\":\"30\"}},\"fullfillment_id\":\"1\",\"@ondc/org/returnable\":false,\"@ondc/org/seller_pickup_return\":false,\"@ondc/org/return_window\":\"PT30M\",\"@ondc/org/cancellable\":false,\"@ondc/org/available_on_cod\":true,\"@ondc/org/time_to_ship\":\"P45M\"}],\"ttl\":\"PT30S\",\"@ondc/org/fssai_license_no\":\"TEST_FSSAI3\"}],\"bpp/fulfillments\":[{\"id\":\"1\",\"type\":\"Delivery\"}]}}}\n}";
        Request r =new Request(payload);
        Map<String,String> params = r.extractAuthorizationParams("Authorization",headers);

        String signature = params.get("signature");
        String created = params.get("created");
        String expires = params.get("expires");
        String keyId = params.get("keyId");
        String subscriberId = params.get("subscriber_id");
        String pub_key_id  = params.get("pub_key_id");


        String signingString = r.getSigningString(Long.parseLong(created),Long.parseLong(expires));


        Assert.assertTrue(Request.verifySignature(signature,signingString,publicKey));

    }

    @Test
    public void testnsdlJan82023()throws Exception{
        String key     = "ONtluFKk9FfVIePk0FJjCZE1XlTdcXtcWWRjqzllVSw=";
        String jsonText = "{\"context\":{\"transaction_id\":\"cf40d775-397a-401a-a0db-028cb7a0dd09\",\"country\":\"IND\",\"bpp_id\":\"becknify.humbhionline.in.local_retail.BPP/ondc/app1-magnolabs-in\",\"city\":\"std:080\",\"message_id\":\"f88f3744-3316-45de-a9f8-b8e18d45efd5\",\"core_version\":\"1.0.0\",\"ttl\":\"PT30S\",\"bap_id\":\"buyer-app.ondc.org\",\"domain\":\"nic2004:52110\",\"bpp_uri\":\"https://becknify.humbhionline.in/local_retail/ondc/app1-magnolabs-in/bpp\",\"action\":\"on_search\",\"bap_uri\":\"https://buyer-app.ondc.org/protocol/v1\",\"timestamp\":\"2023-01-08T06:43:43.439Z\"},\"message\":{\"catalog\":{\"bpp/providers\":[{\"fssai_license_no\":\"12345678901234\",\"id\":\"ondcconnect.myshopify.com\",\"descriptor\":{\"name\":\"ONDC Connect Test Store\",\"short_desc\":\"ONDC Connect Test Store\"},\"items\":[{\"@ondc/org/cancellable\":true,\"@ondc/org/returnable\":true,\"@ondc/org/seller_pickup_return\":true,\"@ondc/org/contact_details_consumer_care\":\"1212121212\",\"@ondc/org/time_to_ship\":\"12PTH\",\"descriptor\":{\"images\":[\"https://cdn.shopify.com/s/files/1/0664/8034/1217/products/Double-Chocolate-Walnut-Cake-Mix-Cookies-4.jpg?v=1663683413\"],\"name\":\"Chocolate biscuit\",\"short_desc\":\"Double Chocolate Walnut Cookies\",\"long_desc\":\"Double Chocolate Walnut Cookies\"},\"rateable\":true,\"location_id\":\"-1\",\"fulfillment_id\":\"-1\",\"recommended\":true,\"@ondc/org/return_window\":\"TBD\",\"@ondc/org/available_on_cod\":true,\"category_id\":\"-1\",\"related\":true,\"price\":{\"currency\":\"INR\",\"value\":\"340.00\"},\"matched\":true,\"id\":\"43320248860897\"}]}],\"bpp/descriptor\":{\"short_desc\":\"ONDC Connect Test Store\",\"long_desc\":\"ONDC Connect Test Store\"}}}}";
        String sign = "Vl6OIxw1TmR5SEreUFSkT9ESoGP8M5YX5zQXyR5XOlwmYNv6JGo2KIwHxWkHfmBncoP+aRcKibI8E3uljPpgAQ==";

        String signingString = "(created): 1673160228\n" +
                "(expires): 1673160258\n" +
                "digest: BLAKE-512=SpFz7itdhVJIdo+eIVQ0IJRQ5q+5NE5Np+6UhniKxSGKuOU2FbPQV5hU3JFmvWPjj3J6Prh24jKLzUjnG8l7Cw==";
        //signingString = new Request(jsonText).getSigningString(1673160228L,1673160258L);

        Ed25519PublicKeyParameters pubKey = new Ed25519PublicKeyParameters(Base64.getDecoder().decode(key),0);
        Ed25519PrivateKeyParameters privateKeyParameters = new Ed25519PrivateKeyParameters(Base64.getDecoder().decode("jW0MxDzdqbJTJAsskUl8Gx1JZLCPVgu1Ij7McmvBLrw="),0);

        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true,privateKeyParameters);

        byte[] signingBytes =  signingString.getBytes(StandardCharsets.UTF_8);

        signer.update(signingBytes,0,signingBytes.length);
        Assert.assertEquals(Base64.getEncoder().encodeToString(signer.generateSignature()),sign);

        signer.init(false,pubKey);



        signer.update(signingBytes,0,signingBytes.length);
        Assert.assertTrue(signer.verifySignature(Base64.getDecoder().decode(sign)));
    }
    @Test
    public void testPrivatekey() throws Exception{
        String pv = "MFECAQEwBQYDK2VwBCIEII1tDMQ83amyUyQLLJFJfBsdSWSwj1YLtSI+zHJrwS68gSEAONtluFKk9FfVIePk0FJjCZE1XlTdcXtcWWRjqzllVSw=";
        PrivateKey privateKey = Crypt.getInstance().getPrivateKey(Request.SIGNATURE_ALGO,pv);
        Assert.assertNotNull(privateKey);

        //Convert java PrivateKey to privateKeyParams
        BCEdDSAPrivateKey privateKey1 = (BCEdDSAPrivateKey) privateKey;
        Field f = privateKey1.getClass().getSuperclass().getDeclaredField("eddsaPrivateKey");
        f.setAccessible(true); //BC Desnot expose this hence this reflection stuff.
        Ed25519PrivateKeyParameters privateKeyParameters1 = (Ed25519PrivateKeyParameters) f.get(privateKey1);


        byte[] privateJceBytes = new PrivateKeyInfo(new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519),
                new DEROctetString(privateKeyParameters1.getEncoded())).getEncoded();

        Assert.assertNotNull(Crypt.getInstance().getPrivateKey(Request.SIGNATURE_ALGO,pv));
        System.out.println(Base64.getEncoder().encodeToString(privateJceBytes));

        System.out.println(Base64.getEncoder().encodeToString(privateKeyParameters1.getEncoded()));

    }

    @Test
    public void testSignBA(){
        String key     = "lqegf0O1Ok+FPEWqaAH9OswRldrH6ClQ2b895TwHucc=";

        Ed25519PublicKeyParameters pubKey = new Ed25519PublicKeyParameters(Base64.getDecoder().decode(key),0);

        Ed25519Signer signer = new Ed25519Signer();

        String payload = "{\"context\": {\"domain\": \"nic2004:52110\", \"country\": \"IND\", \"city\": \"std:080\", \"action\": \"select\", \"core_version\": \"1.0.0\", \"bap_id\": \"buyer-app.ondc.org\", \"bap_uri\": \"https://buyer-app.ondc.org/protocol/v1\", \"bpp_uri\": \"https://z1o2ovg69j.execute-api.ap-south-1.amazonaws.com/QA6/\", \"transaction_id\": \"1aed2dad-3eb0-48da-b905-18777c6c29dd\", \"message_id\": \"7721241e-b821-4e67-9856-a5286e68dbc6\", \"timestamp\": \"2023-01-11T18:56:24.867Z\", \"bpp_id\": \"testing.chaipoint.com\", \"ttl\": \"PT30S\"}, \"message\": {\"order\": {\"items\": [{\"id\": \"169\", \"quantity\": {\"count\": 1}}], \"provider\": {\"id\": \"108\", \"locations\": [{\"id\": \"1063\"}]}, \"fulfillments\": [{\"end\": {\"location\": {\"gps\": \"12.935532, 77.6244960000001\", \"address\": {\"area_code\": \"560095\"}}}}]}}}";

        String signingString = new Request(payload).getSigningString(1673463384L,1673466984L);
        byte[] signingBytes =  signingString.getBytes(StandardCharsets.UTF_8);

        String sign = "5X09CW8MecsKbtUqlNKraei2mz2iDAcyC3sdUF8iWsUfZBKSwenscF5oieeEtVOSx8aT4sIM06VdpGCWmTbVAA==";

        signer.init(false,pubKey);
        signer.update(signingBytes,0,signingBytes.length);
        Assert.assertTrue(signer.verifySignature(Base64.getDecoder().decode(sign)));
    }

    @Test
    public void testAdyaKey() throws Exception{
        String key = "Fe/QcJJIR/lu3CUWNp/tz1HwWYaRLedMD8XvuU/A2w8=";
        PublicKey pKey = Request.getSigningPublicKey(key);
        Signature s = Signature.getInstance(Request.SIGNATURE_ALGO,BouncyCastleProvider.PROVIDER_NAME);
        s.initVerify(pKey);
    }
}
