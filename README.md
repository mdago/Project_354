# Project 354

Update 26 March 2017:
-added images next to each stock  
-added custom font  
-choosing a timespan will cause the graph to update with new values  
-choosing moving averages will add the lines to the graph  
-linechart was changed to an areachart  
-added push notifications warning user of potential problems  
-added program icon when GUI is running  
-bug fixes

Here are the files required for the project. Iteration 2 under "src" is where the work needs to be done. The file "COMP-354.zip" is
a zip folder that can be directly imported to eclipse for easy editing.

The 'external' folder contains the Yahoo API jars that need to added to the project's build path so that the 'Display.java' file
can connect to Yahoo Finance. If you're unsure how to do this, just download the zip file and import it into eclipse to save any
potential headaches.

This read me will contain a list of things currently that need to fixed or added.

At this moment, some tasks that are required:
  1) Code cleanup (should be done either at end of implementation or during editing)
  2) Log In (requires a log-in that reads from a user database file to determine correct log in credentials. Also requires an
     option to create a new user
  3) Log Out (likely will be initiated by clicking a log out button on the main gui in which case the user is brought back to the
     log in page
  4) Title page?? (everyone should vote on this)
  5) Buy/Sell signals on crossing of moving average and stock prices
  6) Settings page to change password? The interview answers posted by the teacher mentions user AND password management
  7) Conversion to MVC
  8) Extensive Testing
  
These are some task currently requiring working. This list may be lengthened or shortened as need be.

If anyone has any comments, modifications, additions or whatever then feel free to add it here so everyone can see and offer 
their opinions.

A screenshot of the current state (23 March 2017) of the program will be provided for reference in the repo.

Happy Coding!
