public class GovernmentOfficial extends PoliticalActor
{
    private String position;

    public GovernmentOfficial(){
        super();
    }
    public GovernmentOfficial(String position){
        super();
        this.position = position;
    }

    public String getPosition(){
        return position;
    }
    public void setPosition(String position){
        this.position = position;
    }

}