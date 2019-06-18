# Presentation Tool

This is a presentation tool I wrote for a school event. 

## Table of Contents

- [Features](#features)
- [How to Use](#how-to-use)
- [Libraries and Technologies Used](#libraries-and-technologies-used)

### Features

- Shows a series of images in a sorted way
- Displays a logo on the upper left side of the screen
- Shows a description on the left and right side of the screen
- Highly configurable:
    - Use *A* and *D* to go through the images
    - Use *E* to display the running directory in your file browser
    - Use *L* to enable/disable the logo
    - Use *U* to invert the colors (black <-> white)
    - Use *F* to show the image on the whole UI 
    - Use *B* to show a black screen (fullscreen)
    - Use *P* to lock/unlock the UI
    - Use *R* to force the repaint of a screen
- Scaling:
    - Use *S* to toggle between screen and logo scaling (default: screen)
    - Use *+* and *-* to increase or decrease the size of the screen/logo
    - This is especially useful when working with a beamer because you don't know how large
       the image will be displayed!
- Fast-Type:
    - Type in the index (starting at 0) of the image to jump to it by pressing *Enter*
    - **Important:** You always have to type TWO numbers! (so 02 for the picture at index 2)

### How to Use

- Put a logo named "logo.\*" (\* = Any valid picture file extension just like png or jpeg) inside the "res" directory
- Place all your images in the "pics" directory
- Naming scheme for the images:
    - Number of the image (or a text NOT including a -)
        - Will be displayed on the right hand side of the screen
    - The -
    - Another text (can include -'s)
        - Displayed on the left hand side of the screen
    - You can do line breaks by using ;n .
        - *The software won't do this automatically.*
- Run the program. Everything should work by default, create an issue if this does not work for you.

Quick disclaimer: I wrote this within approximately 5 hours due to
missing time, however it should run stable, but it isn't the user-friendliest
software in the world. 

Pull request are open for improvements if you want to tackle this!


### Libraries and Technologies Used

- Java Swing for the UI
- Logback Classic for logging purposes
- (GSON for the image configuration, I switched to filenames
    because I could add/remove images faster in that way)
- Maven as my build tool    

 