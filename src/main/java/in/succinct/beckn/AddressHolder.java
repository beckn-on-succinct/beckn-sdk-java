package in.succinct.beckn;

import in.succinct.json.JSONAwareWrapper;
import in.succinct.json.JSONAwareWrapper.JSONAwareWrapperCreator;

import java.util.StringTokenizer;

@SuppressWarnings("all")
public interface AddressHolder {
    default Address getAddress() {
        Object o = get("address");
        if (o instanceof String) {
            return getAddress((String) o);
        } else {
            return get(Address.class, "address");
        }
    }

    default void setAddress(Object address) {
        if (address instanceof String) {
            set("address", (String) address);
        } else {
            set("address", ((Address) address));
        }
    }

    default Address getAddress(String oAddress) {
        Address address = getObjectCreator().create(Address.class);
        StringTokenizer tokenizer = new StringTokenizer((String) oAddress, ",");
        if (tokenizer.hasMoreTokens()) address.setDoor(tokenizer.nextToken());
        if (tokenizer.hasMoreTokens()) address.setBuilding(tokenizer.nextToken());
        if (tokenizer.hasMoreTokens()) address.setStreet(tokenizer.nextToken());
        if (tokenizer.hasMoreTokens()) address.setLocality(tokenizer.nextToken());
        if (tokenizer.hasMoreTokens()) address.setWard(tokenizer.nextToken());
        if (getCountry() != null) {
            address.setCountry(getCountry().getName());
            if (address.getCountry() == null) {
                address.setCountry(getCountry().getCode());
            }
        }
        if (getState() != null) {
            address.setState(getState().getName());
            if (address.getState() == null) {
                address.setState(getState().getCode());
            }
        }
        if (getCity() != null) {
            address.setCity(getCity().getName());
            if (address.getCity() == null) {
                address.setCity(getCity().getCode());
            }
        }
        address.setPinCode(getPinCode());
        return address;
    }

    public City getCity() ;
    public void setCity(City city) ;


    public State getState() ;
    public void setState(State state);

    public String getPinCode() ;
    public void setPinCode(String pin_code) ;

    public Country getCountry() ;
    public void setCountry(Country country);

    public <W extends JSONAwareWrapper> W get(Class<W> clazz, String name);
    public <W> W get(String key);
    public void set(String key, String value);
    public <W extends JSONAwareWrapper> void set(String key, W value);
    JSONAwareWrapperCreator getObjectCreator();
}