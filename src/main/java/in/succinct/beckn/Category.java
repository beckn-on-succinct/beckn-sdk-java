package in.succinct.beckn;

import java.time.Duration;

public class Category extends BecknObjectWithId implements  TagGroupHolder{
    public Category(){
        super();
    }
    public Category(String  payload){
        super(payload);
    }
    public Descriptor getDescriptor(){
        return get(Descriptor.class,"descriptor");
    }
    public void setDescriptor(Descriptor descriptor){
        set("descriptor",descriptor);
    }

    public enum CategoryCode {
        FOOD_AND_BEVERAGES,
        FASHION,
        HOME_DECOR,
        PACKAGED_FOOD,
        PACKAGED_GOODS_NON_FOOD,
        ON_DEMAND_LOGISTICS;
        
        
        
        public static final CategoryCodeConverter convertor = new CategoryCodeConverter();
    }
    public static class CategoryCodeConverter extends EnumConvertor<CategoryCode>{};


    public CategoryCode getCategoryCode(){
        Descriptor descriptor = getDescriptor();
        if (descriptor != null){
            String code = descriptor.getCode();
            if (code != null){
                return CategoryCode.valueOf(code);
            }
        }
        return null;
    }
    public void setCategoryCode(CategoryCode category_code){
        Descriptor descriptor = getDescriptor();
        if (descriptor == null){
            descriptor = new Descriptor();
            setDescriptor(descriptor);
        }
        descriptor.setCode(category_code.toString());
    }

    public String getParentCategoryId(){
        return get("parent_category_id");
    }
    public void setParentCategoryId(String parent_category_id){
        set("parent_category_id",parent_category_id);
    }
    public Long getTtl(){
        String ttl = get("ttl");
        return ttl == null ? null : Duration.parse(ttl).getSeconds();
    }
    public void setTtl(Long ttl){
        set("ttl",ttl == null ? null  :Duration.ofSeconds(ttl).toString());
    }

    public Time getTime(){
        return get(Time.class,"time");
    }
    public void setTime(Time time){
        set("time",time);
    }

    @Override
    public TagGroups getTags() {
        return TagGroupHolder.super.getTags();
    }

    @Override
    public void setTags(TagGroups tags) {
        TagGroupHolder.super.setTags(tags);
    }
}
