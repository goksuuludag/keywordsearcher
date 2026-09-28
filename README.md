A keyword searching tool that currently searches through plain text, DOC and DOCX files. The program searches for given keyword in given directory and displays the files that contain it as a result.

# Main Screen

The user is welcomed with a blank search bar and an empty result table at the start.

<img width="1520" height="856" alt="asdff_fjdslk" src="https://github.com/user-attachments/assets/eb8a514b-d342-4e78-b829-113ca736fc88" />

## Main Screen Components

<img width="1581" height="905" alt="asdff" src="https://github.com/user-attachments/assets/509b65fb-16b1-42ec-a247-332a45a8d31c" />

1. The button that opens the directory selecting screen. The given keyword will be searched inside this directory.

2. The text field that the keyword to be searched is typed in.

3. The button that triggers the search. This also opens a recreational area to be enjoyed(?) during the search in case it takes a while.

4. The button that opens the filtering options screen. The filters to be applied to the result set could be set before or after the searching process.

5. The button that removes the current filter.

6. The table that contains the result set after a search. Rows can be double clicked to display the file on the file explorer.

7. The footer that displays the whole and displaying result set count. The current row index being hovered over is also displayed on the right side.

# Extra components

As mentioned before, there are extra components that could be utilized or enjoyed(?) based on user's preference or needs.

## The Recreational Area

This dialog opens after a search is triggered. Based on the scope of the search it could take a minute, so there are a few ways provided by this area that will help the user pass the time as the search gets completed.

There could also be times where the user may want to cut the search short, which is also supported. Cancelling the search will display unfinished results.

<img width="1266" height="857" alt="consolee" src="https://github.com/user-attachments/assets/658932f8-0063-41a2-858f-1b1e7124758a" />

### The Recreational Area Components

<img width="1313" height="902" alt="consolee_2" src="https://github.com/user-attachments/assets/2555fba9-09c7-4f3f-bc24-30d8b83ce703" />

1. The button that switches the recreational area. There are currently a few choices:

    - A console screen to watch the errors pile up as search progresses,
    - A canvas to scribble some things down,
    - Minesweeper!!!
3. The recreational area itself.
4. The button that will close the dialog and cancel the search. This may still display an unfinished list of results on the main screen.
5. The button to close the dialog after the search is completed. This will not be enabled while the search is in progress.
6. The exit button. This will also close the dialog and cancel the search.

## The Filter Dialog

This dialog displays filter options to apply to the eventual set of results that the user will get after the search is completed. These options may be set before or after the search.

<img width="446" height="277" alt="filtre" src="https://github.com/user-attachments/assets/6bb0e960-1d0f-456a-8b44-b87c897ed549" />

### The Filter Dialog Components

<img width="481" height="317" alt="filtre_2" src="https://github.com/user-attachments/assets/09a99c5d-95b1-402e-b887-d550c39931ac" />

1. The text to search among the file names (not whole paths) of the result set is typed here.
2. Possible file types the result set will include. Unchecking a file type will exclude that whole type in the result set.
3. The buttons that display the file extensions the corresponding file type includes. These extensions can be individually ticked off to be excluded from the result set. These buttons are not enabled while the corresponding type is ticked off.
4. The button that applies the filter with given options. The selections made on the dialog are not applied until this button is clicked.
5. The button that closes the filter dialog. This button does not get rid of the current filter applied to the result set, if there is any. This button also does not reset the selections made to the filtering options itself, the remove filter button on the main screen does.

## Some Things to Mention

The main file types and the extensions said types include come from the main configuration file (/resources/config/app.properties). I do not recommend touching the DOC and DOCX types and extensions they include but the plain text extensions can be added and removed as needed, as long as any new extension added corresponds to files that contain actual plain text. Otherwise the plain text search will not work on those files!

Minesweeper grid and mine count settings can also be adjusted.
