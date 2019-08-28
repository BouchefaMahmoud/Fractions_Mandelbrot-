/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import entities.Complexe;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JApplet;
import javax.swing.JPanel;

/**
 *
 * @author VMI
 */
public class Modelbrot extends JPanel {
    
    
    
        // notre constante 
    private Complexe c;
    private Color b_color = Color.WHITE;

    public void setB_color(Color b_color) {
        this.b_color = b_color;
    }
    // Itérations avant de déclarer une partie de l'ensemble
    public static int maxIt = 64;

    // un setter
    public void setStart(Complexe c) {
        this.c = c;
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

        Complexe x0 = new Complexe();
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

                // Mise à l'échelle des pixels: map sur une zone 4x4 avec (0,0) au centre
                x0.setR(((double) x - d.width / 2) / d.width * 4);
                x0.setIm(-((double) y - d.height / 2) / d.height * 4);

                nombreIter = Complexe.divergenceIndex(x0, c, maxIt);
                // Nous renforçons artificiellement les couleurs
                nombreIter = (int) (maxIt * Math.sqrt((double) nombreIter / maxIt));

                // Définir la couleur et tracer le pixel calculé actuel
                int r = b_color.getRed() - nombreIter * d_color.getRed() / maxIt;
                int gr = b_color.getGreen() - nombreIter * d_color.getGreen() / maxIt;
                int bl = b_color.getBlue() - nombreIter * d_color.getBlue() / maxIt;
                g.setColor(new Color(Math.abs(r), Math.abs(gr), Math.abs(bl)));
            
                g.drawLine(x, y, x, y);
            }
        }
        g.setColor(d_color);
        String s = "Partie Réelle: " + c.getIm();
        g.drawString("Partie Imaginaire: " + c.getR(), 5, d.height - 5 - fm.getHeight());
        g.drawString(s, d.width - 5 - fm.stringWidth(s), d.height - 5 - fm.getHeight());

    }
}






