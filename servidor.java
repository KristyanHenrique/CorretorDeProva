import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ServidorMulticast {

  public static void main(String[] args) throws java.io.IOException {
    while(true){
      //preparar gabarito
      String archive = "gabarito.txt";
      FileReader fr = new FileReader(archive);
      BufferedReader br = new BufferedReader(fr);
      //variaveis que armazenam a quantidade de respostas certas e erradas 
      int acertos = 0;
      int erros = 0;
      String resposta="";

      try {
        while(br.ready()){
          MulticastSocket mcs = new MulticastSocket(12347);//instanciamento do multicast socket
          InetAddress grp = InetAddress.getByName("239.0.0.1");//definição do ip
          mcs.joinGroup(grp);//entra no grupo da faixa ip
          byte rec[] = new byte[1000];//definição do byte array que vai receber os dados do pacote
          DatagramPacket pkg = new DatagramPacket(rec, rec.length);//definição do pacote
          mcs.receive(pkg);//recebendo o pacote
          String data = new String(pkg.getData());//extraindo dados
          System.out.println("Dados recebidos:" + data);//exibindo dados da mensagem
          String inetaddress = (pkg.getAddress()).getHostAddress();//capturando endereço ip e de host
          //correção
          
          String linhaGabarito = br.readLine();//linha de respostas do gabarito escaneado no servidor
          String linhaProva = data;//linha de respostas da prova do aluno 

          
          for (int i=0;i<linhaGabarito.length();i++ ) {
            if ((linhaGabarito.charAt(i)==linhaProva.charAt(i))&&(i>1)) {//verifica os caracteres 
              acertos++;
              resposta = resposta + "v";
            }else if (i<2){
              resposta = resposta+linhaGabarito.charAt(i);
            }else{
              System.out.print("error\n");
              resposta = resposta + "f";
              erros++;
            }if (i==5) {
              resposta = resposta + "\n";
            }
          }
        System.out.print(acertos+"\n");
        System.out.print(erros+"\n");
        System.out.print(resposta+"\n\n");
        //esta enviando apenas uma linha
        System.out.print("enviando: \n"+resposta+"\n");
        byte[] respEnv = resposta.getBytes();
        DatagramSocket ds = new DatagramSocket();
        DatagramPacket respPacket = new DatagramPacket(respEnv, respEnv.length, InetAddress.getByName("239.0.0.2"), Integer.parseInt("12347"));
        ds.send(respPacket);
        }
      }
      catch(Exception e) {
        System.out.println("Erro: " + e.getMessage());
      }
      br.close();
      fr.close();
    }
  }
}