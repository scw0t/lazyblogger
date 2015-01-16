package lazyblogger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.BloggerScopes;
import com.google.api.services.blogger.model.Post;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;

public class ExecutePost {

    private final String BLOG_ID = "4604026761903363296";
    private final HttpTransport HTTP_TRANSPORT;
    private final JsonFactory JSON_FACTORY;

    public ExecutePost() {
        HTTP_TRANSPORT = new NetHttpTransport();
        JSON_FACTORY = new JacksonFactory();
    }

    public void execute(Post content) throws Exception {
        try {
            List<String> scopes = Lists.newArrayList(BloggerScopes.BLOGGER);
            Credential credential = Auth.authorize(scopes, "blogger", "/client_secret.json");

            Blogger.Builder b = new Blogger.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential);
            b.setApplicationName(Lazyblogger.APP_NAME);
            Blogger blogger = b.build();

            Blogger.Posts.Insert postsInsertAction = blogger.posts().insert(BLOG_ID, content);

            postsInsertAction.setFields("author/displayName,content,published,title,url");

            Post post = postsInsertAction.execute();

            System.out.println("Title: " + post.getTitle());
            System.out.println("Author: " + post.getAuthor().getDisplayName());
            System.out.println("Published: " + post.getPublished());
            System.out.println("URL: " + post.getUrl());
            System.out.println("Content: " + post.getContent());
        } catch (IOException ex) {
        }
    }
}
