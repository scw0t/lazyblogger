package lazyblogger;

import com.google.api.services.blogger.model.Post;
import java.util.ArrayList;

public class Content {

    private Post post;
    private String imageLink;
    private String tracklist;
    private String title;
    private String postBody;

    private final String DISCOGS_LOGO = "https://drive.google.com/uc?id=0B6uafUciiCLSSEFtSkRtaC1VcTg";
    private final String MB_LOGO = "https://drive.google.com/uc?id=0B6uafUciiCLSc2xFZ1BWNXJxQlU";
    private final String RYM_LOGO = "https://drive.google.com/uc?id=0B6uafUciiCLSdVpDXzE0ank4WFU";
    private final String YOUTUBE_LOGO = "https://drive.google.com/uc?id=0B6uafUciiCLSY1JEV1hGeWlVTzg";
    private final String DOWNLOAD_ICON = "https://drive.google.com/uc?id=0B6uafUciiCLSQ3F5TjNFX1NwTEE";
    
    private PostData postData;
    

    public Content() {

    }

    public void constructPost() {
        postBody = constructBody();

        if (!"".equals(postBody)) {
            post = new Post();
            post.setTitle(title);
            post.setContent(postBody);
            post.setLabels(fillLabels());
        }

    }

    private String constructBody() {
        StringBuilder body = new StringBuilder();

        body.append("<div dir=\"ltr\" style=\"text-align: left;\" trbidi=\"on\">\n");
        body.append("<div class=\"separator\" style=\"clear: both; text-align: center;\">\n");
        body.append("<a href=\"");
        body.append(postData.getThumbLink());
        body.append("\" imageanchor=\"1\" style=\"margin-left: 1em; margin-right: 1em;\"><img border=\"0\" height=\"388\" src=\"");
        body.append(postData.getThumbLink());
        body.append("\" width=\"400\" />&nbsp;</a></div></div>\n");
        //body.append("<br>\n");
        
        body.append(putAdditionalLinks());

        body.append("Tracklst:<br>");
        body.append(tracklist);
        body.append("<br>");
        body = constructLinkImage(body, postData.getTxtLink(), DOWNLOAD_ICON, 30, 30);
        //body.append("\" rel=\"nofollow\" target=\"_blank\">click</a>\n");

        return body.toString();
    }

    

    private String putAdditionalLinks() {
        String[] additionalLinks 
                = {postData.getDiscogsLink(),
                postData.getMbLink(),
                postData.getRymLink(),
                postData.getYoutubeLink()};
        
        StringBuilder str = new StringBuilder();
        str.append("<p class=\"db_links\" style=\"clear: both; text-align: center;\">\n");
        for (String link : additionalLinks) {
            if (!link.equals("none")) {
                if (link.startsWith("http://www.discogs.com/")) {
                    str = constructLinkImage(str, link, DISCOGS_LOGO, 30, 30);
                }

                if (link.startsWith("http://musicbrainz.org/")) {
                    link = link.replaceFirst("\\?tport=8000", "");
                    str = constructLinkImage(str, link, MB_LOGO, 30, 30);
                }
                
                if (link.startsWith("http://rateyourmusic.com/")) {
                    str = constructLinkImage(str, link, RYM_LOGO, 30, 30);
                }
                if (link.startsWith("http://www.youtube.com/")) {
                    str = constructLinkImage(str, link, YOUTUBE_LOGO, 80, 30);
                }
            }
        }
        
        str.append("</p>");
        return str.toString();
    }
    

    private StringBuilder constructLinkImage(StringBuilder str, String link, String image, int x, int y) {
        str.append("<a href=\"");
        str.append(link);
        str.append("\" target=\"_blank\"><img alt=\"\" src=\"");
        str.append(image);
        str.append("\" style=\"width: ");
        str.append(x);
        str.append("px; height: ");
        str.append(y);
        str.append("px;\" /></a>\n");
        return str;
    }

    private ArrayList<String> fillLabels() {
        ArrayList<String> labels = new ArrayList<>();
        if (!"".equals(postData.getCountry())) {
            labels.add(postData.getCountry());
        }
        if (!"".equals(postData.getGenre())) {
            labels.add(postData.getGenre());
        }
        /*if (!"".equals(label)) {
            labels.add(label);
        }*/
        return labels;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getTracklist() {
        return tracklist;
    }

    public void setTracklist(String tracklist) {
        this.tracklist = tracklist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PostData getPostData() {
        return postData;
    }

    public void setPostData(PostData postData) {
        this.postData = postData;
    }

    public String getPostBody() {
        return postBody;
    }

}
