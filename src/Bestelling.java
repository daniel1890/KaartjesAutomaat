public class Bestelling {
    private int aantalKaartjes;
    private double prijs;
    private Film film;

    public Bestelling(int aantalKaartjes, Film film) {
        this.aantalKaartjes = aantalKaartjes;
        this.film = film;
    }

    public int getAantalKaartjes() {
        return aantalKaartjes;
    }

    public void setAantalKaartjes(int aantalKaartjes) {
        this.aantalKaartjes = aantalKaartjes;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }
}
