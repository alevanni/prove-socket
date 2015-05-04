import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.net.*;

class MultiEchoServer {
  private int port;

public MultiEchoServer(int port){//costruttore
  this.port=port;
 }//costruttore
public void startServer() {//startServer

  ServerSocket serversocket;
  ExecutorService executor = Executors.newCachedThreadPool();
  Socket socket;
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
       //attendo la connessione
       socket=serversocket.accept(); 
       System.out.println(socket);
       //passo la richiesta al clienthandler
       executor.submit(new EchoServerClientHandler(socket)); 
       }//try
     catch (IOException e) {
         break; //ci entro se serversocket viene chiuso
        }//catch
     }//while
       executor.shutdown();


   
}//startServer
}//multiechoserver

//classe che gestisce i client
class EchoServerClientHandler extends Thread {
  private Socket socket;
  
  public EchoServerClientHandler(Socket socket) {
    this.socket=socket;
    
  }//costruttore
  
  public void run() {
     
     Scanner in;
     PrintWriter out;
     String line;
     
     try {
         //input e output
         in=new Scanner(socket.getInputStream());
         out= new PrintWriter(socket.getOutputStream());
         
         
        
         while (true){
            line=in.nextLine();
             
            if (line.equals("quit")) { break ;} 
            else {
               //stampo da lato client
               out.println("Received:"+line);
               //stampo da lato server  
               System.out.println( "Client says: "+ line+ " on socket "+ socket);
               out.flush();
               }


          }//while 

         //ora chiudo tutto
         in.close();
         out.close();
         socket.close();
      }//try
      catch (IOException e ){
        System.err.println(e.getMessage());
       }//catch  

   }//run


}//echoserverclienthandler


public class provamultiechoserver {
  public static void main(String[] args){//main
    MultiEchoServer echoserver=new MultiEchoServer(4331);
    echoserver.startServer(); //attivo il server

   }//main


}//provamultiechoserver
