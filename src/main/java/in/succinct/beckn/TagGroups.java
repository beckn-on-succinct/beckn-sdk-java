package in.succinct.beckn;

import org.json.simple.JSONArray;

import java.util.List;

public class TagGroups extends BecknObjectsWithId<TagGroup>{
    public TagGroups() {
        super(false);
    }

    public TagGroups(JSONArray array) {
        super(array,false);
    }
    
    public void rmTag(String tagGroup){
        List<TagGroup> tagGroupList = all(tagGroup);
        if (tagGroupList != null) {
            for (TagGroup tg : tagGroupList) {
                remove(tg);
            }
        }
    }
    public String getTag(String tagGroup, String code) {
        TagGroup tg = get(tagGroup);
        TagGroups tagList = tg == null ? null : tg.getList();
        TagGroup tag = tagList == null ? null : tagList.get(code);
        return tag == null ? null : tag.getValue();
    }
    public void setTag(String tagGroup, String code, String value){
        TagGroup tg = get(tagGroup);
        if (tg == null){
            tg = getObjectCreator().create(TagGroup.class);
            tg.setId(tagGroup);
            add(tg);
        }
        TagGroups tagList = tg.getList();
        if (tagList == null){
            tagList = getObjectCreator().create(TagGroups.class);
            tg.setList(tagList);
        }

        TagGroup tag = tagList.get(code);
        if (tag == null){
            if (value != null) {
                tag = getObjectCreator().create(TagGroup.class);
                tag.setId(code);
                tag.setValue(value);
                tagList.add(tag);
            }
        }else {
            tag.setValue(value);
        }
    }
}
