import java.io.InputStream;
import java.util.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/////////////////////////////////////////////////
// PROGRAMMA CHE GESTISCE IL CLIENT DELLA CHAT // 
/////////////////////////////////////////////////

// classe che lancia due thread, uno per mandare messaggi e uno per riceverli
class ChatClient  {
    private int port; 
    private InetAddress ip; 
    Thread send, receive; 
    boolean stopThread=false; 

    public ChatClient(InetAddress ip, int port) {
        this.port=port;
        this.ip=ip;
    }
 

    public void startClient() throws IOException, InterruptedException {

        String launch; // stringa che notifica l'avvenuta connessione
        Socket socket=new Socket(ip, port);
        Scanner stdin = new Scanner(System.in); 
        Scanner socketIn ; // stream di entrata
        PrintWriter socketOut; // stream di uscita
         
 
        try {
             socketOut = new PrintWriter(socket.getOutputStream());
             socketIn = new Scanner(socket.getInputStream()) ;
             System.out.println("Connection estabilished");

             //sono connesso con il server, attendo notifiche
             launch=socketIn.nextLine();             
             System.out.println(launch);
             System.out.println("If you want to exit, just type 'quit' ");

             this.send=new Thread(new Send(socketOut)) ;
             this.receive=new Thread(new Receive(socketIn));
             
             send.start();
             receive.start();
        }
 
        catch (IOException e) {
             System.out.println("Error");
             socket.close();
        }

        
       
    }


}
//////////////////////////////////////////////////////////////////////////////
//Thread per mandare i messaggi

class Send implements Runnable  {

   private PrintWriter socketOut; // serve solo lo stream di uscita
   

   public Send(PrintWriter given_socketOut) {
       
       this.socketOut=given_socketOut;
    }


   public void run()  {

       boolean stopThread=false; // variabile di controllo
       Scanner stdin = new Scanner(System.in); 
       String line; // messaggio da invare 
   
       while (!stopThread)  {
      
           line = stdin.nextLine();
           socketOut.println(line);
           socketOut.flush();

           if (line.equals("quit")) {
              stopThread=true;
           }
             
       }
       
       System.out.println("You can't send messages anymore");   
   }
}

//////////////////////////////////////////////////////////////////////////////
// Thread per ricevere messaggi


class Receive implements Runnable {

    private Scanner socketIn; //serve solo lo stream di entrata
    

    public Receive(Scanner given_socketIn)  {
        
        this.socketIn=given_socketIn;

    }
 

    public void run()  {
     
        String socketLine; //messaggio ricevuto
        boolean stopThread=false; //variabile di controllo
     
          
         while (!stopThread) { 
       
            if (socketIn.hasNextLine())  {
                socketLine=socketIn.nextLine();
         
                if (socketLine.equals("quit"))  stopThread=true;         
                
                else    System.out.println("Received: "+ socketLine);             
            }
         
         }

         System.out.println("You can't receive messages anymore.");
         
      
         
    }
}

////////////////////////////////////////////////////////////////////


public class Chat_Client  {
    //main
    public static void main(String[] args) throws InterruptedException {
         String address; //indirizzo del server
         int port; // porta a cui connettersi
         // immetto tutto da terminale
         
         Scanner standard = new Scanner(System.in);  
         System.out.println("Address:");
         address=standard.nextLine();
         System.out.println("Port");
         port=standard.nextInt();
  
         // creo il client
         try {
             ChatClient Session=new ChatClient(InetAddress.getByName(address), port);

             try {
                
                Session.startClient(); 
             }

             catch (IOException e ){
                System.err.println(e.getMessage());
             }
          }

        catch (UnknownHostException e){
             System.out.println("Host sconosciuto");
        }

 
  
   }

}

