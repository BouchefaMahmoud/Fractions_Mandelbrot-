package View;

/**
 * Dans ce projet nous utilisons l'objet Graphics
 */
import entities.Complexe;
import java.awt.*;
import javax.swing.*;

public class JuliaDessin extends JPanel {

    // notre constante 
    private Complexe c, x0;
    private Color b_color = Color.WHITE;
    private Ensemble e;

    public void setB_color(Color b_color) {
        this.b_color = b_color;
    }
    // Itérations avant de déclarer une partie de l'ensemble
    public static int maxIt = 64;

    // un setter
    public void setC(Complexe c) {
        this.c = c;
    }

    public void setE(Ensemble e) {
        this.e = e;
    }

    public void mandelbrot_julia(int x, int y, Dimension d, Ensemble e) {

        if (e == Ensemble.Mandelbrot) {
            // Mise à l'échelle des pixels: map sur une zone 4x4 avec (0,0) au centre    
            c.setR(((double) x - d.width / 2) / d.width * 4);
            c.setIm(-((double) y - d.height / 2) / d.height * 4);

        } else {
            // Mise à l'échelle des pixels: map sur une zone 4x4 avec (0,0) au centre

            x0.setR(((double) x - d.width / 2) / d.width * 4);
            x0.setIm(-((double) y - d.height / 2) / d.height * 4);
        }
    }

    /**
     * redefinition de la méthode paintComponent aura comme effet *
     * récaptulation sur notre fonction Z-> Z*Z +C Soit x0 un nombre complexe
     * calculé en fonction des dimensions de la fenetre et soit c un nombre
     * complexe quelconque xn+1 = f(x) en d'autre terme : X1 =X0*X0 + C
     * X2=X1*X1+C et ainsi de suite si |Xn|>2 alors la fonction diverge,
     * autrement elle est bornée et on va colorier en bleu les pixels calculé
     *
     *
     * @param g Objet Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = getSize();
        //nombre d'iteration avant de savoir si la fonction est non bornée  
        int nombreIter = 0;

        Color d_color = new Color(255, 100, 10);

        x0 = new Complexe();
        // choisir la font du panel
        Font f = new Font("SansSerif", Font.BOLD, 14);
        FontMetrics fm = g.getFontMetrics(f);
        g.setFont(f);
        /**
         * les deux boucles imbriqué vont representer notre plan des nombres
         * complexes x va en quelque sorte representer l'axe des réelle et y
         * celui des imaginaires
         */
        
        for (int x = 0; x < d.width; ++x) {
            
            for (int y = 0; y < d.height; ++y) {

                mandelbrot_julia(x, y, d, e);
                nombreIter = Complexe.divergenceIndex(x0, c, maxIt);
                // Nous renforçons artificiellement les couleurs
                nombreIter = (int) (maxIt * Math.sqrt((double) nombreIter / maxIt));

                // Définir la couleur et tracer le pixel calculé actuel
                /**
                 * Afin de nuancer les couleur
                 */
                int r = b_color.getRed() - nombreIter * d_color.getRed() / maxIt;
                int gr = b_color.getGreen() - nombreIter * d_color.getGreen() / maxIt;
                int bl = b_color.getBlue() - nombreIter * d_color.getBlue() / maxIt;
                g.setColor(new Color(Math.abs(r), Math.abs(gr), Math.abs(bl)));

                
                /* if(nombreIter <  maxIt) {
              g.setColor(new Color( b_color.getRed(),
                          b_color.getGreen() ,
                           b_color.getBlue() ));
             }else {
                 g.setColor(new Color( b_color.getRed() - nombreIter * d_color.getRed()/ maxIt,
                        b_color.getGreen()- nombreIter * d_color.getGreen() / maxIt,
                        b_color.getBlue() - nombreIter * d_color.getBlue() / maxIt));
             }*/
                //Dessiner les pixels
                g.drawLine(x, y, x, y);
            }
        }
        if (e == Ensemble.Julia) {
            g.setColor(d_color);
            String s = "Partie Réelle: " + c.getIm();
            g.drawString("Partie Imaginaire: " + c.getR(), 5, d.height - 5 - fm.getHeight());
            g.drawString(s, d.width - 5 - fm.stringWidth(s), d.height - 5 - fm.getHeight());
        }
    }

    public Complexe getC() {
        return c;
    }

}
