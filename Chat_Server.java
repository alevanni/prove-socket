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

    private Scanner in;
    private PrintWriter  out;
    private Socket socket;
    public ChatServerClientHandler(Socket given_socket, Scanner given_in, PrintWriter given_out) {
         this.in=given_in;
         this.socket=given_socket;
         this.out=given_out;
         
    }
  
    public void run() {
     
         String line;
         boolean stopthread=false;
         
                      
              while (!stopthread){
             
                   line=in.nextLine();
                   out.println(line);
                   out.flush();
                   
                   if (line.equals("quit")) stopthread=true;

              } 

              System.out.println("Connection Closed by "+ socket);
    
               
    }
}
 ////////////////////
 
class chatserver {

    private int port;

    public chatserver(int given_port){//costruttore
        this.port=given_port;
    }

    public void startserver() {//startServer
         PrintWriter out1, out2;
         Scanner in1, in2;
         ServerSocket serversocket;
         ExecutorService executor = Executors.newCachedThreadPool();
         Socket socket1, socket2;
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
                   socket1=serversocket.accept(); 
                   System.out.println("Ricevuta connessione da "+socket1);
             
                   out1=new PrintWriter(socket1.getOutputStream());
                   in1=new Scanner(socket1.getInputStream());
       
                   //attendo la connessione del secondo
                   socket2=serversocket.accept();
                   System.out.println("Ricevuta connessione da "+socket2);

                   out2=new PrintWriter(socket2.getOutputStream());
                   in2=new Scanner(socket2.getInputStream()); 
       
       
                   //passo la richiesta alla chat
                   out1.println("Chat ready!");
                   out1.flush();
       
                   out2.println("Chat ready!");
                   out2.flush();
            
                   //grazie a due processi distinti posso mandare un messaggio senza attendere la risposta dell'altro
                   //li inizializzo con gli scanner e i printwriter
                   executor.submit(new ChatServerClientHandler(socket1, in1, out2)); 
                   executor.submit(new ChatServerClientHandler(socket2, in2, out1));
              } 

              catch (IOException e) {
                   break; 
              }
         }

       executor.shutdown();
       
    }
}

///////////////////////////////////////
//Main
public class Chat_Server {

    public static void main(String[] args) {

         chatserver session=new chatserver(4332);
         session.startserver(); //attivo il server
    }

}
