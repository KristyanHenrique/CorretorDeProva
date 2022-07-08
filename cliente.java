import java.net.*;
import java.io.*;

public class ClienteMulticast {

  public static void main(String[] args) {

    if(args.length != 2) {
      System.out.println("Digite <endereco multicast> <porta>");
      System.exit(0);
    }

    try {
      String archive = "prova.txt";
      FileReader fr = new FileReader(archive);
      BufferedReader br = new BufferedReader(fr);
      
      MulticastSocket mcs = new MulticastSocket(12347);
      InetAddress grp = InetAddress.getByName("239.0.0.2");
      mcs.joinGroup(grp);
      byte rec[] = new byte[10000];
      DatagramPacket recPackage = new DatagramPacket(rec, rec.length);

      while(br.ready()){ 
        String linha = br.readLine();
        //System.out.print(linha+"\n");
        
        //montagem do datagrama
        byte[] b = linha.getBytes();
        InetAddress addr = InetAddress.getByName(args[0]);
        DatagramSocket ds = new DatagramSocket();
        DatagramPacket pkg = new DatagramPacket(b, b.length, addr, Integer.parseInt(args[1]));
        ds.send(pkg);
        //fim montagem datagrama
        Thread.currentThread().sleep(1000);//Repouso para o servidor/evitar congestionamento
        mcs.receive(recPackage);
      }
      String data = new String(recPackage.getData());
      System.out.print(data+"\n");
      
      int acertos=0;
      int erros=0;
      
      char sentV = 'v';
      char sentF = 'f';
      //String chara = null;
      for (int i=0;i<data.length();i++) {
        char chara=data.charAt(i);
        if (chara==sentV) {
          acertos++;
        }else if (chara==sentF) {
          erros++;
        }
      }
      System.out.print("Acertos: "+acertos+"\n");
      System.out.print("Erros: "+erros+"\n");

      br.close(); 
      fr.close();
    }
    catch(Exception e) {
      System.out.println("Nao foi possivel enviar a mensagem");
    }
  }
}