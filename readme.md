# Pre-work - SimpleToDo

SimpleToDo is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: Madhurima Roy

Time spent: 40 hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:
* [x] set App icon
* [x] Implemented Date picker for setting the due date in edit DialogFragment.
* [x] Used db schema and contract class for good readability(https://developer.android.com/training/basics/data-storage/databases.html)
* [x] Name cannot be null. Toast message display on clicking add button with null or empty new item. Same in edit view.
* [x] Color coded Priority.(Red - high, Orange - Medium, Blue - Low)
* [x] Toolbar implementation

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

Video Walkthrough (http://i.imgur.com/ymbU6xo.gifv)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.
- changing colors for drawable
- The implementation of DialogFragment. Spent > 10 hrs
- implementing DatePicker from DialogFragment
- Could not add download icon to toolbar. The code for toolbar set and read, write to file is present.

## License

    Copyright [2017] [Madhurima Roy]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.