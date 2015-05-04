import java.io.InputStream;
import java.util.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.net.*;
//prova la connessione sul localhost
class Lineclient {
  private int port; //numero della porta
  private InetAddress ip; //indirizzo ip

public Lineclient(InetAddress ip, int port) { //costruttore
  this.port=port;
  this.ip=ip;
}//costruttore
 

public void startClient() throws IOException {//metodo
 String socketline;
 String line;
 Socket socket=new Socket(ip, port);  

 System.out.println("Connection estabilished");  

 Scanner socketIn = new Scanner(socket.getInputStream()) ;
 PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
 Scanner stdin = new Scanner(System.in);
   
  try {
   //System.out.println("Come ti chiami?");
   //line = stdin.nextLine();
   //socketOut.println(line);
   //socketOut.flush();
   while (true)  {
    System.out.print("I say: ");
    //prendo una stringa da riga di comando
    line = stdin.nextLine();
    socketOut.println(line);
    socketOut.flush();
    socketline=socketIn.nextLine();
    System.out.println(socketline);
   }//while
  }//try


  catch(NoSuchElementException e ) {
   System.out.println("Connection Closed");
  } // catch
   //adesso chiudo tutto
   finally {
    stdin.close();
    socketIn.close();
    socketOut.close();
    socket.close();
  }//finally 
}//metodo


}

public class provaclient {
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
      Lineclient prova=new Lineclient(InetAddress.getByName(address), porta);

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

