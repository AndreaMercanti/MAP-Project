import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

//import java.util.ArrayList;

import javax.swing.*;

public class KMeans extends JFrame {
    /**S t r e a m   p e r   i n v i a r e   a l   s e r v e r */
    private ObjectOutputStream out;
    /**S t r e a m   p e r   r i c e v e r e   d a l   s e r v e r */
    private ObjectInputStream in;

    /**
     * Il costruttore ha l'incarico di creare il frame e chiamare il metodo di 
     * inizializzazione {@link #init(java.lang.String, int)}
     * @throws HeadlessException 
     */
    public KMeans(String server_addr, int port) throws HeadlessException {
        super();
        init(server_addr, port);
    }
    
    /**
     * Il metodo si occupa di inizializzare il frame inserendo al suo interno le 
     * due sezioni (tab) per la gestione delle richieste da parte dell'utente.
     * Viene inoltre stabilita una connessione al server e quindi si ottengono i 
     * relativi stream di output e di input, andando a inizializzare i due attributi 
     * {@link #in} e {@link #out}.
     * Nel caso in cui non è possibile stabilire una connessione con il server 
     * viene mostrato un messaggio di errore. Sarà quindi necessario riaggiornare 
     * la pagina del browser per ritentare una connessione.
     * 
     * @param server_addr nome DNS del server
     * @param port numero di porta del server
     */
    public void init(String server_addr, int port){
        Container apCont = this.getContentPane();
        apCont.setLayout(new GridLayout(1, 1));

        Socket socket = null;
        try{
            InetAddress serverAddr = InetAddress.getByName(server_addr); 
            socket = new Socket(serverAddr, port);  //create a client socket that connects to serverAddr at port
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            TabbedPane tab = new TabbedPane();
            this.getContentPane().add(tab);
        } catch(IOException e){
            JOptionPane.showMessageDialog(this, "Impossibile Connttersi al Server!");
            apCont.add(Box.createRigidArea(new Dimension(20, 20)));
            apCont.add(new JLabel("Impossibile Connttersi al Server!"));
            apCont.add(Box.createRigidArea(new Dimension(20, 20)));
            apCont.add(new JLabel("Non e' possibile eseguire alcuna operazione."));
            apCont.add(Box.createRigidArea(new Dimension(20, 20)));
            apCont.add(new JLabel("Chiudere e riaprire la finestra per tentare nuovamente una nuova connessione."));
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Il metodo principale per l'esecuzione dell'intero programma.
     * @param args gli argomenti a linea di comando dove, se specificati, il 
     * primo deve essere il DNS del server e il secondo deve essere la porta su 
     * cui il sever è in ascolto.
     */
    public static void main(String[] args) {
        KMeans window;
        if(args.length == 1)
            window = new KMeans(null, 8080);
        else
            window = new KMeans(args[1], Integer.parseInt(args[2]));
    }
    
    /**
     * Inner class privata che estende JPanel. La classe rappresenta il pannello 
     * principale del frame.
     */
    private class TabbedPane extends JPanel {
        /**Tab per l'utilizzo delle funzionalità  su DB*/
        private JPanelCluster panelDB;
        /**Tab per l'utilizzo delle funzionalità  su file*/
        private JPanelCluster panelFile;

        /**
         * Il costruttore della classe si occupa di assegnare un layout al pannello 
         * che conterrà le due sezioni gestendole attraverso tab con la classe 
         * {@link javax.swing.JTabbedPane}.
         * I due tab verranno inizializzati con un nome, un'icona, il relativo 
         * pannello (di tipo {@link JPanelCluster}) e un tooltip.
         * Ciascun pannello di tipo {@link JPanelCluster} verrà inizializzato con 
         * un bottone e un ascoltatore. Ciascun ascoltatore corrisponderà alle 
         * richieste di un'attività di scoperta dei cluster su database oppure di 
         * lettura di una precedente attività di scoperta da file.
         * Nel caso in cui le richieste al server non vadano a buon fine verrà 
         * mostrato un messaggio a video attraverso una finestra di tipo 
         * {@link javax.swing.JOptionPane}
         * 
         * @see javax.swing.JTabbedPane
         * @see JPanelCluster
         * @see javax.swing.JOptionPane
         */
        TabbedPane() {
            super(new GridLayout(1, 1)); 
            JTabbedPane tabbedPane = new JTabbedPane();
            java.net.URL imgURL = getClass().getResource("img/db.jpg");
            ImageIcon iconDB = new ImageIcon(imgURL);

            panelDB = new JPanelCluster("MINE", new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        learningFromDBAction();
                    } catch(SocketException e1) {
                        JOptionPane.showMessageDialog(panelFile, "Errore! - Impossibile Connettersi al Server\n\nDettagli:\n" + e1);
                    } catch(IOException e1) {
                        JOptionPane.showMessageDialog(panelFile, e1);
                    } catch(ClassNotFoundException e1) {
                        JOptionPane.showMessageDialog(panelFile, e1);
                    }
                }
            });
            tabbedPane.addTab("DB", iconDB, panelDB, "Kmeans from Database");

            imgURL = getClass().getResource("img/file.jpg");
            ImageIcon iconFile = new ImageIcon(imgURL);
            panelFile = new JPanelCluster("STORE FROM FILE", new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        learningFromFileAction();
                    } catch(SocketException e1) {
                            JOptionPane.showMessageDialog(panelFile, "Errore! - Impossibile Connettersi al Server\n\nDettagli:\n" + e1);
                    } catch(IOException e1) {
                            JOptionPane.showMessageDialog(panelFile, e1);
                    } catch(ClassNotFoundException e1) {
                            JOptionPane.showMessageDialog(panelFile, e1);						
                    }
                }
            });
            tabbedPane.addTab("FILE", iconFile, panelFile,"Kmeans from File");

            add(tabbedPane);         

            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        }

        /**
         * Il metodo si occupa di restituire il riferimento all'oggetto istanza 
         * della classe Frame a cui verrà  momentaneamente aggiunto l'{@link AttributeDialog}.
         * 
         * @return Frame a cui aggiungere l'attribute dialog.
         */
        private Frame findParentFrame(){ 
            Component c = getParent();

            while(true){
                if(c instanceof Frame)
                    return (Frame)c;
                c = c.getParent();
            }
        }

        /**
         * Metodo privato utilizzato nel caso in cui viene richiesta un'attività 
         * di scoperta dei cluster sulla base di dati (richiedendo quindi l'utilizzo 
         * dell'algoritmo kmeans su database).<br>
         * Tale richiesta è effettuata nell'apposita sezione (tab) del frame.<br>
         * Il metodo si occupa di leggere le due caselle di testo corrispondenti 
         * al nome della tabella del database e al numero di cluster da scoprire.
         * Se viene inserito un valore minore di 0 oppure un valore maggiore 
         * rispetto al numero di centroidi generabili dall'insieme di transazioni 
         * (presenti nella base di dati), viene mostrato un messaggio di errore 
         * in una {@link javax.swing.JOptionPane}.<br>
         * Le diverse richieste al server vengono effettuate attraverso una serie 
         * di valori numerici.<br> 
         * In questa attività di scoperta dei cluster dalla base di dati, la 
         * sequenza è la seguente:
         * <li>Viene inviato il comando 0 e il nome della tabella.<br>
         * Questo comando corrisponde (sul server) a un'interrogazione sulla base di dati.<br>
         * In caso di risposta diversa da "OK", viene visualizzato un messaggio 
         * di errore in una {@link javax.swing.JOptionPane} e termina l'esecuzione del metodo.</li>
         * <li>Viene inviato il comando 1 e il numero di cluster da scoprire e 
         * aspetta la risposta del server.<br>
         * Questo comando corrisponde (sul server) alla vera e propria attività 
         * di scoperta attraverso l'algoritmo kmeans.<br>
         * In caso di risposta diversa da "OK", visualizza un messaggio di errore 
         * in una {@link javax.swing.JOptionPane} e termina l'esecuzione del metodo.</li>
         * <li>Quindi viene letto il numero di iterazioni e i cluster cos&igrave 
         * come sono trasmessi da server e li visualizza nell'area di testo 
         * corrispondente all'output del server {@link JPanelCluster#clusterOutput}.</li>
         * <li>Viene inviato al server il comando 2 e aspetta la risposta del server.
         * Questo comando corrisponde (sul server) al salvataggio su file dell'attività  
         * di scoperta per letture successive.<br>
         * In caso di risposta diversa da "OK", visualizza un messaggio di errore 
         * in una {@link javax.swing.JOptionPane} e termina l'esecuzione del metodo.</li>
         * <li>Viene inviato al server il comando 4 e il tipo di azione che si 
         * sta effettuando (ovvero un'attività di scoperta dalla base di dati).<br>
         * Questo comando corrisponde (sul server) alla generazione di tutti quei 
         * dati per la creazione del grafico.<br>
         * Infatti inviato questo comando viene richiamato il metodo {@link #chartAction(JPanelCluster)} 
         * che utilizzerà  i dati restituiti dal server per il grafico che verrà 
         * inserito nel pannello {@link JPanelCluster#panelChart} utile a questo scopo.<br>
         * Nel caso in cui anche quest'ultima operazine vada a buon fine verrà 
         * mostrato un messaggio ("OK") in una {@link javax.swing.JOptionPane} 
         * terminando l'esecuzione del metodo.</li>
         * 
         * @throws SocketException	
         * @throws IOException Sollevata in caso di errori nelle operazioni di lettura dei
         *                     risultati dal server.
         * @throws ClassNotFoundException Sollevata nelle operazioni di lettura dei risultati da file
         *                                nel caso in cui gli oggetti passati sullo stream sono
         *                                istanza di una classe non presente su client.
         */
        private void learningFromDBAction() throws SocketException, IOException, ClassNotFoundException {
            int k = 0;
            String tableName = "";
            String result = "";

            tableName = this.panelDB.kText.getText();
            if(tableName.equals("")){
                JOptionPane.showMessageDialog(this, "Errore! - Inserire il nome della tabella!");
                return;
            }

            try {
                k = new Integer(panelDB.kText.getText()).intValue();
            } catch(NumberFormatException e){
                JOptionPane.showMessageDialog(this, "Errore! - Inserire un valore per k!");
                return;
            }

            out.writeObject(0);
            out.writeObject(tableName);

            result = (String)in.readObject();

            if(!result.equals("OK"))
                JOptionPane.showMessageDialog(this, result);
            else{
                out.writeObject(1);
                out.writeObject(k);

                result = (String)in.readObject();
                if(!result.equals("OK")){
                    JOptionPane.showMessageDialog(this, result);
                    return;
                } else {
                    Integer iter = (Integer)in.readObject();

                    panelDB.clusterOutput.setText((String)in.readObject() + "Numero iterate: " + iter + "\n");

                    out.writeObject(2);

                    result = (String)in.readObject();

                    if(!result.equals("OK")){
                        JOptionPane.showMessageDialog(this, result);
                        return;
                    } else{
                        out.writeObject(4);
                        out.writeObject("db");
                    }
                }
            }
        }

        /**
         * Metodo privato utilizzato nel caso in cui viene richiesta la funzione 
         * di lettura di un determinato file su server. Tale richiesta è effettuata 
         * nell'apposita sezione (tab) del frame.<br>
         * Il metodo si occupa di leggere il nome della tabella e il numero di 
         * cluster, la cui concatenazione va a costituire il nome del file presente su server.<br>
         * Viene quindi effettuata la richiesta al server (attraverso il comando 3) 
         * il quale, nel caso positivo, visualizza in una JOptionPane un messaggio 
         * che confermi il successo della attività, visualizzando poi il risultato 
         * nell'area di testo corrispondente {@link JPanelCluster#clusterOutput}.<br>
         * Viene inviato al server il comando 4 e il tipo di azione che si sta 
         * effettuando (ovvero un'attività di lettura da file).<br>
         * Questo comando corrisponde (sul server) alla generazione di tutti quei dati per la creazione
         * del grafico.<br>
         * Inviato questo comando viene richiamato il metodo {@link #chartAction(JPanelCluster)}
         * che utilizzerà i dati restituiti dal server per il grafico che verrà 
         * inserito nel pannello {@link JPanelCluster#panelChart} utile a questo scopo.<br>
         * In caso di risposta diversa da "OK" viene mostrato un messaggio di errore
         * attraverso una finestra di tipo {@link javax.swing.JOptionPane}.
         * 
         * @throws SocketException
         * @throws IOException Sollevata in caso di errori nelle operazioni di lettura dei
         *                     risultati dal server.
         * @throws ClassNotFoundException Sollevata nelle operazioni di lettura dei risultati da file
         *                                nel caso in cui gli oggetti passati sullo stream sono
         *                                istanze di una classe non presente su client.
         */
        private void learningFromFileAction() throws SocketException, IOException, ClassNotFoundException {
            String tableName = panelFile.tableText.getText();
            String ncluster = panelFile.kText.getText();

            if(tableName.equals("")){
                JOptionPane.showMessageDialog(this, "Errore! - Inserire il nome della tabella!");
                return;
            }

            if(ncluster.equals("")){
                JOptionPane.showMessageDialog(this, "Errore! - Inserire un valore per k!");
                return;
            }

            out.writeObject(3);
            out.writeObject(tableName);
            out.writeObject(ncluster);

            String result = (String)in.readObject();

            if(!result.equals("OK")){
                JOptionPane.showMessageDialog(this, result);
                return;
            } else {
                JOptionPane.showMessageDialog(this, "OK!");
                String kmeansString = (String)in.readObject();
                String newStr = kmeansString.replaceAll("\\)- ", "\\)\n");

                out.writeObject(4);
                out.writeObject("file");
                panelFile.clusterOutput.setText(newStr);
                return;
            }
        }
        
        /**
         * Classe privata, che modella un JPanel, che rappresenta ogni singola 
         * sezione (tab) che fa parte del frame.
         * La sezione creata verrà poi aggiunta ad un {@link javax.swing.JTabbedPane}.
         * Ciascuna sezione contiene al suo interno:
         * <ul>
         * <li>Una casella di testo dove verrà inserito il nome della tabella dalla 
         * quale prelevare i dati con cui lavorare;</li>
         * <li>Una casella di testo in cui verrà inserito il numero di cluster che 
         * si intende scoprire</li>
         * <li>Una area di testo dalla quale visionare il risultato della elaborazione
         * dell'algortimo di clustering K-Means</li>
         * <li>Un bottone che si occuperà di effettuare la richiesta di clustering al server.</li>
         * </ul>
         */
        private class JPanelCluster extends JPanel{
            /**
             * Casella di testo dove verrà inserito il nome della tabella.
             * 
             * @see javax.swing.JTextField
             */
            private JTextField tableText = new JTextField(20);

            /**
             * Casella di testo in cui verrà inserito il numero di cluster che si intende scoprire.
             * Tale valore sarà concatenato a quello della casella di testo precedente.
             * 
             * @see javax.swing.JTextField
             */
            private JTextField kText = new JTextField(10);
            
            /**
             * Conterrà l'output del server. Essa sarà un'area di testo non editabile.
             */
            private JTextArea clusterOutput = new JTextArea(20, 20);
            
            /**
             * Il bottone che si occuperà di effettuare una particolare richiesta al server.
             * Al bottone verrà associato un nome e un ascoltatore entrambi passati 
             * come argomenti al costruttore.
             * 
             * @see javax.swing.JButton
             */
            private JButton executeButton;

            /**
             * Il costruttore si occupa di creare ciascuna sezione (tab) andandole 
             * a suddividere in due parti.<br>
             * Nella parte sinistra ci saranno:
             * <li>Due texfield per la lettura del nome della tabella e il numero di cluster</li>
             * <li>Una area di testo scorrevole non editabile contenente l'output del server</li>
             * <li>Un bottone per l'invio della richiesta al server</li>
             * Il bottone avrà nome pari al parametro <i>buttonName</i> e 
             * l'ascoltatore che si occuperà di gestire una particolare richiesta 
             * rappresentato dal parametro <i>a</i>.<br>
             * Nella parte destra ci sarà il pannello contenente il relativo grafico 
             * e quando necessario anche la legenda con alcune funzionalità utili 
             * per gestire al meglio la visualizzazione del grafico stesso.
             *  
             * @param buttonName Nome per il bottone {@link #executeButton}
             * @param a Ascoltatore da assegnare al bottone {@link #executeButton}
             */
            JPanelCluster(String buttonName, java.awt.event.ActionListener a){
                super(new FlowLayout());
                JScrollPane outputScroll = new JScrollPane(clusterOutput);
                JPanel mainLeft = new JPanel();
                JPanel mainRight = new JPanel();

                mainLeft.setPreferredSize(new Dimension(450, 600));
                mainLeft.setLayout(new BorderLayout());
                mainRight.setLayout(new BorderLayout());

                JPanel upPanel = new JPanel();
                JPanel centralPanel = new JPanel();
                JPanel downPanel = new JPanel();

                upPanel.add(new JLabel("Tabel"));
                upPanel.add(this.tableText);
                upPanel.add(new JLabel("k"));
                upPanel.add(this.kText);

                clusterOutput.setEditable(false);
                clusterOutput.setLineWrap(true);

                centralPanel.setLayout(new BorderLayout());
                centralPanel.add(outputScroll, BorderLayout.CENTER);

                downPanel.setLayout(new FlowLayout());
                downPanel.add(this.executeButton = new JButton(buttonName));

                mainLeft.add(upPanel, BorderLayout.NORTH);
                mainLeft.add(centralPanel, BorderLayout.CENTER);
                mainLeft.add(downPanel, BorderLayout.SOUTH);

                add(mainLeft);
                add(mainRight);

                this.executeButton.addActionListener(a);
            }
        }
    }
}