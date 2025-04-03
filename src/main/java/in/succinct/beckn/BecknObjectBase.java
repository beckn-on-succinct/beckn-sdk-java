package in.succinct.beckn;

import org.json.simple.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BecknObjectBase extends BecknAware<JSONObject> {
    public BecknObjectBase(JSONObject value) {
        super(value);
    }

    public BecknObjectBase(String payload) {
        super(payload);
    }

    //Called by Reflection! don't remove.!
    public BecknObjectBase(){
        this(new JSONObject());
    }

    public <T extends  BecknObjectBase> void update(T from){
        update(from,true);
    }
    public <T extends  BecknObjectBase> void update(T from, boolean reset){
        update(from, getObjectCreator(),reset);
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T extends  BecknObjectBase> void update(T fromSource , JSONAwareWrapperCreator boCreator, boolean reset){
        if (fromSource == null){
            return;
        }
        if (this == fromSource){
            return;
        }
        if (!hasCommonAncestor(this,fromSource)){
            throw new IllegalArgumentException("Incompatible type of the parameter");
        }

        Class<? extends BecknObjectBase> sourceClass = fromSource.getClass();

        Map<String, Method> targetSetters = new HashMap<>();
        Map<String,Method> targetGetters = new HashMap<>();
        Map<String,Method> sourceGetters = new HashMap<>();
        for (Method m : sourceClass.getMethods()) {
            if ((m.getName().startsWith("get") || m.getName().startsWith("is")) && m.getParameterCount() == 0 && !m.getName().equals("getInner") && BecknObject.class.isAssignableFrom(m.getDeclaringClass())){
                sourceGetters.put(m.getName(),m);
            }
        }

        for (Method m : getClass().getMethods()) {
            if (!BecknObject.class.isAssignableFrom(m.getDeclaringClass())){
                continue;
            }
            if ((m.getName().startsWith("set") && m.getParameterCount() == 1 )) {
                String setterName = m.getName();

                String getterName = String.format("g%s",setterName.substring(1));
                Method sourceGetter = sourceGetters.get(getterName);

                if (sourceGetter == null){
                    getterName = String.format("is%s",setterName.substring(3));
                    sourceGetter = sourceGetters.get(getterName);

                }
                if (sourceGetter !=  null && hasCommonAncestor(m.getParameterTypes()[0],sourceGetter.getReturnType())){
                    targetSetters.put(m.getName(), m);
                }
            }else if ((m.getName().startsWith("get") || m.getName().startsWith("is")) && m.getParameterCount() == 0){
                targetGetters.put(m.getName(),m);
            }
        }

        for (Map.Entry<String,Method> entry : sourceGetters.entrySet()) {

            String getterName = entry.getKey();
            String setterName = getterName.startsWith("get") ? String.format("s%s",getterName.substring(1)) : String.format("set%s",getterName.substring(2));

            Method sourceGetter = entry.getValue();
            Method targetSetter = targetSetters.get(setterName);
            Method targetGetter = targetGetters.get(getterName);

            if (targetSetter == null || targetGetter == null) {
                continue;
            }

            Class<?> sourceFieldType = sourceGetter.getReturnType();
            Class<?> targetFieldType = targetSetter.getParameterTypes()[0];
            try {
                Object sourceField = sourceGetter.invoke(fromSource);
                Object targetField = targetGetter.invoke(this);
                if (sourceField == null){
                    continue;
                }
                if (targetField == null || reset){
                    if (BecknAware.class.isAssignableFrom(targetFieldType)){
                        targetField = boCreator.create(targetFieldType);
                    }else {
                        targetField = sourceField;
                    }
                }


                if (BecknObject.class.isAssignableFrom(sourceFieldType) && BecknObject.class.isAssignableFrom(targetFieldType)) {
                    if (((BecknObject)sourceField).hasAdditionalProperties() ){
                        //IGM Will bomb!! as we need to start right from request
                        for (Entry fieldEntry : (Iterable<Entry>) (((BecknObject) sourceField).getInner()).entrySet()) {
                            if (reset){
                                ((BecknObject) targetField).getInner().put(fieldEntry.getKey(), fieldEntry.getValue());
                            }else {
                                ((BecknObject) targetField).getInner().putIfAbsent(fieldEntry.getKey(), fieldEntry.getValue());
                            }
                        }
                    }else {
                        ((BecknObject) targetField).update((BecknObject) sourceField,reset);
                    }
                } else if (BecknObjects.class.isAssignableFrom(sourceFieldType) && BecknObjects.class.isAssignableFrom(targetFieldType)){
                    BecknObjects sourceFields = (BecknObjects) sourceField;
                    BecknObjects targetFields = (BecknObjects) targetField;
                    for (Object o : sourceFields){
                        if (o instanceof BecknObject){
                            BecknObject listElement = (BecknObject) boCreator.create(targetFields.clazz);
                            if (listElement.getClass() == o.getClass()){
                                listElement.setPayload(((BecknObject) o).getInner().toString());
                                targetFields.add(listElement,reset);
                            }else {
                                listElement.update((BecknObject) o);
                                if (listElement instanceof BecknObjectWithId && targetFields instanceof BecknObjectsWithId) {
                                    BecknObjectWithId lid = (BecknObjectWithId) listElement;
                                    BecknObjectsWithId lids = ((BecknObjectsWithId) targetFields);
                                    if (lids.get(lid.getId()) == null || !lids.unique) {
                                        lids.add(lid);
                                    } else  {
                                        lids.get(lid.getId()).update(lid);
                                    }
                                } else {
                                    targetFields.add(listElement,reset);
                                }
                            }
                        }else {
                            targetFields.add(o,reset);
                        }
                    }
                }
                targetSetter.invoke(this, targetField);
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }


    }

    public static boolean hasCommonAncestor(Class<?> targetType, Class<?> sourceType) {
        Class<?> commonAncestor = targetType;
        while (commonAncestor != null && !commonAncestor.isAssignableFrom(sourceType)){
            commonAncestor = commonAncestor.getSuperclass();
        }
        return commonAncestor != null && commonAncestor.isAssignableFrom(sourceType) && commonAncestor != BecknObjectBase.class && commonAncestor != Object.class;
    }


    public static  <T extends BecknObjectBase, S extends BecknObjectBase> boolean hasCommonAncestor(T target, S source){
        Class<?> commonAncestor = target.getClass();
        Class<?> sourceType = source.getClass();
        while (!commonAncestor.isAssignableFrom(sourceType)){
            commonAncestor = commonAncestor.getSuperclass();
        }
        return commonAncestor != BecknObjectBase.class;
    }

    public void set(String key, Double value) {
        super.set(key, value == null ? null :
                    (value == value.intValue())? String.valueOf(value.intValue()) :
                            String.valueOf(value));
    }

}
