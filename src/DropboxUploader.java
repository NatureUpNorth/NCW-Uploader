import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.DbxRequestConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.JSONObject;

import javax.swing.*;
import java.nio.file.FileSystems;

public class DropboxUploader extends SwingWorker {
    private UploadPanel parent;
    private DataProcessor dataProcessor;

    private DbxClientV2 dropboxClient;

    private String prefix;

    public DropboxUploader(UploadPanel parent, DataProcessor dataProcessor) {
        this.parent = parent;
        this.dataProcessor = dataProcessor;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream configInput = classLoader.getResourceAsStream("tokens.json");
        Scanner configScanner = new Scanner(configInput).useDelimiter("\\A");
        JSONObject tokenObj = new JSONObject(configScanner.hasNext() ? configScanner.next() : "{}");
        String refreshToken = tokenObj.getString("refresh");
        String appAuthString = tokenObj.getString("appId") + ":" + tokenObj.getString("appSecret");

        DbxRequestConfig config = DbxRequestConfig.newBuilder("NCW-Upload/0.9").build();

        String accessToken = refreshAccessToken(refreshToken, appAuthString);

        // TODO: error checking with accessToken

        dropboxClient = new DbxClientV2(config, accessToken);


        prefix = "/";
        prefix += Uploader.getInstance().getResponses().get("affiliation");
        prefix += "/";
        prefix += Uploader.getInstance().getResponses().get("displayName");
        prefix += "/";
    }

    private String refreshAccessToken(String refreshToken, String appAuthString) {
        String authString = Base64.getUrlEncoder().encodeToString(appAuthString.getBytes());
        try {
            URL url = new URL("https://api.dropbox.com/oauth2/token?grant_type=refresh_token&refresh_token=" + refreshToken);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Host", "api.dropbox.com");
            con.setRequestProperty("Authorization", "Basic " + authString);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "curl/8.1.2");
            con.setRequestProperty("Accept", "*/*");
            con.setRequestMethod("POST");

            int status = con.getResponseCode();

            if (status != 200) {
                throw new RuntimeException("Error getting the access token");
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String jsonResponse = in.readLine();
            in.close();

            con.disconnect();

            JSONObject jsonObj = new JSONObject(jsonResponse);
            return jsonObj.getString("access_token");

        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Error getting the access token");
    }

    @Override
    protected Object doInBackground() throws Exception {
        Set<String> imageFiles = (Set<String>)Uploader.getInstance().getResponses().get("imageFiles");

        prefix += System.currentTimeMillis();
        prefix += "/";

        int numberOfFiles = imageFiles.size() + 2;
        int count = 0;

        String systemSeparator = FileSystems.getDefault().getSeparator();
        systemSeparator = systemSeparator.equals("/") ? "/" : "\\\\";

        Map<String, String> uniqueNames = new HashMap<String, String>();

        for (String imageFile : imageFiles) {
            try {
                int fileNumber = count + 1;
                parent.setImage(imageFile);

                String[] findExtension = imageFile.split("\\.");
                String extension = findExtension[findExtension.length - 1];
                String filename = fileNumber + "." + extension;

                uniqueNames.put(imageFile, filename);

                try (InputStream in = new FileInputStream(imageFile)) {
                    FileMetadata metadata = dropboxClient.files().uploadBuilder(prefix + filename)
                            .uploadAndFinish(in);
                } catch (InvalidAccessTokenException e) {
                    System.err.println("Error uploading file " + imageFile + " to the dropbox repository.");
                    System.err.println(e);
                }

                count++;

                Thread.sleep(100);
                parent.getProgressBar().setValue((int)(100.0 * count / numberOfFiles));

            } catch (IllegalAccessError e) {
                System.err.println("Error accessing the dropbox repository.");
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        parent.setImage(null);

        File csvFile = dataProcessor.writeCSVData(uniqueNames);

        try (InputStream in = new FileInputStream(csvFile.getAbsolutePath())) {
            FileMetadata metadata = dropboxClient.files().uploadBuilder(prefix + csvFile.getName())
                    .uploadAndFinish(in);
        }

        count++;
        parent.getProgressBar().setValue((int)(100.0 * count / numberOfFiles));

        File otherData = dataProcessor.writeOtherData();

        try (InputStream in = new FileInputStream(otherData.getAbsolutePath())) {
            FileMetadata metadata = dropboxClient.files().uploadBuilder(prefix + otherData.getName())
                    .uploadAndFinish(in);
        }

        parent.getProgressBar().setValue(100);

        return null;
    }

    @Override
    protected void done() {
        Uploader.getInstance().thankyou();
    }



}