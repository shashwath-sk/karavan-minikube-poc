# Notes Application Using Karavan Toolkit

To understand the capabilities that Karavan toolkit provide, we made a Notes CRUD API. By creating a systematic workflow & configuration of karavan components we created a REST api that allows a user to create, read, update & delete notes which are stored in a MYSQL databaseserver. Along with this we also have integrated Authentication & Authorization services which works with Keycloak (an Open SourceIdentity and Access Management Service).

## Work-flow for app in brief
Number of REST APIS created -> 2
- Auth Service consists of 3 routes for </br>
  * login </br>
  * register </br>
  * authorisation
- Notes Service consists of 6 routes for 
  * getAllNotes
  * getNotebyId
  * createNote
  * updateNotePartially
  * updateNoteCompletely
  * deleteNoteById

here is a snapshot for reference -
<img width="820" alt="Screenshot 2023-04-18 at 11 57 29 PM" src="https://user-images.githubusercontent.com/86059764/232869881-99060250-e9ed-4f8c-b20e-8e90b266bd24.png">

## Problems faced using the toolkit
- Lag noticed while configuring karavan components (in VScode plugin) which increases with the increase of LOC in yaml file
