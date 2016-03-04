# Testidokumentointi / post mortem
*AKA: "Mksi test coverage on noin huono?"*

SourCream käyttää yksikkötesteihin JUNit-testikirjastoa. Pieni osa ohjelmasta on testattu ~100% kattavasti (esimerkiksi bitwise-operaatiot), mutta suurin osa testeistä keskittyy vaikeisiin yksittäistapauksiin.

Rakenteen puolesta SourCream on erittäin helppo testata; emulaatiokoodi on lähes täysin puhtaasti funktionaalista, joten käskyjä on erittäin helppo testata erikseen. Järjestelmätason testaamisen avuksi on luotu `ProgramBuilder`-luokka, jolla voidaan muuttaa käskyinstasseja tavukoodiksi. Emulaatiokoodi on myös täysin riippumatonta käyttöliittymästä.

Testaamisessa on kuitenkin haasteita. Emulaattorin testaamisen isoin ongelma on se, että aina ei voi tietää mikä on "oikeaa toimintaa". Alustana Chip-8:sta on suhteellisen vähän informaatiota (vaikkapa verratuna kasibittiseen Nintendoon), ja käytännössä kaikissa hiemankin kattavissa järjestelmädokumenteissä on virheitä ja ristiriitoja. Vaikka käskyjen testaamisella on löytynyt muutama bugia, suurin osa käskyistä on hyvin yksinkertaisia ja niille kattavien testien kirjoittamien on käytännössä ajan tuhlausta ja implementaation kopioimista testeihin. Suurin osa bugeista on löytynyt testaamalla manuaalisesti, lukemalla suorituslogia terminaalista ja hyppimällä debuggerilla ympäri ohjelmaa. Bugeja on myös etsitty vertailemalla ohjelmaa toisiin Chip-8 emulaattoreihin, pelien ja itse kirjoitettujen tavukoodiohjelmien avulla.

Käyttöliittymälle ja konfiguraatiolle ei ole yksikkötestejä. Käyttöliittymätestien kirjoittamien on suhteellisen haastavaa, ja toisaalta SourCreamin tapauksessa käyttöliittymä on melko yksinkertainen ja yllätyksetön. Konfiguraatio koostuu lähinnä sisäkkäisistä dataluokista; varsinaista ohjelmalogiikkaa siihen ei liity.

Huonosti testatuin paketti `sourcream.utils` sisältää melko triviaalia koodia, ja se koostuu vain 149 rivistä (kun tyhjät rivit ja kommentit jätetään huomiotta).