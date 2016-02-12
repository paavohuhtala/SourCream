
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

## Riippuvuudet

* Projekti hyödyntää jOOq-prjektin [jOOλ](https://github.com/jOOQ/jOOL) ja [jOOu](https://github.com/jOOQ/jOOU)-kirjastoja, jota Maven asentaa automaattisesti.