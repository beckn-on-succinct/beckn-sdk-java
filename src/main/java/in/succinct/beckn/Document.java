package in.succinct.beckn;

public class Document extends in.succinct.beckn.BecknObjectWithId {
    public Document() {super(); }

    public String getUrl(){
        return get("url");
    }
    public void setUrl(String url){
        set("url",url);
    }

    public String getLabel(){
        return get("label");
    }
    public void setLabel(String label){
        set("label",label);
    }

}
