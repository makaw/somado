# Somado
Program "Somado" umożliwia optymalizację dostaw towarów; wykorzystuje dane OpenStreetMap. Problem marszrutyzacji (VRP) rozwiązywany przy użyciu biblioteki JSprit. Wyświetlanie map przy użyciu biblioteki JXMapViewer2.
Geokodowanie przy użyciu usługi OSM Nominatim, mapa podkładowa - wybrana usługa TMS.

*Jest to uproszczona wersja aplikacji, będącej oryginalnie częścią pracy dyplomowej inż., obronionej na Politechnice Warszawskiej (06.2016).*
### Baza danych przestrzennych
W folderze dist umieszczono skompresowany plik bazy SQLite + Spatialite, zawierającej dane sieci drogowej woj. mazowieckiego, przygotowane na podstawie danych OpenStreetMap (dist/roads.sqlite.zip)

### Konfiguracja 
- Import bazy danych MySQL (plik mysql_dump.sql.zip w folderze dist)
- Ustawienie dostępu do bazy MySQL, ew. zmiana serwera TMS i współrzędnych obsługiwanego obszaru w pliku dist/conf.properties
- Rozpakowanie lokalnej bazy danych przestrzennych do katalogu z archiwum jar (plik dist/roads.sqlite.zip)
- Windows: rozpakowanie bibliotek DLL do katalogu z archiwum jar (plik dist/windows_dll.zip)

----------

![Lista zamówień - główne okno](http://kaw.net.pl/somado_img/main_zamowienia.jpg)

![Planowanie nowej dostawy](http://kaw.net.pl/somado_img/nowa_dostawa_plan.jpg)

![Trasa dostawy na mapie](http://kaw.net.pl/somado_img/dostawa_trasa.jpg)

![Trasa dostawy na mapie #2](http://kaw.net.pl/somado_img/dostawa1_trasa1.jpg)

![Trasa dostawy na mapie #3](http://kaw.net.pl/somado_img/dostawa1_trasa1_wwa.jpg)

![Trasa dostawy na mapie #2](http://kaw.net.pl/somado_img/dostawa1_trasa2.jpg)

----------

### Zależności / depedencies

BrowserLauncher2 1.3

commons-collections 3.2.2

commons-configuration 1.10

commons-lang 2.6

commons-logging 1.2

commons-math 2.2

gson 2.3.1

httpclient 4.3.4

httpcore 4.4.4

jdatepicker 1.3.4

jsprit-analysis 1.6.2

jsprit-core 1.6.2

jsprit-instances 1.6.2

jts 1.8

jxmapviewer2 2.0

log4j-api 2.5

log4j-core 2.5

mysql-connector-java 5.1.23

nominatim-api 3.2

slf4j.api 1.6.1

slf4j-simple 1.7.18

sqlite-jdbc 3.8.11.2
