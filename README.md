#Emilijaus Stankaus
#Kurso "Objektinis programavimas" (PS) 2015/16 m.m. pavasario (2) sem.
#Projektinio darbo aprašas

##•	Paskirtis
Ši programa yra žaidimas, skirtas švaistyti ir taip laikiną žmonių gyvenimą.
##•	Paleidimas
Jei programa dar nesukompiliuota, tada reiktų sukompiliuoti failą GameStart.java (visi kiti bus irgi automatiškai sukompiliuoti). Norint paleisti žaidimą, reikia su Java mašina paleidinėti GameStart.class failą. Žaidimui nenumatyti jokie paleidinėjimo argumentai.
##•	Žaidimo aprašymas
Žaidėjas su kaire ir dešine klaviatūros rodyklėmis valdo lango apačioje esančią juostelę, sudarytą iš kelių skirtingų spalvų sekcijų. Iš viršaus nuolatos krenta spalvoti blokai. Žaidėjo tikslas yra, kad atitinkamos spalvos blokelis nukristų ant atitinkamos spalvos juostelės sekcijos. Jei blokelis nukrenta ant kitokios spalvos juostelės sekcijos, žaidimas baigiasi.
##•	Funkcionalumas
Be žaidimo aprašyme aprašyto veikimo egzistuoja dar šios funkcijos:

1. Taškų skaičiavimas ir atvaizdavimas ekrane.
2. Galimybė išsaugoti žaidimo būseną ir kitą kartą įjungus įkrauti būseną iš failo ir žaidimą pratęsti.
3. Pele valdomi meniu:
  1. Pagrindinis meniu iš kurio galima pradėti naują žaidimą arba tęsti buvusį.
  2. Žaidimo meniu (pasiekiamas nuspaudus Esc mygtuką žaidimo metu), kuriame galima išsaugoti esamą žaidimo būseną.
  3. Pabaigos meniu, kuriame rodomi surinkti taškai ir galima pradėti naują žaidimą.
  
##•	Klasės
GameStart – sukuria Swing langą ir Game objektą.
Game – Pagrindinė klasė. Vykdo didžiąją dalį žaidimo logikos, naudoja visas kitas klases ir piešia žaidimą.
GameObject – abstrakti klasė kurią praplečia kiti judantys žaidimo objektai.
Block – aprašo krentantįjį žaidimo blokelį.
Catcher – aprašo žaidėjo valdomą juostelę. Naudoja Singleton šabloną.
BlockFactory – kuria Block tipo objektus. Naudoja gamyklos šabloną.
GameMusic – groja muziką.
##•	Plėtimo galimybės
Žaidimas labai lengvai modifikuojamas GameStart klasėje keičiant final kintamuosius. Galima keisti beveik visas charakteristikas ir žaidimas toliau puikiai funkcionuos. Žaidimo kūrimą būtų galima tęsti jį tobulinant ir pridedant naujų galimybių. Nusprendus tai daryti, vis dėlto reikėtų piešimą perkelti iš Java Swing sąsajos (ji nėra pritaikyta žaidimams) ir suskaidyti Game klasę, nes ji atlieka kiek per daug funkcijų. Geriausia turbūt iš vis perrašyti viską, nes grynai dariau tik kad veiktų.
 
