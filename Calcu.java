public class Calcu
{
    private double amount;
    private int number;
    Calcu() { }
    Calcu(double a, int n)
    {
        amount = a;
        number = n;
    }
    public void setAmount(double a)
    {
        if(a>=0)
            amount = a;
    }
    public void setNumber(int n)
    {
        if(n>1)
            number = n;
    }
    public double[] output()
    {
        //exception
        if(number == 1)
        {
            return new double[]{amount};
        }
        //else, number != 1
        //why "-number" because I want every division at last greater than 0
        int tot = (int)(amount * 100)-number;
        int[] division = new int[number-1];
        for(int i=0; i<number-1; i++)
        {
            //Math.random output range between [0,1)
            //eg. tot = 5; in order to get division=5, random need multi (tot+1)
            division[i] = (int)(Math.random()*(tot+1));
        }
        java.util.Arrays.sort(division);

        double[] out = new double[number];
        out[0] = division[0]/100.0 + 0.01;
        for(int i=1; i<number-1; i++)
            out[i] = (division[i] - division[i-1])/100.0 + 0.01;
        out[number-1] = (tot - division[number-2])/100.0 + 0.01;
        return out;
    }
    public void print2Console()
    {
        double[] temp = output();
        for(int i=0; i<temp.length; i++)
        {
            System.out.printf("%.2f ", temp[i]);
        }
        System.out.println();
    }
}
