package nl.daxte1.dax1;


import java.util.Date;

public class BlogPost extends BlogPostId {

    public String description, image, title, user_id;
    public Date timestamp;


    public BlogPost(){}

    public BlogPost(String description, String image, String title, String user_id, Date timestamp) {
        this.description = description;
        this.image = image;
        this.title = title;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
