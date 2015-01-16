package lazyblogger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DriveUploader {

    
    private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private Drive service;
    private String imageLink;

    public DriveUploader() throws IOException {
        init();
    }

    public void init() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        
        List<String> scopes = Lists.newArrayList(DriveScopes.DRIVE);
        
        Credential credential = Auth.authorize(scopes, "drive", "/drive_secret.json");
        
        Drive.Builder b = new Drive.Builder(httpTransport, jsonFactory, credential);
        b.setApplicationName(Lazyblogger.APP_NAME);

        /*GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();*/

        //String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        /*System.out.println("Please open the following URL in your browser then type the authorization code:");
        System.out.println("  " + url);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();*/

        /*GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
        GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientSecrets)
                .build()
                .setFromTokenResponse(response);

        String accessToken = credential.getAccessToken();
        String refreshToken = credential.getRefreshToken();*/

        //Create a new authorized API client
        service = b.build();
    }

    public void uploadImage(java.io.File image) throws IOException {
        File body = new File();
        body.setParents(Arrays.asList(new ParentReference().setId("0B6uafUciiCLSRUc0c01MVm9WQVk")));
        body.setTitle(image.getParentFile().getName().replaceAll(" ", "_") + "_thumb");
        body.setMimeType("image/jpeg");

        FileContent mediaContent = new FileContent("text/plain", image);

        File file = service.files().insert(body, mediaContent).execute();
        file.setShared(Boolean.TRUE);

        System.out.println("File ID: " + file.getId());
        //imageLink = file.getDownloadUrl().replaceFirst("\\?e=download&gd=true", "");
        imageLink = "https://drive.google.com/uc?id=" + file.getId();
        System.out.println("URL: " + imageLink);
    }
    
    public String getImageLink() {
        return imageLink;
    }
}
