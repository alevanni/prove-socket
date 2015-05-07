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
  Thread send, receive;
public chatclient(InetAddress ip, int port) { //costruttore
  this.port=port;
  this.ip=ip;
}//costruttore
 

public void startClient() throws IOException {//metodo

 String avvio;
 Socket socket=new Socket(ip, port);
 //ci sono due thread, uno per ricevere e uno per mandare  
 //ExecutorService executor = Executors.newCachedThreadPool();
 
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
    //while (true) {
    //executor.submit(new invia(socketOut, socketIn));
    //s=stdin.nextLine();
    //socketOut.println(s);
    //socketOut.flush();
    //s=socketIn.nextLine();
    //System.out.println(s);
    //}
      




    
    //attivo il thread per ricevere
    //socketIn.close();
    //socketOut.close();
    
    this.send=new Thread(new invia(socket, socketOut)) ;
    this.receive=new Thread(new ricevi(socket, socketIn));
    send.start();
    receive.start();
     
   }
   catch (IOException e) {
     System.out.println("Error");
     socket.close();
     
   }

  //Attenzione, chiude i thread perche' si chiude subito!
 
}//metodo


}
//////////////////////////////////////////////////////////////////////////////
class invia implements Runnable  {
 private Socket socket;
 private PrintWriter socketOut;
 public invia(Socket socket, PrintWriter socketOut) {
  this.socket=socket;
  this.socketOut=socketOut;
}//costruttore
 public void run()  {
   
   //per prendere da tastiera
   Scanner stdin = new Scanner(System.in);
   String line;
   
   //Scanner socketIn;
   try {
     
       //per mandare al server
        //socketOut=new PrintWriter(this.socket.getOutputStream());
        //socketIn=new Scanner(this.socket.getInputStream());
        while (true)  {
      
          System.out.print("I say: ");
         
          //prendo una stringa da riga di comando
          line = stdin.nextLine();
         
          if (line.equals("quit")) {
              socket.close();
              break ;}
          else {
            socketOut.println(line);
            socketOut.flush();
          }//else
         }//while
         socket.close();
     }//try
      
    catch(IOException e ) {
        System.out.println("Connection Closed by invia");
     } // catch

    
    
}//run

}

//////////////////////////////////////////////////////////////////////////////
class ricevi implements Runnable {
 private Socket socket;
 private Scanner socketIn;
 public ricevi(Socket socket, Scanner socketIn)  {//costruttore
 this.socket=socket;
 this.socketIn=socketIn;

 }
 public void run()  {
 
 //PrintWriter socketOut;
 String socketline;
 
  try {
     //socketOut=new PrintWriter(this.socket.getOutputStream());
     //socketIn=new Scanner(this.socket.getInputStream());
     
      
     while (true) { 
       
        if (socketIn.hasNextLine())  {
            socketline=socketIn.nextLine();
         
            if (socketline.equals("quit")) { break; }
            else {
               System.out.println(socketline);
            }//else
         }
         //else Thread.sleep(2000);
      }//while
   
   

socket.close();
  }
 catch (IOException e){
  System.out.println("Connection Closed by ricevi");

  } 
   
 
 }//run
}//ricevi

////////////////////////////////////////////////////////////////////
public class provachatclient {
//metti qui il main
 public static void main(String[] args)  {
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

