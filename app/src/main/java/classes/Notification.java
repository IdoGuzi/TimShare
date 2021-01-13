package classes;

public class Notification {
    private String ID, from,to,additional;
    private Request type;
    private Boolean active;

    public Notification(){
        from="";
        to="";
        additional="";
        type=Request.none;
        active = true;
    }

    public Notification(String from, String to, Request t){
        this.from=from;
        this.to=to;
        this.type=t;
        this.active=true;
        this.additional="";
    }

    public String getID(){
        return ID;
    }

    public void setID(String id){
        this.ID=id;
    }

    public String getFrom(){
        return from;
    }

    public void setFrom(String user){
        this.from=user;
    }

    public String getTo(){
        return to;
    }

    public void setTo(String user){
        this.to=user;
    }

    public String getAdditional(){
        return additional;
    }

    public void setAdditional(String add){
        this.additional=add;
    }

    public Request getType(){
        return type;
    }

    public void setType(Request t){
        this.type=t;
    }

    public Boolean getActive(){
        return active;
    }

    public void setActive(Boolean a){
        active=a;
    }
}
