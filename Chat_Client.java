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

//classe che lancia due thread, uno per mandare messaggi e uno per riceverli
class chatclient  {
    private int port; 
    private InetAddress ip; 
    Thread send, receive; 
    boolean stopthread=false; 

    public chatclient(InetAddress ip, int port) {
        this.port=port;
        this.ip=ip;
    }//costruttore
 

    public void startClient() throws IOException, InterruptedException {

        String launch;
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

   private PrintWriter socketOut;
   

   public Send(PrintWriter given_socketOut) {
       
       this.socketOut=given_socketOut;
    }


   public void run()  {

       boolean stopthread=false;
       Scanner stdin = new Scanner(System.in); 
       String line;
   
       while (!stopthread)  {
      
           System.out.print("I say: ");
           line = stdin.nextLine();
           socketOut.println(line);
           socketOut.flush();

           if (line.equals("quit")) {
              stopthread=true;
           }
             
       }
       
       System.out.println("You can't send messages anymore");   
   }
}

//////////////////////////////////////////////////////////////////////////////
// Thread per ricevere messaggi


class Receive implements Runnable {

    private Scanner socketIn;
    

    public Receive(Scanner given_socketIn)  {
        
        this.socketIn=given_socketIn;

    }
 

    public void run()  {
     
        String socketline;
        boolean stopthread=false; 
     
          
         while (!stopthread) { 
       
            if (socketIn.hasNextLine())  {
                socketline=socketIn.nextLine();
         
                if (socketline.equals("quit"))  stopthread=true;         
                
                else    System.out.println("Received: "+ socketline)   ;             
            }
         
         }

         System.out.println("You can't receive messages anymore.");
         
    

         
         
    }
}

////////////////////////////////////////////////////////////////////
//Main

public class Chat_Client  {

    public static void main(String[] args) throws InterruptedException {
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
                
                Session.startClient(); 
             }//try

             catch (IOException e ){
                System.err.println(e.getMessage());
             }
          }

        catch (UnknownHostException e){
             System.out.println("Host sconosciuto");
        }

 
  
   }

}

