package lazyblogger;

import com.google.api.services.blogger.model.Post;

public class Content {

    private String text;
    private Post post;

    Content(String text) {
        this.text = text;

        if (!"".equals(text)) {
            post = new Post();
            post.setTitle("A test post");
            post.setContent(text);
        } else {
            System.out.println("textarea is empty");
        }

    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    
    

}
