package in.succinct.beckn;

import java.lang.reflect.ParameterizedType;

public class AdditionalProperties<T extends BecknAware> extends BecknObject{

    Class<T> clazz;
    public AdditionalProperties(){
        ParameterizedType pt = (ParameterizedType)(getClass().getGenericSuperclass());
        clazz = (Class<T>)pt.getActualTypeArguments()[0];
    }
    public AdditionalProperties(Class<T> clazz){
        this.clazz = clazz;
    }

    @Override
    public T get(String key) {
        return super.get(clazz,key);
    }


    public static class AdditionalSources extends AdditionalProperties<ResolutionSource>{

    }
}
