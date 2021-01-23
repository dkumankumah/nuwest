package nl.daxte1.dax1;

import com.google.firebase.database.Exclude;

import io.reactivex.annotations.NonNull;

public class BlogPostId {
    @Exclude
    public String BlogPostId;

    public <T extends BlogPostId> T withId(@NonNull final String id) {
        this.BlogPostId = id;
        return (T) this;
    }
}
