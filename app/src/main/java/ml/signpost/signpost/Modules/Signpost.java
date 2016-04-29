package ml.signpost.signpost.Modules;

import java.util.List;

import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Models.Sign;
import ml.signpost.signpost.Models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


/**
 * Created by Trevor on 4/10/16.
 */
public interface Signpost {

    @GET("user/{username}")
    Call<List<User>> getUser (
        @Path("username") String username
    );

    @GET("post/{id}/signs")
    Call<List<Sign>> signsForPost (
        @Path("id") int id
    );



    @GET("post/title/{title}")
    Call<List<Post>> postFromTitle (
            @Path("title") String title
    );

    @GET("post/{lat}/{lng}/{rad}")
    Call<List<Post>> locationPosts(
            @Path("lat") double lat,
            @Path("lng") double lng,
            @Path("rad") int i);

    @GET("posts")
    Call<List<Post>> allPosts();

    @GET("users")
    Call<List<User>> allUsers();

    @GET("user/username")
    Call<List<User>> userFromUsername(
            @Path("username") String username
    );

    @POST ("post/new")
    Call<List<Post>> createPost(@Body Post newPost);

    @POST ("user/new")
    Call<List<User>> createUser(@Body User newUser);

    @POST ("sign/new")
    Call<List<Sign>> createSign(@Body Sign newSign);

    @PUT ("post/update")
    Call<List<Post>> updatePost(@Body Post updatePost);

    @PUT ("user/update")
    Call<List<User>> updateUser(@Body User updateUser);

    @PUT ("sign/update")
    Call<List<Sign>> updateSign(@Body Sign updateSign);


}
