public class Character
{
    private static List instances = new ArrayList();
  
    public String given_name;
    public String middle_name;
    public String family_name;
    public int[] nameform;
    public String[] demographics;
    public int age = 0;
    public String presentation;

    public Character(String buildstring){

    }
  
    public Character(String given_name, String middle_name, String family_name, int[] nameform){
        this.given_name = given_name;
        this.middle_name = middle_name;
        this.family_name = family_name;
        this.nameform = nameform;

        this.class.instances.add(this); //hopefully this works
    }
}
