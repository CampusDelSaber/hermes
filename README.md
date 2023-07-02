# Hermes

---
## Description
The Maps Navigation app is an intuitive navigation tool designed exclusively for mobile devices. 
It provides a unique and exclusive navigation experience, allowing users to get directions, 
explore places of interest and calculate optimal routes in real time.

### Sing Up
This section is about register and login your account to the app, if you aren't registered, 
you need to sign up, but if you already signed up, just log in.

### Map Creation
After sign up or log in, the map displays with streets, avenues,
certified places, its names and every country information.

### Map Personalization
The map can be personalized changing the way the map is displayed, like only streets and avenues,
map by satellite image and dark color map.

### Report Incidents
There's incidents that can be reported, like traffic accident, it can be generated randomly only putting
the number of incidents and radius to generate.

### Navigation
You can put points on the map, where based on your position, the trip is calculated depending on whether the trip will be by car,
bicycle or on foot, the trip is traced from your position to the indicated position.

### Offline Mode
Offline mode appears when there's no internet on the mobile, zones of the map can be downloaded,
for use it later when there's no internet, downloaded zones can be renamed and deleted.

---

## Dependencies

The project has the following dependencies:

- `Java 17:`

The project uses Java 17 to compile and run.

- `Mapbox Android SDK (version 9.7.1):`

The Mapbox SDK is used for integrating maps into the Android application.

- `Glide (version 4.15.1):`

Glide is used for image loading and display in the Android application.

- `Google Play Services Auth (version 20.5.0):`

Google Play Services Auth is used for authentication in the Android application.

- `MongoDB (version 4.5.0):`

MongoDB is used as the database for the project.

---

## Installation

### Clone the repository in Android Studio

After installing Android Studio and running it, this window will appear:

[![image.png](https://i.postimg.cc/25Z0ZD3J/image.png)](https://postimg.cc/Y4t65T6f)

Inside Android Studio, click this button:

[![image.png](https://i.postimg.cc/FRtNyNpJ/image.png)](https://postimg.cc/2qxsmpPz)

Paste this link (https://github.com/CampusDelSaber/hermes.git) into the text field :

[![image.png](https://i.postimg.cc/TYnnTR7N/image.png)](https://postimg.cc/CzLRNVVb)

The directory can be changed where you want. When the download directory has been chosen,
click on this button:

[![image.png](https://i.postimg.cc/2jh1Z0QV/image.png)](https://postimg.cc/c611qM5S)

You can select Trust the project or open in safe mode (It is recommended to select Trust the project):

[![image.png](https://i.postimg.cc/SQ6KsJ49/image.png)](https://postimg.cc/XXvW1NpN)

It is necessary to load the gradle Project to load configuration and dependencies :

[![image.png](https://i.postimg.cc/7PmpmkMc/image.png)](https://postimg.cc/RqJsZ2YQ)

Going to any class, it is necessary to define the SDK, load Java 17 SDK:

[![image.png](https://i.postimg.cc/DyhPJJX3/image.png)](https://postimg.cc/5jPCDtgs)

Now It's ready to execute the app.

---

##Repo Structure

- classes
  - com
    - isc
      - hermes
        - AboutUs
        - AccountInformation
        - ActivitySelectRegion
        - BuildConfig
        - controller
          - authentication
            - AuthenticationFactory
            - AuthenticationServices
            - GoogleAuthentication
            - IAuthentication
          - CurrentLocationController
          - FilterController
          - GenerateRandomIncidentController
          - IncidentDialogController
          - IncidentFormController
          - incidents
            - IncidentPointVisualizationController
          - IncidentsGetterController
          - interfaces
            - MapClickConfigurationController
          - LocationPermissionsController
          - MapPolygonController
          - MapWayPointController
          - offline
            - CardViewHandler
            - OfflineDataRepository
            - RegionDeleter
            - RegionDownloader
            - RegionLoader
            - RegionObservable
            - RegionObserver
          - PolygonOptionsController
          - PopUp
            - DialogListener
            - PopUp
            - PopUpDeleteAccount
            - PopUpEditAccount
            - TextInputPopup
            - TypePopUp
          - SearcherController
          - Utils
            - ImageUtil
          - WaypointOptionsController
        - database
          - AccountInfoManager
          - ApiHandler
          - ApiRequestHandler
          - ApiResponseParser
          - IncidentsDataProcessor
          - IncidentsUploader
        - generators
          - CoordinateGen
          - CoordinateParser
          - CoordinatesGenerable
          - IncidentGenerator
          - LinestringGenerator
          - PointGenerator
          - PolygonGenerator
        - MainActivity
        - model
          - exceptions
            - CorruptedTokenException
            - InvalidUserRoleException
          - CurrentLocationModel
          - graph
            - Edge
            - Graph
            - Node
            - GraphManager
          - incidents
            - Geometry
            - GeometryType
            - Incident
            - IncidentGetterModel
            - IncidentType
            - PointIncident
          - LocationPermissionsModel
          - navigation
            - Route
            - NavigationTrackerTools
            - RouteEstimatesManager
            - RouteSegmentRecord
            - TransportationType
            - UserOutsideRouteException
            - UserRouteTracker
          - Radium
          - RegionData
          - Searcher
          - User
          - Utils
            - ImageUploaderToServerImgur.class
            - DataAccountOffline
            - IncidentsUtils
            - MapPolyline
            - Utils
          - WayPoint.class
        - OfflineMapsActivity.class
        - requests
          - geocoders
            - Geocoder.class
            - Geocoding.class
            - ReverseGeocoding.class
            - StreetValidator.class
        - SearchViewActivity.class
        - SignUpActivityView.class
        - UserSignUpCompletionActivity.class
        - utils
          - Animations.class
          - CoordinatesDistanceCalculator.class
          - DijkstraAlgorithm.class
          - GeoJsonUtils.class
          - IncidentsManager.class
          - ISO8601Converter.class
          - LocationListeningCallback.class
          - MapClickEventsManager.class
          - MapConfigure.class
          - MarkerManager.class
          - offline
            - MapboxOfflineManager.class
            - OfflineUtils.class
          - SearcherAdapter.class
          - SearcherAdapterUpdater.class
          - SearcherViewHolder$1.class
          - SearcherViewHolder.class
          - SharedSearcherPreferencesManager.class
          - WayPointClickListener.class
        - view
          - GenerateRandomIncidentView.class
          - IncidentTypeButton.class
          - MapDisplay.class
          - MapPolygonStyle.class
          - OfflineCardView.class

##Program usage

- ### SEARCH
Above the map display, there is a search engine in which the user can enter the location he/she wants 
and then the searched place will be displayed on the map.


![Search](https://i.postimg.cc/cCFqMVtZ/Screenshot-from-2023-06-28-23-58-58.png)

- ### SEARCH FILTERS.

Below the search engine you can see the different options for the type of services that the 
user wishes to search for, such as restaurants, hotels, parks, etc.

![Search](https://i.postimg.cc/B6LbLBjm/Screenshot-from-2023-06-29-00-08-10.png)

- #### REPORT
  Clicking on a point on the route displays a menu of options for reporting incidents, 
  traffic and natural disasters. The user must choose the type of report he/she wants to make 
  and click on the desired option.

![Report](https://i.postimg.cc/QCfy21br/Screenshot-from-2023-06-29-00-10-53.png)

 To continue the report, it will show us the different characteristics that we can fill in to report 
 such as the type of incident, the user can write the reason and the estimated time, and finally we 
 have the options to accept and cancel.

![Report](https://i.postimg.cc/DzzdNHNm/Screenshot-from-2023-06-29-00-15-25.png)

- #### NAVIGATION:

 To start a route, you must press the navigate option.

![Navigation](https://i.postimg.cc/fRPj6BQK/Screenshot-from-2023-06-29-00-22-12.png)

 Next, the navigation options will scroll, we will be able to select the starting point, the user 
 can choose to do it from the current location or choose a point on the map, the type of transport 
 and the start and end coordinates of the route will be displayed.

![Navigation](https://i.postimg.cc/vHQJBHgK/Screenshot-from-2023-06-29-00-23-21.png)



### Contributors

Teams that contributed to the project:

- ## Team A:

 - Carlos Antonio Ballester Paniagua
 - Cristian Sebastian Barra Zurita
 - Camila Bustos Valverde
 - Karina Aguirre Janco
 - Jhael Katherine Arce Chavez
 - Axel Javier Ayala Siles
 - Juan Pablo David Arequipa
 - Elvis Jose Castro Huanca
 - Santiago Caballero Manzaneda

- ## Team B:

 - Victor Alejandro Cespedes Cartagena
 - Gabriel Santiago Concha Saavedra
 - Jeferson Jhovani Coronel Lavadenz
 - Samuel Alejandro Escalera Herrera
 - Daniel Pablo Espinoza Escalera
 - Diego Hernan Figueroa Sevillano
 - Emanuel Javier Galindo Corpa
 - Denis Jorge Gandarillas Delgado
 - Gabriela Garcia Villalobos

- ## Team C:

 - Gaston Gutierrez Condori
 - Salet Yasmin Gutierrez Nava
 - Jorge Ignacio Heredia Bazoalto
 - Leonardo Alberto Herrera Rosales
 - Juan Carlos Hidalgo Sosa
 - Luiggy Mamani Condori
 - Fernando Mauricio Mamani Navarro
 - Fabian Romero Claros
 - Victor Leon Villca Silva