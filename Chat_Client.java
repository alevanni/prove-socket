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
//correggi la prima
//prova la connessione sul localhost
class chatclient  {
    private int port; //numero della porta
    private InetAddress ip; //indirizzo ip
    Thread send, receive; //ci sono due thread, uno per ricevere e uno per mandare
    boolean stopthread=false; //per interrompere

    public chatclient(InetAddress ip, int port) {
        this.port=port;
        this.ip=ip;
    }//costruttore
 

    public void startClient() throws IOException {//metodo

        String avvio;
        Socket socket=new Socket(ip, port);
        Scanner stdin = new Scanner(System.in); 
        Scanner socketIn ;
        PrintWriter socketOut;
        String s;  
 
        try {
             socketOut = new PrintWriter(socket.getOutputStream());
             socketIn = new Scanner(socket.getInputStream()) ;
             System.out.println("Connection estabilished");
             //sono connesso con il server, attendo notifiche
             avvio=socketIn.nextLine();
             //stampo l'avviso di avvenuta connessione
             System.out.println(avvio);
        
             this.send=new Thread(new Send(socket, socketOut)) ;
             this.receive=new Thread(new Receive(socket, socketIn));
             
             send.start();
             receive.start();
     
        }
 
        catch (IOException e) {
             System.out.println("Error");
             socket.close();
        }

  
       //System.out.println("Chiuso startclient");
    }


}
//////////////////////////////////////////////////////////////////////////////
//elimina il socket non ti serve
class Send implements Runnable  {

   private Socket socket;
   private PrintWriter socketOut;
   

   public Send(Socket given_socket, PrintWriter given_socketOut) {
       this.socket=given_socket;
       this.socketOut=given_socketOut;
    }


   public void run()  {
       boolean stopthread=false;
       Scanner stdin = new Scanner(System.in); //per prendere da tastiera
       String line;
   
       while (!stopthread)  {
      
           System.out.print("I say: ");
           line = stdin.nextLine();
         
           if (line.equals("quit")) {
              stopthread=true;
              socketOut.println(line);
              socketOut.flush();
           }
           else {
              socketOut.println(line);
              socketOut.flush();
           }
       }
       this.socketOut.close();
      
       System.out.println("You cannot send messages");   

    }

}

//////////////////////////////////////////////////////////////////////////////
//elimina il socket, non ti serve
class Receive implements Runnable {

    private Socket socket;
    private Scanner socketIn;
    

    public Receive(Socket given_socket, Scanner given_socketIn)  {
        this.socket=given_socket;
        this.socketIn=given_socketIn;

    }
 

   public void run()  {
     
        String socketline;
        boolean stopthread=false; //variabile di controllo
     
          
         while (!stopthread) { 
       
            if (socketIn.hasNextLine())  {
                socketline=socketIn.nextLine();
         
                if (socketline.equals("quit")) { 
                     stopthread=true;
                }
                else  {
                     System.out.println(socketline);
                }
            }
         
         }

         this.socketIn.close();
    

         
     System.out.println("You cannot receive messages");
    }
}

////////////////////////////////////////////////////////////////////
 //gia' corretto
public class Chat_Client {

    public static void main(String[] args)  {
         String address;
         int port;
         //immetto l'indirizzo e la porta del server
         Scanner standard = new Scanner(System.in);
         System.out.println("Address:");
         address=standard.nextLine();
         System.out.println("Port");
         port=standard.nextInt();
  
         //ora creo il client
         try {
             chatclient Session=new chatclient(InetAddress.getByName(address), port);

             try {
                //attivo startclient
                Session.startClient(); 
             }//try

             catch (IOException e ){
                System.err.println(e.getMessage());
             }//catch
          }//try

        catch (UnknownHostException e){
             System.out.println("Host sconosciuto");
        }

 
  
   }

}

