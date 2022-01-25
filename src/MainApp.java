import processing.core.PApplet;
import processing.event.MouseEvent;

public class MainApp extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[]{"MainApp"});
    }

    public void settings() {
        size(600, 800);
    }

    KaartjesAutomaat k = new KaartjesAutomaat(this);

    public void setup() {
        k.voegFilmToe("Forest Gump", 10.00);
        k.voegFilmToe("Finding Nemo", 20.00);
        k.voegFilmToe("World at War", 5.00);
        k.voegFilmToe("Saving Private Ryan", 15.00);
        k.voegFilmToe("Shrek", 50.00);
        k.voegFilmToe("Inception", 25.00);
        k.voegFilmToe("Baba is You", 100.00);
        k.instantieerKnoppen();
    }

    public void draw() {
        k.tekenSysteem();
    }

    public void mousePressed() {
        k.verwerkSysteemInput();
    }

    // functie die een float passed aan een methode binnen de kaartjesautomaat die muisscroll verwerkt in het hoofdmenu
    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        k.verwerkMuisScroll(e);
    }
}
