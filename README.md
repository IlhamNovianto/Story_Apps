# Story_Apps 
(Unfinished, In the development phase, Git Branch. Unit Testing ViewModel) 

1. Authentication page
The conditions that must be met are as follows.
    
Displays the login page to sign in to the application. Here's the input needed.
>- Email (R.id.ed_login_email)
>- Password (R.id.ed_login_password)

Create a register page to register yourself in the application. Here's the input needed.
>- Name (R.id.ed_register_name)
>- Email (R.id.ed_register_email)
>- Password (R.id.ed_register_password)
        
The password must be hidden.
Create a Custom View in the form of EditText on the login and register page with the following conditions.
>- If the number of passwords is less than 6 characters, display an error directly on the EditText.

Stores session data and tokens in preferences. Session data is used to set up app flows with specifications such as the following.
>- If you have logged in, go directly to the main page.
>- If not, it will go to the login page. 

There is a feature to log out (R.id.action_logout) on the main page with the following conditions.
>- When the logout button is pressed, the token and session information must be deleted.

2. List Story
The conditions that must be met are as follows.
Displays a list of stories from the provided API. Here's the minimum amount of information you must display.
>- Username (R.id.tv_item_name)
>- Photo (R.id.iv_item_photo)

A details page appears when one of the story items is pressed. Here's the minimum amount of information you must display.
>- Username (R.id.tv_detail_name)
>- Photo (R.id.iv_detail_photo)
>- Description (R.id.tv_detail_description)

3. Add Story
The conditions that must be met are as follows.

Create a page to add a new story that can be accessed from the story list page. Here's the minimum input required.
>- Photo files (can be from gallery and camera)
>- Story Description (R.id.ed_add_description)

Here are the conditions for adding a new story:
>- There is a button (R.id.button_add) to upload data to the server. 
>- After the button is clicked and the upload process is successful, it will return to the story list page. 
>- The latest story data should appear at the very top.

4. Displays the Condition Animation that must be met as follows.

Create animations in applications by using one type of animation :
>- Property Animation
>- Motion Animation
>- Shared Element

5. Displaying Maps
The conditions that must be met are as follows.

Displays a new single page containing a map that displays all stories that have locations correctly. It can be a marker or an icon in the form of an image. Story data that has latitude and longitude locations can be retrieved through the location parameter as follows.
>- https://story-api.dicoding.dev/v1/stories?location=1.

6. Paging List
The conditions that must be met are as follows.
>- Display a list story by using Paging 3 correctly.

7. Creating Testing (In this case, I just created a Unit Testing on the ViewModel.)
The conditions that must be met are as follows.
>- Apply unit tests to existing functions in each ViewModel.
>- Write the test scenario in a separate file with the name test-scenario.txt in the root of the >- project folder. For test screenwriting formats

Screenshot, While Project Running :
https://github.com/IlhamNovianto/Story_Apps/tree/main/assets

All modules in the project are obtained in the Dicoding Intermediate class. 
©Copyright by Dicoding

