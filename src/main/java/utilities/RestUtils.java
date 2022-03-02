package utilities;
import org.json.JSONObject;

public class RestUtils  {

    String testName = Generators.genName();
    String testEmail = Generators.genEmail();
    public JSONObject jsonObj;

    public JSONObject createJson() {

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("clientName", testName);
        jsonObj.put("clientEmail", testEmail);

        return this.jsonObj;
    }

    public static void main(String[] args) {
        System.out.println();
    }

}
