package in.succinct.beckn;

import com.venky.core.util.ObjectUtil;
import org.json.simple.JSONObject;

import java.util.UUID;

public class BecknObjectWithId extends BecknObject {
    public BecknObjectWithId(){
        super();
    }
    public BecknObjectWithId(String payload){
        super(payload);
    }
    public BecknObjectWithId(JSONObject object){
        super(object);
    }

    public String getId(){
        return get(getIdAttribute());
    }
    public void setId(String id){
        if (!ObjectUtil.isVoid(id) && !ObjectUtil.isVoid(getId()) && !ObjectUtil.equals(getId(),id)){
            throw new RuntimeException("Id cannot be changed!");
        }
        set(getIdAttribute(),id);
    }
    
    public String getIdAttribute(){
        return "id";
    }


    protected void setDefaultId() {
        setId(UUID.randomUUID().toString());
    }
}
