package in.succinct.beckn;

import in.succinct.beckn.CancellationTerm.CancellationTerms;
import in.succinct.beckn.RefundTerm.RefundTerms;
import in.succinct.beckn.ReplacementTerm.ReplacementTerms;
import in.succinct.beckn.ReturnTerm.ReturnTerms;
import org.json.simple.JSONObject;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Item extends BecknObjectWithId implements TagGroupHolder {
    public Item() {
        super();
    }
    public Item(String payload){
        super(payload);
    }
    public Item(JSONObject item){
        super(item);
    }

    public String getCategoryId(){
        BecknStrings categoryIds = getCategoryIds();
        if (categoryIds.isEmpty()){
            return null;
        }else {
            return categoryIds.get(0);
        }
    }
    public void setCategoryId(String criteriaId){
        if (criteriaId != null) {
            getCategoryIds().add(criteriaId);
        }else {
            getCategoryIds().clear();
        }
    }
    
    
    public String getLength(){
        return getTag("dimension","length");
    }
    public void setLength(String length){
        setTag("dimension","length",length);
    }
    
    public String getWidth(){
        return getTag("dimension","width");
    }
    public void setWidth(String width){
        setTag("dimension","width", width);
    }
    
    public String getHeight(){
        return getTag("dimension","height");
    }
    public void setHeight(String height){
        setTag("dimension","height", height);
    }
    
    public String getWeight(){
        return getTag("dimension","weight");
    }
    public void setWeight(String weight){
        setTag("dimension", "weight", weight);
    }
    
    public BecknStrings getCategoryIds(){
        return extendedAttributes.get(BecknStrings.class, "category_ids",true);
    }
    public void setCategoryIds(BecknStrings category_ids){
        extendedAttributes.set("category_ids",category_ids);
    }

    public BecknStrings getLocationIds(){
        return extendedAttributes.get(BecknStrings.class, "location_ids",true);
    }
    public void setLocationIds(BecknStrings location_ids){
       extendedAttributes.set("location_ids",location_ids);
    }


    public BecknStrings getPaymentIds(){
        return extendedAttributes.get(BecknStrings.class, "payment_ids",true);
    }
    public void setPaymentIds(BecknStrings payment_ids){
        extendedAttributes.set("payment_ids",payment_ids);
    }
    public BecknStrings getFulfillmentIds(){
        return extendedAttributes.get(BecknStrings.class, "fulfillment_ids",true);
    }
    public void setFulfillmentIds(BecknStrings fulfillment_ids){
        extendedAttributes.set("fulfillment_ids",fulfillment_ids);
    }

    public Descriptor getDescriptor(){
        return get(Descriptor.class,"descriptor");
    }
    public void setDescriptor(Descriptor descriptor){
        set("descriptor",descriptor);
    }
    public Price getPrice(){
        return get(Price.class,"price");
    }
    public void setPrice(Price price){
        set("price",price);
    }

    public String getLocationId(){
        BecknStrings locationIds = getLocationIds();
        return locationIds != null && !locationIds.isEmpty() ? locationIds.get(0) : null ;
    }

    public void setLocationId(String id){
        if (id != null) {
            getLocationIds().add(id);
        }else {
            getLocationIds().clear();
        }
    }

    public boolean getRecommended(){
        return getBoolean("recommended");
    }
    public void setRecommended(boolean recommended){
        set("recommended",recommended);
    }

    public String getFulfillmentId(){
        BecknStrings fulfillmentIds = getFulfillmentIds();
        if (fulfillmentIds.isEmpty()){
            return null;
        }else {
            return fulfillmentIds.get(0);
        }
    }
    public void setFulfillmentId(String fulfillment_id){
        BecknStrings fulfillmentIds =  getFulfillmentIds();
        if (!fulfillmentIds.getInner().contains(fulfillment_id)){
            fulfillmentIds.add(fulfillment_id);
        }
    }



    //

    public String getParentItemId(){
        return get("parent_item_id");
    }
    public void setParentItemId(String parent_item_id){
        set("parent_item_id",parent_item_id);
    }

    public ItemQuantity getParentItemQuantity(){
        return get(ItemQuantity.class, "parent_item_quantity");
    }
    public void setParentItemQuantity(ItemQuantity parent_item_quantity){
        set("parent_item_quantity",parent_item_quantity);
    }

    public Organization getCreator(){
        return get(Organization.class, "creator");
    }
    public void setCreator(Organization creator){
        set("creator",creator);
    }

    public ItemQuantity getItemQuantity(){
        JSONObject q = get("quantity");
        if (q == null || q.containsKey("count") || q.keySet().isEmpty()){
            return null;
        }
        return get(ItemQuantity.class,"quantity");
    }
    public void setItemQuantity(ItemQuantity quantity){
        set("quantity",quantity);
    }

    public ItemQuantity getTentativeItemQuantity(){
        JSONObject q = extendedAttributes.get("quantity");
        if (q == null || q.containsKey("count") || q.keySet().isEmpty()){
            return null;
        }
        return extendedAttributes.get(ItemQuantity.class,"quantity");
    }
    public void setTentativeItemQuantity(ItemQuantity quantity){
        extendedAttributes.set("quantity",quantity);
    }



    public Quantity getQuantity(){
        JSONObject q = get("quantity");
        if (q == null ||  !q.containsKey("count") ){
            return null;
        }
        return get(Quantity.class, "quantity");
    }
    public void setQuantity(Quantity quantity){
        set("quantity",quantity);
    }

    public Integer getRating(){
        return getInteger("rating", null);
    }
    public void setRating(Integer rating){
        set("rating",rating);
    }

    public Time getTime(){
        return get(Time.class,"time");
    }
    public void setTime(Time time){
        set("time",time);
    }

    public boolean getRateable(){
        return getBoolean("rateable");
    }
    public void setRateable(boolean rateable){
        set("rateable",rateable);
    }

    public boolean getMatched(){
        return getBoolean("matched");
    }
    public void setMatched(boolean matched){
        set("matched",matched);
    }

    public boolean getRelated(){
        return getBoolean("related");
    }
    public void setRelated(boolean related){
        set("related",related);
    }



    //Attributes added by all networks. Rationalized.


    public boolean isExtendedAttributesDisplayed(){
        return true;
    }

    public String getCountryOfOrigin(){
        return extendedAttributes.get("country_of_origin");
    }
    public void setCountryOfOrigin(String country_of_origin){
        extendedAttributes.set("country_of_origin",country_of_origin);
    }

    public String getHsnCode(){
        return extendedAttributes.get("hsn_code");
    }
    public void setHsnCode(String hsn_code){
        extendedAttributes.set("hsn_code",hsn_code);
    }

    public double getTaxRate(){
        return extendedAttributes.getDouble("tax_rate");
    }
    public void setTaxRate(double tax_rate){
        extendedAttributes.set("tax_rate",tax_rate);
    }
    
    public Boolean isVeg(){
        return extendedAttributes.getBoolean("veg", null);
    }
    public void setVeg(Boolean veg){
        extendedAttributes.set("veg",veg);
    }


    public Price getTax(){
        return extendedAttributes.get(Price.class, "tax");
    }
    public void setTax(Price tax){
        extendedAttributes.set("tax",tax);
    }

    public boolean isReturnable(){
        return extendedAttributes.getBoolean("returnable");
    }
    public void setReturnable(boolean returnable){
        extendedAttributes.set("returnable",returnable);
    }

    public boolean isSellerPickupReturn(){
        return extendedAttributes.getBoolean("seller_pickup_return");
    }
    public void setSellerPickupReturn(boolean seller_pickup_return){
        extendedAttributes.set("seller_pickup_return",seller_pickup_return);
    }

    public boolean isCancellable(){
        return extendedAttributes.getBoolean("cancellable");
    }
    public void setCancellable(boolean cancellable){
        extendedAttributes.set("cancellable",cancellable);
    }

    public Duration getTimeToShip(){
        String s =  extendedAttributes.get("time_to_ship");
        return s == null ? null : Duration.parse(s);
    }
    public void setTimeToShip(Duration duration){
        extendedAttributes.set("time_to_ship",duration == null ? null : duration.toString());
    }

    public Duration getReturnWindow(){
        String s = extendedAttributes.get("return_window");
        return s == null ? null :Duration.parse(s);
    }
    public void setReturnWindow(Duration return_window){
        extendedAttributes.set("return_window",return_window == null ? null : return_window.toString());
    }

    public boolean isAvailableOnCod(){
        return extendedAttributes.getBoolean("available_on_cod");
    }
    public void setAvailableOnCod(boolean available_on_cod){
        extendedAttributes.set("available_on_cod",available_on_cod);
    }
    public String getContactDetailsConsumerCare(){
        return extendedAttributes.get("contact_details_consumer_care");
    }
    public void setContactDetailsConsumerCare(String contact_details_consumer_care){
        extendedAttributes.set("contact_details_consumer_care",contact_details_consumer_care);
    }

    public PackagedCommodity getPackagedCommodity(){
        return extendedAttributes.get(PackagedCommodity.class, "packaged_commodity");
    }
    public void setPackagedCommodity(PackagedCommodity packaged_commodity){
        extendedAttributes.set("packaged_commodity",packaged_commodity);
    }

    public PrepackagedFood getPrepackagedFood(){
        return extendedAttributes.get(PrepackagedFood.class, "prepackaged_food");
    }
    public void setPrepackagedFood(PrepackagedFood prepackaged_food){
        extendedAttributes.set("prepackaged_food",prepackaged_food);
    }

    public VeggiesFruits getVeggiesFruits(){
        return extendedAttributes.get(VeggiesFruits.class, "veggies_fruits");
    }
    public void setVeggiesFruits(VeggiesFruits veggies_fruits){
        extendedAttributes.set("veggies_fruits",veggies_fruits);
    }



    public static class PackagedCommodity extends BecknObject {
        public static final Set<String> CategoryNames = new HashSet<>(){{
            add("Masala & Seasoning");
            add("Oil & Ghee");
            add("Foodgrains");
            add("Eggs, Meat & Fish" );
            add("Cleaning & Household");
            add("Beauty & Hygiene" );
            add("Kitchen Accessories" );
            add("Baby Care" );
            add("Pet Care" );
            add("Stationery");
        }};

        public String getManufacturerOrPackerName() {
            return get("manufacturer_or_packer_name");
        }

        public void setManufacturerOrPackerName(String manufacturer_or_packer_name) {
            set("manufacturer_or_packer_name", manufacturer_or_packer_name);
        }

        public String getManufacturerOrPackerAddress() {
            return get("manufacturer_or_packer_address");
        }

        public void setManufacturerOrPackerAddress(String manufacturer_or_packer_address) {
            set("manufacturer_or_packer_address", manufacturer_or_packer_address);
        }

        public String getCommonOrGenericNameOfCommodity() {
            return get("common_or_generic_name_of_commodity");
        }

        public void setCommonOrGenericNameOfCommodity(String common_or_generic_name_of_commodity) {
            set("common_or_generic_name_of_commodity", common_or_generic_name_of_commodity);
        }

        public String getMultipleProductsNameNumberOrQty() {
            return get("multiple_products_name_number_or_qty");
        }

        public void setMultipleProductsNameNumberOrQty(String multiple_products_name_number_or_qty) {
            set("multiple_products_name_number_or_qty", multiple_products_name_number_or_qty);
        }

        public String getNetQuantityOrMeasureOfCommodityInPkg() {
            return get("net_quantity_or_measure_of_commodity_in_pkg");
        }

        public void setNetQuantityOrMeasureOfCommodityInPkg(String net_quantity_or_measure_of_commodity_in_pkg) {
            set("net_quantity_or_measure_of_commodity_in_pkg", net_quantity_or_measure_of_commodity_in_pkg);
        }

        public String getMonthYearOfManufacturePackingImport() {
            return get("month_year_of_manufacture_packing_import");
        }

        public void setMonthYearOfManufacturePackingImport(String month_year_of_manufacture_packing_import) {
            set("month_year_of_manufacture_packing_import", month_year_of_manufacture_packing_import);
        }

        public String getImportedProductCountryOfOrigin() {
            return get("imported_product_country_of_origin");
        }

        public void setImportedProductCountryOfOrigin(String imported_product_country_of_origin) {
            set("imported_product_country_of_origin", imported_product_country_of_origin);
        }
    }

    public static class PrepackagedFood extends BecknObject {
        public static final Set<String> CategoryNames = new HashSet<>(){{
            addAll(Arrays.asList(
                    "Gourmet & World Foods","Beverages","Bakery, Cakes & Dairy","Snacks & Branded Foods"
            ));
        }};
        public String getIngredientsInfo() {
            return get("ingredients_info");
        }

        public void setIngredientsInfo(String ingredients_info) {
            set("ingredients_info", ingredients_info);
        }

        public String getNutritionalInfo() {
            return get("nutritional_info");
        }

        public void setNutritionalInfo(String nutritional_info) {
            set("nutritional_info", nutritional_info);
        }

        public String getAdditivesInfo() {
            return get("additives_info");
        }

        public void setAdditivesInfo(String additives_info) {
            set("additives_info", additives_info);
        }

        public String getManufacturerOfPackerName() {
            return get("manufacturer_or_packer_name");
        }

        public void setManufacturerOfPackerName(String manufacturer_or_packer_name) {
            set("manufacturer_or_packer_name", manufacturer_or_packer_name);
        }

        public String getManufacturerOfPackerAddress() {
            return get("manufacturer_or_packer_address");
        }

        public void setManufacturerOfPackerAddress(String manufacturer_or_packer_address) {
            set("manufacturer_or_packer_address", manufacturer_or_packer_address);
        }

        public String getBrandOwnerName() {
            return get("brand_owner_name");
        }

        public void setBrandOwnerName(String brand_owner_name) {
            set("brand_owner_name", brand_owner_name);
        }

        public String getBrandOwnerAddress() {
            return get("brand_owner_address");
        }

        public void setBrandOwnerAddress(String brand_owner_address) {
            set("brand_owner_address", brand_owner_address);
        }

        public String getBrandOwnerFSSAILogo() {
            return get("brand_owner_FSSAI_logo");
        }

        public void setBrandOwnerFSSAILogo(String brand_owner_FSSAI_logo) {
            set("brand_owner_FSSAI_logo", brand_owner_FSSAI_logo);
        }

        public String getBrandOwnerFSSAILicenseNo() {
            return get("brand_owner_FSSAI_license_no");
        }

        public void setBrandOwnerFSSAILicenseNo(String brand_owner_FSSAI_license_no) {
            set("brand_owner_FSSAI_license_no", brand_owner_FSSAI_license_no);
        }

        public String getOtherFSSAILicenseNo() {
            return get("other_FSSAI_license_no");
        }

        public void setOtherFSSAILicenseNo(String other_FSSAI_license_no) {
            set("other_FSSAI_license_no", other_FSSAI_license_no);
        }

        public String getNetQuantity() {
            return get("net_quantity");
        }

        public void setNetQuantity(String net_quantity) {
            set("net_quantity", net_quantity);
        }

        public String getImporterName() {
            return get("importer_name");
        }

        public void setImporterName(String importer_name) {
            set("importer_name", importer_name);
        }

        public String getImporterAddress() {
            return get("importer_name");
        }

        public void setImporterAddress(String importer_name) {
            set("importer_name", importer_name);
        }

        public String getImporterFSSAILogo() {
            return get("importer_FSSAI_logo");
        }

        public void setImporterFSSAILogo(String importer_FSSAI_logo) {
            set("importer_FSSAI_logo", importer_FSSAI_logo);
        }

        public String getImporterFSSAILicenseNo() {
            return get("importer_FSSAI_license_no");
        }

        public void setImporterFSSAILicenseNo(String importer_FSSAI_license_no) {
            set("importer_FSSAI_license_no", importer_FSSAI_license_no);
        }

        public String getImportedProductCountryOfOrigin() {
            return get("imported_product_country_of_origin");
        }

        public void setImportedProductCountryOfOrigin(String imported_product_country_of_origin) {
            set("imported_product_country_of_origin", imported_product_country_of_origin);
        }

        public String getOtherImporterName() {
            return get("other_importer_name");
        }

        public void setOtherImporterName(String other_importer_name) {
            set("other_importer_name", other_importer_name);
        }

        public String getOtherImporterAddress() {
            return get("other_importer_address");
        }

        public void setOtherImporterAddress(String other_importer_address) {
            set("other_importer_address", other_importer_address);
        }

        public String getOtherPremises() {
            return get("other_premises");
        }

        public void setOtherPremises(String other_premises) {
            set("other_premises", other_premises);
        }

        public String getOtherImporterCountryOfOrigin() {
            return get("other_importer_country_of_origin");
        }

        public void setOtherImporterCountryOfOrigin(String other_importer_country_of_origin) {
            set("other_importer_country_of_origin", other_importer_country_of_origin);
        }
    }

    public static class VeggiesFruits extends BecknObject {
        public static final Set<String> CategoryNames = new HashSet<>(){{
            add("Fruits And Vegetables");
        }};
        public String getNetQuantity(){
            return get("net_quantity");
        }
        public void setNetQuantity(String net_quantity){
            set("net_quantity",net_quantity);
        }
    }


    /* Tage Group s*/

    @Override
    public TagGroups getTags() {
        return TagGroupHolder.super.getTags();
    }

    @Override
    public void setTags(TagGroups tags) {
        TagGroupHolder.super.setTags(tags);
    }

    public AddOns getAddOns(){
        return get(AddOns.class, "add_ons");
    }
    public void setAddOns(AddOns add_ons){
        set("add_ons",add_ons);
    }

    public CancellationTerms getCancellationTerms(){
        return get(CancellationTerms.class, "cancellation_terms");
    }
    public void setCancellationTerms(CancellationTerms cancellation_terms){
        set("cancellation_terms",cancellation_terms);
    }

    public RefundTerms getRefundTerms(){
        return get(RefundTerms.class, "refund_terms");
    }
    public void setRefundTerms(RefundTerms refund_terms){
        set("refund_terms",refund_terms);
    }


    public ReplacementTerms getReplacementTerms(){
        return get(ReplacementTerms.class, "replacement_terms");
    }
    public void setReplacementTerms(ReplacementTerms replacement_terms){
        set("replacement_terms",replacement_terms);
    }

    public ReturnTerms getReturnTerms(){
        return get(ReturnTerms.class, "return_terms");
    }
    public void setReturnTerms(ReturnTerms return_terms){
        set("return_terms",return_terms);
    }


    public Xinput getXinput(){
        return get(Xinput.class, "xinput");
    }
    public void setXinput(Xinput xinput){
        set("xinput",xinput);
    }

    
}
