package in.succinct.beckn;

import in.succinct.beckn.BreakUp.BreakUpElement;
import in.succinct.beckn.BreakUp.BreakUpElement.BreakUpCategory;

public class BreakUp extends BecknObjects<BreakUpElement> {
    public BreakUp(){
        super();
    }
    public BreakUpElement createElement(BreakUpCategory type, String title, Price unitPrice){
        return createElement(type,title,unitPrice,1);
    }
    public BreakUpElement createElement(BreakUpCategory type, String title, Price unitPrice, int count){
        BreakUpElement element =new BreakUpElement();
        element.setTitle(title);

        Price newPrice = new Price();
        newPrice.update(unitPrice);
        newPrice.setOfferedValue(newPrice.getOfferedValue() == null ? null :newPrice.getOfferedValue() * count);
        newPrice.setListedValue(newPrice.getListedValue() == null ? null  : newPrice.getListedValue() * count);
        newPrice.setValue(newPrice.getValue() == null ? null  : newPrice.getValue() * count);
        newPrice.setComputedValue(newPrice.getComputedValue() == null ? null  : newPrice.getComputedValue() * count);
        newPrice.setMaximumValue(newPrice.getMaximumValue() == null ? null  : newPrice.getMaximumValue() * count);
        newPrice.setMinimumValue(newPrice.getMinimumValue() == null ? null  : newPrice.getMinimumValue() * count);

        //Quote supposed to have unit price in breakup
        element.setPrice(newPrice);
        element.setType(type);
        return element;
    }



    public static class BreakUpElement extends BecknObject {
        public BreakUpElement(){
            super();
        }
        public String getTitle(){
            return get("title");
        }
        public void setTitle(String title){
            set("title",title);
        }
        public Item getItem(){
            return get(Item.class, "item");
        }
        public void setItem(Item item){
            set("item",item);
        }
        public Price getPrice(){
            return get(Price.class, "price");
        }
        public void setPrice(Price price){
            set("price",price);
        }

        public BreakUpCategory getType(){
            String s = extendedAttributes.get("type");
            return s == null ? null : BreakUpCategory.valueOf(s);
        }
        public void setType(BreakUpCategory type){
            extendedAttributes.set("type",type == null ? null : type.toString());
        }

        public boolean isExtendedAttributesDisplayed(){
            return true;
        }

        public String getItemId(){
            return extendedAttributes.get("item_id");
        }
        public void setItemId(String item_id){
            extendedAttributes.set("item_id",item_id);
        }
        public Quantity getItemQuantity(){
            return extendedAttributes.get(Quantity.class, "item_quantity");
        }
        public void setItemQuantity(Quantity item_quantity){
            extendedAttributes.set("item_quantity",item_quantity);
        }


        
        
        public enum BreakUpCategory {
            item,
            delivery,
            packing,
            tax,
            misc,
            discount;
            
            public static EnumConvertor<BreakUpCategory> convertor = new EnumConvertor<>(){};
        }
    }
}
