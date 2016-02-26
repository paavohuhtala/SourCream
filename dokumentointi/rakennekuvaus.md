# SourCreamin rakenne

Kirjoitushetken tilastoja:

* 51 luokkaa
* 12 rajapintaa
* 5750 riviä koodia...
    - Josta noin 1000 riviä on testejä
    - 1379 riviä kommentteja
    - 983 riviä tyhjää tilaa
    - "Varsinaista" koodia 3392 riviä

## Chip-8 alustana

Koska Chip-8 ei ole "oikea" fyysinen tietokone, vaan harrastelijastandardi, sen speksit ovat hieman joustavat. Siitä on myös useita väännöksiä ja johdannaisia, kuten SuperChip.

* Lähtöisin 70-luvun puolivälin mikrotietokoneskenestä, mutta noussut suurempaan suosioon ohjelmoitavien laskinten myötä.
* 35 erilaista 16-bitin kokoista käskyä
* Neljä kilotavua muistia
* 16 8-bittistä rekisteriä, 16-bittinen osoiterekisteri ja 8-bittinen stack pointteri
* Kaksi 8-bittistä kelloa, jotka tikittävät nollaa kohti 60 päivityksen sekuntivauhtia.
* Vaihteleva kellotaajuus; suurin osa moderneista emulaattoreista toimii noin nopeudella 500 käskyä/sekunti.
* 64 x 32 pikselin 1-bittinen näyttöpuskuri
* 16 näppäintä
    - Ei määrättyjä nuolinäppäimiä; kontrollit ovat yleensä ohjelmakohtaisia.
* Beeper-kaiutin
* Järjestelmäfontti, joka sisältää heksamerkit 0 - F

## Yleisesti

SourCream on kirjoitettu funktionaalisen- ja olio-ohjelmoinnin välimallilla. Emulaatiossa on pyritty käyttämään mahdollisimman paljon puhtaita funktioita ja immutableja tietorakenteita, ja perintää ja rajapintoja käytetään runsaasti koodin toistamisen välttämiseksi. Java ei kuitenkaan välttämättä ole kielenä paras mahdollinen tälläiseen paradigmaan, joten "funktionaalisten ideaalinen" saavuttaminen vaatii melko paljon toistuvaa liimakoodia.

## Emulaation näkökulmasta
Kaikki käskyt toteuttavat `Instruction`-rajapinnan, joka tarjoaa metodeina mahdollisuuden suorittaa käsky ja hakea käskyä vastaava tavukoodi. Rajapinnasta on johdettu useita abstrakteja luokkia (esim `Instruction.WithRegister`), jotka helpottavat parametrejä sisältävien käskyjen luontia. Käskyintanssit sisältävät parametrinsä (kuten rekisteri-id:t ja vakiot) ja osaavat muodostaan oman 16-bittisen tavukoodinsa.

Sen sijaan että emulaattori loisi uusia käskyinstansseja suorituksen edetessä, se toimii lähes täysin päinvastoin. Ohjelman käynnistyksessä luodaan kaikkien käskyjen kaikki instanssit (joitain kymmeniä tuhansia), jotka tallennetaan yhteen taulukkoon. Kun "prosessori" dekoodaa käskyn, se haetaan käskycachesta (`InstructionDecoder` -> `InstructionCache` -> `ArrayInstructionCache`). Tämä yksinkertaistaa emulaatiosykliä huomattavasti. Käskygeneraattorit löytyvät staattisina metodeina luokasta `InstructionFactory`, ja ne hyödyntävät useita Java 8:n ominaisuuksia, kuten metodiviitteitä.

Käskyn suoritus (`Instruction.execute(state)`) on puhdas funktio, eli se ei itsessään muuta ohjelman tilaa, vaan ainoastaan palauttaa uuden tilan (projektissa `State`). Tilaolio sisältää käytännössä kaiken virtuaalikoneen suoritusdatan, ja se on ulkoisesti immutable. Tilaolio sisältää myös näyttöpuskurin, joka on toteutettu `ScreenBuffer`-luokkana.

Käskyt löytyvät `paavohuh.sourcream.emulation.instructions`-paketista. Paketti sisältää luokat `Arithmetic`, `Bitwise`, `Control`, `Graphics`, `Input` ja `Transfer`, joiden sisältä löytyvät varsinaiset käskytoteutukset, kategorioiden mukaan lajiteltuna.

Emulaation ydin on `Device`-luokka, joka yhdistää käskycachen, tilan ja kellot suoritettavaksi kokonaisuudeksi. Se sisältää "prosessorin" perusloopin, jonka suorittaminen ajoitetaan konfiguroitavan kellotaajuuden mukaan. Se kerää ja välittää päivitetyt näppäinsyötteet joka emulaatiosyklillä. Lisäksi se tarjoaa myös mahdollisuuden seurata näyttöpuskurin muutoksia, johon emulaattorin graafinen käyttöliittymä perustuu. Näistä huolimatta `Device` on täysin riippumaton graafisesta käyttöliittymästä, ja sitä käytetäänkin useissa yksikkötesteissä.

## Käyttöliittymän näkökulmasta

Käyttöliittymä on toteuttu hyvin perinteisesti Swingillä. `MainWindow`-luokka sisältää valikot, näyttöemulaatiopaneelin (`EmulatorPanel`) sekä itse `Device`-luokan.

Näyttöemulaatio perustuu kolmeen eri luokkaan: edellämainittuun `ScreenBuffer`iin, `EmulatedLcdBuffer`iin ja `EmulatorPanel`iin. Pääikkuna rekisteröi `EmulatorPanel`in vastaanottamaan näyttöpuskuripäivityksiä, ja `EmulatorPanel` välittää datan `EmulatedLcdBuffer`ille. Chip-8 ei tunne framen käsitettä, joten grafiikat täytyy päivittää aina kun näyttöpuskuria päivitetään, mikä johtaa usein kuvan vilkkumiseen. SourCream emuloi nestekidenäyttöjen ghosting-efektiä laajentamalla yksibittisen näyttöpuskurin liukuluvuiksi, ja feidaamalla pikseleitä sisään ja ulos muutosten mukaan. Kun `EmulatedLcdBuffer` on tehnyt laskunsa, `EmulatorPanel` laskee ja näyttää lopulliset pikselit interpoloimalla etu- ja taustavärien välillä. Koska näyttöpuskurin koko on tavallisesti vain 64x32, puskuria skaalataan konfiguroitavalla vakiolla.

`InputMapper` seuraa koko ohjelmassa tapahtuvia näppäinsyötteitä, ja välittää bindatut näppäimet `Device`lle.