## Bestö Buam

### LIVE TODO
-------------
 - TODO
    -----
 - Checks keine Umlaute! 
 - Kellner Login richtiges fest zuteilen
 - Username ist vor - nachname ersten stellen + feuerwehr id
 - Nach fest + zeit alle benutzer löschen
 - Datenbank muss noch persistierung ordentlich gemacht werden

 - DONE
    ----- 
- Auto Username erstellung
- usernamen bei bearbeitung disabled machen
- Keycloak Benutzer aktuallisieren + mit hinzufügen und wieder entfernen
- Prüfen ob Keycloak Benutzer schon vorhanden ist
- Keycloak Benutzer über Quarkus Anlegen
- Anzahl in Bestellübersicht anzeigen
- Popup nach bezahlen schließen wenn erfolgreich
- Buffet Ordentlich machen und Praktischer
- Buffet wenn schließen oder neuladen vom fenster bestellungen behalten und wieder neu anzeigen


### STRUKTUR
------------
#### Admin Page:
- Liste aller aktuellen Feste
    - Bearbeitungs Button für Einstellungen !
- Neues Fest anlegen
    - Name Fest !
    - Von wann - bis !
    - Vor- und Nachlaufzeit !
        - Festadmin Tätigkeiten !

- Benutzer Anlage (Eventuell auch über Seite direkt)
    - ... Eventuell 2 Keycloak benutzer (Aufnahme und Bringer) und dann selbst Namen eingeben
    - Hauptverantwortlicher... !
        - Telefonnummer !
        - Email !
        - Name !
        - Organisation !
- ...
- Backdoor is eh kloor :-D

#### Fest Admin:
- Dashboard
    - Nützliche übersichten 
    - ...

- Kellner verwalten
    - Name !
    - Bestellung oder Bringer !

- Abrechnungen sehen
    - Was wurde wie oft verkauft 

- Ausgabe-Positionen verwalten
    - Name  !
    - Artikel Verwalten !
        - Preis !
        - Name !
        - Auswertungen !

    - Rechnungs-Drucker !
        - Drucker hinzufügen !
        - Testdrucke 
        - Funktionieren !

- Gimick steuerung

#### Kellner:
- Aufnahme Bestellungen
    - Klick auf Artikeln für hinzufügen weiterm Artikels
        - Bearbeiten für spezielle Artikel
            - Eingabe wie viele von den Speziell sind
            - Text Feld für das Speziale

    - Artikel
    - Bezahlung
        - Summe anzeigen
        - Rückgeld ausrechnen
            - In scheinen und Müntzenv

#### Ausgabe Positionen (zB Ausschank ...)
- Anzeige Bestellte Artikel
    - Id-Anzeige zu übersichtbarkeit
    - Was wie oft + Zusatz
    - In Bearbeitung setzen?
        - Andere Farbe
    - Auftrag beenden
        - Rechnungsdruck


#### !!! Gimicks !!!
- Wird nu überlegt
- Popups bei bestimmten bestellungen