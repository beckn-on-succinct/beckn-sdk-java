package in.succinct.beckn;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class    Descriptor extends BecknObject implements ImagesHolder{
    public static class Descriptors extends BecknObjects<Descriptor>{
        public Descriptors() {
        }

        public Descriptors(JSONArray value) {
            super(value);
        }

        public Descriptors(String payload) {
            super(payload);
        }
    }

    public Descriptor(){
        super();
    }

    public String getName(){
        return get("name");
    }
    public void setName(String name){
        set("name",name);
    }

    public String getCode(){
        return get("code");
    }
    public void setCode(String code){
        set("code",code);
    }

    public String getSymbol(){
        return get("symbol");
    }
    public void setSymbol(String symbol){
        set("symbol",symbol);
    }
    public String getShortDesc(){
        return get("short_desc");
    }
    public void setShortDesc(String short_desc){
        set("short_desc",short_desc);
    }
    public String getLongDesc(){
        return get("long_desc");
    }
    public void setLongDesc(String long_desc){
        set("long_desc",long_desc);
    }
    public Images getImages(){
        return ImagesHolder.super.getImages();
    }
    public void setImages(Images images){
        ImagesHolder.super.setImages(images);
    }

    public String getAudio(){
        return get("audio");
    }
    public void setAudio(String audio){
        set("audio",audio);
    }

    public String get3dRender(){
        return get("3d_render");
    }
    public void set3dRender(String render3d){
        set("3d_render",render3d);
    }

    public AdditionalDesc getAdditionalDesc(){
        return get(AdditionalDesc.class, "additional_desc");
    }
    public void setAdditionalDesc(AdditionalDesc additional_desc){
        set("additional_desc",additional_desc);
    }

    public MediaFile getMediaFile(){
        return get(MediaFile.class, "media");
    }
    public void setMediaFile(MediaFile media_file){
        set("media",media_file);
    }

    public static class MediaFile extends BecknObject {
        public MediaFile() {
        }

        public MediaFile(String payload) {
            super(payload);
        }

        public MediaFile(JSONObject object) {
            super(object);
        }

        public String getMimeType(){
            return get("mime_type");
        }
        public void setMimeType(String mime_type){
            set("mime_type",mime_type);
        }

        public String getUrl(){
            return get("url");
        }
        public void setUrl(String url){
            set("url",url);
        }
        
        public String getSignature(){
            return get("signature");
        }
        public void setSignature(String signature){
            set("signature",signature);
        }

        public String getDsa(){
            return get("dsa");
        }
        public void setDsa(String dsa){
            set("dsa",dsa);
        }

    }


    public static class AdditionalDesc extends BecknObject {
        public AdditionalDesc() {
        }

        public AdditionalDesc(String payload) {
            super(payload);
        }

        public AdditionalDesc(JSONObject object) {
            super(object);
        }
        
        public String getUrl(){
            return get("url");
        }
        public void setUrl(String url){
            set("url",url);
        }
        public String getContentType(){
            return get("content_type");
        }
        public void setContentType(String content_type){
            set("content_type",content_type);
        }

    }
}
