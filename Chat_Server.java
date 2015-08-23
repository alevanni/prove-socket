import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.net.*;


//classe che gestisce i client
class ChatServerClientHandler implements Runnable {

    private Socket socket1, socket2;
  
    public ChatServerClientHandler(Socket given_socket1, Socket given_socket2) {
         this.socket1=given_socket1;
         this.socket2=given_socket2;
    }
  
    public void run() {
     
         Scanner in1, in2;
         PrintWriter out1, out2;
         String line;
     
         try {
              //input e output
              in1=new Scanner(socket1.getInputStream());
              out1= new PrintWriter(socket1.getOutputStream());
              
              in2=new Scanner(socket2.getInputStream());
              out2= new PrintWriter(socket2.getOutputStream());
               
              while (true){
             
                   line=in1.nextLine();
             
                   if (line.equals("quit")) { 
                        //devo dire al client di chiudere a connessione in entrata
                        out1.println("quit");
                        out1.flush();
                        //stampo da lato client
                        out2.println("L'altro ha chiuso la connessione: per chiudere digitare 'quit'");
                        out2.flush();
                        out2.println("quit");
                        out2.flush();
                        break;
                   } 
                   else {
                        //stampo da lato client
                        out2.println("Received:"+line);
                        //stampo da lato server  
                        System.out.println( "bla bla says: "+ line+ " from socket "+ socket1);
                        out2.flush();
                   }

              } 

              //ora chiudo tutto
              in1.close();
              out1.close();
              socket1.close();

              in2.close();
              out2.close();
              socket2.close();
         }
      
         catch (IOException e ){
              System.err.println(e.getMessage());
         }  
        
         System.out.println("Connection Closed by "+ socket1);
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
                   executor.submit(new ChatServerClientHandler(socket1, socket2)); 
                   executor.submit(new ChatServerClientHandler(socket2, socket1));
              } 

              catch (IOException e) {
                   break; //ci entro se serversocket viene chiuso
              }
         }

       executor.shutdown();
       
    }
}



public class Chat_Server {

    public static void main(String[] args) {

         chatserver session=new chatserver(4332);
         session.startserver(); //attivo il server
    }

}
