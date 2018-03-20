# Capstone-Project
Udacity Capstone Project

// TODO: 20/03/2018: AddNoteActivity
AFTER TAKE A PICTURE:
	- save picture path in table "images" with column noteId = -1 (PENDING_NOTE_ID)
AFTER PRESS SAVE BUTTON:
	- save note information in table "notes" and use new added record id
	- to update all rows in table "images" with noteId equals to PENDING_NOTE_ID
	- set first picture as thumbnail - COLLUMN_THUMNAIL=1
IF BACK BUTTON PRESS
	- if has any picture taken:
		- remove record/s from table "images" with PENDING_NOTE_ID/s
		- remove file/s from external storage
