package in.succinct.beckn;

import com.venky.cache.UnboundedCache;
import com.venky.core.util.ObjectUtil;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BecknObjectsWithId<T extends BecknObjectWithId> extends BecknObjects<T> {
    boolean unique ;
    public BecknObjectsWithId(){
        this(new JSONArray());
    }
    public BecknObjectsWithId(String payload){
        this((JSONArray) parse(payload));
    }
    public BecknObjectsWithId(JSONArray array){
        this(array,true);
    }
    public BecknObjectsWithId(boolean unique){
        this(new JSONArray(),unique);
    }
    public BecknObjectsWithId(String payload,boolean unique){
        this((JSONArray) parse(payload),unique);
    }
    public BecknObjectsWithId(JSONArray array,boolean unique){
        super(array);
        this.unique= unique;
        loadMap();
    }
    @Override
    public T get(int index) {
        T t = super.get(index);
        if (unique && map != null && t != null) {
            //Make sure only one object exists for the id.
            List<T> values   = map.get(t.getId()); // Be careful changing this. possibility of recursion with loadMap()
            if (values.isEmpty()){
                values.add(t);
            }else {
                t = values.get(0);
            }
        }
        return t;
    }

    @Override
    public void add(Object t1,boolean reset){
        loadMap();
        T t = (T)t1;
        if (ObjectUtil.isVoid(t.getId())){
            t.setDefaultId();
        }
        List<T> list = map.get(t.getId());
        if (list.size() == 1){
            if (unique || reset) {
                list.get(0).update(t, reset);
                return;
            }
        }else if (list.size() > 1){
            if (unique) {
                throw new RuntimeException("Multiple IDS found");
            }
        }

        list.add(t);
        super.add(t,false);
    }
    public void remove(T t){
        if (ObjectUtil.isVoid(t.getId())){
            throw new RuntimeException("ID missing");
        }
        List<T> values = map.get(t.getId());
        if (values.remove(t)){
            super.remove(t);
        }
    }

    @Override
    public void setInner(JSONArray value) {
        super.setInner(value);
        map = null;
    }

    Map<String, List<T>> map = null;
    public List<T> all(String id){
        loadMap();
        return id == null ? null : map.get(id);
    }
    @SuppressWarnings("unchecked")
    public T get(String id){

        List<T> l = all(id);
        if (l == null || l.isEmpty()){
            return null;
        }

        return l.get(0);
    }
    private void loadMap(){
        if (map == null){
            Map<String,List<T>> tmpMap =  new UnboundedCache<>() {
                @Override
                protected List<T> getValue(String Id) {
                    return new ArrayList<>();
                }
            };
            for (int i = 0 ; i < size() ; i ++){
                T t = get(i);
                tmpMap.get(t.getId()).add(t);
            }
            map = tmpMap;
        }
    }
    public Set<String> ids(){
        loadMap();
        return new HashSet<>(map.keySet());
    }

}
