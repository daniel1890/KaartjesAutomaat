import processing.core.PApplet;

public class Knop {
    private float x, y, breedte, hoogte;
    private String tekst;
    private float tekstGrootte;
    private boolean isGeklikt;

    public Knop(float x, float y, float breedte, float hoogte, String tekst, float tekstGrootte) {
        this.x = x;
        this.y = y;
        this.breedte = breedte;
        this.hoogte = hoogte;
        this.tekst = tekst;
        this.tekstGrootte = tekstGrootte;
        this.isGeklikt = false;
    }

    public void klikKnop(PApplet app) {
        isGeklikt = app.mouseX > this.x && app.mouseX < this.x + this.breedte && app.mouseY > this.y && app.mouseY < this.y + this.hoogte;
    }

    public void tekenKnop(PApplet app) {
        app.stroke(0);
        app.fill(6, 57, 112);
        app.rect(this.x, this.y, this.breedte, this.hoogte);
        app.fill(255);
        app.textAlign(app.CENTER);
        app.textSize(this.tekstGrootte);
        app.text(this.tekst, this.x + (this.breedte / 2), this.y + (this.hoogte / 2) + (this.tekstGrootte / 4));
    }

    public boolean isGeklikt() {
        return isGeklikt;
    }

    public float getHoogte() {
        return hoogte;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
