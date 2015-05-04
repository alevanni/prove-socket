import java.io.InputStream;
import java.util.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;



//prova la connessione sul localhost
class chatclient  {
  private int port; //numero della porta
  private InetAddress ip; //indirizzo ip

public chatclient(InetAddress ip, int port) { //costruttore
  this.port=port;
  this.ip=ip;
}//costruttore
 

public void startClient() throws IOException{//metodo

 String avvio;
 Socket socket=new Socket(ip, port);
 //ci sono due thread, uno per ricevere e uno per mandare  
 ExecutorService executor = Executors.newCachedThreadPool();
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
    System.out.println(avvio);
    //attivo il thread per inviare
   
    executor.submit(new invia(socketOut, socketIn));
    //attivo il thread per ricevere
    //executor.submit(new ricevi(socketIn, socketOut));
    
   
   }
   catch (Exception e) {
     System.out.println("Error");
     socket.close();
     
   }

  executor.shutdown();
  socket.close();
}//metodo


}
//////////////////////////////////////////////////////////////////////////////
class invia extends Thread  {
 PrintWriter socketOut;
 Scanner socketIn;
 public invia(PrintWriter socketOut, Scanner socketIn) {
  this.socketOut=socketOut;
  this.socketIn=socketIn;
}//costruttore
 public void run(){
   
   //per prendere da tastiera
   Scanner stdin = new Scanner(System.in);
   String line;
   
   try {
     
       //per mandare al server
        
        
        while (true)  {
      
          System.out.print("I say: ");
         
          //prendo una stringa da riga di comando
          line = stdin.nextLine();
         
          if (line.equals("quit")) {break ;}
          else {
            this.socketOut.println(line);
            this.socketOut.flush();
          }//else
         }//while
         
     }//try
      
    catch(Exception e ) {
        System.out.println("Connection Closed by invia");
     } // catch

    this.socketOut.close();
  
}//run

}

//////////////////////////////////////////////////////////////////////////////
class ricevi extends Thread {
 Scanner socketIn;
 PrintWriter socketOut;
 public ricevi(Scanner socketIn, PrintWriter socketOut) {//costruttore
 this.socketIn=socketIn;
 this.socketOut=socketOut;

 }
 public void run() {
 
 
 String socketline;
 
  
     
     
      
     while (true) { 
       
         socketline=socketIn.nextLine();
         
         if (socketline.equals("quit")) { break; }
         else {
            System.out.println(socketline);
          }//else
       
         
      }//while
   
   

socketIn.close();
   
   
 
 }//run
}//ricevi

////////////////////////////////////////////////////////////////////
public class provachatclient {
//metti qui il main
 public static void main(String[] args) {
  String address;
  int porta;
  //immetto l'indirizzo e la porta del server
  Scanner standard = new Scanner(System.in);
  System.out.println("Address:");
  address=standard.nextLine();
  System.out.println("Port");
  porta=standard.nextInt();
  //ora creo il client
  try {
      chatclient prova=new chatclient(InetAddress.getByName(address), porta);

      try {
          prova.startClient(); 
       }//try
      catch (IOException e ){
          System.err.println(e.getMessage());
       }//catch
   }//try

  catch (UnknownHostException e){
      System.out.println("Host sconosciuto");
  }


  //attivo startclient  
  
 }//main

}

