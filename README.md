# Project 3 - *Memories*

**Memories** is a photo sharing app similar to Instagram but using Parse as its backend.

Time spent: **13** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can sign up to create a new account using Parse authentication.
- [x] User can log in to their account.
- [x] User can log out of their account
- [x] The current signed in user is persisted across app restarts.
- [x] User can take a photo, add a caption, and post it to "Instagram".
- [x] User can view the last 20 posts submitted to "Instagram".
- [x] The user should switch between different tabs - viewing all posts (feed view), compose (capture photos form camera) and profile tabs (posts made) using fragments and a Bottom Navigation View. (2 points)
- [ ] User can pull to refresh the last 20 posts submitted to "Instagram".

The following **optional** features are implemented:

- [ ] User sees app icon in home screen and styled bottom navigation view
- [ ] Style the feed to look like the real Instagram feed.
- [ ] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse.
- [ ] User can load more posts once he or she reaches the bottom of the feed using infinite scrolling.
- [x] Show the username and creation time for each post.
- [x] User can tap a post to view post details, including timestamp and caption.
- [ ] User Profiles
  - [ ] Allow the logged in user to add a profile photo
  - [x] Display the profile photo with each post
  - [x] Tapping on a post's username or profile photo goes to that user's profile page and shows a grid view of the user's posts
- [ ] User can comment on a post and see all comments for each post in the post details screen.
- [ ] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:

- [ ] User is notified of errors 
  - [ ] during signup, 
  - [ ] login, 
  - [ ] submitting a post, 
  - [ ] refreshing their feed, etc.
- [ ] Post descriptions are truncated and user can tap on post description to see its detail page and read full description
- [x] user profile includes profile picture

## Video Walkthrough

-add after adding sign up functionality-

## Notes/Challenges

Deprecated function fix
Maintenance through different versions
finding out that profile pictures aren't stored locally and i need to query them
clicking on user post = fragments + profiles -> launching fragment inside of a fragment
trying to access the bottom nav bar inside of a recycler view adapter (i should just change it when i am at the fragment)

## Open-source libraries used

- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

credits to icons + images
- <a href="https://www.flaticon.com/free-icons/home-accessories" title="home accessories icons">Home accessories icons created by alihmirza111 - Flaticon</a>
- <a href="https://www.flaticon.com/free-icons/plus" title="plus icons">Plus icons created by Freepik - Flaticon</a>
- <a href="https://www.flaticon.com/free-icons/profile" title="profile icons">Profile icons created by Freepik - Flaticon</a>
- <a href="https://www.flaticon.com/free-icons/picture" title="picture icons">Picture icons created by Reion - Flaticon</a>

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
