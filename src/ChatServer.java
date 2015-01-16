// IMPORTING CLASSES
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
public class ChatServer {
// DECLARING DIFFERENT COMPONENTS
private JDialog aboutDialog;
private JFrame jf;
private JTextArea ChatHistory;
private JTextField NewMsg;
private JButton Send,Quit;
private JScrollPane pane;
private JPanel p;
static ServerSocket server;
static Socket conn;
DataInputStream dis;
DataOutputStream dos;
  
// INITIALIZING COMPONENTS
public ChatServer() throws IOException{
jf=new JFrame("CHAT ROOM");
NewMsg=new JTextField(50);
ChatHistory=new JTextArea(10,50);
Send=new JButton("SEND");
Quit=new JButton("QUIT");
p=new JPanel();
pane=new JScrollPane(ChatHistory,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
jf.setLayout(new BorderLayout());
jf.add(NewMsg,BorderLayout.SOUTH);
jf.add(pane,BorderLayout.WEST);
p.setLayout(new GridLayout(3,1));
p.add(Send);
p.add(Quit);
ChatHistory.setEnabled(false);
jf.add(p,BorderLayout.CENTER);
// APPLYING ACTION LISTENER TO FORM PERFORM ACTIONS WHEN BUTTON SEND IS PRESSED i.e. COPYING TEXT FROM TEXT FIELD TO TEXT AREA
Send.addActionListener(new ActionListener()
{
@Override
public void actionPerformed(ActionEvent e) {
if((e.getSource()==Send)&&(NewMsg.getText()!=""))
  {
   ChatHistory.setText(ChatHistory.getText()+'\n'+"ME:"+NewMsg.getText());
   try {
    DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
    dos.writeUTF(NewMsg.getText());
   } 
   catch (Exception e1)
   {
    try {
     Thread.sleep(3000);
     System.exit(0);
    } catch (InterruptedException e2) {
     // TODO Auto-generated catch block
     e2.printStackTrace();
    }
   }
   NewMsg.setText(""); 
  }
}
});
// APPLYING ACTION LISTENER TO FORM PERFORM ACTIONS WHEN BUTTON QUIT IS PRESSED i.e. CLOSING THE GUI INTERFACE
Quit.addActionListener(new ActionListener(){
@Override
public void actionPerformed(ActionEvent ae) {
System.exit(0);
}});
jf.pack();//COMPRESSING DIFFERENT COMPONENTS IN THE FRAME
//SET FRAME AS VISIBLE
jf.setVisible(true);
//APPLYING MENU BAR AND ITEMS FILE AND HELP HAVING FUNCTIONS OF QUIT AND ABOUT
JMenuBar mb=new JMenuBar();
// ADDING QUIT & FILE
JMenu file=new JMenu("File");
JMenuItem quit=new JMenuItem("Quit");
quit.addActionListener(new ActionListener(){
// QUIT FUNCTION ON PRESSING FILE->QUIT
@Override
public void actionPerformed(ActionEvent ae) {
System.exit(0);
}});
file.add(quit);
mb.add(file);
// ADDING ABOUT & HELP
JMenu help=new JMenu("Help");
JMenuItem about=new JMenuItem("About");
about.addActionListener(new AboutHandler());
help.add(about);
mb.add(help);
jf.setJMenuBar(mb);//SETTING THE MENU BAR

  server=new ServerSocket(2000,1,InetAddress.getByName("169.254.2.75") );
  /*
   FOR LOCAL HOST USE THIS PARAMETER INSTEAD 
   InetAddress.getLocalHost()
   FOR CONNECTED 
   InetAddress.getByName("169.254.2.75")   
  */
  ChatHistory.setText("Waiting for Client");
  conn =server.accept();
  ChatHistory.setText(ChatHistory.getText()+'\n'+"Client Found");
  while(true)
  {
  try
  { 
   DataInputStream dis=new DataInputStream(conn.getInputStream());
   String string=dis.readUTF();
   ChatHistory.setText(ChatHistory.getText()+'\n'+"Client:"+string);
  }
  catch (Exception e1)
  {
   ChatHistory.setText(ChatHistory.getText()+'\n'+"Message sending fail:Network Error");
   try {
    Thread.sleep(3000);
    System.exit(0);
   } catch (InterruptedException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
  }
  }
}
// LAUNCHING GUI FRAME AND SETTING POSITIONS AND SIZES
private class AboutHandler implements ActionListener{
@Override
public void actionPerformed(ActionEvent ae) {
aboutDialog=new AboutDialog(jf,"About",true);
aboutDialog.setVisible(true);
}
}
private class AboutDialog extends JDialog implements ActionListener{
public AboutDialog(Frame parent,String title,boolean modal){
super(parent,title,modal);
add(new JLabel("The ChatServer is a neat tool that allows you to talk other ChatServers via a chat server"),BorderLayout.NORTH);
JButton b=new JButton("OK");
add(b,BorderLayout.SOUTH);
b.addActionListener(this);
pack();
}
@Override
public void actionPerformed(ActionEvent e){
setVisible(false);
}
}

// CREATING OBJECT AND FUNCTION CALLING
public static void main(String[] args) throws IOException{
ChatServer c=new ChatServer();
}
}
