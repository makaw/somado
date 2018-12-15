# Somado
The application is meant as an attempt to optimize the delivery of goods using the OpenStreetMap data. It is using:
- JSprit library to solve the Vehicle Routing Problem (VRP),
- JXMapViewer2 to render maps,
- OSM Nominatim geocoding service,
- SQLite + Spatialite database,
- JTS library for modelling spatial data (according to the OGC standard)

Application needs Sqlite and Spatialite libraries. It is working in Linux and Windows (also Windows 10 with the "next gen" Spatialite). Just download the binaries from [https://www.gaia-gis.it/gaia-sins/](https://www.gaia-gis.it/gaia-sins/), unpack it and add its path to the PATH system variable. Example sqlite database files are provided in the release section. 
   
Due to OSM Nominatim usage policy, you should put your email address in the resources/conf.default.properties file (key email.nominatim).  
   
----------

![Lista zamówień - główne okno](http://kaw.net.pl/somado_img/main_zamowienia_en.jpg)

![Planowanie nowej dostawy](http://kaw.net.pl/somado_img/nowa_dostawa_plan.jpg)

![Trasa dostawy na mapie](http://kaw.net.pl/somado_img/dostawa_trasa.jpg)

![Trasa dostawy na mapie #2](http://kaw.net.pl/somado_img/dostawa1_trasa1.jpg)

![Trasa dostawy na mapie #3](http://kaw.net.pl/somado_img/dostawa1_trasa1_wwa.jpg)

![Trasa dostawy na mapie #2](http://kaw.net.pl/somado_img/dostawa1_trasa2.jpg)

----------

# Somado
Program "Somado" umożliwia optymalizację dostaw towarów; wykorzystuje dane OpenStreetMap. Problem marszrutyzacji (VRP) rozwiązywany przy użyciu biblioteki JSprit. Wyświetlanie map przy użyciu biblioteki JXMapViewer2.
Geokodowanie przy użyciu usługi OSM Nominatim, mapa podkładowa - wybrana usługa TMS.
Baza danych SQLite + Spatialite. Dane przestrzenne są "rzutowane" na obiekty przy pomocy biblioteki JTS (zgodnie ze standardem OGC).
