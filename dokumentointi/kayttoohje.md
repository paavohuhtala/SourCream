# SourCreamin käyttöohje

## Yleistä

Koska SourCream on emulaattori, sen käyttäminen vaatii Chip-8 -alustan ohjelmia (ROMeja) toimiakseen. Repositorio sisältää useita public domainissa olevia testiromeja, joista ainakin Tetris, Pong (PONG, PONG2), UFO, Tank toimivat pelattavasti.

Voit käynnistää ROM-tiedoston valitsemalla pääikkunan ylävalikosta "File -> Load ROM..." (tai painamalla CTRL+L). Valittuasi tiedoston se käynnistetään automaattisesti.

Emulaation voi pysäyttää (ja jatkaa) pääikkunan "Emulation"- valikosta, tai painamalla P-näppäintä.

Asetusikkuna löytyy valikosta "Options -> Configuration".

## Näyttöemulaatioasetukset

SourCreamissa on melko kattavat näyttöemulaatioasetukset.

* Klikkaa tausta- tai etuvärinappia valitaksesi kyseisen värin.
* Ghosting-efektin voi ottaa käyttöön tai poistaa käytöstä Ghosting-ryhmälaatikon valintaruudulla. Efektiä voi hienosäätää Fade in- ja Fade out- valintalaatikoilla. Fade in määrää kuinka paljon päällä olevat pikselit lisäävät näyttöpuskurin arvoa, ja fade out vastaavasta määrää kuinka paljon sammutetut pikselit vähentävät näyttöpuskurin arvoa.
* Koska Chip-8:n näyttöpuskuri on hyvin pieni, sitä täytyy skaalata ylöspäin. Scale factor-valintalaatikolla voi säätää kuinka moninkertaisesti puskuria suurennetaan.

## Näppäinasetukset

Koska Chip-8 alustana ei sisällä varsinaisia nuolinäppäimiä, näppäimet pitää määrittää pelikohtaisesti. Näppäimet voi määrittää asetusikkunan oikeanpuoleisen palstan valintalaatikoilla, ja palstan yläosan valintalaatikosta löytyy muutamia valmiita näppäinpresettejä.

## Ääniasetukset

Beeper-kaiutimen saa päälle ja pois Sound-ryhmäpaneelin valintaruudulla.

## Kellotaajuus

Emuloitua kellotaajuutta voi vaihtaa asetusikkunan toisen välilehden valitsimella.

## Save statet

Voit tallentaa emulaattorin tilan millä hetkellä hyvänsä painamalla mitä tahansa näppäimistä F1-F8. Tilan saa palautettua painamalla samaa näppäintä CTRL-pohjassa. Save stateja voi käyttää myös pääikkunan ylävalikosta. Stateja **ei** tallenneta kun emulaattori sammutetaan.