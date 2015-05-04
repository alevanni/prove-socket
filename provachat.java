import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.net.*;


class ricevi implements Runnable {
private Socket socket1, socket2;
public String nome;
public ricevi (Socket socket1, Socket socket2, String a ) {
this.socket1=socket1;
this.socket2=socket2;
this.nome=a;
}//costruttore

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
             
            if (line.equals("quit")) { break ;} 
            else {
               //stampo da lato client
               out2.println(nome +" says: " +line);
               //stampo da lato server  
               System.out.println( nome+ " says: "+ line+ " on socket "+ socket1);
               out2.flush();
               }


          }//while 

         //ora chiudo tutto
         in1.close();
         out1.close();
         socket1.close();
         in2.close();
         out2.close();
         socket2.close();
      }//try
      catch (IOException e ){
        System.err.println(e.getMessage());
       }//catch  


}
}//class
 ////////////////////
 
class MultiEchoServer {
  private int port;

public MultiEchoServer(int port){//costruttore
  this.port=port;
 }//costruttore
public void startServer() {//startServer
  PrintWriter out1, out2;
  Scanner in1, in2;
  ServerSocket serversocket;
  ExecutorService executor = Executors.newCachedThreadPool();
  Socket socket1, socket2;
   try {
      serversocket=new ServerSocket(port);
      
 
       }//try
   catch (IOException e) {
        System.err.println(e.getMessage());
        return ;
       }//catch 

   System.out.println("Server ready");
   
   while (true) {//while
    try {
       //attendo la connessione del primo
       socket1=serversocket.accept(); 
       System.out.println("Ricevuta connessione da "+socket1);
       //costrisco il printwriter
       out1=new PrintWriter(socket1.getOutputStream());
       in1=new Scanner(socket1.getInputStream());
       
       //attendo la connessione del secondo
       socket2=serversocket.accept();
       //costrisco il printwriter
       out2=new PrintWriter(socket2.getOutputStream());
       in2=new Scanner(socket2.getInputStream()); 
       System.out.println("Ricevuta connessione da "+socket2);
       
       //passo la richiesta alla chat
       out1.println("Chat ready!");
       out1.flush();
       out2.println("Chat ready");
       out2.flush();
       executor.submit(new ricevi(socket1, socket2, "tizio")); 
       executor.submit(new ricevi(socket2, socket1, "caio"));
       }//try
     catch (IOException e) {
         break; //ci entro se serversocket viene chiuso
        }//catch
     }//while
       executor.shutdown();
       
       
   
}//startServer
}//multiechoserver



public class provachat {
public static void main(String[] args) {
  MultiEchoServer echoserver=new MultiEchoServer(4331);
  echoserver.startServer(); //attivo il server
}//main

}
