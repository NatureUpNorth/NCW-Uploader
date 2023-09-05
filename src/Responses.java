import javax.swing.*;
import java.util.*;

public class Responses {
    private Map<String, Object> responses;

    public Responses() {
        responses = new HashMap<String, Object>();
    }

    public void addKey(String key) {
        addKey(key, null);
    }

    public void addKey(String key, Object obj) {
        responses.put(key, obj);
    }

    public void replaceKeyValue(String key, Object obj) {
        responses.replace(key, obj);
    }

    public Object get(String key) {
        return responses.get(key);
    }
    public Map<String, String> validate() {
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("valid", "true");

        // busy?
        if ((boolean)Uploader.getInstance().getResponses().get("busy")) {
            response.replace("valid", "false");
            response.put("title", "Busy...");
            response.put("message", "Still searching for images in the specified folder.");
            return response;
        }

        // check affiliation
        String affiliation = (String)responses.get("affiliation");
        if (affiliation == null || affiliation.length() == 0) {
            response.replace("valid", "false");
            response.put("title", "Affiliation Missing");
            response.put("message", "No information is provided for Other in the Affiliation section.");
            return response;
        }

        // check files
        Set<String> files = (Set<String>)responses.get("imageFiles");
        if (files != null && files.size() == 0) {
            response.replace("valid", "false");
            response.put("title", "Image File Error");
            response.put("message", "There is no image file selected to upload.");
            return response;
        }

        // check dates
        Date startDate = (Date)responses.get("startDate");
        Date endDate = (Date)responses.get("endDate");
        if (startDate != null && endDate != null && !startDate.equals(endDate) && !startDate.before(endDate)) {
            response.replace("valid", "false");
            response.put("title", "Deployment Dates Error");
            response.put("message", "Start date must be before end date.");
            return response;
        }

        // check location
        String location = (String)responses.get("location");
        if (location == null) {
            response.replace("valid", "false");
            response.put("title", "Location Not Provided");
            response.put("message", "You must provide the latitude and longitude for the camera deployment location.");
            return response;
        }
        try {
            String[] locationInfo = location.split(",");
            double latitude = Double.parseDouble(locationInfo[0]);
            double longitude = Double.parseDouble(locationInfo[1]);
            if (!(latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180)) {
                throw new Exception();
            }
        } catch (Exception e) {
            response.replace("valid", "false");
            response.put("title", "Invalid Location");
            response.put("message", "Please double check the camera deployment location.");
            return response;
        }

        // check habitat
        HashSet<String> habitat = (HashSet<String>)responses.get("habitat");
        if (habitat == null || habitat.size() == 0) {
            response.replace("valid", "false");
            response.put("title", "No Habitat Information");
            response.put("message", "You must select at least one in the Habitat section.");
            return response;
        }
        for (String aHabitat: habitat) {
            if (aHabitat.equals("Other: ")) {
                response.replace("valid", "false");
                response.put("title", "Missing Habitat Information");
                response.put("message", "No information is provided for Other in the Habitat section.");
                return response;
            }
        }

        return response;
    }
}
