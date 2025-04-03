package in.succinct.beckn;

import com.venky.core.string.StringUtil;

public class TagGroup extends BecknObjectWithId{

    public TagGroup(){
        super();
        setDisplay(true);
    }
    
    public TagGroup(String payload) {
        super(payload);
        setDisplay(true);
    }
    
    public TagGroup(String code, Object value){
        this(code,null,value);
    }
    public TagGroup(String code, String name,Object value){
        setCode(code);
        if (name != null) setName(name);
        if (value instanceof TagGroups){
            setList((TagGroups)value);
        }else {
            setValue(String.valueOf(value));
        }
        setDisplay(true);
    }
    @Override
    public String getId() {
       return getCode();
    }

    @Override
    public void setId(String id) {
        setCode(id);
    }

    
    public boolean isDisplay(){
        return getBoolean("display");
    }
    public void setDisplay(boolean display){
        set("display",display);
    }
    

    protected String getCode(){
        return get("code");
    }
    protected void setCode(String code){
        set("code",code);
    }
    public String getName(){
        return get("name");
    }
    public void setName(String name){
        set("name",name);
    }





    public String getValue(){
        return StringUtil.valueOf(get("value"));
    }
    public void setValue(String value){
        set("value",value);
    }

    public TagGroups getList(){
        return get(TagGroups.class, "list");
    }
    public void setList(TagGroups tags){
        set("list",tags);
    }
}
