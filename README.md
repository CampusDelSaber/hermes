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

