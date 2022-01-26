import processing.core.PApplet;
import processing.event.Event;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class KaartjesAutomaat {
    // Klasses die nodig zijn
    private final PApplet app;
    private final DecimalFormat format;
    private Bestelling huidigeBestelling;

    // Arrays/arraylists die nodig zijn
    private ArrayList<Film> films;
    private final double[] geldBedragen;
    private ArrayList<Knop> filmKnoppen;
    private final ArrayList<Knop> betaalKnoppen;
    private final ArrayList<Knop> aantalTicketKnoppen;
    private ArrayList<Bestelling> bestellingen;
    private Knop terugKnop;


    // statische variabelen die bepaalde collecties optellen
    public static int aantalVerkochteKaartjes;
    private static int aantalFilms;
    private static double opbrengst;

    // variabelen
    private int schermStatus;
    private final int hoofdMenuScherm;
    private final int betaalScherm;
    private final int ticketScherm;
    private boolean filmIsGekozen;
    private boolean betalingVoldaan;
    private int indexHuidigeFilm;
    private int aantalKaartjesInBestelling;
    private double openStaandBedrag;
    private double wisselGeld;
    private double scrollPositie;

    public KaartjesAutomaat(PApplet app) {
        this.app = app;
        this.films = new ArrayList<>();
        this.filmKnoppen = new ArrayList<>();
        this.betaalKnoppen = new ArrayList<>();
        this.aantalTicketKnoppen = new ArrayList<>();
        this.bestellingen = new ArrayList<>();
        this.geldBedragen = new double[]{0.05, 0.10, 0.20, 0.50, 1.00, 2.00, 5.00, 10.00};
        this.openStaandBedrag = 0;
        this.aantalKaartjesInBestelling = 1;
        this.scrollPositie = 0;

        opbrengst = 0;
        aantalFilms = 0;
        aantalVerkochteKaartjes = 0;

        this.format = new DecimalFormat("##.00");
        this.schermStatus = 0;
        this.hoofdMenuScherm = 0;
        this.betaalScherm = 1;
        this.ticketScherm = 2;
    }

    // Functies die schakelen tussen de verschillende schermen en inputs.
    public void tekenSysteem() {
        switch (schermStatus) {
            // 0 = kiesfilmscherm, 1 = betaalscherm, 2 = ticketscherm
            case 0 -> tekenKiesFilmScherm();
            case 1 -> tekenBetaalScherm();
            case 2 -> tekenTicketScherm();
        }
    }

    public void verwerkSysteemInput() {
        switch (schermStatus) {
            // 0 = kiesfilmscherm, 1 = betaalscherm, 2 = ticketscherm
            case 0 -> verwerkKiesFilmSchermInput();
            case 1 -> verwerkBetaalSchermInput();
            case 2 -> verwerkTicketSchermInput();
        }
    }

    // Functies voor het instantieeren van knoppen waar de gebruiker mee interacteert
    public void instantieerKnoppen() {
        genereerFilmKnoppen();
        genereerBetaalKnoppen();
        genereerTerugKnop();
        genereerAantalTicketKnoppen();
    }

    // Functies voor het tekenen van de verschillende schermen.
    private void tekenKiesFilmScherm() {
        app.background(155);
        tekenFilmKnoppen();
        tekenTerugKnop();
    }

    private void tekenBetaalScherm() {
        app.background(155);
        tekenTekstBetaalScherm();
        tekenBetaalKnoppen();
        tekenTerugKnop();
        tekenAantalTicketKnoppen();
    }

    private void tekenTicketScherm() {
        app.background(155);
        tekenTicket();
        tekenTerugKnop();
    }

    // Functies die input van de gebruiker verwerken per scherm
    private void verwerkKiesFilmSchermInput() {
        verwerkFilmKnoppenInput();
        verwerkTerugKnopInput();
    }

    private void verwerkBetaalSchermInput() {
        verwerkBetaalKnoppenInput();
        verwerkTerugKnopInput();
        verwerkAantalTicketKnoppenInput();
    }

    private void verwerkTicketSchermInput() {
        verwerkTerugKnopInput();
    }

    // Functies die knoppen genereren aan de hand van hoe gevuld de bijbehorende arraylists zijn
    private void genereerFilmKnoppen() {
        float knopHoogte = app.height / 10f;
        float knopBreedte = app.width / 2f;
        float tussenRuimte = knopHoogte / 2f;
        float startX = (app.width / 2f) - (knopBreedte / 2f);
        float startY = tussenRuimte + (tussenRuimte / 2f);

        int index = 0;
        for (Film film : this.films) {
            Knop knop = new Knop(startX, startY + (index * (knopHoogte + tussenRuimte)), knopBreedte, knopHoogte, film.getNaam(), 28);
            filmKnoppen.add(knop);
            index++;
        }
    }

    private void genereerBetaalKnoppen() {
        float knopHoogte = app.height / 12f;
        float knopBreedte = app.width / 10f;
        float tussenRuimte = (app.width - (geldBedragen.length * knopBreedte)) / geldBedragen.length;
        float startX = tussenRuimte / 2f;
        float startY = (app.height / 5f) * 4f;

        int index = 0;
        for (double bedrag : geldBedragen) {
            Knop knop = new Knop(startX + ((knopBreedte + tussenRuimte) * index), startY, knopBreedte, knopHoogte, String.valueOf(bedrag), 18);
            this.betaalKnoppen.add(knop);
            index++;
        }
    }

    private void genereerTerugKnop() {
        float knopHoogte = app.height / 11f;
        float knopBreedte = app.width / 5f;
        float startX = app.width - knopBreedte - 5f;
        float startY = app.height - knopHoogte - 5f;

        this.terugKnop = new Knop(startX, startY, knopBreedte, knopHoogte, "Ga terug", 18);
    }

    private void genereerAantalTicketKnoppen() {
        float knopHoogte = app.height / 12f;
        float knopBreedte = app.width / 10f;
        float startX = app.width / 2f + (app.width / 6f);
        float startY = (100f + 40f * 5f) - (knopHoogte / 2f);
        float tussenRuimte = knopBreedte;

        String[] symbolen = {"<", ">"};

        for (int i = 0; i < 2; i++) {
            Knop knop = new Knop(startX + ((knopBreedte + tussenRuimte) * i), startY, knopBreedte, knopHoogte, symbolen[i], 28);
            this.aantalTicketKnoppen.add(knop);
        }
    }

    // Functies die de knoppen tekent aan de hand van hoe gevuld de bijbehorende arraylists zijn
    private void tekenFilmKnoppen() {
        for (Knop knop : filmKnoppen) {
            knop.tekenKnop(app);
        }
    }

    private void tekenBetaalKnoppen() {
        for (Knop knop : betaalKnoppen) {
            knop.tekenKnop(app);
        }
    }

    private void tekenAantalTicketKnoppen() {
        for (Knop knop : aantalTicketKnoppen) {
            knop.tekenKnop(app);
        }
    }

    private void tekenTerugKnop() {
        terugKnop.tekenKnop(app);
    }

    // Functies die de input van de gebruiker verwerkt aan de hand van hoeveel knoppen in de arraylist zijn
    private void verwerkFilmKnoppenInput() {
        for (Knop knop : filmKnoppen) {
            knop.klikKnop(app);
        }
        for (Knop knop : filmKnoppen) {
            if (knop.isGeklikt()) {
                indexHuidigeFilm = filmKnoppen.indexOf(knop);
                kiesFilm(indexHuidigeFilm);
            }
        }
    }

    private void verwerkBetaalKnoppenInput() {
        for (Knop knop : betaalKnoppen) {
            knop.klikKnop(app);
        }
        for (Knop knop : betaalKnoppen) {
            if (knop.isGeklikt()) {
                werpGeldIn(betaalKnoppen.indexOf(knop));
            }
        }
    }

    private void verwerkAantalTicketKnoppenInput() {
        for (Knop knop : aantalTicketKnoppen) {
            knop.klikKnop(app);
        }
        // als de linker knop ingedrukt < is dan wordt aantal tickets verlaagt met 1 als minimum(1) niet bereikt is
        if (aantalTicketKnoppen.get(0).isGeklikt() && this.aantalKaartjesInBestelling > 1) {
            this.aantalKaartjesInBestelling -= 1;
            setOpenStaandBedrag(this.openStaandBedrag - this.films.get(indexHuidigeFilm).getPrijs());
        } else if (aantalTicketKnoppen.get(1).isGeklikt() && this.aantalKaartjesInBestelling < 100) {
            this.aantalKaartjesInBestelling += 1;
            setOpenStaandBedrag(this.openStaandBedrag + this.films.get(indexHuidigeFilm).getPrijs());
        }
    }

    public void verwerkMuisScroll(float e) {
        System.out.println(e);
        Knop filmknop = filmKnoppen.get(0);
        int aantalExtraKnoppenBuitenScherm = filmKnoppen.size() - 6;
        double scrollMax = 0;

        if (aantalExtraKnoppenBuitenScherm > 0) {
            scrollMax = (aantalExtraKnoppenBuitenScherm * filmknop.getHoogte()) + (aantalExtraKnoppenBuitenScherm * (filmknop.getHoogte() / 2));
        }

        if (schermStatus == hoofdMenuScherm) {
            if (e == 1.0 && this.scrollPositie < scrollMax) {
                for (Knop knop : filmKnoppen) {
                    knop.setY(knop.getY() - 10);
                }
                this.scrollPositie += 10;
            } else if (e == -1.0 && this.scrollPositie > 0) {
                for (Knop knop : filmKnoppen) {
                    knop.setY(knop.getY() + 10);
                }
                this.scrollPositie -= 10;
            }
        }
        System.out.println(scrollPositie);
    }

    private void verwerkTerugKnopInput() {
        this.terugKnop.klikKnop(app);

        if (terugKnop.isGeklikt()) {
            switch (schermStatus) {
                // 0 = kiesfilmscherm, 1 = betaalscherm, 2 = ticketscherm
                case 0 -> schermStatus = hoofdMenuScherm;
                case 1 -> {
                    resetStandNaAnnulering();
                    schermStatus = hoofdMenuScherm;
                }
                case 2 -> {
                    resetStandNaBetaling();
                    schermStatus = hoofdMenuScherm;
                }
            }
        }
    }

    // Functies die tekst op het scherm tekent
    private void tekenTekstBetaalScherm() {
        float tekstGrootte = 28;
        float tussenRuimte = tekstGrootte * 1.5f;
        app.textAlign(app.CENTER);
        app.textSize(tekstGrootte);
        app.text("Geselecteerde film: " + this.films.get(indexHuidigeFilm).getNaam(), app.width / 2f, 100);
        app.text("Prijs van geselecteerde film: €" + formatBedrag(this.films.get(indexHuidigeFilm).getPrijs()), app.width / 2f, 100 + tussenRuimte);
        app.text("Totaal bedrag van uw bestelling: €" + formatBedrag(this.films.get(indexHuidigeFilm).getPrijs() * aantalKaartjesInBestelling), app.width / 2f, 100f + tussenRuimte * 2f);
        app.text("Nog te betalen bedrag: €" + formatBedrag(this.openStaandBedrag), app.width / 2f, 100f + tussenRuimte * 3f);
        app.text("Aantal kaartjes in bestelling:", app.width / 2f - (app.width / 6f), 100f + tussenRuimte * 5f);
        app.text(this.aantalKaartjesInBestelling, app.width / 2f + (app.width / 3.2f), 100f + tussenRuimte * 5f);
    }

    private void tekenTicket() {
        float kaartjeX1 = 0;
        float lijnBuffer = 5;
        float kaartjeY1 = app.height / 4f;
        float kaartjeBreedte = app.width;
        float kaartjeHoogte = app.height / 2f;
        app.fill(255);
        app.textAlign(app.CENTER);
        app.textSize(22);
        app.rect(kaartjeX1, kaartjeY1, kaartjeBreedte, kaartjeHoogte);
        kaartjeArt(300, 200, 400);
        app.strokeWeight(2);
        app.stroke(0, 100);
        app.line(kaartjeX1 + lijnBuffer, kaartjeY1 + lijnBuffer, app.width - lijnBuffer, kaartjeY1 + lijnBuffer);
        app.line(kaartjeX1 + lijnBuffer, kaartjeY1 + (kaartjeHoogte) - (lijnBuffer * 1), app.width - lijnBuffer, kaartjeY1 + (kaartjeHoogte) - (lijnBuffer * 1));
        app.line(kaartjeX1 + lijnBuffer, kaartjeY1 + lijnBuffer, kaartjeX1 + lijnBuffer, kaartjeY1 + kaartjeHoogte - (lijnBuffer * 1));
        app.line(kaartjeX1 + kaartjeBreedte - (lijnBuffer), kaartjeY1 + lijnBuffer, kaartjeX1 + kaartjeBreedte - (lijnBuffer), kaartjeY1 + kaartjeHoogte - (lijnBuffer * 1));
        app.strokeWeight(1);

        app.textAlign(app.CENTER);
        app.textSize(18);
        app.stroke(0);
        app.fill(0);
        app.text("U heeft gekozen voor de film: " + films.get(indexHuidigeFilm).getNaam(), app.width / 2f, 275);
        app.text("Uw ticketnummer is: " + aantalVerkochteKaartjes, app.width / 2f, 325);
        app.text("Uw filmcode is: " + films.get(indexHuidigeFilm).getIndex(), app.width / 2f, 375);
        app.text("De prijs van de film is: €" + formatBedrag(films.get(indexHuidigeFilm).getPrijs()), app.width / 2f, 425);
        app.text("U heeft betaald: €" + formatBedrag((films.get(indexHuidigeFilm).getPrijs() + wisselGeld)), app.width / 2f, 475);
        if (wisselGeld > 0.00) {
            app.text("Hier is uw wisselgeld: €" + formatBedrag(wisselGeld), app.width / 2f, 525);
        }
    }

    public void kaartjeArt(float x, float y, float d) {
        app.stroke(0, 15);
        app.noFill();
        app.ellipse(x, y, d, d);
        if (d > 2) {
            kaartjeArt(x + (d / 2), y, d / 2);
            kaartjeArt(x - (d / 2), y, d / 2);
            kaartjeArt(x, y + (d / 2), d / 2);
        }
    }

    private void printTicket() {
        System.out.println("#----------------------------------------------------------#");
        System.out.println("U heeft gekozen voor de film: " + films.get(indexHuidigeFilm).getNaam());
        System.out.println("De prijs van de film is: " + formatBedrag(films.get(indexHuidigeFilm).getPrijs()));
        System.out.println("U heeft betaald: " + formatBedrag((films.get(indexHuidigeFilm).getPrijs() + wisselGeld)));
        if (wisselGeld > 0.00) {
            System.out.println("Hier is uw wisselgeld: " + formatBedrag(wisselGeld));
        }
        System.out.println("Uw ticketnummer is: " + aantalVerkochteKaartjes);
        System.out.println("Uw filmcode is: " + aantalFilms);
        System.out.println("#-----------------------------------------------------------#");
    }

    // Functies die functionaliteit geven aan de gebruiker in interactie met de films
    public void voegFilmToe(String naam, double prijs) {
        Film nieuweFilm = new Film(naam, prijs);
        nieuweFilm.setIndex(aantalFilms);
        films.add(nieuweFilm);
        this.filmIsGekozen = false;
        this.betalingVoldaan = false;

        aantalFilms++;
    }

    public void toonFilms() {
        for (Film film : films) {
            System.out.println(film.toString());
            System.out.println("Index van de film: " + film.getIndex());
        }
    }

    public void kiesFilm(int index) {
        this.indexHuidigeFilm = index;
        this.schermStatus = betaalScherm;
        this.betalingVoldaan = false;
        //this.openStaandBedrag = films.get(index).getPrijs();
        setOpenStaandBedrag(films.get(index).getPrijs());
        System.out.println("U heeft gekozen " + films.get(index).toString());
    }

    private void setOpenStaandBedrag(double nieuwBedrag) {
        this.openStaandBedrag = nieuwBedrag;
    }

    // Functies die betalingen verwerken
    public void werpGeldIn(int index) {
        openStaandBedrag -= geldBedragen[index];
        System.out.println("U heeft ingeworpen: " + formatBedrag(geldBedragen[index]));
        if (openStaandBedrag > 0.01) {
            System.out.println("Nog te betalen bedrag: " + formatBedrag(openStaandBedrag));
        }

        if (openStaandBedrag < 0.00) {
            wisselGeld -= openStaandBedrag;
            openStaandBedrag += wisselGeld;
        }

        if (openStaandBedrag == 0.00) {
            betalingVoldaan = true;
            verwerkBestelling();
            schermStatus = ticketScherm;
        }
    }

    private void verwerkBestelling() {
        Bestelling bestelling = new Bestelling(this.aantalKaartjesInBestelling, films.get(indexHuidigeFilm));
        this.huidigeBestelling = bestelling;

        // Voeg de bestelling toe aan de bestellingen array, hiermee kan in het admin scherm wat later gecreeerd wordt alle bestellingen ingezien worden door simpelweg in de bestellingen array te kijken
        this.bestellingen.add(bestelling);
    }

    private void resetStandNaBetaling() {
        aantalVerkochteKaartjes += this.aantalKaartjesInBestelling;
        this.aantalKaartjesInBestelling = 1;
        this.wisselGeld = 0;
        this.filmIsGekozen = false;
        this.betalingVoldaan = false;
        this.huidigeBestelling = null;
    }

    private void resetStandNaAnnulering() {
        this.wisselGeld = 0;
        this.aantalKaartjesInBestelling = 1;
        this.filmIsGekozen = false;
        this.betalingVoldaan = false;
        this.huidigeBestelling = null;
    }

    private String formatBedrag(double bedrag) {
        if (bedrag >= 1.00) {
            return format.format(bedrag);
        } else if (bedrag < 1.00) {
            return "0" + (format.format(bedrag));
        }
        return "";
    }

}
