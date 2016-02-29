[Signpost](/screenshots/Metaphor.JPG) is an app where users can “plant posts” or “hang signs” on existing posts, or use a map to see posts that other people have planted and added to. 


## [Screen 1: Main Map](/screenshots/Screen.Main.JPG)
- Google Map View displaying locations of Posts
- Edit Text with Auto Place Complete moves the map to any location to view the signposts
- FAB (only visible when users are logged in) which brings users to the Create Sign Activity
- Tab View along the bottom brings you to the Popular Signs and Nearby Signs 
- Back button quits


## [Screen 2: Popular Signs](/screenshots/Screen.Popular.JPG)

- The primary part of this activity will be populated by a RecyclerView which contains CardViews of the 100 most popular nearby signs. These signs will have a ImageView with a drawable representing their category and a TextView with the message of the sign.
- The action bar will say the word “Popular” and the same overflow menu as in the Main Map Activity.
- There will be the same tab view from the Main Map Activity but with Popular highlighted.
- Back button goes back to Main Map


## [Screen 3: Nearby Signs](/screenshots/Screen.Nearby.JPG)

- Same as Popular Signs but sorts signs by closest to user’s location.
- Back Button goes back to Main Map


## [Screen 4: Login](/screenshots/Screen.Login.JPG) 

- This will most likely be a fragment overlaid on top of the Main Map Activity. 
- users provide a username and password
- pressing sign up creates their account record in the database
- pressing either sign up or the back button take the user back to whichever view/fragment they were in previously


## [Screen 5: Post](/screenshots/Screen.Post.JPG)

- This activity will show a specific post. It will consist of a RecyclerView whose first Card is the name of the post while the subsequent ones are signs as shown in Popular and Nearby except that there will be a post connecting them together.
- Pressing the back button will bring the user back to the previous activity.
- Clicking on a sign will take the user to the detail sign activity.
- If the user is close enough to the current post there will also be a FAB to hang a sign.
- The post exists as long as it has a sign. If no more signs, then it will disappear from the map after a while. The post will only be able to be seen in the JSpinner.
- We would like to do the fancy thing where the name of the post transitions into the toolbar as the user scrolls down.


## [Screen 6: Sign](/screenshots/Screen.Sign.JPG)

- Action Bar with the post name.
- This activity will have a zoomed in ImageView of the map where the post you are checking is from.
- Might later split it into two ImageViews if the adding image feature is added.
- Below it there is an ImageView with the symbol that the user selected for this particular sign.
- TextView with the message the user inputted for their sign.
- Another TextView for the date the sign was added. 
- The time period the sign will stay alive for is in an inverse relationship to the number of sign on a post. The more number of signs the lesser time they stay alive for.
- The back button takes you to all the signs for a particular post.


## [Screen 7: Create Sign](/screenshots/Screen.Create.JPG)

- Action Bar called Hang Sign with a TabView.
- MapView with your location and all the posts near you. You have to be around that location to to hang a sign.
- Series of buttons showing symbols that will have meaning to them. Such as, the star could mean happy memory.
- JSpinner to let you choose posts around that location or create new one. Each post will have a name assigned to it, eg. Dovecote.
- The signs will not have names but only text and a symbol.
- Another TextView to add the message.
- Save button at the end. It takes you back to the previous page you came from.

##Back End - signpost.ml

- Custom back end to supply data
- Proposed Schema
    - Post
        - has_many :signs
        - string :name
        - number :latitude
        - number :longitude
        - boolean :visible
    - Sign
        - string :message
        - DateTime :date created
        - int :type 
        - belongs_to :user
        - belongs_to :post
    - User
        - has_many :signs
        - username
        - email
        - password
- Standard REST implementation you know the drill
- Sinatra app
- hosted on [Heroku](http://www.heroku.com) until we’re famous
- visit us at [signpost.ml](http://www.signpost.ml)

## Features to add:

- See friends only signs
- ‘so ‘n’ so saw your sign’
- Voting for signs (popularity a-la-yik yak)
- Notifications when near a post
- Add images to the signs
- Tablet version
- Filter posts by type 
