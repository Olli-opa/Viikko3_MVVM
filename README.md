# Viikkotehtävä: Tehtävänhallintasovellus Composella

Tällä viikolla jatkettiin tehtävänhallintasovellusta, joka on rakennettu käyttäen Jetpack Composea ja modernia Android-arkkitehtuuria. Sovelluksen avulla käyttäjä voi lisätä, poistaa, muokata ja suodattaa tehtäviä.

Viikkotehtävän päätavoitteena oli harjoitella MVVM-arkkitehtuurin ja reaktiivisen tilanhallinnan periaatteita Compose-ympäristössä.

## Arkkitehtuuri: MVVM (Model-View-ViewModel)

Sovellus noudattaa MVVM-arkkitehtuurimallia. Tämä malli jakaa sovelluksen kolmeen pääkerrokseen:

1.  **Model**: Datan ja sovelluksen liiketoimintalogiikan kerros.
    *   Tässä projektissa `Model`-kerroksen muodostaa `Task.kt`-datamalli, joka määrittelee, miltä yksittäinen tehtävä näyttää (esim. `id`, `title`, `done`).

2.  **View**: Käyttöliittymäkerros, joka näyttää datan ja ottaa vastaan käyttäjän syötteet.
    *   Jetpack Composessa `View`-kerroksen muodostavat Composable-funktiot, kuten `HomeScreen.kt` ja `DetailScreen.kt`.
    *   Näkymän vastuulla on ainoastaan datan esittäminen ja käyttäjän toimintojen välittäminen ViewModelille. Se ei sisällä itse mitään liiketoimintalogiikkaa.

3.  **ViewModel**: Toimii siltana `Model`- ja `View`-kerrosten välillä.
    *   `TaskViewModel.kt` on tämän sovelluksen ViewModel. Se valmistelee ja hallinnoi käyttöliittymän tarvitsemaa dataa, paljastaen sen `View`-kerrokselle `StateFlow`-olion kautta.
    *   ViewModel ottaa vastaan komentoja näkymältä (esim. "lisää uusi tehtävä") ja suorittaa tarvittavat muutokset dataan.

### Miksi MVVM on hyödyllinen Compose-sovelluksissa?

*   **Vastuun eriyttäminen (Separation of Concerns)**: Koodi on selkeämpää, helpommin testattavaa ja ylläpidettävää, kun käyttöliittymä, sovelluksen tila ja datalogiikka ovat erillään toisistaan.
*   **Lifecycle-tietoisuus**: `ViewModel` säilyttää tilansa, vaikka käyttöliittymä tuhottaisiin ja luotaisiin uudelleen esimerkiksi näytön kääntämisen yhteydessä. Tämä estää datan katoamisen ja tekee tilanhallinnasta vankkaa.
*   **Sopii deklaratiiviseen ohjelmointiin**: Compose on deklaratiivinen käyttöliittymäkirjasto, mikä tarkoittaa, että UI on funktiona sovelluksen tilasta. ViewModel tarjoaa tämän tilan, ja Compose-näkymät vain "reagoivat" sen muutoksiin. Tämä yhteispeli on luonnollinen ja tehokas.

## Tilanhallinta: StateFlow

Sovelluksen reaktiivinen tilanhallinta on toteutettu Kotlin Coroutines -kirjaston `StateFlow`-oliolla.

### Miten StateFlow toimii?

`StateFlow` on "tietoinen" (state-aware) ja observable-tyyppinen datan säiliö. Yksinkertaistettuna:

1.  **Säiliö tilalle**: ViewModelissa `StateFlow` (`_tasks`) pitää sisällään nykyisen tilan (tässä tapauksessa listan tehtäviä).
2.  **Aina olemassa oleva arvo**: Toisin kuin perinteisillä `Flow`-virroilla, `StateFlow`:lla on aina arvo. Kun uusi tarkkailija (collector) liittyy, se saa heti viimeisimmän arvon.
3.  **Päivitykset ja tarkkailu**:
    *   Kun ViewModelissa dataa päivitetään (esim. `addTask`-funktiossa), uusi lista asetetaan `StateFlow`-olion `value`-ominaisuuteen.
    *   Käyttöliittymässä (`HomeScreen`) käytetään `collectAsState()`-funktiota, joka muuttaa `StateFlow`:n Compose-yhteensopivaksi `State`-olioksi.
    *   Aina kun ViewModelin `StateFlow`-arvo muuttuu, Compose-käyttöliittymä saa siitä automaattisesti tiedon ja piirtää itsensä uudelleen näyttäen päivittyneen datan.

Tämä mekanismi varmistaa, että käyttöliittymä pysyy aina synkronissa sovelluksen tilan kanssa ilman manuaalisia päivityskutsuja.