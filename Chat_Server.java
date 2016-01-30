import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.net.*;

/////////////////////////////////////////////////
// PROGRAMMA CHE GESTISCE IL SERVER DELLA CHAT // 
/////////////////////////////////////////////////

//classe che riceve i messaggi da un client e li passa all'altro
class ChatServerClientHandler extends Thread {
    
    private Scanner in; // stream di entrata
    private PrintWriter  out; // stream di uscita
    private Socket socket; // porta 
    private String led;  //led a cui e' connesso l'utente
    
    //costruttore 
    public ChatServerClientHandler(Socket given_socket, Scanner given_in, PrintWriter given_out, String given_led) {
         this.in=given_in;
         this.socket=given_socket;
         this.out=given_out;
         this.led=given_led;
    }
    
    // metodo che prende un messaggio dallo stream di entrata di un utente lo passa allo stream di uscita dell'altro
    // si esce scrivendo "quit" 
    public void run() {
         Process myProcess;
         String line;
         boolean stopThread=false;
         
                      
              while (!stopThread){
             
                   line=in.nextLine();
                   out.println(line);
                   out.flush();
                   try {
                      myProcess=Runtime.getRuntime().exec("sudo python ../led/led2.py "+led);
                   }
                   catch(IOException e){
                      System.out.println("Led non acceso");
                   }
                   if (line.equals("quit")) stopThread=true;

              } 

              System.out.println("Connection Closed by "+ socket);
    
               
    }
}
 ////////////////////
 // classe che smista le connessioni
 // prende in entrata il numero della porta a cui si connettono i client
class ChatServer {

    private int port;
    //costruttore
    public ChatServer(int given_port){
        this.port=given_port;
    }
   
    //metodo che attiva il server
    public void startserver() {
         
         PrintWriter out1, out2; // connessioni in uscita del primo  del secondo utente
         Scanner in1, in2;  // connessioni in entrata del primo e del secondo utente
         ServerSocket serversocket; // porta su cui sta in ascolto il server
         ExecutorService executor = Executors.newCachedThreadPool(); 
         Socket socket1, socket2; // porte su cui vengono smistati i due utenti
         String firstLed; 
         String secondLed;
         firstLed="18";
         secondLed="23";
         Process myProcess;

         try {
              serversocket=new ServerSocket(port);
         }
        
         catch (IOException e) {
              System.err.println(e.getMessage());
              return ;
         } 

         System.out.println("Server ready");
   
         while (true) {
              try {
                   //attendo la connessione del primo
                   // e creo gli stream di entrata e uscita
                   socket1=serversocket.accept(); 
                   System.out.println("Received Client Connection "+socket1);
             
                   out1=new PrintWriter(socket1.getOutputStream());
                   in1=new Scanner(socket1.getInputStream());
                   try {
                      myProcess=Runtime.getRuntime().exec("sudo python ../led/led2.py "+firstLed);
                   }
                   catch(IOException e){
                      System.out.println("Led non acceso");
                   }
                   // attendo la connessione del secondo
                   // e creo gli stream di entrata e uscita

                   socket2=serversocket.accept();
                   System.out.println("Received Client Connection "+socket2);

                   out2=new PrintWriter(socket2.getOutputStream());
                   in2=new Scanner(socket2.getInputStream()); 
       
                   try {
                      myProcess=Runtime.getRuntime().exec("sudo python ../led/led2.py "+secondLed);
                   }
                   catch(IOException e){
                      System.out.println("Led non acceso");
                   }
                   //notifico l'avvenuta connessione agli utenti
                   out1.println("Chat ready!");
                   out1.flush();
       
                   out2.println("Chat ready!");
                   out2.flush();
            
                   // grazie a due processi distinti posso mandare un messaggio senza attendere la risposta dell'altro
                   // ad ogni utente e' collegato un led
                   executor.submit(new ChatServerClientHandler(socket1, in1, out2, firstLed)); 
                   executor.submit(new ChatServerClientHandler(socket2, in2, out1, secondLed));
              } 

              catch (IOException e) {
                   break; 
              }
         }

       executor.shutdown();
       
    }
}

///////////////////////////////////////

public class Chat_Server {

    public static void main(String[] args) {
         
         // attiva il server 
         ChatServer session=new ChatServer(4332);
         session.startserver(); //attivo il server
    }

}

