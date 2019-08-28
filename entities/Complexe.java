package entities;

public class Complexe {
    //Partie réelle
    private double r = 0;
    //partie imaginaire
    private double im = 0;
    
    //constructeur sans param renvoie un nmbr complexe initialisé à (0,0)
    public Complexe() {
    }
    /**
     * 
     * @param r
     * @param im 
     */
    public Complexe(double r, double im) {
        this.r = r;
        this.im = im;
    }

   
    public double getR() {
        return r;
    }
    
    public void setR(double r) {
        this.r = r;
    }
    
    public double getIm() {
        return im;
    }

    public void setIm(double im) {
        this.im = im;
    }

    /**
     * @param c nombre complexe
     * @return un nombre complexe résultant de la somme du nombre complexe courant et celui passé en paramêtre 
     */
    public Complexe plus(Complexe c) {
        return new Complexe(this.r + c.getR(), this.im + c.getIm());
    }

    /**
     * @param c nombre complexe
     * @return un nombre complexe résultant de la multiplication du nombre complexe courant et celui passé en paramêtre 
     */
    public Complexe times(Complexe c) {
        return new Complexe((this.r * c.getR()) - (this.im * c.getIm()), (this.r * c.getIm()) + (this.im * c.getR()));
    }

    /**
     * @return la distance du point par rapport au centre (0,0)
     */
    public double modulus() {
        return Math.sqrt(r * r + im * im);
    }

    /**
     * méthode de classe
     * @param x0 Notre 
     * @param c
     * @param maxIteration
     * @return 
     */
    public static int divergenceIndex(Complexe x0, Complexe c, int maxIteration) {
        int ite = 0;
        Complexe xn = x0;
        for (ite = 0; ite < maxIteration ; ++ite) {
            xn = c.plus(xn.times(xn));
            if (xn.modulus() > 2) {
                break;
            }
        }
        return ite;
    }

    public String toString() {
        return (im >= 0) ? "Z = " + r + " +i " + Math.abs(im) : "Z = " + r + " -i" + Math.abs(im);
    }

}
