package View;

import java.awt.*;
import javax.swing.*;
import entities.Complexe;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Cette classe est l'interface graphique de l'application
 */
public class JuliaFrame extends JFrame {

    private JScrollBar realScroll;
    private JScrollBar imagScroll;
    private JuliaDessin j;
    // constante servant à définir les valeurs de realScroll et imagScroll 
    private static final int STEPS = 100;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem saisie;
    private JMenuItem exporterImage;
    private JMenuItem redemarrer;
    private JMenuItem Clonner;
    private JMenuItem backgroundColor;
    private JPanel contentPane;

    //nombre complexe qui sert de constante pour la fonction Z-> Z*Z + C
    private Complexe c;
    private Ensemble e = null;

    /**
     * Constructeur
     *
     * @param c : Z-> Z*Z + c
     */
   
    public JuliaFrame(Complexe c, Ensemble e) {
        if (c == null) {
            this.c = new Complexe();
        } else {
            this.c = c;
        }
        this.e = e;
        init(c);
        listeners();
    }

    /**
     * Méthode appelé au démarrage pour initialiser nos composants graphiques et
     * d'appeler la classe JuliaDessin en lui passant la constante C
     *
     * @param c
     */
    private void init(Complexe c) {
        this.setSize(new Dimension(700, 700));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("Julia Fractale");
        j = new JuliaDessin();
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        // Crée deux barres de défilement pour définir des parties réelles et imaginaires
        contentPane.add(realScroll = new JScrollBar(Adjustable.VERTICAL), "West");
        contentPane.add(imagScroll = new JScrollBar(Adjustable.VERTICAL), "East");

        realScroll.setValues(0, 0, -2 * STEPS, 2 * STEPS);
        imagScroll.setValues(0, 0, -2 * STEPS, 2 * STEPS);

        // Ajouter un menuBar
        menuBar = new JMenuBar();
        menu = new JMenu("Générer");
        saisie = new JMenuItem("Saisie Automatique");
        exporterImage = new JMenuItem("Exporter l'image ");
        redemarrer = new JMenuItem("Redémarrer");
        Clonner = new JMenuItem("Clonner");
        backgroundColor = new JMenuItem("Modifier la couleur du fond");

        menu.add(saisie);
        menu.add(exporterImage);
        menu.add(redemarrer);
        menu.add(Clonner);
        menu.add(backgroundColor);

        if (e == Ensemble.Mandelbrot) {
            saisie.setEnabled(false);
            realScroll.setVisible(false);
            imagScroll.setVisible(false);
            redemarrer.setEnabled(false);
        }

        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        contentPane.add(j, BorderLayout.CENTER);
        this.add(contentPane);
        j.setE(e);
        j.setC(this.c);
        this.setVisible(true);

    }

    /**
     * Methode appelée pour redessiner en fonction de c passé en parametre
     *
     * @param c
     */
    public void repaintEvent(Complexe c) {
        j.setC((c == null) ? new Complexe((double) realScroll.getValue() / STEPS, (double) imagScroll.getValue() / STEPS) : c);
        j.repaint();
    }

    /**
     * Methode regroupant nos listeners
     */
    private void listeners() {
        // Ajoute un auditeur sensible aux variations 
        realScroll.addAdjustmentListener(e -> repaintEvent(null));
        imagScroll.addAdjustmentListener(e -> repaintEvent(null));

        //lancer une fenetre en saissant la canstante c
        saisie.addActionListener(e -> {
            Saisi saisieframe = new Saisi();
            saisieframe.setVisible(true);
        });

        redemarrer.addActionListener(e -> {
            this.dispose();
            new JuliaFrame(new Complexe(), this.e);
        });

        /**
         * Choisir une couleur du fond
         */
        backgroundColor.addActionListener(e -> {
            Color color = Color.WHITE;
            color = JColorChooser.showDialog(null, "Veuillez sélectionner une couleur", color);
            j.setE(this.e);
            j.setB_color(color);
            j.repaint();
        });
        //la fonction devient trop grande
        imageListener();
        cloneListener();
    }

    /**
     * exportation de l'image, dans un dossier choisis
     */
    public void imageListener() {

        exporterImage.addActionListener(e -> {
            //création d'un explorateur de dossier
            JFileChooser cf = new JFileChooser();
            cf.setDialogTitle("Veuillez Choisir un dossier");
            //exploration des dossier inuquement
            cf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            //si le choix approuvé    
            if (cf.showOpenDialog(exporterImage) == JFileChooser.APPROVE_OPTION) {
                //récuperation du lien
                String path = cf.getSelectedFile().getAbsoluteFile().toString();
                //nom de l'image
                String name = "c=(" + j.getC().getR() + "," + j.getC().getIm() + ").png";
                // si on est sous linux / sera ajouté autrement \ 
                // nous prenons d'autre systems d'exploitation en compte
                path += path.contains("/") ? "/" : "\\";
                path += name;

                try {
                    // on fait passer le dessin et le lien 
                    save_pic(j, path);
                    JOptionPane.showMessageDialog(this, "Image enregistrée");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur de sauvegarde d'image ");
                    Logger.getLogger(JuliaFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vous n'avez pas sélectionné un dossier !");
                return;
            }
        });

    }

    // créer une deuxième fentre identique sans fermer la première
    public void cloneListener() {
        Clonner.addActionListener(e -> {
            //l'execution se fait dans un processus séparé
            new Thread(() -> {
                JuliaFrame juliaFrame = new JuliaFrame(j.getC(), this.e);
                //sa fermiture n'arrêtera pas le programme
                juliaFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                // adaptation des valeurs des scroll
                juliaFrame.getRealScroll().setValue(this.realScroll.getValue());
                juliaFrame.getImagScroll().setValue(this.imagScroll.getValue());
            }).start();
        });
    }

    /**
     * Methode qui crée l'image
     *
     * @param component notre dessin
     * @return une image
     */
    public static BufferedImage take_pic(Component component) {
        //en fonction de la taille du conteneur
        //on choisit un system de coloriage 
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_BGR);
        component.paint(image.getGraphics());
        return image;
    }

    /*
        methode qui exporte l'image
     */
    public static void save_pic(Component component, String path) throws Exception {
        //prend en parametre notre image, son format, ainsi qu'un fichier ayant un flux de sortie
        ImageIO.write(take_pic(component), "png", new FileOutputStream(new File(path)));
    }

    //getter
    public JScrollBar getRealScroll() {
        return realScroll;
    }

    //getter
    public JScrollBar getImagScroll() {
        return imagScroll;
    }

    /*
    public static void main(String[] args) {
        new JuliaFrame(new Complexe());
    }*/
}
