import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import javax.swing.text.AttributeSet;
import java.awt.event.*;

public class Painter extends JFrame
{
    public static void main(String[] args)
    {
        new Painter();
    }

    int w   = 400;
    int h  = 530;
    int fontSize;
    PackagePanel pp;
    JLabel ajl;//amount JLabel
    AmountTextField atf;
    JLabel njl;//number JLabel
    NumberTextField ntf;
    JButton jb;//enter button
    RedPackageListener lis;//mouseListener

    public Painter()
    {
        fontSize = 25;
        pp = new PackagePanel(w,h);
        add(pp);

        ajl = new JLabel("塞入红包的金额", JLabel.CENTER);
        atf = new AmountTextField();
        atf.setHorizontalAlignment(SwingConstants.CENTER);
        njl = new JLabel("红包的个数", JLabel.CENTER);
        ntf = new NumberTextField();
        ntf.setHorizontalAlignment(SwingConstants.CENTER);
        jb = new JButton("塞入红包");
        atf.chainNTF(ntf);
        pp.setLayout(null);
        //set component's font and color
        ajl.setFont(new Font("幼圆",Font.PLAIN,fontSize));
        ajl.setForeground(Color.WHITE);
        njl.setFont(new Font("幼圆",Font.PLAIN,fontSize));
        njl.setForeground(Color.WHITE);
        jb.setFont( new Font("幼圆",Font.BOLD,fontSize*4/5));
        jb.setBackground(Color.ORANGE);
        //add these components to the PackagePanel pp
        pp.add(ajl); pp.add(atf); pp.add(njl); pp.add(ntf); pp.add(jb);
        //adjust components' positions
        ajl.setBounds(w/4,h/4,w/2,fontSize);
        atf.setBounds(w/4,h/4+fontSize*3/2,w/2,atf.fontSize);
        njl.setBounds(w/4,h/2,w/2,fontSize);
        ntf.setBounds(w/4,h/2+fontSize*3/2,w/2,ntf.fontSize);
        jb.setBounds(w/3,h*3/4,w/3,fontSize*5/4);
        //jb's keyListener
        lis = new RedPackageListener();
        jb.addMouseListener(lis);
        Calcu cal = new Calcu();
        lis.add_C_TF(cal, atf, ntf);

        //set frame's attributes
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(w, h);
        setVisible(true);

        //set this frame at center
        int ww = (Toolkit.getDefaultToolkit().getScreenSize().width - w)/2;
        int hh = (Toolkit.getDefaultToolkit().getScreenSize().height - h)/2;
        setLocation(ww, hh);
    }
}

class RedPackageListener implements MouseListener
{
    Calcu cal;
    JTextField atf;
    JTextField ntf;
    @Override
    public void mouseClicked(MouseEvent e)
    {
        cal.setAmount(Double.parseDouble(atf.getText()));
        cal.setNumber(Integer.parseInt(ntf.getText()));
        cal.print2Console();
    }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e){ }

    public void add_C_TF(Calcu c,JTextField a, JTextField n)
    {
        if(c!=null&&a!=null&&n!=null)
        {
            cal = c;
            atf = a;
            ntf = n;
        }
    }
}

class PackagePanel extends JPanel
{
    int w = 200;
    int h = 400;
    PackagePanel(int w, int h)
    {
        this.w = w;
        this.h = h;
    }
    protected void paintComponent(Graphics g)
    {
        g.setColor(new Color(209,61,75));
        g.fillRoundRect(0,0,w,h,w/20,h/20);
        g.setColor(Color.white);
        int fontSize = 50;
        g.setFont(new Font("幼圆",Font.PLAIN,fontSize));
        g.drawString("红包",w/2-fontSize,h/7);
    }
}

class AmountTextField extends JTextField
{
    AmountDocument AD = new AmountDocument("(0|([1-9]\\d{0,2}))\\.{0,1}\\d{0,2}");
    int fontSize = 40;
    public AmountTextField()
    {

        super("塞入红包的金额",7);
        setFont(new Font("幼圆",Font.PLAIN,fontSize));
        setDocument(AD);
    }

    public void chainNTF(NumberTextField ntf)
    {
        AD.chainedNTF = ntf;
    }
}
class AmountDocument extends PlainDocument
{
    private String regex = null;
    NumberTextField chainedNTF;
    public AmountDocument()
    {
        super();
    }
    public AmountDocument(String regex)
    {
        this();
        this.regex = regex;
    }

    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException
    {
        if(regex!=null)
        {
            if(!new StringBuilder(getText(0,getLength())).insert(offs,str).toString().matches(regex))
            {
                return;
            }
        }
        super.insertString(offs,str,a);
        if(chainedNTF != null)
        {
            double temp = Double.parseDouble(getText(0, getLength()));
            chainedNTF.ND.setUpperLimit(temp);
            //System.out.println(temp);
        }
    }
}
class NumberTextField extends JTextField
{
    NumberDocument ND = new NumberDocument("[1-9]\\d{0,2}");
    int fontSize = 40;
    public NumberTextField()
    {

        super("红包个数",7);
        setFont(new Font("幼圆",Font.PLAIN,fontSize));
        setDocument(ND);
    }
}
class NumberDocument extends PlainDocument
{
    private int upperLimit = 0;
    private String regex = null;
    public NumberDocument()
    {
        super();
    }
    public NumberDocument(String regex)
    {
        this();
        this.regex = regex;
    }

    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException
    {
        if(regex!=null)
        {
            if(!new StringBuilder(getText(0,getLength())).insert(offs,str).toString().matches(regex))
            {
                return;
            }
        }
        if( upperLimit>0 && upperLimit>=Integer.parseInt(new StringBuilder(getText(0,getLength())).insert(offs,str).toString()) )
        {
            super.insertString(offs,str,a);
        }
    }

    public void setUpperLimit(int UL) throws BadLocationException {
        if(UL>0) upperLimit = UL;
        if(getText(0,getLength()).length() > 0)
        {
            double currentValue = Integer.parseInt(getText(0,getLength()));
            if(UL<currentValue)
            {
                remove(0, getLength());
                insertString(0,""+UL,null);
            }
        }


    }
    public void setUpperLimit(double UL) throws BadLocationException {
        setUpperLimit((int)(UL*100));
    }
}
