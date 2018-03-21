# Capstone-Project
Udacity Capstone Project

// TODO: Possibility to delete thumbnail
THUMBNAIL
THUMBNAIL --> OnLongClick:
	- set check box vissibility to VISIBLE in all paths,
	  no matter which thumbnail has clicked
	- show Delete button, but disable it before at least one check box
	  selected
	- use back button to disable checkable state (if has at least one 
          selected thumbnail otherwise keep usual behaviour of back button).
DELETE BUTTON --> CLICKED
	- delete selected paths from table "images" and
	- delete images from external storage


ADD or EDIT ACTIVITY --> BACK BUTTON PRESSED
	- delete images that doesn't save in table "images" from 
	  external storage (asynchrouniously)
	- and go to parent activity