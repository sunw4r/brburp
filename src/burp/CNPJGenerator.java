package burp;

public class CNPJGenerator implements IIntruderPayloadGenerator, IIntruderPayloadGeneratorFactory
{
    private boolean punctuation;
    private int payloadIndex;

    public CNPJGenerator(boolean punctuation)
    {
        this.punctuation = punctuation;
    }

    @Override
    public String getGeneratorName()
    {
        if (punctuation)
        {
            return "[BRBurp] CNPJ - punctuation";
        }
        else
        {
            return "[BRBurp] CNPJ";
        }
    }

    @Override
    public IIntruderPayloadGenerator createNewInstance(IIntruderAttack attack)
    {
        return new CNPJGenerator(punctuation);
    }

    @Override
    public boolean hasMorePayloads()
    {
        return true;
    }

    @Override
    public byte[] getNextPayload(byte[] baseValue)
    {
        byte[] payload = cnpj(punctuation).getBytes();
        payloadIndex++;
        return payload;
    }

    @Override
    public void reset()
    {
        payloadIndex = 0;
    }

    // ==============================================
    // CNPJ Stuff
    // ==============================================
    private int randomiza(int n)
    {
        return (int) (Math.random() * n);
    }

    private int mod(int dividendo, int divisor)
    {
        return (int) Math.round(dividendo - (Math.floor(dividendo / divisor) * divisor));
    }

    public String cnpj(boolean comPontos) {
        int n = 9;
        int n1 = randomiza(n);
        int n2 = randomiza(n);
        int n3 = randomiza(n);
        int n4 = randomiza(n);
        int n5 = randomiza(n);
        int n6 = randomiza(n);
        int n7 = randomiza(n);
        int n8 = randomiza(n);
        int n9 = 0; //randomiza(n);
        int n10 = 0; //randomiza(n);
        int n11 = 0; //randomiza(n);
        int n12 = 1; //randomiza(n);
        int d1 = n12 * 2 + n11 * 3 + n10 * 4 + n9 * 5 + n8 * 6 + n7 * 7 + n6 * 8 + n5 * 9 + n4 * 2 + n3 * 3 + n2 * 4 + n1 * 5;

        d1 = 11 - (mod(d1, 11));

        if (d1 >= 10)
            d1 = 0;

        int d2 = d1 * 2 + n12 * 3 + n11 * 4 + n10 * 5 + n9 * 6 + n8 * 7 + n7 * 8 + n6 * 9 + n5 * 2 + n4 * 3 + n3 * 4 + n2 * 5 + n1 * 6;

        d2 = 11 - (mod(d2, 11));

        if (d2 >= 10)
            d2 = 0;

        String retorno = null;

        if (comPontos)
            retorno = "" + n1 + n2 + "." + n3 + n4 + n5 + "." + n6 + n7 + n8 + "/" + n9 + n10 + n11 + n12 + "-" + d1 + d2;
        else
            retorno = "" + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + n10 + n11 + n12 + d1 + d2;

        return retorno;
    }
}
