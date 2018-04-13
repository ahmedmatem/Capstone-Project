# Capstone-Project
Udacity Capstone Project

For very first time when application starts, worker must authenticate yourself by username and password given from team manager. The successful authentication will load Price and Place no editable lists which are needed to add notes. They can be viewed from main menu, clicking on Price list and Place list commands.

The main activity contains a list of all notes created by current worker sorted by date and float action bar button that allows adding new note.

Note card contains:

    - information for dimension, location and price;
    - action buttons for viewing and editing;
    - all above information on the top of image background (if such exists)

Each note could be viewed and edited. The last action is available before upload command happened. Once it happened note could not be changed by app requirements.

Add new note

To add new note at least place and dimension must be chosen and entered. Otherwise add action at the action bar is disabled. Dimension must be in format: width x height { x layer_numbers { x copies}} Image(s) could be added simultaneously or later by switching to note edit mode.