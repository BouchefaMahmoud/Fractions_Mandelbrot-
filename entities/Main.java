/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import View.*;
import java.awt.Dimension;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import javax.xml.ws.Response;

/**
 *
 * @author VMI
 */
public class Main {

   
    /**
     * Démarrage de l'app
     * @throws Exception 
     */
    public static void startApp() throws Exception {
        //Scanner s = new Scanner(System.in);
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("***********Projet de java******************");
        System.out.println("* Université Paris Diederot               *");
        System.out.println("* Réalisé par :                           *");
        System.out.println("* BOUCHEFA Mahmoud & EL RAIS David        *");
        System.out.println("*******************************************");

        
        String response = "";
        do {

             
                while (!response.matches("[01]")) {
                    System.out.println("Voulez vous généner un ensemble de :\n"
                            + "0 :Julia ?"
                            + "\n1 :Mandelbrot ?");
                    response= s.readLine();
                }
                Ensemble e= response.equals("0")?Ensemble.Julia : Ensemble.Mandelbrot ;
            
             response = "";
            while (!response.matches("[01]")) {
                System.out.println("\n Voulez vous exécuter l'application en mode :\n"
                        + "0 :Console ?"
                        + "\n1 :Graphique ?");
                response = s.readLine();
            }
            if (response.equals("1")) {
               
                new Thread(() -> new Splash(e).setVisible(true)).start();
                
            } else {
                String r = "";
                String im = "";
                String x="0.0" , y="0.0";
                
                while(!(x.matches("[0-9]+") && y.matches("[0-9]+"))){
                System.out.println("Veuillez saisir les dimensions de l'image  : ");
                        System.out.println("WIDTH : ");x= s.readLine();
                        System.out.println("HEIGHT : "); y= s.readLine();
                }
                
                if( e == Ensemble.Julia) {
                while (!(Saisi.isValid(im) && Saisi.isValid(r))) {
                    System.out.println("Veuillez Saisir une constante, C = : ");
                    System.out.println("Partie Réelle : ");
                    r = s.readLine();
                    System.out.println("Partie Imaginaire : ");
                    im = s.readLine();
                } 
                }

                JuliaDessin j = new JuliaDessin();
                j.setSize(new Dimension(Integer.valueOf(x), Integer.valueOf(y)));
                j.setC((e==Ensemble.Julia)? new Complexe(Double.valueOf(r), Double.valueOf(im)): new Complexe() );
                j.setE(e);
                j.repaint();

                String path = "";
                System.out.println("Veuillez saisir un lien d'exportation d'image: ");
                path = s.readLine();

                Boolean bool = false;

                while (!bool) {
                    try {

                        JuliaFrame.save_pic(j, path);
                        System.out.println("Image Enregistrée avec succes");
                        bool = true;
                    } catch (Exception ex) {
                        // printStackTrace(ex);
                        // System.out.println(path);
                        System.out.println("Erreur ! Veuillez ressaisir votre lien :");
                        path = s.readLine();
                    }
                }
            }
            response ="";
            System.out.println("Voulez vous recommencer ? oui/non");
            response = s.readLine();

        } while (response.equals("oui"));

    }

    public static void main(String[] args) throws Exception {
        startApp();
    }
}
