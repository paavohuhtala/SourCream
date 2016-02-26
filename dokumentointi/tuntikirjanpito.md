## 19.1.2016: 45 min

GitHub-sivu luotu, NetBeans-projekti aloitettu.

## 22.1.2016: 1h

Readme.md luotu, alustava aihemäärittely laadittu.

## 24.1.2016: 6h

Koodaaminen aloitettu. Toteutettiin mm.

* Käskyrajapinta ja useita abstrakteja käskyluokkia
* Rekisteriluokka
* Koneen tilaluokka
* AND ja OR -käskyt
* Yksi testi

## 25.1.2016: 1h

Eilistä koodia siistitty ja bugeja/suunniteluvirheitä korjattu.

## 29.1.2016: 4h

* Parikymmentä uutta testiä
* Bugeja/testivajavaisuuksia korjattu PIT-raporttien perusteella
* XOR-käsky ja kaksi rekisterikäskyä
* Käskycache abstraktoitu rajapinnoiksi ja toteutukseksi

## 5.2.2016: 7h

* Aloita Device ja CPU
* Aloita UI-toteutus
* Säädä PIT-raportin rajausta
* Paljon refaktorointia

## 12.2.2016: 6h

* Runsaasti lisää käskyjä: bitwise, transfer, arithmetic, graphics yms.
* Paljon lisää JavaDocseja
* Aloita grafiikkatuki: ScreenBuffer ja sen piirtometodit, lisää näyttöpuskuri Stateen.
* Refaktoroi käskyrajapintoja
* Lisää checkstyle ja lisää rapotti mukaan
* Muutama uusi testi

## 13.2.2016: 1h

* Unit testien korjausta ja siistimistä

## 17.2.2016: 5h

* Muutama uutta käskyä
* Mahdollisuus ladata näyttöpuskuri tekstitiedostosta
* Grafiikkatoimintoja
* Perustoiminnallisuus!
    * Lataa ohjelmia tiedostoista
    * Suorittaa päälooppia (rajoitetulla käskykannalla)
    * Piirtää grafiikat
* LCD-emulaatio
    * Näytön vilkkuminen on CHIP8:n tyyppivika, jonka korjaaminen on haastavaa. LCD-emulaatiolla emuloidaan nestekidenäyttöjen ghosting-efektiä.

## 18.2.2016: 2h

* Apuluokkia, siistimistä
* Konfiguraation laajentamista

## 19.2.2016: 4h

* Uusia testejä ja entisten parantelua
* Checkstylen mukaisia korjauksia
* Uutta dokumentaatiota
* Sekvenssikuvaaja emulaatiosyklistä

## 20.2.2016: Noin 4h
* Yhdistä Device ja CPU pelkäksi Deviceksi. Luokkien roolit olivat melko epäselviä, sillä Device oli vain ohut wrapperi CPU:n päällä.
* Aloita toteuttamaan inputtia, timereitä ja syklien ajotusta
* Aloita toteuttamaan asetusdialogia
* Paljon uusi käskyjä
* Bugeja korjattu

## 21.2.2016: Noin 4h
* Jatka eilistä kehitystä
* Key bindingit - ei vielä käyttöliittymää
* Pong on pelattavissa!

## 22.2.2016: 30 min
* Hiottu viikonlopun muutoksia ja pushattu

## 25.2.2016 (plus 26.2 yö): 7h
* Demopäivä!
* Pongia ja paria muuta peliä koskee mysteeribugi, joka on edelleen tuntematon. Bugista huolimatta Tetris ja Pong ovat pelattavissa.
* Korjattu pääikkunan overlappiongelma (näyttöpuskuri näkyy nyt kokonaan)
* Viimeinen puuttuva käsky toteutettu!
* Testejä lisätty ja turhia karsittu.
* JavaDocsit kaikkialle, ja checkstyle-korjailuja.
    - Ainoat jäljelle jääneet checkstyle-virheet johtuvat liian pitkistä tiedostoista.
* Konffausikkunaa kehitetty huomattavasti eteenpäin.
* Device-luokan instanssi on nyt siirretty MainWindowiin.
* Ohjelmia voi nyt ladata graafisesti.
* Konfiguraatio jätetty pois PIT-raportista toistaiseksi.
* Aloitettu rakennekuvauksen kirjoittaminen

## 26.2.2016: 2h
* Näppäinpresetit lisätty käyttöliittymään
* Kirjoitettu lisää dokumentaatiota (käyttöohje, rakennekuvaus)