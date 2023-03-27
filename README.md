# webapp9 | TripScanner

## Index

1. [Phase 0 additions](#phase0)
2. [Phase 1 additions](#phase1)
3. [Phase 2 additions](#phase2)
4. [Phase 3 additions](#phase3)

<a name="phase0"></a>
## Phase 0 additions

### Team members and info

Here you can find information about the members that form the developer team for TripScanner

| Name        | Surname        | URJC Mail                        | Github                                               |
| -           | -              | -                                | -                                                    |
| Marcos      | Ferrer Zalve   | m.ferrer.2020@alumnos.urjc.es    | [@LovetheFrogs](https://github.com/LovetheFrogs)     |
| Anna        | Trofimova      | a.trofimova.2020@alumnos.urjc.es | [@anna-trofimova](https://github.com/anna-trofimova) |
| Iván        | Penedo Ventosa | i.penedo.2020@alumnos.urjc.es    | [@xIvqn](https://github.com/xIvqn)                   |
| Adaya María | Ruíz Mayoral   | a.ruiz.2020@alumnos.urjc.es      | [@AdayaUwU](https://github.com/AdayaUwU)             |

### Team Organization

The team will be organized using the intregration with the native GitHub Projects. That way, team members can use GitHub Issues and other GitHub options to automate the workflow of the web application development. GitHub Project can be accessed through the following [link](https://github.com/orgs/CodeURJC-DAW-2022-23/projects/3/views/1?layout=board).

### App features

Here you can see the features of our web application TripScanner.

#### Entities

We'll have the following entities **users**, **places**, **destinations** and **itineraries**.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://user-images.githubusercontent.com/72264031/217202549-74b12ef3-274f-477a-935e-f8d553fb5e04.svg">
  <source media="(prefers-color-scheme: light)" srcset="https://user-images.githubusercontent.com/72264031/216968255-32aef1b5-eaf1-45b7-acb8-776c2c00c8fc.svg">
  <img alt="Shows an illustrated sun in light mode and a moon with stars in dark mode." src="https://user-images.githubusercontent.com/25423296/163456779-a8556205-d0a5-45e2-ac17-42d089e3c3f8.png">
</picture>

#### User privileges

Our web app will have the following types of users:

- **Unregistered users:** Not registered users can search for itineraries, destinations and places, but can not save them anywhere.

- **Registered users:** Registerd users can do the same as unregistered ones, but they can also create new itineraries (either public or private), save public ones and edit them privately. Registered users also have a personal account in the website where they can create a biography and change their profile picture and how they appear to other users.

- **Administrator:** Administrators can do all the previous tasks. They can also edit the public itineraries and create new ones, add new places and destinations.

#### Images

Out of our four entities, three of them will have images asociated to them. Users will have a profile picture of their liking, places will have a photo of them and destinations will have a picture of an iconic landmark they have.

#### Graphs

Inside each destination, users will be able to see a bar graph containing the most popular places inside them. There will also be a graph in the main page where you can see the most popular destinations.

#### Aditional technologies

The app will be able to send confirmation emails to users when they register, and anytime they save or edit an itinerary.

It will also be able to generate pdf documents with a selected itinerary that a user has saved.

Our app will use the OpenStreetMap API which is an Open Source map API. We will also be using SkyScanner's and TripAdvisor API to get information about hotels, destinations and flights.

#### Advanced algorithms

TripScanner will have a selection of algorithms. These are:

- An algorithm to search for the shortest path between places of an itinerary.
- A search algorithm with different search criteria, for both places and destinations.

<a name="phase1"></a>
## Phase 1 aditions

### Webpage screenshots

Here you can see screenshots of the webpage developed during phase 1.

![image](https://user-images.githubusercontent.com/102818341/218514617-db631ba1-2f02-4f34-8a24-52230c3a11cf.png)

The main page will show popular itineraries, destinations and places. Those are the ones most seen by the users. It will also allow for manual search using the search bar, and login into your account.

![image](https://user-images.githubusercontent.com/102818341/218514955-bb821902-daf1-4766-a1d1-510619b4bf83.png)

The login popup allows users to log into their account or create an account if they don't have one.

![image](https://user-images.githubusercontent.com/102818341/218515154-eb5aa2c4-0f4b-4ba8-a12d-54fe4da58ce8.png)

The registration page allows for unregistered users to create a new account to get all the features of TripScanner.

![image](https://user-images.githubusercontent.com/102818341/218515601-bed69071-20ea-440f-a1e4-00f3b87c5f17.png)

Results are shown after using the search bar and can be filtered by public results or private ones you have created as a registered user. You also have a number of filters to narrow down the search results.

![image](https://user-images.githubusercontent.com/102818341/218515651-1c54e6c2-a9b2-4128-9c1a-ce9a791aafe1.png)

The details of an item are shown when you click on it and show some more information about it.

![image](https://user-images.githubusercontent.com/72264031/218572147-eb1ee025-adb0-480e-bf65-70b62e403fb0.png)

Registered users can see their profile information here, as well as editing it, and their itineraries.

![image](https://user-images.githubusercontent.com/72264031/218571911-a75a0a10-7461-4631-a2e8-dff7bf44f2e4.png)

At last, administrators can see a panel with the options they have. This panel (as well as the user information one) is not accesible yet by navigating through the webpage.

### Navigation diagram

![Diagrama DAW-3-1](https://user-images.githubusercontent.com/102818341/218516781-fdaf6302-f9dd-4d04-948e-cc64230ff477.png)

<a name="phase2"></a>
## Phase 2 additions

### Webpage screenshots

![image](https://user-images.githubusercontent.com/102818341/224549052-005ae1bb-13c8-4fbc-9cd5-66962c2e23da.png)

The main page will show popular itineraries, destinations and places. Those are the ones most seen by the users. It will also allow for manual search using the search bar, and login into your account.

![image](https://user-images.githubusercontent.com/102818341/224549082-76ad642d-906c-471d-8a36-00970db1ffdf.png)

The login page allows users to log into their account or create an account if they don't have one.

![image](https://user-images.githubusercontent.com/102818341/224549112-f2c45238-cae7-4b8f-b94a-d2668e73e84c.png)

The registration page allows for unregistered users to create a new account to get all the features of TripScanner.

![image](https://user-images.githubusercontent.com/72264031/224675126-39fdfa93-8b74-40cb-9567-81f8a099f7e1.png)

The search page allows to search for an specific keyword, as well as filter the results showed.

![image](https://user-images.githubusercontent.com/102818341/224549298-9d24e39b-45c0-4067-8116-dcb0844a1ec2.png)

The details of an item are shown when you click on it and show some more information about it.

![image](https://user-images.githubusercontent.com/102818341/224549334-42cb05fc-3712-4cf1-b3e4-caf134d851f5.png)

Registered users can see their profile information here, as well as editing it, and their itineraries.

![image](https://user-images.githubusercontent.com/102818341/224549525-3cdb6ae9-bb35-47fd-882b-a27ee974574c.png)

This page shows a registered user created and saved itineraries, as well as allowing them to create new ones.

![image](https://user-images.githubusercontent.com/102818341/224549663-5dc9c7a9-7b47-45f3-ad26-cf805c8bcf6d.png)

Administrators can see a panel with the options they have. Here they can filter by users, itineraries, places or destinations, as well as editing existing entries or creating new ones.

![image](https://user-images.githubusercontent.com/102818341/224549788-e7bb0aa0-6317-4dce-9ffa-d9e26cc8712f.png)

The views for adding a new element or user to the webpage are all the same, so the add new destination one is shown for reference.

![image](https://user-images.githubusercontent.com/102818341/224549893-6b228f37-0db1-4e85-8566-c2ddd4f843d4.png)

Users can edit the itineraries they own, similarly to admins being able to edit all itineraries.

![image](https://user-images.githubusercontent.com/102818341/224549948-be162308-f76f-49c4-b060-a170f1b45025.png)

They can also edit their profile. This view is similar to the register page.

![image](https://user-images.githubusercontent.com/102818341/224549588-b00afc57-b876-42ab-8aea-e75e1ea532e9.png)

Users can generate a pdf of an itinerary and download it.

![image](https://user-images.githubusercontent.com/102818341/224550026-1d97af3c-8a15-4c8a-8a88-9367a68f5e8c.png)

An email is sent when a user registers to the app.

![image](https://user-images.githubusercontent.com/102818341/224550066-5d35e6fe-627c-4e89-b1ce-470d6f41c27e.png)

An email is also sent if you change the email you have linked to your account.

![navigation](https://user-images.githubusercontent.com/102818341/224582389-2e89b9a6-ecd8-405c-bc4d-1f93c23c08c3.svg)

Finally, above is the updated navigation diagram of the webpage.

### Executing instructions.

In order to execute the app, you will need to follow this steps:

1. Install all the needed dependencies. This are:
    - Git bash. Use command `sudo apt get install git` to install.
    - Java version 11. Use command `sudo apt get install openjdk-11-jdk` to install.
    - Maven. Use command `sudo apt get install maven` to install.

2. Once you have all the required dependencies, clone the repo from github using the commad `git clone https://github.com/CodeURJC-DAW-2022-23/webapp9` and login to your github account.

3. After cloning the repo to your local machine, go to the directory where the repository was cloned and navigate to the root directory of the spring-boot project by using the command `cd webapp9/TripScanner`.

4. Finally, use the command `mvn spring-boot:run` to boot up the project and navigate to <https://localhost:8443> to use the website.

### Database entity diagram

Below there is an entity-relationship diagram that shows the database entities, its fields and the relations between them.

![Entity-Relationship Diagram](https://user-images.githubusercontent.com/102818341/224564564-71b8c637-e8cc-4bf5-8cbf-e0659c1897e3.svg)

### Class and templates diagram

This is the class diagram of TripScanner.

![Diagrama de clases y templates](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/diagrams/svgs/templates2.svg)

### Members participation

Here, each member of the team shows what they have contributed to during the development of this phase.

1. [Adaya María Ruiz Mayoral](https://github.com/AdayaUwU)
    - **Description of tasks:** In this phase, I've developed the functionality to show item's details and its related information, interface and creation of user itineraries and the admin management view and functionality, including editing, deleting and adding all kinds of items.
    - **5 biggest commits:** These commits are:
        - [Feat: Delete and edit Places, Itineraries, Destinations and Users](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/11d19c92ada593ba3356b5afaf10a844bfc25de2)
        - [Feat: Add Places, Itineraries, Destinations and Users](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/9360ed6761636d8b7b86bc51ae5b24edbd22e6d7)
        - [Feat: Details controller](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/736d3e293765239291bc088b0b49503c5957088d)
        - [Fix: Changed way of showing lists](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/c39605a58a4d547ef14af94f3d3db523a0e88db1)
        - [Feat: Complete my itineraries page](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/736d3e293765239291bc088b0b49503c5957088d)
    - **5 files with most participation:** These files are:
        - [ManagementController](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/ManagementController.java)
        - [PlaceWebController](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/PlaceWebController.java)
        - [DestinationWebController](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/DestinationWebController.java)
        - [UserWebController](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/UserWebController.java)
        - [addEditItem](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/resources/templates/addEditItem.html)

2. [Ivan Penedo Ventosa](https://github.com/xIvqn)
    - **Description of tasks:** I've been in charge of deploying the H2 database at the begining of the stage to start working with it and at the end, when everything was implemented, migrating it to MySQL. Also, I've been developing the pagination section in all pages that required it such as details (for information and reviews for the itineraries) and search results; the controller for downloading database images, aditional technologies (pdf exportation and email sending), private/public itinerary visibility functionality and some diagrams (such as the database entity-relationship diagram) for the documentation. Apart from that, I've been reviewing code and helping others.
    - **5 biggest commits:** Note that I usually upload more commits with less changes each, so I have a lot of contributions but each one has less changes. These commits are:
        - [Feat: Load images on pages](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/98cfbaa6a1adeb4dd533fcb74550d79d67cb59bc)
        - [Feat: Log In forms](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/03548dd901a82f822346aff19d4fbdd3dc9cf7ab)
        - [Feat: Itinerary review pagination](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/6ee2806f59e3355919a15d96786cd8560201541f)
        - [Feat: Search pagination](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/0ca1c2a5e6cc8ca10d149907aded673645fade46)
        - [Feat: PDF exporter class](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/57457eb49da909e7979c78ef18f9d06537e7b52b)

    - **5 files with most participation:** These files are:
        - [/java/../controller/SearchController.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/SearchController.java)
        - [/java/../utils/PdfGenerator.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/java/com/tripscanner/TripScanner/utils/PdfGenerator.java)
        - [/java/../model/*](https://github.com/CodeURJC-DAW-2022-23/webapp9/tree/main/TripScanner/src/main/java/com/tripscanner/TripScanner/model)
        - [/java/../repository/*](https://github.com/CodeURJC-DAW-2022-23/webapp9/tree/main/TripScanner/src/main/java/com/tripscanner/TripScanner/repository)
        - [/resources/../js/search.js](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/resources/static/js/search.js)

3. [Anna Trofimova](https://github.com/anna-trofimova)
    - **Description of tasks:** I developed home page, search page, the necessary algorithms to search for different elements, search filters and the ability to see your own private itineraries. These commits are:
    - **5 biggest commits:** These commits are:
        - [Fixed: filter destination/place/itinerary](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/43ec94d8397a1132830e656def4eed8ba480d6c4)
        - [Added: private itinerary](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/f0ae68bde5f062708e0b531f90cd562a6ea1388f)
        - [Added:sort of search](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/d6330e66e82b075fc3453219549cedf46f81f490)
        - [Fixed: sort by view](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/3e33581a3c8839c4d2d57849710d09289280a5ac)
        - [Fixed:HomeController](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/6ef6cdeb3e5dadb26c648b3aebcdfe34a557385b)
    - **5 files with most participation:** These files are:
      - [SearchControler](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/SearchController.java)
      - [SearcService](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/src/main/java/com/tripscanner/TripScanner/service/SearchService.java)
      - [Search](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/src/main/resources/templates/search.html)
      - [HomePage](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/src/main/resources/templates/index.html)
      - [HomeController](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/HomeController.java)

4. [Marcos Ferrer Zalve](https://github.com/LovetheFrogs)
    - **Description of tasks:** I've developed the secure login and user functionality, and added the missing touches for editing and saving itineraries, as well as adding custom error pages and the graph for the most poppular destinations.
    - **5 biggest commits:** Note that I usually upload more commits with less changes each, so I have a lot of contributions but each one has less changes.These commits are:
        - [Feature: Added secure login](https://github.com/CodeURJC-DAW-2022-23/webapp9/pull/60/commits/c3da1695d8f232c6a9293765bc126bf751f5ff00)
        - [Feature: Graph showing the most visited Destinations](https://github.com/CodeURJC-DAW-2022-23/webapp9/pull/73/commits/c245e2970853ce383d04994f85f06a44fa54de0a)
        - [Feature: Users can copy an Itinerary to their account](https://github.com/CodeURJC-DAW-2022-23/webapp9/pull/66/commits/605bb5ff9de1c6c0521c8d3c739458b25194cb54)
        - [Feature: Users can now edit their pofile data](https://github.com/CodeURJC-DAW-2022-23/webapp9/pull/66/commits/fd4b16fc16c47352a5615cbb246b20ebeaa4e923)
        - [Feature: Unregistered users can now register](https://github.com/CodeURJC-DAW-2022-23/webapp9/pull/60/commits/c8d83f6f48fdd3ced6129e4bd8a078fb8076f731)
    - **5 files with most participation:** These files are:
        - [SignController.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/SignController.java)
        - [ProfileController.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/ProfileController.java)
        - [CSRFHandlerConfiguration.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/java/com/tripscanner/TripScanner/security/CSRFHandlerConfiguration.java)
        - [WebSecurityConfig.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/java/com/tripscanner/TripScanner/security/WebSecurityConfig.java)
        - [profile.html](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/main/TripScanner/src/main/resources/templates/profile.html)
        
<a name="phase3"></a>
# Phase 3 additions


### API REST Documentation

The API REST documentation has been generated using OpenAPI specification. As so, it has been automatically generated into a `.yml` and `.html` file. Both files are accessible from this repository, through the following links:

* [**api-docs.yml**](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/api-docs/api-docs.yaml)
* [**api-docs.html**](https://raw.githack.com/CodeURJC-DAW-2022-23/webapp9/dev/TripScanner/api-docs/api-docs.html)

### Updated classes and templates diagram

The classes and templates diagram has been updated with the new `@RestController` classes. Please note that the `@Service` classes have been implemented before, in the *Stage 2*.

![Phase3DAW](https://user-images.githubusercontent.com/103607990/228073072-d75470f1-8616-49de-88e1-09d99020b0db.svg)

### Docker

In the following sections, it'll be explaned everything about the Docker usage in the application, including the execution of the dockerized webapp and the building of the docker image

#### Executing dockerized application

To run the Docker image using the `docker-compose.yml` file you might need to install Docker first:

* Windows:
  * Microsoft Windows 10 Professional or Enterprise 64-bit or Windows 10:
    * https://store.docker.com/editions/community/docker-ce-desktop-windows
    * https://docs.docker.com/docker-for-windows/install-windows-home/
* Linux:
  * Ubuntu: https://store.docker.com/editions/community/docker-ce-server-ubuntu
  * Fedora: https://store.docker.com/editions/community/docker-ce-server-fedora
  * Debian: https://store.docker.com/editions/community/docker-ce-server-debian
  * CentOS: https://store.docker.com/editions/community/docker-ce-server-centos
* Mac:
  * Apple macOS Mojave (10.14) or newer:
    * https://store.docker.com/editions/community/docker-ce-desktop-mac
    * Apple M1 chip: https://docs.docker.com/docker-for-mac/apple-m1/

These are the instructions to run it: 

1. Open a terminal.
2. Clone this repository using `git clone https://github.com/CodeURJC-DAW-2022-23/webapp9.git`.
3. Go to the docker folder using `cd webapp9/docker`.
4. Run the `docker-compose.yml` file using `docker-compose up`.
5. Now you've got both webapp and API running on `https://localhost:8443`.


#### Building docker image

To build the Docker image using the `create_image.sh` file you might need to install Docker first as the previous step shows.

After that, you should:

1. Cretae an account on DockerHub and log-in locally.
2. Open a terminal.
3. Clone this repository using `git clone https://github.com/CodeURJC-DAW-2022-23/webapp9.git`.
4. Go to the docker folder using `cd webapp9/docker`.
5. Run `./create_image.sh %username%/tripscanner-webapp` replacing %username% with your DockerHub username.


### Members participation

Here, each member of the team shows what they have contributed to during the development of this phase.

1. [Adaya María Ruiz Mayoral](https://github.com/AdayaUwU)
    - **Description of tasks:** In this phase I have been in charge of the admin functionality for places and users in the API. Initially I was going to work on Railway too, but since it was removed, I was also in charge of the documentation (README, diagrams and API) and of checking that everything was working properly and fixing errors.
    - **5 biggest commits:** These commits are:
        - [Feat: Upload and edit image methods](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/e5f1175200b391c4c7d176cc0f168a28246132e0)
        - [Feat: get, add, edit and delete Places](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/9a201c60ef490b6e20df1bdcd8bb13409e63b575)
        - [Feat: get, add, edit and delete Users](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/d57f59571e951ba7a65f09895347f891ae8bb051)
        - [Feat: API upload and edit image](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/32214c5a7d44a92e04e50ccd4ccfe86de271a9cb)
        - [Feat: authentication needed URLs](https://github.com/CodeURJC-DAW-2022-23/webapp9/commit/139106c173d97eed396c5a35cbd47f8300e8103c)
    - **5 files with most participation:** These files are:
        - [UserManagementRestController.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/restController/UserManagementRestController.java)
        - [PlaceManagementRestController.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/src/main/java/com/tripscanner/TripScanner/controller/restController/PlaceManagementRestController.java)
        - [RestSecurityConfig.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/src/main/java/com/tripscanner/TripScanner/security/restSecurity/RestSecurityConfig.java)
        - [PlaceDTO.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/src/main/java/com/tripscanner/TripScanner/model/rest/PlaceDTO.java)
        - [UserDTO.java](https://github.com/CodeURJC-DAW-2022-23/webapp9/blob/dev/TripScanner/src/main/java/com/tripscanner/TripScanner/model/rest/UserDTO.java)

2. [Ivan Penedo Ventosa](https://github.com/xIvqn)
    - **Description of tasks:** 
    - **5 biggest commits:** These commits are:
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
    - **5 files with most participation:** These files are:
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)

3. [Anna Trofimova](https://github.com/anna-trofimova)
    - **Description of tasks:** 
    - **5 biggest commits:** These commits are:
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
    - **5 files with most participation:** These files are:
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)

4. [Marcos Ferrer Zalve](https://github.com/LovetheFrogs)
    - **Description of tasks:** This phase, I've been in charge of coding the Rest Controllers used by registered users, as well as its documentation. This controllers implement functionalities such as managing your itineraries and creating new ones, and editing your profile info. I've also been in charge of developing the Docker functionalities with [@anna-trofimova](https://github.com/anna-trofimova).
    - **5 biggest commits:** These commits are:
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
    - **5 files with most participation:** These files are:
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)
        - [NAME](URL)


