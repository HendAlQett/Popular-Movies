# Popular-Movies

##Project Overview

The app allows users to discover the most popular movies playing, and allows them to 'favorite' movies, and use the app on phones and tablets.

##The app has the following functionalities
* Use [Retrofit](http://square.github.io/retrofit/) to retrieve movies.
* Use Content Provider and Database
* Support Portrait and Landscape modes.
* Support Phones and Tablets.
* Test case to test parsing.
* Android test case to test Database.
* Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
* UI contains settings menu to toggle the sort order of the movies by: most popular, highest rated, and favorites.
* UI contains a screen for displaying the details for a selected movie.
* Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.
* When a user changes the sort criteria (“most popular and highest rated”) the main view gets updated correctly.
* When a movie poster thumbnail is selected, the movie details screen is launched.
* In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.
* Add share functionality.
* When a trailer is selected, app uses an Intent to launch the trailer (in Youtube).
* In the movies detail screen, a user can tap a star to mark it as a Favorite.
* App saves a "Favorited" movie to SharedPreferences or a database using the movie’s id.
* When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie IDs stored in SharedPreferences or a database.
* Objects classes implements parcable.
* Use [Picasso](http://square.github.io/picasso/) to download images
* Use the design support library for Android.

##To run the app

Fill MOVIES_API_KEY in build.gradle with the appropriate API key from [The Movie DB](https://www.themoviedb.org/?_dc=1441732310)
 
