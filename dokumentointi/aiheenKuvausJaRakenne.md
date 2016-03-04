
# Aihe ja tavoite

Tulkki ja emulaattori binäärimuotoisille [CHIP-8](https://en.wikipedia.org/wiki/CHIP-8)
-peleille, graafisella Swing-pohjaisella käyttöliittymällä. Tavoitteena on ensin toteuttaa emulaatioydin, jonka tueksi myöhemmin rakennetaan käyttöllitymä. Aikarajoitteiden salliessa emulaattori voidaan myös laajentaa tukemaan SuperChip-väännöksen käskyjä ja suurempaa resoluutiota.

## Suunnitellut toiminnot

### Emulaatio
* Prosessori
* Grafiikka
* Beeper-kaiutin

### Käyttäjän toimminot
* Pelin käynnistäminen tiedostosta
* Pelin samuttaminen, pysäyttäminen ja jatkaminen
* [Save state](https://en.wikipedia.org/wiki/Saved_game#Save_states)t
* Graafinen asetusdialogi
 * Kontrollien valinta

## Lisätavoitteet/jatkokehitys
* "Aikamatkustus"
  * Automaattinen save state joka ruduunpäivityksellä, joiden välillä voi liikkua vapaasti. Määrä rajattu käyttäjän asettaman rajan mukaan.
* Tuki CHIP48/SuperChip-peleille

## Määrittelyvaiheen luokkakaavio

![Luokkakaavio](/dokumentointi/uml/concept.png)

## Emulaatiosyklin sekvenssikaavio
![Emulaatiosykli](/dokumentointi/uml/cyclesequence.png)

## Loppudokumentaatio
[Rakennekuvaus](/dokumentointi/rakennekuvaus.md)

[Käyttöohje](/dokumentointi/kayttoohje.md)

[Testidokumentointi / post mortem](/dokumentointi/testidokumentointi.md)

## Riippuvuudet

* Projekti hyödyntää jOOq-projektin kirjastoja [jOOλ](https://github.com/jOOQ/jOOL) ja [jOOu](https://github.com/jOOQ/jOOU), Googlen JSON-kirjastoa Gson ja commons-io-kirjastoa, jotka Maven asentaa automaattisesti.