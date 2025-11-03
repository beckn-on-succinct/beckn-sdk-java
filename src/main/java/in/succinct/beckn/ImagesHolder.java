package in.succinct.beckn;

import in.succinct.json.JSONAwareWrapper;
import in.succinct.json.JSONAwareWrapper.JSONAwareWrapperCreator;
import org.json.simple.JSONArray;

import java.util.StringTokenizer;

@SuppressWarnings("all")
public interface ImagesHolder {
    default Images getImages() {
        JSONArray o = get("images");
        Images images ;
        if (o == null){
            images = null;
        }else if (o.isEmpty()){
            images= getObjectCreator().create(Images.class);
            setImages(images);
        }else if (o.get(0) instanceof String) {
            images = getObjectCreator().create(Images.class);
            setImages(images);
            o.forEach(s->{
                images.add(new Image(){{
                    setUrl((String)s);
                }});
            });
        } else {
            images = get(Images.class, "images");
        }
        return images;
    }

    default void setImages(Images images) {
        set("images", images);
    }



    public <W extends JSONAwareWrapper> W get(Class<W> clazz, String name);
    public <W> W get(String key);
    public void set(String key, String value);
    public <W extends JSONAwareWrapper> void set(String key, W value);
    JSONAwareWrapperCreator getObjectCreator();
}