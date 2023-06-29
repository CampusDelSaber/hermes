# hermes

The Maps Navigation app is an intuitive navigation tool designed exclusively for mobile devices. 
It provides a unique and exclusive navigation experience, allowing users to get directions, 
explore places of interest and calculate optimal routes in real time.






































##Repo Structure


classes
 └── com
 └── isc
 └── hermes
 ├── AboutUs.class
 ├── AccountInformation.class
 ├── ActivitySelectRegion.class
 ├── BuildConfig.class
 ├── controller
 │  ├── authentication
 │  │  ├── AuthenticationFactory$1.class
 │  │  ├── AuthenticationFactory.class
 │  │  ├── AuthenticationServices.class
 │  │  ├── GoogleAuthentication$1.class
 │  │  ├── GoogleAuthentication$2.class
 │  │  ├── GoogleAuthentication.class
 │  │  └── IAuthentication.class
 │  ├── CurrentLocationController.class
 │  ├── FilterController$1.class
 │  ├── FilterController$2.class
 │  ├── FilterController.class
 │  ├── GenerateRandomIncidentController$1.class
 │  ├── GenerateRandomIncidentController.class
 │  ├── IncidentDialogController$1.class
 │  ├── IncidentDialogController.class
 │  ├── IncidentFormController$1.class
 │  ├── IncidentFormController$2.class
 │  ├── IncidentFormController.class
 │  ├── incidents
 │  │  ├── IncidentPointVisualizationController$1.class
 │  │  └── IncidentPointVisualizationController.class
 │  ├── IncidentsGetterController.class
 │  ├── interfaces
 │  │  └── MapClickConfigurationController.class
 │  ├── LocationPermissionsController.class
 │  ├── MapPolygonController.class
 │  ├── MapWayPointController.class
 │  ├── offline
 │  │  ├── CardViewHandler.class
 │  │  ├── OfflineDataRepository.class
 │  │  ├── RegionDeleter.class
 │  │  ├── RegionDownloader$1.class
 │  │  ├── RegionDownloader.class
 │  │  ├── RegionLoader.class
 │  │  ├── RegionObservable.class
 │  │  └── RegionObserver.class
 │  ├── PolygonOptionsController.class
 │  ├── PopUp
 │  │  ├── DialogListener.class
 │  │  ├── PopUp.class
 │  │  ├── PopUpDeleteAccount.class
 │  │  ├── PopUpEditAccount.class
 │  │  ├── TextInputPopup.class
 │  │  └── TypePopUp.class
 │  ├── SearcherController$1.class
 │  ├── SearcherController.class
 │  ├── Utiils
 │  │  └── ImageUtil.class
 │  └── WaypointOptionsController.class
 ├── database
 │  ├── AccountInfoManager.class
 │  ├── ApiHandler.class
 │  ├── ApiRequestHandler.class
 │  ├── ApiResponseParser.class
 │  ├── IncidentsDataProcessor.class
 │  └── IncidentsUploader.class
 ├── generators
 │  ├── CoordinateGen.class
 │  ├── CoordinateParser.class
 │  ├── CoordinatesGenerable.class
 │  ├── IncidentGenerator.class
 │  ├── LinestringGenerator.class
 │  ├── PointGenerator.class
 │  └── PolygonGenerator.class
 ├── MainActivity$1.class
 ├── MainActivity.class
 ├── model
 │  ├── CurrentLocationModel.class
 │  ├── graph
 │  │  ├── Edge.class
 │  │  ├── Graph.class
 │  │  └── Node.class
 │  ├── incidents
 │  │  ├── Geometry.class
 │  │  ├── GeometryType.class
 │  │  ├── Incident.class
 │  │  ├── IncidentGetterModel.class
 │  │  ├── IncidentType.class
 │  │  └── PointIncident.class
 │  ├── LocationPermissionsModel.class
 │  ├── navigation
 │  │  └── Route.class
 │  ├── Radium.class
 │  ├── RegionData.class
 │  ├── Searcher.class
 │  ├── User$1.class
 │  ├── User.class
 │  ├── Utils
 │  │  ├── ImageUploaderToServerImgur.class
 │  │  ├── ImageUploaderToServerImgur$ImgurUploadTask.class
 │  │  ├── IncidentsUtils.class
 │  │  ├── MapPolyline.class
 │  │  └── Utils.class
 │  └── WayPoint.class
 ├── OfflineMapsActivity.class
 ├── requests
 │  └── geocoders
 │      ├── Geocoder.class
 │      ├── Geocoding.class
 │      ├── ReverseGeocoding.class
 │      └── StreetValidator.class
 ├── SearchViewActivity.class
 ├── SignUpActivityView.class
 ├── UserSignUpCompletionActivity.class
 ├── utils
 │  ├── Animations.class
 │  ├── CoordinatesDistanceCalculator.class
 │  ├── DijkstraAlgorithm.class
 │  ├── GeoJsonUtils.class
 │  ├── IncidentsManager.class
 │  ├── ISO8601Converter.class
 │  ├── LocationListeningCallback.class
 │  ├── MapClickEventsManager.class
 │  ├── MapConfigure.class
 │  ├── MarkerManager.class
 │  ├── offline
 │  │  ├── MapboxOfflineManager.class
 │  │  └── OfflineUtils.class
 │  ├── SearcherAdapter.class
 │  ├── SearcherAdapterUpdater.class
 │  ├── SearcherViewHolder$1.class
 │  ├── SearcherViewHolder.class
 │  ├── SharedSearcherPreferencesManager.class
 │  └── WayPointClickListener.class
 └── view
 ├── GenerateRandomIncidentView.class
 ├── IncidentTypeButton.class
 ├── MapDisplay.class
 ├── MapPolygonStyle.class
 └── OfflineCardView.class

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

