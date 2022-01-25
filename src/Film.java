public class Film {
    private String naam;
    private double prijs;
    private int index;

    public Film(String naam, double prijs) {
        this.naam = naam;
        this.prijs = prijs;
    }

    public String getNaam() {
        return this.naam;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public String toString() {
        String tekst = "";
        tekst += "*-----------------------------*";
        tekst += "\n";
        tekst += "Film naam: " + this.getNaam();
        tekst += "\n";
        tekst += "Film prijs: " + this.getPrijs();

        return tekst;
    }

    public double getPrijs() {
        return this.prijs;
    }
}
