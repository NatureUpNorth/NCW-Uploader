import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;
import com.opencsv.CSVWriter;

public class DataProcessor {
    private Map<String, Metadata> data;
    private String[] header;

    public DataProcessor() {
        data = new HashMap<String, Metadata>();
    }

    public boolean addFile(String file, File imageFile) {
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(imageFile);
            data.put(file, metadata);
        } catch (ImageProcessingException|IOException e) {
            // shouldn't fail, hopefully
            System.err.println("Error reading meta data from " + imageFile);
            return false;
        }
        // is there a header already?
        if (header == null) {
            List<String> columns = new LinkedList<String>();
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    columns.add(tag.getTagName());
                }
            }
            header = columns.toArray(new String[0]);
        }
        return true;
    }

    public File writeCSVData(Map<String, String> uniqueNames) {
        if (data.size() == 0) {
            // empty data
            return null;
        }

        // File outputFile = new File(System.getProperty("user.home") + "/Downloads/metadata.csv");
        File outputFile = null;

        try {
            outputFile = File.createTempFile("metadata", ".csv");

            // create FileWriter object with file as parameter
            FileWriter outputFileWriter = new FileWriter(outputFile);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFileWriter);

            writer.writeNext(header);

            for (String imageFile: data.keySet()) {
                Metadata metadata = data.get(imageFile);
                Map<String, String> tagInfo = new HashMap<String, String>();

                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        String description = tag.getDescription();
                        if (tag.getTagName().contains("Size") || tag.getTagName().contains("Width") || tag.getTagName().contains("Height")) {
                            description = description.split(" ")[0];
                        }
                        if (tag.getTagName().equals("File Name")) {
                            description = uniqueNames.get(imageFile).toString();
                        }
                        tagInfo.put(tag.getTagName(), description);
                    }
                }

                List<String> dataRow = new LinkedList<String>();
                for (String column: header) {
                    dataRow.add(tagInfo.get(column));
                }
                String[] dataRowArray = dataRow.toArray(new String[0]);

                writer.writeNext(dataRowArray);
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Unable to write the CSV file");
            return null;
        }
        // header
        return outputFile;
    }

    public File writeOtherData() {
        // File outputFile = new File(System.getProperty("user.home") + "/Downloads/metadata.csv");
        File outputFile = null;

        try {
            outputFile = File.createTempFile("info", ".txt");

            // create FileWriter object with file as parameter
            FileWriter outputFileWriter = new FileWriter(outputFile);

            // name
            outputFileWriter.write("Display Name: " + Uploader.getInstance().getResponses().get("displayName") + "\n");

            // affiliation
            outputFileWriter.write("Affiliation: " + Uploader.getInstance().getResponses().get("affiliation") + "\n");

            // dates
            // startDate, endDate
            SimpleDateFormat simple = new SimpleDateFormat("EEE, MMM d, yyyy");

            outputFileWriter.write("Start Date: " + simple.format(Uploader.getInstance().getResponses().get("startDate")) + "\n");
            outputFileWriter.write("End Date: " + simple.format(Uploader.getInstance().getResponses().get("endDate")) + "\n");

            // location
            // latitude, longitude
            outputFileWriter.write("Latitude/Longitude: " + Uploader.getInstance().getResponses().get("location") + "\n");

            // habitat
            // set
            outputFileWriter.write("Habitats: " + Uploader.getInstance().getResponses().get("habitat") + "\n");
/*
            for (String habitat:  (Set<String>)Uploader.getInstance().getResponses().get("habitats")) {
                outputFileWriter.write(habitat +", ");
                System.err.println(Uploader.getInstance().getResponses().get("habitats"));
            }
*/

            // urbanization
            outputFileWriter.write("Urbanization: " + Uploader.getInstance().getResponses().get("urbanization") + "\n");

            // comments
            outputFileWriter.write("Comments:\n" + Uploader.getInstance().getResponses().get("comments") + "\n");

            outputFileWriter.close();
        } catch (IOException e) {
            System.err.println("Unable to write the info file");
            return null;
        }
        return outputFile;
    }
}
